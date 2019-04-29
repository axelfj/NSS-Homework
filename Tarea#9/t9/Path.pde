class Path{
  float radious;
  ArrayList<PVector> points;
  
  Path(){
    radious = 15;
    points = new ArrayList();
  }
  
  void addPoint(float x, float y){
    this.points.add(new PVector(x,y));
  }
  
  ArrayList<Segment> getSegments(){
    ArrayList<Segment> segments = new ArrayList();
    for (int i = 0; i < points.size() -1; i++){
      PVector start = points.get(i);
      PVector end = points.get(i+1);
      segments.add(new Segment(start,end));
    }
    return segments;
  }
  
  void display(){
    for (int i = 0; i < points.size() -1 ; i++){
      PVector start = points.get(i);
      PVector end = points.get(i+1);
      strokeWeight(radious * 3);
      stroke(200,45);
      line(start.x,start.y,end.x,end.y);
    }
    for (int i = 0; i < points.size() -1; i++){
      PVector start = points.get(i);
      PVector end = points.get(i+1);
      strokeWeight(0.1);
      stroke(255);
      line(start.x,start.y,end.x,end.y);
    }
  }
}