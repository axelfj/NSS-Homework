class Spring implements IAgent{
  Agent a1, a2;
  float k, restLen, lifespan, lifeDecrease;
  color c;
  
  Spring(Agent a1, Agent a2, float k, float restLen){
    this.a1 = a1;
    this.a2 = a2;
    this.k = k;
    this.restLen = restLen;
    c = color(255,255,255);
    lifespan = a1.lifespan;
    lifeDecrease = a1.lifeDecrease;
  }
  
  void update(){
    lifespan -= lifeDecrease;
    PVector springForce = PVector.sub(a2.pos,a1.pos);
    float len = springForce.mag();
    springForce.normalize();
    float displacement = len - restLen;
    springForce.mult(-k*displacement);
    springForce.limit(10);
    springForce.div(2);
    a2.addForce(springForce);
    springForce.mult(-1);
    a1.addForce(springForce);
  }
  
  void display(){
    stroke(c,lifespan);
    strokeWeight(1);
    line(a1.pos.x, a1.pos.y, a2.pos.x,a2.pos.y);
  }

  void setK(float k){
    this.k = k;
  }

  void setRestLen(float restLen){
    this.restLen = restLen;
  }

}
