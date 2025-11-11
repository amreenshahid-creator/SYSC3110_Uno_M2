/**
 * View interface for the UNO game.
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
public interface UnoView {
    void update(UnoModel model);
    void updateHandPanel(UnoModel model, UnoController controller);
    void updateStatusMessage(String msg);
    void updateWinner(String winner, int score);

}