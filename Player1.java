import java.util.ArrayList;
import java.util.List;
/**
* Representation of the player in game. Each player has a name, and personal deck of cards.
* 
* @author Amreen Shahid
* @version 1.0 October 24, 2025
* 
*/

public class Player {
    private final String name;
    private final List<Card> personalDeck = new ArrayList<>();
    
    /**
    * Constructs a Player with the specified name.
    *
    * @param name the name of player.
    */
    public Player(String name){
        this.name = name;
    }

    /**
    * Gets player name. 
    * 
    * @return name of player
    */
    public String getName() {
        return name;
    }
    
    /**
    * Returns the list of cards currently held by the player.
    *
    * @return a list of cards representing the player's personal deck.
    */
    public List<Card> getPersonalDeck() {
        return personalDeck;
    }

    /**
    * Adds a card to the player's personal deck.
    * 
    * @param card the card to add to the player's personal deck.
    */
    public void addCard(Card c) {
        personalDeck.add(c);
    }
    
    /**
    * Returns string representation of player name.
    * 
    * @return the player's name as a string.
    */
    @Override
    public String toString() {
        return name;
    }
}

