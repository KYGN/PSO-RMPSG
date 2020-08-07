package main_interface;

import models.*;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Font;
import java.util.Random;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import main_interface.MFrame;

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
public class GN_Graph extends JPanel{
    BorderLayout borderLayout1 = new BorderLayout();
    final static float dash1[] = {2.0f};
    final static BasicStroke dashed = new BasicStroke(1.0f,
                                                          BasicStroke.CAP_BUTT,
                                                          BasicStroke.JOIN_MITER,
                                                      10.0f, dash1, 0.0f);
    public GN_Graph() {
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public double G_X[]; public double G_Y[][]; int G_XN; int G_YN;
    public double G_X1[]; public double G_Y1[];
    String G_T[];
    String G_XStr; String G_YStr;
    boolean RedrawFlag=false;
    boolean SecFlag=false;
    public boolean InQFlag=false;
    public boolean RedFlag=false;
    public boolean BlueFlag=false;
    public boolean Z_Flag=false;
    public boolean Q_Flag=false;
    public double FloodZ;
    public double NormalZ;
    public double DieZ;
    public double BondZ;
    public double Gate_Sill;
    public void paint(Graphics g){
       this.setBackground(Color.white);
       //repaint();
       Graphics2D GG = (Graphics2D) g;
       if(InQFlag==true){
           this.Draw_Graph(GG);
       }

     }
     double P_Ymax_Str[];
     int DN;
     public void InputBoundaryGraphData(double XX[],double YY[][], int DataN,int XN, String X_x, String Y_y) {
         G_XN=XN;
         DN=DataN;
         G_X=new double[G_XN+1];
         G_Y=new double[DataN+1][G_XN+1];
         P_Ymax_Str=new double[DataN+1];
         if(this.BlueFlag==true){
             int aa=0;
         }
         P_Xmax=XX[1]; P_Ymax=YY[1][1]; P_Xmin=XX[1]; P_Ymin=YY[1][1];
         for (int i = 1; i <= G_XN; i++) {
             G_X[i] = XX[i];
             if (P_Xmax < G_X[i]) {
                 P_Xmax = G_X[i];
             }
         }
         
         for(int i=1;i<=DataN;i++) {
        	 P_Ymax_Str[i]=G_Y[i][1];
        	 for(int j=1;j<=G_XN;j++) {
        		 G_Y[i][j]=YY[i][j];
        		 if(P_Ymax_Str[i]<G_Y[i][j]) {
        			 P_Ymax_Str[i]=G_Y[i][j];
        		 }
        		 if(P_Ymax<G_Y[i][j]) {
        			 P_Ymax=G_Y[i][j];
        		 }
        		 if(P_Ymin>G_Y[i][j]) {
        			 P_Ymin=G_Y[i][j];
        		 }
        	 }
        	 ///System.out.println("P_Ymax_Str="+P_Ymax_Str[i]);;
         }
         P_Ymax=P_Ymax+P_Ymax*0.1;
         G_XStr = X_x;  G_YStr = Y_y;    	 
     }
     public double P_Xmin,P_Xmax,P_Ymin,P_Ymax;
     public double X_max,X_min,Y_max,Y_min,X_rate,Y_rate;
     public int SelN=1;
     public String SelString;
     public int Day,Time,Min;
     public int FM,FDy,FT;
     public boolean GrayFlag=false;
     public String D_String[];
     public Color D_Color[];

     public void Draw_Graph(Graphics2D GG){
       try{

           DecimalFormat myFormat = new DecimalFormat("##.##");
           X_max = this.getWidth()-this.getWidth()/30;
           Y_max = this.getHeight()-this.getHeight()/10;
           X_min=this.getWidth()/30;
           Y_min=this.getHeight()/10;

           if(P_Ymax==P_Ymin){
               P_Ymax=P_Ymin+10;
           }
           P_Ymax=5;
           Y_rate = (P_Ymax - P_Ymin) / (Y_max-Y_min);
           X_rate = (P_Xmax - P_Xmin) / (X_max-X_min);

           int x1; int y1;int x2;int y2;
           int P_SX=(int)(P_Xmax-P_Xmin);
           int X_dif=P_SX/10;
           double Y_dif=(P_Ymax-P_Ymin)/10;
           GG.setColor(new Color(216,216,213));
           int k=0;
           for (double i = (int) P_Ymin; i <= (int) P_Ymax; i=i+1) {
               y1 = (int) (Y_max - (i - P_Ymin) / Y_rate);
               GG.drawLine((int)X_min, (int)(y1), (int)X_max, (int)(y1));
           }
           k=0;

           GG.setColor(new Color(232,232,229));
           GG.setStroke(new BasicStroke(3.5f));
           GG.drawLine((int)X_min, (int)Y_min, (int)X_max, (int)Y_min);
           GG.drawLine((int)X_min, (int)Y_min, (int)X_min, (int)Y_max);
           GG.drawLine((int)X_min, (int)Y_max, (int)X_max, (int)Y_max);
           GG.drawLine((int)X_max, (int)Y_min, (int)X_max, (int)Y_max);
           GG.setColor(Color.black);
           
           for(int i=1;i<=G_XN;i++) {
        	   //System.out.println(MFrame.Reservoir[1].C_Time[i]);
        	   if(MFrame.Reservoir[1].Time[i]==0) {
                   GG.setStroke(new BasicStroke(3.0f));
                   GG.setColor(new Color(232,232,229));
                   x1=(int)((this.G_X[i]-P_Xmin)/X_rate);
                   GG.drawLine((int) (x1 + X_min), (int) Y_max, (int) (x1 + X_min),
                           (int) Y_min);
                   GG.setColor(Color.black);
                   GG.drawString((MFrame.Reservoir[1].Mth[i])+"/"+(MFrame.Reservoir[1].Dy[i]), (int) (x1 + X_min),
                           (int) (Y_min+Y_max * 0.04));
                   
        	   }
        	   if(MFrame.Reservoir[1].Time[i]==12) {
                   GG.setStroke(new BasicStroke(2.0f));
                   GG.setColor(new Color(232,232,229));
                   x1=(int)((this.G_X[i]-P_Xmin)/X_rate);
                   GG.drawLine((int) (x1 + X_min), (int) Y_max, (int) (x1 + X_min),
                           (int) Y_min);
                   GG.setColor(Color.black);
                   GG.drawString((MFrame.Reservoir[1].Time[i])+"h", (int) (x1 + X_min),
                           (int) (Y_max - Y_max * 0.01));        		   
        	   }
           }

           for (double i = (int) P_Ymin; i <= (int) P_Ymax; i++) {
               y1 = (int) (Y_max - (i - P_Ymin) / Y_rate);
               GG.drawString(""+(int)i,(int)X_min, (int)(y1));
           }
           GG.setStroke(new BasicStroke(1.5f));
           GG.setColor(new Color(251, 126, 138));
           GG.setStroke(new BasicStroke(2.0f));

            for(int j=1;j<=DN;j++) {
                for (int i = 1; i <= G_XN - 2; i++) {
                    //if(G_Y[j][i+1]>=0) {
                            x1 = (int) ((G_X[i] - P_Xmin) / X_rate);
                            y1 = (int) ((Y_max - (G_Y[j][i] - P_Ymin) / Y_rate));
                            x2 = (int) ((G_X[i + 1] - P_Xmin) / X_rate);
                            y2 = (int) ((Y_max - (G_Y[j][i + 1] - P_Ymin) / Y_rate));
                            GG.setColor(new Color(D_Color[j].getRGB()));
                            GG.drawOval((int) (x1 + X_min) - 3, y1 - 3, 6, 6);
                            GG.drawLine((int) (x1 + X_min), (int) (y1), (int) (x2 + X_min),
                            (int) (y2)); 
                    //}
                }
            }   
            for(int i=1;i<=DN;i++){
                 x1=(int)(X_max*0.6);
                 y1=0;
                 GG.setColor(new Color(D_Color[i].getRGB()));
                 if(i==1){
                     y1=(int)(Y_max*0.25);     
                 }else if(i==2){
                     y1=(int)(Y_max*0.25);     
                 }else if(i==3){
                     y1=(int)(Y_max*0.30);
                 }else if(i==4){
                     y1=(int)(Y_max*0.35);
                 }else if(i==5){
                     y1=(int)(Y_max*0.40);
                 }else if(i==6){
                     y1=(int)(Y_max*0.45);
                 }
                 GG.drawString(D_String[i], x1, y1);
           }          
 
           GG.setColor(Color.black);
           GG.drawString(G_XStr, (int)X_max, (int)this.getHeight());
           GG.drawString(G_YStr, (int)X_min-20, (int)(Y_min));

           for(int i=1;i<=DN;i++){
                x1 = (int)( X_min+((G_X[SelN] - P_Xmin) / X_rate));
                y1 = (int) ((Y_max - (G_Y[i][SelN] - P_Ymin) / Y_rate));
                x2 = (int) (X_min+((G_X[SelN + 1] - P_Xmin) / X_rate));
                y2 = (int) ((Y_max - (G_Y[i][SelN + 1] - P_Ymin) / Y_rate));
                if(this.RedFlag==true){
                    GG.setColor(Color.red);
                }else if(this.BlueFlag==true){
                    GG.setColor(Color.BLUE);
                }
                GG.setStroke(new BasicStroke(1.0f));
                GG.drawOval(x1 - 3, y1 - 3, 6, 6);
                GG.setStroke(dashed);
                GG.drawLine(x1,y1,x1,(int)Y_max);
                GG.drawLine(x1,y1,(int)X_min,y1);
                GG.setColor(Color.black);
         
           }


       }catch(Exception ex){
           //ex.printStackTrace();
       }

   }
    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.setBackground(Color.white);
        this.setOpaque(false);
        this.addMouseMotionListener(new GN_Graph_this_mouseMotionAdapter(this));
    }
    int MX,MY;
    public void this_mouseMoved(MouseEvent mouseEvent) {
        MX = mouseEvent.getX();
        MY = mouseEvent.getY();
    }
}


class GN_Graph_this_mouseMotionAdapter extends MouseMotionAdapter {
    private GN_Graph adaptee;
    GN_Graph_this_mouseMotionAdapter(GN_Graph adaptee) {
        this.adaptee = adaptee;
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        adaptee.this_mouseMoved(mouseEvent);
    }
}
