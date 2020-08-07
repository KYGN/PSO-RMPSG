/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;
import main_interface.MFrame;

/**
 *
 * @author Administrator
 */
public class Reservoir {
    public double NormalZ;      //Normal
    public double DieZ;         //사수위
    public double UpZ;          //상류수위
    public String Name;         //저수지이름
    public int R_No;            //저수지번호
    public double FloodZ;       //큰물수위
    public double Pre_FZ;       //큰물맞이수위
    public int Pre_FT;          //큰물맞이용적을 조성하는 시간
    public double NormalV;	//저수지정상용적
    public double DieV;		//저수지사용적
    public double FloodV;	//저수지큰물용적
    int Reservoir_Key;		//저수지키
    int Reservoir_ID;		//저수지ID    
    public int GateN;           //
    public double Z_Sill;       //
    public int Cal_MaxGN;
    //자료기지로부터 저수지클라스 상류큰물류입과정선을 불러들인다.

    public int UpN,k;
    public String StrUpT[];
    public int Yr[],Mth[],Dy[],Time[];
    public double UpT_SM[];
    public double UpT[],UpQ[];
    public long UpTS[];
    public int getYear(String str) {
        return Integer.valueOf(str.substring(0, 4));
    }
    public int getMonth(String str) {
        return Integer.valueOf(str.substring(5, 7));
    }

    public int getDay(String str) {
        return Integer.valueOf(str.substring(8, 10));
    }

