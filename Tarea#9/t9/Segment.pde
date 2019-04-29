class Segment{
  
  PVector start;
  PVector end;
  
  Segment(PVector start, PVector end){
    this.start = start;
    this.end = end;
  }

  boolean contains(PVector point){
   if (((point.x >= start.x && point.x <= end.x) || (point.x >= end.x && point.x <= start.x)) && 
       ((point.y >= start.y && point.y <= end.y) || (point.y >= end.y && point.y <= start.y)))
         return true;
    return false;
  }
}
