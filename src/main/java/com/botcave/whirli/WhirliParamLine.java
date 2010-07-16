package com.botcave.whirli;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Created by IntelliJ IDEA.
 * User: mnc
 * Date: Dec 27, 2004
 * Time: 7:20:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class WhirliParamLine extends JPanel {
    private static final long serialVersionUID = 1L;
    
    public WhirliParamLine(String label, Color color, double a, double b, double c, double d) {
        this.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));

        JLabel stratLabel = new JLabel(label);
        stratLabel.setOpaque(true);
        stratLabel.setBackground(color);

        this.add(stratLabel);
        this.add(new JLabel(" A: "));
        this.add(new JTextArea(""+a));
        this.add(new JLabel(" B: "));
        this.add(new JTextArea(""+b));
        this.add(new JLabel(" C: "));
        this.add(new JTextArea(""+c));
        this.add(new JLabel(" D: "));
        this.add(new JTextArea(""+d));

    }



}
