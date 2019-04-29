class Agent {
  PVector pos;
  PVector vel;
  PVector acc;
  float ang;
  float velAng;
  float accAng;
  color c;
  float mass;
  
  
  Agent(float x, float y){
    this(x, y, random(20, 50));
  }
  
  Agent(float x, float y, float m){
    pos = new PVector(x,y);
    vel = new PVector(0,0);
    acc = new PVector(0.0, 0);
    ang = random(TWO_PI);
    velAng = 0.1;
    accAng = 0;
    c = color(random(255), random(255), random(255));
    this.mass = m;
  }
  
  void display(){
    noStroke();
    c = color(map(pos.x,0,width,0,255),map(pos.y,0,height,0,255),128);
    fill(c,50);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    triangle(mass*2/3,0,-mass *1/3, mass/5,-mass*1/3,-mass/5);
    popMatrix();
  }
  
  void update(){
    pos.add(vel);
    vel.add(acc);
    acc.mult(0);   
    ang = vel.heading();
    vel.limit(10);
  }
  
  void addForce(PVector force){
    PVector f = PVector.div(force, mass);
    acc.add(f);
  }
  
}
