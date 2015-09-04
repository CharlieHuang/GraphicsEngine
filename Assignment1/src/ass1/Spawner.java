package ass1;

import java.util.Random;

/**
 * Class to help spawn enemies, powerups
 * 
 * @author Charlie
 *
 */
public class Spawner {
	
	 public static final Spawner theSpawner = new Spawner();
	 private static double enemySpawnRate;
	 private static double enemySpawnIncreaseRate;
	 private static double enemySpawnIncreaseTime;
	 private static double enemySpawnTicker;
	 private static double powerupSpawnRate;
	 private static double powerupTicker;
	 private static double enemyTicker;
	 private Random RNG;
	 
	 private Spawner() {
		 enemySpawnRate = 1;
		 powerupSpawnRate = 10;
		 powerupTicker = 0;
		 enemySpawnIncreaseRate = 0.9;
		 enemySpawnIncreaseTime = 10;
		 enemyTicker = 0;
		 enemySpawnTicker = 0;
		 RNG = new Random();
	 }

	 /**
	  * Spawns everything
	  * 
	  * @param dt
	  */
	 public void Spawn(double dt) {
		 SpawnEnemies(dt);
		 SpawnPowerups(dt);
	 }
	 
	 /**
	  * Spawns various powerups, including weapons and
	  * projectile modifiers
	  * 
	  * @param dt
	  */
	 private void SpawnPowerups(double dt) {
		 powerupTicker += dt;
		 if (powerupTicker >= powerupSpawnRate) {
			 // Time to spawn a random powerup
			 
			 double random = RNG.nextDouble();
			 if (random <= 0.7) {
				 PiercingWeapon weapon = new PiercingWeapon(GameObject.ROOT,0.5, PiercingWeapon.weaponColour, PiercingWeapon.weaponColour);
				 weapon.translate(getRandomBetween(-GameEngine.BORDER,GameEngine.BORDER),getRandomBetween(-GameEngine.BORDER,GameEngine.BORDER));
			 } else if (random <= 1) {
				 BFGWeapon weapon = new BFGWeapon(GameObject.ROOT,0.5, BFGWeapon.weaponColour, BFGWeapon.weaponColour);
				 weapon.translate(getRandomBetween(-GameEngine.BORDER,GameEngine.BORDER),getRandomBetween(-GameEngine.BORDER,GameEngine.BORDER));
			 } else {
				 
			 }
			 powerupTicker = 0;
		 }
		 
	 }
	 
	 /**
	  * Spawn enemies, depending on the spawn rate
	  * @param dt
	  */
	 private void SpawnEnemies(double dt) {
		 enemyTicker += dt;
		 while (enemyTicker>enemySpawnRate) {
			Enemy enemy = new Enemy(GameObject.ROOT, 1.2, Enemy.enemyColour, Enemy.enemyColour); 
			enemyTicker -= enemySpawnRate;
			double Pos = getRandomBetween(-GameEngine.BORDER,GameEngine.BORDER);
					
			// Select which border to spawn enemy along
			int border = RNG.nextInt(4);
			if (border == 0) {
				// Left border
				enemy.setPosition(-GameEngine.BORDER, Pos);
			} else if (border == 1) {
				// Bottom border
				enemy.setPosition(Pos,-GameEngine.BORDER);
			} else if (border == 2) {
				// Right border
				enemy.setPosition(GameEngine.BORDER, Pos);
			} else {
				// Top border
				enemy.setPosition(Pos, GameEngine.BORDER);
			}
		 }
		 enemySpawnTicker += dt;
		 if (enemySpawnTicker >= enemySpawnIncreaseTime) {
			 // Time to increase the spawn rates
			 enemySpawnTicker = 0;
			 enemySpawnRate *= enemySpawnIncreaseRate;
		 }
	 }
	 
	 /**
	  * Returns a random double between the lower and
	  * upper bound
	  * @param lowerBound
	  * @param upperBound
	  * @return
	  */
	 private double getRandomBetween(double lowerBound, double upperBound) {
		 return lowerBound + (upperBound-lowerBound)*RNG.nextDouble();
	 }
	 
}
