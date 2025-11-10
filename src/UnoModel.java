import java.util.*;

public class UnoModel {
    public enum Colours {RED, YELLOW, GREEN, BLUE};
    public enum Values {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DRAW_ONE, REVERSE, SKIP, WILD, WILD_DRAW_TWO};
    private List<Player> players = new ArrayList<>();
    private int currPlayerIndex = 0;
    int direction = 1;
    private Card topCard;
    private Map<String, Integer> finalScores = new HashMap<>();
    private List<UnoView> views = new ArrayList<>();


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
                return "images/" + value.toString() + ".png";
            }
            return "images/" + colour.toString() + "_" + value.toString() + ".png";
        }

        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if (!(o instanceof Card other)) {
                return false;
            }
            return this.colour == other.colour && this.value == other.value;
        }
    }

    public static class Player {
        private final List<Card> personalDeck = new ArrayList<>();
        private String name;

        public Player(String name){
            this.name = name;
        }

        public List<Card> getPersonalDeck() {
            return personalDeck;
        }
        public void addCard(Card c) {
            personalDeck.add(c);
        }

        public String getName() {
            return name;
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

    public void playCard(Card card) {
        getCurrPlayer().getPersonalDeck().remove(card);
        setTopCard(card);
    }

    public void drawCard() {
        Player currPlayer = getCurrPlayer();
        currPlayer.addCard(getRandomCard());
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

    public void newRound() {
        for(Player player: players) {
            player.getPersonalDeck().clear();
            for(int i = 0; i < 7; i++) {
                player.addCard(getRandomCard());
            }
        }
        do {
            topCard = getRandomCard();
        }while (topCard.getValue() == Values.WILD || topCard.getValue() == Values.WILD_DRAW_TWO);

        currPlayerIndex = 0;
        direction = 1;
    }

    public int getScore(Player winner) {
        int score = 0;
        for(Player player : players) {
            if (player == winner){
                continue;
            }
            List<Card> deck = player.getPersonalDeck();
            for (int i = 0; i < deck.size(); i++ ) {
                Card card = deck.get(i);

                switch(card.getValue()) {
                    case ZERO -> score += 0;
                    case ONE -> score +=1;
                    case TWO -> score += 2;
                    case THREE -> score += 3;
                    case FOUR -> score += 4;
                    case FIVE -> score +=5;
                    case SIX -> score += 6;
                    case SEVEN -> score += 7;
                    case EIGHT -> score +=8;
                    case NINE -> score += 9;
                    case DRAW_ONE -> score += 10;
                    case SKIP, REVERSE -> score += 20;
                    case WILD_DRAW_TWO -> score += 25;
                    case  WILD -> score += 50;
                }
            }
        }
        return score;
    }

    public void advance() {
        currPlayerIndex = (currPlayerIndex + direction + players.size()) % players.size();
    }

    public boolean checkWinner(Player winner) {
        int winnerScore = getScore(winner);
        finalScores.put(winner.getName(), finalScores.get(winner.getName()) + winnerScore);

        for(Player p: players) {
            int SCORE_TO_WIN = 500;
            if(finalScores.get(p.getName()) >= SCORE_TO_WIN) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayable(Card card){
        if(card.getValue() == Values.WILD || card.getValue() == Values. WILD_DRAW_TWO) { //wild cards can always be played
            return true;
        }

        boolean sameColour = topCard.getColour() != null && card.getColour() != null && card.getColour().equals(topCard.getColour());
        boolean sameValue = card.getValue() == topCard.getValue();

        return sameColour || sameValue;
    }

    public void addPlayer(String playerName) {
        players.add(new Player(playerName));
        finalScores.put(playerName, 0);
    }

    public Player getCurrPlayer() {
        return players.get(currPlayerIndex);
    }


    public Card getTopCard() {
        return topCard;
    }

    public void setTopCard(Card card) {
        topCard = card;
    }

    public void addView(UnoView view) {
        if(!views.contains(view)) {
            views.add(view);
        }
    }

    public void removeView(UnoView view){
        views.remove(view);
    }

    public void notifyViews() {
        for(UnoView v: views) {
            v.update(this);
        }
    }
}
