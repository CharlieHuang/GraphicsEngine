package ass1;

public class DefaultProjectile extends CircularGameObject {
	
	protected double age;
	private static final double lifeTime = 5;
	protected double projectileSpeed;
	protected Direction myDirection;
	public static final double defaultRadius = 0.3;
	
	public DefaultProjectile(GameObject parent,double radius, double[] fillColour, double[] lineColour, Direction direction) {
		super(parent, radius, fillColour, lineColour);
		age = 0;
		projectileSpeed = 30;
		myDirection = direction;
	}
	
	public double getSpeed() {
		return projectileSpeed;
	}
	
	public void setSpeed(double speed) {
		projectileSpeed = speed;
	}

	public void scaleSpeed(double scale) {
		projectileSpeed *= scale;
	}

	public void update(double dt) {
		// Check if projectile has expired
		age += dt;
		if (age >= lifeTime) {
			destroy();
		}
		double distance = dt*projectileSpeed;
		double diagonal = Math.pow(distance*distance/2, 0.5);
		
		switch (myDirection) {
		case UP:
			translate(0,distance);
			break;
		case DOWN:
			translate(0,-distance);
			break;
		case LEFT:
			translate(-distance,0);
			break;
		case RIGHT:
			translate(distance,0);
			break;
		case LEFT_UP:
			translate(-diagonal,diagonal);
			break;
		case RIGHT_UP:
			translate(diagonal,diagonal);
			break;
		case LEFT_DOWN:
			translate(-diagonal,-diagonal);
			break;
		case RIGHT_DOWN:
			translate(diagonal,-diagonal);
			break;
		default:
			break;
		}
	}
	
}
