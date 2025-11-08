import java.util.*;
/**
* GameFlow is responsible for managing the overall UNO game structure and progression.
* Initializes players and begins the game, handles player turns and turn progression.
* Compute and maintain player's scores to determine winner.
*
* @author Marc Aoun
* @version 1.0 October 23, 2025
* 
* @author Amreen Shahid
* @version 1.1 October 24, 2025
* 
* @author Naima Mamun
* @verion 1.2 October 24, 2025
*/

public class GameFlow {
    Scanner input = new Scanner(System.in);
    private List<Player> players = new ArrayList<>();
    private Card topCard;
    private Player playerSkipped = null;
    private Map<String, Integer> finalScores = new HashMap<>();
    private int direction = 1;
    private int SCORE_TO_WIN = 500;

    /**
    * Generate random card instance, can return a regular number card or wild card
    * 
    * @return a new insatnce of Card or ActionCards
    */
    public static Card GetRandomCard(){            
        Random rand = new Random();

        //String[] colours= {"Yellow", "Red", "Blue", "Green"};

        Card.Values[] values = Card.Values.values();
        Card.Values value = values[rand.nextInt(values.length)];

        Card.Colours colour = null;

        if (value != Card.Values.WILD && value != Card.Values.WILD_DRAW_TWO) {
            Card.Colours[] colours = Card.Colours.values();
            colour = colours[rand.nextInt(colours.length)];
        }

        if(value == Card.Values.DRAW_ONE ||value == Card.Values.REVERSE || value == Card.Values.SKIP || value == Card.Values.WILD || value == Card.Values.WILD_DRAW_TWO ) {
            return new ActionCards(colour, value);
        }

        //int value = rand.nextInt(9)+1;
        return new Card(colour, value);

    }
    
    /**
    * Makes player draw a card and adds to their personal deck.
    * Prints a description of the card attributes.
    * 
    * @param nextPlayer the player that must draw the next card.
    */
    public void drawOne(Player nextPlayer) {
        Card drawnCard = GetRandomCard();
        nextPlayer.addCard(drawnCard);
        System.out.println(nextPlayer.getName() + " has to draw a card: " + drawnCard);
    }
    
    /**
    * Marks a player who's turn will be skipped at the next turn.
    *
    * @param currPlayer the player that caused the direction change.
    */
    public void reverse(Player currPlayer){
        direction = -direction;
        System.out.println(currPlayer.getName() + " has reversed the order");
    }
    
    /**
    * Reverse the current direction of play, prints a message about the player that did so.
    * 
    * @param nextPlayer the player who's turn is skipped.
    */
    public void skip(Player nextPlayer) {
        System.out.println(nextPlayer.getName() + " misses their turn");
        playerSkipped = nextPlayer;
    }
    
    /**
    * Allows current player to set a new colour to be played starting at the next turn.
    * Updates the colour of the top of the pile of cards.
    */
    public void wild(){
        boolean valid = false;
        while(!valid) {
            System.out.println("Enter the colour that will be played next (RED, YELLOW, GREEN, BLUE): ");
            String colour = input.next().toUpperCase();

            if(colour.equals("RED") || colour.equals("YELLOW") || colour.equals("GREEN") || colour.equals("BLUE")){
                Card.Colours newColour = Card.Colours.valueOf(colour);
                topCard.setColour(newColour);
                System.out.println(colour + " has been chosen for the next card");
                valid = true;
            }

            else{
                System.out.println("Invalid colour, choose again");
            }
        }
    }

    /**
    * Adds two random cards to next player's deck and skips that player's turn. 
    * 
    * @param nextPlayer the player who will be drawing two more cards, and who's turn
    * will be skipped.
    */
    public void wildDrawTwo(Player nextPlayer) {
        wild();
        Card drawnCard1 = GetRandomCard();
        Card drawnCard2 = GetRandomCard();
        nextPlayer.addCard(drawnCard1);
        nextPlayer.addCard(drawnCard2);

        System.out.println(nextPlayer.getName() + " has to draw 2 cards: " + drawnCard1 + ", " + drawnCard2);

        skip(nextPlayer);
    }

