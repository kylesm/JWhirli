/*
 */
package com.botcave.whirli.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.List;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

/**
 * @author kylesm
 */
public class WhirliEditor extends JFrame
{
    private static final long serialVersionUID = 1L;

    private int pondWidth = 500; // XXX: temporary
    private int pondHeight = 500;

    public static void main( String[] args )
    {
        new WhirliEditor();
    }

    public WhirliEditor()
    {
        WhirliEditorMenuHandler menuHandler = new WhirliEditorMenuHandler();

        setJMenuBar( menuHandler.getMenuBar() );
        WhirliCanvas canvas = new WhirliCanvas();
        canvas.addMouseListener(new WhirliCanvasListener(canvas));
        JScrollPane scrollPane = new JScrollPane( canvas ); // left portion of
                                                             // the frame

        // add props to split pane
        JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT,
                true, scrollPane, getPropertiesPanel() );
        splitPane.setDividerLocation( 0.75 );
        
        JToolBar toolbar = new JToolBar(SwingConstants.HORIZONTAL);
        JButton whirliButton = new JButton("Whirli");
        JButton foodButton = new JButton("Food");
        toolbar.add(whirliButton);
        toolbar.add(foodButton);
        
        JPanel shebang = new JPanel(new GridBagLayout());
        shebang.add(toolbar, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 3, 3));
        shebang.add(splitPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,0,0,0), 3, 3));

        setContentPane( shebang );
        setTitle( "JWhirli Pond Editor" );
        setSize( 500, 500 );
        show();
    }

    private JComponent getContentPanel()
    {
        return new JPanel();
    }

    public JComponent getPropertiesPanel()
    {
        //      x, y, xvel, yvel, degree, status, shape, strategy
        JTextField x = new JTextField();
        // XXX: pondWidth should come from a WhirliPond instance instead of a
        // WhirliEditor member var
        x.setInputVerifier( new WhirliIntParamVerifier( 0, pondWidth ) );
        JTextField y = new JTextField();
        y.setInputVerifier( new WhirliIntParamVerifier( 0, pondHeight ) );
        JTextField xvel = new JTextField();
        xvel.setInputVerifier( new WhirliFloatParamVerifier() );
        JTextField yvel = new JTextField();
        yvel.setInputVerifier( new WhirliFloatParamVerifier() );
        JTextField degree = new JTextField();
        degree.setInputVerifier( new WhirliFloatParamVerifier( 0.0f, 360.0f ) );

        Object[] statusOptions = { "Normal", "Loner", "Group", "Coast",
                "Evasive" };
        JComboBox status = new JComboBox( statusOptions );

        /*
         * shape is currently only width and height, but allowing the user to
         * specify an image might be useful, because then they could define the
         * appearance of the whirligig (perhaps even use images of other
         * critters if they like)
         */
        JTextField width = new JTextField();
        width.setInputVerifier( new WhirliIntParamVerifier() ); // max should be
        // pond width
        JTextField height = new JTextField();
        height.setInputVerifier( new WhirliIntParamVerifier() ); // max should
        // be pond
        // height

        JComboBox strategy = new JComboBox();

        JButton applyButton = new JButton( "Apply" );
        JButton clearButton = new JButton( "Clear" );

        // add all the widgets to the panel
        // XXX: need to add another panel for the whole window (and have it
        // split -- JSplitPane?)

        JPanel propertiesPanel = new JPanel(); // right portion of the frame
        propertiesPanel.setLayout( new GridBagLayout() );

        propertiesPanel.add( new JLabel( "Properties" ),
                new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTH, GridBagConstraints.NONE,
                        new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( new JLabel( "Width:" ), new GridBagConstraints( 0,
                1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( width, new GridBagConstraints( 1, 1, 1, 1, 0.0,
                0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( new JLabel( "Height:" ), new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( height, new GridBagConstraints( 1, 2, 1, 1, 0.0,
                0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( new JLabel( "X Coordinate:" ),
                new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( x, new GridBagConstraints( 1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( new JLabel( "Y Coordinate:" ),
                new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( y, new GridBagConstraints( 1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( new JLabel( "X Velocity:" ),
                new GridBagConstraints( 0, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( xvel, new GridBagConstraints( 1, 4, 1, 1, 0.0,
                0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( new JLabel( "Y Velocity:" ),
                new GridBagConstraints( 0, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( yvel, new GridBagConstraints( 1, 5, 1, 1, 0.0,
                0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( new JLabel( "Status:" ), new GridBagConstraints(
                0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( status, new GridBagConstraints( 1, 6, 1, 1, 0.0,
                0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( new JLabel( "Strategy:" ), new GridBagConstraints(
                0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( strategy, new GridBagConstraints( 1, 7, 1, 1, 0.0,
                0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( clearButton, new GridBagConstraints( 0, 8, 1, 1,
                0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 3, 3 ) );
        propertiesPanel.add( applyButton, new GridBagConstraints( 1, 8, 1, 1,
                0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 0 ), 3, 3 ) );

        return propertiesPanel;
    }
    
    class WhirliPoint extends java.awt.Point
    {
        private static final long serialVersionUID = 1L;
        private static final int WHIRLI = 0;
        private static final int FOOD = 1;
        
        private int type;
        
        public WhirliPoint(int x, int y,int type)
        {
            super(x, y);
            
            if (WHIRLI != type && FOOD != type)
            {
                throw new IllegalArgumentException("Invalid point type.");
            }
            
            this.type = type;
        }
        
        public WhirliPoint(Point p, int type)
        {
            this (p.x, p.y, type);
        }
        
        public WhirliPoint(int x, int y)
        {
            this(x, y, WHIRLI);
        }
        
        public void setType(int type)
        {
            if (WHIRLI != type && FOOD != type)
            {
                throw new IllegalArgumentException("Invalid point type.");
            }
            
            this.type = type;
        }
        
        public int getType()
        {
            return type;
        }
    }
    
    class WhirliCanvasListener implements MouseListener
    {
        private WhirliCanvas panel = null;
        
        public WhirliCanvasListener(WhirliCanvas panel)
        {
            this.panel = panel;
        }
        
        public void mouseClicked( MouseEvent e )
        {
            Point p = e.getPoint();
            panel.push(new WhirliPoint(p, WhirliPoint.WHIRLI));
            panel.repaint();
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
    }
    
    class WhirliCanvas extends JPanel
    {
        /* need a way to query what mode we're in: whirli or food */
        private static final long serialVersionUID = 1L;
        
        /* this should contain more than just Points -- also want to know
         * what kind of point it is (food vs. whirligig)
         */
        private List newPoints = new java.util.Vector();
        private List existingPoints = new java.util.Vector();
        
        public WhirliCanvas()
        {
            setBackground(Color.WHITE);
        }
        
        public void push(WhirliPoint p)
        {
            newPoints.add(p);
        }
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            
            if (!newPoints.isEmpty())
            {
                Iterator it = newPoints.iterator();
                
                while (it.hasNext())
                {
                    WhirliPoint p = (WhirliPoint)it.next();
                    paintPoint((Graphics2D)g, p /* also color */);
                    existingPoints.add(p);
                }
                
                newPoints.clear();
                
                it = existingPoints.iterator();
                
                while (it.hasNext())
                {
                    WhirliPoint p = (WhirliPoint)it.next();
                    paintPoint((Graphics2D)g, p);
                }
            }
        }
        
        public void paintPoint(Graphics2D g, WhirliPoint p)
        {
            g.setColor((p.getType() == WhirliPoint.WHIRLI) ? Color.BLACK : Color.GRAY);
            g.setColor(Color.BLACK /* c */);
            g.fill( new Ellipse2D.Double(p.getX(), p.getY(), 10, 10));
        }
    }

    /*
     * Adapted from code written by Sun Microsystems for the Java 1.4.2 API
     * documentation.
     * 
     * Makes sure that if there is text present in the widget that it is an
     * integer. Otherwise it prevents the user from moving to another field.
     */
    class WhirliIntParamVerifier extends InputVerifier
    {
        private int minValue = Integer.MIN_VALUE;
        private int maxValue = Integer.MAX_VALUE;

        public WhirliIntParamVerifier()
        {
        }

        public WhirliIntParamVerifier( int minValue, int maxValue )
        {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        public boolean verify( JComponent input )
        {
            JTextField tf = (JTextField) input;
            String val = tf.getText();

            if ( 0 == val.length() )
                return true;

            try
            {
                int intVal = Integer.parseInt( val );
                return (intVal >= minValue) && (intVal <= maxValue);
            }
            catch ( NumberFormatException nfe )
            {
                return false;
            }
        }
    }

    class WhirliFloatParamVerifier extends InputVerifier
    {
        private float minValue = Float.MIN_VALUE;
        private float maxValue = Float.MAX_VALUE;

        public WhirliFloatParamVerifier()
        {
        }

        public WhirliFloatParamVerifier( float minValue, float maxValue )
        {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        public boolean verify( JComponent input )
        {
            JTextField tf = (JTextField) input;
            String val = tf.getText();

            if ( 0 == val.length() )
                return true;

            try
            {
                float floatVal = Float.parseFloat( val );

                return (floatVal >= minValue) && (floatVal <= maxValue);
            }
            catch ( NumberFormatException nfe )
            {
                return false;
            }
        }
    }
}