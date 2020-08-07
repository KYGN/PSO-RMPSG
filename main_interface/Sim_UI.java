/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main_interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import models.PSO_Q;
import models.PSO_RMPSG;


/**
 *
 * @author Administrator
 */
public class Sim_UI extends javax.swing.JPanel {

    /**
     * Creates new form Sim_UI
     */
    public Sim_UI() {
        //initComponents();
  
        this.Sim_UI_initUI();
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        } 
        SwingUtilities.updateComponentTreeUI(this);         
    }
    int FontSize=12;//서체의 크기
    
    BorderLayout Sim_UI_BL=new BorderLayout();
    BorderLayout LPane_BL=new BorderLayout();
    JPanel LPane=new JPanel();
    JPanel RPane=new JPanel();
    BorderLayout RPane_BL=new BorderLayout();
    JPanel LC_MPane=new JPanel();
    BorderLayout LC_MPane_BL=new BorderLayout();
    
    double rate;
    boolean Cal_Flag=false;
    public  void Sim_UI_initUI() {

    	this.setBorder(BorderFactory.createEtchedBorder(0));
    	this.setLayout(this.Sim_UI_BL);
        //this.setBackground(Color.white);
    	//this.pack_ZCond_MPane();
        LPane.setPreferredSize(new Dimension(230,0));  
//        LPane.setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
        this.add(this.LPane,Sim_UI_BL.WEST); 
        RPane.setPreferredSize(new Dimension(50,0));  
        this.add(this.RPane,Sim_UI_BL.CENTER); 
        //RPane.setBackground(Color.white);
        this.LPane.setLayout(LPane_BL);
        //this.Sim_UI_pack_Select_SimDatePane();
        LC_MPane.setLayout(LC_MPane_BL);
        this.LPane.add(this.LC_MPane, LPane_BL.CENTER);
        this.Sim_UI_pack_ZCond_MPane();
        Sim_UI_pack_PSO_RMPSG_Pane();
        Sim_UI_pack_PSO_Q_Pane();
        this.Sim_UI_pack_Select_SimMethodPane();
        this.Sim_UI_pack_BtnPane();
        Sim_UI_set_InitValue();
        
        RPane.setLayout(RPane_BL);
        RPane.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        this.Sim_UI_pack_GraphPane();
	
  	
       
   }

    JPanel SimMthPane=new JPanel();
    JLabel SimMthLBL=new JLabel("Select Algorithm");
    GridLayout SimMthPane_GL=new GridLayout(3,2);
    JRadioButton Mth_PSO_RMPSG_OPT=new JRadioButton("PSO-RMPSG method");
    JRadioButton Mth_PSO_Q_OPT=new JRadioButton("PSO-Q algorithm");
    ButtonGroup Methodgroup = new ButtonGroup();

    public void Sim_UI_pack_Select_SimMethodPane(){
        try{
            SimMthLBL.setHorizontalAlignment(0);
            SimMthPane.setBorder(new EmptyBorder(new Insets(8, 8, 8, 8)));
            SimMthPane.setBorder(BorderFactory.createEtchedBorder(0));
            SimMthPane.setLayout(SimMthPane_GL);
            SimMthPane.add(SimMthLBL);
            SimMthPane.add(Mth_PSO_RMPSG_OPT);
            SimMthPane.add(Mth_PSO_Q_OPT);
            Methodgroup.add(Mth_PSO_RMPSG_OPT);
            Methodgroup.add(Mth_PSO_Q_OPT);
            SimMthPane.setPreferredSize(new Dimension(0,100));
            this.LPane_PSO_Q.add(SimMthPane,this.LPane_PSO_Q_BL.NORTH);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    boolean C_Flag=false;
    boolean G_Flag=false;
    ActionListener actionListener = new ActionListener(){
      public void actionPerformed(ActionEvent e) {
          
      }
    };   
    JPanel BtPane=new JPanel();
    BorderLayout BtPane_BL=new BorderLayout();
    JPanel Button_Pane=new JPanel();
    GridLayout Button_Pane_GL=new GridLayout(1,2);
    JButton SimRunCMD=new JButton();
    JButton SimStopCMD=new JButton();
    public static ProBarPane progBar;
    public void Sim_UI_pack_BtnPane() {
    	try {
            Button_Pane.setLayout(Button_Pane_GL);
            //SimRunCMD.setFont(new java.awt.Font("Batang", Font.BOLD, 14));
            SimRunCMD.setText("Start");
            //SimStopCMD.setFont(new java.awt.Font("Batang", Font.BOLD, 14));
            SimStopCMD.setText("Stop");            
            BtPane.setLayout(BtPane_BL);
            progBar=new ProBarPane();
            SimRunCMD.setPreferredSize(new Dimension(100,0));
            SimStopCMD.setPreferredSize(new Dimension(100,0));
            Button_Pane.add(SimRunCMD);
            Button_Pane.add(SimStopCMD);
            BtPane.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
            BtPane.add(Button_Pane,BtPane_BL.WEST);
            BtPane.add(progBar,BtPane_BL.CENTER);
            this.BtPane.setPreferredSize(new Dimension(0,45));    
            this.add(BtPane,this.Sim_UI_BL.SOUTH);
            SimStopCMD.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    MFrame.Sim_UI.stop();
                    MFrame.Sim_UI.progBar.progBar.setValue(0);
                }
            });
            SimRunCMD.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        SimRunCMD.setEnabled(false);
                        start();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    };
                    System.out.println("ok!");  
                }
            });                
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    JPanel Q_GPane=new JPanel();
    JPanel Z_GPane=new JPanel();
    BorderLayout Q_GPane_BL=new BorderLayout();
    BorderLayout Z_GPane_BL=new BorderLayout();
    JLabel GraphPane_LBL=new JLabel("Select Reservoir  ");
    JPanel T_GPane=new JPanel();
    JPanel T_L_GPane=new JPanel();
    BorderLayout T_GPane_BL=new BorderLayout();
    JComboBox N_ComBo=new JComboBox();
    JTabbedPane TabPane = new JTabbedPane();
    JPanel InQ_Pane=new JPanel();
    JPanel InT_Pane=new JPanel();
    BorderLayout InQ_Pane_BL=new BorderLayout();
    BorderLayout InT_Pane_BL=new BorderLayout();
    public void Sim_UI_pack_GraphPane(){
        try{
            //TabPane.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
            //GraphPane_LBL.setHorizontalAlignment(0);
            T_GPane.setPreferredSize(new Dimension(0,30));
            T_GPane.setLayout(T_GPane_BL);
            T_L_GPane.setPreferredSize(new Dimension(600,0));
            GraphPane_LBL.setPreferredSize(new Dimension(400,0));
            GraphPane_LBL.setHorizontalAlignment(4);
            T_GPane.add(T_L_GPane,T_GPane_BL.EAST);
            T_GPane.add(N_ComBo,T_GPane_BL.CENTER);
            T_GPane.add(GraphPane_LBL,T_GPane_BL.WEST);
            N_ComBo.addItem("Huichon");
            N_ComBo.addItem("Jangsu");
            Q_GPane.setBackground(Color.white);
            Z_GPane.setBackground(Color.white);
            InQ_Pane.setBackground(Color.white);
            InT_Pane.setBackground(Color.white);
            this.RPane.add(T_GPane,RPane_BL.NORTH);
            this.RPane.add(Q_GPane,RPane_BL.CENTER);
            this.Q_GPane.setLayout(Q_GPane_BL);
            this.Z_GPane.setLayout(Z_GPane_BL);
            this.InQ_Pane.setLayout(InQ_Pane_BL);
            this.InT_Pane.setLayout(InT_Pane_BL);
            RPane.add(TabPane,RPane_BL.CENTER);
            Sim_UI_pack_OptQ_Pane();
            Sim_UI_pack_GN_Pane(2);
            TabPane.add(InQ_Pane,"Natural Inflow");
            TabPane.add(OptQ_Pane,"Discharge Control");
            TabPane.add(GN_Pane,"Operating Plan");
            TabPane.setSelectedComponent(InQ_Pane);
            N_ComBo.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        Object PName=N_ComBo.getSelectedItem();
                            if(PName.equals("Huichon")){
                                  Sim_UI_draw_in_QData(1);
                                  Sim_UI_Draw_Sim_QData(1);
                                  Sim_UI_Draw_Sim_ZData(1);
                                  Sim_UI_Input_TableData(1);
                                  if(Mth_PSO_RMPSG_OPT.isSelected() && MFrame.Reservoir[1].Opt_Flag==true){
                                    Sim_UI_InputGN_TableData(1);
                                    Sim_UI_pack_GN_Pane(MFrame.Reservoir[1].Cal_MaxGN);
                                    Sim_UI_Draw_OptGN_Graph(1);                                      
                                  }else{
                                    GN_Pane.removeAll();
                                    GN_Pane.setVisible(false);
                                    GN_Pane.setVisible(true);
                                  }
                            }else if(PName.equals("Jangsu")){
                                  Sim_UI_draw_in_QData(2);
                                  Sim_UI_Draw_Sim_QData(2);
                                  Sim_UI_Draw_Sim_ZData(2);   
                                  Sim_UI_Input_TableData(2);
                                  if(Mth_PSO_RMPSG_OPT.isSelected() && MFrame.Reservoir[2].Opt_Flag==true){
                                    Sim_UI_InputGN_TableData(2);
                                    Sim_UI_pack_GN_Pane(MFrame.Reservoir[2].Cal_MaxGN);
                                    Sim_UI_Draw_OptGN_Graph(2);                                      
                                  }else{
                                    GN_Pane.removeAll();
                                    GN_Pane.setVisible(false);
                                    GN_Pane.setVisible(true);                                    
                                  }
                            }
                        System.out.println(TabPane.getSelectedIndex());
                        if(TabPane.getSelectedIndex()==0){
                            TabPane.remove(OptQ_Pane);
                            TabPane.remove(GN_Pane);
                            TabPane.add(InQ_Pane,"Natural Inflow");
                            TabPane.add(OptQ_Pane,"Discharge Control");
                            TabPane.add(GN_Pane,"Operating Plan");
                            TabPane.setSelectedComponent(InQ_Pane);
                        }else if(TabPane.getSelectedIndex()==1){
                            TabPane.remove(OptQ_Pane);
                            TabPane.remove(GN_Pane);
                            TabPane.add(InQ_Pane,"Natural Inflow");
                            TabPane.add(OptQ_Pane,"Discharge Control");
                            TabPane.add(GN_Pane,"Operating Plan");                      
                            TabPane.setSelectedComponent(OptQ_Pane);
                        }else if(TabPane.getSelectedIndex()==2){
                            TabPane.remove(OptQ_Pane);
                            TabPane.remove(GN_Pane);
                            TabPane.add(InQ_Pane,"Natural Inflow");
                            TabPane.add(OptQ_Pane,"Discharge Control");
                            TabPane.add(GN_Pane,"Operating Plan");                        
                            TabPane.setSelectedComponent(GN_Pane);
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    };
                }
            });               
        }catch(Exception ex){
            
        }
    }
    JPanel OptQ_Pane=new JPanel();
    BorderLayout OptQ_Pane_BL=new BorderLayout();
    JPanel OptQ_LPane=new JPanel();
    BorderLayout OptQ_LPane_BL=new BorderLayout();
    JPanel OptQ_RPane=new JPanel();
    BorderLayout OptQ_RPane_BL=new BorderLayout();
    public TablePane TPane=new TablePane();
    JPanel OptQ_R_UpPane=new JPanel();
    BorderLayout OptQ_R_UpPane_BL=new BorderLayout();
    JPanel OptQ_R_DnPane=new JPanel();
    BorderLayout OptQ_R_DnPane_BL=new BorderLayout();
    
    public void Sim_UI_pack_OptQ_Pane(){
        OptQ_Pane.setLayout(OptQ_Pane_BL);
        OptQ_LPane.setLayout(OptQ_LPane_BL);
        OptQ_RPane.setLayout(OptQ_RPane_BL);
        OptQ_LPane.setPreferredSize(new Dimension(300,0));
        OptQ_LPane.add(TPane,OptQ_LPane_BL.CENTER);
        OptQ_Pane.add(OptQ_LPane,OptQ_Pane_BL.WEST);
        OptQ_Pane.add(OptQ_RPane,OptQ_Pane_BL.CENTER);
        OptQ_R_UpPane.setLayout(OptQ_R_UpPane_BL);
        OptQ_R_DnPane.setLayout(OptQ_R_DnPane_BL);
        OptQ_R_UpPane.setPreferredSize(new Dimension(0,200));
        OptQ_R_UpPane.setBackground(Color.white);
        OptQ_R_DnPane.setBackground(Color.white);
        OptQ_RPane.add(OptQ_R_UpPane,OptQ_RPane_BL.NORTH);
        OptQ_RPane.add(OptQ_R_DnPane,OptQ_RPane_BL.CENTER);
    }
    JPanel GN_Pane=new JPanel();
    BorderLayout GN_Pane_BL=new BorderLayout();
    JPanel GN_LPane=new JPanel();
    BorderLayout GN_LPane_BL=new BorderLayout();
    JPanel GN_RPane=new JPanel();
    GridLayout GN_RPane_GL;
    public TablePane G_TPane=new TablePane();
    JPanel GN_R_CPane[];
    BorderLayout GN_R_CPane_BL[];
    public void Sim_UI_pack_GN_Pane(int GN){
        GN_Pane.removeAll();
        GN_Pane.setLayout(GN_Pane_BL);
        GN_LPane.setLayout(GN_LPane_BL);
        GN_RPane_GL=new GridLayout(GN,1);
        GN_RPane.setLayout(GN_RPane_GL);
        GN_LPane.setPreferredSize(new Dimension(300,0));
        GN_LPane.add(G_TPane,GN_LPane_BL.CENTER);
        GN_Pane.add(GN_LPane,GN_Pane_BL.WEST);
        GN_Pane.add(GN_RPane,GN_Pane_BL.CENTER);
        GN_RPane.setBackground(Color.white);
//        GN_R_CPane=new JPanel[GN+1];
//        GN_R_CPane_BL=new BorderLayout[GN+1];
//        GN_RPane.removeAll();
//        for(int i=1;i<=GN;i++){
//            GN_R_CPane[i]=new JPanel();
//            GN_R_CPane[i].setLayout(GN_R_CPane_BL[i]);
//            GN_RPane.add(GN_R_CPane[i]);
//            GN_R_CPane[i].setBorder(BorderFactory.createLineBorder(Color.yellow));
//            GN_R_CPane[i].setBackground(Color.white);
//        }
   }    
   public void Sim_UI_Input_TableData(int R_No){
       try{
           DecimalFormat myFormat = new DecimalFormat("##.##");
           TPane.Flag2=true; TPane.Flag1=false;
           TPane.RowN=MFrame.Reservoir[R_No].UpN;
           if(MFrame.Reservoir[R_No].Opt_Flag==true){
                if(this.Mth_PSO_Q_OPT.isSelected()){
                 TPane.ColN = 3;
                  //SectionTable.TitleStr = R_No+"호저수지큰물예보자료";
                  TPane.ColStr = new String[TPane.ColN+1];
                  TPane.ColStr[1] = "Time";
                  TPane.ColStr[2] = "Q(㎥/s)";
                  TPane.ColStr[3] = "Z(m)";
                  TPane.Col_Data=new String[TPane.ColN +1][TPane.RowN+1];
                  int k=0;
                  for (int i = 1; i <= MFrame.Reservoir[R_No].UpN-1; i++) {
                      TPane.Col_Data[1][i]=MFrame.Reservoir[1].Dy[i] + "/" +MFrame.Reservoir[1].Time[i];
                      TPane.Col_Data[2][i]=String.valueOf((int)(MFrame.Reservoir[R_No].PSO_Q_Q[i]));
                      TPane.Col_Data[3][i]=String.valueOf(myFormat.format(MFrame.Reservoir[R_No].PSO_Q_Z[i]));
                  }               
                }else if(this.Mth_PSO_RMPSG_OPT.isSelected()){
                     TPane.ColN = 4;
                     //SectionTable.TitleStr = R_No+"호저수지큰물예보자료";
                     TPane.ColStr = new String[TPane.ColN+1];
                     TPane.ColStr[1] = "Time";
                     TPane.ColStr[2] = "Q(㎥/s)";
                     TPane.ColStr[3] = "Z(m)";
                     TPane.ColStr[4] = "GN";
                     TPane.Col_Data=new String[TPane.ColN +1][TPane.RowN+1];
                     int k=0;
                     for (int i = 1; i <= MFrame.Reservoir[R_No].UpN-1; i++) {
                         TPane.Col_Data[1][i]=MFrame.Reservoir[1].Dy[i] + "/" +MFrame.Reservoir[1].Time[i];
                         TPane.Col_Data[2][i]=String.valueOf((int)(MFrame.Reservoir[R_No].PSO_RMPSG_Q[i]));
                         TPane.Col_Data[3][i]=String.valueOf(myFormat.format(MFrame.Reservoir[R_No].PSO_RMPSG_Z[i]));
                         TPane.Col_Data[4][i]=String.valueOf(myFormat.format(MFrame.Reservoir[R_No].PSO_RMPSG_GN[i]));
                     }               
                }               
           }else{
                 TPane.ColN = 3;
                  //SectionTable.TitleStr = R_No+"호저수지큰물예보자료";
                  TPane.ColStr = new String[TPane.ColN+1];
                  TPane.ColStr[1] = "Time";
                  TPane.ColStr[2] = "Q(㎥/s)";
                  TPane.ColStr[3] = "Z(m)";
                  TPane.Col_Data=new String[TPane.ColN +1][TPane.RowN+1];
                  int k=0;
                  for (int i = 1; i <= MFrame.Reservoir[R_No].UpN-1; i++) {
                      TPane.Col_Data[1][i]=MFrame.Reservoir[1].Dy[i] + "/" +MFrame.Reservoir[1].Time[i];
                      TPane.Col_Data[2][i]=String.valueOf((int)(MFrame.Reservoir[R_No].T_RCalQ[i]));
                      TPane.Col_Data[3][i]=String.valueOf(myFormat.format(MFrame.Reservoir[R_No].T_RCalZ[i]));
                  }                 
           }

           TPane.Set_Data_Table();
           TPane.setVisible(false);
           TPane.setVisible(true);
       }catch(Exception ex){
           ex.printStackTrace();
       }
   }        
   public void Sim_UI_InputGN_TableData(int R_No){
       try{
           if(MFrame.Reservoir[R_No].Opt_Flag==true){
                DecimalFormat myFormat = new DecimalFormat("##.##");
                G_TPane.Flag2=true; TPane.Flag1=false;
                G_TPane.RowN=MFrame.Reservoir[R_No].UpN;
                G_TPane.ColN = MFrame.Reservoir[R_No].Cal_MaxGN+1;
                //SectionTable.TitleStr = R_No+"호저수지큰물예보자료";
                G_TPane.ColStr = new String[G_TPane.ColN+1];
                G_TPane.ColStr[1] = "Time";
                G_TPane.ColStr[2] = "Init-level";
                for(int i=3;i<=G_TPane.ColN;i++){
                     G_TPane.ColStr[i] =(i-2)+"-level";
                }

                G_TPane.Col_Data=new String[TPane.ColN +1][TPane.RowN+1];
                int k=0;
                for (int i = 1; i <= MFrame.Reservoir[R_No].UpN-1; i++) {
                    G_TPane.Col_Data[1][i]=MFrame.Reservoir[1].Dy[i] + "/" +MFrame.Reservoir[1].Time[i];
                    for(int j=2;j<=G_TPane.ColN;j++){
                        G_TPane.Col_Data[j][i]=String.valueOf(MFrame.Reservoir[R_No].Step_OptGN[j-1][i]);
                    }
                }
                G_TPane.Set_Data_Table();               
           }


       }catch(Exception ex){
           ex.printStackTrace();
       }
   }            
    //저수지체계의 초기수위설정 및 마감수위설정을 진행하는 대면부를 구성한다.
    JPanel ZCond_MPane=new JPanel();
    JPanel ZCond_MMPane=new JPanel();
    JPanel ZCond_TitlePane=new JPanel();
    JPanel ZCond_CondPane=new JPanel();
    JPanel ZCond_DataPane[][];
    BorderLayout ZCond_M_BL=new BorderLayout();
    BorderLayout ZCond_MM_BL=new BorderLayout();
    BorderLayout ZCond_Title_BL=new BorderLayout();
    GridLayout ZCond_Cond_GL=new GridLayout(4,3);
    BorderLayout ZCond_Data_BL[][];
    JLabel FZ_LBL,MinZ_LBL,MaxZ_LBL,Place_LBL,GNum_LBL;
    JLabel PlaceName_LBL[];
    JLabel ZCond_LBL=new JLabel();
    public  JTextField R_FZ[];
    public  JTextField R_MinZ[];
    public  JTextField R_MaxZ[];
    public  JTextField G_Num[];
    JPanel LPane_ZCond=new JPanel();
    BorderLayout LPane_ZCond_BL=new BorderLayout();
    public void Sim_UI_pack_ZCond_MPane() {
    	try {
            ZCond_MPane.setLayout(ZCond_M_BL);
            //ZCond_MPane.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
            ZCond_MMPane.setLayout(ZCond_MM_BL);
            ZCond_CondPane.setLayout(ZCond_Cond_GL);
            ZCond_DataPane=new JPanel[4][4];
            ZCond_Data_BL=new BorderLayout[4][4];
            PlaceName_LBL=new JLabel[11];
            for(int i=1;i<=3;i++) {
                PlaceName_LBL[i]=new JLabel();
                PlaceName_LBL[i].setHorizontalAlignment(0);
                for(int j=1;j<=3;j++) {
                    ZCond_DataPane[i][j]=new JPanel();
                    ZCond_Data_BL[i][j]=new BorderLayout();
                    //ZCond_DataPane[i][j].setBorder(new EmptyBorder(new Insets(1, 1,1, 1)));
                    ZCond_DataPane[i][j].setLayout(ZCond_Data_BL[i][j]);
                    ZCond_CondPane.add(ZCond_DataPane[i][j]);
                }
            }
            FZ_LBL=new JLabel();
            MinZ_LBL=new JLabel();
            MaxZ_LBL=new JLabel();
            Place_LBL=new JLabel();
            GNum_LBL=new JLabel();
            ZCond_DataPane[1][1].add(Place_LBL);
            ZCond_DataPane[1][2].add(FZ_LBL);
            //ZCond_DataPane[1][3].add(MinZ_LBL);
            ZCond_DataPane[1][3].add(MaxZ_LBL);
            //ZCond_DataPane[1][5].add(GNum_LBL);
            Place_LBL.setText("Reservoir");
            FZ_LBL.setText("Z0(m)");
            MaxZ_LBL.setText("Zmax(m)");
            GNum_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            FZ_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            MinZ_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            MaxZ_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            Place_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            Place_LBL.setHorizontalAlignment(0);
            FZ_LBL.setHorizontalAlignment(0);
            MinZ_LBL.setHorizontalAlignment(0);
            MaxZ_LBL.setHorizontalAlignment(0);
            GNum_LBL.setHorizontalAlignment(0);
            for(int i=1;i<=3;i++) {
                ImageIcon InQCMD = new ImageIcon("image/btn_7.gif");
                PlaceName_LBL[i].setBorder(BorderFactory.createEtchedBorder(0));
            }
            PlaceName_LBL[1].setText("Huichon");
            PlaceName_LBL[2].setText("Jangsu");
            ZCond_DataPane[2][1].add(PlaceName_LBL[1],ZCond_Data_BL[2][1].CENTER);
            ZCond_DataPane[3][1].add(PlaceName_LBL[2],ZCond_Data_BL[3][1].CENTER);

            R_FZ=new JTextField[3];
            R_MaxZ=new JTextField[3];    
            for(int i=1;i<=2;i++) {
                R_FZ[i]=new JTextField();
                R_MaxZ[i]=new JTextField();  
            }
            ZCond_DataPane[2][2].add(R_FZ[1]);
            ZCond_DataPane[2][3].add(R_MaxZ[1]);
            ZCond_DataPane[3][2].add(R_FZ[2]);
            ZCond_DataPane[3][3].add(R_MaxZ[2]);
            this.ZCond_LBL.setHorizontalAlignment(0);
            this.ZCond_LBL.setText("Water level of Reservoirs");
            ZCond_TitlePane.setLayout(this.ZCond_Title_BL);
            this.ZCond_TitlePane.add(this.ZCond_LBL,ZCond_Title_BL.CENTER);
            this.ZCond_TitlePane.setPreferredSize(new Dimension(0,30));
            ZCond_MMPane.add(ZCond_TitlePane,ZCond_MM_BL.NORTH);
            ZCond_MMPane.add(ZCond_CondPane,ZCond_MM_BL.CENTER);
            ZCond_MPane.add(ZCond_MMPane,ZCond_M_BL.CENTER);
            ZCond_MPane.setPreferredSize(new Dimension(0,140));
            this.LC_MPane.add(ZCond_MPane, LC_MPane_BL.NORTH);
            LPane_ZCond.setLayout(LPane_ZCond_BL);
            this.LC_MPane.add(LPane_ZCond, LC_MPane_BL.CENTER);
            System.out.println("ok! ZCond");
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }   
    public int PSO_RMPSG_M[]=new int[4];
    public int PSO_RMPSG_N[]=new int[4];
    public double PSO_RMPSG_MaxDQ[]=new double[4];
    
    public int PSO_Q_M[]=new int[4];
    public int PSO_Q_N[]=new int[4];
    public double PSO_Q_MaxGN[]=new double[4];

    public void Sim_UI_set_InitValue(){
        for(int i=1;i<=3;i++){
            this.PSO_RMPSG_M[i]=100;
            this.PSO_RMPSG_N[i]=500;
            this.PSO_Q_M[i]=100;
            this.PSO_Q_N[i]=500;
        }
        this.PSO_Q_MaxGN[1]=1.0;
        this.PSO_Q_MaxGN[2]=1.2;
        
        this.PSO_RMPSG_M_TXT.setText(""+PSO_RMPSG_M[1]);
        this.PSO_RMPSG_N_TXT.setText(""+PSO_RMPSG_N[1]);
        this.PSO_Q_M_TXT.setText(""+PSO_Q_M[1]);
        this.PSO_Q_N_TXT.setText(""+PSO_Q_N[1]);
        this.PSO_Q_MaxGN_TXT.setText(""+PSO_Q_MaxGN[1]);
        this.R_FZ[1].setText(""+(MFrame.Reservoir[1].NormalZ-1));
        this.R_FZ[2].setText(""+(MFrame.Reservoir[2].NormalZ-1));
        this.R_MaxZ[1].setText(""+MFrame.Reservoir[1].FloodZ);
        this.R_MaxZ[2].setText(""+MFrame.Reservoir[2].FloodZ);        
    }
    JPanel PSO_RMPSG_Pane=new JPanel();
    BorderLayout PSO_RMPSG_Pane_BL=new BorderLayout();
    JLabel PSO_RMPSG_LBL=new JLabel("PSO-RMPSG parameters");
    JPanel PSO_RMPSG_TPane=new JPanel();
    GridLayout PSO_RMPSG_TPane_GL=new GridLayout(4,2);
    JLabel PSO_RMPSG_RN_LBL=new JLabel("Select Reservoir");
    JComboBox PSO_RMPSG_RN_CB=new JComboBox();     
    JLabel PSO_RMPSG_M_LBL=new JLabel("M=");
    JLabel PSO_RMPSG_N_LBL=new JLabel("Kmax=");
    JTextArea PSO_RMPSG_M_TXT=new JTextArea("100");
    JTextArea PSO_RMPSG_N_TXT=new JTextArea("500");
    JPanel LPane_PSO_RMPSG=new JPanel();
    BorderLayout LPane_PSO_RMPSG_BL=new BorderLayout();
    public void Sim_UI_pack_PSO_RMPSG_Pane(){
        try{
            PSO_RMPSG_TPane.setLayout(PSO_RMPSG_TPane_GL);
            PSO_RMPSG_RN_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            PSO_RMPSG_RN_LBL.setHorizontalAlignment(0);
            PSO_RMPSG_RN_CB.setSelectedItem("Huichon");
            PSO_RMPSG_RN_CB.addItem("Huichon");
            PSO_RMPSG_RN_CB.addItem("Jangsu");            
            PSO_RMPSG_LBL.setHorizontalAlignment(0);
            PSO_RMPSG_M_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            PSO_RMPSG_M_LBL.setHorizontalAlignment(0);
            PSO_RMPSG_N_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            PSO_RMPSG_N_LBL.setHorizontalAlignment(0);
            PSO_RMPSG_TPane.add(PSO_RMPSG_RN_LBL);
            PSO_RMPSG_TPane.add(PSO_RMPSG_RN_CB);            
            PSO_RMPSG_TPane.add(PSO_RMPSG_M_LBL);
            PSO_RMPSG_TPane.add(PSO_RMPSG_M_TXT);
            PSO_RMPSG_TPane.add(PSO_RMPSG_N_LBL);
            PSO_RMPSG_TPane.add(PSO_RMPSG_N_TXT);
            //PSO_GN_Pane.setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
            PSO_RMPSG_Pane.setLayout(PSO_RMPSG_Pane_BL);
            PSO_RMPSG_Pane.add(PSO_RMPSG_LBL,PSO_RMPSG_Pane_BL.NORTH);
            PSO_RMPSG_Pane.add(PSO_RMPSG_TPane,PSO_RMPSG_Pane_BL.CENTER);
            PSO_RMPSG_Pane.setPreferredSize(new Dimension(0,120));
            LPane_ZCond.add(PSO_RMPSG_Pane,LPane_ZCond_BL.NORTH);
            LPane_PSO_RMPSG.setLayout(LPane_PSO_RMPSG_BL);
            LPane_ZCond.add(LPane_PSO_RMPSG,LPane_ZCond_BL.CENTER);
            PSO_RMPSG_M_TXT.getDocument().addDocumentListener(new DocumentListener(){
                    public void changedUpdate(DocumentEvent e){

                    }
                    public void insertUpdate(DocumentEvent e){
                            Object PName=PSO_RMPSG_RN_CB.getSelectedItem();
                            if(PName.equals("Huichon")){
                                PSO_RMPSG_M[1]=Integer.parseInt(PSO_RMPSG_M_TXT.getText().toString());
                            }else if(PName.equals("Jangsu")){
                                PSO_RMPSG_M[2]=Integer.parseInt(PSO_RMPSG_M_TXT.getText().toString());
                            }         
                    }
                    public void removeUpdate(DocumentEvent e){
                    }
            });  
            PSO_RMPSG_N_TXT.getDocument().addDocumentListener(new DocumentListener(){
                    public void changedUpdate(DocumentEvent e){

                    }
                    public void insertUpdate(DocumentEvent e){
                            Object PName=PSO_RMPSG_RN_CB.getSelectedItem();
                            if(PName.equals("Huichon")){
                                PSO_RMPSG_N[1]=Integer.parseInt(PSO_RMPSG_N_TXT.getText().toString());
                            }else if(PName.equals("Jangsu")){
                                PSO_RMPSG_N[2]=Integer.parseInt(PSO_RMPSG_N_TXT.getText().toString());
                            }         
                    }
                    public void removeUpdate(DocumentEvent e){
                    }
            });   
            PSO_RMPSG_RN_CB.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        Object PName=PSO_RMPSG_RN_CB.getSelectedItem();
                        if(PName.equals("Huichon")){
                            PSO_RMPSG_M_TXT.setText(""+PSO_RMPSG_M[1]);
                            PSO_RMPSG_N_TXT.setText(""+PSO_RMPSG_N[1]);
                        }else if(PName.equals("Jangsu")){
                            PSO_RMPSG_M_TXT.setText(""+PSO_RMPSG_M[2]);
                            PSO_RMPSG_N_TXT.setText(""+PSO_RMPSG_N[2]);                  
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            });            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    JPanel PSO_Q_Pane=new JPanel();
    BorderLayout PSO_Q_Pane_BL=new BorderLayout();
    JPanel PSO_Q_TPane=new JPanel();
    GridLayout PSO_Q_TPane_GL=new GridLayout(5,2);
    JLabel PSO_Q_LBL=new JLabel("PSO-Q parameters");
    JLabel PSO_Q_RN_LBL=new JLabel("Select reservoir");
    JComboBox PSO_Q_RN_CB=new JComboBox();     
    JLabel PSO_Q_M_LBL=new JLabel("M=");
    JLabel PSO_Q_N_LBL=new JLabel("Kmax=");
    JLabel PSO_Q_MaxDQ_LBL=new JLabel("Qmax=");
    JLabel PSO_Q_MinPN_LBL=new JLabel("");
    JTextArea PSO_Q_M_TXT=new JTextArea("100");
    JTextArea PSO_Q_N_TXT=new JTextArea("500");
    JTextArea PSO_Q_MaxGN_TXT=new JTextArea("1.5");
    JTextArea PSO_Q_MinPN_TXT=new JTextArea();
    JPanel LPane_PSO_Q=new JPanel();
    BorderLayout LPane_PSO_Q_BL=new BorderLayout();
    
    public void Sim_UI_pack_PSO_Q_Pane(){
        try{
            PSO_Q_LBL.setHorizontalAlignment(0);
            PSO_Q_TPane.setLayout(PSO_Q_TPane_GL);
            PSO_Q_RN_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            PSO_Q_RN_LBL.setHorizontalAlignment(0);      
            PSO_Q_RN_CB.addItem("Huichon");
            PSO_Q_RN_CB.addItem("Jangsu");
            PSO_Q_TPane.setLayout(PSO_Q_TPane_GL);
            PSO_Q_M_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            PSO_Q_N_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            PSO_Q_M_LBL.setHorizontalAlignment(0);
            PSO_Q_N_LBL.setHorizontalAlignment(0);
            PSO_Q_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            PSO_Q_MaxDQ_LBL.setHorizontalAlignment(0);
            PSO_Q_MinPN_LBL.setBorder(BorderFactory.createEtchedBorder(0));
            PSO_Q_MinPN_LBL.setHorizontalAlignment(0);
            PSO_Q_TPane.add(PSO_Q_RN_LBL);
            PSO_Q_TPane.add(PSO_Q_RN_CB);
            PSO_Q_TPane.add(PSO_Q_M_LBL);
            PSO_Q_TPane.add(PSO_Q_M_TXT);
            PSO_Q_TPane.add(PSO_Q_N_LBL);
            PSO_Q_TPane.add(PSO_Q_N_TXT);
            PSO_Q_TPane.add(PSO_Q_MaxDQ_LBL);
            PSO_Q_TPane.add(PSO_Q_MaxGN_TXT);            
            PSO_Q_Pane.setLayout(PSO_Q_Pane_BL);
            PSO_Q_Pane.add(PSO_Q_TPane,PSO_RMPSG_Pane_BL.CENTER);
            PSO_Q_Pane.setPreferredSize(new Dimension(0,150));
            LPane_PSO_RMPSG.add(PSO_Q_Pane,LPane_PSO_RMPSG_BL.NORTH);
            LPane_PSO_Q.setLayout(LPane_PSO_Q_BL);
            LPane_PSO_RMPSG.add(LPane_PSO_Q,LPane_PSO_RMPSG_BL.CENTER);
            PSO_Q_MaxGN_TXT.getDocument().addDocumentListener(new DocumentListener(){
                public void changedUpdate(DocumentEvent e){

                }
                public void insertUpdate(DocumentEvent e){
                        Object PName=PSO_Q_RN_CB.getSelectedItem();
                        if(PName.equals("Huichon")){
                            PSO_Q_MaxGN[1]=Double.parseDouble(PSO_Q_MaxGN_TXT.getText().toString());
                        }else if(PName.equals("Jangsu")){
                            PSO_Q_MaxGN[2]=Double.parseDouble(PSO_Q_MaxGN_TXT.getText().toString());
                        }         
                }
                public void removeUpdate(DocumentEvent e){
                }
            });
            PSO_Q_M_TXT.getDocument().addDocumentListener(new DocumentListener(){
                    public void changedUpdate(DocumentEvent e){

                    }
                    public void insertUpdate(DocumentEvent e){
                            Object PName=PSO_Q_RN_CB.getSelectedItem();
                            if(PName.equals("Huichon")){
                                PSO_Q_M[1]=Integer.parseInt(PSO_Q_M_TXT.getText().toString());
                            }else if(PName.equals("Jangsu")){
                                PSO_Q_M[2]=Integer.parseInt(PSO_Q_M_TXT.getText().toString());
                            }         
                    }
                    public void removeUpdate(DocumentEvent e){
                    }
            });  
            PSO_Q_N_TXT.getDocument().addDocumentListener(new DocumentListener(){
                    public void changedUpdate(DocumentEvent e){

                    }
                    public void insertUpdate(DocumentEvent e){
                            Object PName=PSO_Q_RN_CB.getSelectedItem();
                            if(PName.equals("Huichon")){
                                PSO_Q_N[1]=Integer.parseInt(PSO_Q_N_TXT.getText().toString());
                            }else if(PName.equals("Jangsu")){
                                PSO_Q_N[2]=Integer.parseInt(PSO_Q_N_TXT.getText().toString());
                            }         
                    }
                    public void removeUpdate(DocumentEvent e){
                    }
            });         
            PSO_Q_RN_CB.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        Object PName=PSO_Q_RN_CB.getSelectedItem();
                        if(PName.equals("Huichon")){
                            PSO_Q_M_TXT.setText(""+PSO_Q_M[1]);
                            PSO_Q_N_TXT.setText(""+PSO_Q_N[1]);
                            PSO_Q_MaxGN_TXT.setText(""+PSO_Q_MaxGN[1]);
                        }else if(PName.equals("Jangsu")){
                            PSO_Q_M_TXT.setText(""+PSO_Q_M[2]);
                            PSO_Q_N_TXT.setText(""+PSO_Q_N[2]);
                            PSO_Q_MaxGN_TXT.setText(""+PSO_Q_MaxGN[2]);                        
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            });             
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }    

    public static Graph InQ_Graph=new Graph();
    public double G_Data[][];
    public int GN;
    public void Sim_UI_draw_in_QData(int R_No) {
        try {
     	    GN=(int)(MFrame.Reservoir[R_No].UpN*0.9);
            G_Data=new double[2][GN+1];
            G_Data[1]=MFrame.Reservoir[R_No].UpQ;
            InQ_Graph.D_String=new String[4];
            InQ_Graph.D_String[1]="Qmax=";
            InQ_Graph.D_Color=new Color[2];
            InQ_Graph.D_Color[1]=Color.red;
            InQ_Graph.InputBoundaryGraphData(MFrame.Reservoir[R_No].UpT_SM,
                                 G_Data,
                                 1,
                                 GN,
                                 "T/h", "Q(㎥/s)");     
            InQ_Graph.InQFlag=true;
            InQ_Graph.Q_Flag=true;
            InQ_Graph.RedFlag=true;
            if(InQ_Graph.P_Ymax<1500){
                InQ_Graph.P_Ymax=1500;
            }
            this.InQ_Pane.add(InQ_Graph, InQ_Pane_BL.CENTER);
            InQ_Graph.setVisible(false);
            InQ_Graph.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

     } 
    public Graph Sim_QGraph=new Graph();
    public void Sim_UI_Draw_Sim_QData(int R_No) {
        try{
            GN=(int)(MFrame.Reservoir[R_No].UpN*0.9);
            //System.out.println("GN="+GN);
            if(MFrame.Reservoir[R_No].Opt_Flag==true) {

                System.out.println("ok! Success!");
                G_Data=new double[5][GN+1];
                G_Data[1]=MFrame.Reservoir[R_No].UpQ;

                if(Mth_PSO_RMPSG_OPT.isSelected()){
                    G_Data[2]=MFrame.Sim_UI.PSO_RMPSG[MFrame.SIM_MDL.Cal_ResN].N_SumQ;
                    G_Data[3]=MFrame.Sim_UI.PSO_RMPSG[MFrame.SIM_MDL.Cal_ResN].D_SumQ;
                    G_Data[4]=MFrame.Reservoir[R_No].PSO_RMPSG_Q;
                }else if(Mth_PSO_Q_OPT.isSelected()){
                    G_Data[2]=MFrame.Sim_UI.PSO_Q[MFrame.SIM_MDL.Cal_ResN].N_SumQ;
                    G_Data[3]=MFrame.Sim_UI.PSO_Q[MFrame.SIM_MDL.Cal_ResN].D_SumQ;                            
                    G_Data[4]=MFrame.Reservoir[R_No].PSO_Q_Q;
                }
                Sim_QGraph.D_String=new String[5];
                Sim_QGraph.D_String[1]="Natural inflow of reservoir";
                Sim_QGraph.D_String[2]="Natural inflow at downstream";
                Sim_QGraph.D_String[3]="Control discharge at downstream";
                Sim_QGraph.D_String[4]="Control discharge of reservoir";

                Sim_QGraph.D_Color=new Color[7];
                Sim_QGraph.D_Color[1]=Color.gray;
                Sim_QGraph.D_Color[2]=Color.gray;
                Sim_QGraph.D_Color[3]=new Color(0,200,0);
                Sim_QGraph.D_Color[4]=new Color(255,0,0);
                Sim_QGraph.InputBoundaryGraphData(MFrame.Reservoir[R_No].UpT_SM,
                                     G_Data,
                                     4,
                                     GN,
                                     "T/h", "Q(㎥/s)");  
            }else{
                G_Data=new double[5][GN+1];
                G_Data[1]=MFrame.Reservoir[R_No].UpQ;
                Sim_QGraph.D_String=new String[5];
                Sim_QGraph.D_String[1]="Natural inflow of reservoir"; 
                Sim_QGraph.D_Color=new Color[7];
                Sim_QGraph.D_Color[1]=Color.gray;  
                Sim_QGraph.InputBoundaryGraphData(MFrame.Reservoir[R_No].UpT_SM,
                                     G_Data,
                                     1,
                                     GN,
                                     "T/h", "Q(㎥/s)");                 
            }
                Sim_QGraph.InQFlag=true;
                Sim_QGraph.Q_Flag=true;
                if(Sim_QGraph.P_Ymax<3000){
                    Sim_QGraph.P_Ymax=3000;
                }
                this.OptQ_R_DnPane.add(Sim_QGraph,OptQ_R_DnPane_BL.CENTER);
                OptQ_R_DnPane.setVisible(false);
                OptQ_R_DnPane.setVisible(true);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
     }   
    public Graph SimZ_Graph=new Graph();
    public void Sim_UI_Draw_Sim_ZData(int R_No) {

        try {
            GN=(int)(MFrame.Reservoir[R_No].Time_SelN*0.9);
                    if(MFrame.Reservoir[R_No].Opt_Flag==true){
                            G_Data=new double[2][GN+1];
                            if(Mth_PSO_RMPSG_OPT.isSelected()){
                                G_Data[1]=MFrame.Reservoir[R_No].PSO_RMPSG_Z;
                            }else if(Mth_PSO_Q_OPT.isSelected()){
                                G_Data[1]=MFrame.Reservoir[R_No].PSO_Q_Z;
                            }
                    }else{
                            G_Data=new double[2][GN+1];     
                            G_Data[1]=MFrame.Reservoir[R_No].T_RCalZ;
                    }
                            SimZ_Graph.D_String=new String[2];
                            SimZ_Graph.D_String[1]="Watel level process of reservoir";
                            SimZ_Graph.D_Color=new Color[2];
                            SimZ_Graph.D_Color[1]=Color.BLUE;
                            SimZ_Graph.InputBoundaryGraphData(MFrame.Reservoir[R_No].UpT_SM,
                                                                G_Data,
                                                                1,
                                                                GN,
                                                                "T/h", "Z/m");

                    if(R_No==1){
                        SimZ_Graph.P_Ymax = MFrame.Reservoir[R_No].FloodZ+3;
                        SimZ_Graph.P_Ymin=MFrame.Reservoir[R_No].NormalZ-5;
                    }else if(R_No==2){
                        SimZ_Graph.P_Ymax = MFrame.Reservoir[R_No].FloodZ+3;
                        SimZ_Graph.P_Ymin=MFrame.Reservoir[R_No].NormalZ-10;
                    }
                    SimZ_Graph.InQFlag = true;
                    SimZ_Graph.Z_Flag=true;
                    SimZ_Graph.FloodZ=MFrame.Reservoir[R_No].FloodZ;
                    SimZ_Graph.NormalZ=MFrame.Reservoir[R_No].NormalZ;
                    SimZ_Graph.DieZ=MFrame.Reservoir[R_No].DieZ;
                    this.OptQ_R_UpPane.add(SimZ_Graph,OptQ_R_UpPane_BL.CENTER);
                    OptQ_R_UpPane.setVisible(false);
                    OptQ_R_UpPane.setVisible(true);
           
        } catch (Exception exception) {
            exception.printStackTrace();
        }

     }   
    public GN_Graph OptGN_Graph[];
    
    public void Sim_UI_Draw_OptGN_Graph(int R_No) {

        try {
            GN=(int)(MFrame.Reservoir[R_No].Time_SelN*0.9);
            OptGN_Graph=new GN_Graph[MFrame.Reservoir[R_No].Cal_MaxGN+1];
            
            for(int i=1;i<=MFrame.Reservoir[R_No].Cal_MaxGN;i++){
                OptGN_Graph[i]=new GN_Graph();
            }
            if(MFrame.Reservoir[R_No].Opt_Flag==true){    
                if(MFrame.Sim_UI.Mth_PSO_RMPSG_OPT.isSelected()){
                    GN_RPane.removeAll();
                    GN_RPane.setLayout(GN_RPane_GL);
                    for(int i=1;i<=MFrame.Reservoir[R_No].Cal_MaxGN;i++){
                        G_Data=new double[2][GN+1];
                        for(int j=1;j<=GN;j++){
                            G_Data[1][j]=Double.parseDouble(""+MFrame.Reservoir[R_No].Step_OptGN[i][j]);
                            //System.out.println(G_Data[i][j]);
                        }
//                        G_Data[1]=MFrame.Reservoir[R_No].PSO_RMPSG_Z;
                        OptGN_Graph[i].D_String=new String[2];
                        if(i==1){
                            OptGN_Graph[i].D_String[1]="Intital operating plan by PSO-GN";
                        }else{
                            OptGN_Graph[i].D_String[1]=i-1+"-level arrangement by RMPSG";
                        }
                            
                        
                        OptGN_Graph[i].D_Color=new Color[2];
                        OptGN_Graph[i].D_Color[1]=Color.BLUE;
                        OptGN_Graph[i].InputBoundaryGraphData(MFrame.Reservoir[R_No].UpT_SM,
                                                            G_Data,
                                                            1,
                                                            GN,
                                                            "T/h", "GN");  
                        OptGN_Graph[i].InQFlag = true;
                        OptGN_Graph[i].Z_Flag=true;
                        this.GN_RPane.add(OptGN_Graph[i]);
                        OptGN_Graph[i].setVisible(false);
                        OptGN_Graph[i].setVisible(true);
//                        this.GN_R_CPane[i].add(OptGN_Graph[i],GN_R_CPane_BL[i].CENTER);
//                        GN_R_CPane[i].setVisible(false);
//                        GN_R_CPane[i].setVisible(true);                             
                    }
                   }
            }
           
        } catch (Exception exception) {
            exception.printStackTrace();
        }

     }        
     public int F_GN[];
     public boolean Flag=false;
     public PSO_RMPSG PSO_RMPSG[];
     public PSO_Q PSO_Q[];
     public double Gbest_MaxQ=100000;
     public class RunFlowModelThread extends Thread implements Runnable {
         public void run(){ 
            try{
                    for(int i=1;i<=2;i++) {
                            MFrame.Reservoir[i].UpZ=Double.parseDouble(R_FZ[i].getText().toString());
                            MFrame.Reservoir[i].CalZ=Double.parseDouble(R_FZ[i].getText().toString());
                            MFrame.Reservoir[i].C_CalZ=Double.parseDouble(R_FZ[i].getText().toString());
                    }            
                    Thread myThread = Thread.currentThread();
                    MFrame.Sim_UI.progBar.progBar.setValue(0);
                    MFrame.Sim_UI.progBar.progBar.setString("Searching optimal operating plan...");
               
                    while (SimulationThread == myThread) {
                        if(Flag==false){
                        MFrame.SIM_MDL.SIM_Check_EndZ_For_Reservoirs();
                        //수문개방대수를 립자무리로 설정한 경우 SimplePSO에 의한 최량큰물조절모의
                        if(Mth_PSO_RMPSG_OPT.isSelected()){
                            if(MFrame.SIM_MDL.Cal_ResN>=1){
                                PSO_RMPSG=new PSO_RMPSG[MFrame.SIM_MDL.Cal_ResN+1];
                                for(int i=1;i<=MFrame.SIM_MDL.Cal_ResN;i++){
                                    PSO_RMPSG[i]=new PSO_RMPSG();
                                    PSO_RMPSG[i].ResNo=MFrame.SIM_MDL.O_ResNo[i];
                                    PSO_RMPSG[i].Opt_RN=0;
                                    if(PSO_RMPSG[i].ResNo==1){
                                        PSO_RMPSG[i].M=PSO_RMPSG_M[1];
                                        PSO_RMPSG[i].opt_tmax=100;
                                    }else if(PSO_RMPSG[i].ResNo==2){
                                        PSO_RMPSG[i].M=PSO_RMPSG_M[2];
                                        PSO_RMPSG[i].opt_tmax=100;
                                    }
                                    PSO_RMPSG[i].k_n=0;
                                }
                                MFrame.Sim_UI.progBar.progBar.setMaximum(PSO_RMPSG[1].opt_tmax*MFrame.SIM_MDL.Cal_ResN);
                                MFrame.Sim_UI.progBar.progBar.setValue(0);
                                for(int i=1;i<=MFrame.SIM_MDL.Cal_ResN;i++) {
                                        Gbest_MaxQ=100000;
                                        PSO_RMPSG[i].Opt_RN=(i-1)*PSO_RMPSG[i].opt_tmax;
                                        PSO_RMPSG[i].r_n=0;
                                        PSO_RMPSG[i].CHAPSO_GN_Optimal_Simulation_For_One_Reservoir();
                                        MFrame.Sim_UI.progBar.progBar.setValue(PSO_RMPSG[i].Opt_RN+PSO_RMPSG[i].opt_tmax);
                                }                                
                            }                         
                        }
                        if(Mth_PSO_Q_OPT.isSelected()){
                            if(MFrame.SIM_MDL.Cal_ResN>=1){
                               PSO_Q=new PSO_Q[MFrame.SIM_MDL.Cal_ResN+1];
                                for(int i=1;i<=MFrame.SIM_MDL.Cal_ResN;i++){
                                    PSO_Q[i]=new PSO_Q();
                                    PSO_Q[i].ResNo=MFrame.SIM_MDL.O_ResNo[i];
                                    PSO_Q[i].Opt_RN=0;
                                    if(PSO_Q[i].ResNo==1){
                                        PSO_Q[i].Msize=PSO_Q_M[1];
                                        PSO_Q[i].Opt_Max=PSO_Q_N[1];
                                        PSO_Q[i].MaxGN=PSO_Q_MaxGN[1];
                                    }else if(PSO_Q[i].ResNo==2){
                                        PSO_Q[i].Msize=PSO_Q_M[2];
                                        PSO_Q[i].Opt_Max=PSO_Q_N[2];     
                                        PSO_Q[i].MaxGN=PSO_Q_MaxGN[2];
                                    }                  
                                }                                
                            }    
                            MFrame.Sim_UI.progBar.progBar.setMaximum(MFrame.SIM_MDL.Cal_ResN*PSO_Q[1].Opt_Max);
                            MFrame.Sim_UI.progBar.progBar.setValue(0);
                                for(int i=1;i<=MFrame.SIM_MDL.Cal_ResN;i++){
                                    PSO_Q[i].Opt_RN=(i-1)*PSO_Q[i].Opt_Max;
                                    PSO_Q[i].SIMPSO_PLAN_Optimal_FloodControl_For_OneReservoir_By_SimplePSO();
                                    MFrame.Sim_UI.progBar.progBar.setValue(PSO_Q[i].Opt_RN+PSO_Q[i].Opt_Max);
                                }                            
                        }
                            SimRunCMD.setEnabled(true);
                            Cal_Flag=true;
                            MFrame.Sim_UI.progBar.progBar.setString("Successfully Completed!");
                            Sim_UI_Input_TableData(1);
                            Sim_UI_Draw_Sim_QData(1);
                            Sim_UI_Draw_Sim_ZData(1);
                            if(Mth_PSO_RMPSG_OPT.isSelected()){
                                Sim_UI_pack_GN_Pane(MFrame.Reservoir[1].Cal_MaxGN);
                                Sim_UI_InputGN_TableData(1);
                                Sim_UI_Draw_OptGN_Graph(1);
                                TabPane.setSelectedComponent(OptQ_Pane);                                 
                            }
                            MFrame.Sim_UI.stop();
                        }
                    }
            }catch(Exception ex){
            	ex.printStackTrace();
            }
        }
    }   
    Thread SimulationThread;
    public void start() {
        SimulationThread = new RunFlowModelThread();
        SimulationThread.start();
     }
     public void stop(){
         try{
            SimulationThread.stop();
            SimulationThread=null;
            progBar.progBar.setString("Ok! Stop simulation!");
            this.SimRunCMD.setEnabled(true);
                  
         }catch(Exception ex){
             
         }
    } 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
