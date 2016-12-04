package uk.org.anthonyhull.radiotowers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import uk.org.anthonyhull.radiotowers.RadioTowerCalculator.PowerIncrease;
import uk.org.anthonyhull.radiotowers.RadioTowerCalculator.Solution;

public class RadioTowerCalculatorTest {

	//---------------------------------------------------------------
	// Successful runs
	//---------------------------------------------------------------

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
		
		final Solution soln = runTest(input);
		assertEquals(3, soln.totalReceivers);
		assertEquals(2, soln.receiversWithInitialSignal);

		assertEquals(1, soln.powerIncreases.size());
		final PowerIncrease inc1 = soln.powerIncreases.get(0);
		assertEquals(4, inc1.transmitterId);
		assertEquals(5, inc1.newPower);
	}
	
	@Test
	public void testSmallestIncrease() throws IOException {
		// One transmitter requires a smaller increase in power
		final String input = 
				"10 10\n" + 
				"1 1 6 1\n" +
				"2 7 6 2\n" +
				"1 4 8\n" +
				"2 4 4";
		
		final Solution soln = runTest(input);
		assertEquals(2, soln.totalReceivers);
		assertEquals(0, soln.receiversWithInitialSignal);
		
		assertEquals(1, soln.powerIncreases.size());
		final PowerIncrease inc1 = soln.powerIncreases.get(0);
		assertEquals(2, inc1.transmitterId);
		assertEquals(3, inc1.newPower);
	}
	
	@Test
	public void testMoreReceivers() throws IOException {
		// Increasing the power of one transmitter brings more receivers into range
		final String input = 
				"6 6\n" + 
				"1 1 4 1\n" +
				"2 3 4 1\n" +
				"1 2 2\n" +
				"2 4 2";
		
		final Solution soln = runTest(input);
		assertEquals(2, soln.totalReceivers);
		assertEquals(0, soln.receiversWithInitialSignal);
		
		assertEquals(1, soln.powerIncreases.size());
		final PowerIncrease inc1 = soln.powerIncreases.get(0);
		assertEquals(2, inc1.transmitterId);
		assertEquals(2, inc1.newPower);
	}
	
	@Test
	public void testMultipleIncreases() throws IOException {
		// Multiple transmitters need to be increased in power
		final String input =
				"10 10\n" + 
				"1 1 4 1\n" +
				"2 3 4 1\n" +
				"3 6 3 1\n" +
				"1 2 2\n" +
				"2 4 2\n" +
				"3 9 0";
		
		final Solution soln = runTest(input);
		assertEquals(3, soln.totalReceivers);
		assertEquals(0, soln.receiversWithInitialSignal);
		
		assertEquals(2, soln.powerIncreases.size());
		final PowerIncrease inc1 = soln.powerIncreases.get(0);
		assertEquals(2, inc1.transmitterId);
		assertEquals(2, inc1.newPower);

		final PowerIncrease inc2 = soln.powerIncreases.get(1);
		assertEquals(3, inc2.transmitterId);
		assertEquals(3, inc2.newPower);
}

	//---------------------------------------------------------------
	// Data errors
	//---------------------------------------------------------------

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDimensions() throws IOException {
		final String input =
				"10 10 11\n" + 
				"1 1 4 1\n" +
				"2 3 4 1\n" +
				"1 2 2\n" +
				"2 4 2\n";
	
		runTest(input);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNoTowers() throws IOException {
		final String input =
				"10 10";
	
		runTest(input);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testTransmittingTowerIdNot1() throws IOException {
		final String input =
				"10 10\n" + 
				"2 1 4 1\n" +
				"3 3 4 1\n" +
				"1 2 2\n" +
				"2 4 2\n";
	
		runTest(input);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWrongNumberOfParametersForTransmitter() throws IOException {
		final String input =
				"10 10\n" + 
				"1 1 4\n" +
				"2 3 4 1\n" +
				"1 2 2\n" +
				"2 4 2\n";
	
		runTest(input);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testTransmitterCoordinatesWrong() throws IOException {
		final String input =
				"10 10\n" +
				"1 1 10 1\n" +
				"2 3 4 1\n" +
				"1 2 2\n" +
				"2 4 2\n";
	
		runTest(input);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNoReceivers() throws IOException {
		final String input =
				"10 10\n" +
				"1 1 4 1\n" +
				"2 3 4 1\n";
	
		runTest(input);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testFirstReceiverIdNot1() throws IOException {
		final String input =
				"10 10\n" +
				"1 1 4 1\n" +
				"2 3 4 1\n" +
				"2 2 2\n" +
				"3 4 2\n";
	
		runTest(input);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testReceiverCoordinatesWrong() throws IOException {
		final String input =
				"10 10\n" +
				"1 1 4 1\n" +
				"2 3 4 1\n" +
				"1 2 2\n" +
				"2 10 2\n";
	
		runTest(input);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testReceiverWrongNumberOfParameters() throws IOException {
		final String input =
				"10 10\n" +
				"1 1 4 1\n" +
				"2 3 4 1\n" +
				"1 2 2 6\n" +
				"2 4 2\n";
	
		runTest(input);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testReceiverIdOutOfSequence() throws IOException {
		final String input =
				"10 10\n" +
				"1 1 4 1\n" +
				"2 3 4 1\n" +
				"1 2 2\n" +
				"2 4 2\n" +
				"4 4 2\n";
	
		runTest(input);
	}

	//---------------------------------------------------------------

	private Solution runTest(final String input) throws IOException {
		final RadioTowerCalculator calc = new RadioTowerCalculator();
		calc.initialise(new StringReader(input));
		return calc.calculate();
	}

}
