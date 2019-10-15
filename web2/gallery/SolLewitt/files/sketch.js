var distMouse; 

var distC; 
var distY; 

var bug;  

//ball 
var xspeed = 10;
var yspeed = 0;

var divNum = 1.6; 


var MposX = 0;
var MposY = 0;
var YposX;
var YposY;
var CposX;
var CposY;


var imgWidth = 300/divNum; 
var imgHeight = 56/divNum; 

function preload() {
  imgC = loadImage("richC.png");
  imgM = loadImage("richM.png");
  imgY = loadImage("richY.png");
  imgK = loadImage("rich.png");
}


function drawChannel(image, x, y) {
    var u = 60;
    var v = 60;
    blend(image, 0, 0, u, v,  x - 0.5 * u, y - 0.5 * v, u, v, blendMode(ADD));
}



function setup() {
  createCanvas(imgWidth, imgHeight);
  bug = new Jitter();

}

function draw(){
  background(255);
  imgC.loadPixels();
  imgM.loadPixels();
  imgY.loadPixels();  
  imgK.loadPixels(); 

  bug.move();
  bug.display();


///////
distMouse = dist(imgM.width/3, imgM.height/3, bug.x, bug.y);


if (distMouse < 250){
blendMode(MULTIPLY);
image(imgM, MposX, MposY, imgWidth, imgHeight);


YposX = (bug.x/80)/divNum;
YposY =  (bug.y/80)/divNum; 
image(imgY, YposX, YposY, imgWidth, imgHeight);

CposX = (-bug.x/80)/divNum;
CposY = (-bug.y/80)/divNum;
image(imgC, CposX, CposY, imgWidth, imgHeight);
}


blendMode(ADD);


///Animate


if (distMouse > 250){
blendMode(MULTIPLY);
YdistX = dist(YposX, YposX, 0, 0); 
YdistY = dist(YposY, YposY, 0, 0); 

CdistX = dist(CposX, CposX, 0, 0); 
CdistY = dist(CposY, CposY, 0, 0); 

image(imgM, MposX, MposY, imgWidth, imgHeight);

///yellow
//x
if (YdistX > 0){
  YposX -= .5; 
} 
if (YdistX <= .5){
  YposX = 0; 
}
//y
if (YdistY > 0){
  YposY -= .5; 
} 
if (YdistY <= .5){
  YposY = 0; 
}
image(imgY, YposX, YposY, imgWidth, imgHeight);
////cyan 

//x
if (CdistX > 0){
  CposX += .5; 
} 
if (CdistX <= .5){
  CposX = 0; 
}
//y
if (CdistY > 0){
  CposY += .5; 
} 
if (CdistY <= .5){
  CposY = 0; 
}
image(imgC, CposX, CposY, imgWidth, imgHeight);

}

blendMode(ADD);


} // end draw

   // Jitter class
function Jitter() {
  this.x = 0;
  this.y = 0;
  this.diameter = 10;
  this.speed = 1;

  this.move = function() {
    this.x = this.x + xspeed;
    this.y = this.y + yspeed;

    if (this.x > 3600 || this.x < 0)  {
      xspeed = -xspeed;
    }

    if (this.y > 3600 || this.y < 0) {
      yspeed = -yspeed;
    }
  };

  this.display = function() {
    fill(255);
    noStroke(); 
    ellipse(this.x, this.y, this.diameter, this.diameter);
  }
};

