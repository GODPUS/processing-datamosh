package traer.physics;

public class EulerIntegrator implements Integrator
{
	ParticleSystem s;
	
	public EulerIntegrator( ParticleSystem s )
	{
		this.s = s;
	}
	
	public void step( float t )
	{
		s.clearForces();
		s.applyForces();
		
		for ( int i = 0; i < s.numberOfParticles(); i++ )
		{
			Particle p = (Particle)s.getParticle( i );
			if ( p.isFree() )
			{
				p.velocity().add( p.force().x()/(p.mass()*t), p.force().y()/(p.mass()*t), p.force().z()/(p.mass()*t) );
				p.position().add( p.velocity().x()/t, p.velocity().y()/t, p.velocity().z()/t );
			}
		}
	}

}
