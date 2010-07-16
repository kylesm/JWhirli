package com.botcave.whirli;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jconfig.Configuration;
import org.jconfig.ConfigurationManager;

import com.botcave.whirli.util.Stopwatch;

/**
 * The simulator's representation of a pond containing Whirligig
 * beetles.
 */
public class WhirliPond extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    
    private int pmaxx, pmaxy; // Maximum dimensions in pixels
    private double pcmx, pcmy; // Maximum dimensions in centimeters
    private double scalex, scaley; // Ratio of pixels to centimeters
    private int stop_whirli;
    private Strategy strategies[];  // Valid strategies for Whirlis
    private Whirligig whirlis[];
    private Random rand = new Random();
    private Thread thread;
    private BufferedImage bimg;
    private JFrame wp;
    
    
    /**
     * Implementation of the thread methods
     */
    public void run() {
        // General idea here:
        // While we have an assigned thread
        // - Update Whirli positions
        // - Force repaint
        Stopwatch swatch = new Stopwatch();
        long execution_time;
        
        Thread me = Thread.currentThread();
        while(thread == me) {
            
            swatch.start();
            critterMove();
            repaint();
            swatch.stop();
            
            //System.out.println("Total processing time: "+swatch);
            execution_time = swatch.toValue();
            
            if(execution_time > 40) {
                execution_time = 40;
            }
            
            try{
                Thread.sleep(40-execution_time);
            } catch (InterruptedException e) { break;}
        }
        thread = null;
    }
    
    /**
     * Simulator control methods
     */
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
    
    /**
     * Stop this sim during an execution. Can be restarted with start().
     */
    public synchronized void stop() {
        thread = null;
    }
    
    
    /**
     * Implementation of our JPanel methods
     */
    
    /**
     * Paints the current state of the Whirlis.
     * Employs the standard offscreen buffered image technique.
     * @param g Graphics context to be used in painting the Whirlis.
     */
    public void paint(Graphics g) {
        Dimension d = getSize();
        Graphics2D g2 = createGraphics2D(d.width, d.height);
        drawWhirlis(d.width, d.height, g2);
        g2.dispose();
        g.drawImage(bimg, 0, 0, this);
    }
    
    /**
     * Go through our Whirli list and draw each one in their current
     * position. Typically used to fill in an offscreen buffer that
     * we will ultimately copy to the onscreen display.
     * @param w Width of the pond (in pixels)
     * @param h Height of the pond (in pixels)
     * @param g2 Graphics context to draw into.
     */
    public void drawWhirlis(int w, int h, Graphics2D g2) {
        AffineTransform atrotate, origat;
        Rectangle ws;
        Whirligig wgb;
        
        pmaxx = w;
        pmaxy = h;
        
        scalex = pmaxx / pcmx;
        scaley = pmaxy / pcmy;
        
        if(whirlis == null) {
            return;
        }
        
        for(int x=0; x < whirlis.length; x++) {
            wgb = getWhirli(x);
            origat = g2.getTransform();
            ws =(Rectangle)wgb.getWhirShape();
            
            atrotate = AffineTransform.getRotateInstance(wgb.getAngleInRadians(), wgb.getX()*scalex, wgb.getY()*scaley);
            
            g2.transform(atrotate);
            g2.setPaint(Color.black);
            
            // Move the rectangle to the Whirli's coordinates and proper size
            ws.setSize((int)(Whirligig.WHIRLI_WIDTH * scalex), (int)(Whirligig.WHIRLI_HEIGHT * scaley));
            ws.setLocation((int)(wgb.getX() * scalex),(int)(wgb.getY() * scaley));
            
            // Draw (and transform)
            g2.draw(ws);
            
            // Fill in the proper color
            g2.setPaint(whirlis[x].getStrategy().getW_strat_color());
            g2.fill(ws);
            
            // Restore transform
            g2.setTransform(origat);
        }
        
    }
    
    /**
     * Create a graphics context within a bufferedImage that matches the current width and height
     * of the JPanel object.
     * @param w Width, in pixels
     * @param h Height, in pixels
     * @return New Graphic context
     */
    public Graphics2D createGraphics2D(int w, int h) {
        boolean trails = false;
        
        Graphics2D g2 = null;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        
        g2 = bimg.createGraphics();
        
        if (!trails) {
            g2.setBackground(getBackground());
            g2.clearRect(0, 0, w, h);
        }
        
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(1.0f));
        
        return g2;
    }
    
    
    /**
     * Constructor to build a pond environment for the Whirligig
     * beetles to run around in. Reads in the strategy configuration
     * files and creates strategies based on them, then builds the
     * Whirlis as configured in the initial config file.
     * @param parent Parent Swing component (usually a top-level JFrame)
     */
    public WhirliPond(JFrame parent) {
        
        // Declare locals
        double a;
        double b;
        double c;
        double d;
        double velmax;
        double old_coeff;
        double dist_coeff;
        double vdistmax;
        int scolor_rgb;
        int rand_deflect;
        int rand_moves;
        int num_strats;
        int num_strat_whirlis;
        int total_whirlis = 0;
        Strategy strat;
        
        Configuration conf_initial = ConfigurationManager.getConfiguration();
        Configuration conf_strats = ConfigurationManager.getConfiguration("strategies");

        // Read in the initial config file ('config.xml') using the jConfig libraries.
        // Initial values are: pmaxx/pmaxy, the size of the pond in pixels; metersx/metersy,
        // the size of the pond in meters; stop_whirli, the number of whirlis in a viewport
        // that will make a single whirli stop; and num_whirli, the initial number of
        // whirlis to build.
        
        pmaxx = conf_initial.getIntProperty("pmaxx",500,"initial");
        pmaxy = conf_initial.getIntProperty("pmaxy",500,"initial");
        pcmx = 100 * conf_initial.getDoubleProperty("metersx",2,"initial");
        pcmy = 100 * conf_initial.getDoubleProperty("metersy",2,"initial");
        stop_whirli = conf_initial.getIntProperty("stop_whirli",4,"initial");
        //num_whirlis = conf_initial.getIntProperty("num_whirli",10,"initial");
        
        // Read in the strategies
        Vector v = new Vector();
        
        num_strats = conf_strats.getIntProperty("num_strats",1,"global");
        
        for(int x=1;x<(num_strats+1);x++) {
            System.out.println("Reading strategy "+x);
            a = conf_strats.getDoubleProperty("a",2.0,"strategy"+x);
            b = conf_strats.getDoubleProperty("b",1.0,"strategy"+x);
            c = conf_strats.getDoubleProperty("c",3.0,"strategy"+x);
            d = conf_strats.getDoubleProperty("d",1.0,"strategy"+x);
            velmax = conf_strats.getDoubleProperty("velmax",5.0,"strategy"+x);
            old_coeff = conf_strats.getDoubleProperty("old_coeff",0.8,"strategy"+x);
            dist_coeff = conf_strats.getDoubleProperty("dist_coeff",0.2,"strategy"+x);
            vdistmax = conf_strats.getDoubleProperty("vdistmax",200.0,"strategy"+x);
            scolor_rgb = conf_strats.getIntProperty("color_rgb",800,"strategy"+x);
            rand_deflect = conf_strats.getIntProperty("rand_deflection",30,"strategy"+x);
            rand_moves = conf_strats.getIntProperty("rand_moves",155,"strategy"+x);
            num_strat_whirlis = conf_strats.getIntProperty("num_strat_whirlis",5,"strategy"+x);
            total_whirlis += num_strat_whirlis;
            strat = new Strategy(a,b,c,d,velmax,old_coeff,dist_coeff,vdistmax,scolor_rgb,rand_deflect,
                    rand_moves,num_strat_whirlis);
            v.add(strat);
        }
        
        // Save our strategy array
        strategies = (Strategy[]) v.toArray(new Strategy[1]);
        
        // Build the Whirlis themselves
        buildWhirlis(total_whirlis);
        
        // Calculate our scale
        scalex = pmaxx / pcmx;
        scaley = pmaxy / pcmy;
        
        // Set up the graphics (color, etc.)
        setPreferredSize(new Dimension(pmaxx, pmaxy));
        setBackground(Color.white);

        // Create our non-modal dialogue for user parameter changes
        wp = new JFrame("Whirli Parameters");
        wp.setContentPane(new WhirliParams(strategies));//parent, false);

        wp.setSize(new Dimension(300, 400));
        wp.setLocationRelativeTo(parent);

        // We're ready to go!
    }
    
    
    /**
     * Initialize the pond with a bunch of Whirlis at random positions.
     * Distributes the number of Whirlis evenly across the
     * available strategies.
     * The number of Whirlis to create is deduced by multiplying num_strats * num_whirlis per strategy.
     */
    public void buildWhirlis(int total_whirlis) {
        whirlis = new Whirligig[total_whirlis];  // TODO: Change the whirligig array to better storage mechanism!
        Vector whirliVector = new Vector();
        int curstrat = 0;
        Whirligig whirli;
        double maxVelocity;

        while(curstrat < strategies.length) {
            Strategy s = strategies[curstrat];

            for(int x=0; x < s.getNum_whirlis(); x++) {

                //for(int x=0; x< numWhirlis; x++) {
                whirli = new Whirligig(this, s);

                whirli.setStatus(Whirligig.NORMAL);
                whirli.setX((double)rand.nextInt((int)pcmx));
                whirli.setY((double)rand.nextInt((int)pcmy));

                maxVelocity = s.getW_velmax();

                whirli.setXvel((double)(rand.nextInt((int)(maxVelocity * 2))-maxVelocity));
                whirli.setYvel((double)(rand.nextInt((int)(maxVelocity * 2))-maxVelocity));

                //			whirli.setXvel((double)(rand.nextInt(15)-5));
                //			whirli.setYvel((double)(rand.nextInt(15)-5));

                whirli.setWhirShape(new Rectangle());

                // Don't allow Whirli to start off at zero velocity
                if(whirli.getXvel() == 0.0) {
                    whirli.setXvel(0.5);
                }

                if(whirli.getYvel() == 0.0) {
                    whirli.setYvel(0.5);
                }


                // If we're tapped out on this strategy, move to the next one.
                if(s.isFullyAssigned()) {
                    s = strategies[curstrat++];
                }

                whirliVector.addElement(whirli);
            }
        }

        whirlis = (Whirligig[]) whirliVector.toArray(new Whirligig[1]);

    }
    
    /**
     * Do an iteration on the Whirlis for movement. Goes through each
     * Whirli and calls crittercalc() to figure out how it should move.
     */
    public void critterMove() {

        // TODO: Crittermove should be completely located in the Whirli object. This whole double-looping crap should go away.
        int current;
                
        // If we're counting predator turns, subtract one now
        //if(tire_count > 0) tire_count--;
        
        for(current=0; current!=whirlis.length; current++) {
            crittercalc(current);
        }
    }
    
    /**
     * Given which Whirli to consider, calculate its next move.
     * @param current Index into the whirli array indicating which Whirli to run the calculation for.
     */
    public void crittercalc(int current) {
        int compare;
        double tdx, tdy;    /* Holding values for computing vdx, vdy (which are sums) */
        double vdx,vdy;		/* Vectors due to sum of inverse distance squared */
        double odx,ody;		/* Vectors due to previous motion of critter */
        double hypo, hypv;	/* Hypotenuse calculations for vdx/y and odx/y */
        double reducer,
        reducero,
        reducerv; 	/* Reduction factors for vdx/y and odx/y */
        double hyp;
        
        int max_whirli = whirlis.length;
        
        double dx, dy;     // The final calculated dx/dy
        
        double distance;	/* Calculator variable for individual distances */
        double factor;		/* Weighting factor from 'the' formula */
        Strategy strat;		/* Which strategy the current Whirli is using */
        Whirligig thisWhirli;   /* Holding for the "current" Whirligig beetle */
        Whirligig compareWhirli; /* Holding for the "compare" Whirli when calculating distance */
        int vis_whirlis;	/* Temp. holder for visible whirlis */
        
        // Initialize our final, calculated vector
        dx = dy = 0.0;
        vdx = vdy = 0.0;
        
        // Set up our objects
        thisWhirli = getWhirli(current);
        strat = thisWhirli.getStrategy();
        
        // Initially, assume that every whirli (except itself) is visible.
        vis_whirlis = max_whirli - 1;
        
        // Reset this Whirli's distance vector
        thisWhirli.resetDistances();
        
        // STAGE 1: Calculation of vdx, the vector due to influence by
        //          other whirlis within a certain distance.
        
        for(compare=0; compare != max_whirli; compare++) {
            
            compareWhirli = getWhirli(compare);
            
            tdx = tdy = 0.0;
            
            tdx = compareWhirli.getX() - thisWhirli.getX();
            tdy = compareWhirli.getY() - thisWhirli.getY();
            distance = Math.sqrt(Math.pow(tdx,2.0)+Math.pow(tdy,2.0));
            
            // Save the distance in the internal Whirli array for output printing
            thisWhirli.addDistance(distance);
            
            // If the distance is greater than the defined maximum viewport,
            // then we don't consider this whirli to be important.
            if((distance > strat.getW_vdistmax()) && (current != compare)) {
                vis_whirlis--;
                tdx = tdy = 0.0;
            }
            
            // If the whirli is a large distance away, apply a formula to
            // calculate the weight of the delta factors.
            if(distance!=0) {
                factor = (Math.pow(distance, strat.getA()) - strat.getB() ) /
                         ( Math.pow(distance, strat.getC()) + strat.getD());
                tdx *= factor;
                tdy *= factor;
            } else {
                // Distance is zero, so no change to x/y
                tdx = tdy = 0.0;
            }
            
            vdx += tdx;
            vdy += tdy;
        }
        
        thisWhirli.setInport(vis_whirlis);
        
        // STAGE 2: Get old vector from current whirli
        
        odx = thisWhirli.getXvel();
        ody = thisWhirli.getYvel();
        
        // STAGE 3: Normalize the two component vectors
        
        hypo = Math.sqrt(Math.pow(odx,2.0) + Math.pow(ody,2));
        hypv = Math.sqrt(Math.pow(vdx,2.0) + Math.pow(vdy,2));
        
        if(hypo == 0) hypo = 0.001;
        if(hypv == 0) hypv = 0.001;
        
        reducero = strat.getW_velmax() / hypo;
        reducerv = strat.getW_velmax() / hypv;
        
        odx *= reducero;
        ody *= reducero;
        vdx *= reducerv;
        vdy *= reducerv;
        
        // STAGE 4: Calculate the new weighted vector sum
        
        dx = (strat.getW_dist_coeff() * vdx) + (strat.getW_old_coeff() * odx);
        dy = (strat.getW_dist_coeff() * vdy) + (strat.getW_old_coeff() * ody);
        
        hyp = Math.sqrt(Math.pow(dx,2.0) + Math.pow(dy,2.0));
        if(hyp == 0) hyp = 0.001;
        
        reducer = strat.getW_velmax() / hyp;
        
        dx *= reducer;
        dy *= reducer;
        
        // STAGE 5: Figure out randomization, if it's time
        if(thisWhirli.timeForRandom()) {
            System.out.println("Random for bug #"+current);
            Dimension rvec = randvect(thisWhirli);
            dx = rvec.getWidth();
            dy = rvec.getHeight();
            thisWhirli.pickNewRandomTime();
        }
                
        // Now, does this whirli see too many other whirlis? If so, it is
        // *so* excited, it just has to stop and celebrate. Otherwise, if it isn't time,
        // set the Whirli's new calculated vector.
        
        // NOTE: The stopping stuff really isn't working, pulled it out of the equation for now.
        //       Needs a rework.
        boolean stop_enabled = false;
        
        if(thisWhirli.getInport() >= stop_whirli && stop_enabled) {
            thisWhirli.setXvel(0.001);
            thisWhirli.setYvel(0.001);
        } else {
            thisWhirli.setXvel(dx);
            thisWhirli.setYvel(dy);
        }
        
        // System.err.println("dx="+dx+",dy="+dy+",len="+
        //                       Math.sqrt((dx*dx)+(dy*dy))+
        //                          ",angle="+
      //  			Math.toDegrees(Math.atan2(dy,dx)));
        
        thisWhirli.calcPosition(pcmx, pcmy);
    }
    
    private Dimension randvect(Whirligig whirli) {
        int rdegree;
        Strategy strat;
        double tdx, tdy;
        int degrees;
        
        strat = whirli.getStrategy();
        
        degrees = strat.getW_rand_degrees();
        
        // If the strategy specifies a deflection degree greater than 360,
        // then it indicates pick a random deflection degree, so we pick it.
        // Otherwise, use the degree specified in the strategy.
        if(degrees > 360) {
            rdegree = (rand.nextInt(361));
        } else {
            rdegree = degrees;
        }
        
        // Pull current Whirli degrees and add this one.
        rdegree += whirli.getAngleInDegrees();
        rdegree = rdegree % 360;
        
        tdy = Math.cos(Math.toRadians(rdegree)) * strat.getW_velmax();
        tdx = Math.sin(Math.toRadians(rdegree)) * strat.getW_velmax();
        
        tdy = Math.abs(tdy);
        tdx = Math.abs(tdx);
        
        if( (rdegree >= 180) && (rdegree <= 360) ) {
            tdx = 0 - tdx;
        }
        
        if ((rdegree >= 270) && (rdegree <= 360)) {
            tdy = 0 - tdy;
        }
        
        if ((rdegree >= 0) && (rdegree <= 90)) {
            tdy = 0 - tdy;
        }
        
        Dimension d = new Dimension();
        d.setSize(tdx, tdy);
        
        //		System.out.println("Randvect dx,dy=("+tdx+","+tdy+")");
        
        return(d);
    }
    
    public Whirligig getWhirli(int index) {
        if(whirlis != null && index < whirlis.length) {
            return(whirlis[index]);
        } else {
            return(null);
        }
    }
    
    public void showParameters() {
        wp.setVisible(true);
    }
    
    public void hideParameters() {
        wp.setVisible(false);
    }
    
    public Random getPondRandomizer() {
        return(rand);
    }
}
