package uk.org.anthonyhull.radiotowers;

import java.io.FileReader;
import java.io.Reader;

import uk.org.anthonyhull.radiotowers.RadioTowerCalculator.PowerIncrease;
import uk.org.anthonyhull.radiotowers.RadioTowerCalculator.Solution;

public class RadioTowerMain {

	public static void main(String[] args) {

		try {
			// Initialise calculator with input file
			final Reader fileReader = new FileReader("input.txt");
			final RadioTowerCalculator calculator = new RadioTowerCalculator();
			calculator.initialise(fileReader);
			
			// Calculate solution
			final Solution solution = calculator.calculate();
			
			// Output number of receivers initially in range of a signal / total receivers
			System.out.println(String.format("%d/%d", solution.receiversWithInitialSignal, solution.totalReceivers));
			
			// Loop over the transmitters whose power has increased
			for (final PowerIncrease increase : solution.powerIncreases) {
				System.out.println(String.format("%d %d", increase.transmitterId, increase.newPower));
			}
			
		} catch (Exception ex) {
			System.out.println("Program failed: " + ex.getMessage());
		}
	}

}
