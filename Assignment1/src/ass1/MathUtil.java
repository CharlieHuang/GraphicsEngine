package ass1;

/**
 * A collection of useful math methods 
 *
 * @author malcolmr
 */
public class MathUtil {

    /**
     * Normalise an angle to the range [-180, 180)
     * 
     * @param angle 
     * @return
     */
    static public double normaliseAngle(double angle) {
        return ((angle + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
    }

    /**
     * Clamp a value to the given range
     * 
     * @param value
     * @param min
     * @param max
     * @return
     */

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Multiply two matrices
     * 
     * @param p A 3x3 matrix
     * @param q A 3x3 matrix
     * @return
     */
    public static double[][] multiply(double[][] p, double[][] q) {

        double[][] m = new double[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                   m[i][j] += p[i][k] * q[k][j]; 
                }
            }
        }

        return m;
    }

    /**
     * Multiply a vector by a matrix
     * 
     * @param m A 3x3 matrix
     * @param v A 3x1 vector
     * @return
     */
    public static double[] multiply(double[][] m, double[] v) {

        double[] u = new double[3];

        for (int i = 0; i < 3; i++) {
            u[i] = 0;
            for (int j = 0; j < 3; j++) {
                u[i] += m[i][j] * v[j];
            }
        }

        return u;
    }



    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    

    /**
     * A 2D translation matrix for the given offset vector
     * 
     * @param pos
     * @return
     */
    public static double[][] translationMatrix(double[] v) {
    	double translationMatrix[][] = {{1,0,v[0]},{0,1,v[1]},{0,0,1}};
        return translationMatrix;
    }

    /**
     * A 2D inversed translation matrix for the given offset vector
     * 
     * @param pos
     * @return
     */
    public static double[][] inverseTranslationMatrix(double[] v) {
    	double translationMatrix[][] = {{1,0,-v[0]},{0,1,-v[1]},{0,0,1}};
        return translationMatrix;
    }

    /**
     * A 2D rotation matrix for the given angle
     * 
     * @param angle in degrees
     * @return
     */
    public static double[][] rotationMatrix(double angle) {
    	double rads = angle*Math.PI/180;
    	double rotationMatrix[][] = {{Math.cos(rads),-Math.sin(rads),0}, {Math.sin(rads),Math.cos(rads),0}, {0,0,1}};
        return rotationMatrix;
    }
    
    /**
     * A 2D inverse rotation matrix for the given angle
     * 
     * @param angle in degrees
     * @return
     */
    public static double[][] inverseRotationMatrix(double angle) {
    	double rads = -angle*Math.PI/180;
    	double rotationMatrix[][] = {{Math.cos(rads),-Math.sin(rads),0}, {Math.sin(rads),Math.cos(rads),0}, {0,0,1}};
        return rotationMatrix;
    }

    /**
     * A 2D scale matrix that scales both axes by the same factor
     * 
     * @param scale
     * @return
     */
    public static double[][] scaleMatrix(double scale) {
    	double scaleMatrix[][] = {{scale,0,0},{0,scale,0},{0,0,1}};
        return scaleMatrix;
    }

    /**
     * A 2D inverse scale matrix that scales both axes by the same factor
     * 
     * @param scale
     * @return
     */
    public static double[][] inverseScaleMatrix(double scale) {
    	double scaleMatrix[][] = {{1.0/scale,0,0},{0,1.0/scale,0},{0,0,1}};
        return scaleMatrix;
    }

    /**
     * A 2D transform matrix for the given translation,
     * rotation and scale matrices
     * 
     * @return
     */
    public static double[][] transformMatrix(double[][] translation, double[][] rotation, double[][] scale) {
        double[][] transformationMatrix = new double[3][3];
        transformationMatrix = (multiply(multiply(translation,rotation),scale));
    	return transformationMatrix;
    }
    
    /**
     * A 2D inverse transform matrix for the given inverse translation,
     * inverse rotation and inverse scale matrices
     * 
     * @return
     */
    public static double[][] inverseTransformMatrix(double[][] iTranslation, double[][] iRotation, double[][] iScale) {
        double[][] iTransformationMatrix = new double[3][3];
        iTransformationMatrix = (multiply(multiply(iScale,iRotation),iTranslation));
    	return iTransformationMatrix;
    }
    
    
}
