package com.botcave.whirli;

import javax.swing.JFrame;

/**
 * The main class in the jWhirli simulator. Sets up the sim, and
 * kicks off the sim thread.
 */
public class WhirliMain extends Object {
    
    /**
     * Main routine into the Whirli simulator. Creates a top-level
     * JFrame, then creates a WhirliPond as the content pane in that
     * frame, then starts the simulation thread.
     * @param argv The command-line arguments.
     */
    public static void main(String[] argv) {
        
        // Construct a jFrame for ourselves
        JFrame frame = new JFrame("jWhirli Simulator");
        
        // Build our menu event handler, which (nicely) builds a menu for us, too
        WhirliMenuHandler menuHandler;
        menuHandler = new WhirliMenuHandler();
        
        // Add the menuBar built by our handler to the top-level JFrame
        frame.setJMenuBar(menuHandler.getMenuBar());
        
        // Build a whirli pond. It will read from its configuration file
        // for initial values.
        WhirliPond pond = new WhirliPond(frame);
        pond.setOpaque(true);
        
        // Hook the pond into the menuHandler so it can act on events
        // given by the menus.
        menuHandler.setPond(pond);
        
        // Pack, show the top-level frame
        frame.setContentPane(pond);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        // Run the simulation.
        pond.start();
        
    }
    
}