class RepelentAgent extends Agent{
  float G;
  LinkedList<AgentSystem> systems;
  float sizeManagement = 0.05;

  RepelentAgent(float x, float y, float G, LinkedList<AgentSystem> systems){
    super(x,y);
    this.G = G;
    this.systems = systems;
    size = 10;
  }
  
  void update(){
    if (size < 10) sizeManagement*=-1;
    else if (size > 20) sizeManagement*=-1;
    size += sizeManagement;
    
    for (AgentSystem s : systems){
      for (Agent a : s.agents){
        if (a != this){
          PVector r = PVector.sub(a.pos,pos);
          float d = r.magSq();
          r.normalize();
          r.mult(a.size * size * G);
          r.div(d);
          a.addForce(r);
        }
      }
    }
  }
  
  void display(){
    noStroke();
    fill(0,map(size,10,20,0,128),map(size,10,20,128,255));
    ellipse(pos.x,pos.y,size,size);
  }
  
  void setG(float G){
    this.G = G;
  }
}
