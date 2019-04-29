// presione R para resetear toda la matriz //
// presione C para limpiar la pantalla //


import controlP5.*;
ControlP5 cp5;

Conway conway;

int cellSize = 10;

void setup() {
  size (1280, 720);
  stroke(10);

  conway = new Conway(cellSize);
  background(0);
  initControls();
}


void draw() {
  conway.step();
  conway.display();
}

void keyPressed() {
  if (key=='r' || key == 'R') {
    conway.restart();
  }
  if (key=='c' || key == 'C') {
    conway.clear();
  }
}

void initControls(){
  cp5 = new ControlP5(this);

  cp5.addSlider("despoblacion")
    .setPosition(20,height - 25)
    .setSize(70,10)
    .setRange(0,1)
    .setValue(1);
    
  cp5.addSlider("sobrepoblacion")
    .setPosition(20,height - 45)
    .setSize(70,10)
    .setRange(0,1)
    .setValue(1);
    
  cp5.addSlider("nacimiento")
    .setPosition(200,height - 25)
    .setSize(70,10)
    .setRange(0,1)
    .setValue(1);
    
  cp5.addSlider("permanencia")
    .setPosition(200,height - 45)
    .setSize(70,10)
    .setRange(0,1)
    .setValue(1);
}

void despoblacion(float despoblacion){
  conway.setDespoblacion(despoblacion);
}

void sobrepoblacion(float sobrepoblacion){
  conway.setSobrepoblacion(sobrepoblacion);
}

void nacimiento(float nacimiento){
  conway.setNacimiento(nacimiento);
}

void permanencia(float permanencia){
  conway.setPermanencia(permanencia);
}
