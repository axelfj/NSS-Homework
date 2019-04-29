class ConwayCell {
  int state;

  ConwayCell(int state) {
    this.state = state;
  }
  
  int getState(){
    return this.state;
  }
  
  void display(float x, float y, int cellSize){
    color alive = color(map(x,0,width/cellSize,0,255),map(y,0,height/cellSize,0,255),128);
    if (state == 1) fill(alive);
    else fill(color(0));
    
    rect (x*cellSize, y*cellSize, cellSize, cellSize);
  }
  
}