    public int getTime(String str) {
        return Integer.valueOf(str.substring(11, 13));
    }    
    public void Input_UpData() {
        try {
            UpN=MFrame.ConnectData.UpN;
            StrUpT = new String[UpN+1];
            UpT_SM=new double[UpN+1];
            this.Yr=new int[UpN+1];
            this.Mth=new int[UpN+1];
            this.Dy=new int[UpN+1];
            this.Time=new int[UpN+1];
            UpT=new double[UpN+1];
            UpTS=new long[UpN+1];
            UpQ=new double[UpN+1];
            k=0;
            String FStrUpT="";
            int FMth=0,FDy=0,FT=0;
            FMth=this.getMonth(MFrame.ConnectData.FStrUpT);
            FDy=this.getDay(MFrame.ConnectData.FStrUpT);
            FT=this.getTime(MFrame.ConnectData.FStrUpT);
            int FFT=0;
            for(int i=1;i<=UpN;i++){
               UpT[i]=FT+(i-1);
               UpTS[i]=(long)(UpT[i]-UpT[1])*3600;
               UpT_SM[i]=(int)(UpT[i]-UpT[1])*60;
               if(this.Name.equals("Huichon")){
                   UpQ[i]=MFrame.ConnectData.Huichon_InQ[i];
               }else if(this.Name.equals("Jangsu")){
                   UpQ[i]=MFrame.ConnectData.Jangsu_InQ[i];
               }
               k=(int)(UpT[i]/24);
               int TT = (int)(UpT[i] - 24 * k);
               int Com_Dy = 0;
               if (FMth == 1 || FMth == 3 || FMth == 5 || 
                   FMth == 7 || FMth == 8 || FMth == 10 || FMth == 12) {
                   Com_Dy = 31;
               } else if (FMth == 4 || FMth == 6 || FMth == 9 || FMth == 11) {
                   Com_Dy = 30;
               } else if (FMth == 2){
                   Com_Dy = 28;
               }  
               if ((FDy + k) > Com_Dy) {
                   Mth[i]=FMth+1;
                   Time[i]=TT;
                   Dy[i]=FDy + k - Com_Dy;            	   
               } else {
                   Mth[i]=FMth;
                   Time[i]=TT;
                   Dy[i]=FDy + k;
               }    
                if(Mth[i]<10){
                    if(Dy[i]<10){
                        if(Time[i]<10){
                            StrUpT[i]="0"+Mth[i]+"-0"+Dy[i]+" 0"+Time[i]+":00";
                        }else{
                            StrUpT[i]="0"+Mth[i]+"-0"+Dy[i]+" "+Time[i]+":00";
                        }
                    }else{
                        if(Time[i]<10){
                            StrUpT[i]="0"+Mth[i]+"-"+Dy[i]+" 0"+Time[i]+":00";
                        }else{
                            StrUpT[i]="0"+Mth[i]+"-"+Dy[i]+" "+Time[i]+":00";
                        }
                    }
                }else{
                    if(Dy[i]<10){
                        if(Time[i]<10){
                            StrUpT[i]=Mth[i]+"-0"+Dy[i]+" 0"+Time[i]+":00";
                        }else{
                            StrUpT[i]=Mth[i]+"-0"+Dy[i]+" "+Time[i]+":00";
                        }
                    }else{
                        if(Time[i]<10){
                            StrUpT[i]=Mth[i]+"-"+Dy[i]+" 0"+Time[i]+":00";
                        }else{
                            StrUpT[i]=Mth[i]+"-"+Dy[i]+" "+Time[i]+":00";
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        //검사완료
    } 
    public int Time_GN[];
    public int Time_GT[];
    public int Time_PartGN[];
    public double Time_PartZ[];
    public int Time_SelN;
    public int CountT;
    public int TotalN;
    public int FullOpenGN;
    public int OneOpenGN;
    public int Gate_OpenCloseT;
    public int DifGN;
    public int Pre_N;
    public int Pre_GS[];
    public int Pre_GN[];
    public String Pre_Flag[];
    public int W_GN;
    public double C_CalZ;
    public double CalZ;
    public boolean Opt_Flag=false;
    public int H_dt=3600;
    public double  CC_ResUpQ[];
    public double CalV,BCalV;
    public double Q1,Q2,OutQ1,OutQ2;
    public double DnZ;
    public double PartOpenZ;
    public double T_RCalV[],T_RCalInQ[],T_RCalQ[],T_RCalZ[],T_RCalDnZ[],T_RCalPartZ[];
    public int T_RCalGN[],T_RCalT[],T_RCalPartGN[],T_RCalAGN[];
    public void FloodControl_For_Reservoir_By_One_Hour(){
    	try {
            if(CountT==0) {
                C_CalZ=CalZ;CalV=this.Interpol_V(C_CalZ);BCalV=CalV;
                FullOpenGN=this.Time_GN[1];
                OutQ2 = this.MainGateQ(C_CalZ) * this.Time_GN[1];                    
                OutQ1=OutQ2;
                Q1=0;Q2=0;k=0;
                T_RCalV=new double[TotalN+1];
                T_RCalQ=new double[TotalN+1];
                T_RCalInQ=new double[TotalN+1];
                T_RCalZ=new double[TotalN+1];
                T_RCalGN=new int[TotalN+1];
                T_RCalT=new int[TotalN+1];
                T_RCalDnZ=new double[TotalN+1];
                T_RCalPartZ=new double[TotalN+1];
                T_RCalPartGN=new int[TotalN+1];
                T_RCalAGN=new int[TotalN+1];
            }else {
                OutQ2 = this.MainGateQ(C_CalZ) *  this.FullOpenGN;
                Q1=this.Upper_Q(CountT,UpN,UpTS,UpQ);
                Q2=this.Upper_Q(CountT+H_dt,UpN,UpTS,UpQ);
            //************시단말 저수지계산용적과 계산물높이를 계산한다.*********************************
                CalV=BCalV+(Q1+Q2)*0.0001*H_dt*0.5-(OutQ1+OutQ2)*0.0001*H_dt*0.5;
                C_CalZ=this.Interpol_Z(CalV);
            //***********만일 시단말계산물높이가 수문턱높이보다 작다면 발전기사용수량으로 방출량을 제한하여 다시 조절계산을 진행하고
                if(C_CalZ<=Z_Sill){
                    OutQ2=0;
                    OutQ1=OutQ2;
                    CalV=BCalV+(Q1+Q2)*0.0001*H_dt*0.5-(OutQ1+OutQ2)*0.0001*H_dt*0.5;
                    C_CalZ=this.Interpol_Z(CalV);
                }
                    OutQ1=OutQ2; BCalV=CalV;
            }
                k++;
                this.T_RCalGN[k]=this.FullOpenGN;
                //System.out.println("Name="+Name+" FullOpenGN="+this.T_RCalGN[k]+" CalZ="+C_CalZ+"OutQ="+OutQ2+"InQ="+(Q1+Q2)*0.5);
                this.T_RCalInQ[k]=(Q1+Q2)*0.5;
                this.T_RCalZ[k]=C_CalZ;
                this.T_RCalT[k]=(int)CountT;
                this.T_RCalQ[k]=(OutQ1+OutQ2)*0.5;
                this.T_RCalV[k]=CalV;
                this.T_RCalDnZ[k]=DnZ;
                this.T_RCalPartZ[k]=this.PartOpenZ;
                CountT=CountT+H_dt;

    	}catch(Exception ex) {
            System.out.println("RN="+this.R_No);
            ex.printStackTrace();
    	}
    }
    public void FloodControl_For_Reservoir_By_DischargePlan(){
        try{
    		if(CountT==0) {
                    C_CalZ=CalZ;CalV=this.Interpol_V(C_CalZ);BCalV=CalV;
                    OutQ1=OutQ2;
                    Q1=0;Q2=0;k=0;
                    T_RCalV=new double[TotalN+1];
                    T_RCalQ=new double[TotalN+1];
                    T_RCalInQ=new double[TotalN+1];
                    T_RCalZ=new double[TotalN+1];
                    T_RCalGN=new int[TotalN+1];
                    T_RCalT=new int[TotalN+1];
                    T_RCalDnZ=new double[TotalN+1];
                    T_RCalPartZ=new double[TotalN+1];
                    T_RCalPartGN=new int[TotalN+1];
                    T_RCalAGN=new int[TotalN+1];

    		}else{
                    Q1=this.Upper_Q(CountT,UpN,UpTS,UpQ);
                    Q2=this.Upper_Q(CountT+H_dt,UpN,UpTS,UpQ);

                    //************시단말 저수지계산용적과 계산물높이를 계산한다.*********************************
                    CalV=BCalV+(Q1+Q2)*0.0001*H_dt*0.5-(OutQ1+OutQ2)*0.0001*H_dt*0.5;
                    C_CalZ=this.Interpol_Z(CalV);

                    //***********만일 시단말계산물높이가 수문턱높이보다 작다면 발전기사용수량으로 방출량을 제한하여 다시 조절계산을 진행하고
                    if(C_CalZ<=Z_Sill){
                        OutQ2=0;
                        OutQ1=OutQ2;
                        CalV=BCalV+(Q1+Q2)*0.0001*H_dt*0.5-(OutQ1+OutQ2)*0.0001*H_dt*0.5;
                        C_CalZ=this.Interpol_Z(CalV);
                    }
                    OutQ1=OutQ2; BCalV=CalV;
    		}
            //System.out.println("Name="+Name+"  k="+k+"  CalZ="+C_CalZ+"  OutQ="+OutQ2+"  InQ="+(Q1+Q2)*0.5);
            //if(k<=Time_SelN-1){
                k++;
                this.T_RCalGN[k]=this.FullOpenGN;
                //System.out.println("Name="+Name+" k="+k+" FullOpenGN="+this.T_RCalGN[k]+" CalZ="+C_CalZ+"OutQ="+OutQ2+"InQ="+(Q1+Q2)*0.5);
                this.T_RCalInQ[k]=(Q1+Q2)*0.5;
                this.T_RCalZ[k]=C_CalZ;
                this.T_RCalT[k]=(int)CountT;
                this.T_RCalQ[k]=OutQ2;
                this.T_RCalV[k]=CalV;
                this.T_RCalDnZ[k]=DnZ;
                CountT=CountT+H_dt;

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }    
    public double In_V;
    public int VN;
    public double V_V[],V_Z[];
    public void Input_VZData_From_NotePad(String FileName){
        try{
            BufferedReader reader = new BufferedReader
                                   (new FileReader(new File(FileName)));
            String T_1 = reader.readLine();
            T_1=reader.readLine();
            VN=Integer.parseInt(T_1);
            V_V=new double[VN+1];
            V_Z=new double[VN+1];
            for(int i=1;i<=VN;i++){
                T_1=reader.readLine();
                StringTokenizer st = new StringTokenizer(T_1, "\t");
                V_Z[i]=Double.parseDouble(st.nextToken());
                V_V[i]=Double.parseDouble(st.nextToken());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }          
    public double Interpol_V(double CalZ) {
      //-----------------임의의 물높이에 대응하는 저수지용적보간함수처리부-----------------

     for (int i = 1; i <= VN - 1; i++) {
         if (CalZ >= V_Z[i] && CalZ < V_Z[i + 1]) {
             In_V=V_V[i]+(V_V[i+1]-V_V[i])*(CalZ-V_Z[i])/(V_Z[i+1]-V_Z[i]);
             break;
         }
     }
     if(V_Z[VN]>V_Z[VN-1]){
         if (CalZ > V_Z[VN]) {
             In_V = V_V[VN];
         }
     }
     return In_V;
    }
    double In_Z;
    public double Interpol_Z(double CalV) {
        //-----------------임의의 저수지용적에 대응하는 저수지물높이보간함수처리부-----------------
        for (int i = 1; i <= VN - 1; i++) {
            if (CalV >= V_V[i] && CalV < V_V[i + 1]) {
                In_Z=V_Z[i]+(V_Z[i+1]-V_Z[i])*(CalV-V_V[i])/(V_V[i+1]-V_V[i]);
                break;
            }
        }
        if(V_V[VN]>V_V[VN-1]){
            if (CalV > V_V[VN]) {
                In_Z = V_Z[VN];
            }
        }
        return In_Z;
    }
    double Upp_Q;
    public double Upper_Q(long TT,int UpN,long T_in[], double UpQQ[]) {
        //------------------임의의 시간에 해당하는 상류흐름량 계산처리부 -------------
        for (int i = 1; i <= UpN - 1; i++) {
            if (TT >= T_in[i] && TT < T_in[i + 1]) {
                Upp_Q = UpQQ[i] +
                        (UpQQ[i + 1] - UpQQ[i]) * (TT - T_in[i]) /
                        (T_in[i + 1] - T_in[i]);
                break;
            } else if (TT >= T_in[UpN-1]) {
                Upp_Q = UpQQ[UpN-1];
                break;
            }
        }
        return Upp_Q;
        //검사완료
     }       
    //****************일반저수지수문에서의 방출량공식**********************
     double MainGateQ;
     double Mue=0.94;
     double Gate_H, Gate_W;
     public double MainGateQ(double CalZ){
         if(CalZ>=(Z_Sill+Gate_H)){
             MainGateQ = Mue * Gate_W *Gate_H* Math.sqrt(2 * 9.8*(CalZ-Z_Sill-Gate_H*0.5));
         }else if(CalZ>=Z_Sill){
             MainGateQ = Mue * Gate_W * Math.sqrt(2 * 9.8) * Math.pow((CalZ-Z_Sill), 1.5) ;
         }else{
             MainGateQ=0;
         }
         if(CalZ<=DieZ){
             MainGateQ=0;
         }
         return MainGateQ;
    }    
    public double F_Opt_MaxGN;
    public double PSO_RMPSG_Q[];
    public double PSO_RMPSG_Z[];
    public double PSO_RMPSG_GN[];
    public double PSO_Q_Q[];
    public double PSO_Q_Z[];    
    public int Step_OptGN[][];
    public double Cal_MaxZ,Cal_MinZ,Cal_MaxQ,Cal_MinQ;
 
    public void Find_MaxZ_MaxQ_MinZ_MinQ(){
        Cal_MaxZ=T_RCalZ[1];Cal_MinZ=T_RCalZ[1];
        Cal_MaxQ=T_RCalQ[1];Cal_MinQ=T_RCalQ[1];
        for(int i=1;i<=UpN;i++){
            if(Cal_MaxZ<T_RCalZ[i]){
                Cal_MaxZ=T_RCalZ[i];
            }
            if(Cal_MinZ>T_RCalZ[i]){
                Cal_MinZ=T_RCalZ[i];
            }
            if(Cal_MaxQ<T_RCalQ[i]){
                Cal_MaxQ=T_RCalQ[i];
            }
            if(Cal_MinQ>T_RCalQ[i]){
                Cal_MinQ=T_RCalQ[i];
            }            
        }
  
    }
}
