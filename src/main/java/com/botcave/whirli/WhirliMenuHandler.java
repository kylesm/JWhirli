/*
 * WhirliMenuHandler.java
 *
 * Created on April 10, 2004, 6:42 PM
 */

package com.botcave.whirli;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author  Marc N. Cannava
 */
public class WhirliMenuHandler implements ActionListener, ItemListener {
    
    private WhirliPond pond;
    private JMenuBar menuBar;
    private JMenu fileMenu, windowMenu;
    private JMenuItem about;
    private JCheckBoxMenuItem parameters, statistics;
    
    /** Creates a new instance of WhirliMenuHandler */
    public WhirliMenuHandler() {
    }
    
    
    public JMenuBar getMenuBar() {
        
        if(menuBar == null) {
            
            menuBar = new JMenuBar();
            fileMenu = new JMenu("File");
            windowMenu = new JMenu("Windows");
            
            menuBar.add(fileMenu);
            menuBar.add(windowMenu);
            
            // Build the File menu
            about = new JMenuItem("About..");
            fileMenu.add(about);
            
            // Build the Window menu
            parameters =  new JCheckBoxMenuItem("Parameters");
            windowMenu.add(parameters);
            parameters.addItemListener(this);
            
            statistics = new JCheckBoxMenuItem("Statistics");
            windowMenu.add(statistics);
            statistics.addItemListener(this);
            
        } 
        
        return(menuBar);
        
    }
    
    /**
     * Set the Pond that will take action from the menus and
     * do the right thing.
     */
    public void setPond(WhirliPond p) {
        pond = p;
    }
    
    public WhirliPond getPond() {
        return(pond);
    }
    
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action performed!");
    }
    
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        
        int state = e.getStateChange();
        
        if(source == parameters) {
            if(state == ItemEvent.SELECTED) {
                pond.showParameters();
            } else {
                pond.hideParameters();        
            }
        } else if (source == statistics) {
            System.out.println("Statistics changed!");
        }
    }
}
