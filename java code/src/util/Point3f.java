package util;
/*
 * Modified by Abraham Campbell on 15/01/2020.
 *   Copyright (c) 2020  Abraham Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
   
   (MIT LICENSE ) e.g do what you want with this :-) 
   
   Modified by Jiaheng Guo£¬
   20210848
 */ 
//Modified from Graphics 3033J course point class  by Abey Campbell 


public class Point3f {

	private float x;
	private float y;
	private float z;
	private double degree;
	
	private int Widthboundary=1250;
	private int Heightboundary=800;
	
	
	
	// default constructor
	public Point3f() { 
		setX(0.0f);
		setY(0.0f);
		setZ(0.0f);
		setDegree(0);
	}
	
	//initializing constructor
	public Point3f(float x, float y, float z) { 
		this.setX(x);
		this.setY(y);
		this.setZ(z); 
	}
	


	// sometimes for different algorithms we will need to address the point using positions 0 1 2 
	public float getPostion(int postion)
	{
		switch(postion)
		{
		case 0: return getX();
		case 1: return getY();
		case 2: return getZ(); 
		default: return Float.NaN;  
		} 
	}
	
	public String toString()
	{
		return ("(" + getX() +"," + getY() +"," + getZ() +")");
    }



	 //implement Point plus a Vector and comment what the method does 
	public Point3f PlusVector(Vector3f Additonal) { 
		return new Point3f(this.getX()+Additonal.getX(), this.getY()+Additonal.getY(), this.getZ()+Additonal.getZ());
	} 
	
	 //implement Point minus a Vector and comment what the method does 
	public Point3f MinusVector(Vector3f Minus) { 
		return new Point3f(this.getX()-Minus.getX(), this.getY()-Minus.getY(), this.getZ()-Minus.getZ());
	}
	
	
	/// implement Point - Point  and comment what the method does  
	public Vector3f MinusPoint(Point3f Minus) { 
		return new Vector3f(this.getX()-Minus.getX(), this.getY()-Minus.getY(), this.getZ()-Minus.getZ());
	}
	 
	
	
//	 Use for direct application of a Vector 
	public void ApplyLocateVector(Vector3f vector) { 
		 setX(CheckWidthBoundary(this.getX()+vector.getX()));
		 setY(CheckHeightBoundary(this.getY()-vector.getY()));
		 
	}
	public void ApplyVector(int speed,double direction) {
		double sin = Math.sin(Math.toRadians(direction%360)),
			   cos = Math.cos(Math.toRadians(direction%360));
		 
		 setX(CheckWidthBoundary((float)(this.getX()+speed*sin)));
		 setY(CheckHeightBoundary((float)(this.getY()-speed*cos)));
		 
	}
	
	

	private float CheckHeightBoundary(float f) {
		if (f<0) f=0.0f;
		if (f>Heightboundary)f=(float) Heightboundary;
		return f;
	}
	
	private float CheckWidthBoundary(float f) {
		if (f<0) f=0.0f;
		if (f>Widthboundary)f=(float) Widthboundary;
		return f;
	}
	public void ApplyTurn(Double x) { 
		 setDegree((this.getDegree()+x));
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	public void setDegree(double degree) {
		this.degree = degree;
	}
	
	public double getDegree() {
		return degree;
	}

	public int getHeightboundary() {
		return Heightboundary;
	}

	public void setHeightboundary(int heightboundary) {
		Heightboundary = heightboundary;
	}
	
	public int getWidthtboundary() {
		return Widthboundary;
	}

	public void setWidthtboundary(int heightboundary) {
		Widthboundary = Widthboundary;
	}


	
	  
	  
	 // Remember point + point  is not defined so we not write a method for it.  
}

/*................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
................................................................................
....................................=?7777+.....................................
.............................,8MMMMMMMMMMMMMMMMM7...............................
...........................$MMMMMMMMMMMMMMMMMMMMMM7.............................
........................IMMMMMMMMMMMMMMMMMMMMMMMMMMMM?..........................
......................?MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN........................
.....................MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM$......................
...................ZMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.....................
..................MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM8....................
.................NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM...................
................IMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM..................
................MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM$.................
...............=MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMZ................
..............:MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM................
..............7MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM:...............
..............DMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMZ...............
..............MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM8...............
..............MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN...............
..............NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMO...............
..............$MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMI...............
..............+MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM=...............
...............8MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM................
................MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM8................
................MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMN,................
................=MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM..................
.................MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMZ..................
..................MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMD...................
...................?MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.....................
....................8MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM......................
.....................,8MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM,.......................
........................NMMMMMMMMMMMMMMMMMMMMMMMMMMMMN?.........................
..........................NMMMMMMMMMMMMMMMMMMMMMMMMMI...........................
.............................$MMMMMMMMMMMMMMMMMMM?..............................
.................................,I$NMMMMMN$?...................................
................................................................................
................................................................................
................................................................................
.......................................................................*/
