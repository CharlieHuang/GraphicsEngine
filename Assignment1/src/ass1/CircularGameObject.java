package ass1;

import javax.media.opengl.GL2;

public class CircularGameObject extends GameObject {

	private double myRadius;
	private double[] myCentre = new double[2];
	private double[] myFillColour;
    private double[] myLineColour;
	public static int RESOLUTION = 32;
	
	//Create a CircularGameObject with centre 0,0 and radius 1
	public CircularGameObject(GameObject parent, double[] fillColour,
			double[] lineColour) {
		super(parent);
		myFillColour = fillColour;
		myLineColour = lineColour;
		myCentre[0] = 0;
		myCentre[1] = 0;
		myRadius = 1;
	}

	//Create a CircularGameObject with centre 0,0 and a given radius
	public CircularGameObject(GameObject parent, double radius,
			double[] fillColour, double[] lineColour) {
		super(parent);
		myFillColour = fillColour;
		myLineColour = lineColour;
		myCentre[0] = 0;
		myCentre[1] = 0;
		myRadius = radius;
	}

	/**
	 * Get the radius
	 * 
	 * @return
	 */
	public double getRadius() {
		return myRadius;
	}
	
	/**
	 * Set the radius
	 * 
	 * @param radius
	 */
	public void setRadius(double radius) {
		myRadius = radius;
	}

	/**
	 * Get the centre of the circle
	 * 
	 * 
	 * @return A 2D array in the form {x,y}
	 */
	public double[] getCentre() {
		return myCentre;
	}
	
	/**
	 * Set the centre of the circle
	 * Input must be a 2D array {x,y}
	 * 
	 * @param centre
	 */
	public void setCentre(double[] centre) {
		myCentre = centre;
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
     * Get the points of the circle
     * 
     * @return
     */
    public double[][] getPoints() {
    	return getCirclePoints();
    }

    @Override
    public void drawSelf(GL2 gl) {

    	// Get the points of the circle
    	double[][] myPoints = getCirclePoints();
    	
    	// First draw the filling
    	if (myFillColour!=null) {
    		gl.glColor4d(myFillColour[0], myFillColour[1], myFillColour[2], myFillColour[3]);
        	gl.glBegin(GL2.GL_POLYGON);
        	for (int i=0; i<RESOLUTION;i++) {
        		gl.glVertex2d(myPoints[i][0], myPoints[i][1]);
        	}
           	gl.glEnd();
    	}
    	
    	// Now draw the outline
    	if (myLineColour!=null) {
    		gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2], myLineColour[3]);
    		gl.glBegin(GL2.GL_LINE_LOOP);
        	for (int i=0; i<RESOLUTION;i++) {
        		gl.glVertex2d(myPoints[i][0], myPoints[i][1]);
        	}
        	gl.glEnd();
    	}

    }
	
	/**
	 * Using centre and radius information, generates all the
	 * points of a circle with a given resolution (default 32) 	
	 * 
	 * @param centre
	 * @param radius
	 * @return
	 */
	private double[][] getCirclePoints() {
		double points[][] = new double[RESOLUTION][2];
		double dTheta = 2*Math.PI/RESOLUTION;
		double angle = 0;
		
		for (int i=0; i<RESOLUTION; i++) {
			double x = myRadius * Math.cos(angle);
	    	double y = myRadius * Math.sin(angle);
	    	points[i][0] = myCentre[0] + x;
	    	points[i][1] = myCentre[1] + y;
	    	angle += dTheta;
		}
		return points;
	}

	/**
	 * Check if the point is within the circle
	 */
	@Override
	public boolean pointInObject(double[] point) {
		return (Math.pow(point[0]-myCentre[0],2) + Math.pow(point[1]-myCentre[1],2))
				<= Math.pow(myRadius,2);
	}
	
}
