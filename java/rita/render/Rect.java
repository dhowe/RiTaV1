package rita.render;

public class Rect 
{  
  public float x,y,w,h; 

  public Rect() {}

  public Rect(float x, float y,float w, float h)
  {
    this.set(x,y,w,h);
  }
  
  public Rect(double x, double y, double w, double h)
  {
    this((float)x,(float)y,(float)w,(float)h);
  }

  public boolean contains(float mx, float my)
  {
    return (mx >= this.x && my >= this.y && 
        mx < this.x + this.w && my < this.y + this.h);
  }

  public void set(float x, float y, float w, float h)
  {
    this.x=x;
    this.y=y;
    this.w=w;
    this.h=h;
  }

  public float[] asArray()
  {
    return new float[] { this.x, this.y, w, h };
  }
  
  public String toString()
  {
    return "["+this.x+","+ this.y+","+ w+","+h+"]";
  }
}