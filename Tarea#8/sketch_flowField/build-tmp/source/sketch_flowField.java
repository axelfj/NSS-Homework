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

public class sketch_flowField extends PApplet {

ArrayList<Vehicle> cars;
FlowField ff;

public void setup(){
  
  background(0);
  cars = new ArrayList();
  ff = new FlowField(35,0.5f,0.1f);
}
public void draw(){
  background(0);
  PVector mouse = new PVector(mouseX,mouseY);
  
  noCursor();
  noStroke();
  fill(255);
  ellipse(mouseX,mouseY,15,15);

  ff.update();
  for (Vehicle volswagen: cars){
    PVector f = ff.getForce(volswagen.pos.x,volswagen.pos.y);
    volswagen.applyForce(f);
    volswagen.arrive(mouse);
    volswagen.update();
    volswagen.display();
  }
  
  if (mousePressed){
    cars.add(new Vehicle(mouseX, mouseY,new PVector(0,0)));
  }   
  if (keyPressed && key == 'c') ff.display();
}
class Vehicle{
  PVector pos, vel, acc;
  float mass, size, maxSpeed, maxForce, arrivalRadius; 
  int c;
  
  Vehicle(float x, float y, PVector vel){
    pos = new PVector(x,y);
    this.vel = vel;
    acc = new PVector(0,0);
    mass = 1;
    size = random(3,7);
    maxSpeed = random(2,3);
    maxForce = random(0.08f,0.02f);
    arrivalRadius = 200;    
    colorMode(HSB);
    c = color(frameCount % 255, 255, 255);
    colorMode(RGB);
  }
  
  public void update(){
    vel.add(acc);
    vel.limit(maxSpeed);
    pos.add(vel);
    pos.x = (pos.x + width)%width;
    pos.y = (pos.y + height)%height;
    acc.mult(0);
  }
  
  public void applyForce(PVector force){
    PVector f = PVector.div(force,mass);
    acc.add(f);
  }
  
  public void display(){
    float ang = vel.heading();
    //stroke(255);
    //strokeWeight(.5);
    //c = color(map(pos.x,0,width,0,255),map(pos.y,0,height,0,255),128);
    fill(c);
    noStroke();
    pushMatrix();
    translate(pos.x,pos.y);
    rotate(ang);
    beginShape();
    vertex(size * 3, 0);
    vertex(0, -size);
    vertex(0, size);
    endShape(CLOSE);
    popMatrix();
  }
  
  public void seek(PVector target){
    PVector desired = PVector.sub(target,pos);
    desired.setMag(maxSpeed);
    PVector steering = PVector.sub(desired,vel);
    steering.limit(maxForce);
    applyForce(steering);
  }
  
  public void arrive(PVector target){
    float d = PVector.dist(pos,target);
    d = constrain(d,0,arrivalRadius);
    float speed = map(d, 0, arrivalRadius, 0, maxSpeed);
    vel.setMag(speed);
    seek(target);
  }
}
class FlowField{
  PVector[][] grid;
  float resolution;
  int rows, columns;
  float noiseOffset = 0.00000300f;
  float defaultMag = 5;
  
  FlowField(float resolution, float noiseOffset, float defaultMag){
    this.resolution = resolution;
    this.noiseOffset = noiseOffset;
    this.defaultMag = defaultMag;
    rows = (int)(height/resolution)+1;
    columns = (int)(width/resolution)+1;
    
    grid = new PVector[rows][];
    for (int r = 0; r < rows; r++){
      grid[r] = new PVector[columns];
      for (int c = 0; c < columns; c++){
        float noiseVal = noise(
                          (float)r * noiseOffset,
                          (float)c * noiseOffset);
        float angle = map(noiseVal,0,1,-TWO_PI,TWO_PI) + frameCount/180;
        grid[r][c] = PVector.fromAngle(angle);
        grid[r][c].setMag(defaultMag);
        noiseOffset += 0.000003f;
      }
    }
  }
  
  public PVector getForce(float x, float y){
    if (x >= 0 && x <= width && y >= 0 && y <= height){
      int r = (int)(y / resolution);
      int c = (int)(x / resolution);
      return grid[r][c];
    }
    return new PVector(0,0);
  }
  
  public void displayVector(float column, float row, PVector vector){
    PVector copy = vector.copy();
    float midResolution = resolution/2;
    copy.setMag(midResolution);
    pushMatrix();
    stroke(color(map(column,0,width,0,255),map(row,0,height,0,255),map(vector.heading(),-TWO_PI,TWO_PI,0,255)));
    strokeWeight(.5f);
    translate(column + midResolution, row + midResolution);
    line(0,0,copy.x,copy.y);
    popMatrix();
  }
  
  public void display(){
    for (int r = 0; r < rows; r++)
      for (int c = 0; c < columns; c++)
        displayVector(c * resolution, r * resolution, grid[r][c]);
  }

  public void update(){
    for (int r = 0; r < rows; r++){
      for (int c = 0; c < columns; c++){
        float noiseVal = noise(
                          (float)r * noiseOffset,
                          (float)c * noiseOffset);
        float angle = map(noiseVal,0,1,-TWO_PI,TWO_PI) + frameRate/180;
        grid[r][c] = PVector.fromAngle(angle);
        grid[r][c].setMag(defaultMag);
        noiseOffset += 0.00000020f;
      }
    }
  }
}
  public void settings() {  size(1280,720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_flowField" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
