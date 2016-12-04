# Radio Towers program #

This program is a solution to the Nominet Software Engineer Technical Test and aims to find the most efficient way to transmit a radio signal to a set of receiving towers distributed across a rectangular island.

## Structure ##

The program is available as an Eclipse project on GitHub:

    https://github.com/AntsHull/radio-towers

It consists of two classes:

- RadioTowerMain: the main program
- RadioTowerCalculator: which does most of the work

There is also a JAR file and an input file in the radio-towers subdirectory.


## Scope ##

In order to contain the scope of the program:

  - The program assumes an input format exactly like that of the example.
	  - It is not tolerant to formatting errors and assumes that tower ids begin at 1
  - It uses standard Java exceptions rather than creating its own.
  - Its only output is that specified in the problem statement. I have left in (but commented out) some print statements that I used for debugging purposes.
  - Private classes in RadioTowerCalculator have public data members rather than following the common Java convention of private data with get/set functions.
  - All the code, including tests, is in a single source folder.

## Strategy ##

The program has to calculate "the minimum increase in transmission power required for all of the receivers to be able to receive a signal".

To achieve this, the strategy is to increase the power of one transmitter at a time, choosing the transmitter that, at the current stage, requires the smallest increase in order to bring at least one receiver into range. If multiple transmitters could have their power increased by the same amount, the program chooses the transmitter that would affect most receivers.

The preceding step is repeated until all receivers are in range.

I considered two other ways of choosing which transmitter's power to increase and by how much:

- the increase that would affect the largest number of receivers.
- the increase with the highest value of (number of receivers) / (increase)

I was concerned that these strategies (especially the first) might lead in some configurations to one transmitter's power being increased greatly, potentially to cover the whole island, so I decided to stay with the strategy above of minimum increases, at the risk of increasing the number of iterations. 