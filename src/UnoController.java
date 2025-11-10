import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnoController implements ActionListener {
    private final UnoModel model;
    private final UnoView view;
    private final UnoFrame frame;
    private boolean isAdvanced;

    public UnoController(UnoModel model, UnoView view, UnoFrame frame) {
        this.model = model;
        this.view = view;
        this.frame = frame;

        isAdvanced = false;
    }

    public void play() {
        for(String player: frame.getPlayerName()) {
            model.addPlayer(player);
        }
        model.newRound();
        view.update(model);
        view.updateHandPanel(model, this);
        frame.enableCards();
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Next Player")) {
            if(!isAdvanced) {
                model.advance();
            }
            view.update(model);
            view.updateHandPanel(model, this);
            frame.enableCards();

        }

        if(e.getActionCommand().equals("Draw Card")) {
            frame.getNextButton().setEnabled(true);
            model.drawCard();
            view.update(model);
            view.updateHandPanel(model, this);
            frame.disableCards();
            view.updateStatusMessage(model.getCurrPlayer().getName() + " draws a card.");

        }

        else {
            UnoModel.Card cardPicked = null;
            String cmd;
            for(UnoModel.Card card: model.getCurrPlayer().getPersonalDeck()) { //Find the card that was picked
                if(card.getValue().equals(UnoModel.Values.WILD) || card.getValue().equals(UnoModel.Values.WILD_DRAW_TWO)){
                    cmd = card.getValue() + "_" + System.identityHashCode(card);
                } else { cmd = card.getColour() + "_" + card.getValue();

                } if(cmd.equals(e.getActionCommand())) {
                    cardPicked = card;
                    break;
                }
            }

            if (cardPicked != null && model.isPlayable(cardPicked)) {
                model.playCard(cardPicked);
                model.setTopCard(cardPicked);

                if(cardPicked.getValue().equals(UnoModel.Values.DRAW_ONE)){
                    model.drawOne();
                    //model.advance();
                    view.update(model);
                    view.updateHandPanel(model, this);
                    frame.disableCards();
                    isAdvanced = false;
                    view.updateStatusMessage(model.getNextPlayer().getName() + " draws a card");
                }

                else if(cardPicked.getValue().equals(UnoModel.Values.REVERSE)) {
                    model.reverse();
                    view.update(model);
                    view.updateHandPanel(model, this);
                    frame.disableCards();
                    isAdvanced = false;
                    view.updateStatusMessage(model.getCurrPlayer().getName() + " has reversed the order");
                }

                else if(cardPicked.getValue().equals(UnoModel.Values.SKIP)) {
                    String nextPlayer = model.getNextPlayer().getName();
                    model.skip();
                    view.update(model);
                    view.updateHandPanel(model, this);
                    frame.disableCards();
                    isAdvanced = true;
                    view.updateStatusMessage("Skip card has been played, " + nextPlayer + " skips their turn.");
                }

                else if(cardPicked.getValue().equals(UnoModel.Values.WILD)) {
                    String colour = frame.colourSelectionDialog();
                    if(colour != null) {
                        model.wild(UnoModel.Colours.valueOf(colour));
                    }
                    view.update(model);
                    view.updateHandPanel(model, this);
                    frame.disableCards();
                    isAdvanced = false;
                    view.updateStatusMessage("New colour chosen, " + colour + ".");
                }

                else if(cardPicked.getValue().equals(UnoModel.Values.WILD_DRAW_TWO)) {
                    String colour = frame.colourSelectionDialog();
                    String nextPlayer = model.getNextPlayer().getName();
                    if(colour != null) {
                        model.wildDrawTwo(UnoModel.Colours.valueOf(colour));
                    }

                    view.update(model);
                    view.updateHandPanel(model, this);
                    frame.disableCards();
                    isAdvanced = true;
                    view.updateStatusMessage("New colour chosen, " + colour + ", " + nextPlayer + " draws two cards and skips their turn.");
                }

                else {
                    view.update(model);
                    view.updateHandPanel(model, this);
                    frame.disableCards();
                    isAdvanced = false;
                    view.updateStatusMessage(model.getCurrPlayer().getName() + " played a card");
                }

                if(model.isDeckEmpty()) {
                    UnoModel.Player winner = model.getCurrPlayer();
                    int score = model.getScore(winner);
                    view.updateStatusMessage(winner.getName() +  "is the Winner!");
                    view.updateWinner(winner.getName(), score);
                    frame.disableAllButtons();
                }
            }
            if(cardPicked != null && !model.isPlayable(cardPicked)){
                view.updateStatusMessage("Placing that card is not a valid move. Try again.");
            }
        }
    }
}
