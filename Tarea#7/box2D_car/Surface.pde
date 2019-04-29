class Surface{
  ArrayList<Vec2> points;
  float offset = random(100);
  float xOff = 0.01;

  Surface(){
    this.points = new ArrayList();
    
    BodyDef bodyDef = new BodyDef();
    bodyDef.position = new Vec2(0,0);
    Body body;
    body = box2d.createBody(bodyDef);
    
    for (float x = 0; x < width; x += 4){
      float y = map(noise(offset),0,1,height*2/3,height);
      Vec2 v = new Vec2(x,y);
      points.add(v);
      offset+=xOff;
    }
    
    Vec2[] worldPoints = new Vec2[points.size()];
    for (int i = 0; i < points.size(); i++) worldPoints[i] = box2d.coordPixelsToWorld(points.get(i));
    ChainShape chainShape = new ChainShape();
    chainShape.createChain(worldPoints, worldPoints.length);
    body.createFixture(chainShape, 1);
  }
  
  void display() {
    stroke(255);
    fill(255);
    beginShape();
    vertex(0, height);
    for (int i = 0; i < points.size(); i++) 
      vertex(points.get(i).x, points.get(i).y);
    vertex(width, height);
    endShape(CLOSE);
  }
}
