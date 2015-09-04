package ass1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.media.opengl.GL2;


/**
 * A GameObject is an object that can move around in the game world.
 * 
 * GameObjects form a scene tree. The root of the tree is the special ROOT object.
 * 
 * Each GameObject is offset from its parent by a rotation, a translation and a scale factor. 
 *
 * @author malcolmr
 */
public class GameObject {

    // the list of all GameObjects in the scene tree
    public final static List<GameObject> ALL_OBJECTS = new ArrayList<GameObject>();
    
    // the root of the scene tree
    public final static GameObject ROOT = new GameObject();
    
    // the links in the scene tree
    private GameObject myParent;
    private List<GameObject> myChildren;

    // the local transformation
    //myRotation should be normalised to the range (-180..180)
    private double myRotation;
    private double myScale;
    private double[] myTranslation;
    
    // is this part of the tree showing?
    private boolean amShowing;

    /**
     * Special private constructor for creating the root node. Do not use otherwise.
     */
    private GameObject() {
        myParent = null;
        myChildren = new ArrayList<GameObject>();

        myRotation = 0;
        myScale = 1;
        myTranslation = new double[2];
        myTranslation[0] = 0;
        myTranslation[1] = 0;

        amShowing = true;
        
        ALL_OBJECTS.add(this);
    }

    /**
     * Public constructor for creating GameObjects, connected to a parent (possibly the ROOT).
     *  
     * New objects are created at the same location, orientation and scale as the parent.
     *
     * @param parent
     */
    public GameObject(GameObject parent) {
        myParent = parent;
        myChildren = new ArrayList<GameObject>();

        parent.myChildren.add(this);

        myRotation = 0;
        myScale = 1;
        myTranslation = new double[2];
        myTranslation[0] = 0;
        myTranslation[1] = 0;

        // initially showing
        amShowing = true;

        ALL_OBJECTS.add(this);
    }

    /**
     * Remove an object and all its children from the scene tree.
     */
    public void destroy() {
        for (GameObject child : myChildren) {
            child.destroy();
        }
        
        myParent.myChildren.remove(this);
        ALL_OBJECTS.remove(this);
    }

    /**
     * Get the parent of this game object
     * 
     * @return
     */
    public GameObject getParent() {
        return myParent;
    }

    /**
     * Get the children of this object
     * 
     * @return
     */
    public List<GameObject> getChildren() {
        return myChildren;
    }

    /**
     * Get the local rotation (in degrees)
     * 
     * @return
     */
    public double getRotation() {
        return myRotation;
    }

    /**
     * Set the local rotation (in degrees)
     * 
     * @return
     */
    public void setRotation(double rotation) {
        myRotation = MathUtil.normaliseAngle(rotation);
    }

    /**
     * Rotate the object by the given angle (in degrees)
     * 
     * @param angle
     */
    public void rotate(double angle) {
        myRotation += angle;
        myRotation = MathUtil.normaliseAngle(myRotation);
    }

    /**
     * Get the local scale
     * 
     * @return
     */
    public double getScale() {
        return myScale;
    }

    /**
     * Set the local scale
     * 
     * @param scale
     */
    public void setScale(double scale) {
        myScale = scale;
    }

    /**
     * Multiply the scale of the object by the given factor
     * 
     * @param factor
     */
    public void scale(double factor) {
        myScale *= factor;
    }

    /**
     * Get the local position of the object 
     * 
     * @return
     */
    public double[] getPosition() {
        double[] t = new double[2];
        t[0] = myTranslation[0];
        t[1] = myTranslation[1];

        return t;
    }

    /**
     * Set the local position of the object
     * 
     * @param x
     * @param y
     */
    public void setPosition(double x, double y) {
        myTranslation[0] = x;
        myTranslation[1] = y;
    }

    /**
     * Move the object by the specified offset in local coordinates
     * 
     * @param dx
     * @param dy
     */
    public void translate(double dx, double dy) {
        myTranslation[0] += dx;
        myTranslation[1] += dy;
    }

    /**
     * Test if the object is visible
     * 
     * @return
     */
    public boolean isShowing() {
        return amShowing;
    }

    /**
     * Set the showing flag to make the object visible (true) or invisible (false).
     * This flag should also apply to all descendents of this object.
     * 
     * @param showing
     */
    public void show(boolean showing) {
        amShowing = showing;
    }

    /**
     * Update the object. This method is called once per frame. 
     * 
     * This does nothing in the base GameObject class. Override this in subclasses.
     * 
     * @param dt The amount of time since the last update (in seconds)
     */
    public void update(double dt) {
        // do nothing
    }

