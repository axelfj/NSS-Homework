class FlowField{
  PVector[][] grid;
  float resolution;
  int rows, columns;
  float noiseOffset = 0.00000300;
  float defaultMag = 5;
  
  FlowField(float resolution, float noiseOffset, float defaultMag){
    this.resolution = resolution;
    this.noiseOffset = noiseOffset;
    this.defaultMag = defaultMag;
    rows = (int)(height/resolution)+1;
    columns = (int)(width/resolution)+1;
    
    grid = new PVector[rows][];
    for (int r = 0; r < rows; r++){
      grid[r] = new PVector[columns];
      for (int c = 0; c < columns; c++){
        float noiseVal = noise(
                          (float)r * noiseOffset,
                          (float)c * noiseOffset);
        float angle = map(noiseVal,0,1,-TWO_PI,TWO_PI) + frameCount/180;
        grid[r][c] = PVector.fromAngle(angle);
        grid[r][c].setMag(defaultMag);
        noiseOffset += 0.000003;
      }
    }
  }
  
  PVector getForce(float x, float y){
    if (x >= 0 && x <= width && y >= 0 && y <= height){
      int r = (int)(y / resolution);
      int c = (int)(x / resolution);
      return grid[r][c];
    }
    return new PVector(0,0);
  }
  
  void displayVector(float column, float row, PVector vector){
    PVector copy = vector.copy();
    float midResolution = resolution/2;
    copy.setMag(midResolution);
    pushMatrix();
    stroke(color(map(column,0,width,0,255),map(row,0,height,0,255),map(vector.heading(),-TWO_PI,TWO_PI,0,255)));
    strokeWeight(.5);
    translate(column + midResolution, row + midResolution);
    line(0,0,copy.x,copy.y);
    popMatrix();
  }
  
  void display(){
    for (int r = 0; r < rows; r++)
      for (int c = 0; c < columns; c++)
        displayVector(c * resolution, r * resolution, grid[r][c]);
  }

  void update(){
    for (int r = 0; r < rows; r++){
      for (int c = 0; c < columns; c++){
        float noiseVal = noise(
                          (float)r * noiseOffset,
                          (float)c * noiseOffset);
        float angle = map(noiseVal,0,1,-TWO_PI,TWO_PI) + frameRate/180;
        grid[r][c] = PVector.fromAngle(angle);
        grid[r][c].setMag(defaultMag);
        noiseOffset += 0.00000020;
      }
    }
  }
}
