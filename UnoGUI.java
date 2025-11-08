import javax.swing.*;
import java.awt.*;
import java.util.List*;

public class UnoGUI {
  private JFrame frame;
  private JPanel topCardPanel;
  private JPanel handPanel;
  private JPanel controlPanel;
  private JLabel currentPlayerLabel;
  private JLabel topCardLabel;
  private JTextArea messageArea;
  private JButton drawButton;
  private JButton nextButton;
  

  public UnoGUI () {
    initializeGUI();
  }
  private void initializeGUI() {
    frame = new JFrame ("UNO Game");
    frame.setSize (900, 700);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout(10, 10));

    //top panel
    JPanel infoPanel = new JPanel(new GridLayout(2,1));
    currentPlayerLabel = new JLabel("Current Player: ", JLabel.CENTER);
    infoPanel.add(currentPlayerLabel);
    topCardPanel = new JPanel();
    topCardLabel = new JLabel("Top Card: ");
    topCardPanel.add(topCardLabel);
    infoPanel.add(topCardPanel);

    frame.add(infoPanel, BorderLayout.NORTH);
    
    // Control panel
    controlPanel = new JPanel(new BorderLayout()));
    
    // Button panel
    JPanel buttonPanel = new JPanel();
    drawButton = new JButton("Draw Card");
    nextButton = new JButton("Next Player");
    buttonPanel.add (drawButton); 
    buttonPanel.add (nextButton);
    controlPanel.add (buttonPanel, BorderLayout.NORTH);

    // Message area
    messageArea = new JTextArea (8, 50);
    messageArea.setEditable (false); 
    messageArea.setLineWrap(true); 
    messageArea.setWrapStyleWord (true);

    ScrollPane messageScroll = new JScrollPane (messageArea);
    controlPanel.add(messageScroll, BorderLayout.CENTER);
    frame.add (controlPanel, BorderLayout.SOUTH);
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
    return draw Button; 
  }
  public JButton getNextButton() { 
    return nextButton; 
  }
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(frame, message);
  }
  public String colourSelectionDialog() {
    String[] colours = {"RED", "YELLOW", "GREEN", "BLUE");
    return (String) JOptionPane.showInputDialog(frame, "Choose new colour for Wild Card:", "Wild Card Colour", JOptionPane.PLAIN_MESSAGE, null, colours, colours[O]);
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

}
