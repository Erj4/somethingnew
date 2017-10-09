package com.xor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.xor.tiles.Tile;
import java.util.ArrayDeque;

class Map extends Canvas{
  Color BACKGROUND_COLOR = Color.DARKOLIVEGREEN;
  ArrayDeque<Tile> tiles = new ArrayDeque<Tile>();
  double tileSize;

  public Map(double tileSize, int width, int height){
    super(width, height);
    this.tileSize = tileSize;
  }

  private GraphicsContext gc(){
    return getGraphicsContext2D();
  }

  public void draw(){
    clear();
    for (Tile tile : tiles) {
      gc().drawImage(tile.getImage(), tile.getX()*tileSize, tile.getY()*tileSize, tileSize, tileSize);
    }
  }

  private void clear(){
    gc().setFill(BACKGROUND_COLOR);
    gc().fillRect(0, 0, getWidth(), getHeight());
  }

  public boolean build(Tile toBuild){
    if(isFree(toBuild)) {
      tiles.add(toBuild);
      return true;
    }
    return false;
  }

  public boolean isFree(Tile toBuild){
    for (Tile tile : tiles) {
      if(tile.getX()==toBuild.getX() && tile.getY()==toBuild.getY()) return false;
    }
    return true;
  }
}
