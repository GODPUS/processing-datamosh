package traer.physics;

import java.util.*;

public class RungeKuttaIntegrator implements Integrator
{	
	ArrayList originalPositions;
	ArrayList originalVelocities;
	ArrayList k1Forces;
	ArrayList k1Velocities;
	ArrayList k2Forces;
	ArrayList k2Velocities;
	ArrayList k3Forces;
	ArrayList k3Velocities;
	ArrayList k4Forces;
	ArrayList k4Velocities;
	
	ParticleSystem s;
	
	public RungeKuttaIntegrator( ParticleSystem s )
	{
		this.s = s;
		
		originalPositions = new ArrayList();
		originalVelocities = new ArrayList();
		k1Forces = new ArrayList();
		k1Velocities = new ArrayList();
		k2Forces = new ArrayList();
		k2Velocities = new ArrayList();
		k3Forces = new ArrayList();
		k3Velocities = new ArrayList();
		k4Forces = new ArrayList();
		k4Velocities = new ArrayList();
	}
	
	final void allocateParticles()
	{
		while ( s.particles.size() > originalPositions.size() )
		{
			originalPositions.add( new Vector3D() );
			originalVelocities.add( new Vector3D() );
			k1Forces.add( new Vector3D() );
			k1Velocities.add( new Vector3D() );
			k2Forces.add( new Vector3D() );
			k2Velocities.add( new Vector3D() );
			k3Forces.add( new Vector3D() );
			k3Velocities.add( new Vector3D() );
			k4Forces.add( new Vector3D() );
			k4Velocities.add( new Vector3D() );
		}
	}
	
