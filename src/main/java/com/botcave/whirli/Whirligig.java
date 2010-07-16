package com.botcave.whirli;

import java.awt.Color;
import java.awt.Shape;
import java.util.Random;
import java.util.Vector;

/**
 * Class to define a single Whirligig beetle.
 *
 * Capable of determining its next move based on strategy.
 *
 * Everything in this class that needs real-world units is held in centimeters.
 * The WhirliPond class worries about scaling to the real pond size.
 */
public class Whirligig extends Object {
    
    private double x, y;         // Coordinates for this Whirli (in centimeters from upper left)
    private double xvel, yvel;   // Current velocity in each direction (in cm per move)

    private double degrees;      // Degrees off of 0 this Whirli is rotated
    private Vector distances;    // Vector of distances to other Whirlis (in cm)
    
    private int status;          // Current status of this whirli;
    private int inport;          // Num of Whirlis in visible port
    
    private long lastRandomTime; // Last time (in Milliseconds from The Epoch) we were random
    private long nextRandomTime; // Next time (in Milliseconds from The Epoch) we will be random
    
    // Graphics-related information will go here
    
    private Shape whirShape;  // The Whirli's shape. Eventually to be an image. FIXME
    
    private Strategy strategy;   // Which strategy?
    
    private Random rand;            // Internal random number generator
    private WhirliPond myPond;  // Pointer back to the pond I live in
    
    // Individual Whirli modes
    
    /**
     * Status value when Whirli is alone.
     */
    public static final int LONER = 1;
    /**
     * Status value when Whirli is in a group
     */
    public static final int GROUPED = 2;
    /**
     * Status value when Whirli is coasting (not influenced)
     */
    public static final int COAST = 3;
    
    // Global Whirli modes
    
    /**
     * Status value default
     */
    public static final int NORMAL = 4;
    /**
     * Status value when Whirli is avoiding.
     */
    public static final int EVASIVE = 5;
    
    // Global Whirli sizes (in centimeters)
    
    /**
     * This Whirli's width.
     */
    public static final int WHIRLI_WIDTH = 5;
    
    /**
     * This Whirli's height
     */
    public static final int WHIRLI_HEIGHT = 2;
    
    /**
     * Constructor for a Whirligig beetle. Sets up the internal
     * storage vector which holds distances.
     */
    public Whirligig(WhirliPond p, Strategy s) {
        // Constructor
        myPond = p;
        rand = p.getPondRandomizer();
        distances = new Vector();
        setStrategy(s);
        pickNewRandomTime();
    }
    
    /**
     * Wipe out the internal vector of distances.
     */
    public void resetDistances() {
        assert distances != null : "Distance vector uninitialized!";
        
        distances.clear();
    }
    
    /**
     * Add a distance measurement to the internal
     * Vector of distances.
     * @param dist Distance calculation to add to the Vector.
     * Gets wrapped as a Double object.
     */
    public void addDistance(double dist) {
        assert distances != null : "Distance vector uninitialized!";
        
        distances.addElement(new Double(dist));
    }
    
    /**
     * Set this Whirli's status.
     * @param stat Status value. <I>Not really used at this point.</I>
     */
    public void setStatus(int stat) {
        status = stat;
    }
    
    /**
     * Get this Whirli's status value.
     * @return The Whirli status value, as an int.
     */
    public int getStatus() {
        return(status);
    }
    
    // Set this whirli's location in cm from upper left of pond
    /**
     * Set this Whirli's X position (in centimeters, from upper left)
     * @param xx X value
     */
    public void setX(double xx) {
        x = xx;
    }
    
    /**
     * Get this Whirli's X position, in centimeters from upper left hand corner.
     * @return The X position, in centimeters.
     */
    public double getX() {
        return(x);
    }
    
    /**
     * Set this Whirli's Y coordinate, in centimeters from the upper left.
     * @param yy The Y value in cm.
     */
    public void setY(double yy) {
        y = yy;
    }
    
    /**
     * Get this Whirli's Y position, offset from upper left.
     * @return The Y coordinate, in cm.
     */
    public double getY() {
        return(y);
    }
    
    /**
     * Get the <B>Shape</B> object corresponding to this Whirli.
     * @return A <B>Shape</B> object corresponding to the graphic representation of this Whirli.
     */
    public Shape getWhirShape() {
        return(whirShape);
    }
    
    /**
     * Sets the shape object to be used when drawing this Whirli.
     * @param s The Shape object representing this Whirli onscreen.
     */
    public void setWhirShape(Shape s) {
        whirShape = s;
    }
    
    /**
     * Set this Whirli's X velocity, in cm/sec
     * @param xv The X velocity, in cm/sec
     */
    public void setXvel(double xv) {
        xvel = xv;
    }
    
