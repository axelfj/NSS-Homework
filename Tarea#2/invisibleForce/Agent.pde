class Globe{
  PVector pos;
  PVector vel;
  PVector acc;
  color c;
  float size;
  float mass;
  float velMag;
  
  Globe(float x, float y){
    pos = new PVector(x,y);
    vel = PVector.random2D();
    velMag = randomGaussian() * 50 + random(50,100);
    vel.setMag(PVector.random2D(),velMag);
    acc = new PVector(0,0);
    c = color(random(255),random(255),random(255));
    mass = random(1,90);
  }
  
  void display(){
    noStroke();
    stroke(map(pos.x,0,height,255,0));
    strokeWeight(.9);
    // red blue
    c = color(map(pos.x,0,height,0,255),0,map(pos.y,0,width,0,255));
    // red green
    //c = color(map(pos.x,0,height,0,255),map(pos.y,0,width,0,255),0);
    // green blue
    //c = color(0,map(pos.y,0,width,0,255),map(pos.x,0,height,0,255));
    fill(c,45);
    ellipse(pos.x,pos.y,mass,mass);
  }

  void update(){
    
    vel.limit(20);
    vel.add(acc);
    pos.add(vel);
    acc.mult(0);
  }

  void addForce(PVector force){
    PVector f = PVector.div(force,mass);
    acc.add(f);  
  }

}
