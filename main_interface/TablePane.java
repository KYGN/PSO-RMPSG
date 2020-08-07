package main_interface;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.text.DecimalFormat;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TablePane extends JPanel {

    JTable Table = new JTable();
    DefaultTableModel D_Model = new DefaultTableModel();
    JScrollPane ScrollPanel = new JScrollPane();
    BorderLayout borderLayout4 = new BorderLayout();
    JLabel TitlLBL=new JLabel();
    public TablePane() {
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public String TitleStr;
    public String ColStr[];
    public String Col_Data[][];
    public int RowN,ColN;
    private void jbInit() throws Exception {
        this.setLayout(borderLayout4);
        TitlLBL.setHorizontalAlignment(SwingConstants.CENTER);
        Table.addMouseListener(new TablePane_Table_mouseAdapter(this));
        Table.setFont(new java.awt.Font("WKSystem", Font.PLAIN, 14));
        //this.add(this.TitlLBL,java.awt.BorderLayout.NORTH);
        this.TitlLBL.setPreferredSize(new Dimension(50,50));
        TitlLBL.setFont(new java.awt.Font("WKSystem", Font.BOLD, 14));

        this.TitlLBL.setText("");
    }
    DecimalFormat myFormat = new DecimalFormat("##.##");
    public void Set_Data_Table(){
        try{
            D_Model.setColumnCount(0);
            D_Model.setRowCount(0);
            
            this.TitlLBL.setText(TitleStr);
            for(int i=1;i<=ColN;i++){
                D_Model.addColumn(ColStr[i]);
            }
            D_Model.setRowCount(RowN+1);
            for(int i=1;i<=ColN;i++){
                for(int j=1;j<=RowN;j++){
                    D_Model.setValueAt(Col_Data[i][j], j - 1, i - 1);
                    /*if(i!=2){
                        D_Model.setValueAt(Col_Data[i][j], j - 1, i - 1);
                    }else{
                        D_Model.setValueAt(myFormat.format(Col_Data[i][j]), j - 1, i - 1);
                    }*/
                }
            }
            this.Table.setModel(D_Model);
            ScrollPanel.getViewport().add(Table);
            //ScrollPanel.setVerticalScrollBar(1);
            this.add(ScrollPanel, java.awt.BorderLayout.CENTER);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public boolean Flag1=false;
    public boolean Flag2=false;

    public void Table_mouseClicked(MouseEvent e) {
        Object Row = this.Table.getSelectedRow();
        int S_RN = Integer.parseInt(Row.toString());
    }
}


class TablePane_Table_mouseAdapter extends MouseAdapter {
    private TablePane adaptee;
    TablePane_Table_mouseAdapter(TablePane adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseClicked(MouseEvent e) {
        adaptee.Table_mouseClicked(e);
    }
}
