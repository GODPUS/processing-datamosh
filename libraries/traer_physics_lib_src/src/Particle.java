
package traer.physics;

public class Particle
{
  protected Vector3D position;
  protected Vector3D velocity;
  protected Vector3D force;
  protected float mass;
  protected float age;
  protected boolean dead;
  
  boolean fixed;
	
  public Particle( float m )
  {
	  position = new Vector3D();
	  velocity = new Vector3D();
	  force = new Vector3D();
	  mass = m;
	  fixed = false;
	  age = 0;
	  dead = false;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#distanceTo(traer.physics.Particle)
 */
public final float distanceTo( Particle p )
  {
	  return this.position().distanceTo( p.position() );
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#makeFixed()
 */
public final void makeFixed()
  {
	  fixed = true;
	  velocity.clear();
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#isFixed()
 */
public final boolean isFixed()
  {
	  return fixed;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#isFree()
 */
public final boolean isFree()
  {
	  return !fixed;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#makeFree()
 */
public final void makeFree()
  {
	  fixed = false;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#position()
 */
public final Vector3D position()
  {
	  return position;
  }
  
  public final Vector3D velocity()
  {
	  return velocity;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#mass()
 */
public final float mass()
  {
	  return mass;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#setMass(float)
 */
public final void setMass( float m )
  {
	  mass = m;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#force()
 */
public final Vector3D force()
  {
	  return force;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#age()
 */
public final float age()
  {
	  return age;
  }
  
  protected void reset()
  {
	  age = 0;
	  dead = false;
	  position.clear();
	  velocity.clear();
	  force.clear();
	  mass = 1f;
  }
}
