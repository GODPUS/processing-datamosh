import processing.opengl.*;
import gifAnimation.*;
import JMyron.*;

PImage img1, img2, staticImage;
PImage[] allFrames;
// img1 and img2 are the location to temporarily store the previous 
// and current frame.
int NROWS, NCOLS;
int PIXELSIZE = 6; //has to be even
final float LIMIT = 30.0;

int w, h;
// w and h are the size of the grid.
JMyron m;

boolean hasStaticImage = false;

boolean useGif = false;
boolean useBlendmode = true;

boolean PAUSE = false;

int blendMode = LIGHTEST;
int videoWidth = 640;
int videoHeight = 480;
   
void setup(){
  
  size(1280, 960, OPENGL);
  //hint(ENABLE_OPENGL_4X_SMOOTH);
  frameRate(60); 

   m = new JMyron();
   m.start(videoWidth, videoHeight);
   m.findGlobs(0);
   
   img1 = createImage(videoWidth,videoHeight,ARGB);
   img2 = createImage(videoWidth,videoHeight,ARGB);
   staticImage = createImage(videoWidth,videoHeight,ARGB);
   
   if(useGif)
   {
     allFrames = Gif.getPImages(this, "gorilla.gif");
     img1.width = allFrames[1].width;
     img1.height = allFrames[1].height;
   }
   
   NROWS = int(img1.height/PIXELSIZE);
   NCOLS = int(img1.width/PIXELSIZE);
   
   w = img1.width/NCOLS;
   h = img1.height/NROWS;
}

int frameCounter = 0;
void draw(){
  if(!PAUSE)
  {
    if(useGif)
    {
      if(frameCounter > allFrames.length-2)
      {
        frameCounter = 0;
      }else{
        frameCounter++;
      }
    }
    
     background(#FFFFFF);
      // copy the image from img1 to img2 - the previous frame.
     img2.copy(img1,0,0,img1.width,img1.height,0,0,img2.width,img2.height);
     img2.updatePixels();
     m.update();
     
     if(useGif)
     {
       img1 = allFrames[frameCounter];
     }else{
       // update the current frame to img1.
       m.imageCopy(img1.pixels);
     }
     img1.updatePixels();
     //image(img1,0,0);
     
     findFlow();
     
     if(hasStaticImage)
     {
       if(useBlendmode){staticImage.blend(img1, 0, 0, videoWidth, videoHeight, 0, 0, videoWidth, videoHeight, blendMode);}
       image(staticImage, videoWidth, 0, videoWidth, videoHeight);
     }
  }
}

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

void drawLine(Point _p, Point _q) {
// draw the arrow line from point _q to point _p.
   if (_p.x!=_q.x || _p.y!=_q.y) {
     line(_p.x,_p.y,_q.x,_q.y);
     float ang = atan2(_q.y-_p.y,_q.x-_p.x);
     float ln = w/3.0;
     float tx = _p.x + ln*cos(ang-PI/6);
     float ty = _p.y + ln*sin(ang-PI/6);
     line(_p.x,_p.y,tx,ty);
     tx = _p.x + ln*cos(ang+PI/6);
     ty = _p.y + ln*sin(ang+PI/6);
     line(_p.x,_p.y,tx,ty);
     
      //Datamosh
     //color c1 = staticImage.pixels[(_q.y)*staticImage.width+(_q.x)];
     
     if(_p.x < staticImage.width && _p.y < staticImage.height)
     {
       for (int r=0-(h/2);r<(h/2);r++) {
        for (int c=0-(w/2);c<(w/2);c++) {
         if(_q.x+c < staticImage.width-1 && _q.y+r < staticImage.height-1)
         {
           color c1 = staticImage.pixels[(_q.y+r)*staticImage.width+(_q.x+c)];
           staticImage.pixels[(_p.y+r)*staticImage.width+(_p.x+c)] = c1;
         }
        }
       }
     }
     
   }else {
     line(_p.x,_p.y,_q.x,_q.y);
   }
   
   staticImage.updatePixels();
}

Point findPoint(int _x, int _y, int _xo, int _yo) {

// Given a pixel (_x, _y) in img1, we search the neighborhood of that
// pixel in img2 and try to find a matching colour.
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
// Loop through the pixels in img2 within the boundary.
// Find the pixel with minimum difference from the original one 
// in img1.

   float minDiff = 999999999;
   Point p = new Point(_x,_y);
   color c1 = img1.pixels[_y*img1.width+_x];
   color c2 = img2.pixels[_y*img2.width+_x];
   if (!matchCol(c1,c2)) {
      for (int r=y0;r<y1;r++) {
        for (int c=x0;c<x1;c++) {
          c2 = img2.pixels[r*img2.width+c];
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

void keyPressed()
{
  if(key == 'w')
  {
    staticImage = createImage(videoWidth,videoHeight,ARGB);
    m.update();
    m.imageCopy(staticImage.pixels);
    staticImage.updatePixels();
    hasStaticImage = true;
  }else if(key == 'i'){
    staticImage = loadImage("manson.jpg");
    staticImage.updatePixels(); 
    hasStaticImage = true;
  }else if(key == 'p'){
    PAUSE = !PAUSE;
  }else if(key == 'b'){
    useBlendmode = !useBlendmode;
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
