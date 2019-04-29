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

public class circleMatrix extends PApplet {

// size variables //
float zOffs = 0;
float size = random(10);

// place variables //
float zPlace = 0;
float place = random(1000);

// color variables //
float zOffr = 255;
float zOffg = 255;
float zOffb = 255;

int xValue = 8;
int yValue = 5;

public void setup() {
  //fullScreen();
  
  background(0);
}

public void draw() {
  background(0);
  float xOffs = 0;
  float xOffp = 0;
  for (float x = -25; x < width+25; x+=xValue) {
    float yOffs = 0;
    float yOffp = 0;
    for (float y = -25; y < height+25; y+=yValue) { 
      float a = map(noise(xOffs, yOffs, zOffr), 0, 1, 0, 255);
      float b = map(noise(xOffs, yOffs, zOffg), 0, 1, 0, 255);
      float c = map(noise(xOffs, yOffs, zOffb), 0, 1, 0, 255);
      stroke(a, b, c);

      size = map(noise(xOffs, yOffs, zOffs), 0, 1, 0, 17);
      strokeWeight(size);

      place = map(noise(xOffp, yOffp, zPlace), 0, 1, -50, 50);
      point(x+place, y+place);
      noFill();
      yOffs += 0.00786f;
      yOffp += 0.00786f;


      if (keyPressed && (key == 'c')) {
        zOffr = random(255);
        zOffg = random(255);
        zOffb = random(255);
      } else if (keyPressed && (key == 's')) {
        zOffs = random(255);
      } 
      
    }
    zOffs += .00025f;
    zPlace += .0003f;
    xOffs += .01f;
    xOffp += .01f;
  }
  zOffs += .000093f;
  zOffr += .005f;
  zOffg += .005f;
  zOffb += .005f;
}
  public void settings() {  size(600, 420); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "circleMatrix" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
