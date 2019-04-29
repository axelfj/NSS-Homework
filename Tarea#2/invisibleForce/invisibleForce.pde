// se recomienda pantallas cuadradas y mayores a 600x600 //
// se borraron los bordes para evitar que los circulos reboten, sin embargo la fuerza se aplica. //

// si se presiona 'a' se aplicará fuerza a la izquierda //
// si se presiona 'd' se aplicará fuerza a la derecha //
// si se presiona 'w' se aplicará fuerza hacia arriba //
// si se presiona 's' se aplicará fuerza hacia abajo //
// si se presiona 'r' se anularan las fuerzas //
// si se presiona 'b' se cambiará el color del fondo en b/n //

ArrayList<Globe> globes;
float up, down, left, right;
PVector forceUp, forceDown, forceLeft, forceRight, f;
float newMag, strength;
float xOff = random(0, 255);
float dist = 1000;
int maxF = 10;
int value = 255;

void setup() {
  size(900, 900);
  strength = 0.5;
  globes = new ArrayList();
  for (int i = 0; i < 100; i++) 
    globes.add(new Globe(random(width), random(height)));
}

void draw() {
  //setBackground(value);
  
  fill(0,5);
  rect(0,0,width*2,height*2);
  rectMode(CORNER);

  for (Globe g : globes) {
    g.display();

    // Calcular las distancias //
    up = g.pos.x;
    down = (g.pos.y - height/15);
    left = g.pos.y;
    right = (g.pos.x - width/15);

    // Aplicar fuerzas //
    forceLeft = new PVector(0, left);
    force(forceLeft, g);
    forceRight = new PVector(width, right);
    force(forceRight, g);
    forceUp = new PVector(up, 0);
    force(forceUp, g);
    forceDown = new PVector(down, width);
    force(forceDown, g);


    // Good mods //
    if (keyPressed) {
      if (key == 'a')g.vel.add(-1, 0);
      if (key == 'w')g.vel.add(0, -1);
      if (key == 's')g.vel.add(0, 1);
      if (key == 'd')g.vel.add(1, 0);
      if (key == 'r')g.vel.set(0, 0);
      if (key == 'b')value = (int)map(noise(xOff),0,1,0,255);
    }    
    g.update();
  }
  xOff+=0.01;
}

void force(PVector vector, Globe g) {
  f = PVector.sub(g.pos, vector);
  newMag = map(f.mag(), 0, dist, strength, -strength);
  f.setMag(newMag*maxF);
  g.addForce(f);
}

void setBackground(int value) {
  background(value);
}
void mousePressed() {
  if (mouseButton == LEFT) globes.add(new Globe(mouseX, mouseY));
}
