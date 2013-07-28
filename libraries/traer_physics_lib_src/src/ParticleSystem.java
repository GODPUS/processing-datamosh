/*
 * May 29, 2005
 */
package traer.physics;

import java.util.*;

public class ParticleSystem
{
	public static final int RUNGE_KUTTA = 0;
	public static final int MODIFIED_EULER = 1;
	
	protected static final float DEFAULT_GRAVITY = 0;
	protected static final float DEFAULT_DRAG = 0.001f;	
	
  ArrayList particles;
  ArrayList springs;
  ArrayList attractions;
  ArrayList customForces = new ArrayList();
 
  Integrator integrator;
  
  Vector3D gravity;
  float drag;

  boolean hasDeadParticles = false;
  
  public final void setIntegrator( int integrator )
  {
	switch ( integrator )
	{
		case RUNGE_KUTTA:
			this.integrator = new RungeKuttaIntegrator( this );
			break;
		case MODIFIED_EULER:
			this.integrator = new ModifiedEulerIntegrator( this );
			break;
	}
  }
  
  public final void setGravity( float x, float y, float z )
  {
	  gravity.set( x, y, z );
  }
  
  // default down gravity
  public final void setGravity( float g )
  {
	  gravity.set( 0, g, 0 );
  }
  
  public final void setDrag( float d )
  {
	  drag = d;
  }
  
  public final void tick()
  {
	  tick( 1 );
  }
  
  public final void tick( float t )
  {  
	  integrator.step( t );
  }
  
  public final Particle makeParticle( float mass, float x, float y, float z )
  {
	  Particle p = new Particle( mass );
	  p.position().set( x, y, z );
	  particles.add( p );
	  return p;
  }
  
  public final Particle makeParticle()
  {  
	  return makeParticle( 1.0f, 0f, 0f, 0f );
  }
  
  public final Spring makeSpring( Particle a, Particle b, float ks, float d, float r )
  {
	  Spring s = new Spring( a, b, ks, d, r );
	  springs.add( s );
	  return s;
  }
  
  public final Attraction makeAttraction( Particle a, Particle b, float k, float minDistance )
  {
	  Attraction m = new Attraction( a, b, k, minDistance );
	  attractions.add( m );
	  return m;
  }
  
  public final void clear()
  {
	  particles.clear();
	  springs.clear();
	  attractions.clear();
  }
  
  public ParticleSystem( float g, float somedrag )
  {
	integrator = new RungeKuttaIntegrator( this );
    particles = new ArrayList();
    springs = new ArrayList();
    attractions = new ArrayList();
    gravity = new Vector3D( 0, g, 0 );
    drag = somedrag;
  }
  
  public ParticleSystem( float gx, float gy, float gz, float somedrag )
  {
	integrator = new RungeKuttaIntegrator( this );
    particles = new ArrayList();
    springs = new ArrayList();
    attractions = new ArrayList();
    gravity = new Vector3D( gx, gy, gz );
    drag = somedrag;
  }
  
  public ParticleSystem()
  {
	integrator = new RungeKuttaIntegrator( this );
	particles = new ArrayList();
	springs = new ArrayList();
	attractions = new ArrayList();
	gravity = new Vector3D( 0, ParticleSystem.DEFAULT_GRAVITY, 0 );
	drag = ParticleSystem.DEFAULT_DRAG;
  }
  
  protected final void applyForces()
  {
	  if ( !gravity.isZero() )
	  {
		  for ( int i = 0; i < particles.size(); ++i )
		  {
			  Particle p = (Particle)particles.get( i );
			  p.force.add( gravity );
		  }
	  }
	
	  for ( int i = 0; i < particles.size(); ++i )
	  {
		  Particle p = (Particle)particles.get( i );
		  p.force.add( p.velocity.x() * -drag, p.velocity.y() * -drag, p.velocity.z() * -drag );
	  }
	  
	  for ( int i = 0; i < springs.size(); i++ )
	  {
		  Spring f = (Spring)springs.get( i );
		  f.apply();
	  }
	  
	  for ( int i = 0; i < attractions.size(); i++ )
	  {
		  Attraction f = (Attraction)attractions.get( i );
		  f.apply();
	  }
	  
	  for ( int i = 0; i < customForces.size(); i++ )
	  {
		  Force f = (Force)customForces.get( i );
		  f.apply();
	  }
  }
  
  protected final void clearForces()
  {
	  Iterator i = particles.iterator();
	  while ( i.hasNext() )
	  {
		  Particle p = (Particle)i.next();
		  p.force.clear();
	  }
  }
  
  public final int numberOfParticles()
  {
	  return particles.size();
  }
  
  public final int numberOfSprings()
  {
	  return springs.size();
  }
  
  public final int numberOfAttractions()
  {
	  return attractions.size();
  }
  
  public final Particle getParticle( int i )
  {
	  return (Particle)particles.get( i );
  }
  
  public final Spring getSpring( int i )
  {
	  return (Spring)springs.get( i );
  }
  
  public final Attraction getAttraction( int i )
  {
	  return (Attraction)attractions.get( i );
  }
  
  public final void addCustomForce( Force f )
  {
	  customForces.add( f );
  }
  
  public final int numberOfCustomForces()
  {
	  return customForces.size();
  }
  
  public final Force getCustomForce( int i )
  {
	  return (Force)customForces.get( i );
  }
  
  public final Force removeCustomForce( int i )
  {
	  return (Force)customForces.remove( i );
  }
  
  public final void removeParticle( Particle p )
  {
	  particles.remove( p );
  }
  
  public final Spring removeSpring( int i )
  {
	  return (Spring)springs.remove( i );
  }
  
  public final Attraction removeAttraction( int i  )
  {
	 return (Attraction)attractions.remove( i );
  }
  
  public final void removeAttraction( Attraction s )
  {
	  attractions.remove( s );
  }
  
  public final void removeSpring( Spring a )
  {
	  springs.remove( a );
  }
  
  public final void removeCustomForce( Force f )
  {
	  customForces.remove( f );
  }
  
}