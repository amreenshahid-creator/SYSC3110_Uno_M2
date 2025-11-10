/**
 * @version 
 * JUnit testings for Milestone 2
 * 
 */
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
public class UnoModelTest{
  private UnoModel model;
  /**creates a game with 2 players John and Mark */
  @Before
  public void setUp(){
    model = new UnoModel();
    model.addPlayer("John");
    model.addPlayer("Mark");
  }
  /**Checks random card generator */
  @Test
  public void testGetRandomCard(){
    for (int i = 0; i < 20; i++) {
      UnoModel.Card card = model.getRandomCard();
      assertNotNull(card);
      assertNotNull(card.getValue());
      if (card.getValue() != UnoModel.Values.WILD &&
          card.getValue() != UnoModel.Values.WILD_DRAW_TWO) {
        assertNotNull(card.getColour());
      }
    }
  }
   /** sets a new top on pile by removing played card from player thats currently playing*/
  @Test
  public void testPlayCardTop(){
    model.newRound();
    UnoModel.Player player = model.getCurrPlayer();
    UnoModel.Card first = player.getPersonalDeck().get(0);
    int before = player.getPersonalDeck().size();
    model.playCard(first);
    assertEquals(before - 1, player.getPersonalDeck().size());
    assertEquals(first, model.getTopCard());
}
  /** New round that deals with 7 cards per player */
  @Test
  public void testNewRoundSevenCards(){
    model.addPlayer("Lina");
    model.newRound();
    assertEquals(7, model.getCurrPlayer().getPersonalDeck().size());
    model.advance();
    assertEquals(7, model.getCurrPlayer().getPersonalDeck().size());
    model.advance();
    assertEquals(7, model.getCurrPlayer().getPersonalDeck().size());
    UnoModel.Card top = model.getTopCard();
    assertNotNull(top);
    assertTrue(top.getValue() != UnoModel.Values.WILD &&
               top.getValue() != UnoModel.Values.WILD_DRAW_TWO);
  }
  
  /** drawCard adds one card to current player*/
  @Test
  public void testDrawCardAddsOne(){
    model.newRound();
    int before = model.getCurrPlayer().getPersonalDeck().size();
    model.drawCard();
    assertEquals(before + 1, model.getCurrPlayer().getPersonalDeck().size());
  }
  /** drawOne is used to add one card to upcoming player */
  @Test
  public void testDrawOneNextPlayer(){
    model = new UnoModel();
    model.addPlayer("John");
    model.addPlayer("Mark");
    model.newRound();
    UnoModel.Card c = model.drawOne();
    assertNotNull(c);
    model.advance();
    assertEquals(8, model.getCurrPlayer().getPersonalDeck().size());
  }
  /** reverse and skip affect*/
  @Test
  public void testSkipAfterReverse(){
    model = new UnoModel();
    model.addPlayer("A");
    model.addPlayer("B");
    model.addPlayer("C");
    model.newRound();
    assertEquals("A", model.getCurrPlayer().getName());
    UnoModel normalGame = new UnoModel();
    normalGame.addPlayer("A");
    normalGame.addPlayer("B");
    normalGame.addPlayer("C");
    normalGame.newRound();
    normalGame.skip();
    assertEquals("C", normalGame.getCurrPlayer().getName());
    model.reverse(); // reverse 
    model.skip(); //skip
    assertEquals("B", model.getCurrPlayer().getName());
  }
  /**this test checks who plays after current player */
  @Test
  public void testNextPlayer(){
    model = new UnoModel();
    model.addPlayer("A");
    model.addPlayer("B");
    model.addPlayer("C");
    model.newRound();
    assertEquals("B", model.getNextPlayer().getName());
    model.reverse();
    assertEquals("C", model.getNextPlayer().getName());
    model.advance();
    assertEquals("B", model.getNextPlayer().getName());
  }
  /** wild sets the top card colour */
  @Test
  public void testWildSetsColour(){
    model.newRound();
    model.wild(UnoModel.Colours.RED);
    assertEquals(UnoModel.Colours.RED, model.getTopCard().getColour());
  }
  /** sets colour and gives two cards to upcoming player */
  @Test
  public void testWildDrawTwoAddsCards(){
    model = new UnoModel();
    model.addPlayer("John");
    model.addPlayer("Mark");
    model.newRound();
    model.advance();
    int markStart = model.getCurrPlayer().getPersonalDeck().size();
    model = new UnoModel();
    model.addPlayer("John");
    model.addPlayer("Mark");
    model.newRound();
    List<UnoModel.Card> drawn = model.wildDrawTwo(UnoModel.Colours.GREEN);
    assertNotNull(drawn);
    assertEquals(2, drawn.size());
    assertEquals(UnoModel.Colours.GREEN, model.getTopCard().getColour());
    model.advance();
    assertEquals(markStart + 2, model.getCurrPlayer().getPersonalDeck().size());
  }

