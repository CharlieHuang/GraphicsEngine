package ass1;

public class PiercingProjectile extends DefaultProjectile {

	public PiercingProjectile(GameObject parent, double radius,
			double[] fillColour, double[] lineColour, Direction direction) {
		super(parent, radius, fillColour, lineColour, direction);
		age = 0;
		projectileSpeed = 60;
		myDirection = direction;
	}
	
}
