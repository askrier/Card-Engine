package ooga.cardtable;

import java.util.Arrays;

public class Suit implements ISuit {
  private int[] color;
  private String name;

  public Suit(String nm, int[] c) {
    if (c.length != 3) {
      throw new IllegalArgumentException("color not 3 long"); //fixme
    }
    name = nm;
    color = Arrays.copyOf(c, c.length);
  }

  @Override
  public int[] getColor() {
    return color;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getType() {
    return "suit";
  }
}