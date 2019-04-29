// con 'c' activa el display del flowfield

ArrayList<Vehicle> cars;
FlowField ff;

void setup(){
  size(1280,720);
  background(0);
  cars = new ArrayList();
  ff = new FlowField(35,0.5,0.1);
}
void draw(){
  background(0);
  PVector mouse = new PVector(mouseX,mouseY);
  
  noCursor();
  noStroke();
  fill(255);
  ellipse(mouseX,mouseY,15,15);

  ff.update();
  for (Vehicle volswagen: cars){
    PVector f = ff.getForce(volswagen.pos.x,volswagen.pos.y);
    volswagen.applyForce(f);
    volswagen.arrive(mouse);
    volswagen.update();
    volswagen.display();
  }
  
  if (mousePressed){
    cars.add(new Vehicle(mouseX, mouseY,new PVector(0,0)));
  }   
  if (keyPressed && key == 'c') ff.display();
}
