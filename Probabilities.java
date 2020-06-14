import java.util.Scanner;
import java.text.DecimalFormat;

/**
 * Calculates the probabilities of achieving each scoring category in the game of Yahtzee
 * given the roll number and current dice values
 * */
public class Probabilities {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        //Get roll number from user
        int roll = 0;
        while (roll < 1 || roll > 3) {
            System.out.println("Enter your roll number (1, 2, or 3)");
            roll = scan.nextInt();
        }

        //Only take dice values if player has already rolled at least once
        if (roll >= 2) {
            int val1, val2, val3, val4, val5;
            Dice dice;

            while (true) {
                //Take the value of the 5 dice as input from user
                System.out.println("Enter the current values of your dice");
                System.out.print("Die 1: ");
                val1 = scan.nextInt();
                System.out.print("Die 2: ");
                val2 = scan.nextInt();
                System.out.print("Die 3: ");
                val3 = scan.nextInt();
                System.out.print("Die 4: ");
                val4 = scan.nextInt();
                System.out.print("Die 5: ");
                val5 = scan.nextInt();

                //Ensure entered values are all between 1 and 6 inclusive, and initialize dice if so
                if (val1 > 0 && val1 < 7 && val2 > 0 && val2 < 7 && val3 > 0 && val3 < 7 && val4 > 0 && val4 < 7 &&
                val5 > 0 && val5 < 7)
                {
                    dice = new Dice(val1, val2, val3, val4, val5);
                    break;
                }

                System.out.println("Error: Please enter values between 1 and 6 for all five dice");
            }
            scan.close();

            int bestValue = dice.maxCount();    //The value showing up most on dice (tie goes to highest value)
            int count = dice.mostOfAKind();     //How many of said value there are

            int secondValue = dice.secondMost();   //The value showing up the second most (tie goes to highest value)
            int count2 = dice.secondMostOfAKind(); //Number of times this value appears

            //Counter variable for each of 3 possible Sm. Str. (1-2-3-4 ; 2-3-4-5 ; 3-4-5-6)
            int count1to4 = 0;
            int count2to5 = 0;
            int count3to6 = 0;


            //For each die value, get number of appearances in each Small Straight possibility
            for (int i = 1; i <= 6; i++) {
                //Only update count for each series if a die value shows up at least once
                if (dice.exists(i)) {
                    if (i <= 4) {    //If die value between 1-4, update count for 1-2-3-4 series
                        count1to4++;
                    }

                    if (i >= 2 && i <= 5) {  //If die value between 2-5, update count for 2-3-4-5 series
                        count2to5++;
                    }

                    if (i >= 3) { //If die value between 3-6, update count for 3-4-5-6 series
                        count3to6++;
                    }
                }
            }
            //Save series with most die values already present to get a Small Straight
            int bestSmall = customMax(count1to4, count2to5, count3to6);

            //Do the same but now for Large Straights
            int count1to5 = 0;
            int count2to6 = 0;

            //See comments for Small Straight process which is largely similar
            for (int i = 1; i <= 6; i++) {
                if (dice.count(i) > 0) {
                    if (i <= 5)
                        count1to5++;
                    if (i >= 2)
                        count2to6++;
                }
            }
            int bestLarge = Math.max(count1to5, count2to6);

            //Begin calculating and printing probabilities for each category
            System.out.println("---Probabilities---");
            System.out.println("TOP");

            //Calculate probability of breaking even (at least 3 or more of a certain die value) for each value 1-6
            for (int i = 1; i <= 6; i++) {
                System.out.println("Break even " + i + "'s: "
                        + truncate(probBreakEven(roll, dice.count(i)) * 100)
                        + " %");
            }

            System.out.println();
            System.out.println("BOTTOM");

            System.out.println("3 of a Kind: " + truncate(probThreeOfAKind(roll, count) * 100)
                    + "% by keeping " + bestValue + "'s");

            System.out.println("4 of a Kind: " + truncate(probFourOfAKind(roll, count) * 100)
                    + "% by keeping " + bestValue + "'s");

            //Different scenarios affecting which Full House description prints
            if (count == 1) {
                System.out.println("Full House: " + truncate(probFullHouse(roll, count, count2) * 100)
                        + "% by rolling all dice over again");
            }
            else if (count == 3 && count2 == 2) {
                System.out.println("Full House: " + truncate(probFullHouse(roll, count, count2) * 100)
                        + "% by keeping " + bestValue + "'s and " + secondValue + "'s");
            }
            else if (count == 2 && count2 == 2) {
                System.out.println("Full House: " + truncate(probFullHouse(roll, count, count2) * 100)
                        + "% by keeping " + bestValue + "'s and " + secondValue
                        + "'s and trying to get a third of either of these");
            }
            else if (count == 2) {
                System.out.println("Full House: " + truncate(probFullHouse(roll, count, count2) * 100)
                        + "% by keeping " + bestValue + "'s");
            }
            else if (count != 5) {
                System.out.println("Full House: " + truncate(probFullHouse(roll, count, count2) * 100)
                        + "% by keeping " + bestValue + "'s and trying to get a pair of " + secondValue + "'s");
            }
            else {
                System.out.println("Full House: " + truncate(probFullHouse(roll, count, count2) * 100)
                        + "% by taking two of your " + bestValue + "'s and breaking your Yahtzee (not recommended)");
            }


            //Different Small Straight scenarios (1-2-3-4, 2-3-4-5, or 3-4-5-6)
            if (bestSmall == count2to5) {
                System.out.println("Small Straight: "
                        + truncate(probSmallStraight(roll, count1to4, count2to5, count3to6) * 100) + "% by going" +
                        " for 2-3-4-5 straight");
            }
            else if (bestSmall == count1to4) {
                System.out.println("Small Straight: "
                        + truncate(probSmallStraight(roll, count1to4, count2to5, count3to6) * 100) + "% by going" +
                        " for 1-2-3-4 straight");
            }
            else {
                System.out.println("Small Straight: "
                        + truncate(probSmallStraight(roll, count1to4, count2to5, count3to6) * 100) + "% by going" +
                        " for 3-4-5-6 straight");
            }

            //2 scenarios / strategies for playing a large straight
            if (bestLgStraight(count1to5, count2to6).equals("count1")) {
                System.out.println("Large Straight: "
                        + truncate(probLargeStraight(dice, roll, bestLarge) * 100) + "% by going for " +
                        "1-2-3-4-5 straight");
            }
            else {
                System.out.println("Large Straight: "
                        + truncate(probLargeStraight(dice, roll, bestLarge) * 100) + "% by going for " +
                        "2-3-4-5-6 straight");
            }

            System.out.println("Yahtzee: " + truncate(probYahtzee(roll, count) * 100)
                    + " % by keeping " + bestValue + "'s");
        }
        //Otherwise player has not rolled yet, give default probabilities / messages
        else {
            scan.close();

            int count = 5;      //Placeholder values that do not affect outcome as player has not yet rolled
            int count2 = 5;

            System.out.println("---Probabilities---");
            System.out.println("TOP");

            System.out.println("Break Even: "
                    + truncate(probBreakEven(roll, count) * 100) + "% average for each value on any given " +
                    "set of 3 rolls\n");

            System.out.println("BOTTOM");

            System.out.println("3 of a Kind: " + truncate(probThreeOfAKind(roll, count) * 100)
                    + "% average on any given set of 3 rolls");

            System.out.println("4 of a Kind: " + truncate(probFourOfAKind(roll, count) * 100)
                    + "% average on any given set of 3 rolls");

            System.out.println("Full House: " + truncate(probFullHouse(roll, count, count2) * 100)
                    + "% average on any given set of 3 rolls");

            System.out.println("Small Straight: " + truncate(probSmallStraight(roll, 0, 0, 0)
                    * 100) + "% average on any given set of 3 rolls");

            Dice dice = new Dice();
            System.out.println("Large Straight: " + truncate(probLargeStraight(dice , roll, 0)
                    * 100) + "% average on any given set of 3 rolls");

            System.out.println("Yahtzee: " + truncate(probYahtzee(roll, count) * 100)
                    + "% average on any given set of 3 rolls");
        }
    }
    /**
    * Probability of breaking even (3 of a certain die value) for the top half of the scorecard
    * on one specific given die value (NOT probability of getting any 3 of a kind). Takes roll and highest count
    * */
    public static double probBreakEven(int roll, int count) {
        double probability;

        //Probability that you'll end up with a specific 3oaK after 3 rolls (Source at bottom of file)
        if (roll == 1) {
            probability = 0.07540;  //probability of a generic 3oaK * (1/6)
        }
        //Case of having rolled once already
        else if (roll == 2) {
            if (count == 0) {
                /*
                * P(Break Even) = P(5 matching) + P(4 matching) + P(3 matching)
                *               + P(2 matching, 1+ next) + P(1 matching, 2+ next) + P(0 matching, 3+ next)
                * */
                probability = Math.pow(0.16667, 5)
                        + (nCr(5,4) * Math.pow(0.16667, 4.0) * Math.pow(0.83333, 1.0))
                        + (nCr(5,3) * Math.pow(0.16667, 3.0) * Math.pow(0.83333, 2.0))
                        + (nCr(5,2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 3.0)
                            * probBreakEven(roll + 1, count + 2))
                        + (nCr(5,1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 4.0)
                            * probBreakEven(roll + 1, count + 1))
                        + (Math.pow(0.83333, 5.0)
                            * probBreakEven(roll + 1, count));
            }
            else if (count == 1) {
                /*
                * P(Break Even) = P(4 matching this roll) + P(3 matching) + P(2 matching) + P(1 matching, 1+ next)
                *               + P(0 matching, 2+ next)
                * */
                probability = Math.pow(0.16667, 4.0)
                        + (nCr(4,3) * Math.pow(0.16667, 3.0) * Math.pow(0.83333, 1.0))
                        + (nCr(4,2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 2.0))
                        + (nCr(4,1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 3.0)
                            * probBreakEven(roll + 1, count + 1))
                        + (Math.pow(0.83333, 4.0)
                            * probBreakEven(roll + 1, count));
            }
            else if (count == 2) {
               //P(BE) = P(3 matching this roll) + P(2 matching) + P(1 matching) + P(0 matching, 1+ next roll)
                probability = Math.pow(0.16667, 3.0)
                        + (nCr(3,2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 1.0))
                        + (nCr(3,1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 2.0))
                        + (Math.pow(0.83333, 3.0)
                            * probBreakEven(roll + 1, count));
            }
            //Otherwise we have a 3oaK or better for this value
            else {
                probability = 1.0;
            }
        }
        //Case of last turn
        else {
            if (count == 0) {
                //P(BE) = P(5 matching) + P(4 matching) + P(3 matching)
                probability = Math.pow(0.16667, 5.0)
                        + (nCr(5, 4) * Math.pow(0.16667, 4.0) * Math.pow(0.8333, 1.0))
                        + (nCr(5, 3) * Math.pow(0.16667, 3.0) * Math.pow(0.8333, 2.0));
            }
            else if (count == 1) {
                //P(BE) = P(4 matching) + P(3 matching) + P(2 matching)
                probability =  Math.pow(.16667, 4.0)
                        + (nCr(4, 3) * Math.pow(0.16667, 3.0) * Math.pow(0.83333, 1.0))
                        + (nCr(4, 2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 2.0));
            }
            else if (count == 2) {
                //P(BE) = P(3 matching) + P(2 matching) + P(1 matching)
                probability = Math.pow(0.16667, 3.0)
                        + (nCr(3, 2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 1.0))
                        + (nCr(3, 1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 2.0));
            }
            else {
                probability = 1.0;
            }
        }
        return probability;
    }

    /**
    * Probability of obtaining a Three of a Kind outcome. Takes roll number and highest count
    * */
    public static double probThreeOfAKind(int roll, int count) {
        double probability;

        //Case where you haven't yet rolled (source at bottom)
        if (roll == 1)
            probability = 0.4524;
        //Otherwise same chance as breaking even (3+ of a kind) with the most frequently appearing die value
        else
            probability = probBreakEven(roll, count);

        return probability;
    }

    /**
     * Probability of obtaining a Four of a Kind outcome. Takes roll number and highest count
     * */
    public static double probFourOfAKind(int roll, int count) {
        double probability;

        //Probability that you'll end up with a 4oaK after 3 rolls (Source at bottom)
        if (roll == 1)
            probability = 0.24476;

        //Case of having rolled once already
        else if (roll == 2) {
            //Start at 1 as we'll have at least 1 of a certain value
            if (count == 1) {
                /*
                * P(4oaK) = P(4 matching) + P(3 matching) + P(2 matching and 1+ next roll)
                *         + P(1 matching and 2+ next roll) + P(0 matching and 3+ next roll)
                * */
                probability = Math.pow(0.16667, 4)
                        + (nCr(4, 3) * Math.pow(0.16667, 3.0) * Math.pow(0.83333, 1.0))
                        + (nCr(4, 2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 2.0)
                            * probFourOfAKind(roll + 1, count + 2))
                        + (nCr(4, 1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 3.0)
                            * probFourOfAKind(roll + 1, count + 1))
                        + (Math.pow(0.83333, 4.0)
                            * probFourOfAKind(roll + 1, count));
            }
            else if (count == 2) {
                //P(4oaK) = P(3 matching) + P(2 matching) + P(1 match now 1+ next roll) + P(0 matching and 2+ next roll)
                probability = Math.pow(0.16667, 3.0)
                        + (nCr(3, 2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 1.0))
                        + (nCr(3, 1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 2.0)
                            * probFourOfAKind(roll + 1, count + 1))
                        + (Math.pow(0.83333, 3.0)
                            * probFourOfAKind(roll + 1, count));
            }
            else if (count == 3) {
                //P(4oaK) = P(2 matching) + P(1 match) + P(0 matching and 1+ next roll)
                probability = Math.pow(0.16667, 2.0)
                        + (nCr(2, 1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 2.0))
                        + (Math.pow(0.83333, 2.0)
                            * probFourOfAKind(roll + 1, count));
            }
            else {
                probability = 1.0;
            }
        }
        else {
            if (count == 1) {
                //P(4oaK) = P(4 matching) + P(3 matching)
                probability = Math.pow(0.16667,4.0)
                        + (nCr(4, 3) * Math.pow(0.16667, 3.0) * Math.pow(0.83333, 1.0));
            }
            else if (count == 2) {
                //P(4oaK) = P(3 matching) + P(2 matching)
                probability = Math.pow(0.16667,3.0)
                        + (nCr(3, 2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 1.0));
            }
            else if (count == 3) {
                //P(4oaK) = P(2 matching) + P(1 matching)
                probability = Math.pow(0.16667, 2.0)
                        + (nCr(2, 1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 1.0));
            }
            else {
                probability = 1.0;
            }
        }
        return probability;
    }

    /**
     * Probability of obtaining a Full House outcome (excluding a Yahtzee bonus). Takes roll number
     * and two highest counts
     * */
    public static double probFullHouse(int roll, int count, int count2)
    {
        double probability;

        //If no rolls yet, prob of a FH is roughly 36.614%
        if (roll == 1) {
            probability = 0.36614;
        }
        else if (roll == 2)
        {
            //If count is 1, so is count2 (most and 2nd most both appear once each i.e. 5 distinct dice)
            if (count == 1) {

                    probability =
                            //P(rolling 5 distinct singles once again)
                            (nCr(6,1) * Math.pow(0.83333, 1.0) * Math.pow(0.66667, 1.0)
                            * Math.pow(0.5, 1.0) * Math.pow(0.33333, 1.0)
                            * probFullHouse(roll+1, count, count2))

                            //P(rolling a pair and 3 other distinct singles)
                        + (nCr(6,1) * nCr(5,2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 1.0)
                            * Math.pow(0.66667, 1.0) * Math.pow(0.5, 1.0)
                            * probFullHouse(roll + 1, count + 1, count2))

                            //P(rolling two distinct pairs and one distinct single)
                        + (nCr(6,1) * nCr(5,2) * Math.pow(0.16667,2.0) * nCr(5,1)
                            * nCr(3,2) * Math.pow(0.16667, 2.0) * Math.pow(0.66667, 1.0) / 2
                            * probFullHouse(roll + 1, count + 1, count2 + 1))

                            //P(rolling a full house in next roll (triple and a pair))
                        + (nCr(6,1) * nCr(5,3) * Math.pow(0.16667, 3.0) * nCr(5,1)
                            * Math.pow(0.16667,2.0))

                            //P(rolling a triple and two distinct singles)
                        + (nCr(6,1) * nCr(5,3) * Math.pow(0.16667, 3.0)
                            * Math.pow(0.83333, 1.0) * Math.pow(0.66667, 1.0)
                            * probFullHouse(roll + 1, count + 2, count2))

                            //P(rolling a quadruple (ie 2 matching pairs) and a distinct single)
                        + (nCr(6,1) * nCr(5,4) * Math.pow(0.16667, 4.0) * Math.pow(0.83333, 1.0)
                            * probFullHouse(roll + 1, count + 3, count2))

                            //P(rolling a Yahtzee this turn) : Would have to break it to go for FH next roll
                        + (nCr(6,1) * Math.pow(0.16667, 5.0)
                            * probFullHouse(roll + 1, count + 4, count2));
            }
            //If we've rolled one or more pairs
            else if (count == 2) {
                //Case of one pair and three distinct singles. Keep pair and roll the other 3 dice
                if (count2 == 1) {

                    probability =
                            //P(roll three distinct values again)
                            (Math.pow(0.83333, 1.0) * Math.pow(0.66667, 1.0) * Math.pow(0.5, 1.0)
                            * probFullHouse(roll + 1, count, count2))

                            //P(roll a new pair and a distinct single)
                        + (nCr(5,1) * nCr(3,2) * Math.pow(0.16667, 2.0) * Math.pow(0.66667, 1.0)
                            * probFullHouse(roll + 1, count, count2 + 1))

                            //P(roll a matching pair (i.e. 4oaK) and a distinct single)
                        + (nCr(3,2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333,1.0)
                            * probFullHouse(roll + 1, count + 2, count2))

                            //P(roll a matching single and two distinct singles)
                        + (nCr(3,1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 1.0) * Math.pow(0.66667, 1.0)
                            * probFullHouse(roll + 1, count + 1, count2))

                            //P(roll one matching dice and one distinct pair (giving us a FH)
                        + (nCr(3,1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 1.0) * Math.pow(0.16667, 1.0))

                            //P(Yahtzee) (would have to be broken to get FH next turn)
                        + (Math.pow(0.16667, 3.0)
                            * probFullHouse(roll + 1, count + 3, count2 - 1));
                }
                //Case of two distinct pairs and only outcomes for last die are FH or no FH
                else {
                    //1 - P(not getting either of 2 desired values over 2 consecutive rolls)
                    probability = 1 - Math.pow(0.66667, 2.0);
                }
            }
            //Having triple and two distinct singles after one roll
            else if (count == 3) {
                //Along with two distinct singles, 1/6 chance of pairing one single w/ the other
                if (count2 == 1)
                {
                    probability =
                            //P(roll a distinct pair on this roll)
                        (nCr(5,1) * Math.pow(0.16667, 2.0))

                            //P(1 matches triple and 1 distinct)
                        + (nCr(2, 1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 1.0)
                            * probFullHouse(roll + 1, count + 1, count2))

                            //P(2 distinct singles again)
                        + (Math.pow(0.83333, 1.0) * Math.pow(0.66667, 1.0)
                            * probFullHouse(roll + 1, count, count2));
                }
                //Case of having FH on first roll (triple + a distinct pair)
                else {
                    probability = 1.0;
                }
            }
            //Case of 4oaK after one roll (play one die from the 4oaK)
            else if (count == 4) {

                probability =
                            //P(getting FH this roll)
                        (Math.pow(0.16667, 1.0))

                            //P(getting another distinct single)
                        + (Math.pow(0.66667, 1.0) * probFullHouse(roll + 1, count - 1, count2))

                            //P(rolling the same value)
                        + (Math.pow(0.16667, 1.0) * probFullHouse(roll + 1, count, count2));
            }
            /*
            * If at a Yahtzee after first roll i.e. count = 5. Keep 3 and roll other 2 dice
            * Note, count2 will also be 5 (see Dice.secondMost()), need to subtract when recursively calling
            * */
            else {
                probability =
                            //P(taking 2 of the dice and rolling a new distinct pair)
                        (nCr(5,1) * Math.pow(0.83333, 1.0) * Math.pow(0.16667, 1.0))

                            //P(rolling and getting two distinct singles)
                        + (Math.pow(0.83333, 1.0) * Math.pow(0.66667, 1.0)
                            * probFullHouse(roll + 1, count - 2, count2 - 4))

                            //P(one match and one distinct single)
                        + (nCr(2,1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 1.0)
                            * probFullHouse(roll + 1, count - 1, count2 - 4))

                            //P(getting the same pair (a Yahtzee again))
                        + (Math.pow(0.16667, 2.0) * probFullHouse(roll + 1, count, count2));
            }
        }
        //Otherwise we are on last roll
        else {
            if (count == 1) {
                //Roll everything again, probability of a triple + distinct pair
                probability = (nCr(6,1) * nCr(5,3) * Math.pow(0.16667, 3.0) * nCr(5,1)
                        * Math.pow(0.16667, 2.0));
            }
            else if (count == 2) {
                if (count2 == 1) {
                    probability = (nCr(3,1) * Math.pow(0.16667, 1.0)
                                * Math.pow(0.83333, 1.0) * Math.pow(0.16667, 1.0));
                }
                //Two distinct pairs and a single
                else
                    probability = 0.33333;
            }
            else if (count == 3) {
                //3 matching and 2 other distinct from each other
                if (count2 == 1)
                    probability = 0.16667;

                //Otherwise we have a triple and a pair, hence a FH
                else
                    probability = 1.00;
            }
            //Keep 3 of the 4 matching dice and try to pair the fourth with the single
            else if (count == 4)
                probability = 0.16667;

            else {
                //Take 2 dice from the Yahtzee and roll for a separate pair
                probability = 0.83333 * 0.16667;
            }
        }
        return probability;
    }

    /**
     * Probability of obtaining a Small Straight outcome. Takes roll number and counts of each small straight
     * combination
     * */
    public static double probSmallStraight(int roll, int count1, int count2, int count3)
    {

        double probability;

        String bestStraight = bestSmStraight(count1, count2, count3);
        /*
        * Save the best count (for the straight we are going for) as a separate variable
        * */
        int best;
        if (bestStraight.equals("count1"))
            best = count1;
        else if (bestStraight.equals("count2"))
            best = count2;
        else
            best = count3;

        if (roll == 1)
            probability = 0.6160;

        else if (roll == 2) {
            /*
            * 1-2-3-4 has most values. A 1 has to be part of our straight, otherwise count2 >= count1
            * and we go with count2 instead
            * */
            if (bestStraight.equals("count1")) {
                //We only have 1's showing (otherwise count2 or count3 would be >= count1 = 1)
                if (count1 == 1) {
                    probability =
                            //P(3 values you need to complete the 1-2-3-4 straight)
                        (nCr(4, 3) * Math.pow(0.16667, 3.0))

                            //P(2 values needed for the straight and 2 values that don't help the straight
                        + (nCr(4,2) * Math.pow(0.5,1.0) * Math.pow(0.33333, 1.0) * Math.pow(0.83333, 2.0)
                            * probSmallStraight(roll + 1, count1 + 2, count2 + 2, count3 + 1))

                            //P(1 needed value and 3 non-contributing values)
                        + (nCr(4,1) * Math.pow(0.5, 1.0) * Math.pow(0.6667, 3.0)
                            * probSmallStraight(roll + 1, count1 + 1, count2, count3))

                            //P(no values contribute towards 1-2-3-4 straight)
                        + (Math.pow(0.5, 4.0)
                            * probSmallStraight(roll + 1, count1, count2, count3));
                }
                //Only 1's and 2's (otherwise count2 >= count1 = 2)
                else if (count1 == 2) {
                    probability =
                                //P(2 needed values which gives us a small straight)
                        (nCr(3,2) * Math.pow(0.16667, 2.0))

                                //P(1 needed value to get small straight)
                        + (nCr(3,1) * Math.pow(0.33333, 1.0) * Math.pow(0.83333, 2.0)
                            * probSmallStraight(roll + 1, count1 + 1, count2, count3))

                                //P(no added values to 1-2-3-4 small straight)
                        + (Math.pow(0.66667, 3.0)
                            * probSmallStraight(roll + 1, count1, count2, count3));
                }
                //Case where we have 1-2-3 or 1-2-4 (otherwise count2 >= count1 = 3
                else if (count1 == 3) {
                    probability =
                            //P(One value that was needed giving us a small straight)
                        ((1 - Math.pow(0.83333, 2.0)))

                            //P(no values adding to 1-2-3-4 straight)
                        + (Math.pow(0.83333, 2.0)
                            * probSmallStraight(roll + 1, count1, count2, count3));
                }
                else
                    probability = 1.0;
            }
            else if (bestStraight.equals("count2")) {
                if (count2 == 1) {
                    probability =
                            //P(Other 3 values for a 2-3-4-5 straight)
                        (nCr(4,3) * Math.pow(0.16667, 3.0))

                            //P(2 needed values and two not needed)
                        + (nCr(4,2) * Math.pow(0.5, 1.0) * Math.pow(0.33333, 1.0) * Math.pow(0.83333, 2.0)
                            * probSmallStraight(roll + 1, count1 , count2 + 2, count3))

                            //P(One needed value for 2-3-4-5 straight)
                        + (nCr(4,1) * Math.pow(0.5, 1.0) * Math.pow(0.66667, 3.0)
                            * probSmallStraight(roll + 1, count1, count2 + 1, count3))

                            //P(Nothing adding to straight)
                        + (Math.pow(0.5, 4.0)
                            * probSmallStraight(roll + 1, count1, count2, count3));
                }
                else if (count2 == 2) {
                    probability =
                            //P(2 needed to complete 2-3-4-5 straight)
                        (nCr(3,2) * Math.pow(0.16667, 2.0))

                            //P(1 value needed to complete straight)
                        + (nCr(2,1) * Math.pow(0.33333, 1.0) * Math.pow(0.87777, 1.0)
                            * probSmallStraight(roll + 1, count1, count2 + 1, count3))

                            //P(nothing adding to a 2-3-4-5 straight)
                        + (Math.pow(0.66667, 3.0)
                            * probSmallStraight(roll + 1, count1, count2, count3));
                }
                else if (count2 == 3) {
                    probability =
                            //P(getting the 2-3-4-5 small straight with 2 remaining dice * 2 rolls)
                        ((1 - Math.pow(0.83333, 4.0)));
                }
                else
                    probability = 1.0;
            }
            //We are going for the 3-4-5-6 straight
            else {
                //Similar to 1-2-3-4 straight, we must have all 6's or 1's and 6's, otherwise count2 >= count3 = 2
               if (count3 == 1) {
                   probability =
                           //P(getting small straight of 2-3-4-5 this roll)
                       (nCr(4, 3) * Math.pow(0.16667, 3.0))

                           //P(2 values needed for the straight and 2 values that don't help the straight
                       + (nCr(4,2) * Math.pow(0.5, 1.0) * Math.pow(0.33333, 1.0) * Math.pow(0.8333, 2.0)
                           * probSmallStraight(roll + 1, count1, count2, count3 + 2))

                           //P(1 value needed for the straight and 3 that do not help)
                       + (nCr(4,1) * Math.pow(0.5, 1.0) * Math.pow(0.66667, 3.0)
                           * probSmallStraight(roll + 1, count1, count2, count3 + 1))

                           //P(no values that help with small straight)
                       + (Math.pow(0.5, 4.0)
                           * probSmallStraight(roll + 1, count1, count2, count3));
               }
               else if (count3 == 2) {
                   probability =
                                //P(2 values that complete 3-4-5-6 small straight)
                       (nCr(3,2) * Math.pow(0.16667, 2.0))

                               //P(1 value helps 3-4-5-6 straight)
                       + (nCr(3,1) * Math.pow(0.33333, 1.0) * Math.pow(0.5, 2.0)
                           * probSmallStraight(roll + 1, count1, count2, count3 + 1))

                               //P(no values help 3-4-5-6 straight)
                       + (Math.pow(0.66667, 4.0)
                           * probSmallStraight(roll + 1, count1, count2, count3));

               }
               else if (count3 == 3) {
                    probability =
                            //P(One value we need to complete small straight in next 2 rolls)
                        ((1 - Math.pow(0.83333, 4.0)));
               }
               else
                   probability = 1.0;
            }
        }
        //Last roll
        else {
            if (bestStraight.equals("count2")) {
                if (best == 1)
                    probability = nCr(4, 3) * Math.pow(0.16667, 3.0);
                else if (best == 2)
                    probability = nCr(3, 2) * Math.pow(0.16667, 2.0);
                else if (best == 3)
                    probability = 1 - Math.pow(0.66667, 2.0);
                else
                    probability = 1.0;
            }
            else {
                if (best == 1)
                    probability = nCr(4, 3) * Math.pow(0.16667, 3.0);
                else if (best == 2)
                    probability = nCr(3, 2) * Math.pow(0.16667, 2.0);
                else if (best == 3)
                    probability = 1 - Math.pow(0.83333, 2.0);
                else
                    probability = 1.0;
            }
        }
        return probability;
    }

    /**
     * Probability of obtaining a Large Straight outcome. Takes set of dice, roll number, and count of best
     * large straight combination
     * */
    public static double probLargeStraight(Dice dice, int roll, int count) {
        double probability;

        boolean exists2 = dice.exists(2);
        boolean exists3 = dice.exists(3);
        boolean exists4 = dice.exists(4);
        boolean exists5 = dice.exists(5);

        if (roll == 1)
            probability = 0.2653;

        else if (roll == 2) {
            if (count == 1) {
                probability =
                        //P(4 remaining dice for large straight)
                    (Math.pow(0.16667, 4.0)
                        * probLargeStraight(dice,roll +1, count + 4))
                        //P(3 desired values and 1 not)
                    + (nCr(4,3) * Math.pow(0.16667, 3.0) * Math.pow(0.83333, 1.0)
                        * probLargeStraight(dice,roll + 1, count + 3))
                        //P(2 desired and 2 not)
                    + (nCr(4,2) * Math.pow(0.16667, 2.0) * Math.pow(0.66667, 2.0)
                        * probLargeStraight(dice,roll + 1, count + 2))
                        //P(1 desired and 3 not)
                    + (nCr(4,1) * Math.pow(0.16667, 1.0) * Math.pow(0.5, 3.0)
                        * probLargeStraight(dice,roll + 1, count + 1))
                        //P(no desired values)
                    + (Math.pow(0.33333, 4.0)
                        * probLargeStraight(dice,roll + 1, count));
            }
            else if (count == 2) {
                probability =
                        //P(3 desired values for large straight)
                    (Math.pow(0.16667, 3.0)
                        * probLargeStraight(dice,roll + 1, count + 3))
                        //P(2 desired values and 1 not)
                    + (nCr(3,2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 1.0)
                        * probLargeStraight(dice,roll + 1, count + 2))
                        //P(1 desired value and 2 not)
                    + (nCr(3,1) * Math.pow(0.16667, 1.0) * Math.pow(0.66667, 2.0)
                        * probLargeStraight(dice,roll + 1, count + 1))
                        //P(no desired values)
                    + (Math.pow(0.5, 3.0)
                        * probLargeStraight(dice,roll + 1, count));
            }
            else if (count == 3) {
                probability =
                        //P(2 needed values)
                    (Math.pow(0.16667, 2.0)
                        * probLargeStraight(dice,roll + 1, count + 2))
                        //P(1 needed value and 1 not
                    +(nCr(2,1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 1.0))
                            * probLargeStraight(dice,roll + 1, count + 1)
                        //P(no desired values)
                    +(Math.pow(0.66667, 2.0)
                        * probLargeStraight(dice,roll + 1, count));
            }
            else if (count == 4) {
                if (exists2 && exists3 && exists4 && exists5) {
                    probability = Math.pow(0.33333, 1.0)
                            + (Math.pow(0.666667, 1.0) * probLargeStraight(dice, roll + 1, count));
                }
                else {
                    probability =
                            //P(getting last desired value)
                            (Math.pow(0.166667, 1.0)
                                * probLargeStraight(dice, roll + 1, count + 1))

                            //P(not getting last value)
                            + (Math.pow(0.83333, 1.0)
                                * probLargeStraight(dice, roll + 1, count));
                }
            }
            else
                probability = 1.0;
        }
        //Last roll
        else {
            if (exists2 && exists3 && exists4 && exists5)
                probability = 0.33333;
            else
                probability = Math.pow(0.16667, 5 - count);
        }

        return probability;
    }

    /**
     * Probability of obtaining a Yahtzee outcome. Takes roll number and highest count
     * */
    public static double probYahtzee(int roll, int count) {
        double probability;

        //Case of not having rolled yet
        if (roll == 1)
            probability = 0.04629;   //Probability of getting Yahtzee in all possible scenarios (Source at bottom)

        //Case of having rolled once already (second turn)
        else if (roll == 2) {
            if (count == 1) {
                //P(Yahtzee) = P(all 4 others) + P(1 then 3) + P(2 then 2) + P(3 then 1) + P(0 then 4)
                probability =
                        Math.pow(0.16667, 4.0)
                    + (nCr(4,3) * Math.pow(0.16667, 3.0) * Math.pow(0.83333, 1.0)
                        * probYahtzee(roll + 1, count + 3))
                    + (nCr(4,2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 2.0)
                        * probYahtzee(roll + 1, count + 2))
                    + (nCr(4,1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 3.0)
                        * probYahtzee(roll + 1, count + 1))
                    + (Math.pow(0.83333, 4.0) * probYahtzee(roll + 1, count));
            }

            else if (count == 2) {
                //P(Yahtzee) = P(all 3 others) + P(2 then 1) + P(1 then 2) + P(0 then 3)
                probability =
                        Math.pow(0.16667, 3.0)
                    + (nCr(3,2) * Math.pow(0.16667, 2.0) * Math.pow(0.83333, 1.0)
                        * probYahtzee(roll + 1, count + 2))
                    + (nCr(3,1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 2.0)
                        * probYahtzee(roll + 1, count + 1))
                    + (Math.pow(0.83333, 3.0) * probYahtzee(roll + 1, count));
            }

            else if (count == 3) {
                //P(Yahtzee) = P(all 2 others) + P(1 then 1) + P(0 then 2)
                probability =
                        Math.pow(0.16667, 2.0)
                    + (nCr(2,1) * Math.pow(0.16667, 1.0) * Math.pow(0.83333, 1.0)
                        * probYahtzee(roll + 1, count + 1))
                    + (Math.pow(0.83333, 2.0) * probYahtzee(roll + 1, count));
            }
            else if (count == 4)
                //P(Yahtzee) = 1 - chance of not getting the last value twice in a row
                probability = 1.0 - Math.pow(0.83333, 2.0);

            //Otherwise we have a Yahtzee, probability = 1.0
            else
                probability = 1.0;
        }
        //Otherwise we are on the last roll
        else
            probability = Math.pow(0.16667, 5 - count);

        return probability;
    }

    //----------------------------------------Helper Functions------------------------------------------

     /**
     * Binomial coefficient calculator in O(r) time and O(1) space
     * Sourced from: https://www.geeksforgeeks.org/space-and-time-efficient-binomial-coefficient/
     * */
    private static int nCr(int n, int r) {
        int res = 1;

        // Since C(n, r) = C(n, n-r)
        if ( r > n - r )
            r = n - r;

        // Calculate value of [n * (n-1) *---* (n-r+1)] / [r * (r-1) *----* 1]
        for (int i = 0; i < r; ++i) {
            res *= (n - i);
            res /= (i + 1);
        }
        return res;
    }

    /**
    * Used to determine which straight has the most values currently rolled and has best chance of becoming a
    * Small Straight
    * */
    private static String bestSmStraight(int count1, int count2, int count3) {
        int maxCount = customMax(count1, count2, count3);
        //If count2 highest or tied for highest, return that
        if (maxCount == count2)
            return "count2";
        //The count3 if it is highest or tied with count1
        else if (maxCount == count3)
            return "count3";
        //Otherwise count1 is the highest and return that
        else
            return "count1";
    }
    /**
     * Used to determine which straight has the most values currently rolled and has best chance of becoming a
     * Large Straight
     * */
    private static String bestLgStraight(int count1, int count2) {
        int maxCount = Math.max(count1, count2);
        //If count2 highest or tied for highest, return that
        if (maxCount == count2)
            return "count2";
        else
            return "count1";
    }

    /**
    * Returns max of a set of input doubles.
    * */
    private static int customMax(int ... integers) {
        int max = Integer.MIN_VALUE;

        for (int i : integers)
        {
            if (i >= max)
                max = i;
        }
        return max;
    }

    //-----------------------------Formatting Methods------------------------------------------------------//
    /**
    * Takes our probabilities as decimals and turns them into percentages as Strings(capped at 3 decimal places)
    * */
    private static String truncate(double d) {
        if (d == 100.0)
            return "100.00";
        else {
            DecimalFormat df = new DecimalFormat("##.###");
            return df.format(d);
        }
    }

/*
* Sources:
*
* Probability of a 3 or 4 of a Kind over 3 rolls: http://datagenetics.com/blog/january42012/index.html
* Probability of a Full House over 3 rolls: https://www.quora.com/What-is-the-probability-of-scoring-a-full-house-when-playing-Yahtzee
* Probability of a Sm or Lg Straight over 3 rolls: https://en.wikipedia.org/wiki/Yahtzee#Small_Straight
* Probability of a Yahtzee over 3 rolls: http://pi.math.cornell.edu/~mec/2006-2007/Probability/Yahtzeesol.htm
* */
}
