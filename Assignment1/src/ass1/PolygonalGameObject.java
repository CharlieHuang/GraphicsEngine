package ass1;

import javax.media.opengl.GL2;

/**
 * A game object that has a polygonal shape.
 * 
 * This class extend GameObject to draw polygonal shapes.
 *
 * @author malcolmr
 */
public class PolygonalGameObject extends GameObject {

    private double[] myPoints;
    private double[] myFillColour;
    private double[] myLineColour;

    /**
     * Create a polygonal game object and add it to the scene tree
     * 
     * The polygon is specified as a list of doubles in the form:
     * 
     * [ x0, y0, x1, y1, x2, y2, ... ]
     * 
     * The line and fill colours can possibly be null, in which case that part of the object
     * should not be drawn.
     *
     * @param parent The parent in the scene tree
     * @param points A list of points defining the polygon
     * @param fillColour The fill colour in [r, g, b, a] form
     * @param lineColour The outlien colour in [r, g, b, a] form
     */
    public PolygonalGameObject(GameObject parent, double points[],
            double[] fillColour, double[] lineColour) {
        super(parent);

        myPoints = points;
        myFillColour = fillColour;
        myLineColour = lineColour;
    }

    /**
     * Get the polygon
     * 
     * @return
     */
    public double[] getPoints() {        
        return myPoints;
    }

    /**
     * Set the polygon
     * 
     * @param points
     */
    public void setPoints(double[] points) {
        myPoints = points;
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

    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    

    /**
     * 
     * if the fill colour is non-null, fill the polygon with this colour
     * if the line colour is non-null, draw the outline with this colour
     * 
     * @see ass1.spec.GameObject#drawSelf(javax.media.opengl.GL2)
     */
    @Override
    public void drawSelf(GL2 gl) {

    	// First draw the filling by using a polygon and looping
    	if (myFillColour!=null) {
    		gl.glColor4d(myFillColour[0], myFillColour[1], myFillColour[2], myFillColour[3]);
        	gl.glBegin(GL2.GL_POLYGON);
        	int numPoints = myPoints.length/2;
        	for (int i=0; i<numPoints;i++) {
        		gl.glVertex2d(myPoints[i*2], myPoints[i*2+1]);
        	}
           	gl.glEnd();
    	}
    	
    	// Now draw the outline by using a line loop
    	if (myLineColour!=null) {
    		gl.glColor4d(myLineColour[0], myLineColour[1], myLineColour[2], myLineColour[3]);
    		gl.glBegin(GL2.GL_LINE_LOOP);
    		int numPoints = myPoints.length/2;
        	for (int i=0; i<numPoints;i++) {
        		gl.glVertex2d(myPoints[i*2], myPoints[i*2+1]);
        	}
        	gl.glEnd();
    	}
    	
    }
    
    /**
     * See if the point falls on the polygon
     */
    @Override
    public boolean pointInObject(double[] point) {
    	// The point will be checked using the right ray casting technique
    	boolean oddCrosses = false;
    	int numPoints = myPoints.length/2;
    	int i,j;
    	double[] bottomPoint = new double[2];
    	double[] topPoint = new double[2];
    	
    	// Getting the pair of points (first point is paired with the last point
    	// due to looping of polygon)
    	for (i=0, j= numPoints-1; i < numPoints; j = i++) {
    		// Set up the bottom and top point
    		if (myPoints[i*2+1] >= myPoints[j*2+1]) {
    			topPoint[0] = myPoints[i*2];
    			topPoint[1] = myPoints[i*2+1];
    			bottomPoint[0] = myPoints[j*2];
    			bottomPoint[1] = myPoints[j*2+1];
    		} else {
    			topPoint[0] = myPoints[j*2];
    			topPoint[1] = myPoints[j*2+1];
    			bottomPoint[0] = myPoints[i*2];
    			bottomPoint[1] = myPoints[i*2+1];
    		}
			// Horizontal line case
    		if (topPoint[1] == bottomPoint[1]) {
    			// If point ray casts through the line
    			if (point[1] == topPoint[1]) {
    				// If point is between the two points (inclusive)
    				if(bottomPoint[0] <= point[0] && topPoint[0] >= point[0] ||
    						topPoint[0] <= point[0] && bottomPoint[0] >= point[0]) {
    					oddCrosses = !oddCrosses;
    				}
    			}
    			continue;
    		} 
    		// Now check if our point's y value lies between the y values of the
    		// top and bottom point (and hence whether ray casting horizontally would
    		// potentially cut through the line segment formed by the top and bottom points
    		// We only count the lower vertex (not top) to avoid double counting
    		if (point[1] < topPoint[1] && point[1] >= bottomPoint[1]) {
    			// Linearly interpolate to determine the x value if we were to ray cast
    			// horizontally the y value of our point
    			double x = bottomPoint[0] + (topPoint[0] - bottomPoint[0])*(point[1] - bottomPoint[1])/(topPoint[1] - bottomPoint[1]);
    			// now check to see if this x value is to the right of our point's x value
    			if (x >= point[0]) {
    				oddCrosses = !oddCrosses;
    			}
    		}
    	}
    	return oddCrosses;
    }
    
}
