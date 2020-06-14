/**
 * Represents a grouping of 5 dice that can be used in a Yahtzee game
 * */

public class Dice {
    private Die die1, die2, die3, die4, die5;

    /** Basic constructor initializing 5 new Die objects */
    public Dice() {
        this.die1 = new Die();
        this.die2 = new Die();
        this.die3 = new Die();
        this.die4 = new Die();
        this.die5 = new Die();
    }
    /** Custom constructor, useful to depict a specific state of the dice at a point in time */
    public Dice(int val1, int val2, int val3, int val4, int val5) {
        this.die1 = new Die(val1);
        this.die2 = new Die(val2);
        this.die3 = new Die(val3);
        this.die4 = new Die(val4);
        this.die5 = new Die(val5);
    }

    /** Get a specific die of the 5 */
    public Die getDie(int dieNum) {
        //Enforce parameter
       if (dieNum < 1 || dieNum > 5) {
           throw new IllegalArgumentException("Please enter a number between 1 and 5");
       }

        Die[] dice = diceToArray();
        return dice[dieNum - 1];
    }

    /** Return a Die as a String instead of a Die object */
    public String getDie(int dieNum, char pToPrint) {
        //Enforce parameter
        if (dieNum < 1 || dieNum > 5)
            throw new IllegalArgumentException("Please enter a number between 1 and 5");

        if (pToPrint != 'p')
            throw new IllegalArgumentException("Second argument can only be 'p' in order to print Die");

        Die[] dice = diceToArray();
        Die die = dice[dieNum - 1];
        String s = "";

        if (pToPrint == 'p') {
            s += "Die " + dieNum + ": " + die.getValue();
        }
        return s;
    }

    /** Method which outputs an array of the 5 dice for use */
    private Die[] diceToArray() {
        Die[] array = {die1, die2, die3, die4, die5};
        return array;
    }

    /** Returns the sum of the series of dice */
    public int getSum() {
        return die1.getValue() + die2.getValue() + die3.getValue() + die4.getValue() + die5.getValue();
    }

    /** Counts how many dice are of a particular input value */
    public int count(int value) {
        int count = 0;
        Die[] dice = diceToArray();

        for (int i = 0; i < dice.length; i++) {
            if (dice[i].getValue() == value) {
                count++;
            }
        }
        return count;
    }

    /** Returns die value with the highest occurrence (count) */
    public int maxCount() {
        //Initiate variable holding die value that appears the most, default to 1
        int max_val = 1;
        //iterate through die values starting at 2
        for (int i = 2; i <= 6; i++) {
            int count = this.count(i);
            //Replace if count >= previous high (we take higher value since in Yahtzee it will likely give more points)
            if (count >= this.count(max_val)) {
                max_val = i;
            }
        }
        return max_val;
    }

    /** Returns die value with second most occurrences. Used to help calculate Full House */
    public int secondMost() {
        int first = Integer.MIN_VALUE;
        int second = Integer.MIN_VALUE;

        for (int i = 1; i <= 6; i++) {
            //Save counts for most frequent value, second most frequent, and current value being compared
            int countFirst = this.count(first);
            int countSecond = this.count(second);
            int currCount = this.count(i);

            //If current count greater than previous highest count, previous first is new second, current becomes first
            if (currCount >= countFirst) {
                second = first;
                first = i;
            }
            //If current count is between first and second, it is the new second most frequent value
            else if (currCount >= countSecond && i != first) {
                second = i;
            }
        }
        if (count(second) == 0)
            return first;
        else
            return second;
    }

    /** Returns number of most appearing die value */
    public int mostOfAKind() {
        return count(maxCount());
    }

    /** Returns quantity of second most appearing die value */
    public int secondMostOfAKind() {
        return count(secondMost());
    }


    /** Roll of the dice in the set */
    public void rollAll() {
        die1.roll(); die2.roll(); die3.roll(); die4.roll(); die5.roll();
    }

    /** Roll the dice that are passed in as input */
    public void roll(Die ... dice) {
        assert dice != null;

        for (Die die : dice) {
            die.roll();
        }
    }

    /** Checks if current state of dice is a 3 of a kind of any value */
    public boolean isThreeOfAKind() {
        if (mostOfAKind() >= 3)
            return true;

        return false;
    }

    /** Checks if current state of dice is a 4 of a kind of any value */
    public boolean isFourOfAKind() {
        if (mostOfAKind() >= 4)
            return true;

        return false;
    }

    /** Check if we have a Full House */
    public boolean isFullHouse()
    {
        if (mostOfAKind() == 3 && secondMostOfAKind() == 2)
            return true;

        return false;
    }

    /** Check if we have a small straight */
    public boolean isSmallStraight()
    {
        if (exists(1) && exists(2) && exists(3) && exists(4)
            || exists(2) && exists(3) && exists(4) && exists(5)
            || exists(3) && exists(4) && exists(5) && exists(6))
            return true;

        return false;
    }

    /** Check if we have a large straight */
    public boolean isLargeStraight()
    {
        if (exists(1) && exists(2) && exists(3) && exists(4) && exists(5)
                || exists(2) && exists(3) && exists(4) && exists(5) && exists(6))
            return true;

        return false;
    }

    /** Check if we have a Yahtzee */
    public boolean isYahtzee() {
        if (mostOfAKind() == 5)
            return true;

        return false;
    }

    /** Check if a particular value exists in the set of dice */
    public boolean exists(int dieValue) {
        Die[] dice = this.diceToArray();

        for (int i = 0; i < dice.length; i++) {
            if (dice[i].getValue() == dieValue) {
                return true;
            }
        }
        return false;
    }

    /** Method that neatly returns values of all 5 dice as a String */
    @Override
    public String toString() {
        Die[] dice = diceToArray();
        String s = "";

        for (int i = 0; i < dice.length; i++) {
            s += "Die " + (i+1) + ": " + dice[i].getValue() + "\n";
        }
        return s;
    }

}
