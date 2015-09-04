package ass1;

import javax.media.opengl.GL2;

public class LineGameObject extends GameObject {

	private double[] Point1 = new double[2];
	private double[] Point2 = new double[2];
	private double[] myLineColour;
	private static final double EPSILON = 0.001;
	
	//Create a LineGameObject from (0,0) to (1,0)
	public LineGameObject(GameObject parent, double[] lineColour) {
		super(parent);
		myLineColour = lineColour;
		Point1[0] = 0;
		Point1[1] = 0;
		Point2[0] = 1;
		Point2[1] = 0;
	}

	//Create a LineGameObject from (x1,y1) to (x2,y2)
	public LineGameObject(GameObject parent,  double x1, double y1, double x2,
			double y2, double[] lineColour) {
		super(parent);
		myLineColour = lineColour;
		Point1[0] = x1;
		Point1[1] = y1;
		Point2[0] = x2;
		Point2[1] = y2;
	}
	
	/**
	 * Returns the first Point {x,y}
	 * 
	 * @return
	 */
	public double[] getFirstPoint() {
		return Point1;
	}
	
	/**
	 * Sets the first point with an array of size 2
	 * Array represents coordinates as {x,y}
	 * 
	 * @param point
	 */
	public void setFirstPoint(double[] point) {
		Point1 = point;
	}
	
	/**
	 * Returns the 2nd point
	 * 
	 * @return
	 */
	public double[] getSecondPoint() {
		return Point2;
	}
	
	/**
	 * Sets the 2nd point
	 * 
	 * @param point
	 */
	public void setSecondPoint(double[] point) {
		Point2 = point;
	}

	/**
	 * Returns both points
	 * First array is which point
	 * 2nd point is the x/y value
	 * e.g. point[0][1] represents the y value of the first point
	 * 
	 * @return
	 */
	public double[][] getPoints() {
		double[][] points = {{Point1[0], Point1[1]}, {Point2[0], Point2[1]}};
		return points;
	}
	
	/**
	 * Sets both points
	 * First array is which point
	 * 2nd point is the x/y value
	 * e.g. point[0][1] represents the y value of the first point
	 * 
	 * @param points
	 */
	public void setPoints(double[][] points) {
		Point1[0] = points[0][0];
		Point1[1] = points[0][1];
		Point2[0] = points[1][0];
		Point2[1] = points[1][1];
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
     * Draw the line from point1 to point2
     * 
     */
    @Override
    public void drawSelf(GL2 gl){
    	
    	if(myLineColour!=null) {
    		gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2], myLineColour[3]);
    		gl.glBegin(GL2.GL_LINES);
    		gl.glVertex2d(Point1[0], Point1[1]);
    		gl.glVertex2d(Point2[0], Point2[1]);
    		gl.glEnd();
    	}
    	
    }
    
    /**
     * See if the point falls on the line (within a margin given by EPSILON) 
     */
    @Override
    public boolean pointInObject(double[] point) {

    	// First find the point projected onto the line Point1-Point2 by the
    	// line Point1-point
    	double pointXDistance = point[0] - Point1[0];
    	double pointYDistance = point[1] - Point1[1];
    	double lineXDistance = Point2[0] - Point1[0];
    	double lineYDistance = Point2[1] - Point1[1];
    	
    	double dotProduct = pointXDistance*lineXDistance + pointYDistance*lineYDistance;
    	double squareLength = lineXDistance*lineXDistance + lineYDistance*lineYDistance;
    	double parameter = -1;
    	
    	// Don't do this if the line is just a point
    	// If the line is just a point, the following calculations
    	// are all the same (finding distance between 2 points)
    	if(squareLength != 0) {
    		parameter = dotProduct/squareLength;
    	}
    	
    	double closestX,closestY;
    	
    	// If the parameter is negative, the point is on the other side of point1
    	if (parameter < 0) {
    		closestX = Point1[0];
    		closestY = Point1[1];
    	} 
    	// If the parameter is greater than 1, the point is on the other side of point2
    	else if (parameter > 1) {
    		closestX = Point2[0];
    		closestY = Point2[1];
    	}
    	// Otherwise the point is between the two points
    	// Need to find the closest point on the line using
    	// projection mathematics
    	else {
    		closestX = Point1[0] + parameter*lineXDistance;
    		closestY = Point1[1] + parameter*lineYDistance;
    	}

    	// Now figure out the distance between the closest point and the given point
    	double dx = closestX - point[0];
    	double dy = closestY - point[1];
    	double Distance = Math.sqrt(dx*dx + dy*dy);
    	
    	return (Distance<=EPSILON);
    }
}
