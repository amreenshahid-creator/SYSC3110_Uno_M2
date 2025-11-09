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
    private java.util.List<String> playerName;


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
        statusLabel = new JLabel("Status Message: ", JLabel.CENTER);
        infoPanel.add(statusLabel);

        frame.add(infoPanel, BorderLayout.NORTH);

        //scoreboard
        scoreBoardPanel = new JPanel();
        scoreBoardPanel.setLayout(new GridLayout(5, 1, 5, 5));
        scoreBoardPanel.setBorder(BorderFactory.createTitledBorder("Scoreboard"));
        scoreBoardPanel.setPreferredSize(new Dimension(180, 200));
        for (int i = 1; i <= 4; i++){
            scoreBoardPanel.add(new JLabel("Player " + i + ": "));
        }
        frame.add(scoreBoardPanel, BorderLayout.WEST);
    

        //player deck
        handPanel = new JPanel();
        handPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        handPanel.setBorder(BorderFactory.createTitledBorder("Player's Deck"));
        handPanel.setPreferredSize(new Dimension(280, 300));

        //top 
        topCardPanel = new JPanel(new GridBagLayout());
        topCardPanel.setBorder(BorderFactory.createTitledBorder("Top Card"));
        topCardLabel = new JLabel("Card", JLabel.CENTER); 
        topCardLabel.setPreferredSize(new Dimension(100, 150));
        topCardPanel.add(topCardLabel);
        frame.add(topCardPanel, BorderLayout.CENTER);

        // Control panel
        controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.add(handPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        drawButton = new JButton("Draw Card");
        nextButton = new JButton("Next Player");
        buttonPanel.add(drawButton);
        buttonPanel.add(nextButton);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        messageArea = new JTextArea (4, 50);
        messageArea.setEditable (false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord (true);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        controlPanel.add(messageScroll, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.EAST);
        
        frame.setVisible (true);
        
        String[] playerOptions = {"2", "3", "4"};
        String playerCount = (String) JOptionPane.showInputDialog(frame, "Select Number of Players:", "Player Setup", JOptionPane.QUESTION_MESSAGE, null, playerOptions, playerOptions[0]);
        if (playerCount == null){
            System.exit(0);
        }
        int count = Integer.parseInt(playerCount);
        ArrayList<String> playerName = new ArrayList<>();
        for (int i = 1; i <= count; i++){
            String name = JOptionPane.showInputDialog(frame, "Enter name for Player "+ i + ":", "Player Setup", JOptionPane.QUESTION_MESSAGE);
            if (name == null || name.trim().isEmpty()){
                name = "Player" + i;
            }
        playerName.add(name);
        }
        
        scoreBoardPanel.removeAll();
        scoreBoardPanel.setLayout(new GridLayout(playerName.size() + 1, 1, 5, 5));
        JLabel title = new JLabel("Scoreboard", JLabel.CENTER);
        scoreBoardPanel.add(title);

        for (int i = 0; i < playerName.size(); i++){
            JLabel scores = new JLabel(playerName.get(i) + ": 0");
            scoreBoardPanel.add(scores);
        }
        scoreBoardPanel.revalidate();
        scoreBoardPanel.repaint();
        
    
        JOptionPane.showMessageDialog(frame, "Game Start");
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
    public String colourSelectionDialog() {
        String[] colours = {"RED", "YELLOW", "GREEN", "BLUE"};
        return (String) JOptionPane.showInputDialog(frame, "Choose new colour for Wild Card:", "Wild Card Colour", JOptionPane.PLAIN_MESSAGE, null, colours, colours[0]);
  }

    public static void main(String[] args) {
        UnoGUI frame = new UnoGUI();
    }
}