    /**
     * Draw the object (but not any descendants)
     * 
     * This does nothing in the base GameObject class. Override this in subclasses.
     * 
     * @param gl
     */
    public void drawSelf(GL2 gl) {
        // do nothing
    }

    
    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    
    /**
     * Draw the object and all of its descendants recursively.
     * 
     * @param gl
     */
    public void draw(GL2 gl) {
        
        // don't draw if it is not showing
        if (!amShowing) {
            return;
        }
        
        // Set up transform
        gl.glPushMatrix();
        gl.glTranslated(myTranslation[0], myTranslation[1], 0);
        gl.glRotated(myRotation, 0, 0, 1);
        gl.glScaled(myScale, myScale, 1);
        
        // draw self and children
        drawSelf(gl);
        Iterator<GameObject> childIterator = myChildren.iterator();
        while(childIterator.hasNext()) {
        	GameObject child = childIterator.next();
        	child.draw(gl);
        }

        // Pop the transform matrix
        gl.glPopMatrix();
        
    }

    /**
     * Compute the object's position in world coordinates
     * 
     * @return a point in world coordinats in [x,y] form
     */
    public double[] getGlobalPosition() {
        double[] currentVector = {0,0,1};
        double[][] TranslationMatrix;
    	double[][] RotationMatrix;
    	double[][] ScaleMatrix;
    	double[][] TransformMatrix;
        GameObject current = this;

        // Matrix multiply point vector by every transformation matrix up until and including
        // the root. Then you should have a point vector representing global position
        do {
            TranslationMatrix = MathUtil.translationMatrix(current.myTranslation);
        	RotationMatrix = MathUtil.rotationMatrix(current.myRotation);
        	ScaleMatrix = MathUtil.scaleMatrix(current.myScale);
        	TransformMatrix = MathUtil.transformMatrix(TranslationMatrix,RotationMatrix,ScaleMatrix);
            currentVector = MathUtil.multiply(TransformMatrix, currentVector);
            current = current.getParent();
        } while(current!=null);

        double[] position = new double[2];
        position[0] = currentVector[0];
        position[1] = currentVector[1];
        return position;
    }

    /**
     * Compute the object's rotation in the global coordinate frame
     * 
     * @return the global rotation of the object (in degrees) and 
     * normalized to the range (-180, 180) degrees. 
     */
    public double getGlobalRotation() {
        double rotation = myRotation;
        // Just add all rotations together as it is not affected by scale/translation
        if(myParent != null) {
        	rotation += myParent.getGlobalRotation();
        }
        // Make sure to normalise the angle
        return MathUtil.normaliseAngle(rotation);
    }

    /**
     * Compute the object's scale in global terms
     * 
     * @return the global scale of the object 
     */
    public double getGlobalScale() {
        double scale = myScale;
        //Just multiply all scales together as not affected by translation/rotation
        if(myParent != null) {
        	scale *= myParent.getGlobalScale();
        }
    	return scale;
    }

    /**
     * Change the parent of a game object.
     * 
     * @param parent
     */
    public void setParent(GameObject parent) {
    	// Get the global position, rotation and scale.
    	// These must be preserved
    	double[] globalPosition = getGlobalPosition();
    	double globalRotation = getGlobalRotation();
    	double globalScale = getGlobalScale();
    	
    	// Change parents
    	myParent.myChildren.remove(this);
    	myParent = parent;
        myParent.myChildren.add(this);
        
        double[][] TranslationMatrix;
    	double[][] RotationMatrix;
    	double[][] ScaleMatrix;
    	double[][] InverseTransformMatrix = {{1,0,0},{0,1,0},{0,0,1}};
		        
    	// Get the inverse matrix from current parent and all of its ancestors
    	// Needed to calculate local position i.e.
    	// global position = parent matrices * local position
    	// then local position = (parent matrices)^-1 * global position
        double[][] currentInverseMatrix;
        while (parent!=ROOT){
            TranslationMatrix = MathUtil.inverseTranslationMatrix(parent.myTranslation);
        	RotationMatrix = MathUtil.inverseRotationMatrix(parent.myRotation);
        	ScaleMatrix = MathUtil.inverseScaleMatrix(parent.myScale);
        	currentInverseMatrix = MathUtil.inverseTransformMatrix(TranslationMatrix,RotationMatrix,ScaleMatrix);
        	InverseTransformMatrix = MathUtil.multiply(InverseTransformMatrix,currentInverseMatrix);
    		parent = parent.getParent();
    	}
        
    	// Get global position vector
    	double[] globVec = {globalPosition[0],globalPosition[1],1};
    	
    	// Multiplying inverse matrix by the global vector should give you the
    	// new local vector
    	double[] newPos = MathUtil.multiply(InverseTransformMatrix, globVec);
    	
    	// Set the new translation
    	myTranslation[0] = newPos[0];
    	myTranslation[1] = newPos[1];

    	// Find the difference between the global rotation and the parent rotations.
    	// The difference must be this object's rotation. Then normalise the angle
    	myRotation = globalRotation - (getGlobalRotation() - myRotation);
    	myRotation = MathUtil.normaliseAngle(myRotation);
    	// Find the difference between the global scale and the parent scales.
    	// The missing factor must be this object's scale.
    	myScale = globalScale/(getGlobalScale()/myScale);
    }

    /**
     * Determines whether the given point is within the object
     * 
     * Override in subclasses
     * 
     * @param point The point in LOCAL coordinates
     * @return
     */
    public boolean pointInObject(double[] point) {
        return false;
    }

}
