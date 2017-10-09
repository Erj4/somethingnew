package com.xor.tiles;

import java.util.ArrayDeque;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
public abstract class Tile{
  int x;
  int y;
  Color image;

  public Tile(int x, int y, Color image){
    this.x = x;
    this.y = y;
    this.image = image;
  }

  public Image getImage(){ // Lazy hack, replace with an actual image at some point
    WritableImage image = new WritableImage(1, 1);
    image.getPixelWriter().setColor(0, 0, this.image);
    return image;
  }

  public int getX(){
    return x;
  }

  public int getY(){
    return y;
  }
}
