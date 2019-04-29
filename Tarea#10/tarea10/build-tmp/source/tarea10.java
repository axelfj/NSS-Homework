import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class tarea10 extends PApplet {

Conway conway;

public void setup() {
  
  stroke(25);
  

  conway = new Conway(15);
  background(0);
}


public void draw() {
  conway.next();
  conway.display();
}

public void keyPressed() {
  if (key=='r' || key == 'R') {
    conway.restart();
  }
  if (key=='c' || key == 'C') {
    conway.clear();
  }
}
class Conway {
  int cellSize;

  float probabilityOfAliveAtStart = 15;

  int alive;
  int dead;

  int[][] cells; 
  int[][] cellsBuffer;

  Conway(int cellSize) {
    this.cellSize = cellSize;
    cells = new int[width/cellSize][height/cellSize];
    cellsBuffer = new int[width/cellSize][height/cellSize];
    for (int x=0; x<width/cellSize; x++) {
      for (int y=0; y<height/cellSize; y++) {
        float state = random (100);

        if (state > probabilityOfAliveAtStart) state = 0;
        else state = 1;

        cells[x][y] = PApplet.parseInt(state);
      }
    }
  }

  public void display() {
    for (int x=0; x<width/cellSize; x++) {
      for (int y=0; y<height/cellSize; y++) {
        alive = color(map(x,0,width/cellSize,0,255),map(y,0,height/cellSize,0,255),128);
        
        if (cells[x][y]==1) fill(alive);
        else fill(dead);

        rect (x*cellSize, y*cellSize, cellSize, cellSize);
      }
    }
  }

  public void next() { 

    for (int x=0; x<width/cellSize; x++) {
      for (int y=0; y<height/cellSize; y++) {
        cellsBuffer[x][y] = cells[x][y];
      }
    }

    for (int x=0; x<width/cellSize; x++) {
      for (int y=0; y<height/cellSize; y++) {

        int neighbours = aliveNeighbours(x,y); 

        if (cellsBuffer[x][y]==1 && (neighbours < 2 || neighbours > 3)) { 
            cells[x][y] = 0;
        } 
        else {   
          if (neighbours == 3 ) 
            cells[x][y] = 1;
        }
      }
    }
  }

  public int aliveNeighbours(int x, int y) {
    int neighbours = 0;
    for (int a = x-1; a <= x+1; a++) {
      for (int b = y-1; b <= y+1; b++) {  
        if (((a >= 0) && (a < width/cellSize)) && ((b >= 0) && (b < height/cellSize))) { 
          if (!((a == x) && (b == y))) { 
            if (cellsBuffer[a][b] == 1) {
              neighbours++;
            }
          }
        }
      }
    }
    return neighbours;
  }

  public void restart() {
    for (int x=0; x<width/cellSize; x++) {
      for (int y=0; y<height/cellSize; y++) {
        float state = random (100);
        if (state > probabilityOfAliveAtStart) {
          state = 0;
        } else {
          state = 1;
        }
        cells[x][y] = PApplet.parseInt(state); 
      }
    }
  }

  public void clear() {
    for (int x=0; x<width/cellSize; x++) {
      for (int y=0; y<height/cellSize; y++) {
        cells[x][y] = 0; 
      }
    }
  }
}
class ConwayCell {
  int state;

  int alive = color(0, 200, 125);
  int dead = color(0);

  ConwayCell(int state) {
    this.state = state;
  }

  public void display(int x, int y, int cellSize) {
    alive = color(map(x,0,width/cellSize,0,255),map(y,0,height/cellSize,0,255),128);
    if (state == 1) fill(alive);
    else fill(dead);
    
    rect(x*cellSize, y*cellSize, cellSize, cellSize);
  }

  public void setState(int state){
    this.state = state;
  }

  public int getState(){
    return this.state;
  }
}
  public void settings() {  size (1280, 720);  noSmooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "tarea10" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
