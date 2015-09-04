package ass1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * The GameEngine is the GLEventListener for our game.
 * 
 * Every object in the scene tree is updated on each display call.
 * Then the scene tree is rendered.
 *
 * You shouldn't need to modify this class.
 *
 * @author malcolmr
 */
public class GameEngine implements GLEventListener, KeyListener {

	public static final double BORDER = 30;
	private Direction movement;
	private Direction aim;
	
    private Camera myCamera;
    private long myTime;

    /**
     * Construct a new game engine.
     *
     * @param camera The camera that is used in the scene.
     */
    public GameEngine(Camera camera) {
        myCamera = camera;
        movement = Direction.NONE;
        aim = Direction.NONE;
    }
    
    /**
     * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        // initialise myTime
        myTime = System.currentTimeMillis();
        Player player = Player.thePlayer;

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // ignore
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {
        
        // tell the camera and the mouse that the screen has reshaped
        GL2 gl = drawable.getGL().getGL2();

        myCamera.reshape(gl, x, y, width, height);
        
        // this has to happen after myCamera.reshape() to use the new projection
        Mouse.theMouse.reshape(gl);
    }


    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // set the view matrix based on the camera position
        myCamera.setView(gl);
        
        // update the mouse position
        Mouse.theMouse.update(gl);
        
        // update the objects
        update();

        // draw the scene tree
        GameObject.ROOT.draw(gl);        
    }

    private void update() {
        
        // compute the time since the last frame
        long time = System.currentTimeMillis();
        double dt = (time - myTime) / 1000.0;
        myTime = time;
        
        // Spawn things that need to be spawned
        Spawner.theSpawner.Spawn(dt);
        
        // take a copy of the ALL_OBJECTS list to avoid errors 
        // if new objects are created in the update
        List<GameObject> objects = new ArrayList<GameObject>(GameObject.ALL_OBJECTS);
        
        // update all objects
        for (GameObject g : objects) {
        	// Update player
        	if (g instanceof Player) {
            ((Player)g).update(dt,movement,aim);
        	} else if (g instanceof Enemy) {
        		((Enemy)g).update(dt,Player.thePlayer.getGlobalPosition());
        	} else {
        		g.update(dt);
        	}
        }
        
        // Check what has collided with the player
        List<GameObject> colliders = collision(Player.thePlayer.getGlobalPosition());
    	for (GameObject g : colliders) {
    		// If an enemy collides with the player, game over
    		if (g instanceof Enemy) {
    			Player.thePlayer.destroy();
    			((Enemy)g).setSpeed(1);
    		}
    	}
    	
    	// Check if powerup has collided with player (since powerups are smaller)
    	for (GameObject g : objects) {
    		List<GameObject> wepColliders = collision(g.getGlobalPosition());
    		if (wepColliders.contains(Player.thePlayer)) {
	    		if (g instanceof PiercingWeapon) {
	    			Player.thePlayer.changeWeapon(Weapon.PIERCING);
	    			Player.thePlayer.restartPowerupTimer();
	    			g.destroy();
	    		} else if (g instanceof BFGWeapon) {
	    			Player.thePlayer.changeWeapon(Weapon.BFG);
	    			Player.thePlayer.restartPowerupTimer();
	    			g.destroy();
	    		}
    		}
    	}
    	
    	
    	// Check if any enemies have collided with a projectile
    	for (GameObject g : objects) {
    		if (g instanceof PiercingProjectile) {
    			List<GameObject> contacted = collision(g.getGlobalPosition());
    			for (GameObject h : contacted) {
    				// If the projectile touched an enemy, we destroy it
    				if (h instanceof Enemy) {
    					h.destroy();
    				}
    			}
    		} else if (g instanceof DefaultProjectile) {
    			List<GameObject> contacted = collision(g.getGlobalPosition());
    			for (GameObject h : contacted) {
    				// If the projectile touched an enemy, we destroy it
    				if (h instanceof Enemy) {
    					h.destroy();
    					g.destroy();
    				}
    			}
    		}
    	}

    }

    /**
     * Returns a list of game objects that contain the given point
     * 
     * @param p The coordinate in world coordinates
     * @return
     */
    public List<GameObject> collision(double[] p) {
    	List<GameObject> list = new ArrayList<GameObject>();
    	List<GameObject> objects = new ArrayList<GameObject>(GameObject.ALL_OBJECTS);
    	GameObject current;
    	double[] globalPoint =  {p[0],p[1],1};
    	double[] localPoint;
    	double[][] iScale;
    	double[][] iRotation;
    	double[][] iTranslation;
    	double[][] currentInverseTransform;
    	
    	// Add the object to the list we are returning if the point
    	// is inside the object
    	for (GameObject g : objects) {
    		
    		// First change the point into the local frame for that object
    		// Do this by pre-multiplying the global coordinate via an inverse
    		// matrix determined by multiplying all the inverse matrices of the
    		// object's ancestors, including the object
    		current = g;
    		double[][] InverseTransformMatrix = {{1,0,0},{0,1,0},{0,0,1}};
    		do {
    			iScale = MathUtil.inverseScaleMatrix(current.getScale());
    			iRotation = MathUtil.inverseRotationMatrix(current.getRotation());
    			iTranslation = MathUtil.inverseTranslationMatrix(current.getPosition());
    			currentInverseTransform = MathUtil.multiply(MathUtil.multiply(iScale, iRotation), iTranslation);
    			InverseTransformMatrix = MathUtil.multiply(InverseTransformMatrix, currentInverseTransform);
    			current = current.getParent();
    		} while (current!=null);
    		localPoint = MathUtil.multiply(InverseTransformMatrix, globalPoint);
    		
    		// Then check if point in local coordinates is in the object
    		if (g.pointInObject(localPoint)){
    			list.add(g);
    		}
    	}
    	return list;
    }