 /***
 *This test is for checking if cards are playable based on rules of Uno,
 *It tests if cards same colour, same value, wild cards, and any false case.
 */
  @Test
  public void testPlayableCard(){
    model.newRound();
    UnoModel.Card top = model.getTopCard();
    UnoModel.Card sameColour = new UnoModel.Card(top.getColour(), UnoModel.Values.FIVE);
    assertTrue(model.isPlayable(sameColour));
    UnoModel.Colours otherColour;
    if (top.getColour() == UnoModel.Colours.RED){
        otherColour = UnoModel.Colours.BLUE;
    }else{
        otherColour = UnoModel.Colours.RED;
    }
    UnoModel.Card sameValue = new UnoModel.Card(otherColour, top.getValue());
    assertTrue(model.isPlayable(sameValue));
    UnoModel.Card wild = new UnoModel.Card(null, UnoModel.Values.WILD);
    assertTrue(model.isPlayable(wild));
    UnoModel.Values diffValue;
    if (top.getValue() == UnoModel.Values.ONE){
        diffValue = UnoModel.Values.TWO;
    }else{
        diffValue = UnoModel.Values.ONE;
    }
    UnoModel.Card neither = new UnoModel.Card(otherColour, diffValue);
    boolean differsColour = (top.getColour() == null) || (otherColour != top.getColour());
    boolean differsValue  = diffValue != top.getValue();
    if (differsColour && differsValue) {
        assertFalse(model.isPlayable(neither));
    }
}
  /** advance function moves to next player */
  @Test
  public void testAdvanceNextPlayer(){
    model.addPlayer("Max");
    model.newRound();
    String first = model.getCurrPlayer().getName();
    model.advance();
    String second = model.getCurrPlayer().getName();
    assertNotEquals(first, second);
  }
  /** score of equivalent to 71 in assert Equals*/
  @Test
  public void testScoreTotal(){
    model.newRound();
    UnoModel.Player winner = model.getCurrPlayer();
    model.advance();
    UnoModel.Player opponent = model.getCurrPlayer();
    opponent.getPersonalDeck().clear();
    opponent.getPersonalDeck().add(new UnoModel.Card(UnoModel.Colours.RED, UnoModel.Values.ONE));
    opponent.getPersonalDeck().add(new UnoModel.Card(UnoModel.Colours.YELLOW, UnoModel.Values.SKIP));
    opponent.getPersonalDeck().add(new UnoModel.Card(null, UnoModel.Values.WILD));
    int score = model.getScore(winner);
    assertEquals(71, score);
  }
  /** the test checks if a player has no cards left*/
  @Test
  public void testIsDeckEmpty(){
    model.newRound();
    assertFalse(model.isDeckEmpty());
    model.getCurrPlayer().getPersonalDeck().clear();
    assertTrue(model.isDeckEmpty());
    model.drawCard();
    assertFalse(model.isDeckEmpty());
  }
  /** the test for set top cards*/
  @Test
  public void testSetTopCard(){
    model.newRound();
    UnoModel.Card chosen = new UnoModel.Card(UnoModel.Colours.BLUE, UnoModel.Values.THREE);
    model.setTopCard(chosen);
    assertEquals(chosen, model.getTopCard());
  }  
}
