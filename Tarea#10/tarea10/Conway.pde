class Conway {
  int cellSize;

  float startAlive = 15;
  
  float despoblacion = 1;
  float sobrepoblacion = 1;
  float nacimiento = 1; 
  float permanencia = 1;
  float prob = random(0,1);

  ConwayCell[][] cells; 
  ConwayCell[][] cellsBuffer;

  Conway(int cellSize) {

    this.cellSize = cellSize;

    cells = new ConwayCell[width/cellSize][height/cellSize];
    cellsBuffer = new ConwayCell[width/cellSize][height/cellSize];

    for (int x = 0; x < width/cellSize; x++) {
      for (int y = 0; y < height/cellSize; y++) {
        float state = random (100);

        if (state > startAlive) state = 0;
        else state = 1;

        cells[x][y] = new ConwayCell((int)state);
      }
    }
  }

  void display() {
    for (int x = 0; x < width/cellSize; x++) {
      for (int y = 0; y < height/cellSize; y++) {
        cells[x][y].display(x, y, cellSize);
      }
    }
  }
  
  void randomize(){
    prob = random(0,1);
  }
  
  void step() { 

    copyCells();

    for (int x = 0; x < width/cellSize; x++) {
      for (int y = 0; y < height/cellSize; y++) {

        int neighbours = aliveNeighbours(x, y); 
        
        randomize();
        if (cellsBuffer[x][y].getState() == 1){
          if (neighbours < 2 && prob < despoblacion)
            cells[x][y] = new ConwayCell(0);
          
          randomize();
          if (neighbours > 3 && prob < sobrepoblacion)
            cells[x][y] = new ConwayCell(0);
          
          randomize();
          if (prob > permanencia){
            cells[x][y] = new ConwayCell(0);
          }
        }
        else{
          randomize();
          if (neighbours == 3 && prob < nacimiento)
            cells[x][y] = new ConwayCell(1);
          
          randomize();
          if (prob > permanencia){
            cells[x][y] = new ConwayCell(1);
          }
        }
      }
    }
  }

  void copyCells() {
    for (int x = 0; x < width/cellSize; x++) {
      for (int y = 0; y < height/cellSize; y++) {
        cellsBuffer[x][y] = cells[x][y];
      }
    }
  }

  int aliveNeighbours(int x, int y) {
    int neighbours = 0;
    for (int a = x-1; a <= x+1; a++) {
      for (int b = y-1; b <= y+1; b++) {  
        if (((a >= 0) && (a < width/cellSize)) && ((b >= 0) && (b < height/cellSize)))  
          if (!((a == x) && (b == y)))  
            if (cellsBuffer[a][b].getState() == 1) 
              neighbours++;
      }
    }
    return neighbours;
  }

  void restart() {
    for (int x = 0; x < width/cellSize; x++) {
      for (int y = 0; y < height/cellSize; y++) {
        float state = random (100);
        if (state > startAlive) {
          state = 0;
        } else {
          state = 1;
        }
        cells[x][y] = new ConwayCell(int(state));
      }
    }
  }

  void clear() {
    for (int x = 0; x < width/cellSize; x++) {
      for (int y = 0; y < height/cellSize; y++) {
        cells[x][y] = new ConwayCell(0);
      }
    }
  }

  void setDespoblacion(float despoblacion) {
    this.despoblacion = despoblacion;
  }

  void setSobrepoblacion(float sobrepoblacion) {
    this.sobrepoblacion = sobrepoblacion;
  }

  void setNacimiento(float nacimiento) {
    this.nacimiento = nacimiento;
  }

  void setPermanencia(float permanencia) {
    this.permanencia = permanencia;
  }
}
