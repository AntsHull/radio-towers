package uk.org.anthonyhull.radiotowers;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.org.anthonyhull.radiotowers.RadioTowerCalculator.Solution;

public class RadioTowerMain {

	public static void main(String[] args) {

		try {
			final Reader fileReader = new FileReader("input.txt");
			final RadioTowerCalculator calculator = new RadioTowerCalculator();
			calculator.initialise(fileReader);
			Solution solution = calculator.calculate();
			
			System.out.println(String.format("%d/%d", solution.receiversWithInitialSignal, solution.totalReceivers));
			final List<Integer> transmitterIds = new ArrayList<>(solution.powerIncreases.keySet());
			Collections.sort(transmitterIds);
			for (Integer id : transmitterIds) {
				System.out.println(String.format("%d %d", id, solution.powerIncreases.get(id)));
			}
			
		} catch (Exception ex) {
			System.out.println("Program failed: " + ex.getMessage());
		}
	}

}
