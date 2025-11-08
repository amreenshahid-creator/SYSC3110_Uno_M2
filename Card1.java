/**
* Represents a card in the game with a colour and value.
*
* @author Amreen Shahid
* @version 1.0 October 24, 2025
*/

public class Card {

    public enum Colours {RED, YELLOW, GREEN, BLUE};
    public enum Values {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        DRAW_ONE, REVERSE, SKIP, WILD, WILD_DRAW_TWO};

    private Colours colour;
    private Values value;
    
    /**
    * Constructs Card with specified colour and value.
    *
    * @param colour the colour of the card.
    * @param value the value of the card.
    */
    public Card(Colours colour, Values value) {
        this.colour = colour;
        this.value = value;
    }
    /**
    * Gets colour of the Card. 
    * 
    * @return colour of Card.
    */
    public Colours getColour() {
        return colour;
    }
    
    /**
    * Gets value of the Card. 
    * 
    * @return value of Card.
    */
    public Values getValue() {
        return value;
    }

    /**
    * Sets colour of the Card. 
    * 
    * @param colour of new colour to set.
    */
    public void setColour(Colours colour) {
        this.colour = colour;
    }
    
    /**
    * Sets value of the Card. 
    * 
    * @param value of new value to set.
    */
    public void setValue(Values value) {
        this.value = value;
    }
    
    /**
    * Returns a string representation of the card in the format "COLOUR VALUE".
    * For wild cards where colour can be null, return only value.
    *
    * @return string representation of card.
    */
    @Override
    public String toString() {
        if(colour == null) {
            return value.toString();
        }
        return colour + " " + value;
    }

}

