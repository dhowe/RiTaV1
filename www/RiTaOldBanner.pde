String[] letters = { "R", "i", "T", "a"};
PFont pf;
// Set number of circles
int count = 20;
// Set maximum and minimum circle size
int maxSize = 100;
int minSize = 20;
// Build float array to store circle properties
float[][] e = new float[count][5];
// Set size of dot in circle center
float ds=2;
// Selected mode switch
int sel = 0;
// Set drag switch to false
boolean dragging=false;

// If use drags mouse...
void mouseDragged() {
  // Set drag switch to true
  dragging=true;
}
// If user releases mouse...
void mouseReleased() {
  // ..user is no-longer dragging
  dragging=false;
}

// Set up canvas
void setup() {
  // Frame rate
  frameRate(10);
  pf = createFont("Lucida",100);
  // Size of canvas (width,height)
  size(940, 78);
  // Stroke/line/border thickness
  strokeWeight(1);
  // Initiate array with random values for circles
  for (int j=0;j< count;j++) {
    e[j][0]=random(width); // X 
    e[j][1]=random(height); // Y
    e[j][2]=random(minSize, maxSize); // Radius        
    e[j][3]=random(-.5, .5); // X Speed
    e[j][4]=random(-.5, .5); // Y Speed
  }
}

// Begin main draw loop (called 25 times per second)
void draw() {
  // Fill background black
  background(0);
  // Begin looping through circle array
  for (int j=0;j< count;j++) {
    // Disable shape stroke/border
    noStroke();
    // Cache diameter and radius of current circle
    float radi=e[j][2];
    float diam=radi/2;
    // If the cursor is within 2x the radius of current circle...
    if ( dist(e[j][0], e[j][1], mouseX, mouseY) < radi ) {
      // Change fill color to green.
      fill(64, 187, 128, 100);
      // Remember user has circle "selected"  
      sel=1;
      // If user has mouse down and is moving...
      if (dragging) {
        // Move circle to circle position
        e[j][0]=mouseX;
        e[j][1]=mouseY;
      }
    } 
    else {
      // Keep fill color blue
      fill(64, 128, 187, 100);
      // User has nothing "selected"
      sel=0;
    }

    // Draw letter
    String l = letters[j%4];
    float sz = j%2==0 ? radi*2 : radi*3;
    textFont(pf, sz);
    float tw = textWidth(l);
    float th = textAscent()+textDescent();
    text(l, e[j][0]-tw/2, e[j][1]+th/4);

    // Draw circle
    //ellipse(e[j][0], e[j][1], radi, radi);

    // Move circle
    e[j][0]+=e[j][3];
    e[j][1]+=e[j][4];


    /* Wrap edges of canvas so circles leave the top
     and re-enter the bottom, etc... */
    if ( e[j][0] < -diam      ) { 
      e[j][0] = width+diam;
    } 
    if ( e[j][0] > width+diam ) { 
      e[j][0] = -diam;
    }
    if ( e[j][1] < 0-diam     ) { 
      e[j][1] = height+diam;
    }
    if ( e[j][1] > height+diam) { 
      e[j][1] = -diam;
    }

    // If current circle is selected...
    if (sel==1) {
      // Set fill color of center dot to white..
      fill(255, 255, 255, 255);
      // ..and set stroke color of line to green.
      stroke(128, 255, 0, 100);
    } 
    else {            
      // otherwise set center dot color to black.. 
      fill(0, 0, 0, 255);
      // and set line color to turquoise.
      stroke(64, 128, 128, 255);
    }

    // Loop through all circles
    for (int k=0;k< count;k++) {
      // If the circles are close...
      if ( dist(e[j][0], e[j][1], e[k][0], e[k][1]) < radi) {
        // Stroke a line from current circle to adjacent circle
        line(e[j][0], e[j][1], e[k][0], e[k][1]);
      }
    }
    // Turn off stroke/border
    noStroke();      
    // Draw dot in center of circle
    rect(e[j][0]-ds, e[j][1]-ds, ds*2, ds*2);
  }
}