	public final void step( float deltaT )
	{	
		allocateParticles();
		/////////////////////////////////////////////////////////
		// save original position and velocities
		
		for ( int i = 0; i < s.particles.size(); ++i )
		{
			Particle p = (Particle)s.particles.get( i );
			if ( p.isFree() )
			{		
				((Vector3D)originalPositions.get( i )).set( p.position );
				((Vector3D)originalVelocities.get( i )).set( p.velocity );
			}
			
			p.force.clear();	// and clear the forces
		}
		
		////////////////////////////////////////////////////////
		// get all the k1 values
		
		s.applyForces();
		
		// save the intermediate forces
		for ( int i = 0; i < s.particles.size(); ++i )
		{
			Particle p = (Particle)s.particles.get( i );
			if ( p.isFree() )
			{
				((Vector3D)k1Forces.get( i )).set( p.force );
				((Vector3D)k1Velocities.get( i )).set( p.velocity );
			}
			
			p.force.clear();
		}
		
		////////////////////////////////////////////////////////////////
		// get k2 values
		
		for ( int i = 0; i < s.particles.size(); ++i )
		{
			Particle p = (Particle)s.particles.get( i );
			if ( p.isFree() )
			{
				Vector3D originalPosition = (Vector3D)originalPositions.get( i );
				Vector3D k1Velocity = (Vector3D)k1Velocities.get( i );
				
				p.position.x = originalPosition.x + k1Velocity.x * 0.5f * deltaT;
				p.position.y = originalPosition.y + k1Velocity.y * 0.5f * deltaT;
				p.position.z = originalPosition.z + k1Velocity.z * 0.5f * deltaT;
				
				Vector3D originalVelocity = (Vector3D)originalVelocities.get( i );
				Vector3D k1Force = (Vector3D)k1Forces.get( i );
				
				p.velocity.x = originalVelocity.x + k1Force.x * 0.5f * deltaT / p.mass;
				p.velocity.y = originalVelocity.y + k1Force.y * 0.5f * deltaT / p.mass;
				p.velocity.z = originalVelocity.z + k1Force.z * 0.5f * deltaT / p.mass;
			}
		}
		
		s.applyForces();

		// save the intermediate forces
		for ( int i = 0; i < s.particles.size(); ++i )
		{
			Particle p = (Particle)s.particles.get( i );
			if ( p.isFree() )
			{
				((Vector3D)k2Forces.get( i )).set( p.force );
				((Vector3D)k2Velocities.get( i )).set( p.velocity );
			}
			
			p.force.clear();	// and clear the forces now that we are done with them
		}
		
		
		/////////////////////////////////////////////////////
		// get k3 values
		
		for ( int i = 0; i < s.particles.size(); ++i )
		{
			Particle p = (Particle)s.particles.get( i );
			if ( p.isFree() )
			{
				Vector3D originalPosition = (Vector3D)originalPositions.get( i );
				Vector3D k2Velocity = (Vector3D)k2Velocities.get( i );
				
				p.position.x = originalPosition.x + k2Velocity.x * 0.5f * deltaT;
				p.position.y = originalPosition.y + k2Velocity.y * 0.5f * deltaT;
				p.position.z = originalPosition.z + k2Velocity.z * 0.5f * deltaT;
				
				Vector3D originalVelocity = (Vector3D)originalVelocities.get( i );
				Vector3D k2Force = (Vector3D)k2Forces.get( i );
				
				p.velocity.x = originalVelocity.x + k2Force.x * 0.5f * deltaT / p.mass;
				p.velocity.y = originalVelocity.y + k2Force.y * 0.5f * deltaT / p.mass;
				p.velocity.z = originalVelocity.z + k2Force.z * 0.5f * deltaT / p.mass;
			}
		}
		
		s.applyForces();
		
		// save the intermediate forces
		for ( int i = 0; i < s.particles.size(); ++i )
		{
			Particle p = (Particle)s.particles.get( i );
			if ( p.isFree() )
			{
				((Vector3D)k3Forces.get( i )).set( p.force );
				((Vector3D)k3Velocities.get( i )).set( p.velocity );
			}
			
			p.force.clear();	// and clear the forces now that we are done with them
		}
		
		
		//////////////////////////////////////////////////
		// get k4 values
		
		for ( int i = 0; i < s.particles.size(); ++i )
		{
			Particle p = (Particle)s.particles.get( i );
			if ( p.isFree() )
			{
				Vector3D originalPosition = (Vector3D)originalPositions.get( i );
				Vector3D k3Velocity = (Vector3D)k3Velocities.get( i );
				
				p.position.x = originalPosition.x + k3Velocity.x * deltaT;
				p.position.y = originalPosition.y + k3Velocity.y * deltaT;
				p.position.z = originalPosition.z + k3Velocity.z * deltaT;
				
				Vector3D originalVelocity = (Vector3D)originalVelocities.get( i );
				Vector3D k3Force = (Vector3D)k3Forces.get( i );
				
				p.velocity.x = originalVelocity.x + k3Force.x * deltaT / p.mass;
				p.velocity.y = originalVelocity.y + k3Force.y * deltaT / p.mass;
				p.velocity.z = originalVelocity.z + k3Force.z * deltaT / p.mass;

			}
		}
		
		s.applyForces();
		
		// save the intermediate forces
		for ( int i = 0; i < s.particles.size(); ++i )
		{
			Particle p = (Particle)s.particles.get( i );
			if ( p.isFree() )
			{
				((Vector3D)k4Forces.get( i )).set( p.force );
				((Vector3D)k4Velocities.get( i )).set( p.velocity );
			}
		}
		
		/////////////////////////////////////////////////////////////
		// put them all together and what do you get?
		
		for ( int i = 0; i < s.particles.size(); ++i )
		{
			Particle p = (Particle)s.particles.get( i );
			p.age += deltaT;
			if ( p.isFree() )
			{
				// update position
				
				Vector3D originalPosition = (Vector3D)originalPositions.get( i );
				Vector3D k1Velocity = (Vector3D)k1Velocities.get( i );
				Vector3D k2Velocity = (Vector3D)k2Velocities.get( i );
				Vector3D k3Velocity = (Vector3D)k3Velocities.get( i );
				Vector3D k4Velocity = (Vector3D)k4Velocities.get( i );
				
				p.position.x = originalPosition.x + deltaT / 6.0f * ( k1Velocity.x + 2.0f*k2Velocity.x + 2.0f*k3Velocity.x + k4Velocity.x );
				p.position.y = originalPosition.y + deltaT / 6.0f * ( k1Velocity.y + 2.0f*k2Velocity.y + 2.0f*k3Velocity.y + k4Velocity.y );
				p.position.z = originalPosition.z + deltaT / 6.0f * ( k1Velocity.z + 2.0f*k2Velocity.z + 2.0f*k3Velocity.z + k4Velocity.z );
				
				// update velocity
				
				Vector3D originalVelocity = (Vector3D)originalVelocities.get( i );
				Vector3D k1Force = (Vector3D)k1Forces.get( i );
				Vector3D k2Force = (Vector3D)k2Forces.get( i );
				Vector3D k3Force = (Vector3D)k3Forces.get( i );
				Vector3D k4Force = (Vector3D)k4Forces.get( i );
				
				p.velocity.x = originalVelocity.x + deltaT / ( 6.0f * p.mass ) * ( k1Force.x + 2.0f*k2Force.x + 2.0f*k3Force.x + k4Force.x );
				p.velocity.y = originalVelocity.y + deltaT / ( 6.0f * p.mass ) * ( k1Force.y + 2.0f*k2Force.y + 2.0f*k3Force.y + k4Force.y );
				p.velocity.z = originalVelocity.z + deltaT / ( 6.0f * p.mass ) * ( k1Force.z + 2.0f*k2Force.z + 2.0f*k3Force.z + k4Force.z );
			}
		}
	}
}
