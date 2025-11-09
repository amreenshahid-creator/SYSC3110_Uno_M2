import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class UnoFrame {
    private JFrame frame;
    private JPanel topCardPanel;
    private JPanel handPanel;
    private JPanel controlPanel;
    private JPanel scoreBoardPanel;
    private JLabel currentPlayerLabel;
    private JLabel topCardLabel;
    private JLabel statusLabel;
    private JButton drawButton;
    private JButton nextButton;
    private JScrollPane deckScrollPane;
    private java.util.List<String> playerName;


    public UnoFrame () {
        initializeGUI();
    }
    private void initializeGUI() {
        frame = new JFrame ("UNO Game");
        frame.setSize (1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        //top info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout (2, 1));
        currentPlayerLabel = new JLabel("Current Player: ", JLabel.CENTER);
        infoPanel.add(currentPlayerLabel);
        statusLabel = new JLabel("Status Message: ", JLabel.CENTER);
        infoPanel.add(statusLabel);
        frame.add(infoPanel, BorderLayout.NORTH);

        //scoreboard
        scoreBoardPanel = new JPanel();
        scoreBoardPanel.setLayout(new GridLayout(0, 1, 5, 5));
        scoreBoardPanel.setBorder(BorderFactory.createTitledBorder("Scoreboard"));
        scoreBoardPanel.setPreferredSize(new Dimension(180, 200));
        for (int i = 1; i <= 4; i++){
            scoreBoardPanel.add(new JLabel("Player " + i + ": "));
        }
        frame.add(scoreBoardPanel, BorderLayout.WEST);

        //top 
        topCardPanel = new JPanel(new GridBagLayout());
        topCardPanel.setBorder(BorderFactory.createTitledBorder("Top Card"));
        topCardPanel.setPreferredSize(new Dimension(200, 200));
        topCardLabel = new JLabel();
        topCardLabel.setHorizontalAlignment((JLabel.CENTER));
        topCardLabel.setVerticalAlignment(JLabel.CENTER);
        //topCardLabel.setPreferredSize(new Dimension(80, 120));
        topCardPanel.add(topCardLabel);
        frame.add(topCardPanel, BorderLayout.CENTER);

        //player deck
        handPanel = new JPanel();
        handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.X_AXIS));
        handPanel.setBorder(BorderFactory.createTitledBorder("Player's Deck"));
        //handPanel.setPreferredSize(new Dimension(400, 300));
        handPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));


        //button for draw, next
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        drawButton = new JButton("Draw Card");
        nextButton = new JButton("Next Player");
        buttonPanel.add(drawButton);
        buttonPanel.add(nextButton);
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setPreferredSize(new Dimension(400, 300));
        deckScrollPane = new JScrollPane(handPanel);
        deckScrollPane.setPreferredSize(new Dimension(400, 300));
        deckScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        deckScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        controlPanel.add(deckScrollPane, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.EAST);
        
        frame.setVisible (true);
        
        String[] playerOptions = {"2", "3", "4"};
        String playerCount = (String) JOptionPane.showInputDialog(frame, "Select Number of Players:", "Player Setup", JOptionPane.QUESTION_MESSAGE, null, playerOptions, playerOptions[0]);
        if (playerCount == null){
            System.exit(0);
        }
        int count = Integer.parseInt(playerCount);
        playerName = new ArrayList<>();
        for (int i = 1; i <= count; i++){
            String name = JOptionPane.showInputDialog(frame, "Enter name for Player "+ i + ":", "Player Setup", JOptionPane.QUESTION_MESSAGE);
            if (name == null || name.trim().isEmpty()){
                name = "Player" + i;
            }
        playerName.add(name);
        }
        
        scoreBoardPanel.removeAll();
        scoreBoardPanel.setLayout(new GridLayout(playerName.size(), 1, 5, 5));
        for (int i = 0; i < playerName.size(); i++){
            JLabel scores = new JLabel(playerName.get(i) + ": 0");
            scoreBoardPanel.add(scores);
        }
        scoreBoardPanel.revalidate();
        scoreBoardPanel.repaint();


        //JOptionPane.showMessageDialog(frame, "Game Start");
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

    public List<String> getPlayerName() {
        return playerName;
    }

    public JButton cardButtons(UnoModel.Card card) {
        JButton cardButton = new JButton(resizeImage(card.getFileName(), 150, 250));
        cardButton.setPreferredSize(new Dimension(150, 250));
        cardButton.setMaximumSize(new Dimension(150, 250));
        cardButton.setMinimumSize(new Dimension(150, 250));

        return cardButton;
    }

    public ImageIcon resizeImage(String file, int width, int height) {
        ImageIcon image = new ImageIcon(file);
        Image resize = image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resize);
    }

    public void addController(UnoController controller) {
        nextButton.addActionListener(controller);
        nextButton.setActionCommand("Next Player");
        drawButton.addActionListener(controller);
        drawButton.setActionCommand("Draw Card");
    }

    public static void main(String[] args) {
        UnoFrame frame = new UnoFrame();
        UnoModel model = new UnoModel();
        UnoView view = new UnoView(frame);
        UnoController controller = new UnoController(model, view, frame);

        frame.addController(controller);
        controller.play();
    }
}