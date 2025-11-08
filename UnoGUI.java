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
    frame.setLayout(new BorderLayout(10, 10);

    //top panel
    JPanel infoPanel = new JPanel(new GridLayout(2,1));
    currentPlayerLabel = new JLabel("Current Player: ", JLabel.CENTER);
    infoPanel.add(currentPlayerLabel);
    topCardPanel = new JPanel();
    topCardLabel = new JLabel("Top Card: ");
    topCardPanel.add(topCardLabel);
    infoPanel.add(topCardPanel);

    frame.add(infoPanel, BorderLayout.NORTH);
  }
    
    
  
}
