import javax.swing.*;
import java.awt.*;
import java.util.*;

public class UnoGUI {
    private JFrame frame;
    private JPanel topCardPanel;
    private JPanel handPanel;
    private JPanel controlPanel;
    private JPanel scoreBoardPanel;
    private JLabel currentPlayerLabel;
    private JLabel topCardLabel;
    private JLabel statusLabel;
    private JTextArea messageArea;
    private JButton drawButton;
    private JButton nextButton;


    public UnoGUI () {
        initializeGUI();
    }
    private void initializeGUI() {
        frame = new JFrame ("UNO Game");
        frame.setSize (1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        //top panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout (2, 1));
        currentPlayerLabel = new JLabel("Current Player: ", JLabel.CENTER);
        infoPanel.add(currentPlayerLabel);
        statuslabel = new JLabel("Status Message: ", JLabel.CENTER);
        infoPanel.add(statusLabel);

        frame.add(infoPanel, BorderLayout.NORTH);

        //scoreboard
        scoreBoardPanel = new JPanel();
        scoreBoardPanel.setLayout(new GridLayout(5, 1, 5, 5));
        scoreBoardPanel.setBorder(BorderFactory.createTitledBorder("Scoreboard"));
        for (int i = 1; i <= 4; i++){
            scoreBoardPanel.add(new JLabel("Player " + i + ": "));
        }
        frame.add(scoreBoardPanel, BorderLayout.WEST);
    

        //player deck
        handPanel = new JPanel();
        handPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        handPanel.setBorder(BorderFactory.createTitledBorder("Player's Deck"));
        handPanel.setPreferredSize(new Dimension(900, 150));
        frame.add(handPanel, BorderLayout.SOUTH);

        //top 
        topCardPanel = new JPanel(new GridBagLayout());
        topCardPanel.setBorder(BorderFactory.createTitledBorder("Top Card"));
        topCardLabel = new JLabel("Card", JLabel.CENTER); 
        topCardLabel.setPreferredSize(new Dimension(100, 150));
        topCardPanel.add(ropCardLabel);
        frame.add(topCardPanel, BorderLayout.CENTER);

        // Control panel
        controlPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        drawButton = new JButton("Draw Card");
        nextButton = new JButton("Next Player");
        buttonPanel.add (drawButton);
        buttonPanel.add (nextButton);
        controlPanel.add (buttonPanel, BorderLayout.NORTH);

        messageArea = new JTextArea (4, 50);
        messageArea.setEditable (false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord (true);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        controlPanel.add(messageScroll, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.EAST);
        
        frame.setVisible (true);
    }
    public JFrame getFrame() {
        return frame;
    }
    public JPanel getHandPanel() {
        return handPanel;
    }
    public JLabel getTopCardLabel() {
        return topCardLabel;
    }
    public JLabel getCurrentPlayerLabel() {
        return currentPlayerLabel;
    }
    public JTextArea getMessageArea() {
        return messageArea;
    }
    public JButton getDrawButton() {
        return drawButton;
    }
    public JButton getNextButton() {
        return nextButton;
    }
    public JPanel getScoreBoardPanel() {
        return scoreBoardPanel;
    }
    public JLabel getStatusLabel() {
        return statusLabel;
    }
    public JPanel getTopCardPanel() {
        return topCardPanel;
    }
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
    public String colourSelectionDialog() {
        String[] colours = {"RED", "YELLOW", "GREEN", "BLUE"};
        return (String) JOptionPane.showInputDialog(frame, "Choose new colour for Wild Card:", "Wild Card Colour", JOptionPane.PLAIN_MESSAGE, null, colours, colours[0]);
  }

    public int playerCountDialog() {
        String input = JOptionPane.showInputDialog(frame, "Enter number of players (2-4):", "Player Setup", JOptionPane.QUESTION_MESSAGE);
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    public String playerNameDialog(int playerNumber) {
        return JOptionPane.showInputDialog(frame, "Enter name for player " + playerNumber + ":", "Player Setup", JOptionPane.QUESTION_MESSAGE);
    }

    public static void main(String[] args) {
        UnoGUI frame = new UnoGUI();
    }
}


