package uk.org.anthonyhull.radiotowers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RadioTowerCalculator {

	private abstract class Tower {
		public final int id;
		public final int x;
		public final int y;

		public Tower(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}

	private class DistanceFromTower {
		final int towerId;
		final int distance;

		public DistanceFromTower(int towerId, int distance) {
			this.towerId = towerId;
			this.distance = distance;
		}

		@Override
		public String toString() {
			return "DistanceFromTower [towerId=" + towerId + ", distance=" + distance + "]";
		}
	}

	private class ReceivingTower extends Tower {

		// Map of transmitter id to distance of this receiver from the
		// transmitter
		public final List<DistanceFromTower> distances;

		public ReceivingTower(int id, int x, int y) {
			super(id, x, y);
			distances = new ArrayList<>();
		}

		@Override
		public String toString() {
			return "ReceivingTower [id=" + id + ", x=" + x + ", y=" + y + "]";
		}
	}

	private class TransmittingTower extends Tower {
		@Override
		public String toString() {
			return "TransmittingTower [id=" + id + ", x=" + x + ", y=" + y + ", power=" + power + "]";
		}

		public final int initialPower;
		public int power;

		public TransmittingTower(int id, int x, int y, int power) {
			super(id, x, y);
			this.initialPower = power;
			this.power = power;
		}
	}

	public class Solution {
		public final int totalReceivers;
		public final int receiversWithInitialSignal;
		public final Map<Integer, Integer> powerIncreases;

		public Solution(int totalReceivers, int receiversWithInitialSignal, Map<Integer, Integer> powerIncreases) {
			this.totalReceivers = totalReceivers;
			this.receiversWithInitialSignal = receiversWithInitialSignal;
			this.powerIncreases = powerIncreases;
		}
	}

	// Dimensions of the island (x, y)
	private int[] dimensions;

	// Transmitting towers, as read from input file
	private List<TransmittingTower> transmittingTowers = new ArrayList<>();

	// Receiving towers: initially as read from file, but receivers are removed
	// from
	// this list as they are able to receive a signal.
	private List<ReceivingTower> receivingTowers = new ArrayList<>();

	// Total number of receivers
	private int totalReceivers;

	// Number of receivers able to receive a signal when all transmitters are at
	// their initial power.
	private int initialReceiversInRange;

	// --------------------------------------------------------------------------

	public void initialise(final Reader reader) throws IOException {
		final BufferedReader br = new BufferedReader(reader);

		// Read dimensions of island
		dimensions = readAndSplit(br);
		if (dimensions == null || dimensions.length != 2) {
			throw new IllegalArgumentException("Invalid dimensions for island: must be 2 integers");
		}

		// Read transmitting towers
		int last_id = 0;
		int[] tower = readAndSplit(br);
		if (tower == null) {
			throw new IllegalArgumentException("No transmitting towers");
		}
		if (tower[0] != 1) {
			throw new IllegalArgumentException("First transmitting tower must have id of 1");
		}
		while (tower != null && tower[0] == last_id + 1) {
			if (tower.length != 4) {
				throw new IllegalArgumentException("Transmitting tower must have 4 parameters");
			}
			transmittingTowers.add(new TransmittingTower(tower[0], tower[1], tower[2], tower[3]));
			last_id++;
			tower = readAndSplit(br);
		}

		// id is no longer incrementing - assume we are reading receiving towers
		last_id = 0;
		if (tower == null) {
			throw new IllegalArgumentException("No receiving towers");
		}
		if (tower[0] != 1) {
			throw new IllegalArgumentException("First receiving tower must have id of 1");
		}
		while (tower != null) {
			if (tower[0] == last_id + 1)
				if (tower.length != 3) {
					throw new IllegalArgumentException("Receiving tower must have 3 parameters");
				}
			receivingTowers.add(new ReceivingTower(tower[0], tower[1], tower[2]));
			last_id++;
			tower = readAndSplit(br);
		}

		totalReceivers = receivingTowers.size();
		System.out.println(String.format("Initialisation complete: %d transmitting towers, %d receiving towers",
				transmittingTowers.size(), totalReceivers));
	}

	/**
	 * Read a line from the input
	 * 
	 * @param br
	 *            Input source
	 * @return the line parsed into an array of integers
	 * @throws IOException
	 */
	private int[] readAndSplit(final BufferedReader br) throws IOException {
		final String line = br.readLine();
		if (line == null) {
			return null;
		} else {
			final String[] splitLine = line.trim().split(" ");
			final int[] result = new int[splitLine.length];
			int index = 0;
			for (String s : splitLine) {
				result[index++] = Integer.parseInt(s);
			}
			return result;
		}
	}

	/**
	 * Calculate solution
	 */
	public Solution calculate() {
		// For each receiver, calculate its distance from each transmitting
		// tower.
		// If a receiver is in range of a transmitter, remove it from the list,
		// as we do not need to consider it any more.
		initialReceiversInRange = 0;

		final Iterator<ReceivingTower> receiverIterator = receivingTowers.iterator();
		while (receiverIterator.hasNext()) {
			final ReceivingTower receiver = receiverIterator.next();
			boolean inRange = false;

			// For each tower, add its distance to the list, unless already in
			// range.
			for (final TransmittingTower transmitter : transmittingTowers) {
				final int distance = calcDistance(transmitter, receiver);
				if (distance <= transmitter.power) {
					inRange = true;
					initialReceiversInRange++;
					break;
				} else {
					receiver.distances.add(new DistanceFromTower(transmitter.id, distance));
				}
			}

			// If the receiver is in range of a tower, remove from list
			if (inRange) {
				receiverIterator.remove();
			}
		}
		
		// Increase the power of transmitters until all receivers can receive
		while (receivingTowers.size() > 0) {
			increasePower();
		}
		
		// Find transmitters whose power has increased
		final Map<Integer, Integer> increases = new HashMap<>();
		for (final TransmittingTower transmitter : transmittingTowers) {
			if (transmitter.power > transmitter.initialPower) {
				increases.put(transmitter.id, transmitter.power);
			}
		}
		
		// Return complete solution
		return new Solution(totalReceivers, initialReceiversInRange, increases);
	}

	/**
	 * Increase the power of a transmitter and recalculate the state of the
	 * receivers.
	 * 
	 * The strategy is to:<br>
	 * - iterate over the receivers that are still out of range<br>
	 * - calculate by how much each transmitter would need to increase in power
	 * so that receiver can receive from it<br>
	 * - choose the smallest increase that will have an effect. If there is more
	 * than one transmitter that could be increased by the same amount, choose
	 * the increase that will affect the largest number of receivers.<br>
	 * - remove from the list the receivers that are now in range
	 */
	private void increasePower() {
		// transmitter -> receivers for the smallest increase so far calculated
		Map<Integer, List<ReceivingTower>> increases = new HashMap<>();
		Integer smallestIncrease = null;

		// Build up the above map
		for (final ReceivingTower receiver : receivingTowers) {
			for (final DistanceFromTower distanceFromTransmitter : receiver.distances) {
				final int increaseRequired = distanceFromTransmitter.distance
						- transmittingTowers.get(distanceFromTransmitter.towerId - 1).power;

				if (smallestIncrease != null && increaseRequired > smallestIncrease) {
					// We already have a smaller increase
					continue;
				}

				if (smallestIncrease == null || increaseRequired < smallestIncrease) {
					// This is the smallest increase required that we have found
					// so far: throw away information on larger increases
					increases = new HashMap<>();
					smallestIncrease = increaseRequired;
				}

				// Get or create an entry for this transmitter
				List<ReceivingTower> receiversAffected = increases.get(distanceFromTransmitter.towerId);
				if (receiversAffected == null) {
					receiversAffected = new ArrayList<>();
					increases.put(distanceFromTransmitter.towerId, receiversAffected);
				}

				// Add receiver id
				receiversAffected.add(receiver);
			}
		}
		
		// Find the tower that will have the greatest effect.
		int transmitterId = -1;
		int numReceivers = -1;
		
		for (int transId : increases.keySet()) {
			int receivers = increases.get(transId).size();
			
			if (receivers > numReceivers) {
				numReceivers = receivers;
				transmitterId = transId;
			}
		}
		
		// Apply the increase
		transmittingTowers.get(transmitterId - 1).power += smallestIncrease;
		
		// Remove receivers that are now in range
		receivingTowers.removeAll(increases.get(transmitterId));
	}

	/**
	 * Calculate the Chebyshev distance between two towers.
	 * 
	 * This is the maximum of the x and y distances between the towers, because
	 * a move in the smaller dimension can be achieved by a diagonal move.
	 */
	private static int calcDistance(final Tower t1, final Tower t2) {
		return Math.max(Math.abs(t1.x - t2.x), Math.abs(t1.y - t2.y));
	}
}
