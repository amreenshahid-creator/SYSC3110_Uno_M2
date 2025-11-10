import java.util.*;

/**
 * Core game model for a simplified UNO game (Milestone-ready).
 * <p>
 * Responsibilities:
 * - Holds game state (players, direction, current/next player).
 * - Manages deck-less random card generation for draws (stubbed RNG).
 * - Enforces legal-play checks (colour/value match; wilds always playable).
 * - Applies card effects (DRAW_ONE, REVERSE, SKIP, WILD, WILD_DRAW_TWO).
 * - Computes per-round score for the winner and tracks cumulative scores.
 * - Notifies registered views (observer-style hooks via {@link #addView(UnoView)}).
 * <p>
 * Notes:
 * - This class is not thread-safe (single-threaded Swing usage assumed).
 * - Random draws use {@link java.util.Random}; no persistence of a physical deck in this version.
 */
public class UnoModel {

    /** Available card colours. Wilds use null colour. */
    public enum Colours {RED, YELLOW, GREEN, BLUE}

    /** Available card values, including action/wild cards. */
    public enum Values {ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DRAW_ONE, REVERSE, SKIP, WILD, WILD_DRAW_TWO}

    // Players in turn order (clockwise or counterclockwise based on 'direction').
    private List<Player> players = new ArrayList<>();
    // Index of the current player within 'players'.
    private int currPlayerIndex = 0;
    // Turn direction: +1 for clockwise, -1 for counterclockwise.
    int direction = 1;
    // The card currently on top of the discard pile (sets legal-play constraints).
    private Card topCard;
    // Cumulative (match) scores per player name.
    private Map<String, Integer> finalScores = new HashMap<>();
    // Registered views to be notified on model changes.
    private List<UnoView> views = new ArrayList<>();

    /**
     * Immutable-ish card record (mutable colour for wild recolouring).
     * <p>
     * For WILD/WILD_DRAW_TWO, colour may be null until chosen by a player.
     */
    public static class Card {
        private Colours colour;   // can be reassigned for wilds via wild()/wildDrawTwo()
        private Values value;

        /**
         * Creates a card with the given colour and value.
         * For wilds, pass null for colour.
         */
        public Card(Colours colour, Values value) {
            this.colour = colour;
            this.value = value;
        }

        /** @return current colour; may be null for wilds until chosen */
        public Colours getColour() { return colour; }

        /** @return value of the card (number/action/wild) */
        public Values getValue() { return value; }

        /** Assigns a new colour (used when playing a wild). */
        public void setColour(Colours colour) { this.colour = colour; }

        /** Sets the value (not typically used after creation). */
        public void setValue(Values value) { this.value = value; }

        /**
         * @return image file name for this card (assumes resources in /images).
         * Wilds do not include colour in the file name.
         */
        public String getFileName() {
            if (value == Values.WILD || value == Values.WILD_DRAW_TWO) {
                return "images/" + value.toString() + ".png";
            }
            return "images/" + colour.toString() + "_" + value.toString() + ".png";
        }

