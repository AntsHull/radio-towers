# Radio Towers program #

This program is a solution to the Nominet Software Engineer Technical Test and aims to find the most efficient way to transmit a radio signal to a set of receiving towers distributed across a rectangular island.

## Structure ##

The program is available as an Eclipse project on GitHub:

    https://github.com/AntsHull/radio-towers

It consists of two classes:

- RadioTowerMain: the main program
- RadioTowerCalculator: which does most of the work  


## Scope ##

In order to contain the scope of the program:

  - The program assumes an input format exactly like that of the example.
	  - It is not tolerant to formatting errors and assumes that tower ids begin at 1
  - It uses standard Java exceptions rather than creating its own.
  - Its only output is that specified in the problem statement. I have left in (but commented out) some print statements that I used for debugging purposes.
  - Private classes in RadioTowerCalculator have public data members rather than following the common Java convention of private data with get/set functions.
  - All the code, including tests, is in a single source folder.

## Algorithm ##
