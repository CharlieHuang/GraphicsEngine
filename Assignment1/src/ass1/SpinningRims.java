package ass1;

import javax.media.opengl.GL2;

/**
 * This object is a rim
 * The design is a gold dollar sign
 * The default spin speed is half a revolution/second
 * 
 * @author Charlie
 *
 */
public class SpinningRims extends GameObject {
	
	private double[] myFillColour;
	private double[] myLineColour;
	private double spinSpeed;
	private static final double[] defaultColour = {1,0.85,0,1};
	public static final double defaultSpeed = 180;
    
	public SpinningRims(GameObject parent, double[] fillColour, double[] lineColour) {
		super(parent);
		myFillColour = fillColour;
		myLineColour = lineColour;
		spinSpeed = defaultSpeed;
	}
	
	public SpinningRims(GameObject parent) {
		super(parent);
		myFillColour = defaultColour;
		myLineColour = defaultColour;
		spinSpeed = defaultSpeed;
	}
	
	
	/**
     * Get the fill colour
     * 
     * @return
     */
    public double[] getFillColour() {
        return myFillColour;
    }

    /**
     * Set the fill colour.
     * 
     * Setting the colour to null means the object should not be filled.
     * 
     * @param fillColour The fill colour in [r, g, b, a] form 
     */
    public void setFillColour(double[] fillColour) {
        myFillColour = fillColour;
    }

    /**
     * Get the outline colour.
     * 
     * @return
     */
    public double[] getLineColour() {
        return myLineColour;
    }

    /**
     * Set the outline colour.
     * 
     * Setting the colour to null means the outline should not be drawn
     * 
     * @param lineColour
     */
    public void setLineColour(double[] lineColour) {
        myLineColour = lineColour;
    }
    
    /**
     * Get how fast the rims spin (in degrees/sec)
     * 
     * @return
     */
    public double getSpinSpeed() {
    	return spinSpeed;
    }
    
    /**
     * Set how fast the rims spin (degrees/s)
     * 
     * @param speed
     */
    public void setSpinSpeed(double speed) {
    	spinSpeed = speed;
    }
    
    /**
     * Scale the rim speed by a factor
     * 
     * @param scale
     */
    public void scaleSpinSpeed(double scale) {
    	spinSpeed *= scale;
    }
    
    /**
     * Reverses the direction of spin
     */
    public void reverseSpin() {
    	spinSpeed = -spinSpeed;
    }
    
    @Override
    public void drawSelf(GL2 gl) {
    	
    	// Drawing the dollar sign
    	if(myFillColour!=null) {
    		gl.glColor4d(myFillColour[0], myFillColour[1], myFillColour[2], myFillColour[3]);
    		gl.glBegin(GL2.GL_QUADS);
    		gl.glVertex2d(0.6, 0.7);
    		gl.glVertex2d(-0.3, 0.7);
    		gl.glVertex2d(-0.3, 0.5);
    		gl.glVertex2d(0.6, 0.5);
    		
    		gl.glVertex2d(-0.3, 0.7);
    		gl.glVertex2d(-0.7, 0.3);
    		gl.glVertex2d(-0.4, 0.3);
    		gl.glVertex2d(-0.3, 0.5);

    		gl.glVertex2d(-0.7, 0.3);
    		gl.glVertex2d(0.4,-0.3);
    		gl.glVertex2d(0.7,-0.3);
    		gl.glVertex2d(-0.4, 0.3);
    		
    		gl.glVertex2d(0.3, -0.7);
    		gl.glVertex2d(0.7, -0.3);
    		gl.glVertex2d(0.4, -0.3);
    		gl.glVertex2d(0.3, -0.5);
    		
    		gl.glVertex2d(-0.6, -0.7);
    		gl.glVertex2d(0.3, -0.7);
    		gl.glVertex2d(0.3, -0.5);
    		gl.glVertex2d(-0.6, -0.5);
    		
    		gl.glVertex2d(-0.1, 0.9);
    		gl.glVertex2d(-0.1, -0.9);
    		gl.glVertex2d(0.1, -0.9);
    		gl.glVertex2d(0.1, 0.9);
    		gl.glEnd();
    	}
    	
    }
    
    @Override
    public void update(double dt) {
    	rotate(dt*spinSpeed);
    }
    
}
