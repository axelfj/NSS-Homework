// los puntos del path están quemados para un mejor uso //
// solo se pueden crear carros que utilicen la ruta ya creada //


PVector mouse = new PVector(mouseX,mouseY);
ArrayList<Vehicle> cars;
Path p;

void setup(){
  size(1280,720);
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
void draw(){
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
