public class Die {

    private int value;

    //Constructor initiates value to 1 by default, prevents case of uninitiated value
    public Die()
    {
        this.value = 1;
    }

    //Custom constructor that initiates a die to the value the user chooses
    public Die(int value) {
        if (value < 1 || value > 6) {
            throw new IllegalArgumentException("Value must be between 1 and 6");
        }
        this.value = value;
    }

    /** Sets value of the dice to the outcome of the roll */
    public void roll()
    {
        value = (int) (Math.random()*6) + 1;
    }

    /** Returns value of the die */
    public int getValue()
    {
        return value;
    }

    /** String version of a Die simply returns its value */
    @Override
    public String toString()
    {
        return String.valueOf(this.getValue());
    }

    /**Override method checking equality of two Dice */
    @Override
    public boolean equals(Object pObject) {
        //If parameter null or not of the same Class as a DIe, return false
        if (pObject == null || pObject.getClass() != getClass()) {
            return false;
        }
        //If the parameter is the exact instance of the object (ie. compared to itself) return true
        if (pObject == this) {
            return true;
        }
        //Otherwise cast parameter to Die object and check for equality of their face values
        else {
            return ((Die)pObject).getValue() == this.getValue();
        }
    }

    /** hashCode returns value of die, any two dice with same hashcode (value) considered equal */
    @Override
    public int hashCode()
    {
        return value;
    }

}
