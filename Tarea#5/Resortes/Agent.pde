class Agent{
  PVector pos;
  PVector vel;
  PVector acc;
  color c;
  float mass;
  
  Agent(float x, float y){
    this(x, y, random(20, 50));
  }
  
  Agent(float x, float y, float m){
    pos = new PVector(x,y);
    vel = new PVector(0,0); 
    acc = new PVector(0,0);
    c = color(255);
    this.mass = m;
  }
  
  void display(){
    noStroke();
    c = color(
      map(pos.x,0,width,0,255),
      map(pos.y,0,height,0,255),
      255/2);
    fill(c);
    ellipse(pos.x,pos.y,mass,mass);
  }
  
  void update(){
    vel.add(acc);
    pos.add(vel);
    acc.mult(0);   
  }
  
  void addForce(PVector force){
    PVector f = PVector.div(force, mass);
    acc.add(f);
  }

  void setMass(float mass){
    this.mass = mass;
  }
  
}
