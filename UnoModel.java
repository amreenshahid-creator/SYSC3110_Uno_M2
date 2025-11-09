import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnoModel {
    public enum Colours {RED, YELLOW, GREEN, BLUE};
    public enum Values {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DRAW_ONE, REVERSE, SKIP, WILD, WILD_DRAW_TWO};
    private List<Player> players = new ArrayList<>();
    private int currPlayerIndex = 0;
    int direction = 1;
    private Card topCard;

    public static class Card {
        private Colours colour;
        private Values value;

        public Card(Colours colour, Values value) {
            this.colour = colour;
            this.value = value;
        }

        public Colours getColour() { return colour; }
        public Values getValue() {
            return value;
        }
        public void setColour(Colours colour) {
            this.colour = colour;
        }
        public void setValue(Values value) {
            this.value = value;
        }

        public String getFileName() {
            if(value == Values.WILD || value == Values.WILD_DRAW_TWO) {
                return value.toString() + ".png";
            }
            return colour.toString() + "_" + value.toString() + ".png";
        }
    }

    public static class Player {
        private final List<Card> personalDeck = new ArrayList<>();

        public List<Card> getPersonalDeck() {
            return personalDeck;
        }
        public void addCard(Card c) {
            personalDeck.add(c);
        }
    }

    public Card getRandomCard() {
        Random rand = new Random();

        Values[] values = Values.values();
        Values value = values[rand.nextInt(values.length)];

        Colours colour = null;

        if (value != Values.WILD && value != Values.WILD_DRAW_TWO) {
            Colours[] colours = Colours.values();
            colour = colours[rand.nextInt(colours.length)];
        }
        return new Card(colour, value);
    }

    public Card drawOne() {
        Card drawnCard = getRandomCard();
        int nextPlayerIndex = (currPlayerIndex + 1) % players.size();
        Player nextPlayer = players.get(nextPlayerIndex);
        nextPlayer.addCard(drawnCard);
        return drawnCard;
    }

    public void reverse() {
        direction = -direction;
    }

    public void skip() {
        currPlayerIndex = (currPlayerIndex + direction + players.size()) % players.size();
        currPlayerIndex = (currPlayerIndex + direction + players.size()) % players.size();
    }

    public void wild(Colours newColour) {
        topCard.setColour(newColour);
    }

    public List<Card> wildDrawTwo(Colours newColour) {
        topCard.setColour(newColour);
        Card drawnCard1 = getRandomCard();
        Card drawnCard2 = getRandomCard();
        int nextPlayerIndex = (currPlayerIndex + 1) % players.size();
        Player nextPlayer = players.get(nextPlayerIndex);
        nextPlayer.addCard(drawnCard1);
        nextPlayer.addCard(drawnCard2);

        List<Card> drawnCards = new ArrayList<>();
        drawnCards.add(drawnCard1);
        drawnCards.add(drawnCard2);

        return drawnCards;
    }

    public void newRound() {}


    public int getScore() {
        return 1;
    }

    public boolean checkWinner() {
        return false;
    }



    public int getPlayer() {
        return 1;
    }

    public void getPersonalDeck() {}

    public boolean isPlayable(){
        return false;
    }

    public void addView() {}

    public void removeView(){}

    public void notifyViews() {}

}
