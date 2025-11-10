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
    }

    public void updateHandPanel(UnoModel model, UnoController controller) {
        frame.handPanelButtons(model.getCurrPlayer().getPersonalDeck(), controller);
    }

    public void updateStatusMessage(String msg) {
        frame.getStatusLabel().setText("Status Message: " + msg);
    }

    public void updateWinner(String winner, int score) {
        JPanel scoreBoardPanel = frame.getScoreBoardPanel();

        Component[] scores = scoreBoardPanel.getComponents();
        for(Component comp: scores) {
            if(comp instanceof JLabel label) {
                if(label.getText().startsWith(winner)) {
                    label.setText(winner + ": " + score);
                }
            }
        }
    }
}