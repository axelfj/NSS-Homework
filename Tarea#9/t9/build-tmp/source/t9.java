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

public class t9 extends PApplet {

// los puntos del path están quemados para un mejor uso //
// solo se pueden crear carros que utilicen la ruta ya creada //


PVector mouse = new PVector(mouseX,mouseY);
ArrayList<Vehicle> cars;
Path p;

public void setup(){
  
  //fullScreen();
  background(0);
  //blendMode(ADD);
  cars = new ArrayList();
  
  p = new Path();
  p.addPoint(width/2-250,height/2-250);
  p.addPoint(width/2+250,height/2-250);
  p.addPoint(width/2+450,height/2-150);
  p.addPoint(width/2+450,height/2+150);
  p.addPoint(width/2+250,height/2+250);
  p.addPoint(width/2-250,height/2+250);
  p.addPoint(width/2-450,height/2+150);
  p.addPoint(width/2-450,height/2-150);
  p.addPoint(width/2-250,height/2-250); 
  
}
public void draw(){
  background(45);
  
  noCursor();
  ellipse(mouseX,mouseY,5,5);
  noStroke();
  p.display();
  
  for (Vehicle volswagen: cars){
    volswagen.followPath(p);
    volswagen.separate(cars);
    volswagen.align(cars);
    volswagen.cohere(cars);
    volswagen.update();
    volswagen.display();
  }

  if (mousePressed) 
    cars.add(new Vehicle(mouseX, mouseY, PVector.random2D()));
}
class Path{
  float radious;
  ArrayList<PVector> points;
  
  Path(){
    radious = 15;
    points = new ArrayList();
  }
  
  public void addPoint(float x, float y){
    this.points.add(new PVector(x,y));
  }
  
  public ArrayList<Segment> getSegments(){
    ArrayList<Segment> segments = new ArrayList();
    for (int i = 0; i < points.size() -1; i++){
      PVector start = points.get(i);
      PVector end = points.get(i+1);
      segments.add(new Segment(start,end));
    }
    return segments;
  }
  
  public void display(){
    for (int i = 0; i < points.size() -1 ; i++){
      PVector start = points.get(i);
      PVector end = points.get(i+1);
      strokeWeight(radious * 3);
      stroke(200,45);
      line(start.x,start.y,end.x,end.y);
    }
    for (int i = 0; i < points.size() -1; i++){
      PVector start = points.get(i);
      PVector end = points.get(i+1);
      strokeWeight(0.1f);
      stroke(255);
      line(start.x,start.y,end.x,end.y);
    }
  }
}
class Segment{
  
  PVector start;
  PVector end;
  
  Segment(PVector start, PVector end){
    this.start = start;
    this.end = end;
  }

  public boolean contains(PVector point){
   if (((point.x >= start.x && point.x <= end.x) || (point.x >= end.x && point.x <= start.x)) && 
       ((point.y >= start.y && point.y <= end.y) || (point.y >= end.y && point.y <= start.y)))
         return true;
    return false;
  }
}
class Vehicle{
  PVector pos, vel, acc;
  float mass, size, maxSpeed, maxForce, arrivalRadius; 
  int c;
  boolean debug = false;
  float lookAhead = 50;
  float maxPathDistance = 200;
  
  float separationDistance = 15;
  float separationRatio = 3;
  
  float alignmentDistance = 10;
  float alignmentRatio = 0.25f;
  
  float cohesionDistance = 1;
  float cohesionRatio = .01f;

  Vehicle(float x, float y, PVector vel){
    pos = new PVector(x,y);
    this.vel = vel;
    acc = new PVector(0,0);
    mass = 1;
    size = 6;
    maxSpeed = 3;
    maxForce = 0.1f;
    arrivalRadius = 200;
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
    colorMode(HSB);
    c = color(frameCount % 255, 255, 255);
    colorMode(RGB);
    fill(c);
    pushMatrix();
    ellipse(pos.x, pos.y, size, size);
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
  
  public void followPath(Path path){
    PVector predictedPos = getPredictedPos();
    ArrayList<PVector> normalPoints = getNormalPoints(predictedPos, path);
    PVector normalPos = closest(predictedPos, normalPoints);
    float distance = PVector.dist(predictedPos,normalPos);
    if (distance > path.radious) {
      seek(normalPos);
    }
    if (debug){
      noStroke();
      fill(0,0,255);
      ellipse(predictedPos.x,predictedPos.y,5,5);
      fill(0,255,0);
      ellipse(normalPos.x,normalPos.y,5,5);
    }
  }
  
  public PVector getPredictedPos(){
    PVector predictedPos = vel.copy();
    predictedPos.setMag(lookAhead);
    predictedPos.add(pos);
    return predictedPos;
  }
  
  public ArrayList<PVector> getNormalPoints(PVector predictedPos, Path path){
    ArrayList<PVector> normalPoints = new ArrayList();
    for (Segment s: path.getSegments()){
      PVector start,end;
      if (s.start.x < s.end.x){
        start = s.start;
        end = s.end;
      }
      else {
        start = s.end;
        end = s.start;
      }
      PVector a = PVector.sub(predictedPos,s.start);
      PVector b = PVector.sub(s.end,s.start);
      b.normalize();
      float dot = a.dot(b);
      b.setMag(dot);
      b.add(s.start);
      if (s.contains(b))
        normalPoints.add(b);
    }
    return normalPoints;
  }
  
  public PVector closest(PVector predicted, ArrayList<PVector> points){
    if (points.isEmpty()) return predicted;
    PVector closestPoint = points.get(0);
    for (int i = 1; i < points.size();i++){
      if (predicted.dist(points.get(i)) < predicted.dist(closestPoint))
        closestPoint = points.get(i);
    }
    if (predicted.dist(closestPoint) < maxPathDistance) return closestPoint;
    else return predicted;
  }

  public void separate(ArrayList<Vehicle> vehicles) {
    PVector average = new PVector(0, 0);
    int ammount = 0;
    for (Vehicle v : vehicles) {
      float d = PVector.dist(pos, v.pos);
      if (this != v && d < separationDistance) {
        PVector difference = PVector.sub(pos, v.pos);
        difference.normalize();
        difference.div(d);
        average.add(difference);
        ammount++;
      }
    }
    if (ammount > 0) {
      average.div(ammount);
      average.mult(separationRatio);
      average.limit(maxSpeed);
      applyForce(average);
    }
  }
  
  public void align(ArrayList<Vehicle> vehicles){
    PVector average = new PVector(0, 0);
    int ammount = 0;
    for (Vehicle v : vehicles) {
      float d = PVector.dist(pos, v.pos);
      if (this != v && d < alignmentDistance) {
        average.add(v.vel);
        ammount++;
      }
    }
    if (ammount > 0) {
      average.div(ammount);
      average.mult(alignmentRatio);
      average.limit(maxSpeed);
      applyForce(average);
    }
  }
  
  public void cohere(ArrayList<Vehicle> vehicles){
    PVector average = new PVector(0, 0);
    int ammount = 0;
    for (Vehicle v : vehicles) {
      float d = PVector.dist(pos, v.pos);
      if (this != v && d < cohesionDistance) {
        average.add(v.pos);
        ammount++;
      }
    }
    if (ammount > 0) {
      average.div(ammount);
      PVector force = average.sub(pos);
      force.mult(cohesionRatio);
      force.limit(maxSpeed);
      applyForce(force);
    }
  }
}
  public void settings() {  size(1280,720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "t9" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
