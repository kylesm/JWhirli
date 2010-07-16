/*
 * Created on Apr 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.botcave.whirli.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * @author kylesm
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class WhirliEditorMenuHandler
{
    private JFrame parent = null;
    private final JDialog about = new JDialog();

    private JMenuItem openMenuItem = null;
    private JMenuItem closeMenuItem = null;
    private JMenuItem saveMenuItem = null;
    private JMenuItem saveAsMenuItem = null;
    private JMenuItem quitMenuItem = null;
    private JMenu fileMenu = null;
    private JMenuItem cutMenuItem = null;
    private JMenuItem copyMenuItem = null;
    private JMenuItem pasteMenuItem = null;
    private JMenuItem selectAllMenuItem = null;
    private JMenuItem clearMenuItem = null;
    private JMenu editMenu = null;
    private JMenuItem aboutMenuItem = null;
    private JMenu helpMenu = null;

    public JMenuBar getMenuBar()
    {
        openMenuItem = new JMenuItem( "Open" );
        openMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O,
                ActionEvent.CTRL_MASK ) );
        openMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                openMenuItemHandler();
            }
        } );

        closeMenuItem = new JMenuItem( "Close" );
        closeMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_W,
                ActionEvent.CTRL_MASK ) );
        closeMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                closeMenuItemHandler();
            }
        } );

        saveMenuItem = new JMenuItem( "Save" );
        saveMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S,
                ActionEvent.CTRL_MASK ) );
        saveMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                saveMenuItemHandler();
            }
        } );

        saveAsMenuItem = new JMenuItem( "Save As" );
        saveAsMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                saveAsMenuItemHandler();
            }
        } );

        quitMenuItem = new JMenuItem( "Quit" );
        quitMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Q,
                ActionEvent.CTRL_MASK ) );
        quitMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                quitMenuItemHandler();
            }
        } );

        fileMenu = new JMenu( "File" );
        fileMenu.add( openMenuItem );
        fileMenu.add( closeMenuItem );
        fileMenu.addSeparator();
        fileMenu.add( saveMenuItem );
        fileMenu.add( saveAsMenuItem );
        fileMenu.addSeparator();
        fileMenu.add( quitMenuItem );

        cutMenuItem = new JMenuItem( "Cut" );
        cutMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_X,
                ActionEvent.CTRL_MASK ) );
        cutMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                cutMenuItemHandler();
            }
        } );

        copyMenuItem = new JMenuItem( "Copy" );
        copyMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C,
                ActionEvent.CTRL_MASK ) );
        copyMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                copyMenuItemHandler();
            }
        } );

        pasteMenuItem = new JMenuItem( "Paste" );
        pasteMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_V,
                ActionEvent.CTRL_MASK ) );
        pasteMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                pasteMenuItemHandler();
            }
        } );

        selectAllMenuItem = new JMenuItem( "Select All" );
        selectAllMenuItem.setAccelerator( KeyStroke.getKeyStroke(
                KeyEvent.VK_A, ActionEvent.CTRL_MASK ) );
        selectAllMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                selectAllMenuItemHandler();
            }
        } );

        clearMenuItem = new JMenuItem( "Clear" );
        clearMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                clearMenuItemHandler();
            }
        } );

        editMenu = new JMenu( "Edit" );
        editMenu.add( cutMenuItem );
        editMenu.add( copyMenuItem );
        editMenu.add( pasteMenuItem );
        editMenu.addSeparator();
        editMenu.add( selectAllMenuItem );
        editMenu.add( clearMenuItem );

        aboutMenuItem = new JMenuItem( "About the Whirli Editor" );
        aboutMenuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                aboutMenuItemHandler();
            }
        } );

        helpMenu = new JMenu( "Help" );
        helpMenu.add( aboutMenuItem );

        JMenuBar menuBar = new JMenuBar();
        menuBar.add( fileMenu );
        menuBar.add( editMenu );
        menuBar.add( helpMenu );

        return menuBar;
    }

    public void openMenuItemHandler()
    {

    }

    public void closeMenuItemHandler()
    {
    }

    public void saveMenuItemHandler()
    {

    }

    public void saveAsMenuItemHandler()
    {

    }

    public void quitMenuItemHandler()
    {
        /* need to check for changes */
        System.exit( 1 );
    }

    public void cutMenuItemHandler()
    {

    }

    public void copyMenuItemHandler()
    {

    }

    public void pasteMenuItemHandler()
    {

    }

    /**
     * XXX: how the heck can I get the currently focused widget?
     */
    public void selectAllMenuItemHandler()
    {

    }

    /**
     * XXX: how the heck can I get the currently focused widget?
     */
    public void clearMenuItemHandler()
    {

    }

    public void aboutMenuItemHandler()
    {
        about.setModal( true );
        about.addMouseListener( new MouseListener()
        {

            public void mouseClicked( MouseEvent e )
            {
                about.hide();
            }

            public void mousePressed( MouseEvent e )
            {
            }

            public void mouseReleased( MouseEvent e )
            {
            }

            public void mouseEntered( MouseEvent e )
            {
            }

            public void mouseExited( MouseEvent e )
            {
            }
        } );
        
        JLabel name = new JLabel( "JWhirli Editor" );
        // set font
        JLabel desc1 = new JLabel( "Creates and edits configuration files" );
        JLabel desc2 = new JLabel( "for the JWhirli simulator." );
        JLabel author = new JLabel( "Author: Kyle Smith <kylesm@gmail.com>" );
        JLabel url = new JLabel( "http://whirli.sourceforge.net" );
        JPanel content = new JPanel(new GridBagLayout());
        content.add( name, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 3, 3) );
        content.add( desc1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 3, 3) );
        content.add( desc2, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 3, 3) );
        content.add( author, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 3, 3) );
        content.add( url, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 3, 3) );
        about.setContentPane( content );
        about.setSize(300, 200);
        about.show();
    }
}