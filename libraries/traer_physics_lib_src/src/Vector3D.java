package traer.physics;


public class Vector3D
{
	float x;
	float y;
	float z;

	public Vector3D( float X, float Y, float Z )	{ x = X; y = Y; z = Z; }
	public Vector3D()                      				{ x = 0; y = 0; z = 0; }
	public Vector3D( Vector3D p )							{ x = p.x; y = p.y; z = p.z; }
		
	public final float z()									{ return z; }
	public final float y()                   				{ return y; }
	public final float x()                   				{ return x; }
	
	public final void setX( float X )           			{ x = X; }
	public final void setY( float Y )           			{ y = Y; }
	public final void setZ( float Z )						{ z = Z; }
	
	public final void set( float X, float Y, float Z )	{ x = X; y = Y; z = Z; }
	
	public final void set( Vector3D p )						{ x = p.x; y = p.y; z = p.z; }
	
	public final void add( Vector3D p )          				{ x += p.x; y += p.y; z += p.z; }
	public final void subtract( Vector3D p )					{ x -= p.x; y -= p.y; z -= p.z; }
	  
	public final void add( float a, float b, float c )		{ x += a; y += b; z += c; } 
	public final void subtract( float a, float b, float c )		{ x -= a; y -= b; z -= c; } 
		  
	public final Vector3D multiplyBy( float f )					{ x *= f; y *= f; z*= f; return this; }
	
	public final float distanceTo( Vector3D p )  			{ return (float)Math.sqrt( distanceSquaredTo( p ) ); }
	
	public final float distanceSquaredTo( Vector3D p )		{ float dx = x-p.x; float dy = y-p.y; float dz = z-p.z; return dx*dx + dy*dy + dz*dz; }
	
	public final float distanceTo( float x, float y, float z )
	{
		float dx = this.x - x;
		float dy = this.y - y;
		float dz = this.z - z;
		return (float)Math.sqrt( dx*dx + dy*dy + dz*dz );
	}
	
	public final float dot( Vector3D p )         			{ return x*p.x + y*p.y + z*p.z; }
	public final float length()                 			{ return (float)Math.sqrt( x*x + y*y + z*z ); }
	public final float lengthSquared()						{ return x*x + y*y + z*z; }
	  
	public final void clear()                   				{ x = 0; y = 0; z = 0; }

	public final String toString()              				{ return new String( "(" + x + ", " + y + ", " + z + ")" ); }

	public final Vector3D cross( Vector3D p )
	{
		return new Vector3D( 	this.y*p.z - this.z*p.y, 
								this.x*p.z - this.z*p.x,
								this.x*p.y - this.y*p.x );
	}
	
	public boolean isZero()
	{
		return x == 0 && y == 0 && z == 0;
	}
	}
