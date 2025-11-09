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
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Next Player")) {
            model.advance();
            view.update(model);
        }

        if(e.getActionCommand().equals("Draw Card")) {
            model.drawCard();
            model.advance();
            view.update(model);
        }
    }

    public void disableButtons() {

    }


    public void enableButtons() {

    }
}
