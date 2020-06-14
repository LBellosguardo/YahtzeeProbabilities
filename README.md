# YahtzeeProbabilities

This program is a useful tool to help calculate the probability of achieving each scoring category in the game of Yahtzee. 
It takes the roll number and dice values (if applicable) as input from the user and returns probabilities as a percentage 
through the use of combinatorics to calculate possible outcomes and their likelihood.

## How Yahtzee works:
Yahtzee involves rolling 5 dice up to 3 times per turn, scoring points by achieving any of the following combinations:

*Upper Section:*
- Breaking Even: For all values 1 through 6, rolling 3 or more dice with this value

*Lower Section:*
- 3 of a Kind: 3 or more of a certain value
- 4 of a Kind: 4 or more of a certain value
- Full House: 3 of one value and 2 of another value (e.g. 3-3-3-5-5)
- Small Straight: 4 or more consecutive values (e.g. 1-2-3-4)
- Large Straight: 5 consecutive values 
- Yahtzee: All 5 of the same value

[Full Yahtzee rules can be found here](https://www.dicegamedepot.com/yahtzee-rules/)

## Demo:
Below is a demo of 2 scenarios:
- The first where you are on your second of three rolls and enter your current dice values
- You have not yet rolled and average probabilities for any given turn (set of 3 rolls) are dislpayed.

![](https://user-images.githubusercontent.com/46175185/84585303-6ac8e480-adc3-11ea-805e-a2be5ef48d3a.gif)

## Notes and Assumptions
- The program assumes the player plays rationally (i.e. keeps the appropriate dice in order to obtain the desired result)
- Some unused methods in the Dice.java class are present for potential future expansion into a complete Yahtzee game

## Usage:
Start the program by running Probabilities.java

## License:
[MIT](https://github.com/LBellosguardo/YahtzeeProbabilities/blob/master/LICENSE)
