package models;

import java.awt.Color;
import java.text.DecimalFormat;
import main_interface.MFrame;

public class SIM_MDL {

        public int Cal_ResN=0;
        public int O_ResNo[];
        public boolean OptFlag=false;
        public void SIM_Check_EndZ_For_Reservoirs() {
            try {
                for(int i=1;i<=2;i++){
                    MFrame.Reservoir[i].Time_SelN=MFrame.Reservoir[i].UpN;
                    MFrame.Reservoir[i].Time_GN=new int[MFrame.Reservoir[i].Time_SelN+1];
                    MFrame.Reservoir[i].Time_GT=new int[MFrame.Reservoir[i].Time_SelN+1];
                }
                OptFlag=false;
                for(int j=1;j<=MFrame.Reservoir[1].Time_SelN;j++){
                    MFrame.Reservoir[1].Time_GN[j]=0;
                    MFrame.Reservoir[1].Time_GT[j]=(j-1)*1*3600;
                    MFrame.Reservoir[2].Time_GN[j]=0;
                    MFrame.Reservoir[2].Time_GT[j]=(j-1)*1*3600;

                }           
                MFrame.Reservoir[1].FullOpenGN = MFrame.Reservoir[1].Time_GN[1];
                MFrame.Reservoir[1].W_GN = MFrame.Reservoir[1].Time_GN[1];
                MFrame.Reservoir[2].FullOpenGN = MFrame.Reservoir[2].Time_GN[1];
                MFrame.Reservoir[2].W_GN = MFrame.Reservoir[2].Time_GN[1];
                MFrame.SIM_MDL.SIM_Simulation_For_FloodControl_of_Reservoirs_By_BalanceMethod_OneHour();
                for(int i=1;i<=2;i++) {
                    MFrame.Reservoir[i].Opt_Flag=false;
                }
                OptFlag=false;         
                if(MFrame.Reservoir[1].C_CalZ>MFrame.Reservoir[1].NormalZ) {
                        MFrame.Reservoir[1].Opt_Flag=true;this.OptFlag=true;
                }            
                if(MFrame.Reservoir[2].C_CalZ>MFrame.Reservoir[2].NormalZ) {
                        MFrame.Reservoir[2].Opt_Flag=true;this.OptFlag=true;
                }
                //저수지클라스의 최량조절기발변수들을 검사하여 몇개의 저수지들이 최량조절에 참가하는가를 검사한다.
                int rr=0;
                for(int i=1;i<=2;i++) {
                    if(MFrame.Reservoir[i].Opt_Flag==true) {
                            rr++;
                    }
                }
                Cal_ResN=rr;
                //최량조절계산에 참가하는 저수지들의 번호를 기억하기 위한 배렬변수를 구성한다.
                O_ResNo=new int[Cal_ResN+1];
                rr=0;
                for(int i=1;i<=2;i++) {
                    if(MFrame.Reservoir[i].Opt_Flag==true){
                            rr++;O_ResNo[rr]=i;
                            System.out.println("O_ResNo="+O_ResNo[rr]);
                    }
                }	   	        
            }catch(Exception ex) {
                    ex.printStackTrace();
            }
        }   
    public int CN;
    public void SIM_Simulation_For_FloodControl_of_Reservoirs_By_BalanceMethod_OneHour(){
        try {
            CN=(int)(MFrame.Reservoir[1].Time_SelN);
            for (int i = 1; i <= 2; i++) {
                MFrame.Reservoir[i].CountT = 0;
                MFrame.Reservoir[i].TotalN = CN;
                MFrame.Reservoir[i].FullOpenGN = MFrame.Reservoir[i].W_GN;
            }
            for (int i = 1; i <= CN; i++) {
                for (int j = 1; j <=2; j++) {
                    MFrame.Reservoir[j].FullOpenGN=MFrame.Reservoir[j].Time_GN[i];
                    MFrame.Reservoir[j].FloodControl_For_Reservoir_By_One_Hour();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
   }     
}
