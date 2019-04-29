// los 'globos' poseen masa diferente por lo que el viento actua diferente
// Dando click izquierdo se efectua el viento contrario al noise
// Dando click en el centro, se efectua viento hacia abajo
// Dando click derecho, pondrá más globos en pantalla
// si se presiona 'w' el movimiento en X muere.
// si se presiona 'e' el viento noise se hará más fuerte
// si desea reiniciar los vientos, presione 'r'
// para aumentar la fuerza del viento del mouse, presionar 'q'


ArrayList<Globe> globes;
PVector helium,windR,noisyWind,windU;

float xOff = 420;
float mouseForce = -1;
float posX, posY;

int desX = 50;
int desY = 25;
int limit = 2;

PImage bg;

void setup(){
  size(900,680);
  
  helium = new PVector(0,-.01);
  windU = new PVector(0,1);

  globes = new ArrayList();
  for (int i = 0; i < 400; i++) {
    posX = randomGaussian() * desX + width/2;
    posY = height/5 + randomGaussian() * desY + height/2;
    globes.add(new Globe(posX,posY));
  }

  bg = loadImage("clouds.png");
  bg.resize(width,height);
}

void draw(){
  background(bg);

  float windNoise = map(noise(xOff),0,1,0,limit);
  noisyWind = new PVector(windNoise,0);

  windR = new PVector(mouseForce,0);

  for (Globe g : globes){
    g.display();
    PVector gravityForce = PVector.mult(helium, g.mass/4);
    g.addForce(gravityForce);
    g.addForce(noisyWind);
    g.update();
  }
  if (mousePressed){
    if (mouseButton == LEFT) {
      for (Globe g : globes) g.addForce(windR);
    }
    if (mouseButton == RIGHT) globes.add(new Globe(mouseX,mouseY));
    if (mouseButton == CENTER){
      for (Globe g : globes) g.addForce(windU);
    }
  }
  if (keyPressed){
    if (key == 'q'){
      mouseForce--;
    }
    if (key == 'w'){
      for (Globe g : globes) g.vel.x = 0;
    }
    if (key == 'e'){
      limit++;
    }
    if (key == 'r'){
      limit = 2;
      mouseForce = -1;
    }
  }
  xOff +=0.01;
}
