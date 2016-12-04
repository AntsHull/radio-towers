package uk.org.anthonyhull.radiotowers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import uk.org.anthonyhull.radiotowers.RadioTowerCalculator.PowerIncrease;
import uk.org.anthonyhull.radiotowers.RadioTowerCalculator.Solution;

public class RadioTowerCalculatorTest {

	@Test
	public void testExample() throws IOException {
		// Run with the given example
		final String input = 
			"10 10\n" + 
			"1 2 5 1\n" +
			"2 0 6 3\n" +
			"3 1 2 2\n" +
			"4 3 5 3\n" +
			"1 0 1\n" +
			"2 8 8\n" +
			"3 6 5";
		
		final RadioTowerCalculator calc = new RadioTowerCalculator();
		calc.initialise(new StringReader(input));
		final Solution soln = calc.calculate();
		assertEquals(3, soln.totalReceivers);
		assertEquals(2, soln.receiversWithInitialSignal);

		final PowerIncrease inc1 = soln.powerIncreases.get(0);
		assertEquals(4, inc1.transmitterId);
		assertEquals(5, inc1.newPower);
	}

}
