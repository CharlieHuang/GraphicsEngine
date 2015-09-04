package ass1;

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
public class GameEngine implements GLEventListener {

    private Camera myCamera;
    private long myTime;

    /**
     * Construct a new game engine.
     *
     * @param camera The camera that is used in the scene.
     */
    public GameEngine(Camera camera) {
        myCamera = camera;
    }
    
    /**
     * @see javax.media.opengl.GLEventListener#init(javax.media.opengl.GLAutoDrawable)
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        // initialise myTime
        myTime = System.currentTimeMillis();
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
        
        // take a copy of the ALL_OBJECTS list to avoid errors 
        // if new objects are created in the update
        List<GameObject> objects = new ArrayList<GameObject>(GameObject.ALL_OBJECTS);
        
        // update all objects
        for (GameObject g : objects) {
            g.update(dt);
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
    
}
