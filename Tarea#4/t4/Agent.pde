class Agent {
  PVector pos;
  PVector vel;
  PVector acc;
  float ang,velAng,accAng, mass;
  color c;
  float des = random(6,10);
  float med = random(20,25);

  Agent(float x, float y, color c){
    this(x, y, random(20,25), c);
  }
  
  Agent(float x, float y, float m, color c){
    pos = new PVector(x,y);
    vel = new PVector(0,0);
    acc = new PVector(0.0, 0);
    ang = random(TWO_PI);
    velAng = 0.5;
    accAng = 0;
    this.c = c;
    float m1 = randomGaussian() * des + med;
    this.mass = m1;
  }
  
  void display(){
    noStroke();
    //c = color(noise(),noise(),noise());
    fill(c);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(ang);
    rectMode(CENTER);
    rect(0,0,mass*2/3, mass*3/2);
    rectMode(CORNER);
    popMatrix();
  }
  
  void update(){
    pos.add(vel);
    vel.add(acc);
    acc.mult(0);   
    if (pos.y > height/1.25) ang += 0.00001;
    else ang += 0.03;
  }
  
  void addForce(PVector force){
    PVector f = PVector.div(force, mass);
    acc.add(f);
  }
  
}
