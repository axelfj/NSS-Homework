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

import java.util.Iterator;
import controlP5.*;
ControlP5 cp5;
ArrayList<Agent> agents;
PVector gravity,windR,windL,mouse,cannon, dif, fDrag;

float yOff = 0;

float xColor = 0;
float yColor = 100;
float zColor = 200;

float start = 0;
float force = 0.4;
float water = 1.25;
float cDrag = -7;
color c;

PImage wp;

void setup(){
  size(1280,900);
  wp = loadImage("wp.jpg");
  wp.resize(width,height);
  background(wp);

  agents = new ArrayList();
  gravity = new PVector(0,0.5);
  windL = new PVector(1,0);
  windR = new PVector(-1,0);

  noCursor();
  initControls();
}

void draw(){ 
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
      xColor += 0.009;
    }
    yColor+=0.007;
    zColor+=0.005;
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

void dragThem(){
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

void frictThem(){
  for (Agent a : agents){

  }
}

void killBullets(){
  Iterator<Agent> i = agents.iterator();
  while (i.hasNext()){
    Agent a = i.next();
    if (a.pos.y > height+50) i.remove();
  }
}

void displayBackground(){  
  displayRect();
  displayCannon();
}

void displayRect(){
  background(wp);
  colorMode(HSB,360,100,100);
  stroke(190,100,100);
  strokeWeight(5);
  yOff = start;
  for (float x = 0; x < width; x+=5){
    float y = map(
                noise(yOff),
                0,1,
                height/(water-.07),height/(water+.05));
    line(x,y,x,height);
    yOff+=0.009;
  }
  start+=0.01;
}

void eraseAgents(){
  Iterator<Agent> i = agents.iterator();
  while (i.hasNext()){
    Agent a = i.next();
    i.remove();
  }
}

void displayCannon(){
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


void initControls(){
  cp5 = new ControlP5(this);

  cp5.addSlider("force")
    .setPosition(20,10)
    .setSize(100,20)
    .setRange(0.01,1)
    .setValue(0.4)
    .setColorLabel(color(0));

  cp5.addSlider("gravity")
    .setPosition(20,40)
    .setSize(100,20)
    .setRange(0.01,5)
    .setValue(0.5)
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

void gravity(float y){
  gravity.y = y;
}

void windL(float x){
  windL.x = x;
}

void windR(float x){
  windR.x = x;
}