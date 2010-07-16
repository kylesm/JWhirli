package com.botcave.whirli;

import java.awt.Color;

/**
 * Class to define a Whirligig strategy
 *
 */
public class Strategy extends Object {
    
    private double a, b, c, d;  // Distance formula coefficients
    private double w_velmax;
    private double w_old_coeff, w_dist_coeff, w_vdistmax;
    
    private Color w_strat_color;
    
    private int w_rand_degrees, w_rand_moves;
    
    private int num_whirlis; // Number of whirlis following this strategy
    
    /**
     * Constructor for a Strategy. This object contains the various
     * parameters that a Whirligig Beetle needs to do its job.
     * @param ca The "A" coefficient of movement.
     * @param cb The "B" coefficient of movement.
     * @param cc The "C" coefficient of movement.
     * @param cd The "D" coefficient of movement.
     * @param velmax The maximum velocity that a whirligig following
     * this strategy will move, in cm/sec.
     * @param old_coeff The weight that should be given to the Whirligig's
     * previous speed when calculating his new speed.
     * @param dist_coeff The weight that should be given to the vector
     * calculated by summing up the influences all the other
     * Whirligigs are putting on this Whirli.
     * @param vdistmax The maximum distance (in centimeters) we should
     * consider when calculating a Whirli's influence on us.
     * Any whirligig outside this radius will NOT be
     * included when calculating distances.
     * @param strat_color_rgb RGB value for this Whirligig's color.
     * @param rand_deg The number of degrees to deflect when it's time
     * to be random. Specify this value from 1-360. If you
     * would like a random deflection to be chosen for this
     * Whirligig every random period, then specify a number
     * greater than 360.
     * @param rand_moves The maximum time (in seconds) between "randomness".
     * The Whirligig will actually pick a time between 1
     * and this number, and add it to the last time it was
     * "random".
     */    
    public Strategy(double ca, double cb, double cc, double cd,
                    double velmax, double old_coeff, double dist_coeff, double vdistmax,
                    int strat_color_rgb,
                    int rand_deg, int rand_moves, int strat_num_whirlis) {
        a = ca;
        b = cb;
        c = cc;
        d = cd;
        
        w_velmax = velmax;
        w_old_coeff = old_coeff;
        w_dist_coeff = dist_coeff;
        w_vdistmax = vdistmax;
        
        w_strat_color = new Color(strat_color_rgb);
        
        w_rand_degrees = rand_deg;
        w_rand_moves = rand_moves;
        
        num_whirlis = strat_num_whirlis;
    }
    
    /**
     * Get the number of degrees to deflect when randomized.
     * @return Degrees to deflect during a random time.
     */    
    public int getW_rand_degrees() {
        return(w_rand_degrees);
    }
    
    /**
     * Get the length of time (in seconds) between random deflections.
     * @return Length of time (in seconds).
     */
    public int getW_rand_moves() {
        return(w_rand_moves);
    }
    
    public void assigned() {
        num_whirlis--;
    }
    
    public boolean isFullyAssigned() {
        if(num_whirlis <= 0) {
            return(true);
        } else {
            return(false);
        }
    }
    
    public void setNum_whirlis(int nw) {
        num_whirlis = nw;
    }
    
    public int getNum_whirlis() {
        return(num_whirlis);
    }
    
    public Color getW_strat_color() {
        return(w_strat_color);
    }
    
    public String toString() {
        return("Strategy NW:"+getNum_whirlis());
    }
    
    public double getW_vdistmax() {
        return(w_vdistmax);
    }
    
    public double getA() {
        return(a);
    }
    
    public double getB() {
        return(b);
    }
    
    public double getC() {
        return(c);
    }
    
    public double getD() {
        return(d);
    }
    
    public double getW_velmax() {
        return(w_velmax);
    }
    
    public double getW_dist_coeff() {
        return(w_dist_coeff);
    }
    
    public double getW_old_coeff() {
        return(w_old_coeff);
    }
    
}
