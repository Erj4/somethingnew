package com.xor;

import com.xor.tiles.Road;
import com.xor.tiles.Tile;
import com.xor.tiles.Building;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Map extends Canvas{
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

  public boolean build(Building building){
    if(tiles.size()==1) {
      Tile otherTile=tiles.peek();
      if(otherTile instanceof Building) tiles.add(new Road(otherTile.getX()-1, otherTile.getY()));
    }
    if(isFree(building)&&connect(building)){
      make(building);
      return true;
    }
    return false;
  }

  private void make(Tile toBuild){
    tiles.add(toBuild);
  }

  public boolean build(Iterable<Building> buildBatch){
    for(Building toBuild:buildBatch) if(!isFree(toBuild)) return false;
    for(Building toBuild:buildBatch) make(toBuild);
    return true;
  }

  private void make(Iterable<? extends Tile> buildBatch){
    for(Tile toBuild:buildBatch) if(isFree(toBuild)) make(toBuild);
  }

  public boolean isFree(Tile toBuild){
    for (Tile tile : tiles) {
      if(tile.getX()==toBuild.getX() && tile.getY()==toBuild.getY()) {
        System.out.println("("+toBuild.getX()+","+toBuild.getY()+") occupied by "+tile);
        make(toBuild);
        toBuild.setImage(Color.RED);
        return false;
      }
    }
    return true;
  }

  public boolean connect(Tile tile){
    ArrayDeque<Road> path = findRoute(tile); // Find 'best' route
    if(path==null) return false;
    make(path);
    return true;
  }

  private ArrayDeque<Road> findRoute(Tile tile){ // abusing Dijkstra's algorithm (really poorly)
    if(tiles.size()==0) return new ArrayDeque<Road>();

    int minX=tile.getX();
    int maxX=tile.getX();
    int minY=tile.getY();
    int maxY=tile.getY();
    for(Tile currentTile:tiles){
      if     (currentTile.getX()<minX) minX=currentTile.getX();
      else if(currentTile.getX()>maxX) maxX=currentTile.getX();
      if     (currentTile.getY()<minY) minY=currentTile.getY();
      else if(currentTile.getY()>maxY) maxY=currentTile.getY();
    }

    ArrayList<EmptySpace> unvisitedSpaces = new ArrayList<EmptySpace>();
    for(int x=minX-1; x<maxX+1; x++) for(int y=minY-1; y<maxY+1; y++){
      EmptySpace space = new EmptySpace(x, y);
      if(space.isFree()) unvisitedSpaces.add(space);
    }

    EmptySpace start = new EmptySpace(tile.getX(), tile.getY());
    start.distance=0;

    EmptySpace current = start;
    while(unvisitedSpaces.size()>0){ // Main loop
      for(EmptySpace unvisited:unvisitedSpaces){
        if(unvisited.isNeighbour(current) && (unvisited.distance==null||current.distance+1<unvisited.distance)) {
          unvisited.setPrevious(current, 1);
        }
      }

      EmptySpace nextSpace=null;
      for(EmptySpace potential:unvisitedSpaces){
        if(nextSpace==null){
          if(potential.distance!=null) nextSpace=potential;
        }
        else if (potential.distance!=null&&potential.distance<nextSpace.distance) nextSpace=potential;
      }

      for(Tile maybeEnd:tiles) {
        if(current.isNeighbouringRoad(maybeEnd)){
          ArrayDeque<Road> toBuild = new ArrayDeque<>();
          current.unravel(toBuild);
          return toBuild;
        }
      }

      current = nextSpace;
      unvisitedSpaces.remove(current);
    }
    return null;
  }

  private class EmptySpace{
    Integer distance=null;
    private EmptySpace previous;
    private int x;
    private int y;

    @Override
    public String toString(){
      return "distance="+distance+"\nx="+x+"\ny="+y;
    }

    public EmptySpace(int x, int y){
      this.x = x;
      this.y = y;
    }

    void unravel(ArrayDeque<Road> roadArray){
      if(distance!=0) {
        roadArray.add(new Road(x, y));
        previous.unravel(roadArray);
      }
    }

    void setPrevious(EmptySpace previous, int change){
      this.previous=previous;
      this.distance = previous.distance+change;
    }

    boolean isFree(){
      for(Tile tile:tiles){
        if(tile.getX()==x && tile.getY()==y) return false;
      }
      return true;
    }

    private boolean isNeighbour(EmptySpace other){
      boolean isXNeighbour = other.getX()==x+1||other.getX()==x-1;
      boolean isYNeighbour = other.getY()==y+1||other.getY()==y-1;
      return ((isXNeighbour&&other.getY()==y)||(isYNeighbour&&other.getX()==x));
    }

    private boolean isNeighbouringRoad(Tile tile){
      boolean isXNeighbour = tile.getX()==x+1||tile.getX()==x-1;
      boolean isYNeighbour = tile.getY()==y+1||tile.getY()==y-1;
      return ((isXNeighbour&&tile.getY()==y)||(isYNeighbour&&tile.getX()==x)) && (tile instanceof Road);

    }

    int getX(){
      return x;
    }
    int getY(){
      return y;
    }
  }

}
