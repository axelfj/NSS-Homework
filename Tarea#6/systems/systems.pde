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

import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;
import controlP5.*;

ControlP5 cp5;
LinkedList<AgentSystem> systems;
LinkedList<AttractorAgent> attractors;
LinkedList<RepelentAgent> repelents;
PVector gravity;
float attractor =-.05;
float repelent = -.5;
float lifeDecrease = 1;

void setup(){
  size(1280,720, P2D);
  blendMode(ADD);
  gravity = new PVector(0,.98);
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

void draw(){
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
        s.addForce(new PVector(.01,0));
      if (key == 'a')
        s.addForce(new PVector(-.01,0));
      if (key == 'w')
        s.addForce(new PVector(0,-.01));
      if (key == 's')
        s.addForce(new PVector(0,.01));       
    }
  }
  
}

void keyPressed(){
  if (key == 'r'){
    if (random(1) < .5)
      attractors.add(
        new AttractorAgent(random(width), random(height/2,height), repelent, systems));
    else
      repelents.add(
        new RepelentAgent(random(width), random(height/2,height), attractor, systems));
  }
}

void mousePressed(){
  if (mouseButton == RIGHT){
    systems.add(new AgentSystem(mouseX,mouseY, lifeDecrease));
  }
}

void initControls(){
  cp5 = new ControlP5(this);

  cp5.addSlider("gravity")
    .setPosition(20,25)
    .setSize(100,10)
    .setRange(-1,1)
    .setValue(0.98);

  cp5.addSlider("fric")
    .setPosition(20,50)
    .setSize(100,10)
    .setRange(0,-.05)
    .setValue(-0.01);

  cp5.addSlider("attractor")
    .setPosition(160,25)
    .setSize(100,10)
    .setRange(0,-1)
    .setValue(-.05);

  cp5.addSlider("repelent")
    .setPosition(160,50)
    .setSize(100,10)
    .setRange(0,-2)
    .setValue(-.5);

  cp5.addSlider("lifeDecrease")
    .setPosition(320,25)
    .setSize(100,10)
    .setRange(5,0.1)
    .setValue(1);

  cp5.addSlider("k")
    .setPosition(500,25)
    .setSize(100,10)
    .setRange(0.01,0.1)
    .setValue(0.01);

  cp5.addSlider("restLen")
    .setPosition(500,50)
    .setSize(100,10)
    .setRange(1,20)
    .setValue(10);

  
}

void gravity(float g){
  this.gravity.y = g/50;
}

void repelent(float repelent){
  for (AttractorAgent aa : attractors)
    aa.G = repelent;
}

void attractor(float attractor){
  for (RepelentAgent ra : repelents)
    ra.G = attractor;
}
  
void lifeDecrease(float lifeDecrease){
  this.lifeDecrease = lifeDecrease; 
  for (AgentSystem as : systems)
    as.setLifeDecrease(lifeDecrease);
}

void k(float k){
  for (AgentSystem as: systems)
    as.setK(k);
}

void restLen(float restLen){
  for (AgentSystem as: systems)
    as.setRestLen(restLen);
}

void fric(float fric){
  for (AgentSystem as: systems)
    as.setFric(fric);
}