    /**
     * Get this Whirli's X-oriented velocity vector, in cm/sec
     * @return The X vector of this Whirli's speed, in cm/sec
     */
    public double getXvel() {
        return(xvel);
    }
    
    /**
     * Set this Whirli's Y-Vector velocity.
     * @param yv Y-vector velocity, cm/sec
     */
    public void setYvel(double yv) {
        yvel = yv;
    }
    
    /**
     * Get this Whirli's Y-vector velocity
     * @return The Y-vector velocity, cm/sec
     */
    public double getYvel() {
        return(yvel);
    }
    
    /**
     * Set this Whirli's strategy (i.e., how it will behave during the simulation)
     * @param s The Strategy object assigned to this Whirli.
     */
    public void setStrategy(Strategy s) {
        strategy = s;
        s.assigned();
    }
    
    /**
     * Get the Strategy associated with this Whirli.
     * @return A Strategy object, the one this Whirli is using to determine its motion.
     */
    public Strategy getStrategy() {
        return(strategy);
    }
    
    /**
     * Get this whirli's color. A convenience function.
     * Calls into the strategy, where the information
     * is stored. Equivalent to calling getStrategy().getW_strat_color();
     * @return The RGB integer value representing the color of this Whirli.
     */
    public Color getColor() {
        return(strategy.getW_strat_color());
    }
    
    /**
     * Get the angle (i.e., the degree of deflection off of nose-up position) this Whirli is facing.
     * @return Angle in Degrees.
     */
    public double getAngleInDegrees() {
        degrees = Math.toDegrees(Math.atan2(getYvel(),getXvel()));
        
        if(degrees < 0) {
            degrees += 360;
        }
        
        return(degrees);
    }
    
    /**
     * Get the angle (i.e., the degree of deflection off of nose-up position) this Whirli is facing.
     * @return Angle, in Radians.
     */
    public double getAngleInRadians() {
        degrees = Math.toDegrees(Math.atan2(getYvel(),getXvel()));
        
        if(degrees < 0) {
            degrees += 360;
        }
        
        return(Math.toRadians(degrees));
    }
    
    /**
     * Print a textual representation of this Whirli.
     * @return The String representing data about this Whirli.
     */
    public String toString() {
        String output;
        
        output = "W@("+getX()+","+getY()+"), V@("+getXvel()+","+getYvel()+"), S:"+getStrategy()+", IP:"+getInport();
        return(output);
    }
    
    /**
     * Set the # of Whirlis in this 'gig's viewport.
     * @param ip Number of Whirlis this Whirli can see.
     */
    public void setInport(int ip) {
        inport = ip;
    }
    
    /**
     * Get the # of Whirlis currently in this Whirli's view.
     * @return Number of Whirlis in this Whirli's view.
     */
    public int getInport() {
        return(inport);
    }
    
    public long getLastRandomTime() {
        return(lastRandomTime);
    }
    
    public void setLastRandomTime(long rndtime) {
        lastRandomTime = rndtime;
    }
    
    public void pickNewRandomTime() {
        long now = System.currentTimeMillis();
        
        if(getLastRandomTime() == 0) {
            // We've never been initialized.
            setLastRandomTime(now);
            setNextRandomTime(now);
        }
        
        long nextRandom = getNextRandomTime();

        // Check if this random time hasn't progressed past the current time,
        // and if so, do nothing.
        if(nextRandom > System.currentTimeMillis()) {
            return;
        }
        
        setLastRandomTime(nextRandom);
        
        nextRandom += (rand.nextInt(getStrategy().getW_rand_moves()) * 1000);
        setNextRandomTime(nextRandom);
    }
    
    public boolean timeForRandom() {
        long now = System.currentTimeMillis();
        long nextRandom = getNextRandomTime();
 
        if(nextRandom < now) {
            return(true);
        } else {
            return(false);
        }
    }
    
    public long getNextRandomTime() {
        return(nextRandomTime);
    }
    
    public void setNextRandomTime(long rndtime) {
        nextRandomTime = rndtime;
    }
    
    /**
     * Do one calculation of movement. Guard against moving beyond the (centimeter) bounds of the pond.
     * @param mx Size of the width of the pond, in centimeters.
     * @param my Size of the height of the pond, in centimeters.
     */
    public void calcPosition(double mx, double my) {
        x += xvel;
        y += yvel;

        // TODO: Whirlis should be able to calculate their next position themselves.
        // TODO: Whirlis should contain references to all 'inport' whirlis.
        
        // Boundary conditions, don't go outside the pond, silly whirli..
        
        if(x < 1) {
            x = 1;
            xvel = - xvel;
        } else if (x > (mx-1)) {
            x = mx - 1;
            xvel = - xvel;
        }
        
        if(y < 1) {
            y = 1;
            yvel = - yvel;
        } else if (y > (my-1)) {
            y = my - 1;
            yvel = - yvel;
        }
    }
}
