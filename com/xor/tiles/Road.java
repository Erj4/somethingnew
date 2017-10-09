package com.xor.tiles;

import java.util.Deque;
import javafx.scene.paint.Color;
public class Road extends Tile{
  static Color color = Color.DARKGRAY;

  public Road(int x, int y){
    super(x, y, color);
  }

  public Deque<Road> connect(Tile tile, Map map); // should connect building by a series of road tiles to rest of city
}
