import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class t3 extends PApplet {

// se poseen sliders dedicados //

// si se aumenta el nivel de atracción, se acercan al mouse
// con más velocidad

// si se disminuye el nivel de repulsión, los agentes se alejarán
// más rapido de los otros

// la estrella creada posee colores //
// la estrella creada puede variar su tamaño //

// si hace click con el botón derecho del mouse crea más agentes


ArrayList<Agent> agents;
ControlP5 cp5;
float G,d;
PVector r;
int bg, sunColor;
float attraction, repelant, sunMass;

public void setup(){
  
  background(0);
  agents = new ArrayList();
  for (int i = 0; i < 500; i ++){
    agents.add(new Agent(random(width), random(height)));
  }
  sunColor = 0xffFDB813;
  attraction = 1;
  repelant = -15;
  sunMass = 50;
  noCursor();
  initControls();
}

public void mousePressed(){
  if (mouseButton == RIGHT) 
    agents.add(new Agent(mouseX, mouseY));
}

public void draw(){
  fill(bg,25);
  ellipse(width/2,height/2,width,height);

  for(Agent a1 : agents){
    for (Agent a2: agents){
      if(a1 != a2){
        G = repelant/1000;
        r = PVector.sub(a2.pos, a1.pos);
        d = r.magSq();
        d = constrain(d, 1, 3000);
        r.normalize();
        r.mult(G * a1.mass * a2.mass);
        r.div(d);
        a1.addForce(r);
      }
    }
    G = attraction/100;
    Agent sun = new Agent(mouseX, mouseY, 100);
    fill(sunColor);
    ellipse(mouseX,mouseY,sunMass,sunMass);
    r = PVector.sub(sun.pos, a1.pos);
    d = r.magSq();
    d = constrain(d, 1, 15);
    r.normalize();
    r.mult(G * a1.mass * sun.mass);
    r.div(d);
    a1.addForce(r);
  }

  for(Agent a: agents){
    a.update();
    a.display();
  }
}  

public void initControls(){
  cp5 = new ControlP5(this);

  cp5.addSlider("attraction")
    .setPosition(20,10)
    .setSize(100,20)
    .setRange(1,5)
    .setValue(1);
    
  cp5.addSlider("repelant")
    .setPosition(20,35)
    .setSize(100,20)
    .setRange(-50,-1)
    .setValue(-15);
  
  cp5.addSlider("sunMass")
    .setPosition(200,10)
    .setSize(100,20)
    .setRange(10,250)
    .setValue(50);

  cp5.addSlider("sunColor")
    .setPosition(200,35)
    .setSize(100,20)
    .setRange(1,5)
    .setValue(sunColor);
}

public void sunColor(int c){
  if (c == 1) sunColor = color(255,210,125);
  if (c == 2) sunColor = color(255,163,113);
  if (c == 3) sunColor = color(166,168,255);
  if (c == 4) sunColor = color(255,250,134);
  if (c == 5) sunColor = color(168,123,255);
}
class Agent {
  PVector pos;
  PVector vel;
  PVector acc;
  float ang;
  float velAng;
  float accAng;
  int c;
  float mass;
  
  
  Agent(float x, float y){
    this(x, y, random(20, 50));
  }
  
  Agent(float x, float y, float m){
    pos = new PVector(x,y);
    vel = new PVector(0,0);
    acc = new PVector(0.0f, 0);
    ang = random(TWO_PI);
    velAng = 0.1f;
    accAng = 0;
    c = color(random(255), random(255), random(255));
    this.mass = m;
  }
  
  public void display(){
    noStroke();
    c = color(map(pos.x,0,width,0,255),map(pos.y,0,height,0,255),128);
    fill(c,50);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    triangle(mass*2/3,0,-mass *1/3, mass/5,-mass*1/3,-mass/5);
    popMatrix();
  }
  
  public void update(){
    pos.add(vel);
    vel.add(acc);
    acc.mult(0);   
    ang = vel.heading();
    vel.limit(10);
  }
  
  public void addForce(PVector force){
    PVector f = PVector.div(force, mass);
    acc.add(f);
  }
  
}
  public void settings() {  fullScreen(P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "t3" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