    /**
     * Changes the movement and aim based on keys pressed
     * 
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
        	switch(aim) {
        	case NONE:
        		aim = Direction.LEFT;
        		break;
        	case UP:
        		aim = Direction.LEFT_UP;
        		break;
        	case RIGHT_UP:
        		aim = Direction.LEFT_UP;
        		break;
        	case RIGHT:
        		aim = Direction.LEFT;
        		break;
        	case RIGHT_DOWN:
        		aim = Direction.LEFT_DOWN;
        		break;
        	case DOWN:
        		aim = Direction.LEFT_DOWN;
        		break;
       		default:
       			break;
        	}
            break;

        case KeyEvent.VK_RIGHT:
        	
        	switch(aim) {
        	case NONE:
        		aim = Direction.RIGHT;
        		break;
        	case UP:
        		aim = Direction.RIGHT_UP;
        		break;
        	case LEFT_UP:
        		aim = Direction.RIGHT_UP;
        		break;
        	case LEFT:
        		aim = Direction.RIGHT;
        		break;
        	case LEFT_DOWN:
        		aim = Direction.RIGHT_DOWN;
        		break;
        	case DOWN:
        		aim = Direction.RIGHT_DOWN;
        		break;
       		default:
       			break;
        	}
            break;
            
        case KeyEvent.VK_UP:
        	
        	switch(aim) {
        	case NONE:
        		aim = Direction.UP;
        		break;
        	case LEFT:
        		aim = Direction.LEFT_UP;
        		break;
        	case LEFT_DOWN:
        		aim = Direction.LEFT_UP;
        		break;
        	case DOWN:
        		aim = Direction.UP;
        		break;
        	case RIGHT_DOWN:
        		aim = Direction.RIGHT_UP;
        		break;
        	case RIGHT:
        		aim = Direction.RIGHT_UP;
        		break;
       		default:
       			break;
        	}
            break;
        
        case KeyEvent.VK_DOWN:
        	
        	switch(aim) {
        	case NONE:
        		aim = Direction.DOWN;
        		break;
        	case UP:
        		aim = Direction.DOWN;
        		break;
        	case RIGHT_UP:
        		aim = Direction.RIGHT_DOWN;
        		break;
        	case RIGHT:
        		aim = Direction.RIGHT_DOWN;
        		break;
        	case LEFT_UP:
        		aim = Direction.LEFT_DOWN;
        		break;
        	case LEFT:
        		aim = Direction.LEFT_DOWN;
        		break;
       		default:
       			break;
        	}
            break;
            
        case KeyEvent.VK_A:
        	
        	switch(movement) {
        	case NONE:
        		movement = Direction.LEFT;
        		break;
        	case UP:
        		movement = Direction.LEFT_UP;
        		break;
        	case RIGHT_UP:
        		movement = Direction.LEFT_UP;
        		break;
        	case RIGHT:
        		movement = Direction.LEFT;
        		break;
        	case RIGHT_DOWN:
        		movement = Direction.LEFT_DOWN;
        		break;
        	case DOWN:
        		movement = Direction.LEFT_DOWN;
        		break;
       		default:
       			break;
        	}
            break;

        case KeyEvent.VK_D:
        	
        	switch(movement) {
        	case NONE:
        		movement = Direction.RIGHT;
        		break;
        	case UP:
        		movement = Direction.RIGHT_UP;
        		break;
        	case LEFT_UP:
        		movement = Direction.RIGHT_UP;
        		break;
        	case LEFT:
        		movement = Direction.RIGHT;
        		break;
        	case LEFT_DOWN:
        		movement = Direction.RIGHT_DOWN;
        		break;
        	case DOWN:
        		movement = Direction.RIGHT_DOWN;
        		break;
       		default:
       			break;
        	}
            break;
            
        case KeyEvent.VK_W:
        	
        	switch(movement) {
        	case NONE:
        		movement = Direction.UP;
        		break;
        	case LEFT:
        		movement = Direction.LEFT_UP;
        		break;
        	case LEFT_DOWN:
        		movement = Direction.LEFT_UP;
        		break;
        	case DOWN:
        		movement = Direction.UP;
        		break;
        	case RIGHT_DOWN:
        		movement = Direction.RIGHT_UP;
        		break;
        	case RIGHT:
        		movement = Direction.RIGHT_UP;
        		break;
       		default:
       			break;
        	}
            break;
        
        case KeyEvent.VK_S:
        	
        	switch(movement) {
        	case NONE:
        		movement = Direction.DOWN;
        		break;
        	case UP:
        		movement = Direction.DOWN;
        		break;
        	case RIGHT_UP:
        		movement = Direction.RIGHT_DOWN;
        		break;
        	case RIGHT:
        		movement = Direction.RIGHT_DOWN;
        		break;
        	case LEFT_UP:
        		movement = Direction.LEFT_DOWN;
        		break;
        	case LEFT:
        		movement = Direction.LEFT_DOWN;
        		break;
       		default:
       			break;
        	}
            break;
                
        }
    }

    /**
     * 
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
        	
        	switch (aim) {
        	case LEFT:
        		aim = Direction.NONE;
        		break;
        	case LEFT_UP:
        		aim = Direction.UP;
        		break;
        	case LEFT_DOWN:
        		aim = Direction.DOWN;
        		break;
        	default:
        		break;
        	}
        	break;
        	
        case KeyEvent.VK_RIGHT:
        	
        	switch (aim) {
        	case RIGHT:
        		aim = Direction.NONE;
        		break;
        	case RIGHT_UP:
        		aim = Direction.UP;
        		break;
        	case RIGHT_DOWN:
        		aim = Direction.DOWN;
        		break;
        	default:
        		break;
        	}
        	break;
        	
        case KeyEvent.VK_UP:
        	
        	switch(aim) {
        	case UP:
        		aim = Direction.NONE;
        		break;
        	case LEFT_UP:
        		aim = Direction.LEFT;
        		break;
        	case RIGHT_UP:
        		aim = Direction.RIGHT;
        		break;
        	default:
        		break;
        	}
        	break;
        	
        case KeyEvent.VK_DOWN:
        	
        	switch(aim) {
        	case DOWN:
        		aim = Direction.NONE;
        		break;
        	case LEFT_DOWN:
        		aim = Direction.LEFT;
        		break;
        	case RIGHT_DOWN:
        		aim = Direction.RIGHT;
        		break;
        	default:
        		break;
        	}
        	break;
        	
        case KeyEvent.VK_A:
        	
        	switch (movement) {
        	case LEFT:
        		movement = Direction.NONE;
        		break;
        	case LEFT_UP:
        		movement = Direction.UP;
        		break;
        	case LEFT_DOWN:
        		movement = Direction.DOWN;
        		break;
        	default:
        		break;
        	}
        	break;
        	
        case KeyEvent.VK_D:
        	
        	switch (movement) {
        	case RIGHT:
        		movement = Direction.NONE;
        		break;
        	case RIGHT_UP:
        		movement = Direction.UP;
        		break;
        	case RIGHT_DOWN:
        		movement = Direction.DOWN;
        		break;
        	default:
        		break;
        	}
        	break;
        	
        case KeyEvent.VK_W:
        	
        	switch(movement) {
        	case UP:
        		movement = Direction.NONE;
        		break;
        	case LEFT_UP:
        		movement = Direction.LEFT;
        		break;
        	case RIGHT_UP:
        		movement = Direction.RIGHT;
        		break;
        	default:
        		break;
        	}
        	break;
        	
        case KeyEvent.VK_S:
        	
        	switch(movement) {
        	case DOWN:
        		movement = Direction.NONE;
        		break;
        	case LEFT_DOWN:
        		movement = Direction.LEFT;
        		break;
        	case RIGHT_DOWN:
        		movement = Direction.RIGHT;
        		break;
        	default:
        		break;
        	}
        	break;
        	
        	
        }
        
    }

	@Override
	public void keyTyped(KeyEvent e) {
	}
    
}
