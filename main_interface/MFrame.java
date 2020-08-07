/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import models.ConnectData;
import models.Reservoir;
import models.SIM_MDL;

/**
 *
 * @author Administrator
 */
public class MFrame extends javax.swing.JFrame {

    /**
     * Creates new form MFrame
     */
    public static Sim_UI Sim_UI;    
    public static ConnectData ConnectData=new ConnectData(); 
    public static SIM_MDL SIM_MDL=new SIM_MDL();
    public static Reservoir Reservoir[];
    public MFrame() {
        //initComponents();
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(this);  
        initUI();
    }
    public void InitSet(){
        MFrame.ConnectData.InQFile="D:\\MyWork\\04_Work_Java\\PSO-RMPSG\\data\\inqdata.txt";
        MFrame.ConnectData.Input_InQData_From_NotePad(MFrame.ConnectData.InQFile);
        Reservoir=new Reservoir[3];
        for(int i=1;i<=2;i++){
            Reservoir[i]=new Reservoir();
        }        
        MFrame.ConnectData.Set_Parameters_To_Reservoirs();
        for(int i=1;i<=2;i++){
            Reservoir[i].Input_UpData();
        } 
        
        MFrame.Sim_UI=new Sim_UI();
        MFrame.Sim_UI.Mth_PSO_RMPSG_OPT.setSelected(true);
        MFrame.Sim_UI.Sim_UI_draw_in_QData(1);
    }
    public  JFrame parent;
    public int FontSize=12;
    public void initUI(){
        ImageIcon webIcon = new ImageIcon("image//login.png");
        setIconImage(webIcon.getImage());
        setTitle("PSO-RMPSG");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  
        //Set menubar
        JMenuBar menubar = new JMenuBar();
        JMenu OpenDataMenu = new JMenu("Open Data");
        menubar.add(OpenDataMenu);
        menubar.setPreferredSize(new Dimension(0,30));
        menubar.setBorder(BorderFactory.createEtchedBorder());
        setJMenuBar(menubar);        
        //Set toolbar
        JToolBar toolbar = new JToolBar();
        ImageIcon btn_1 = new ImageIcon("image//w71.gif");
        JButton OpenData_CMD = new JButton(btn_1);
        OpenData_CMD.setMaximumSize(new Dimension(40,40));
        OpenData_CMD.setPreferredSize(new Dimension(40,40));
        toolbar.add(OpenData_CMD);
        OpenData_CMD.setToolTipText("Open Data");     
        add(toolbar, BorderLayout.NORTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);    
        this.InitSet();
           OpenData_CMD.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {

                    UIManager.put("FileChooser.cancelButtonText", "Cancel");
                    UIManager.put("FileChooser.openButtonText","Open");
                    UIManager.put("FileChooser.acceptAllFileFilterText",".txt");
                    UIManager.put("FileChooser.fileNameLabelText","FilaName"); 
                    UIManager.put("FileChooser.filesOfTypeLabelText","FileType"); 
                    UIManager.put("FileChooser.lookInLabelText","Search"); 
                    UIManager.put("FileChooser.openDialogTitleText","Open Data"); 
                    
                    javax.swing.JFileChooser jfc = new javax.swing.JFileChooser
                                (new java.io.File("D:\\MyWork\\04_Work_Java\\PSO-RMPSG\\data\\inqdata.txt")) {
                        @Override
                        protected javax.swing.JDialog createDialog
                            (java.awt.Component parent) throws java.awt.HeadlessException {
                            javax.swing.JDialog dialog = super.createDialog(parent);
                            dialog.setIconImage(new javax.swing.ImageIcon("image//w71.gif").getImage());
                            return dialog;
                        }
                    };

                    jfc.addChoosableFileFilter(new FileNameExtensionFilter("notepad", "txt"));
                    jfc.setAcceptAllFileFilterUsed(true);
                    
                    int result=jfc.showOpenDialog(parent);  

                    if (result == JFileChooser.CANCEL_OPTION)
                    return;                    
                    File file=jfc.getSelectedFile();
                    MFrame.ConnectData.Input_InQData_From_NotePad(file.getAbsolutePath());
                    if(MFrame.ConnectData.Flag==true){
                        UIManager.put("OptionPane.okButtonText", "OK");
                        JOptionPane.showMessageDialog
                                    (null, "Ok!  Successfully input inflow data", "Input Result",JOptionPane.INFORMATION_MESSAGE);                          
                    }else{
                         UIManager.put("OptionPane.okButtonText", "OK");
                         JOptionPane.showMessageDialog
                                    (null, "Sorry!  Failed inputting inflow data", "Input Result",JOptionPane.INFORMATION_MESSAGE);                       
                    }
                 }
            }); 
           this.add(Sim_UI);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
