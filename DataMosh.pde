import processing.opengl.*;
import gifAnimation.*;
import JMyron.*;

PImage currentFrame, prevFrame, staticImage;
PImage[] allFrames;
// currentFrame and prevFrame are the location to temporarily store the previous 
// and current frame.
int NROWS, NCOLS;
int PIXELSIZE = 6; //has to be even
final float LIMIT = 30.0;

int w, h;
// w and h are the size of the grid.
JMyron camera;

boolean USE_GIF = false;
boolean USE_BLEND_MODE = true;
boolean PAUSE = false;

int blendMode = LIGHTEST;
int videoWidth = 640;
int videoHeight = 480;
   
void setup(){
  
  size(1280, 960, OPENGL);
  //hint(ENABLE_OPENGL_4X_SMOOTH);
  frameRate(60); 

   camera = new JMyron();
   camera.start(videoWidth, videoHeight);
   camera.findGlobs(0);
   
   currentFrame = createImage(videoWidth,videoHeight,ARGB);
   prevFrame = createImage(videoWidth,videoHeight,ARGB);
   staticImage = createImage(videoWidth,videoHeight,ARGB);
   
   NROWS = int(currentFrame.height/PIXELSIZE);
   NCOLS = int(currentFrame.width/PIXELSIZE);
   
   w = currentFrame.width/NCOLS;
   h = currentFrame.height/NROWS;
   
   if(USE_GIF)
   {
     allFrames = Gif.getPImages(this, "gorilla.gif");
     currentFrame.width = allFrames[1].width;
     currentFrame.height = allFrames[1].height;
   }
}

int frameCounter = 0;
void draw(){
  if(!PAUSE)
  {
    if(USE_GIF)
    {
      if(frameCounter > allFrames.length-2)
      {
        frameCounter = 0;
      }else{
        frameCounter++;
      }
    }
    
     background(#FFFFFF);
      // copy the image from currentFrame to prevFrame - the previous frame.
     prevFrame.copy(currentFrame,0,0,currentFrame.width,currentFrame.height,0,0,prevFrame.width,prevFrame.height);
     prevFrame.updatePixels();
     camera.update();
     
     if(USE_GIF)
     {
       currentFrame = allFrames[frameCounter];
     }else{
       // update the current frame to currentFrame.
       camera.imageCopy(currentFrame.pixels);
     }
     currentFrame.updatePixels();
     
     findFlow();
     
     if(USE_BLEND_MODE){ staticImage.blend(currentFrame, 0, 0, videoWidth, videoHeight, 0, 0, videoWidth, videoHeight, blendMode); }
     image(staticImage, videoWidth, 0, videoWidth, videoHeight);
  }
}

void dataMosh(Point p1, Point p2) {
  if(p1.x < staticImage.width && p1.y < staticImage.height)
   {     
     int dx = int(p2.x - p1.x);
     int dy = int(p2.y - p1.y);

     for (int y = 0 - (h/2); y < (h/2); y++) {
      for (int x = 0 - (w/2); x < (w/2); x++) {
       if(p2.x+x < staticImage.width-1 && p2.y+y < staticImage.height-1)
       {
         color c1 = staticImage.pixels[(p2.y+y)*staticImage.width+(p2.x+x)];
         
         for(int xLine = p1.x; xLine < p2.x; xLine++){
           int yLine = p1.y + (dy) * (xLine-p1.x)/(dx);
           staticImage.pixels[(yLine+y)*staticImage.width+(xLine+x)] = c1;
         }
       }
      }
     }
   }
}

void keyPressed()
{
  if(key == 'w')
  {
    staticImage = createImage(videoWidth,videoHeight,ARGB);
    camera.update();
    camera.imageCopy(staticImage.pixels);
    staticImage.updatePixels();
  }else if(key == 'i'){
    staticImage = loadImage("manson.jpg");
    staticImage.updatePixels(); 
  }else if(key == 'p'){
    PAUSE = !PAUSE;
  }else if(key == 'b'){
    USE_BLEND_MODE = !USE_BLEND_MODE;
  }
  
  switch(key)
  {
    case '1':
    blendMode = ADD;
    break;
    case '2':
    blendMode = SUBTRACT;
    break;
    case '3':
    blendMode = LIGHTEST;
    break;
    case '4':
    blendMode = DARKEST;
    break;
    case '5':
    blendMode = DIFFERENCE;
    break;
    case '6':
    blendMode = EXCLUSION;
    break;
    case '7':
    blendMode = MULTIPLY;
    break;
    case '8':
    blendMode = SCREEN;
    break;
    case '9':
    blendMode = OVERLAY;
    break;
    case '0':
    blendMode = BLEND;
    break;
  }
}

//OPTICAL FLOW FUNCTIONS

void findFlow() {
  int xOff = w/2;
  int yOff = h/2;
  for (int r=1;r<NROWS;r++) {
    for (int c=1;c<NCOLS;c++) {
      Point p1 = new Point(c*w, r*h);
      Point p2 = findPoint(p1.x, p1.y, xOff, yOff);     
      drawLine(p1,p2);
    }
  }
}

void drawLine(Point p1, Point p2) {
// draw the arrow line from point p2 to point p1.
   if (p1.x!=p2.x || p1.y!=p2.y) {
     line(p1.x,p1.y,p2.x,p2.y);
     float ang = atan2(p2.y-p1.y,p2.x-p1.x);
     float ln = w/3.0;
     float tx = p1.x + ln*cos(ang-PI/6);
     float ty = p1.y + ln*sin(ang-PI/6);
     line(p1.x,p1.y,tx,ty);
     tx = p1.x + ln*cos(ang+PI/6);
     ty = p1.y + ln*sin(ang+PI/6);
     line(p1.x,p1.y,tx,ty);
     
     dataMosh(p1, p2);
   }else {
     line(p1.x,p1.y,p2.x,p2.y);
   }
   
   staticImage.updatePixels();
}

Point findPoint(int _x, int _y, int _xo, int _yo) {

// Given a pixel (_x, _y) in currentFrame, we search the neighborhood of that
// pixel in prevFrame and try to find a matching colour.
// 
// The neighborhood size is defined by (w x h) and the boundaries are
// x0 - left
// x1 - right
// y0 - top
// y1 - right

   int x0 = _x - _xo;
   int x1 = _x + _xo;
   int y0 = _y - _yo;
   int y1 = _y + _yo;

// Initialize the minimum difference to a high value.
// Loop through the pixels in prevFrame within the boundary.
// Find the pixel with minimum difference from the original one 
// in currentFrame.

   float minDiff = 999999999;
   Point p = new Point(_x,_y);
   color c1 = currentFrame.pixels[_y*currentFrame.width+_x];
   color c2 = prevFrame.pixels[_y*prevFrame.width+_x];
   if (!matchCol(c1,c2)) {
      for (int r=y0;r<y1;r++) {
        for (int c=x0;c<x1;c++) {
          c2 = prevFrame.pixels[r*prevFrame.width+c];
          float diff = dist(red(c1),green(c1),blue(c1),
          red(c2),green(c2),blue(c2));
          if (diff<minDiff) {
            minDiff = diff;
            p.x = c;
            p.y = r;
          }
        }
      }
   }
   return p;
}

boolean matchCol(color c1, color c2) {
// Compare two colour values and see if they are similar.
   float d = dist(red(c1),green(c1),blue(c1),
     red(c2),green(c2),blue(c2));
   return (d<LIMIT);
}

class Point {
// It is a temporary data structure to hold a point information.
  int x, y;
  int w, h;
  
  Point(int _x, int _y) {
    x = _x;
    y = _y;
  }
}
