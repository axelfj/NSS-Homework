class Spring{
  Agent a1;
  Agent a2;
  float k;
  float restLen;
  color c;
  float ts;
    
  Spring(Agent a1, Agent a2, float k, float restLen){
    this.a1 = a1;
    this.a2 = a2;
    this.k = k;
    this.restLen = restLen;
    c = color(255);
    ts = 1;
  }
  
  void update(){
    c = color(200);
    PVector springForce = PVector.sub(a2.pos,a1.pos);
    float len = springForce.mag();
    springForce.normalize();
    float displacement = len - restLen;
    springForce.mult(-k*displacement);
    springForce.div(2);
    a2.addForce(springForce);
    springForce.mult(-1);
    a1.addForce(springForce);
  }
  
  void display(){
    stroke(c,ts);
    strokeWeight(1 );
    line(a1.pos.x, a1.pos.y, a2.pos.x,a2.pos.y);
  }

  void setK(float k){
    this.k = k;
  }

  void setRestLen(float restLen){
    this.restLen = restLen;
  }

  void setTS(float ts){
    this.ts = ts;
  }

}
