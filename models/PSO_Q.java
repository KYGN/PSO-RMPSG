/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import main_interface.MFrame;

/**
 *
 * @author KIM YONG GUN 
 * 가우스립자무리최량화법에 의한 저수지큰물최량운영계산
 * 작성날자 2019.09
 */

public class PSO_Q {

    public int ResNo;
    public void SIMPSO_PLAN_Calculate_MaxQ_MinQ(){
        try{
            double CalZ=0;
            double NormalZ=MFrame.Reservoir[ResNo].NormalZ;
            /*if(MFrame.Reservoir[ResNo].CountT==0){
                CalZ=MFrame.Reservoir[ResNo].CalZ;
                F_Max_Q=(int)MFrame.Reservoir[ResNo].MainGateQ(CalZ);
                F_Max_Q=F_Max_Q*MFrame.Reservoir[ResNo].F_Opt_MaxGN;
                F_Min_Q=0;
            }else{
                CalZ=MFrame.Reservoir[ResNo].C_CalZ;
                F_Max_Q=(int)MFrame.Reservoir[ResNo].MainGateQ(CalZ);
                F_Max_Q=F_Max_Q*MFrame.Reservoir[ResNo].F_Opt_MaxGN;
                F_Min_Q=0;
            }*/
            CalZ=MFrame.Reservoir[ResNo].CalZ;
            long DifV=(long)(MFrame.Reservoir[ResNo].Interpol_V(NormalZ)
                            -MFrame.Reservoir[ResNo].Interpol_V(CalZ))*10000;
            long InV=0;
            for(int i=1;i<=MFrame.Reservoir[ResNo].UpN;i++){
                InV=InV+(long)MFrame.Reservoir[ResNo].UpQ[i]*3600;
            }
            long CtrlV=InV-DifV;
            F_Max_Q=(int)(CtrlV*2/(3600*MFrame.Reservoir[ResNo].UpN));
            F_Min_Q=0;
            System.out.println("F_Max_Q="+F_Max_Q);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    double Sqrt_PI;
    public double SIMPSO_PLAN_Evaluate_Fitness(int i){
            double Mean_Q=0;
            for(int j=1;j<=Psize;j++){
                Mean_Q=Mean_Q+Plan_Q[i][j]/Psize;
            }
            double Sum_Dif=0;
            for(int j=1;j<=Psize;j++){
                Sum_Dif=Sum_Dif+(Plan_Q[i][j]-Mean_Q)*(Plan_Q[i][j]-Mean_Q);
            }
            Sum_Dif=Sum_Dif/(Psize-1);
            Sqrt_PI=Math.sqrt(Sum_Dif);
            double Plan_max_Q=Plan_Q[i][1];
            for(int j=1;j<=Psize;j++){
                if(Plan_max_Q<Plan_Q[i][j]){
                    Plan_max_Q=Plan_Q[i][j];
                }
            }
            Sqrt_PI=Math.sqrt(Plan_max_Q);
            
            return Sqrt_PI;
    }
    public int F_Max_Q,F_Min_Q,Psize,Msize;
    public double Plan_Rnd[][],Plan_Q[][],Plan_Fit_Val[];
    public double Pbest_Rnd[][],Pbest_Q[][],Pbest_Fit_Val[];
    public double Gbest_Rnd[],Gbest_Q[],Gbest_Z[],Gbest_Fit_Val;
    public double Gbest_DnQ[],Natural_DnQ[];
    public double B_M_Gbest_Q;
    public double C_Z[];
    public double C_MaxQ[],C_MinQ[];
    public void SIMPSO_PLAN_First_Determine_pbest_gbest(){
        try{
            Psize=MFrame.Reservoir[1].Time_SelN;
            Msize=Psize;
            Pbest_Rnd=new double[Msize+1][Psize+1];
            Plan_Rnd=new double[Msize+1][Psize+1];
            Pbest_Q=new double[Msize+1][Psize+1];
            Plan_Q=new double[Msize+1][Psize+1];
            Plan_Fit_Val=new double[Msize+1];
            Pbest_Fit_Val=new double[Msize+1];
            F_v=new double[Msize+1][Psize+1];
            M_Pbest_Q=new double[Msize+1];
            C_Z=new double[Psize+1];
            C_MaxQ=new double[Psize+1];
            C_MinQ=new double[Psize+1];
            double Z_Dif=MFrame.Reservoir[ResNo].FloodZ-MFrame.Reservoir[ResNo].CalZ;
            for(int i=1;i<=Psize;i++){
                if(i==1){
                    C_Z[i]=MFrame.Reservoir[ResNo].CalZ;
                }else{
                    C_Z[i]=C_Z[1]+(i-1)*Z_Dif/(Psize);
                }
            }
            //this.PSO_CHAOTIC_Calculate_MaxQ_MinQ(ResNo);
            System.out.println("Result: PSO_CHAOTIC_First_Determine_pbest_gbest");

            for(int i=1;i<=Msize;i++) {
                for (int k = 1; k <= 2; k++) {
                    MFrame.Reservoir[k].CountT = 0;
                    MFrame.Reservoir[k].TotalN = Psize;
                    MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
                }      
 
                for(int j=1;j<=Psize;j++) {
                    double rnd = Math.random();
                    Pbest_Rnd[i][j]=rnd; Plan_Rnd[i][j]=rnd;
                    C_MaxQ[j]=MFrame.Reservoir[ResNo].MainGateQ(C_Z[j])*MaxGN;
                    //C_MaxQ[j]=MFrame.Reservoir[O_ResNo[ResNo]].MainGateQ(C_Z[j]);//*(MFrame.Reservoir[O_ResNo[ResNo]].F_Opt_MaxGN-1);
                    if(j==1){
                        C_MinQ[j]=0;
                    }else{
                        C_MinQ[j]=Plan_Q[i][j-1];
                    }
                    Plan_Q[i][j]=C_MinQ[j]+Plan_Rnd[i][j]*(C_MaxQ[j]-C_MinQ[j]);
                    Pbest_Q[i][j]=Plan_Q[i][j];
                    MFrame.Reservoir[ResNo].OutQ2=Plan_Q[i][j];
                    MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_DischargePlan();
//                    for(int jj=1;jj<=Cal_ResN;jj++){
//                        if(O_ResNo[jj]!=O_ResNo[ResNo]){
//                            MFrame.Reservoir[O_ResNo[jj]].OutQ2=MFrame.Reservoir[O_ResNo[jj]].VAR_SIMPSO_PLAN_Q[j];
//                        }
//                        MFrame.Reservoir[O_ResNo[jj]].FloodControl_For_Reservoir_By_DischargePlan();
//                    }
                    if(MFrame.Reservoir[ResNo].C_CalZ>MFrame.Reservoir[ResNo].FloodZ){
                        //Plan_Q[i][j]=Plan_Q[i][j]+20;
                        this.SIMPSO_PLAN_Repeate_Simulation_for_Reservoir(i,j,20);
                    }
                   if(MFrame.Reservoir[ResNo].C_CalZ<MFrame.Reservoir[ResNo].NormalZ-5){
                        //Plan_Q[i][j]=Plan_Q[i][j]+20;
                       this.SIMPSO_PLAN_Repeate_Simulation_for_Reservoir(i,j,-20);
                    }                     
                    //this.PSO_CHAOTIC_Calculate_MaxQ_MinQ(ResNo);
                }              
               
                for(int j=1;j<=Psize;j++){
                    Plan_Q[i][j]=MFrame.Reservoir[ResNo].T_RCalQ[j];
                    //Plan_Rnd[i][j]=(Plan_Q[i][j]-F_Min_Q)/(F_Max_Q-F_Min_Q);
                    Plan_Rnd[i][j]=(Plan_Q[i][j]-C_MinQ[j])/(C_MaxQ[j]-C_MinQ[j]);
                    if(MFrame.Reservoir[ResNo].T_RCalQ[j]==0){
                        Plan_Rnd[i][j]=0;
                        Plan_Q[i][j]=0;
                    }
                    Pbest_Q=Plan_Q;
                    Pbest_Rnd=Plan_Rnd;
                }                

                this.SIMPSO_PLAN_Calculate_DownQ();
                M_Pbest_Q[i]=Down_MaxQ;
                Plan_Fit_Val[i]=this.SIMPSO_PLAN_Evaluate_Fitness(i);
                Pbest_Fit_Val[i]=Plan_Fit_Val[i];
                //System.out.println("M="+i+", PbestQ="+F_pbest_Q[i]);
            }
            this.Gbest_Rnd=new double[Psize+1];
            this.Gbest_Q=new double[Psize+1];
            Gbest_Z=new double[Psize+1];
            Gbest_DnQ=new double[Psize+1];
            Natural_DnQ=new double[Psize+1];
            Gbest_Fit_Val=Plan_Fit_Val[1];
            M_Gbest_Q=M_Pbest_Q[1];
            Gbest_Q=Pbest_Q[1];

            for(int i=1;i<=Msize;i++){
                /*if(Gbest_Fit_Val>Plan_Fit_Val[i] && M_Gbest_Q>M_Pbest_Q[i]){
                    Gbest_Fit_Val=Plan_Fit_Val[i];
                    Gbest_Rnd=Pbest_Rnd[i];
                    Gbest_Q=Pbest_Q[i];
                    M_Gbest_Q=M_Pbest_Q[i];
                }*/
                if( M_Gbest_Q>M_Pbest_Q[i]){
                    Gbest_Rnd=Pbest_Rnd[i];
                    Gbest_Q=Pbest_Q[i];
                    M_Gbest_Q=M_Pbest_Q[i];
                }
            }
            //B_Gbest_Fit_Val=Gbest_Fit_Val;
            B_M_Gbest_Q=M_Gbest_Q;
            /*for(int i=1;i<=Psize;i++){
                System.out.println("i="+i+", Gbest_Q="+Gbest_Q[i]);
            }*/
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    double C_1=1.0,C_2=1.0,W=0.8;
    double F_v[][];
    double B_Gbest_Fit_Val;
    int Gbest_N;
    double M_Pbest_Q[];
    double M_Gbest_Q;
    public void SIMPSO_PLAN_Evolution_Process_By_SimplePSO( int Msize, int Psize){
        try{
            for(int i=1;i<=Msize;i++){
                for(int j=1;j<=Psize;j++){
                    W=0.8;
                    F_v[i][j]=W*F_v[i][j]+C_1*Math.random()*(Pbest_Rnd[i][j]-Plan_Rnd[i][j])+
                                          C_2*Math.random()*(Gbest_Rnd[i]-Plan_Rnd[i][j]);
                    Plan_Rnd[i][j]=Plan_Rnd[i][j]+F_v[i][j];	
                    if(Plan_Rnd[i][j]<0 || Plan_Rnd[i][j]>1){
                        Plan_Rnd[i][j]=Math.random();
                    }                    
                }
                for (int k = 1; k <= 2; k++) {
                    MFrame.Reservoir[k].CountT = 0;
                    MFrame.Reservoir[k].TotalN = Psize;
                    MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
                }
                //this.PSO_CHAOTIC_Calculate_MaxQ_MinQ(ResNo);
                for(int j=1;j<=Psize;j++) {
                    //Plan_Q[i][j]=F_Min_Q+Plan_Rnd[i][j]*(F_Max_Q-F_Min_Q);
                    if(j==1){
                        C_MinQ[j]=0;
                    }else{
                        C_MinQ[j]=Plan_Q[i][j-1];
                    }
                    Plan_Q[i][j]=C_MinQ[j]+Plan_Rnd[i][j]*(C_MaxQ[j]-C_MinQ[j]);
                    
                    MFrame.Reservoir[ResNo].OutQ2=Plan_Q[i][j];
                    MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_DischargePlan();
//                    for(int jj=1;jj<=Cal_ResN;jj++){
//                        if(O_ResNo[jj]!=O_ResNo[ResNo]){
//                            MFrame.Reservoir[O_ResNo[jj]].OutQ2=MFrame.Reservoir[O_ResNo[jj]].VAR_SIMPSO_PLAN_Q[j];
//                        }
//                        MFrame.Reservoir[O_ResNo[jj]].FloodControl_For_Reservoir_By_DischargePlan();
//                    }
                   if(MFrame.Reservoir[ResNo].C_CalZ>MFrame.Reservoir[ResNo].FloodZ){
                        //Plan_Q[i][j]=Plan_Q[i][j]+20;
                        this.SIMPSO_PLAN_Repeate_Simulation_for_Reservoir(i,j,20);
                    }
                   /*if(MFrame.Reservoir[O_ResNo[ResNo]].C_CalZ<MFrame.Reservoir[O_ResNo[ResNo]].NormalZ-5){
                        //Plan_Q[i][j]=Plan_Q[i][j]+20;
                       this.PSO_CHAOTIC_Repeate_Simulation_for_Reservoir(i,j, ResNo,-20);
                    }        */           
                    //this.PSO_CHAOTIC_Calculate_MaxQ_MinQ(ResNo);
                }              
               
                for(int j=1;j<=Psize;j++){
                    Plan_Q[i][j]=MFrame.Reservoir[ResNo].T_RCalQ[j];
                    Plan_Rnd[i][j]=(Plan_Q[i][j]-C_MinQ[j])/(C_MaxQ[j]-C_MinQ[j]);
                    //Plan_Rnd[i][j]=(Plan_Q[i][j]-F_Min_Q)/(F_Max_Q-F_Min_Q);
                    if(MFrame.Reservoir[ResNo].T_RCalQ[j]==0){
                        Plan_Rnd[i][j]=0;
                        Plan_Q[i][j]=0;
                    }
                }
                
                Plan_Fit_Val[i]=this.SIMPSO_PLAN_Evaluate_Fitness(i);
                this.SIMPSO_PLAN_Calculate_DownQ();
                /*if(M_Pbest_Q[i]>this.Down_MaxQ && Plan_Fit_Val[i]<Pbest_Fit_Val[i]){
                    Pbest_Fit_Val[i]=Plan_Fit_Val[i];
                    M_Pbest_Q[i]=Down_MaxQ;
                    for(int j=1;j<=Psize;j++){
                        Pbest_Rnd[i][j]=Plan_Rnd[i][j];
                        Pbest_Q[i][j]=Plan_Q[i][j];
                    }                    
                }*/
                if(M_Pbest_Q[i]>this.Down_MaxQ){
                    M_Pbest_Q[i]=Down_MaxQ;
                    for(int j=1;j<=Psize;j++){
                        Pbest_Rnd[i][j]=Plan_Rnd[i][j];
                        Pbest_Q[i][j]=Plan_Q[i][j];
                    }                    
                }                
            }
            
            /*for(int i=1;i<=Msize;i++){
                if(this.Gbest_Fit_Val>this.Pbest_Fit_Val[i] && M_Gbest_Q>M_Pbest_Q[i]){
                    this.Gbest_Fit_Val=this.Pbest_Fit_Val[i];
                    for(int j=1;j<=Psize;j++){
                        this.Gbest_Rnd[j]=Pbest_Rnd[i][j];
                        this.Gbest_Q[j]=Pbest_Q[i][j];
                    }
                    M_Gbest_Q=M_Pbest_Q[i];
                }
            }*/
            for(int i=1;i<=Msize;i++){
                if(M_Gbest_Q>M_Pbest_Q[i]){
                    for(int j=1;j<=Psize;j++){
                        this.Gbest_Rnd[j]=Pbest_Rnd[i][j];
                        this.Gbest_Q[j]=Pbest_Q[i][j];
                    }
                    M_Gbest_Q=M_Pbest_Q[i];
                }
            }
            //*****************************************************************************
            for (int k = 1; k <= 2; k++) {
                 MFrame.Reservoir[k].CountT = 0;
                 MFrame.Reservoir[k].TotalN = Psize;
                 MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
             }
             for(int j=1;j<=Psize;j++) {
                 MFrame.Reservoir[ResNo].OutQ2=Gbest_Q[j];
                 MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_DischargePlan();
//                 for(int jj=1;jj<Cal_ResN;jj++){
//                    if(O_ResNo[jj]!=O_ResNo[ResNo]){
//                        MFrame.Reservoir[O_ResNo[jj]].OutQ2=MFrame.Reservoir[O_ResNo[jj]].VAR_SIMPSO_PLAN_Q[j];
//                    }
//                     MFrame.Reservoir[O_ResNo[jj]].FloodControl_For_Reservoir_By_DischargePlan();
//                 }
                 
             } 
 
            /*if(MFrame.Reservoir[O_ResNo[ResNo]].Sort_Max_Z<MFrame.Reservoir[ResNo].FloodZ-0.1){
                     for(int j=1;j<=Psize;j++) {
                         Gbest_Q[j]=Gbest_Q[j]-5;
                         Gbest_Rnd[j]=(Gbest_Q[j]-F_Min_Q)/(F_Max_Q-F_Min_Q);
                     } 
             }*/
            //**************************************************************************
            /*if(B_Gbest_Fit_Val==Gbest_Fit_Val) {
                    Gbest_N++;
            }else if(B_Gbest_Fit_Val>Gbest_Fit_Val) {
                    B_Gbest_Fit_Val=Gbest_Fit_Val;Gbest_N=1;
            }*/
            if(B_M_Gbest_Q==M_Gbest_Q) {
                    Gbest_N++;
            }else if(B_M_Gbest_Q>M_Gbest_Q) {
                    B_M_Gbest_Q=M_Gbest_Q;Gbest_N=1;
                    //System.out.println("M_Gbest_Q="+M_Gbest_Q);
            }            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void SIMPSO_PLAN_Repeate_Simulation_for_Reservoir(int II,int TN, double DifQ) {
        for (int k = 1; k <= 2; k++) {
             MFrame.Reservoir[k].CountT = 0;
             MFrame.Reservoir[k].TotalN = Psize;
             MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
         }
        for (int k= 1; k <= TN; k++) {
                MFrame.Reservoir[ResNo].OutQ2=Plan_Q[II][k];
                MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_DischargePlan();
//                for(int jj=1;jj<=Cal_ResN;jj++){
//                    if(O_ResNo[jj]!=O_ResNo[ResNo]){
//                        MFrame.Reservoir[O_ResNo[jj]].OutQ2=MFrame.Reservoir[O_ResNo[jj]].VAR_SIMPSO_PLAN_Q[k];
//                    }
//                    MFrame.Reservoir[O_ResNo[jj]].FloodControl_For_Reservoir_By_DischargePlan();
//                }
        }
        if(MFrame.Reservoir[ResNo].C_CalZ>MFrame.Reservoir[ResNo].FloodZ){
            if(Plan_Q[II][TN]<C_MaxQ[TN]*(MFrame.Reservoir[ResNo].F_Opt_MaxGN-1) && Plan_Q[II][TN]>DifQ){
                Plan_Q[II][TN]=Plan_Q[II][TN]+DifQ;
               //System.out.println("Plan_Q="+Plan_Q[II][TN]+", CalZ="+MFrame.Reservoir[O_ResNo[ResNo]].C_CalZ);
               this.SIMPSO_PLAN_Repeate_Simulation_for_Reservoir(II, TN, DifQ);               
            }

        }
    }
    public int Opt_Max=100;
    public int C_Val=0,CC_Val=0;
    public int Opt_RN=10, RN=0;
    public double MaxGN=1.0;
    public int modelN;
    public void SIMPSO_PLAN_Optimal_FloodControl_For_OneReservoir_By_SimplePSO(){
        try{
            modelN=0;
            Msize=Psize;
            this.SIMPSO_PLAN_First_Determine_pbest_gbest();
            this.Gbest_N=0;
            //this.Opt_Max=100;
            C_Val=0;
            //MFrame.Sim_UI.progBar.progBar.setMaximum(Opt_Max);
            while(Gbest_N<=Opt_Max-1){
                this.SIMPSO_PLAN_Evolution_Process_By_SimplePSO(Msize, Psize);
                if(Gbest_N>C_Val){
                    C_Val=Gbest_N;
                    MFrame.Sim_UI.progBar.progBar.setValue(Opt_RN+C_Val);
                }
         
                //System.out.println("M_Gbest_Q=0"+M_Gbest_Q);
                //System.out.println("Gbest_Fit_Val="+this.Gbest_Fit_Val+" Gbest_N="+Gbest_N);
            }
            for (int k = 1; k <= 2; k++) {
                 MFrame.Reservoir[k].CountT = 0;
                 MFrame.Reservoir[k].TotalN = Psize;
                 MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
             }            
            for(int j=1;j<=Psize;j++) {
                MFrame.Reservoir[ResNo].OutQ2=Gbest_Q[j];
                MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_DischargePlan();
//                for(int jj=1;jj<=Cal_ResN;jj++){
//                    if(O_ResNo[jj]!=O_ResNo[ResNo]){
//                        MFrame.Reservoir[O_ResNo[jj]].OutQ2=MFrame.Reservoir[O_ResNo[jj]].VAR_SIMPSO_PLAN_Q[j];
//                    }
//                    MFrame.Reservoir[O_ResNo[jj]].FloodControl_For_Reservoir_By_DischargePlan();
//                }
            }              
            Gbest_Q=MFrame.Reservoir[ResNo].T_RCalQ;
            
            this.SIMPSO_PLAN_Backword_Controling_to_Gurantee_to_NormalZ((int)MFrame.Reservoir[ResNo].NormalZ);
            this.Gbest_Z=MFrame.Reservoir[ResNo].T_RCalZ;
            int NN=MFrame.Reservoir[ResNo].UpN;
            MFrame.Reservoir[ResNo].PSO_Q_Q=new double[NN+1];
            MFrame.Reservoir[ResNo].PSO_Q_Z=new double[NN+1];
            for(int i=1;i<=NN;i++){
                MFrame.Reservoir[ResNo].PSO_Q_Q[i]=Gbest_Q[i];            
                MFrame.Reservoir[ResNo].PSO_Q_Z[i]=Gbest_Z[i];                
            }
            this.SIMPSO_PLAN_Calculate_DownQ();
            this.SIMPSO_PLAN_Calculate_Down_Natural_InQ();

//            MFrame.Sim_UI.Save_Results_to_NotePad("SimPlanQ_Result1.txt",2);
//            MFrame.Sim_UI.Save_Results_to_NotePad("SimPlanQ_Result.txt",9);
            for(int i=1;i<=Msize;i++){
                //System.out.println("Gbest_Q="+MFrame.Reservoir[O_ResNo[ResNo]].G_CHAPSO_PlanQ[i]+" Gbest_Z="+MFrame.Reservoir[1].Sort_T_RCalZ[i]);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    
    /*double C_1=1.0,C_2=1.0;
    double W=0.8;
    public void PSO_CHAOTIC_Evolution_Process(int ResNo, int Msize, int Psize) {

            try {
            B_gbest_Q=(int)gbest_Q;
            pbest_RN=new int[Msize+1];
            for(int i=1;i<=Msize;i++) {

                    for(int k=1;k<=Psize/k_n;k++) {
                            for(int kk=(k-1)*k_n+1;kk<=k*k_n;kk++) {
                            //립자들의 이동속도를 계산한다.
                            //W=(W_max-W_min)*kk/(EndT[j]-FT[j]);
                            W=0.8;
                            F_v[i][k]=W*F_v[i][kk]+C_1*Math.random()*(F_pbest_Rnd[i][kk]-F_OpenGN_Rnd[i][kk])+
                                                                            C_2*Math.random()*(F_gbest_Rnd[kk]-F_OpenGN_Rnd[i][kk]);
                            F_OpenGN_Rnd[i][kk]=F_OpenGN_Rnd[i][kk]+F_v[i][kk];	   		
                                    if(F_OpenGN_Rnd[i][kk]<0) {
                                            F_OpenGN_Rnd[i][kk]=Math.random();
                                    }
                                    if(F_OpenGN_Rnd[i][kk]>1) {
                                            F_OpenGN_Rnd[i][kk]=Math.random();
                                    }	   	   					 
                            }
                    }

                    for(int k=1;k<=Psize;k++) {
                            F_OpenGN_Int[i][k]=(int)(MFrame.Reservoir[O_ResNo[ResNo]].F_Opt_MaxGN*F_OpenGN_Rnd[i][k]);
                            MFrame.Reservoir[O_ResNo[ResNo]].Time_GN[k]=F_OpenGN_Int[i][k];
                            MFrame.Reservoir[O_ResNo[ResNo]].Time_GT[k]=(k-1)*1*3600;
                    }
                            MFrame.Reservoir[O_ResNo[ResNo]].FullOpenGN = MFrame.Reservoir[O_ResNo[ResNo]].Time_GN[1];
                            MFrame.Reservoir[O_ResNo[ResNo]].W_GN = MFrame.Reservoir[O_ResNo[ResNo]].Time_GN[1];
                            MFrame.SimModule.Set_Change_GateOpenCloseCondition(O_ResNo[ResNo]);	   					

                    //진화된 립자들을 리용하여 다시 저수지체계의 류출조절계산을 진행한다.
                    int CN=(int)(MFrame.Reservoir[1].Time_SelN);
                    for (int k = 1; k <= 9; k++) {
                            MFrame.Reservoir[k].Cal_StopFlag = false;
                            MFrame.Reservoir[k].CountT = 0;
                            MFrame.Reservoir[k].TotalN = CN;
                            MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
                    }
                    for (int k= 1; k <= CN; k++) {
                            MFrame.Reservoir[O_ResNo[ResNo]].FullOpenGN=MFrame.Reservoir[O_ResNo[ResNo]].Time_GN[k];
                            MFrame.Reservoir[O_ResNo[ResNo]].FloodControl_For_Reservoir_By_One_Hour();
                            if(MFrame.Reservoir[O_ResNo[ResNo]].C_CalZ>MFrame.Reservoir[O_ResNo[ResNo]].FloodZ) {
                                    if(F_OpenGN_Int[i][k]<MFrame.Reservoir[O_ResNo[ResNo]].F_Opt_MaxGN) {
                                            F_OpenGN_Int[i][k]++;
                                            F_OpenGN_Rnd[i][k]=0.6;
                                            MFrame.Reservoir[O_ResNo[ResNo]].Time_GN[k]++;
                                            this.SIMPLE_Repeate_Simulation_for_Reservoir(k, O_ResNo[ResNo]);
                                    }
                            }	            		
                    }			            
                    //다음 하류보호지점에서의 설정된 방안에 따르는 예견방출과정선을 계산하고 최대방출량을 추정한다.
                    MFrame.Reservoir[O_ResNo[ResNo]].dt=3600;
                    MFrame.Reservoir[O_ResNo[ResNo]].Sort_CalData_By_dt_For_HydroReservoir(3600);
                    MFrame.Reservoir[O_ResNo[ResNo]].dt=60;

                    this.SIMPLE_Calculate_Down_MaxQ();

                    if(F_pbest_Q[i]>Down_MaxQ) {
                            F_pbest_Q[i]=Down_MaxQ;
                            //System.out.println("pbest_Q="+pbest_Q[i]);
                            for(int k=1;k<=Psize;k++) {
                                    F_pbest_Rnd[i][k]=F_OpenGN_Rnd[i][k];
                                    F_pbestGN_Int[i][k]=F_OpenGN_Int[i][k];
                            }		            	
                    }	   				
            }
           
            for(int i=1;i<=Msize;i++) {
                    pbest_RN[i]=0;
                    for(int k=1;k<=Psize;k++) {
                            if(F_pbestGN_Int[i][k]==0) {
                                    pbest_RN[i]++;
                            }
                    }

            }
            
            for(int i=1;i<=Msize;i++) {
                    if(gbest_Q>F_pbest_Q[i]){
                            gbest_Q=F_pbest_Q[i];
                            gbest_RN=pbest_RN[i];

                            for(int k=1;k<=PN;k++) {
                                    F_gbest_Rnd[k]=F_pbest_Rnd[i][k];
                                    F_gbestGN_Int[k]=F_pbestGN_Int[i][k];
                                    //System.out.println("opt_tt="+opt_tt+" gbest_Rnd="+gbest_Rnd[j][k]);
                            }

                    }
            }
            //System.out.println("gbest_Q="+gbest_Q);
           
            for(int i=1;i<=Msize;i++) {
                    if(gbest_Q==F_pbest_Q[i]) {
                            if(gbest_RN>pbest_RN[i]) {
                                    gbest_RN=pbest_RN[i];
                                    //System.out.println("gbest_Q="+gbest_Q+" gbest_RN="+gbest_RN);
                                    for(int k=1;k<=PN;k++) {
                                            F_gbest_Rnd[k]=F_pbest_Rnd[i][k];
                                            F_gbestGN_Int[k]=F_pbestGN_Int[i][k];
                                    }	   						
                            }
                    }
            }
            //this.Backword_Controling_Plan_of_FloodGates_to_Gurantee_NormalZ();
            if(B_gbest_Q==gbest_Q) {
                    B_gbest_N++;
            }else if(B_gbest_Q>gbest_Q) {
                    B_gbest_N=1;
            }
            
            }catch(Exception ex) {
                    ex.printStackTrace();
            }
    }    */
    public double[] C0= {0, 0.0112, 0.1128, 0.0782, 0.0088, 0.1194, 0.0015, 0.0048};
    public double[] C1 = {0, 0.209, 0.5032, 0.4838, 0.6432, 0.4356,0.301, 0.5935};
    public double[] C2 = {0, 0.7798, 0.3839, 0.438, 0.3479, 0.445, 0.6974,0.4017};
    public double D_SumQ[];	 
    public int Down_MaxQ;
    public void SIMPSO_PLAN_Calculate_DownQ() {
   		try {
                D_SumQ=new double[Psize+1];
                double TransQ[]=new double[Psize+1];
                if(MFrame.Reservoir[1].Opt_Flag==true){
                    for(int i=1;i<=Psize;i++){
                        D_SumQ[i]=MFrame.Reservoir[1].T_RCalQ[i];
                    } 
                    for(int i=2;i<=Psize;i++) {
                        TransQ[i]=C0[1]*D_SumQ[i]+C1[1]*D_SumQ[i-1]+C2[1]*TransQ[i-1];
                    }
                    for(int i=1;i<=Psize;i++) {
                        D_SumQ[i]=0;
                        D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Daegak_InQ[i];
                    }                            
                }else{
                    for(int i=1;i<=Psize;i++){
                        D_SumQ[i]=MFrame.Reservoir[1].UpQ[i];
                    } 
                    for(int i=2;i<=Psize;i++) {
                        TransQ[i]=C0[1]*D_SumQ[i]+C1[1]*D_SumQ[i-1]+C2[1]*TransQ[i-1];
                    }
                    for(int i=1;i<=Psize;i++) {
                        D_SumQ[i]=0;
                        D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Daegak_InQ[i];
                    }                    
                }
                //순천지점
                TransQ=new double[Psize+1];
                for(int i=2;i<=Psize;i++) {
                    TransQ[i]=C0[2]*D_SumQ[i]+C1[2]*D_SumQ[i-1]+C2[2]*TransQ[i-1];
                }
                for(int i=1;i<=Psize;i++) {
                    D_SumQ[i]=0;
                    D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Sunchon_InQ[i];
                    //System.out.println("SunChon D_SumQ="+D_SumQ[i]);
                }
                //성천지점
                TransQ=new double[Psize+1];
                for(int i=2;i<=Psize;i++) {
                    TransQ[i]=C0[3]*D_SumQ[i]+C1[3]*D_SumQ[i-1]+C2[3]*TransQ[i-1];
                }
                for(int i=1;i<=Psize;i++) {
                    D_SumQ[i]=0;
                    D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Songchon_InQ[i];
                    //System.out.println("SongChon D_SumQ="+D_SumQ[i]);
                }	  
                //봉화지점
                TransQ=new double[Psize+1];
                for(int i=2;i<=Psize;i++) {
                    TransQ[i]=C0[4]*D_SumQ[i]+C1[4]*D_SumQ[i-1]+C2[4]*TransQ[i-1];
                }
                for(int i=1;i<=Psize;i++) {
                    D_SumQ[i]=0;
                    D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Bonghua_InQ[i];
                    //System.out.println("BongHua D_SumQ="+D_SumQ[i]);
                }
                //미림지점
                TransQ=new double[Psize+1];
                for(int i=2;i<=Psize;i++) {
                    TransQ[i]=C0[5]*D_SumQ[i]+C1[5]*D_SumQ[i-1]+C2[5]*TransQ[i-1];
                }
                for(int i=1;i<=Psize;i++) {
                    D_SumQ[i]=0;
                    D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Mirim_InQ[i];
                    //System.out.println("Mirim D_SumQ="+D_SumQ[i]);
                }
                //남강
                if(MFrame.Reservoir[2].Opt_Flag==false) {
                        TransQ=new double[Psize+1];
                        for(int i=2;i<=Psize;i++) {
                            TransQ[i]=C0[6]*MFrame.Reservoir[2].UpQ[i]+
                                      C1[6]*MFrame.Reservoir[2].UpQ[i-1]+
                                      C2[6]*TransQ[i-1];
                        }
                        for(int i=1;i<=Psize;i++) {
                            D_SumQ[i]=D_SumQ[i]+TransQ[i];
                            //System.out.println((int)D_SumQ[i]);
                        }	   	            	
                }else {
                        TransQ=new double[Psize+1];
                        for(int i=2;i<=Psize;i++) {
                            TransQ[i]=C0[6]*MFrame.Reservoir[2].T_RCalQ[i]+
                                      C1[6]*MFrame.Reservoir[2].T_RCalQ[i-1]+
                                      C2[6]*TransQ[i-1];
                        }
                        for(int i=1;i<=Psize;i++) {
                            D_SumQ[i]=D_SumQ[i]+TransQ[i];
                        }	   	            	
                }
                Down_MaxQ=0;
                for (int i= 1; i <= Psize; i++) {
                    if(Down_MaxQ<D_SumQ[i]) {
                            Down_MaxQ=(int)D_SumQ[i];
                    }

                }
   		}catch(Exception ex) {
   			ex.printStackTrace();
   		}
    }        
	public double N_SumQ[];
   	public void SIMPSO_PLAN_Calculate_Down_Natural_InQ() {
            try {
              N_SumQ=new double[Psize+1];
                //녕원방출을 금성류입에 더하기
                for (int i = 1; i < Psize; i++) { 
                    N_SumQ[i] = MFrame.Reservoir[1].UpQ[i];
                }                      
                //대각지점
                double TransQ[]=new double[Psize+1];
                for(int i=2;i<=Psize;i++) {
                    TransQ[i]=C0[1]*N_SumQ[i]+C1[1]*N_SumQ[i-1]+C2[1]*TransQ[i-1];
                }
                for(int i=1;i<=Psize;i++) {
                    N_SumQ[i]=0;
                    N_SumQ[i]=TransQ[i]+MFrame.ConnectData.Daegak_InQ[i];
                    //System.out.println("DaeGak D_SumQ="+D_SumQ[i]);
                }
                //순천지점
                TransQ=new double[Psize+1];
                for(int i=2;i<=Psize;i++) {
                    TransQ[i]=C0[2]*N_SumQ[i]+C1[2]*N_SumQ[i-1]+C2[2]*TransQ[i-1];
                }
                for(int i=1;i<=Psize;i++) {
                    N_SumQ[i]=0;
                    N_SumQ[i]=TransQ[i]+MFrame.ConnectData.Sunchon_InQ[i];
                    //System.out.println("SunChon D_SumQ="+D_SumQ[i]);
                }
                //성천지점
                TransQ=new double[Psize+1];
                for(int i=2;i<=Psize;i++) {
                    TransQ[i]=C0[3]*N_SumQ[i]+C1[3]*N_SumQ[i-1]+C2[3]*TransQ[i-1];
                }
                for(int i=1;i<=Psize;i++) {
                    N_SumQ[i]=0;
                    N_SumQ[i]=TransQ[i]+MFrame.ConnectData.Songchon_InQ[i];
                    //System.out.println("SongChon D_SumQ="+D_SumQ[i]);
                }	  
                //봉화지점
                TransQ=new double[Psize+1];
                for(int i=2;i<=Psize;i++) {
                    TransQ[i]=C0[4]*N_SumQ[i]+C1[4]*N_SumQ[i-1]+C2[4]*TransQ[i-1];
                }
                for(int i=1;i<=Psize;i++) {
                    N_SumQ[i]=0;
                    N_SumQ[i]=TransQ[i]+MFrame.ConnectData.Bonghua_InQ[i];
                    //System.out.println("BongHua D_SumQ="+D_SumQ[i]);
                }
                //미림지점
                TransQ=new double[Psize+1];
                for(int i=2;i<=Psize;i++) {
                    TransQ[i]=C0[5]*N_SumQ[i]+C1[5]*N_SumQ[i-1]+C2[5]*TransQ[i-1];
                }
                for(int i=1;i<=Psize;i++) {
                    N_SumQ[i]=0;
                    N_SumQ[i]=TransQ[i]+MFrame.ConnectData.Mirim_InQ[i];
                    //System.out.println("Mirim D_SumQ="+D_SumQ[i]);
                }     
                //남강
                TransQ=new double[Psize+1];
                for(int i=2;i<=Psize;i++) {
                    TransQ[i]=C0[6]*MFrame.Reservoir[2].UpQ[i]+
                              C1[6]*MFrame.Reservoir[2].UpQ[i-1]+
                              C2[6]*TransQ[i-1];
                }
                for(int i=1;i<=Psize;i++) {
                    N_SumQ[i]=N_SumQ[i]+TransQ[i];
                    //System.out.println((int)D_SumQ[i]);
                }	   	            

            }catch(Exception ex) {
                    ex.printStackTrace();
            }
   		
   	}
    	int GN_repN[];
	double F_FinalZ;
	public void SIMPSO_PLAN_Backword_Controling_to_Gurantee_to_NormalZ(int F_FinalZ) {
		try {
                    int BackN=0;
                    while(MFrame.Reservoir[ResNo].C_CalZ<F_FinalZ){
                        BackN++;
                        for(int i=Psize-BackN;i<=Psize;i++){
                            Gbest_Q[i]=0;
                            Gbest_Rnd[i]=0;
                        }
                        for (int k = 1; k <= 2; k++) {
                            MFrame.Reservoir[k].CountT = 0;
                            MFrame.Reservoir[k].TotalN = Psize;
                            MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
                        }

                        for (int k = 1; k <= Psize; k++) {
                            MFrame.Reservoir[ResNo].OutQ2=Gbest_Q[k];
                            MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_DischargePlan();
                        }
                    } 
				
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
