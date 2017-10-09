package com.xor;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.xor.tiles.Building;
import com.xor.tiles.Road;

public class Main extends Application {
  public static void main(String[] args) {
      launch(args);
  }

  public void start(Stage stage){
    stage.setScene(createScene());

    stage.show();
  }

  private Scene createScene(){
    Map map = new Map(10, 600, 400);
    map.build(new Building(30, 2));
    map.build(new Building(10, 20));
    map.build(new Building(20, 23));
    map.build(new Building(16, 10));
    map.draw();
    HBox root = new HBox(map);
    return new Scene(root);
  }
}
