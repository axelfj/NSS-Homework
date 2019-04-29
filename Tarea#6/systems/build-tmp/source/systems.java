import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.LinkedList; 
import java.util.Iterator; 
import java.util.ListIterator; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class systems extends PApplet {

/*
  Tarea #6
  Estudiantes:
    Axel Alejandro Fernández Jiménez - 2016098894
    Andres Ottón Brenes Maleaño - 2016078594

    Instrucciones:
      Se poseen sliders de:
        - Gravedad (gravity)
        - Fricción (fric)
        - Atracción hacia los puntos (attractor)
        - Repulsión desde los puntos (repelent)
        - Vida de los agentes (lifeDecrease)
        - Resorte (k)
        - Longitud del resorte (restLen)
      Cada uno aumenta de izquierda a derecha.
    Si desea crear puntos en pantalla, serán creados
    aleatoriamente en la pantalla presionando la tecla
    'r'.

    Se puede aplicar viento presionado wasd, 'w' lo
    hará hacia arriba, 'a' hacia la izq, 'd' hacia la
    derecha y 's' hacia abajo.
*/






ControlP5 cp5;
LinkedList<AgentSystem> systems;
LinkedList<AttractorAgent> attractors;
LinkedList<RepelentAgent> repelents;
PVector gravity;
float attractor =-.05f;
float repelent = -.5f;
float lifeDecrease = 1;

public void setup(){
  
  blendMode(ADD);
  gravity = new PVector(0,.98f);
  systems = new LinkedList();
  attractors = new LinkedList();
  repelents = new LinkedList();

  for (int i = 0; i < 7; i++){
    attractors.add(
      new AttractorAgent(random(width), random(height/2,height), repelent, systems));
    repelents.add(
      new RepelentAgent(random(width), random(height/2,height), attractor, systems));
  }
  initControls();
}

public void draw(){
  background(0);

  for (AttractorAgent a : attractors){
    a.update();
    a.display();  
  }
  
  for (RepelentAgent r : repelents){
    r.update();
    r.display();  
  }
  for (AgentSystem s: systems){
    s.addForce(gravity);
    s.update();
    s.display();
    s.addAgent();
    if (keyPressed){
      if (key == 'd')
        s.addForce(new PVector(.01f,0));
      if (key == 'a')
        s.addForce(new PVector(-.01f,0));
      if (key == 'w')
        s.addForce(new PVector(0,-.01f));
      if (key == 's')
        s.addForce(new PVector(0,.01f));       
    }
  }
  
}

public void keyPressed(){
  if (key == 'r'){
    if (random(1) < .5f)
      attractors.add(
        new AttractorAgent(random(width), random(height/2,height), repelent, systems));
    else
      repelents.add(
        new RepelentAgent(random(width), random(height/2,height), attractor, systems));
  }
}

public void mousePressed(){
  if (mouseButton == RIGHT){
    systems.add(new AgentSystem(mouseX,mouseY, lifeDecrease));
  }
}

public void initControls(){
  cp5 = new ControlP5(this);

  cp5.addSlider("gravity")
    .setPosition(20,25)
    .setSize(100,10)
    .setRange(-1,1)
    .setValue(0.98f);

  cp5.addSlider("fric")
    .setPosition(20,50)
    .setSize(100,10)
    .setRange(0,-.05f)
    .setValue(-0.01f);

  cp5.addSlider("attractor")
    .setPosition(160,25)
    .setSize(100,10)
    .setRange(0,-1)
    .setValue(-.05f);

  cp5.addSlider("repelent")
    .setPosition(160,50)
    .setSize(100,10)
    .setRange(0,-2)
    .setValue(-.5f);

  cp5.addSlider("lifeDecrease")
    .setPosition(320,25)
    .setSize(100,10)
    .setRange(5,0.1f)
    .setValue(1);

  cp5.addSlider("k")
    .setPosition(500,25)
    .setSize(100,10)
    .setRange(0.01f,0.1f)
    .setValue(0.01f);

  cp5.addSlider("restLen")
    .setPosition(500,50)
    .setSize(100,10)
    .setRange(1,20)
    .setValue(10);

  
}

public void gravity(float g){
  this.gravity.y = g/50;
}

public void repelent(float repelent){
  for (AttractorAgent aa : attractors)
    aa.G = repelent;
}

public void attractor(float attractor){
  for (RepelentAgent ra : repelents)
    ra.G = attractor;
}
  
public void lifeDecrease(float lifeDecrease){
  this.lifeDecrease = lifeDecrease; 
  for (AgentSystem as : systems)
    as.setLifeDecrease(lifeDecrease);
}

public void k(float k){
  for (AgentSystem as: systems)
    as.setK(k);
}

public void restLen(float restLen){
  for (AgentSystem as: systems)
    as.setRestLen(restLen);
}

public void fric(float fric){
  for (AgentSystem as: systems)
    as.setFric(fric);
}
class Agent implements IAgent{
  PVector pos, vel, acc;
  int c;
  float lifespan, lifeDecrease, size;
  
  Agent(float x, float y, float size, float lifeDecrease){
    this(x,y);
    this.size = size;
    this.lifeDecrease = lifeDecrease;
  }

  Agent(float x, float y){
    pos = new PVector(x,y);
    vel = PVector.random2D();
    vel.setMag(map(randomGaussian(),-1,1,0,1));
    acc = new PVector(0,0);
    c = color(0xffB46F2E);
    lifespan = 255;
    //lifeDecrease = 1;
    size = 10;
  }

  public void update(){
    /*c = color(
      map(pos.x,0,width,0,255),
      map(pos.y,0,height,0,255),
      128);*/
    colorMode(HSB);
    c = color(frameCount % 255, 255, 255);
    colorMode(RGB);
    vel.add(acc);
    pos.add(vel);
    acc.mult(0);
    lifespan -= lifeDecrease;
  }
  
  public void display(){
    noStroke();
    fill(c,lifespan);
    ellipse(pos.x,pos.y,size,size);
  }
  
  public void addForce(PVector f){
    acc.add(f);
  }
  
  public boolean isDead(){
    if (lifespan <= 0) 
      return true; 
    return false;
  }

  public void setLifeDecrease(float lifeDecrease){
    this.lifeDecrease = lifeDecrease;
  }
}
class AgentSystem implements IAgent{
  LinkedList<Agent> agents;
  LinkedList<Spring> springs;
  PVector pos;
  float size, rythm;
  float fric = -0.01f;
  float lifeDecrease = 1;
  float k = 0.01f;
  float restLen = 10;
  
  AgentSystem(float x, float y, float lifeDecrease){
    this(x,y);
    this.lifeDecrease = lifeDecrease;
  }
  
  AgentSystem(float x, float y){
    agents = new LinkedList();
    springs = new LinkedList();
    pos = new PVector(x,y);
  }
  
  public void update(){
    for (Agent a: agents)
      a.update();
     
    for (Spring s: springs)
      s.update();
    
    ListIterator<Agent> it = agents.listIterator();
    while (it.hasNext()){
      Agent a = it.next();
      if (a.isDead()){
        it.remove();
      }  
    }
    ListIterator<Spring> it2 = springs.listIterator();
    while(it2.hasNext()){
      Spring s = it2.next();
      if (s.a1.isDead() || s.a2.isDead()){
        it2.remove();
      }
    }
  }
  
  public void display(){
    for (Agent a: agents){
      a.display();
      PVector fr= a.vel.copy();
      fr.normalize();
      fr.mult(fric);
      a.addForce(fr);
    }

    for (Spring s: springs)
      s.display();
  }
  
  public void addForce(PVector f){
    for (Agent a: agents)
      a.addForce(f);
  }
    
  public void addAgent(){
    Agent a1 = new Agent(pos.x,pos.y,random(5,25), lifeDecrease);
    Agent a2 = new Agent(pos.x,pos.y,random(5,25), lifeDecrease);
    agents.add(a1);
    agents.add(a2);
    springs.add(new Spring(a1,a2,k,restLen));
  }
  
  public void addAgent(Agent a){
    agents.add(a);
  }

  public void setLifeDecrease(float lifeDecrease){
    this.lifeDecrease = lifeDecrease;
    for (Agent a: agents)
      a.lifeDecrease = lifeDecrease;
  }

  public void setK(float k){
    this.k = k;
    for (Spring s: springs)
      s.k = k;
  }

  public void setRestLen(float restLen){
    this.restLen = restLen;
    for (Spring s: springs)
      s.restLen = restLen;
  }

  public void setFric(float fric){
    this.fric = fric;
  }
}
class AttractorAgent extends Agent{
  float G;
  LinkedList<AgentSystem> systems;
  float sizeManagement = 0.05f;

  AttractorAgent(float x, float y, float G, LinkedList<AgentSystem> systems){
    super(x,y);
    this.G = G;
    this.systems = systems;
    size = 20;
  }
  
  public void update(){
    if (size < 10) sizeManagement*=-1;
    else if (size > 20) sizeManagement*=-1;
    size += sizeManagement;
    for (AgentSystem s : systems){
      for (Agent a : s.agents){
        if (a != this){
          PVector r = PVector.sub(pos,a.pos);
          float d = r.magSq();
          r.normalize();
          r.mult(a.size * size * G);
          r.div(d);
          a.addForce(r);
        }
      }
    }
  }
  
  public void display(){
    noStroke();
    fill(map(size,10,20,128,255),map(size,10,20,0,128),0);
    ellipse(pos.x,pos.y,size,size);
  }
  
  public void setG(float G){
    this.G = G;
  } 
}
class RepelentAgent extends Agent{
  float G;
  LinkedList<AgentSystem> systems;
  float sizeManagement = 0.05f;

  RepelentAgent(float x, float y, float G, LinkedList<AgentSystem> systems){
    super(x,y);
    this.G = G;
    this.systems = systems;
    size = 10;
  }
  
  public void update(){
    if (size < 10) sizeManagement*=-1;
    else if (size > 20) sizeManagement*=-1;
    size += sizeManagement;
    
    for (AgentSystem s : systems){
      for (Agent a : s.agents){
        if (a != this){
          PVector r = PVector.sub(a.pos,pos);
          float d = r.magSq();
          r.normalize();
          r.mult(a.size * size * G);
          r.div(d);
          a.addForce(r);
        }
      }
    }
  }
  
  public void display(){
    noStroke();
    fill(0,map(size,10,20,0,128),map(size,10,20,128,255));
    ellipse(pos.x,pos.y,size,size);
  }
  
  public void setG(float G){
    this.G = G;
  }
}
class Spring implements IAgent{
  Agent a1, a2;
  float k, restLen, lifespan, lifeDecrease;
  int c;
  
  Spring(Agent a1, Agent a2, float k, float restLen){
    this.a1 = a1;
    this.a2 = a2;
    this.k = k;
    this.restLen = restLen;
    c = color(255,255,255);
    lifespan = a1.lifespan;
    lifeDecrease = a1.lifeDecrease;
  }
  
  public void update(){
    lifespan -= lifeDecrease;
    PVector springForce = PVector.sub(a2.pos,a1.pos);
    float len = springForce.mag();
    springForce.normalize();
    float displacement = len - restLen;
    springForce.mult(-k*displacement);
    springForce.limit(10);
    springForce.div(2);
    a2.addForce(springForce);
    springForce.mult(-1);
    a1.addForce(springForce);
  }
  
  public void display(){
    stroke(c,lifespan);
    strokeWeight(1);
    line(a1.pos.x, a1.pos.y, a2.pos.x,a2.pos.y);
  }

  public void setK(float k){
    this.k = k;
  }

  public void setRestLen(float restLen){
    this.restLen = restLen;
  }

}
  public void settings() {  size(1280,720, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "systems" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
