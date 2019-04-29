// size variables //
float zOffs = 0;
float size = random(10);

// place variables //
float zPlace = 0;
float place = random(1000);

// color variables //
float zOffr = 255;
float zOffg = 255;
float zOffb = 255;

int xValue = 8;
int yValue = 5;

void setup() {
  //fullScreen();
  size(600, 420);
  background(0);
}

void draw() {
  background(0);
  float xOffs = 0;
  float xOffp = 0;
  for (float x = -25; x < width+25; x+=xValue) {
    float yOffs = 0;
    float yOffp = 0;
    for (float y = -25; y < height+25; y+=yValue) { 
      float a = map(noise(xOffs, yOffs, zOffr), 0, 1, 0, 255);
      float b = map(noise(xOffs, yOffs, zOffg), 0, 1, 0, 255);
      float c = map(noise(xOffs, yOffs, zOffb), 0, 1, 0, 255);
      stroke(a, b, c);

      size = map(noise(xOffs, yOffs, zOffs), 0, 1, 0, 17);
      strokeWeight(size);

      place = map(noise(xOffp, yOffp, zPlace), 0, 1, -50, 50);
      point(x+place, y+place);
      noFill();
      yOffs += 0.00786;
      yOffp += 0.00786;


      if (keyPressed && (key == 'c')) {
        zOffr = random(255);
        zOffg = random(255);
        zOffb = random(255);
      } else if (keyPressed && (key == 's')) {
        zOffs = random(255);
      } 
      
    }
    zOffs += .00025;
    zPlace += .0003;
    xOffs += .01;
    xOffp += .01;
  }
  zOffs += .000093;
  zOffr += .005;
  zOffg += .005;
  zOffb += .005;
}