        /**
         * Logical equality: same colour and value.
         * (Note: no hashCode override—avoid using as hash keys unless added.)
         */
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Card other)) return false;
            return this.colour == other.colour && this.value == other.value;
        }
    }

    /**
     * Player entity: holds a name and personal hand.
     */
    public static class Player {
        private final List<Card> personalDeck = new ArrayList<>();
        private final String name;

        /** Creates a player with the given display name. */
        public Player(String name) {
            this.name = name;
        }

        /** @return live list of cards held by the player */
        public List<Card> getPersonalDeck() { return personalDeck; }

        /** Adds a single card to the player's hand. */
        public void addCard(Card c) { personalDeck.add(c); }

        /** @return player display name */
        public String getName() { return name; }
    }

    /**
     * Generates a random card. In this milestone, there is no physical deck;
     * draws are random and infinite.
     * @return a pseudo-random {@link Card}
     */
    public Card getRandomCard() {
        Random rand = new Random();

        Values[] values = Values.values();
        Values value = values[rand.nextInt(values.length)];

        Colours colour = null;
        // Wilds have no colour until set; all other cards need a colour.
        if (value != Values.WILD && value != Values.WILD_DRAW_TWO) {
            Colours[] colours = Colours.values();
            colour = colours[rand.nextInt(colours.length)];
        }
        return new Card(colour, value);
    }

    /**
     * Plays a card from the current player's hand and sets it as the top card.
     * Assumes caller checked {@link #isPlayable(Card)} beforehand.
     * @param card the card to play
     */
    public void playCard(Card card) {
        getCurrPlayer().getPersonalDeck().remove(card);
        topCard = card;
    }

    /**
     * Current player draws one random card.
     */
    public void drawCard() {
        Player currPlayer = getCurrPlayer();
        currPlayer.addCard(getRandomCard());
    }

    /**
     * Makes the next player (relative to current direction) draw exactly one card.
     * @return the drawn {@link Card}
     */
    public Card drawOne() {
        Card drawnCard = getRandomCard();
        int nextPlayerIndex = (currPlayerIndex + 1) % players.size();
        Player nextPlayer = players.get(nextPlayerIndex);
        nextPlayer.addCard(drawnCard);
        return drawnCard;
    }

    /**
     * Reverses play direction (clockwise ↔ counterclockwise).
     */
    public void reverse() {
        direction = -direction;
    }

    /**
     * Skips the next player's turn by advancing two steps in current direction.
     */
    public void skip() {
        currPlayerIndex = (currPlayerIndex + 2 * direction + players.size()) % players.size();
    }

    /**
     * Applies a wild colour choice to the current top card.
     * @param newColour chosen colour (cannot be null)
     */
    public void wild(Colours newColour) {
        topCard.setColour(newColour);
    }

    /**
     * Wild Draw Two: set colour and make next player draw 2, then skip them.
     * @param newColour chosen colour for the wild
     * @return list containing the two drawn cards
     */
    public List<Card> wildDrawTwo(Colours newColour) {
        topCard.setColour(newColour);

        Card drawnCard1 = getRandomCard();
        Card drawnCard2 = getRandomCard();

        int nextPlayerIndex = (currPlayerIndex + direction + players.size()) % players.size();
        Player nextPlayer = players.get(nextPlayerIndex);
        nextPlayer.addCard(drawnCard1);
        nextPlayer.addCard(drawnCard2);

        List<Card> drawnCards = new ArrayList<>();
        drawnCards.add(drawnCard1);
        drawnCards.add(drawnCard2);

        // After drawing two, skip next player's turn.
        skip();

        return drawnCards;
    }

    /**
     * Starts a new round:
     * - Clears each player's hand and deals 7 random cards.
     * - Chooses a non-wild top card to begin play.
     * - Resets current player and direction.
     */
    public void newRound() {
        for (Player player : players) {
            player.getPersonalDeck().clear();
            for (int i = 0; i < 7; i++) {
                player.addCard(getRandomCard());
            }
        }
        // Ensure the first top card is not a wild.
        do {
            topCard = getRandomCard();
        } while (topCard.getValue() == Values.WILD || topCard.getValue() == Values.WILD_DRAW_TWO);

        currPlayerIndex = 0;
        direction = 1;
    }

    /**
     * Computes the round score earned by the winner:
     * Sum of point values of all other players' remaining cards.
     * @param winner player who emptied their hand
     * @return numeric score for this round
     */
    public int getScore(Player winner) {
        int score = 0;
        for (Player player : players) {
            if (player == winner) continue;

            List<Card> deck = player.getPersonalDeck();
            for (int i = 0; i < deck.size(); i++) {
                Card card = deck.get(i);
                // Point mapping per standard UNO-like values.
                switch (card.getValue()) {
                    case ZERO -> score += 0;
                    case ONE -> score += 1;
                    case TWO -> score += 2;
                    case THREE -> score += 3;
                    case FOUR -> score += 4;
                    case FIVE -> score += 5;
                    case SIX -> score += 6;
                    case SEVEN -> score += 7;
                    case EIGHT -> score += 8;
                    case NINE -> score += 9;
                    case DRAW_ONE -> score += 10;
                    case SKIP, REVERSE -> score += 20;
                    case WILD_DRAW_TWO -> score += 25;
                    case WILD -> score += 50;
                }
            }
        }
        return score;
    }

    /**
     * Advances to the next player's turn using current direction.
     */
    public void advance() {
        currPlayerIndex = (currPlayerIndex + direction + players.size()) % players.size();
    }

    /**
     * Updates cumulative scores and checks if any player reached the winning threshold.
     * @param winner player who just won the round
     * @return true if someone (possibly the winner) reached the match target (e.g., 500)
     */
    public boolean checkWinner(Player winner) {
        int winnerScore = getScore(winner);
        finalScores.put(winner.getName(), finalScores.get(winner.getName()) + winnerScore);

        for (Player p : players) {
            int SCORE_TO_WIN = 500;
            if (finalScores.get(p.getName()) >= SCORE_TO_WIN) {
                return true;
            }
        }
        return false;
    }

    /**
     * Legal play check:
     * - Wilds are always playable.
     * - Otherwise, either colour matches top card's colour or value matches top card's value.
     * @param card card to evaluate
     * @return true if the card can be legally played now
     */
    public boolean isPlayable(Card card) {
        if (card.getValue() == Values.WILD || card.getValue() == Values.WILD_DRAW_TWO) {
            return true; // Wilds always playable
        }
        boolean sameColour = topCard.getColour() != null && card.getColour() != null
                && card.getColour().equals(topCard.getColour());
        boolean sameValue = card.getValue() == topCard.getValue();
        return sameColour || sameValue;
    }

    /**
     * Adds a new player by name and initializes their cumulative score to 0.
     * @param playerName display name
     */
    public void addPlayer(String playerName) {
        players.add(new Player(playerName));
        finalScores.put(playerName, 0);
    }

    /** @return current player object */
    public Player getCurrPlayer() {
        return players.get(currPlayerIndex);
    }

    /**
     * @return the next player considering current direction
     */
    public Player getNextPlayer() {
        return players.get((currPlayerIndex + direction + players.size()) % players.size());
    }

    /** @return the current top (discard) card */
    public Card getTopCard() { return topCard; }

    /**
     * Sets the current top (discard) card.
     * @param card card to become the new top
     */
    public void setTopCard(Card card) { topCard = card; }

    /**
     * @return true if the current player has emptied their hand
     */
    public boolean isDeckEmpty() {
        return getCurrPlayer().getPersonalDeck().isEmpty();
    }

    /**
     * Registers a view to receive {@link #notifyViews()} updates.
     * @param view a view to add
     */
    public void addView(UnoView view) {
        if (!views.contains(view)) {
            views.add(view);
        }
    }

    /**
     * Unregisters a previously added view.
     * @param view view to remove
     */
    public void removeView(UnoView view) {
        views.remove(view);
    }

    /**
     * Notifies all registered views to refresh from model state.
     * (Simple observer-style callback.)
     */
    public void notifyViews() {
        for (UnoView v : views) {
            v.update(this);
        }
    }
}
