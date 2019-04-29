class Vehicle{
  PVector pos, vel, acc;
  float mass, size, maxSpeed, maxForce, arrivalRadius; 
  color c;
  
  Vehicle(float x, float y, PVector vel){
    pos = new PVector(x,y);
    this.vel = vel;
    acc = new PVector(0,0);
    mass = 1;
    size = random(3,7);
    maxSpeed = random(2,3);
    maxForce = random(0.08,0.02);
    arrivalRadius = 200;    
    colorMode(HSB);
    c = color(frameCount % 255, 255, 255);
    colorMode(RGB);
  }
  
  void update(){
    vel.add(acc);
    vel.limit(maxSpeed);
    pos.add(vel);
    pos.x = (pos.x + width)%width;
    pos.y = (pos.y + height)%height;
    acc.mult(0);
  }
  
  void applyForce(PVector force){
    PVector f = PVector.div(force,mass);
    acc.add(f);
  }
  
  void display(){
    float ang = vel.heading();
    //stroke(255);
    //strokeWeight(.5);
    //c = color(map(pos.x,0,width,0,255),map(pos.y,0,height,0,255),128);
    fill(c);
    noStroke();
    pushMatrix();
    translate(pos.x,pos.y);
    rotate(ang);
    beginShape();
    vertex(size * 3, 0);
    vertex(0, -size);
    vertex(0, size);
    endShape(CLOSE);
    popMatrix();
  }
  
  void seek(PVector target){
    PVector desired = PVector.sub(target,pos);
    desired.setMag(maxSpeed);
    PVector steering = PVector.sub(desired,vel);
    steering.limit(maxForce);
    applyForce(steering);
  }
  
  void arrive(PVector target){
    float d = PVector.dist(pos,target);
    d = constrain(d,0,arrivalRadius);
    float speed = map(d, 0, arrivalRadius, 0, maxSpeed);
    vel.setMag(speed);
    seek(target);
  }
}
