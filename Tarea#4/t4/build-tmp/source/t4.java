import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Iterator; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class t4 extends PApplet {

// se poseen controles dedicados para controlar gravedad y fuerza

// para aplicar el viento hacia los lados 
// se debe hacer con 'a' (izq) y 'd' (der)
// esta fuerza posee controles dedicados
// windR corresponde a la fuerza que se aplica desde la derecha
// windL sería la fuerza desde la izquierda
// cuando los cubos están dentro del agua no los afecta el viento

// el color de los cubos depende de una función noise en el
// modo HSB

// el movimiento del agua esta hecho con un noise //

// el control del 'cañon' que dispara los cubos depende de la
// posición del mouse en ancho y altura de la pantalla

// para no sobrecargar cuando un cubo sale de pantalla, se
// elimina de la lista



ControlP5 cp5;
ArrayList<Agent> agents;
PVector gravity,windR,windL,mouse,cannon, dif, fDrag;

float yOff = 0;

float xColor = 0;
float yColor = 100;
float zColor = 200;

float start = 0;
float force = 0.4f;
float water = 1.25f;
float cDrag = -7;
int c;

PImage wp;

public void setup(){
  
  wp = loadImage("wp.jpg");
  wp.resize(width,height);
  background(wp);

  agents = new ArrayList();
  gravity = new PVector(0,0.5f);
  windL = new PVector(1,0);
  windR = new PVector(-1,0);

  noCursor();
  initControls();
}

public void draw(){ 
  displayBackground();
  killBullets();
  
  dragThem();
  frictThem();
  
  if (mousePressed){
    if (mouseButton == RIGHT){
      colorMode(HSB,360,100,100);
      c = color(
              map(noise(xColor),0,1,0,360),
              map(noise(yColor),0,1,0,100),
              map(noise(zColor),0,1,0,100));
      Agent a = new Agent(cannon.x+dif.x,cannon.y+dif.y-25,c);
      agents.add(a);
      a.vel.x = dif.x*force;
      a.vel.y = dif.y*force;
      xColor += 0.009f;
    }
    yColor+=0.007f;
    zColor+=0.005f;
  }

  if (keyPressed){
    if (key == 'a'){
      for (Agent a : agents) 
        if (a.pos.y < height/water)
          a.addForce(windR);
    } else if (key == 'd'){
      for (Agent a : agents) 
        if (a.pos.y < height/water)
          a.addForce(windL);
    } else if(key == 'r'){
      for (Agent a : agents)
        a.addForce(new PVector(0,0));
    } 
  }
}

public void dragThem(){
  for (Agent a : agents){
    a.display();
    PVector g = PVector.mult(gravity, a.mass);
    a.addForce(g);
    if (a.pos.y > height/water){
      cDrag = -7;
      PVector drag = a.vel.copy();
      drag.normalize();
      drag.mult(a.vel.mag());
      drag.mult(cDrag);
      a.addForce(drag);
      fDrag = new PVector(0,-9);
      a.addForce(fDrag);
    }
    a.update();
  }
}

public void frictThem(){
  for (Agent a : agents){

  }
}

public void killBullets(){
  Iterator<Agent> i = agents.iterator();
  while (i.hasNext()){
    Agent a = i.next();
    if (a.pos.y > height+50) i.remove();
  }
}

public void displayBackground(){  
  displayRect();
  displayCannon();
}

public void displayRect(){
  background(wp);
  colorMode(HSB,360,100,100);
  stroke(190,100,100);
  strokeWeight(5);
  yOff = start;
  for (float x = 0; x < width; x+=5){
    float y = map(
                noise(yOff),
                0,1,
                height/(water-.07f),height/(water+.05f));
    line(x,y,x,height);
    yOff+=0.009f;
  }
  start+=0.01f;
}

public void eraseAgents(){
  Iterator<Agent> i = agents.iterator();
  while (i.hasNext()){
    Agent a = i.next();
    i.remove();
  }
}

public void displayCannon(){
  mouse = new PVector(mouseX,mouseY);
  cannon = new PVector(width/2,height/water);
  noStroke();
  colorMode(RGB,255);
  fill( map(mouseX,0,width,0,255),
        map(mouseY,0,height,0,255),
        128);
  ellipse(mouse.x,mouse.y,10,10);
  dif = PVector.sub(mouse,cannon);
  
  pushMatrix();
  translate(cannon.x,cannon.y);
  dif.setMag(50);
  fill(255);
  colorMode(RGB,255);
  strokeWeight(50);
  stroke( map(mouseX,0,width,0,255),
          map(mouseY,0,height,0,255),
          128);
  line(0,0,dif.x,dif.y);
  popMatrix();
}


public void initControls(){
  cp5 = new ControlP5(this);

  cp5.addSlider("force")
    .setPosition(20,10)
    .setSize(100,20)
    .setRange(0.01f,1)
    .setValue(0.4f)
    .setColorLabel(color(0));

  cp5.addSlider("gravity")
    .setPosition(20,40)
    .setSize(100,20)
    .setRange(0.01f,5)
    .setValue(0.5f)
    .setColorLabel(color(0));

  cp5.addSlider("windL")
    .setPosition(170,10)
    .setSize(100,20)
    .setRange(1,10)
    .setValue(1)
    .setColorLabel(color(0));

  cp5.addSlider("windR")
    .setPosition(170,40)
    .setSize(100,20)
    .setRange(-10,-1)
    .setValue(-1)
    .setColorLabel(color(0));
}

public void gravity(float y){
  gravity.y = y;
}

public void windL(float x){
  windL.x = x;
}

public void windR(float x){
  windR.x = x;
}
class Agent {
  PVector pos;
  PVector vel;
  PVector acc;
  float ang,velAng,accAng, mass;
  int c;
  float des = random(6,10);
  float med = random(20,25);

  Agent(float x, float y, int c){
    this(x, y, random(20,25), c);
  }
  
  Agent(float x, float y, float m, int c){
    pos = new PVector(x,y);
    vel = new PVector(0,0);
    acc = new PVector(0.0f, 0);
    ang = random(TWO_PI);
    velAng = 0.5f;
    accAng = 0;
    this.c = c;
    float m1 = randomGaussian() * des + med;
    this.mass = m1;
  }
  
  public void display(){
    noStroke();
    //c = color(noise(),noise(),noise());
    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    rectMode(CENTER);
    rect(0,0,mass*2/3, mass*3/2);
    rectMode(CORNER);
    popMatrix();
  }
  
  public void update(){
    pos.add(vel);
    vel.add(acc);
    acc.mult(0);   
    if (pos.y > height/1.25f) ang += 0.00001f;
    else ang += 0.03f;
  }
  
  public void addForce(PVector force){
    PVector f = PVector.div(force, mass);
    acc.add(f);
  }
  
}
  public void settings() {  size(1280,900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "t4" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
