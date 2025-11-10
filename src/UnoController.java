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
                }

                else if(cardPicked.getValue().equals(UnoModel.Values.REVERSE)) {
                    model.reverse();
                    view.update(model);
                    view.updateHandPanel(model, this);
                    frame.disableCards();
                    isAdvanced = false;
                }

                else if(cardPicked.getValue().equals(UnoModel.Values.SKIP)) {
                    model.skip();
                    view.update(model);
                    view.updateHandPanel(model, this);
                    frame.disableCards();
                    isAdvanced = true;
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
                }

                else if(cardPicked.getValue().equals(UnoModel.Values.WILD_DRAW_TWO)) {
                    String colour = frame.colourSelectionDialog();
                    if(colour != null) {
                        model.wildDrawTwo(UnoModel.Colours.valueOf(colour));
                    }

                    view.update(model);
                    view.updateHandPanel(model, this);
                    frame.disableCards();
                    isAdvanced = true;
                }

                else {
                    view.update(model);
                    view.updateHandPanel(model, this);
                    frame.disableCards();
                    isAdvanced = false;
                }
            }
            else {
                view.update(model);
            }
        }
    }
}
