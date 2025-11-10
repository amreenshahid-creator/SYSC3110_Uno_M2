import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnoController implements ActionListener {
    private UnoModel model;
    private UnoView view;
    private UnoFrame frame;

    public UnoController(UnoModel model, UnoView view, UnoFrame frame) {
        this.model = model;
        this.view = view;
        this.frame = frame;
    }

    public void play() {
        for(String player: frame.getPlayerName()) {
            model.addPlayer(player);
        }
        model.newRound();
        view.update(model);
        view.updateHandPanel(model, this);
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Next Player")) {
            model.advance();
            view.update(model);
            view.updateHandPanel(model, this);
            return;
        }

        if(e.getActionCommand().equals("Draw Card")) {
            model.drawCard();
            model.advance();
            view.update(model);
            view.updateHandPanel(model, this);
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
                    model.advance();
                    view.update(model);
                    view.updateHandPanel(model, this);
                }

                else if(cardPicked.getValue().equals(UnoModel.Values.REVERSE)) {
                    model.reverse();
                    model.advance();
                    view.update(model);
                    view.updateHandPanel(model, this);
                }

                else if(cardPicked.getValue().equals(UnoModel.Values.SKIP)) {
                    model.skip();
                    view.update(model);
                    view.updateHandPanel(model, this);
                }

                else if(cardPicked.getValue().equals(UnoModel.Values.WILD)) {
                    String colour = frame.colourSelectionDialog();
                    if(colour != null) {
                        model.wild(UnoModel.Colours.valueOf(colour));
                    }
                    model.advance();
                    view.update(model);
                    view.updateHandPanel(model, this);
                }

                else {
                    model.advance();
                    view.update(model);
                    view.updateHandPanel(model, this);
                }
            }
            else {
                view.update(model);
            }
        }
    }

    public void disableButtons() {

    }


    public void enableButtons() {

    }
}
