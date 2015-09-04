package ass1;

public class Player extends CircularGameObject {

	private static double[] playerColour = {0.2,0.5,1,1};
	public static final Player thePlayer = new Player(GameObject.ROOT,1, playerColour, playerColour);
	private static Weapon myWeapon;
	private static double powerupTimer;
	private static double fireRate;
	private static final double defaultFireRate = 30;
	private static double fireTicker;
	public double[] defaultColour = {1,1,1,1};
	public double[] piercingColour = {1,1,0.2,1};
	public double[] BFGColour = {0.2,1,0.2,1};
	private double moveSpeed;
	
	public Player(GameObject parent, double radius, double[] fillColour, double[] lineColour) {
		super(parent, radius, fillColour, lineColour);
		moveSpeed = 10;
		fireRate = 0.25;
		fireTicker = 0.25;
		myWeapon = Weapon.DEFAULT;
		powerupTimer = 10;
	}
	
	public void restartPowerupTimer() {
		powerupTimer = 10;
	}
	
	/**
	 * Change the weapon that the user is wielding
	 * @param weapon
	 */
	public void changeWeapon(Weapon weapon) {
		if (weapon == Weapon.BFG){
			setFireRate(defaultFireRate/2);
		} else if(weapon == Weapon.PIERCING){
			setFireRate(defaultFireRate*2);
		}
		myWeapon = weapon;
	}
	
	/**
	 * Gets the fire rate in seconds/bullet
	 * 
	 * @return
	 */
	public double getFireRate() {
		return fireRate;
	}
	
	/**
	 * Set the fire rate in seconds/bullet
	 * 
	 * @param rate
	 */
	public void setFireRate(double rate) {
		fireRate = rate;
	}
	
	/**
	 * Scale the firing rate
	 * Scaling by half will double the speed
	 * 
	 * @param scale
	 */
	public void scaleFireRate(double scale) {
		fireRate *= scale;
	}
	
	/**
	 * Moves the player depending on what keys are pressed
	 * @param dt
	 * @param movement
	 */
	public void update(double dt, Direction movement, Direction aim) {
		double distance = dt*moveSpeed;
		double diagonal = Math.pow(distance*distance/2, 0.5);
		
		switch (movement) {
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
		
		// Do not let player leave the screen
		double[] myPosition = getPosition();
		myPosition[0] = Math.min(Math.max(myPosition[0],-GameEngine.BORDER),GameEngine.BORDER);
		myPosition[1] = Math.min(Math.max(myPosition[1],-GameEngine.BORDER),GameEngine.BORDER);
		setPosition(myPosition[0], myPosition[1]);
		
		fireTicker += dt;
		Math.max(fireTicker,fireRate);

		// Checking if to see if the powerup has run out
		if (myWeapon != Weapon.DEFAULT) {
			powerupTimer -= dt;
			if (powerupTimer <= 0) {
				if (myWeapon == Weapon.BFG){
					scaleFireRate(0.5);
				}
				myWeapon = Weapon.DEFAULT;
				powerupTimer = 10;
			}
		}
		
		// If not shooting, we stop exit
		if (aim == Direction.NONE) {
			return;
		}
		if (fireTicker >= fireRate) {
			CircularGameObject projectile;
			switch (myWeapon) {
			case DEFAULT:
				projectile = new DefaultProjectile(GameObject.ROOT, DefaultProjectile.defaultRadius, defaultColour, defaultColour, aim);
				projectile.translate(myPosition[0], myPosition[1]);
				break;
			case PIERCING:
				projectile = new PiercingProjectile(GameObject.ROOT, DefaultProjectile.defaultRadius, piercingColour, piercingColour, aim);
				projectile.translate(myPosition[0], myPosition[1]);
				break;
			case BFG:
				for (Direction direction : Direction.values()) {
					if (direction == Direction.NONE){
						continue;
					}
					projectile = new DefaultProjectile(GameObject.ROOT, DefaultProjectile.defaultRadius, BFGColour,BFGColour, direction);
					projectile.translate(myPosition[0], myPosition[1]);
				}
				break;
			default:
				projectile = new DefaultProjectile(GameObject.ROOT, DefaultProjectile.defaultRadius, defaultColour, defaultColour, aim);
				break;
			}
			fireTicker = 0;
		}
		
	}

}
