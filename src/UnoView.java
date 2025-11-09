import javax.swing.*;
import java.awt.*;

public class UnoView {
    private UnoFrame frame;

    public UnoView(UnoFrame frame) {
        this.frame = frame;
    }

    public void update(UnoModel model) {
        frame.getCurrentPlayerLabel().setText("Current Player: " + model.getCurrPlayer().getName());
        Dimension topCardSize = frame.getTopCardPanel().getSize();
        frame.getTopCardLabel().setIcon(frame.resizeImage(model.getTopCard().getFileName(), topCardSize.width - 180, topCardSize.height - 250));

        updateHandPanel(model);
    }

    public void updateHandPanel(UnoModel model) {
        JPanel hand = frame.getHandPanel();
        hand.removeAll();

        UnoModel.Player currPlayer = model.getCurrPlayer();

        for(UnoModel.Card c: currPlayer.getPersonalDeck()) {
            JButton cardButton = frame.cardButtons(c);
            hand.add(cardButton);
            hand.add(Box.createRigidArea(new Dimension(10, 0)));
        }

        hand.revalidate();
        hand.repaint();
    }
}
