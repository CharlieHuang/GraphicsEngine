package ass1;

import javax.media.opengl.GL2;

/**
 * This object is a car with 2 child wheels attached to the body
 * Then there are 2 child spinning rims attached to the wheels
 * Requires animation
 * 
 * By default, the outlines are red
 * By default, the car is gold, the wheels are silver/gray and
 * the rims are gold
 * 
 * @author Charlie
 *
 */
public class MyCoolGameObject extends GameObject {

	private double[] myFillColour;
    private double[] myLineColour;
	private static final double[] defaultFillColour = {0.95,0.1,0.1,1};
	private static final double[] defaultLineColour = {0.8,0.0,0.0,1};
	private static final double[] defaultWheelColour = {0.9,0.9,0.9,1};
	private CircularGameObject wheel1;
    private CircularGameObject wheel2;
	private SpinningRims rim1;
	private SpinningRims rim2;
	
	public MyCoolGameObject() {
		this(GameObject.ROOT);
	}
	
	public MyCoolGameObject(GameObject parent) {
		super(parent);
		// Setting up car body
		myFillColour = defaultFillColour;
		myLineColour = defaultLineColour;
		
		// Setting up car wheels
		double[] pos1 = {-0.6,-0.4};
		double[] pos2 = {0.6,-0.4};
		wheel1 = new CircularGameObject(this, 0.25, defaultWheelColour, defaultLineColour);
		wheel1.setPosition(pos1[0],pos1[1]);
		wheel2 = new CircularGameObject(this, 0.25, defaultWheelColour, defaultLineColour);
		wheel2.setPosition(pos2[0],pos2[1]);
		
		// Setting up car rims
		rim1 = new SpinningRims(wheel1);
		rim1.setScale(0.2);
		rim2 = new SpinningRims(wheel2);
		rim2.setScale(0.2);
	}
	
	/**
	 * Gets the Fill colour
	 * 
	 * @return
	 */
	public double[] getFillColour() {
		return myFillColour;
	}
	
	public void setFillColour(double[] fillColour) {
		myFillColour = fillColour;
	}
	
	public double[] getLineColour() {
		return myLineColour;
	}
	
	public void setLineColour(double[] lineColour) {
		myLineColour = lineColour;
	}
	
	@Override
	public void drawSelf(GL2 gl) {
		
		// First draw the car body
		if (myFillColour!=null) {
    		gl.glColor4d(myFillColour[0], myFillColour[1], myFillColour[2], myFillColour[3]);
    		gl.glBegin(GL2.GL_QUADS);
    		gl.glVertex2d(-1,-0.4);
    		gl.glVertex2d(1,-0.4);
    		gl.glVertex2d(1,0);
    		gl.glVertex2d(-1,0);
    		
    		gl.glVertex2d(-0.5,0);
    		gl.glVertex2d(1,0);
    		gl.glVertex2d(0.9,0.3);
    		gl.glVertex2d(-0.4,0.3);
    		gl.glEnd();
		}
		// Next draw the outline of the car
		if (myLineColour!=null){
			gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2], myLineColour[3]);
    		gl.glBegin(GL2.GL_LINE_LOOP);
    		gl.glVertex2d(-1,0);
    		gl.glVertex2d(-1,-0.4);
    		gl.glVertex2d(1,-0.4);
    		gl.glVertex2d(1,0);
    		gl.glVertex2d(0.9,0.3);
    		gl.glVertex2d(-0.4,0.3);
    		gl.glVertex2d(-0.5,0);
    		gl.glEnd();
		}
		
		// Adding a window
		gl.glColor3d(0.2,0.2,0.2);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2d(-0.35, 0);
		gl.glVertex2d(0.5, 0);
		gl.glVertex2d(0.5, 0.2);
		gl.glVertex2d(-0.2833, 0.2);
		gl.glEnd();
		
	}
	
	public void update(double dt, Direction direction) {
		switch (direction){
		case LEFT:
			rim1.setSpinSpeed(rim1.getSpinSpeed() + dt*SpinningRims.defaultSpeed);
			rim2.setSpinSpeed(rim2.getSpinSpeed() + dt*SpinningRims.defaultSpeed);
			break;
		case RIGHT:
			rim1.setSpinSpeed(rim1.getSpinSpeed() - dt*SpinningRims.defaultSpeed);
			rim2.setSpinSpeed(rim2.getSpinSpeed() - dt*SpinningRims.defaultSpeed);
			break;
		default:
			break;
		}
	}
	
}
