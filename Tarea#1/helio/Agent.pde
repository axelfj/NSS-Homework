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
    velMag = randomGaussian() * 50 + random(0,100);
    vel.setMag(PVector.random2D(),velMag);
    acc = new PVector(0,0);
    c = color(random(255),random(255),random(255));
    mass = random(25,35);
  }
  
  void display(){
    noStroke();
    fill(c);
    ellipse(pos.x,pos.y,mass,mass);
    
  }

  void update(){
    if (pos.x < mass/2 || pos.x > width - mass/2){
      pos.x = constrain(pos.x,mass/2, width - mass/2);
      vel.x *= -.8;
    }
    if (pos.y < mass/2 || pos.y > height - mass/2){
      pos.y = constrain(pos.y, mass/2, height-mass/2);
      vel.y *= -.8;
    }
    vel.add(acc);
    pos.add(vel);
    acc.mult(0);
  }

  void addForce(PVector force){
    PVector f = PVector.div(force,mass);
    acc.add(f);  
  }

}
