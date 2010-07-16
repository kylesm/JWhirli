package com.botcave.whirli;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Created by IntelliJ IDEA.
 * User: mnc
 * Date: Dec 12, 2004
 * Time: 5:35:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class WhirliParams extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JButton cancel;
    public JPanel buttons;
    private JButton apply;
    private JFormattedTextField FPS;
    private JCheckBox trails;
    private JTabbedPane optsTabPane;
    private JPanel whirlisTab = new JPanel();
    private JPanel pondTab;
    private JPanel programTab;
    private JPanel scrollPanel;
    private JScrollPane parametersList;
    private JPanel top;
    private WhirliParamLine wpl;

    public WhirliParams(Strategy[] s) {

        this.setLayout(new BorderLayout());

        // Build the two accept/cancel buttons
        apply = new JButton("Apply");
        cancel = new JButton("Cancel");

        buttons = new JPanel(new GridLayout(1,0));
        buttons.add(apply);
        buttons.add(cancel);

        FPS = new JFormattedTextField();

        // Build the rows of the strategies
        scrollPanel = new JPanel();
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.PAGE_AXIS));

        parametersList = new JScrollPane(scrollPanel);

        for(int x=0; x < s.length; x++) {
            wpl = new WhirliParamLine("Strategy "+x+": ", s[x].getW_strat_color(), s[x].getA(), s[x].getB(), s[x].getC(), s[x].getD());
            scrollPanel.add(wpl);
        }


        this.add(parametersList, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.PAGE_END);

        apply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fps = FPS.getText();
                System.out.println("Apply:"+fps);
            }
        });

    }


}
