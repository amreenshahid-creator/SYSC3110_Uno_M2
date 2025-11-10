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
            for(UnoModel.Card card: model.getCurrPlayer().getPersonalDeck()) {           //Find the card that was picked
                if((card.getColour() + "_" + card.getValue()).equals(e.getActionCommand())) {
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
