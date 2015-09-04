package ass1;

public class Enemy extends CircularGameObject {

	private static double moveSpeed;
	public static double[] enemyColour = {1,0.2,0.2,1};
	
	public Enemy(GameObject parent, double radius, double[] fillColour, double[] lineColour) {
		super(parent, radius, fillColour, lineColour);
		moveSpeed = 3;
	}

	/**
	 * Stops the movement of all enemies
	 */
	public void stop() {
		moveSpeed = 0;
	}
	
	public void setSpeed(double speed) {
		moveSpeed = speed;
	}
	
	/**
	 * Updates the enemy
	 * Behaviour of the enemy is to chase the player
	 * 
	 * @param dt
	 * @param playerPosition
	 */
	public void update(double dt, double[] playerPosition) {
		// Enemy is not moving
		if (moveSpeed == 0) {
			return;
		}
		double distance = moveSpeed*dt;
		double[] myPosition = getPosition();
		
		// If x values are same, we cannot get tan
		if (playerPosition[0] - myPosition[0] == 0) {
			if (playerPosition[1] >= myPosition[1]) {
				translate(0,distance);
			} else {
				translate(0,-distance);
			}
			return;
		}
		
		double angle = Math.atan((playerPosition[1] - myPosition[1])/(playerPosition[0] - myPosition[0]));
		double xMove = distance*Math.cos(angle);
		double yMove = distance*Math.sin(angle);
		
		// If player is to the left of enemy, need to flip directions for movement
		if (playerPosition[0] <= myPosition[0]) {
			xMove *= -1;
			yMove *= -1;
		}
		translate(xMove,yMove);
	}
}