    /**
    * Calculates score of the winner of the current round, by taking a sum of all the other 
    * player's cards according to specific game rules.
    * 
    * @param winner the player who won the round.
    * @return integer score calculated of winner.
    */
    public int score(Player winner){
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

    /**
    * Computes, updates and displays player's current round scores, 
    * prompts player to start a new round or quit.
    * 
    * @param winner the player who won the round.
    * @return true if the round has a winner that have no cards left, or false otherwise.
    */
    public boolean checkWinner(Player winner) {
        int winnerScore = score(winner);
        finalScores.put(winner.getName(), finalScores.get(winner.getName()) + winnerScore);

        System.out.println("Player " + winner.getName() + " wins this round!");
        System.out.println(winner.getName() + " earned " + winnerScore + " points");

        System.out.println("CURRENT SCORES: ");
        for(Player p: players) {
            System.out.println(p.getName() + ": " + finalScores.get(p.getName()));
            if(finalScores.get(p.getName()) >= SCORE_TO_WIN) {
                System.out.println(p.getName() + " is the final winner!");
                return true;
            }
        }

        System.out.println("No winner yet. Type 'NEW' to start a new round");
        String newRoundInput = input.nextLine().toUpperCase();
        if(newRoundInput.equals("NEW")) {
            newRound();
        } else {
            System.exit(0);
        }
        return false;
    }

    /**
    * Starts a new rounf by clearing player's current deck of cards, 
    * choosing a regular card to start a new pile.
    */
    public void newRound() {
        for(Player player: players) {
            player.getPersonalDeck().clear();
            for(int i = 0; i < 7; i++) {
                player.addCard(GetRandomCard());
            }
        }
        do {
            topCard = GetRandomCard();
        }while (topCard.getValue() == Card.Values.WILD || topCard.getValue() == Card.Values.WILD_DRAW_TWO);  //top card cant be a wild card

        playerSkipped = null;
    }

    /**
    * Runs main loop for the UNO Game.
    * Prompts player for number or participants and names.
    * Initializes scores, deals the cards.
    * Goes through the player turns taking into account wild card changes.
    * 
    */
    public void play() {
        int numPlayers;

        do {
            System.out.print("Enter the number of players (2-4): ");
            while (!input.hasNextInt()) {
                System.out.print("Invalid input. Please try again: ");
                input.next();
            }
            numPlayers = input.nextInt();
        } while (numPlayers < 2 || numPlayers > 4);


        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter a name for player " + i + ": ");
            String name = input.next();
            players.add(new Player(name));
            finalScores.put(name, 0);
        }

        /* put this piece of code in newRound()
        topCard = GetRandomCard();
        for (Player player : players) {
            for(int i = 0; i < 7;i++){
                player.addCard(GetRandomCard());
            }
        }
        */
        newRound();

        //boolean next = false;
        boolean win = false;
        int currIndex = 0;

        while (!win) {
            //while(!next){
            //for (Player player : players) {
            Player player = players.get(currIndex);

            if (playerSkipped != null && player == playerSkipped) { //helps skip player without messing with reverse method
                playerSkipped = null;
                currIndex = (currIndex + direction + players.size()) % players.size();
                continue;
            }

            boolean next = false;
            while(!next){
                System.out.println("------ " + player.getName() + "s'turn ------");
                System.out.println("Top card: " + topCard.toString());
                System.out.println("Your cards: ");

                //int i = 1;
                //for(Card card: player.getPersonalDeck()){
                for(int i = 0; i < player.getPersonalDeck().size(); i++) {
                    System.out.println((i + 1) + ". " + player.getPersonalDeck().get(i));
                    //System.out.println(i + ". " + card.toString());
                    //i++;
                }

                System.out.println("Enter a card index to play a card, or 0 to draw a card");

                int cardIndex = -1;

                while(!input.hasNextInt()){
                    System.out.print("Invalid input. Please try again: ");
                    input.next();
                }

                cardIndex = input.nextInt();
                input.nextLine();

                while(cardIndex < 0 || cardIndex > player.getPersonalDeck().size()){
                    System.out.print("Invalid input. Please try again: ");
                    cardIndex = input.nextInt();
                }

                if (cardIndex == 0){
                    player.getPersonalDeck().add(GetRandomCard());
                    next = true;
                }

                else if(cardIndex >= 1 && cardIndex <= player.getPersonalDeck().size()){

                    Card chosen = player.getPersonalDeck().get(cardIndex - 1);
                    boolean sameColour = false;

                    if (chosen.getColour() != null && topCard.getColour() != null) {
                        sameColour = chosen.getColour().equals(topCard.getColour());
                    } else if (chosen.getValue() == Card.Values.WILD || chosen.getValue() == Card.Values.WILD_DRAW_TWO) {
                        sameColour = true;
                    }

                    //boolean sameColour = chosen.getColour().equals(topCard.getColour());
                    //boolean sameColour = chosen.getColour().equals(topCard.getColour());
                    boolean sameValue  = chosen.getValue() == topCard.getValue();

                    if (sameColour || sameValue){
                        if(chosen instanceof ActionCards actions) {
                            int currPlayerNum = players.indexOf(player);
                            int nextPlayerNum = (currPlayerNum + direction + players.size()) % players.size();

                            actions.processActionCard(this, players.get(nextPlayerNum), player);
                        }

                        //topCard.setColour(player.getPersonalDeck().get(cardIndex - 1).getColour());
                        //topCard.setValue(player.getPersonalDeck().get(cardIndex - 1).getValue());
                        //player.getPersonalDeck().remove(cardIndex - 1);

                        Card playedCard = player.getPersonalDeck().remove(cardIndex - 1);
                        topCard.setValue(playedCard.getValue());
                        if (playedCard.getColour() != null) {
                            topCard.setColour(playedCard.getColour());
                        }

                        if(player.getPersonalDeck().isEmpty()){
                            boolean finalWinner = checkWinner(player);
                            if(finalWinner) {
                                return;
                            }

                            //win = true;
                            next = true;
                            continue;
                        }
                        next = true;

                    } else {
                        System.out.println("Placing that card is not a valid move. Try again. ");
                    }
                } else{
                    System.out.println("Invalid input. Please try again: ");
                }
            } currIndex = (currIndex + direction + players.size()) % players.size();
        }
    }

    /**
    * Main method that creates a new GameFlow class that will initilize our game,
    * begins the game.
    */
    public static void main(String[] args) {
        GameFlow game = new GameFlow();
        game.play();
    }

}

