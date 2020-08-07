package main_interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class ProBarPane extends JPanel {
    public ProBarPane() {

        initUI();
    }
    BorderLayout BL=new BorderLayout();
    public JProgressBar progBar;
    private void initUI() {
    	this.setLayout(BL);
        progBar = new JProgressBar();
        //progBar.setStringPainted(true);
        progBar.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
        this.add(progBar,BL.CENTER);
        progBar.setForeground(Color.blue);
        progBar.setStringPainted(true);
        progBar.setFont(new java.awt.Font("WKSystem", Font.BOLD, 14));
        
    }

}
