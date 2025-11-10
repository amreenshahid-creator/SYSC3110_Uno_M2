import javax.swing.*;
import java.awt.*;

/**
 * View class for the UNO game.
 * 
 * Responsible for:
 *  - Displaying the current player
 *  - Showing the top card image
 *  - Updating the player's hand panel
 *  - Updating status messages
 *  - Updating the scoreboard when a player wins
 *
 * The UnoView does NOT contain game logic. It only updates visible elements
 * inside the UnoFrame based on changes in the UnoModel.
 */
public class UnoView {

    /** Reference to the game's main GUI frame. */
    private UnoFrame frame;

    /**
     * Constructs a UnoView connected to a specific UnoFrame.
     *
     * @param frame the GUI window to update
     */
    public UnoView(UnoFrame frame) {
        this.frame = frame;
    }

    /**
     * Updates the top portion of the GUI:
     *  - Displays the current player's name
     *  - Updates the top card image
     *
     * @param model the game model providing updated state
     */
    public void update(UnoModel model) {
        frame.getCurrentPlayerLabel().setText("Current Player: " + model.getCurrPlayer().getName());

        // Resize and update the displayed top card image
        Dimension topCardSize = frame.getTopCardPanel().getSize();
        frame.getTopCardLabel().setIcon(
                frame.resizeImage(
                        model.getTopCard().getFileName(),
                        topCardSize.width - 180,
                        topCardSize.height - 250
                )
        );
    }

    /**
     * Updates the player's hand panel by:
     *  - Removing old card buttons
     *  - Creating fresh buttons for the current player's hand
     *  - Adding listeners for each card button
     *
     * @param model the game model containing the player's hand
     * @param controller controller handling card-click events
     */
    public void updateHandPanel(UnoModel model, UnoController controller) {
        frame.handPanelButtons(model.getCurrPlayer().getPersonalDeck(), controller);
    }

    /**
     * Updates the status message shown at the top of the screen.
     *
     * @param msg text describing what just happened (e.g., "Player drew a card")
     */
    public void updateStatusMessage(String msg) {
        frame.getStatusLabel().setText("Status Message: " + msg);
    }

    /**
     * Updates the scoreboard panel when a player wins.
     * Replaces that player's score label with their new total.
     *
     * @param winner name of the winning player
     * @param score updated score that should be displayed
     */
    public void updateWinner(String winner, int score) {
        JPanel scoreBoardPanel = frame.getScoreBoardPanel();

        // Loop through scoreboard labels and replace matching entry
        Component[] scores = scoreBoardPanel.getComponents();
        for (Component comp : scores) {
            if (comp instanceof JLabel label) {
                if (label.getText().startsWith(winner)) {
                    label.setText(winner + ": " + score);
                }
            }
        }
    }
}
