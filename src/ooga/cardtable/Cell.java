package ooga.cardtable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Cell implements ICell {
  private IDeck deck;
  private String name;
  private Function<IDeck, IDeck> cellDeckBuilder;
  //TODO: HULLOO SHOULD CHANGE TO IDECK AND ICELL
  private Map<IOffset, ICell> children;

  public Cell(String nm) {
    name = nm;
    deck = new Deck();
  }

  //TODO: ASSUMES NONE OFFSET?
  //TODO: LAMBDA SHOULD CALL THIS'S ADD CARD NOT DECK'S?
  public Cell(String nm, Deck d) {
    this(nm);
    deck = d;
  }

  @Override
  public void setDraw(Function<IDeck, IDeck> initializer) {
    cellDeckBuilder = initializer;
  }

  @Override
  public void initializeCards(IDeck mainDeck) {
    if (cellDeckBuilder!= null) {
      deck = cellDeckBuilder.apply(mainDeck);
    }
    cellDeckBuilder = null;
  }

  @Override
  public IDeck getDeck() {
    return deck;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Map<IOffset, ICell> getAllChildren() { //fixme do a proper copy
    Map<IOffset, ICell> ret = new HashMap<>(children);
    ret.put(Offset.NONE, this);
    return ret;
  }

  @Override
  public Map<IOffset, ICell> getHeldCells() {
    return getAllChildren();
  }

  @Override
  public void addCard(IOffset offset, ICard card) {
    if (offset.equals(Offset.NONE)) {
      deck.addCard(card);
      return;
    }
    children.putIfAbsent(offset, new Cell(getName()+","+offset.getOffset())); //fixme namespace shenanigans
    children.get(offset).addCard(Offset.NONE, card);
  }
}
