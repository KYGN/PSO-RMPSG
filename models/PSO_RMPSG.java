package models;

import main_interface.MFrame;

public class PSO_RMPSG {

   	//****************************************************************************************************//
   	//************저수지에서 초기수문개방대수를 결정하기 위한 처리부, 개별적인 저수지로 류입되는 큰물과정에 대하여 최소한 몇대의 수문을 열어
   	//************놓아야 정상수위를 초과하지 않으면서 큰물을 통과시키겠는가를 결정한다.***************************************//
   	public int Cal_F_GN=0;
   	public int Cal_F_MaxGN=0;
   	public double Cal_F_MaxZ;
        
   	public void CHAPSO_GN_Check_First_MaxOpenGN_For_Reservoir() {

            try {
                    Cal_F_GN=0;
                    //MFrame.Reservoir[1].UpZ=297;
                    MFrame.Reservoir[1].CalZ=MFrame.Reservoir[1].UpZ;
                    MFrame.Reservoir[1].C_CalZ=MFrame.Reservoir[1].UpZ;

                    while(Cal_F_GN<=MFrame.Reservoir[ResNo].GateN) {
                        for(int i=1;i<=MFrame.Reservoir[ResNo].Time_SelN;i++) {
                            MFrame.Reservoir[ResNo].Time_GN[i]=Cal_F_GN;
                            MFrame.Reservoir[ResNo].Time_GT[i]=(i-1)*1*3600; 
                            //System.out.println("T="+i+"  GN="+Rnd_GN);
                        }
                        MFrame.Reservoir[ResNo].FullOpenGN = MFrame.Reservoir[ResNo].Time_GN[1];
                        MFrame.Reservoir[ResNo].W_GN = MFrame.Reservoir[ResNo].Time_GN[1];
                        int CN=(int)(MFrame.Reservoir[1].Time_SelN);
                        for (int i = 1; i <= 2; i++) {
                            MFrame.Reservoir[i].CountT = 0;
                            MFrame.Reservoir[i].TotalN = CN;
                            MFrame.Reservoir[i].FullOpenGN = MFrame.Reservoir[i].W_GN;
                        }
                        Cal_F_MaxZ=0;
                        for (int i = 1; i <= CN; i++) {
                            MFrame.Reservoir[ResNo].FullOpenGN=MFrame.Reservoir[ResNo].Time_GN[i];
                            MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_One_Hour();
                            if(Cal_F_MaxZ<MFrame.Reservoir[ResNo].T_RCalZ[i]) {
                                    Cal_F_MaxZ=MFrame.Reservoir[ResNo].T_RCalZ[i];
                            }
                        }
                        if(Cal_F_MaxZ<=MFrame.Reservoir[ResNo].NormalZ) {
                            break;
                        }
                        Cal_F_GN++;
                    }
                    Cal_F_MaxGN=Cal_F_GN+1;
                    MFrame.Reservoir[ResNo].Cal_MaxGN=Cal_F_MaxGN;
                    System.out.println("MaxGN="+Cal_F_MaxGN);    
                    MFrame.Reservoir[ResNo].F_Opt_MaxGN=Cal_F_MaxGN;

            }catch(Exception ex) {
                ex.printStackTrace();
            }
   	}
   	int F_pbest_Q[];
   	int F_OpenGN_Int[][];
   	double F_OpenGN_Rnd[][];
   	double F_pbest_Rnd[][];
   	int F_pbestGN_Int[][];
   	double F_v[][];
   	double F_gbest_Rnd[];
   	int F_gbestGN_Int[];
        int F_gbestGN_Q[];
   	int B_gbest_Q;
   	int pbest_RN[];
   	int gbest_RN;
   	public void CHAPSO_GN_First_Determine_pbest_gbest_of_Control_Plan_For_One_Reservoir
   											(int Msize,int Psize) {
                try {
                        //저수지체계최량조절계산에 참가하는 주요계산변수들을 구성한다.
                        F_pbest_Q=new int[Msize+1];
                        Psize=MFrame.Reservoir[1].Time_SelN;
                        F_OpenGN_Int=new int[Msize+1][Psize+1];
                        F_OpenGN_Rnd=new double[Msize+1][Psize+1];	 
                        F_pbest_Rnd=new double[Msize+1][Psize+1];
                        F_pbestGN_Int=new int[Msize+1][Psize+1];
                        F_v=new double[Msize+1][Psize+1];

                        for(int i=1;i<=Msize;i++) {
                        //주어진 류입량조건에서 최대한계개방대수를 초과하지 않는 시간에 따르는 우연개방대수들로 이루어진 행벡토르를 위치로 하는 립자들을 생성시킨다.

                                for(int k=1;k<=Psize;k++) {
                                        F_OpenGN_Rnd[i][k]=0;
                                        F_OpenGN_Int[i][k]=0;
                                        MFrame.Reservoir[ResNo].Time_GN[k]=F_OpenGN_Int[i][k]=0;
                                        MFrame.Reservoir[ResNo].Time_GT[k]=(k-1)*1*3600;	   						
                                }
                                for(int k=1;k<=Psize/k_n;k++) {
                                        double rnd = Math.random();
                                        for(int kk=(k-1)*k_n+1;kk<=k*k_n;kk++) {
                                                F_v[i][k]=rnd;
                                                F_OpenGN_Rnd[i][kk]=rnd;
                                                F_OpenGN_Int[i][kk]=(int)(rnd*MFrame.Reservoir[ResNo].F_Opt_MaxGN);
                                                MFrame.Reservoir[ResNo].Time_GN[kk]=F_OpenGN_Int[i][kk];
                                                MFrame.Reservoir[ResNo].Time_GT[kk]=(kk-1)*1*3600; 			   					
                                        }
                                }
                                MFrame.Reservoir[ResNo].FullOpenGN = MFrame.Reservoir[ResNo].Time_GN[1];
                                MFrame.Reservoir[ResNo].W_GN = MFrame.Reservoir[ResNo].Time_GN[1];
                                //주어진 우연수문개방대수에 의한 방안으로 저수지계큰물조절계산을 진행한다.
                                int CN=(int)(MFrame.Reservoir[1].Time_SelN);
                                for (int k = 1; k <= 2; k++) {
                                        MFrame.Reservoir[k].CountT = 0;
                                        MFrame.Reservoir[k].TotalN = CN;
                                        MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
                                }	
                                for (int k= 1; k <= CN; k++) {
                                        for(int j=1;j<=MFrame.SIM_MDL.Cal_ResN;j++) {
                                            int RN=MFrame.SIM_MDL.O_ResNo[j];
                                                MFrame.Reservoir[RN].FullOpenGN=MFrame.Reservoir[RN].Time_GN[k];
                                                MFrame.Reservoir[RN].FloodControl_For_Reservoir_By_One_Hour();
                                        }
                                }
                                this.CHAPSO_GN_Calculate_Down_MaxQ();
                                F_pbest_Q[i]=Down_MaxQ;
                                //System.out.println("pbest_Q="+pbest_Q[i]+" pbest_W="+pbest_W[i]);
                                //초기에 설정된 최대큰물방출량보다 작은 경우에 립자의 위치가 갱신된다.

                                for(int k=1;k<=Psize;k++) {
                                        F_pbest_Rnd[i][k]=F_OpenGN_Rnd[i][k];
                                        F_pbestGN_Int[i][k]=F_OpenGN_Int[i][k];
                                }		            	
                        }
                        int Mid_Q,MidGN_Int=0;
                        double Mid_rnd;
                        for(int i=1;i<=Msize;i++) {
                                for(int j=i+1;j<=Msize;j++) {
                                        if(F_pbest_Q[i]>F_pbest_Q[j]) {
                                                Mid_Q=F_pbest_Q[i];
                                                F_pbest_Q[i]=F_pbest_Q[j];
                                                F_pbest_Q[j]=Mid_Q;
                                                for(int k1=1;k1<=Psize;k1++) {
                                                        Mid_rnd=F_pbest_Rnd[i][k1];
                                                        MidGN_Int=F_pbestGN_Int[i][k1];
                                                        F_pbest_Rnd[i][k1]=F_pbest_Rnd[j][k1];
                                                        F_pbestGN_Int[i][k1]=F_pbestGN_Int[j][k1];
                                                        F_pbest_Rnd[j][k1]=Mid_rnd;
                                                        F_pbestGN_Int[j][k1]=MidGN_Int;
                                                }
                                        }
                                }
                        }

                        for(int j=1;j<=PN;j++) {
                                F_gbest_Rnd[j]=F_pbest_Rnd[1][j];
                                F_gbestGN_Int[j]=F_pbestGN_Int[1][j];
                        }

                        this.gbest_RN=100000;
                        //System.out.println("gbest_Q="+gbest_Q);
                }catch(Exception ex) {
                ex.printStackTrace();
                }
   	}	  
   	double C_1=1.0,C_2=1.0;
   	double W=0.8;
        long Gbest_OutV=0;
   	public void CHAPSO_GN_New_Evolution_Process_of_Discharge_Simulation_For_One_Reservoirs
                                                                                (int Msize, int Psize) {

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
				F_OpenGN_Int[i][k]=(int)(MFrame.Reservoir[ResNo].F_Opt_MaxGN*F_OpenGN_Rnd[i][k]);
				MFrame.Reservoir[ResNo].Time_GN[k]=F_OpenGN_Int[i][k];
				MFrame.Reservoir[ResNo].Time_GT[k]=(k-1)*1*3600;
			}
				MFrame.Reservoir[ResNo].FullOpenGN = MFrame.Reservoir[ResNo].Time_GN[1];
				MFrame.Reservoir[ResNo].W_GN = MFrame.Reservoir[ResNo].Time_GN[1];
			//진화된 립자들을 리용하여 다시 저수지체계의 류출조절계산을 진행한다.
			int CN=(int)(MFrame.Reservoir[1].Time_SelN);
			for (int k = 1; k <= 2; k++) {
				MFrame.Reservoir[k].CountT = 0;
				MFrame.Reservoir[k].TotalN = CN;
				MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
			}
			for (int k= 1; k <= CN; k++) {
				MFrame.Reservoir[ResNo].FullOpenGN=MFrame.Reservoir[ResNo].Time_GN[k];
				MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_One_Hour();
				if(MFrame.Reservoir[ResNo].C_CalZ>MFrame.Reservoir[ResNo].FloodZ) {
					if(F_OpenGN_Int[i][k]<MFrame.Reservoir[ResNo].F_Opt_MaxGN) {
						F_OpenGN_Int[i][k]++;
						F_OpenGN_Rnd[i][k]=0.6;
						MFrame.Reservoir[ResNo].Time_GN[k]++;
						this.CHAPSO_GN_Repeate_Simulation_for_Reservoir(k);
					}
				}	            		
			}			            
			//다음 하류보호지점에서의 설정된 방안에 따르는 예견방출과정선을 계산하고 최대방출량을 추정한다.
	
			this.CHAPSO_GN_Calculate_Down_MaxQ();
			
			if(F_pbest_Q[i]>Down_MaxQ) {
				F_pbest_Q[i]=Down_MaxQ;
				//System.out.println("pbest_Q="+pbest_Q[i]);
				for(int k=1;k<=Psize;k++) {
					F_pbest_Rnd[i][k]=F_OpenGN_Rnd[i][k];
					F_pbestGN_Int[i][k]=F_OpenGN_Int[i][k];
				}		            	
			}	   				
		}
		//**********************************************************************************************//
		for(int i=1;i<=Msize;i++) {
			pbest_RN[i]=0;
			for(int k=1;k<=Psize;k++) {
				if(F_pbestGN_Int[i][k]==0) {
					pbest_RN[i]++;
				}
			}

		}
		//*********************************************************************************************//
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
		//************************************************************************************//
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
                //*******************************카오스부분**********************************//
                double CHA_GN_Rnd[]=new double[PN+1];
                int CHA_GN_Int[]=new int[PN+1];
                double CHA_x[]=new double[PN+1];
                double CHA_rx[]=new double[PN+1];
                double a1=-4,b1=4,beta=0.5;

                for(int t=1;t<=100;t++){
                    CHA_GN_Rnd=F_gbest_Rnd;
                    if(t==1){
                        for(int i=1;i<=PN;i++){
                            CHA_x[i]=Math.random();
                            if(CHA_x[i]==0){
                                CHA_x[i]=0.1;
                            }
                            if(CHA_x[i]==0.25){
                                CHA_x[i]=0.26;
                            }
                            if(CHA_x[i]==0.5){
                                CHA_x[i]=0.51;
                            }
                            if(CHA_x[i]==0.75){
                                CHA_x[i]=0.76;
                            } 
                            if(CHA_x[i]==1){
                                CHA_x[i]=0.9;
                            }  
                        } 
                    }
                    for(int i=1;i<=PN;i++){
                        CHA_rx[i]=a1+(b1-a1)*CHA_x[i];
                        CHA_GN_Rnd[i]=CHA_GN_Rnd[i]+beta*CHA_rx[i]; 
                        if(CHA_GN_Rnd[i]>1.0 || CHA_GN_Rnd[i]<0 ){
                            CHA_GN_Rnd[i]=Math.random();
                        }
                    }

                    for(int k=1;k<=Psize;k++) {
                            CHA_GN_Int[k]=(int)(MFrame.Reservoir[ResNo].F_Opt_MaxGN*CHA_GN_Rnd[k]);
                            MFrame.Reservoir[ResNo].Time_GN[k]=CHA_GN_Int[k];
                            MFrame.Reservoir[ResNo].Time_GT[k]=(k-1)*1*3600;
                    }
                            MFrame.Reservoir[ResNo].FullOpenGN = MFrame.Reservoir[ResNo].Time_GN[1];
                            MFrame.Reservoir[ResNo].W_GN = MFrame.Reservoir[ResNo].Time_GN[1];
                    int CN=(int)(MFrame.Reservoir[1].Time_SelN);
                    for (int k = 1; k <= 2; k++) {
                            MFrame.Reservoir[k].CountT = 0;
                            MFrame.Reservoir[k].TotalN = CN;
                            MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
                    }
                    for (int k= 1; k <= CN; k++) {
                            MFrame.Reservoir[ResNo].FullOpenGN=MFrame.Reservoir[ResNo].Time_GN[k];
                            MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_One_Hour();
                            if(MFrame.Reservoir[ResNo].C_CalZ>MFrame.Reservoir[ResNo].FloodZ) {
                                    if(CHA_GN_Int[k]<MFrame.Reservoir[ResNo].F_Opt_MaxGN) {
                                            CHA_GN_Int[k]++;
                                            CHA_GN_Rnd[k]=0.6;
                                            MFrame.Reservoir[ResNo].Time_GN[k]++;
                                            this.CHAPSO_GN_Repeate_Simulation_for_Reservoir(k);
                                    }
                            }	            		
                    }		            
                    //다음 하류보호지점에서의 설정된 방안에 따르는 예견방출과정선을 계산하고 최대방출량을 추정한다.
                    this.CHAPSO_GN_Calculate_Down_MaxQ();
                    
                    int CHA_RN=0;
                    for(int i=1;i<=PN;i++){
                         if(CHA_GN_Int[i]==0){
                             CHA_RN++;
                         }
                    }
                    if(this.Down_MaxQ<gbest_Q && CHA_RN<gbest_RN){
                        gbest_Q=this.Down_MaxQ;
                        gbest_RN=CHA_RN;
                        this.F_gbestGN_Int=CHA_GN_Int;
                        this.F_gbest_Rnd=CHA_GN_Rnd;
                    }

                    for(int i=1;i<PN;i++){
                        CHA_x[i]=4*CHA_x[i]*(1-CHA_x[i]);
                    }
   
                }
		//this.Backword_Controling_Plan_of_FloodGates_to_Gurantee_NormalZ();
		if(B_gbest_Q==gbest_Q) {
			B_gbest_N++;
		}else if(B_gbest_Q>gbest_Q) {
			B_gbest_N=1; B_gbest_Q=gbest_Q;
		}
		//*************************************************************************************//
		}catch(Exception ex) {
			ex.printStackTrace();
		}
   	}
   	//*********************************************************************************************************//
   	//*****************조절말기에 저수지의 상류수위를 맞추기 위하여 수문개방대수를 조절한다.***************************************//
   	//*********************************************************************************************************//
	int GN_repN[];
	double F_FinalZ;
	public void CHAPSO_GN_Backword_Controling_to_Gurantee_Downward_to_NormalZ(int F_FinalZ) {
		try {
	        	int BackN=0;
                        while(MFrame.Reservoir[ResNo].T_RCalZ[MFrame.Reservoir[ResNo].UpN-1]>F_FinalZ){
                                //System.out.println(MFrame.Reservoir[O_ResNo[ResNo]].Name+" Z="+(int)MFrame.Reservoir[O_ResNo[ResNo]].Sort_T_RCalZ[MFrame.Reservoir[O_ResNo[i]].SortN]);
                                group mid_big_group=new group();
                                mid_big_group.S_No=BigGN_Group[BigGN_N].S_No;
                                mid_big_group.E_No=BigGN_Group[BigGN_N].E_No;
                                mid_big_group.E_No++;
                                mid_big_group.g_N=BigGN_Group[BigGN_N].g_N;
                                mid_big_group.g_N++;
                                mid_big_group.g_no=new int[mid_big_group.g_N+1];
                                for(int j=1;j<=BigGN_Group[BigGN_N].g_N;j++) {
                                        mid_big_group.g_no[ResNo]=BigGN_Group[BigGN_N].g_no[j];
                                }
                                mid_big_group.g_no[mid_big_group.g_N]=1;
                                BigGN_Group[BigGN_N]=new group();
                                BigGN_Group[BigGN_N]=mid_big_group;

                                        for(int j=1;j<=PN;j++) {
                                                F_gbestGN_Int[j]=0;
                                        }
                                        for(int j=1;j<=BigGN_N;j++) {
                                                int k=0;
                                                for(int jj=BigGN_Group[j].S_No;jj<=BigGN_Group[j].E_No;jj++) {
                                                        k++;
                                                        F_gbestGN_Int[BigGN_Group[j].S_No+k-1]=BigGN_Group[j].g_no[k];
                                                }
                                        }   	   	   				

                                for(int j=1;j<=PN;j++) {
                                        MFrame.Reservoir[ResNo].Time_GN[j]=F_gbestGN_Int[j];
                                        MFrame.Reservoir[ResNo].Time_GT[j]=(j-1)*1*3600; 	   		
                                }
                            MFrame.Reservoir[ResNo].FullOpenGN = MFrame.Reservoir[ResNo].Time_GN[1];
                            MFrame.Reservoir[ResNo].W_GN = MFrame.Reservoir[ResNo].Time_GN[1];
                            int CN=(int)(MFrame.Reservoir[1].Time_SelN);
                            for (int k = 1; k <= 2; k++) {
                                MFrame.Reservoir[k].CountT = 0;
                                MFrame.Reservoir[k].TotalN = CN;
                                MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
                            }

                            for (int k = 1; k <= CN; k++) {
                                MFrame.Reservoir[ResNo].FullOpenGN=MFrame.Reservoir[ResNo].Time_GN[k];
                                MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_One_Hour();
                            }
                        } 
				
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
   	int F_GN_repN=0;
   	public void CHAPSO_GN_Backword_Controling_to_Gurantee_NormalZ_For_One_Reservoir(int FinalZ) {
   		try {
	        	int BackN=0;
   	        	while(MFrame.Reservoir[ResNo].C_CalZ<FinalZ){
   	        		BackN++;
   	        		if(BackN==PN) {
   	        			break;
   	        		}
   	        		//System.out.println("CalZ="+MFrame.Reservoir[ResNo].C_CalZ);
   	        		//System.out.println("BackN="+BackN);
   	        		for(int j=PN-BackN;j<=PN;j++) {
   	        			if(F_gbestGN_Int[j]>0) {
   	        				F_gbestGN_Int[j]=0;
   	        			}else {
   	        				F_gbestGN_Int[j]=0;
   	        			}
   	        			//System.out.println("OpenGN_Int="+OpenGN_Int[gbest_Ind][i]);
   	        		}
   	        		
   	        		for(int j=1;j<=PN;j++) {
   	        			MFrame.Reservoir[ResNo].Time_GN[j]=F_gbestGN_Int[j];
   	        			MFrame.Reservoir[ResNo].Time_GT[j]=(j-1)*1*3600; 	   		
   	        		}
		            MFrame.Reservoir[ResNo].FullOpenGN = MFrame.Reservoir[ResNo].Time_GN[1];
		            MFrame.Reservoir[ResNo].W_GN = MFrame.Reservoir[ResNo].Time_GN[1];
		            int CN=(int)(MFrame.Reservoir[1].Time_SelN);
		            for (int k = 1; k <= 2; k++) {
		                MFrame.Reservoir[k].CountT = 0;
		                MFrame.Reservoir[k].TotalN = CN;
		                MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
		            }
                            for (int k= 1; k <= CN; k++) {
                                    for(int j=1;j<=MFrame.SIM_MDL.Cal_ResN;j++) {
                                        int RN=MFrame.SIM_MDL.O_ResNo[j];
                                            MFrame.Reservoir[RN].FullOpenGN=MFrame.Reservoir[RN].Time_GN[k];
                                            MFrame.Reservoir[RN].FloodControl_For_Reservoir_By_One_Hour();
                                    }
                            }	
                            for(int k=1;k<=2;k++){
                                MFrame.Reservoir[k].Find_MaxZ_MaxQ_MinZ_MinQ();
                            }
   	        	} 
   	        	
   	        	F_GN_repN=0;
   	        	for(int j=1;j<=PN;j++) {
   	        		if(F_gbestGN_Int[j]==0) {
   	        			F_GN_repN++;
   	        		}
   	        	}  
   	      		for(int j=1;j<=PN;j++) {
        			F_gbest_Rnd[j]=F_gbestGN_Int[j]/(MFrame.Reservoir[ResNo].F_Opt_MaxGN);
        		}


   		}catch(Exception ex) {
   			ex.printStackTrace();
   		}
   	}	   	
   	
   	//********************************************************************************************************//
   	//********수문개방대수가 변경되였을때 변경된 시점까지 처음부터 큰물조절계산을 다시 진행하는 처리부**********************************//
   	//********************************************************************************************************//
   	public void CHAPSO_GN_Repeate_Simulation_for_Reservoir(int TN) {
            MFrame.Reservoir[ResNo].FullOpenGN = MFrame.Reservoir[ResNo].Time_GN[1];
            MFrame.Reservoir[ResNo].W_GN = MFrame.Reservoir[ResNo].Time_GN[1];
            int CN=(int)(MFrame.Reservoir[ResNo].Time_SelN);
            for (int k = 1; k <= 2; k++) {
                MFrame.Reservoir[k].CountT = 0;
                MFrame.Reservoir[k].TotalN = CN;
                MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
            }
            for (int k= 1; k <= TN; k++) {
                    MFrame.Reservoir[ResNo].FullOpenGN=MFrame.Reservoir[ResNo].Time_GN[k];
                    MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_One_Hour();
            }		            	   		
   	}
   	//*****************************************************************************************************//
   	//**************하류보호지점에서의 큰물조절과정의 최대값을 결정하기 위한 계산처리부****************************************//
   	//*****************************************************************************************************//
   	public double D_SumQ[];	   	
   	
    public double[] C0= {0, 0.0112, 0.1128, 0.0782, 0.0088, 0.1194, 0.0015, 0.0048};
    public double[] C1 = {0, 0.209, 0.5032, 0.4838, 0.6432, 0.4356,0.301, 0.5935};
    public double[] C2 = {0, 0.7798, 0.3839, 0.438, 0.3479, 0.445, 0.6974,0.4017};
    public int Down_MaxQ;
    public int Down_OutV;
    public void CHAPSO_GN_Calculate_Down_MaxQ() {
            try {
                D_SumQ=new double[PN+1];
                double TransQ[]=new double[PN+1];
                if(MFrame.Reservoir[1].Opt_Flag==true){
                    for(int i=1;i<=PN;i++){
                        D_SumQ[i]=MFrame.Reservoir[1].T_RCalQ[i];
                    } 
                    for(int i=2;i<=PN;i++) {
                        TransQ[i]=C0[1]*D_SumQ[i]+C1[1]*D_SumQ[i-1]+C2[1]*TransQ[i-1];
                    }
                    for(int i=1;i<=PN;i++) {
                        D_SumQ[i]=0;
                        D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Daegak_InQ[i];
                    }                            
                }else{
                    for(int i=1;i<=PN;i++){
                        D_SumQ[i]=MFrame.Reservoir[1].UpQ[i];
                    } 
                    for(int i=2;i<=PN;i++) {
                        TransQ[i]=C0[1]*D_SumQ[i]+C1[1]*D_SumQ[i-1]+C2[1]*TransQ[i-1];
                    }
                    for(int i=1;i<=PN;i++) {
                        D_SumQ[i]=0;
                        D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Daegak_InQ[i];
                    }                    
                }
                //순천지점
                TransQ=new double[PN+1];
                for(int i=2;i<=PN;i++) {
                    TransQ[i]=C0[2]*D_SumQ[i]+C1[2]*D_SumQ[i-1]+C2[2]*TransQ[i-1];
                }
                for(int i=1;i<=PN;i++) {
                    D_SumQ[i]=0;
                    D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Sunchon_InQ[i];
                    //System.out.println("SunChon D_SumQ="+D_SumQ[i]);
                }
                //성천지점
                TransQ=new double[PN+1];
                for(int i=2;i<=PN;i++) {
                    TransQ[i]=C0[3]*D_SumQ[i]+C1[3]*D_SumQ[i-1]+C2[3]*TransQ[i-1];
                }
                for(int i=1;i<=PN;i++) {
                    D_SumQ[i]=0;
                    D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Songchon_InQ[i];
                    //System.out.println("SongChon D_SumQ="+D_SumQ[i]);
                }	  
                //봉화지점
                TransQ=new double[PN+1];
                for(int i=2;i<=PN;i++) {
                    TransQ[i]=C0[4]*D_SumQ[i]+C1[4]*D_SumQ[i-1]+C2[4]*TransQ[i-1];
                }
                for(int i=1;i<=PN;i++) {
                    D_SumQ[i]=0;
                    D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Bonghua_InQ[i];
                    //System.out.println("BongHua D_SumQ="+D_SumQ[i]);
                }
                //미림지점
                TransQ=new double[PN+1];
                for(int i=2;i<=PN;i++) {
                    TransQ[i]=C0[5]*D_SumQ[i]+C1[5]*D_SumQ[i-1]+C2[5]*TransQ[i-1];
                }
                for(int i=1;i<=PN;i++) {
                    D_SumQ[i]=0;
                    D_SumQ[i]=TransQ[i]+MFrame.ConnectData.Mirim_InQ[i];
                    //System.out.println("Mirim D_SumQ="+D_SumQ[i]);
                }
                //남강
                if(MFrame.Reservoir[2].Opt_Flag==false) {
                        TransQ=new double[PN+1];
                        for(int i=2;i<=PN;i++) {
                            TransQ[i]=C0[6]*MFrame.Reservoir[2].UpQ[i]+
                                      C1[6]*MFrame.Reservoir[2].UpQ[i-1]+
                                      C2[6]*TransQ[i-1];
                        }
                        for(int i=1;i<=PN;i++) {
                            D_SumQ[i]=D_SumQ[i]+TransQ[i];
                            //System.out.println((int)D_SumQ[i]);
                        }	   	            	
                }else {
                        TransQ=new double[PN+1];
                        for(int i=2;i<=PN;i++) {
                            TransQ[i]=C0[6]*MFrame.Reservoir[2].T_RCalQ[i]+
                                      C1[6]*MFrame.Reservoir[2].T_RCalQ[i-1]+
                                      C2[6]*TransQ[i-1];
                        }
                        for(int i=1;i<=PN;i++) {
                            D_SumQ[i]=D_SumQ[i]+TransQ[i];
                        }	   	            	
                }
                Down_MaxQ=0;Down_OutV=0;
                for (int i= 1; i <= PN; i++) {
                    if(Down_MaxQ<D_SumQ[i]) {
                            Down_MaxQ=(int)D_SumQ[i];
                    }
                    Down_OutV=Down_OutV+(int)D_SumQ[i];
                }
            }catch(Exception ex) {
                    ex.printStackTrace();
            }
   		
   	}
	public double N_SumQ[];
   	public void CHAPSO_GN_Calculate_Down_Natural_InQ() {
            try {
              N_SumQ=new double[PN+1];
                //녕원방출을 금성류입에 더하기
                for (int i = 1; i < PN; i++) { 
                    N_SumQ[i] = MFrame.Reservoir[1].UpQ[i];
                }                      
                //대각지점
                double TransQ[]=new double[PN+1];
                for(int i=2;i<=PN;i++) {
                    TransQ[i]=C0[1]*N_SumQ[i]+C1[1]*N_SumQ[i-1]+C2[1]*TransQ[i-1];
                }
                for(int i=1;i<=PN;i++) {
                    N_SumQ[i]=0;
                    N_SumQ[i]=TransQ[i]+MFrame.ConnectData.Daegak_InQ[i];
                    //System.out.println("DaeGak D_SumQ="+D_SumQ[i]);
                }
                //순천지점
                TransQ=new double[PN+1];
                for(int i=2;i<=PN;i++) {
                    TransQ[i]=C0[2]*N_SumQ[i]+C1[2]*N_SumQ[i-1]+C2[2]*TransQ[i-1];
                }
                for(int i=1;i<=PN;i++) {
                    N_SumQ[i]=0;
                    N_SumQ[i]=TransQ[i]+MFrame.ConnectData.Sunchon_InQ[i];
                    //System.out.println("SunChon D_SumQ="+D_SumQ[i]);
                }
                //성천지점
                TransQ=new double[PN+1];
                for(int i=2;i<=PN;i++) {
                    TransQ[i]=C0[3]*N_SumQ[i]+C1[3]*N_SumQ[i-1]+C2[3]*TransQ[i-1];
                }
                for(int i=1;i<=PN;i++) {
                    N_SumQ[i]=0;
                    N_SumQ[i]=TransQ[i]+MFrame.ConnectData.Songchon_InQ[i];
                    //System.out.println("SongChon D_SumQ="+D_SumQ[i]);
                }	  
                //봉화지점
                TransQ=new double[PN+1];
                for(int i=2;i<=PN;i++) {
                    TransQ[i]=C0[4]*N_SumQ[i]+C1[4]*N_SumQ[i-1]+C2[4]*TransQ[i-1];
                }
                for(int i=1;i<=PN;i++) {
                    N_SumQ[i]=0;
                    N_SumQ[i]=TransQ[i]+MFrame.ConnectData.Bonghua_InQ[i];
                    //System.out.println("BongHua D_SumQ="+D_SumQ[i]);
                }
                //미림지점
                TransQ=new double[PN+1];
                for(int i=2;i<=PN;i++) {
                    TransQ[i]=C0[5]*N_SumQ[i]+C1[5]*N_SumQ[i-1]+C2[5]*TransQ[i-1];
                }
                for(int i=1;i<=PN;i++) {
                    N_SumQ[i]=0;
                    N_SumQ[i]=TransQ[i]+MFrame.ConnectData.Mirim_InQ[i];
                    //System.out.println("Mirim D_SumQ="+D_SumQ[i]);
                }     
                //남강
                TransQ=new double[PN+1];
                for(int i=2;i<=PN;i++) {
                    TransQ[i]=C0[6]*MFrame.Reservoir[2].UpQ[i]+
                              C1[6]*MFrame.Reservoir[2].UpQ[i-1]+
                              C2[6]*TransQ[i-1];
                }
                for(int i=1;i<=PN;i++) {
                    N_SumQ[i]=N_SumQ[i]+TransQ[i];
                    //System.out.println((int)D_SumQ[i]);
                }	   	            

            }catch(Exception ex) {
                    ex.printStackTrace();
            }
   		
   	}

	int PN;
	public int B_Cal_ResN;
	int S_DT;
	public int opt_tmax,M,k_n;
	int gbest_Q;
	int B_gbest_N=0,C_Value=0;
	public int Opt_RN, r_n;
        double DZ=1.0;
        public int ResNo;
        public int MinPN;
        public int RepN;
        public int Step_OptGN[][];
   	public void CHAPSO_GN_Optimal_Simulation_For_One_Reservoir() {
                S_DT=1;
                PN=(int)MFrame.Reservoir[1].Time_SelN/S_DT;
                gbest_Q=100000;
                F_gbest_Rnd=new double[PN+1];
                F_gbestGN_Int=new int[PN+1];
                F_gbestGN_Q=new int[PN+1];
                
                this.CHAPSO_GN_Check_First_MaxOpenGN_For_Reservoir();
                k_n=1;	B_gbest_N=0;C_Value=0;
                Step_OptGN=new int[Cal_F_MaxGN+1][PN+1];
                MFrame.Reservoir[ResNo].Step_OptGN=new int[Cal_F_MaxGN+1][PN+1];
                this.CHAPSO_GN_First_Determine_pbest_gbest_of_Control_Plan_For_One_Reservoir(M, PN);
        	while(B_gbest_N<=opt_tmax-1) {
                    this.CHAPSO_GN_New_Evolution_Process_of_Discharge_Simulation_For_One_Reservoirs(M, PN);
                    if(C_Value<B_gbest_N) {
                        C_Value=B_gbest_N;
                        if(r_n==0){
                            MFrame.Sim_UI.progBar.progBar.setValue(Opt_RN+C_Value);
                        }
                    }
        	}
                r_n++;
        	int F_FinalZ=(int)MFrame.Reservoir[ResNo].NormalZ;
                this.CHAPSO_GN_Backword_Controling_to_Gurantee_NormalZ_For_One_Reservoir(F_FinalZ);
                //***********만일 수문개방대수가 2개인 풀이가 1개 혹은 2개가 끼여드는 경우에는 그 풀이를 무시하고 개방대수 1로
                //***********처리한다.*************//
                int aa=0;
        	for(int i=1;i<=PN;i++){
                    if(this.F_gbestGN_Int[i]>1){
                        aa++;
                    }
                }
        	if(aa<=2){
                    for(int i=1;i<=PN;i++){
                        if(this.F_gbestGN_Int[i]>1){
                            this.F_gbestGN_Int[i]=1;
                        }
                    }  
                }

//                if(O_ResNo[ResNo]==2){
//                    MFrame.Sim_UI.Save_Results_to_NotePad("R_0_2.txt",2 );
//                }else{
//                    MFrame.Sim_UI.Save_Results_to_NotePad("R_0_9.txt",9 );        
//                }

                for(int i=1;i<=PN;i++){
                        System.out.println(MFrame.Reservoir[ResNo].T_RCalQ[i]+"\t"+
                                           MFrame.Reservoir[ResNo].T_RCalZ[i]+"\t"+
                                           D_SumQ[i]+"\t"+F_gbestGN_Int[i]);
                        Step_OptGN[1][i]=F_gbestGN_Int[i];
                        MFrame.Reservoir[ResNo].Step_OptGN[1][i]=F_gbestGN_Int[i];
                }
                this.CHAPSO_GN_Divide_PartSL_Groups(this.F_gbestGN_Int,0);
                this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups();
                if(this.PartSL_N>2){
                    this.CHAPSO_GN_Optimal_Simulation_For_One_Reservoir();
                }  

                if(MFrame.Reservoir[ResNo].T_RCalZ[MFrame.Reservoir[ResNo].Time_SelN]>
                                                   MFrame.Reservoir[ResNo].NormalZ+1){
                    this.CHAPSO_GN_Optimal_Simulation_For_One_Reservoir();
                }                
                for(int i=1;i<=PN;i++){
                        Step_OptGN[2][i]=F_gbestGN_Int[i];
                        MFrame.Reservoir[ResNo].Step_OptGN[2][i]=F_gbestGN_Int[i];
                }      
//                if(ResNo==Cal_ResN){
//                    //MFrame.Sim_UI.Save_Results_to_NotePad("R_0_1.txt",1 );
//                    MFrame.Sim_UI.Save_Results_to_NotePad("R_0_2.txt",2 );
//                    MFrame.Sim_UI.Save_Results_to_NotePad("R_0_9.txt",9 );                    
//                }
                if(Cal_F_MaxGN>=3){
                    this.CHAPSO_GN_Divide_PartSL_Groups_At_SecondStep(1, F_gbestGN_Int);
                    this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups_At_SecondStep(1);
                    for(int i=1;i<=PartSL_N;i++){
                        if(S_PartSL_N[i]>2){
                            System.out.println("Secondlevel No="+i+", S_PartSL_N="+S_PartSL_N[i]);
                            this.CHAPSO_GN_Optimal_Simulation_For_One_Reservoir();
                        }
                    } 
                    for(int i=1;i<=PN;i++){
                            Step_OptGN[3][i]=F_gbestGN_Int[i];
                            MFrame.Reservoir[ResNo].Step_OptGN[3][i]=F_gbestGN_Int[i];
                    }                    
                }
//                for(int i=1;i<=PN;i++){
//                        System.out.println(MFrame.Reservoir[ResNo].Sort_T_RCalQ[i]+"\t"+
//                                           MFrame.Reservoir[ResNo].Sort_T_RCalZ[i]+"\t"+
//                                           D_SumQ[i]+"\t"+F_gbestGN_Int[i]);
//                }                
//                if(ResNo==Cal_ResN){
//                    //MFrame.Sim_UI.Save_Results_to_NotePad("R_0_1.txt",1 );
//                    MFrame.Sim_UI.Save_Results_to_NotePad("R_0_2.txt",2 );
//                    MFrame.Sim_UI.Save_Results_to_NotePad("R_0_9.txt",9 );                    
//                }
                if(Cal_F_MaxGN>=4){
                    this.CHAPSO_GN_Divide_PartSL_Groups(F_gbestGN_Int, 1);
                    this.CHAPSO_GN_Divide_PartSL_Groups_At_SecondStep(2, F_gbestGN_Int);
                    this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups_At_SecondStep(2);
                    for(int i=1;i<=PartSL_N;i++){
                        if(S_PartSL_N[i]>2){
                            System.out.println("Secondlevel No="+i+", S_PartSL_N="+S_PartSL_N[i]);
                            this.CHAPSO_GN_Optimal_Simulation_For_One_Reservoir();
                        }
                    }
                    for(int i=1;i<=PN;i++){
                            Step_OptGN[4][i]=F_gbestGN_Int[i];
                            MFrame.Reservoir[ResNo].Step_OptGN[4][i]=F_gbestGN_Int[i];
                    }                      
                }

                if(Cal_F_MaxGN>=5){
                    this.CHAPSO_GN_Divide_PartSL_Groups(F_gbestGN_Int, 2);
                    this.CHAPSO_GN_Divide_PartSL_Groups_At_SecondStep(3, F_gbestGN_Int);
                    this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups_At_SecondStep(3);
                    for(int i=1;i<=PN;i++){
                            Step_OptGN[5][i]=F_gbestGN_Int[i];
                            MFrame.Reservoir[ResNo].Step_OptGN[5][i]=F_gbestGN_Int[i];
                    }                      
                }           
//                
        	F_FinalZ=(int)MFrame.Reservoir[ResNo].NormalZ;
        	this.CHAPSO_GN_Backword_Controling_to_Gurantee_NormalZ_For_One_Reservoir(F_FinalZ);
          
                MFrame.Reservoir[ResNo].PSO_RMPSG_Q=new double[PN+1];
                MFrame.Reservoir[ResNo].PSO_RMPSG_Z=new double[PN+1];
                MFrame.Reservoir[ResNo].PSO_RMPSG_GN=new double[PN+1];
                for(int i=1;i<=PN;i++){
                    MFrame.Reservoir[ResNo].PSO_RMPSG_Q[i]=MFrame.Reservoir[ResNo].T_RCalQ[i];
                    MFrame.Reservoir[ResNo].PSO_RMPSG_Z[i]=MFrame.Reservoir[ResNo].T_RCalZ[i];
                    MFrame.Reservoir[ResNo].PSO_RMPSG_GN[i]=F_gbestGN_Int[i];
                    //System.out.println("i="+i+" Q="+MFrame.Reservoir[O_ResNo[ResNo]].G_SimPSO_PlanQ[i]);
                }
        	this.CHAPSO_GN_Calculate_Down_Natural_InQ();

   			//this.Backword_Controling_Plan_of_FloodGates_to_Gurantee_NormalZ();
   	}

   	//***************************************************************************************************//
   	//************립자무리분석이 끝난 다음 무리합성분석을 진행하여 잔류풀이들과 부분풀이모임들을 가능한껏 재배렬하기 위한 계산모쥴********//
   	//**********************************2019.4.17작성 작성자 KYGN*******************************************//
   	public group OneGN_Group[];
   	public int OneGN_N;
   	public group BigGN_Group[];
   	public int BigGN_N;
   	group M_BigGN_Group;
        public group PartSL_Group[];
        public int PartSL_N;
   	public void CHAPSO_GN_Divide_PartSL_Groups(int GN_Int[],int GN_Size) {
   		try {
                    //부분풀이모임들을 보관한다.
                    int k=0;
                    PartSL_Group=new group[PN+1];
                    for(int j=1;j<=PN;j++) {
                            PartSL_Group[j]=new group();
                    }
                    for(int j=2;j<=PN-1;j++) {
                            if(GN_Int[j]>GN_Size) {
                                if(GN_Int[j-1]<=GN_Size && GN_Int[j+1]<=GN_Size) {
                                        k++;
                                        PartSL_Group[k].S_No=j;
                                        PartSL_Group[k].E_No=j;
                                        PartSL_Group[k].g_N=1;
                                        PartSL_Group[k].g_no=new int[2];
                                        PartSL_Group[k].g_no[1]=GN_Int[j];
                                }
                                if(j==2) {
                                        if(GN_Int[j-1]>GN_Size && GN_Int[j]>GN_Size) {
                                                k++;
                                                PartSL_Group[k].S_No=j-1;
                                                PartSL_Group[k].g_N=1;
                                        }
                                }
                                if(GN_Int[j-1]<=GN_Size && GN_Int[j+1]>GN_Size) {
                                        k++;
                                        PartSL_Group[k].S_No=j;
                                        PartSL_Group[k].g_N=1;
                                }else if(GN_Int[j-1]>GN_Size && GN_Int[j+1]>GN_Size) {
                                        PartSL_Group[k].g_N++;
                                }else if(GN_Int[j-1]>GN_Size && GN_Int[j+1]<=GN_Size) {
                                        //System.out.println("j="+j);
                                        PartSL_Group[k].g_N++;
                                        PartSL_Group[k].E_No=j;
                                        PartSL_Group[k].g_no=new int[PartSL_Group[k].g_N+1];
                                        //System.out.println("********************************");
                                        for(int jj=1;jj<=PartSL_Group[k].g_N;jj++) {
                                                PartSL_Group[k].g_no[jj]=GN_Int[PartSL_Group[k].S_No+jj-1];
                                                //System.out.println(BigGN_Group[k].g_no[jj]);
                                        }
                                        //System.out.println("********************************");
                                }
                            }
                    }

                    if(k>0){
                        if(PartSL_Group[k].E_No==0) {
                                PartSL_N=k-1;
                        }else {
                                PartSL_N=k;
                        }                                    
                    }
                    for(int i=1;i<=PartSL_N;i++){
                        PartSL_Group[i].S_No--;
                        PartSL_Group[i].E_No++;
                    }
                    System.out.println("1.First Divide PartSL_Groups");
                    //this.SIMPSO_GN_Prit_PartSL_Groups(PartSL_N,PartSL_Group);
   		}catch(Exception ex) {
   			System.out.println("ResNo="+ResNo);
   			ex.printStackTrace();
   		}
   	}
        public void CHAPSO_GN_Prit_PartSL_Groups(int PartSL_N,group PartSL_Group[]){
            try{
                for(int i=1;i<=PartSL_N;i++){
                    //System.out.println();
                    for(int j=1;j<=PartSL_Group[i].g_N; j++){
                        if(PartSL_Group[i].g_N==1){
                            System.out.printf("(("+PartSL_Group[i].g_no[j]+"),","%3d");
                        }else{
                            if(j==1){
                                System.out.printf("(("+PartSL_Group[i].g_no[j]+",","%3d");
                            }else if(j==PartSL_Group[i].g_N){
                                System.out.printf(PartSL_Group[i].g_no[j]+"), ","%3d");
                            }else{
                                System.out.printf(PartSL_Group[i].g_no[j]+",","%3d");
                            }
                        }
                    }
                    System.out.printf("Ns="+PartSL_Group[i].S_No+", ","%5d");
                    System.out.printf("Ne="+PartSL_Group[i].E_No+", ","%5d");
                    System.out.printf("Nu="+PartSL_Group[i].g_N+")");
                    System.out.println();  
                }
                System.out.println();                
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        public void CHAPSO_GN_Prit_S_PartSL_Groups(){
            try{
                for(int i=1;i<=PartSL_N;i++){
                    for(int j=1;j<=S_PartSL_N[i];j++){
                    //System.out.println();
                        for(int k=1;k<=S_PartSL_Group[i][j].g_N; k++){
                            if(S_PartSL_Group[i][j].g_N==1){
                                System.out.printf("(("+S_PartSL_Group[i][j].g_no[k]+"),","%3d");
                            }else{
                                if(k==1){
                                    System.out.printf("(("+S_PartSL_Group[i][j].g_no[k]+",","%3d");
                                }else if(k==S_PartSL_Group[i][j].g_N){
                                    System.out.printf(S_PartSL_Group[i][j].g_no[k]+"), ","%3d");
                                }else{
                                    System.out.printf(S_PartSL_Group[i][j].g_no[k]+",","%3d");
                                }
                            }
                        }
                        System.out.printf("Ns="+S_PartSL_Group[i][j].S_No+", ","%5d");
                        System.out.printf("Ne="+S_PartSL_Group[i][j].E_No+", ","%5d");
                        System.out.printf("Nu="+S_PartSL_Group[i][j].g_N+")");
                        System.out.println();                          
                    }
                    System.out.println();   
                }
                             
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }        
   	//public boolean D_Flag=false;
        
   	public void CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups() {
   		try {
                    for(int j=1;j<=PartSL_N;j++) {
                            this.CHAPSO_GN_Combine_Two_Different_PartSL_Groups(j, 1,this.F_gbestGN_Int);
                    }	   			
   		}catch(Exception ex) {
   			ex.printStackTrace();
   		}
   	}    
        public void CHAPSO_GN_Sub_Left_Combine_Two_PartSL_Groups(int LN,int BN, int GN_Int[]){
            try{
                    //왼쪽결합을 진행한다.
                    mid_big_group=new group();
                    mid_big_group.g_N=PartSL_Group[LN].g_N+PartSL_Group[BN].g_N;
                    mid_big_group.g_no=new int[mid_big_group.g_N+1];
                    mid_big_group.S_No=PartSL_Group[LN].S_No-PartSL_Group[BN].g_N;
                    mid_big_group.E_No=PartSL_Group[LN].E_No;
                    int k=0;
                    for(int j=1;j<=PartSL_Group[BN].g_N;j++) {
                            k++;
                            mid_big_group.g_no[k]=PartSL_Group[BN].g_no[j];
                    }
                    for(int j=1;j<=PartSL_Group[LN].g_N;j++) {
                            k++;
                            mid_big_group.g_no[k]=PartSL_Group[LN].g_no[j];
                    }
                    for(int i=1;i<=PN;i++) {
                            GN_Int[i]=0;
                    }
                    for(int i=1;i<=PartSL_N;i++) {
                        if(i!=LN && i!=BN) {
                            k=0;
                            for(int j=PartSL_Group[i].S_No+1;j<=PartSL_Group[i].E_No-1;j++) {
                                    k++;
                                    GN_Int[j]=PartSL_Group[i].g_no[k];
                            }		   						
                        }else {
                            k=0;
                            for(int j=mid_big_group.S_No+1;j<=mid_big_group.E_No-1;j++) {
                                    k++;
                                    GN_Int[j]=mid_big_group.g_no[k];
                            }     						
                        }
                    }
                    this.CHAPSO_GN_Simulation_for_Reservoirs_by_One_Hour();                
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        public void CHAPSO_GN_Sub_Right_Combine_Two_PartSL_Groups(int LN,int BN, int GN_Int[]){
            try{
                    //오른쪽결합을 진행한다.
                    mid_big_group=new group();
                    mid_big_group.g_N=PartSL_Group[LN].g_N+PartSL_Group[BN].g_N;
                    mid_big_group.g_no=new int[mid_big_group.g_N+1];
                    mid_big_group.S_No=PartSL_Group[LN].S_No;
                    mid_big_group.E_No=PartSL_Group[LN].E_No+PartSL_Group[BN].g_N;
                    int k=0;
                    for(int j=1;j<=PartSL_Group[LN].g_N;j++) {
                            k++;
                            mid_big_group.g_no[k]=PartSL_Group[LN].g_no[j];
                    }
                    for(int j=1;j<=PartSL_Group[BN].g_N;j++) {
                            k++;
                            mid_big_group.g_no[k]=PartSL_Group[BN].g_no[j];
                    }
                    for(int i=1;i<=PN;i++) {
                            GN_Int[i]=0;
                    }
                    for(int i=1;i<=PartSL_N;i++) {
                        if(i!=LN && i!=BN) {
                            k=0;
                            for(int j=PartSL_Group[i].S_No+1;j<=PartSL_Group[i].E_No-1;j++) {
                                if(j<=PN){
                                    k++;
                                    GN_Int[j]=PartSL_Group[i].g_no[k];                                    
                                }
                            }		   						
                        }else {
                            k=0;
                            for(int j=mid_big_group.S_No+1;j<=mid_big_group.E_No-1;j++) {
                                if(j<=PN){
                                    k++;
                                    GN_Int[j]=mid_big_group.g_no[k];                                    
                                }
                            }     						
                        }
                    }
                    this.CHAPSO_GN_Simulation_for_Reservoirs_by_One_Hour();
            }catch(Exception ex){
                ex.printStackTrace();
            }            
        }
        public void CHAPSO_GN_Sub_Final_Combine_Two_PartSL_Groups(group mid_big_group,int LN,int BN, int GN_Int[]){
            try{
                    int k;
                    group big_group[]=new group[PartSL_N+1];
                    for(int i=1;i<=PartSL_N;i++){
                        big_group[i]=new group();
                    }
                    big_group[1]=mid_big_group;
                    k=1;
                    for(int i=1;i<=PartSL_N;i++) {
                        if(i!=LN && i!=BN) {
                                k++;
                                big_group[k]=PartSL_Group[i];
                        }
                    }
                    PartSL_N--;
                    PartSL_Group=new group[PartSL_N+1];
                    for(int i=1;i<=PartSL_N;i++) {
                            PartSL_Group[i]=new group();
                            PartSL_Group[i]=big_group[i];
                            //System.out.println("S_No="+BigGN_Group[i].S_No+" E_No="+BigGN_Group[i].E_No);
                    }
                    
                    this.CHAPSO_GN_Prit_PartSL_Groups(PartSL_N,PartSL_Group);                
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        group mid_big_group=new group();
        public void CHAPSO_GN_Combine_Two_Different_PartSL_Groups(int LN,int BN, int GN_Int[]){
            try{
                
                int k=0;
                if(LN!=BN && BN<=PartSL_N && LN<=PartSL_N){
                    if(PartSL_Group[LN].S_No-PartSL_Group[BN].g_N<0){
                        //오른쪽결합만을 시도한다.
                        
                        this.CHAPSO_GN_Sub_Right_Combine_Two_PartSL_Groups(LN, BN, GN_Int);
                        if(this.Down_MaxQ>this.B_gbest_Q || 
                        MFrame.Reservoir[ResNo].Cal_MaxZ>MFrame.Reservoir[ResNo].FloodZ+0.1 ||
                        MFrame.Reservoir[ResNo].Cal_MaxZ<MFrame.Reservoir[ResNo].FloodZ-DZ ) {
                        //System.out.println("Right Failure! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[O_ResNo[ResNo]].Sort_Max_Z);
                        //오른쪽결합에 실패하면 본래 풀이모임을 복귀하고 다음 부분풀이를 선택한다.
                                for(int i=1;i<=PN;i++) {
                                        GN_Int[i]=0;
                                }
                                for(int i=1;i<=PartSL_N;i++) {
                                        k=0;
                                        for(int j=PartSL_Group[i].S_No+1;j<=PartSL_Group[i].E_No-1;j++) {
                                                k++;
                                                GN_Int[j]=PartSL_Group[i].g_no[k];
                                        }  						
                                }		
                                if(BN<PartSL_N) {
                                        BN++;
                                        this.CHAPSO_GN_Combine_Two_Different_PartSL_Groups(LN, BN,GN_Int);
                                }                            
                        }else{
                                //System.out.println("Right success! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[O_ResNo[ResNo]].Sort_Max_Z);
                                //오른쪽결합에 성공하면 부분풀이모임들을 갱신한다. 
                                this.CHAPSO_GN_Sub_Final_Combine_Two_PartSL_Groups(mid_big_group,LN, BN, GN_Int);
                                this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups();
                        }
                    }else{
                        //왼쪽, 및 오른쪽결합을 둘다 시도한다.
                        //우선 왼쪽결합을 시도한다.
                        this.CHAPSO_GN_Sub_Left_Combine_Two_PartSL_Groups(LN, BN, GN_Int);
                        if(this.Down_MaxQ>this.B_gbest_Q || 
                        MFrame.Reservoir[ResNo].Cal_MaxZ>MFrame.Reservoir[ResNo].FloodZ+0.1 ||
                        MFrame.Reservoir[ResNo].Cal_MaxZ<MFrame.Reservoir[ResNo].FloodZ-DZ ){
                            //System.out.println("Left Failure! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[O_ResNo[ResNo]].Sort_Max_Z);
                            //왼쪽결합에 실패하면 오른쪽결합을 시도한다.
                            this.CHAPSO_GN_Sub_Right_Combine_Two_PartSL_Groups(LN, BN, GN_Int);
                        if(this.Down_MaxQ>this.B_gbest_Q || 
                        MFrame.Reservoir[ResNo].Cal_MaxZ>MFrame.Reservoir[ResNo].FloodZ+0.1 ||
                        MFrame.Reservoir[ResNo].Cal_MaxZ<MFrame.Reservoir[ResNo].FloodZ-DZ ){
                            //System.out.println("Right Failure! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[O_ResNo[ResNo]].Sort_Max_Z);                                
                                //오른쪽결합에도 실패하면 본래 풀이모임을 복귀하고 다음 부분풀이를 선택한다.
                                for(int i=1;i<=PN;i++) {
                                        GN_Int[i]=0;
                                }
                                for(int i=1;i<=PartSL_N;i++) {
                                        k=0;
                                        for(int j=PartSL_Group[i].S_No+1;j<=PartSL_Group[i].E_No-1;j++) {
                                                k++;
                                                GN_Int[j]=PartSL_Group[i].g_no[k];
                                        }  						
                                }		
                                if(BN<PartSL_N) {
                                        BN++;
                                        this.CHAPSO_GN_Combine_Two_Different_PartSL_Groups(LN, BN,GN_Int);
                                }                                
                            }else{
                                //오른쪽결합에 성공하면 부분풀이모임들을 갱신한다. 
                                //System.out.println("Right success! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[O_ResNo[ResNo]].Sort_Max_Z);
                                this.CHAPSO_GN_Sub_Final_Combine_Two_PartSL_Groups(mid_big_group,LN, BN, GN_Int);
                                this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups();                                
                            }                           
      
                        }else{
                            //왼쪽결합에 성공하면 부분풀이모임들을 갱신한다. 
                            //System.out.println("Left success! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[O_ResNo[ResNo]].Sort_Max_Z);
                            this.CHAPSO_GN_Sub_Final_Combine_Two_PartSL_Groups(mid_big_group, LN, BN, GN_Int);
                            this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups();
                        }
                    }
                }else{
                    if(BN<PartSL_N) {
                            BN++;
                            this.CHAPSO_GN_Combine_Two_Different_PartSL_Groups(LN, BN,GN_Int);
                    }                      
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        //*************************************************************************************************************************//
        //*************************수문개방대수가 2대이상인 경우가 섞여있는 경우에 조합방법********************************************//
        //*************************************************************************************************************************//
        public group S_PartSL_Group[][];
        public int S_PartSL_N[];
        public void CHAPSO_GN_Divide_PartSL_Groups_At_SecondStep(int GN_Size,int GN_Int[]){
            try{
                int k=0;
                S_PartSL_Group=new group[PartSL_N+1][PN+1];
                S_PartSL_N=new int[PartSL_N+1];
                for(int i=1;i<=PartSL_N;i++){
                    for(int j=1;j<=PN;j++) {
                        S_PartSL_Group[i][j]=new group();
                    }
                }
                       
                for(int i=1;i<=PartSL_N;i++){
                    k=0;
                    for(int j=2;j<=PartSL_Group[i].g_N-1;j++){
                        if(PartSL_Group[i].g_no[j]>GN_Size){
                            if(PartSL_Group[i].g_no[j-1]<=GN_Size && PartSL_Group[i].g_no[j+1]<=GN_Size) {
                                    k++;
                                    S_PartSL_Group[i][k].S_No=PartSL_Group[i].S_No+j-1;
                                    S_PartSL_Group[i][k].E_No=S_PartSL_Group[i][k].S_No+2;
                                    S_PartSL_Group[i][k].g_N=1;
                                    S_PartSL_Group[i][k].g_no=new int[2];
                                    S_PartSL_Group[i][k].g_no[1]=GN_Int[PartSL_Group[i].S_No+j];
                            } 
                           if(j==2) {
                               if(PartSL_Group[i].g_no[j-1]>GN_Size && PartSL_Group[i].g_no[j]>GN_Size) {
                                       k++;
                                       S_PartSL_Group[i][k].S_No=PartSL_Group[i].S_No;
                                       S_PartSL_Group[i][k].g_N=1;
                               }
                           } 
                           if(PartSL_Group[i].g_no[j-1]==GN_Size && PartSL_Group[i].g_no[j+1]>GN_Size) {
                                   k++;
                                   S_PartSL_Group[i][k].S_No=PartSL_Group[i].S_No+j-1;
                                   S_PartSL_Group[i][k].g_N=1;
                           }else if(PartSL_Group[i].g_no[j-1]>GN_Size && PartSL_Group[i].g_no[j+1]>GN_Size) {
                                   S_PartSL_Group[i][k].g_N++;
                           }else if(PartSL_Group[i].g_no[j-1]>GN_Size && PartSL_Group[i].g_no[j+1]==GN_Size) {
                                   //System.out.println("j="+j);
                                   S_PartSL_Group[i][k].g_N++;
                                   S_PartSL_Group[i][k].E_No=PartSL_Group[i].S_No+j+1;
                                   S_PartSL_Group[i][k].g_no=new int[S_PartSL_Group[i][k].g_N+1];
                                   //System.out.println("********************************");
                                   for(int jj=1;jj<=S_PartSL_Group[i][k].g_N;jj++) {
                                           S_PartSL_Group[i][k].g_no[jj]=GN_Int[S_PartSL_Group[i][k].S_No+jj];
                                           //System.out.println(BigGN_Group[k].g_no[jj]);
                                   }
                                   //System.out.println("********************************");
                           }else if(PartSL_Group[i].g_no[j-1]>GN_Size && PartSL_Group[i].g_no[j]>GN_Size ){
                                    int ENO=PartSL_Group[i].S_No+j+1;
                                    if(ENO==PartSL_Group[i].E_No){
                                        S_PartSL_Group[i][k].g_N++;
                                        S_PartSL_Group[i][k].E_No=PartSL_Group[i].S_No+j+1;
                                        S_PartSL_Group[i][k].g_no=new int[S_PartSL_Group[i][k].g_N+1];
                                        for(int jj=1;jj<=S_PartSL_Group[i][k].g_N;jj++) {
                                                S_PartSL_Group[i][k].g_no[jj]=GN_Int[S_PartSL_Group[i][k].S_No+jj];
                                                //System.out.println(BigGN_Group[k].g_no[jj]);
                                        }
                                    }
                               
                           }
                        }
                    }
                    S_PartSL_N[i]=k;
                }
                System.out.println("Second Division!");
                //this.CHAPSO_GN_Prit_S_PartSL_Groups();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

   	public void CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups_At_SecondStep(int GN_Size) {
   		try {
                    for(int i=1;i<=PartSL_N;i++){
                        for(int j=1;j<=S_PartSL_N[i];j++) {
                            if(S_PartSL_N[i]>=2){
                                this.CHAPSO_GN_Combine_Two_Different_PartSL_Groups_At_SecondStep(i,j, 1,this.F_gbestGN_Int,GN_Size);
                            }
                             
                        }
                    }
   		}catch(Exception ex) {
                        //this.SIMPSO_GN_Optimal_Simulation_For_One_Reservoir(No);
   			ex.printStackTrace();
   		}
   	}    
        public void CHAPSO_GN_Sub_Left_Combine_Two_PartSL_Groups_At_SecondStep(int IN,int LN,int BN, int GN_Int[], int GN_Size){
            try{
                    //왼쪽결합을 진행한다.
                    mid_big_group=new group();
                    mid_big_group.g_N=S_PartSL_Group[IN][LN].g_N+S_PartSL_Group[IN][BN].g_N;
                    mid_big_group.g_no=new int[mid_big_group.g_N+1];
                    mid_big_group.S_No=S_PartSL_Group[IN][LN].S_No-S_PartSL_Group[IN][BN].g_N;
                    mid_big_group.E_No=S_PartSL_Group[IN][LN].E_No;
                    int k=0,k1=0;
                    for(int j=1;j<=S_PartSL_Group[IN][BN].g_N;j++) {
                            k++;
                            mid_big_group.g_no[k]=S_PartSL_Group[IN][BN].g_no[j];
                    }
                    for(int j=1;j<=S_PartSL_Group[IN][LN].g_N;j++) {
                            k++;
                            mid_big_group.g_no[k]=S_PartSL_Group[IN][LN].g_no[j];
                    }
                    k=0;
                    for(int j=PartSL_Group[IN].S_No+1;j<=PartSL_Group[IN].E_No-1;j++){
                        k++;
                        GN_Int[j]=GN_Size;
                        PartSL_Group[IN].g_no[k]=GN_Size;                         
                    }  
                    for(int i=1;i<=S_PartSL_N[IN];i++) {
                        if(i!=LN && i!=BN) {
                            k=0;
                            for(int j=S_PartSL_Group[IN][i].S_No+1;j<=S_PartSL_Group[IN][i].E_No-1;j++) {
                                    k++;
                                    GN_Int[j]=S_PartSL_Group[IN][i].g_no[k];
                                    k1=j-PartSL_Group[IN].S_No;
                                    PartSL_Group[IN].g_no[k1]= GN_Int[j];                                   
                            }		   						
                        }else {
                            k=0;
                            for(int j=mid_big_group.S_No+1;j<=mid_big_group.E_No-1;j++) {
                                    k++;
                                    GN_Int[j]=mid_big_group.g_no[k];
                                    k1=j-PartSL_Group[IN].S_No;
                                    PartSL_Group[IN].g_no[k1]= GN_Int[j];  
                            }     						
                        }
                    }
                    this.CHAPSO_GN_Simulation_for_Reservoirs_by_One_Hour();                
            }catch(Exception ex){
                //this.SIMPSO_GN_Optimal_Simulation_For_One_Reservoir(ResNo);
                //ex.printStackTrace();
            }
        }
        public void CHAPSO_GN_Sub_Right_Combine_Two_PartSL_Groups_At_SecondStep(int IN,int LN,int BN, int GN_Int[],int GN_Size){
            try{
                    //오른쪽결합을 진행한다.
                    mid_big_group=new group();
                    mid_big_group.g_N=S_PartSL_Group[IN][LN].g_N+S_PartSL_Group[IN][BN].g_N;
                    mid_big_group.g_no=new int[mid_big_group.g_N+1];
                    mid_big_group.S_No=S_PartSL_Group[IN][LN].S_No;
                    mid_big_group.E_No=S_PartSL_Group[IN][LN].E_No+S_PartSL_Group[IN][BN].g_N;
                    int k=0,k1=0;
                    for(int j=1;j<=S_PartSL_Group[IN][LN].g_N;j++) {
                            k++;
                            mid_big_group.g_no[k]=S_PartSL_Group[IN][LN].g_no[j];
                    }
                    for(int j=1;j<=S_PartSL_Group[IN][BN].g_N;j++) {
                            k++;
                            mid_big_group.g_no[k]=S_PartSL_Group[IN][BN].g_no[j];
                    }
                    k=0;
                    for(int j=PartSL_Group[IN].S_No+1;j<=PartSL_Group[IN].E_No-1;j++){
                        k++;
                        GN_Int[j]=GN_Size;
                        PartSL_Group[IN].g_no[k]=GN_Size;
                    }    
                    for(int i=1;i<=S_PartSL_N[IN];i++) {
                        if(i!=LN && i!=BN) {
                            k=0;
                            for(int j=S_PartSL_Group[IN][i].S_No+1;j<=S_PartSL_Group[IN][i].E_No-1;j++) {
                                    k++;
                                    GN_Int[j]=S_PartSL_Group[IN][i].g_no[k];
                                    k1=j-PartSL_Group[IN].S_No;
                                    PartSL_Group[IN].g_no[k1]= GN_Int[j];
                                    
                            }		   						
                        }else {
                            k=0;
                            for(int j=mid_big_group.S_No+1;j<=mid_big_group.E_No-1;j++) {
                                    k++;
                                    GN_Int[j]=mid_big_group.g_no[k];
                                    k1=j-PartSL_Group[IN].S_No;
                                    PartSL_Group[IN].g_no[k1]= GN_Int[j];
                            }     						
                        }
                    }
                    this.CHAPSO_GN_Simulation_for_Reservoirs_by_One_Hour();
            }catch(Exception ex){
                //this.SIMPSO_GN_Optimal_Simulation_For_One_Reservoir(ResNo);
                //ex.printStackTrace();
            }            
        }
        public void CHAPSO_GN_Sub_Final_Combine_Two_PartSL_Groups_At_SecondStep(group mid_big_group,int IN,int LN,int BN, int GN_Int[]){
            try{
                    int k;
                    group big_group[]=new group[S_PartSL_N[IN]+1];
                    for(int i=1;i<=S_PartSL_N[IN];i++){
                        big_group[i]=new group();
                    }
                    big_group[1]=mid_big_group;
                    k=1;
                    for(int i=1;i<=S_PartSL_N[IN];i++) {
                        if(i!=LN && i!=BN) {
                                k++;
                                big_group[k]=S_PartSL_Group[IN][i];
                        }
                    }
                    S_PartSL_N[IN]=S_PartSL_N[IN]-1;
                    S_PartSL_Group[IN]=new group[S_PartSL_N[IN]+1];
                    for(int i=1;i<=S_PartSL_N[IN];i++) {
                            S_PartSL_Group[IN][i]=new group();
                            S_PartSL_Group[IN][i]=big_group[i];
                            //System.out.println("S_No="+BigGN_Group[i].S_No+" E_No="+BigGN_Group[i].E_No);
                    }
                    //System.out.println("Success!");
                    this.CHAPSO_GN_Prit_PartSL_Groups(PartSL_N,PartSL_Group);                
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        public void CHAPSO_GN_Combine_Two_Different_PartSL_Groups_At_SecondStep(int IN,int LN,int BN, int GN_Int[],int GN_Size){
            try{
                
                int k=0;
                if(LN!=BN && BN<=S_PartSL_N[IN] && LN<=S_PartSL_N[IN]){
                    if((S_PartSL_Group[IN][LN].S_No+S_PartSL_Group[IN][BN].g_N)>PartSL_Group[IN].E_No){
                        //왼쪽결합만을 시도한다.
                        if((S_PartSL_Group[IN][LN].S_No-S_PartSL_Group[IN][BN].g_N)>=PartSL_Group[IN].S_No){
                            this.CHAPSO_GN_Sub_Left_Combine_Two_PartSL_Groups_At_SecondStep(IN, LN, BN, GN_Int,GN_Size);
                            if(this.Down_MaxQ>this.B_gbest_Q || 
                            MFrame.Reservoir[ResNo].Cal_MaxZ>MFrame.Reservoir[ResNo].FloodZ+0.1 ||
                            MFrame.Reservoir[ResNo].Cal_MaxZ<MFrame.Reservoir[ResNo].FloodZ-DZ ) {
                            System.out.println("Left Failure! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[ResNo].Cal_MaxZ);
                            //왼쪽결합에 실패하면 본래 풀이모임을 복귀하고 다음 부분풀이를 선택한다.
                                    for(int j=PartSL_Group[IN].S_No+1;j<PartSL_Group[IN].E_No-1;j++){
                                        GN_Int[j]=1;
                                    }                             
                                    for(int i=1;i<=S_PartSL_N[IN];i++) {
                                            k=0;
                                            for(int j=S_PartSL_Group[IN][i].S_No+1;j<=S_PartSL_Group[IN][i].E_No-1;j++) {
                                                    k++;
                                                    GN_Int[j]=S_PartSL_Group[IN][i].g_no[k];
                                            }  						
                                    }		
                                    if(BN<S_PartSL_N[IN]) {
                                            BN++;
                                            this.CHAPSO_GN_Combine_Two_Different_PartSL_Groups_At_SecondStep(IN, LN, BN,GN_Int,GN_Size);
                                    }                            
                            }else{
                                    System.out.println("Left success! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[ResNo].Cal_MaxZ);
                                    //오른쪽결합에 성공하면 부분풀이모임들을 갱신한다. 
                                    this.CHAPSO_GN_Sub_Final_Combine_Two_PartSL_Groups_At_SecondStep(mid_big_group,IN, LN, BN, GN_Int);
                                    this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups_At_SecondStep(GN_Size);
                            } 
                        }

                    }else if((S_PartSL_Group[IN][LN].S_No-S_PartSL_Group[IN][BN].g_N)<PartSL_Group[IN].S_No){
                        //오른쪽결합만을 시도한다.
                        if((S_PartSL_Group[IN][LN].S_No+S_PartSL_Group[IN][BN].g_N)<PartSL_Group[IN].E_No){
                            this.CHAPSO_GN_Sub_Right_Combine_Two_PartSL_Groups_At_SecondStep(IN, LN, BN, GN_Int,GN_Size);
                            if(this.Down_MaxQ>this.B_gbest_Q || 
                            MFrame.Reservoir[ResNo].Cal_MaxZ>MFrame.Reservoir[ResNo].FloodZ+0.1 ||
                            MFrame.Reservoir[ResNo].Cal_MaxZ<MFrame.Reservoir[ResNo].FloodZ-DZ ){
                            System.out.println("Right Failure! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[ResNo].Cal_MaxZ);
                            //오른쪽결합에 실패하면 본래 풀이모임을 복귀하고 다음 부분풀이를 선택한다.
                                    for(int j=PartSL_Group[IN].S_No+1;j<=PartSL_Group[IN].E_No-1;j++){
                                        GN_Int[j]=1;
                                    }                             
                                    for(int i=1;i<=S_PartSL_N[IN];i++) {
                                            k=0;
                                            for(int j=S_PartSL_Group[IN][i].S_No+1;j<=S_PartSL_Group[IN][i].E_No-1;j++) {
                                                    k++;
                                                    GN_Int[j]=S_PartSL_Group[IN][i].g_no[k];
                                            }  						
                                    }		
                                    if(BN<S_PartSL_N[IN]) {
                                            BN++;
                                            this.CHAPSO_GN_Combine_Two_Different_PartSL_Groups_At_SecondStep(IN, LN, BN,GN_Int,GN_Size);
                                    }                            
                            }else{
                                    System.out.println("Right success! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[ResNo].Cal_MaxZ);
                                    //오른쪽결합에 성공하면 부분풀이모임들을 갱신한다. 
                                    this.CHAPSO_GN_Sub_Final_Combine_Two_PartSL_Groups_At_SecondStep(mid_big_group,IN, LN, BN, GN_Int);
                                    this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups_At_SecondStep(GN_Size);
                            } 
                        }

                    }else{
                        //왼쪽, 및 오른쪽결합을 둘다 시도한다.
                        //우선 왼쪽결합을 시도한다.
                        this.CHAPSO_GN_Sub_Left_Combine_Two_PartSL_Groups_At_SecondStep(IN, LN, BN, GN_Int,GN_Size);
                        if(this.Down_MaxQ>this.B_gbest_Q || 
                        MFrame.Reservoir[ResNo].Cal_MaxZ>MFrame.Reservoir[ResNo].FloodZ+0.1 ||
                        MFrame.Reservoir[ResNo].Cal_MaxZ<MFrame.Reservoir[ResNo].FloodZ-DZ ){
                            System.out.println("Left Failure! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[ResNo].Cal_MaxZ);
                            //왼쪽결합에 실패하면 오른쪽결합을 시도한다.
                            this.CHAPSO_GN_Sub_Right_Combine_Two_PartSL_Groups_At_SecondStep(IN, LN, BN, GN_Int,GN_Size);
                            if(this.Down_MaxQ>this.B_gbest_Q || 
                            MFrame.Reservoir[ResNo].Cal_MaxZ>MFrame.Reservoir[ResNo].FloodZ+0.1 ||
                            MFrame.Reservoir[ResNo].Cal_MaxZ<MFrame.Reservoir[ResNo].FloodZ-DZ ){
                                System.out.println("Right Failure! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[ResNo].Cal_MaxZ);                                
                                //오른쪽결합에도 실패하면 본래 풀이모임을 복귀하고 다음 부분풀이를 선택한다.
                                for(int j=PartSL_Group[IN].S_No+1;j<=PartSL_Group[IN].E_No-1;j++){
                                    GN_Int[j]=1;
                                }
                                for(int i=1;i<=S_PartSL_N[IN];i++) {
                                        k=0;
                                        for(int j=S_PartSL_Group[IN][i].S_No+1;j<=S_PartSL_Group[IN][i].E_No-1;j++) {
                                                k++;
                                                GN_Int[j]=S_PartSL_Group[IN][i].g_no[k];
                                        }  						
                                }		
                                if(BN<S_PartSL_N[IN]) {
                                        BN++;
                                        this.CHAPSO_GN_Combine_Two_Different_PartSL_Groups_At_SecondStep(IN, LN, BN,GN_Int,GN_Size);
                                }                                
                            }else{
                                //오른쪽결합에 성공하면 부분풀이모임들을 갱신한다. 
                                System.out.println("Right success! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[ResNo].Cal_MaxZ);
                                this.CHAPSO_GN_Sub_Final_Combine_Two_PartSL_Groups_At_SecondStep(mid_big_group,IN, LN, BN, GN_Int);
                                this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups_At_SecondStep(GN_Size);                                
                            }                           
      
                        }else{
                            //왼쪽결합에 성공하면 부분풀이모임들을 갱신한다. 
                            System.out.println("Left success! LN="+LN+", BN="+BN+", Down_MaxQ="+(int)Down_MaxQ+", MaxZ="+MFrame.Reservoir[ResNo].Cal_MaxZ);
                            this.CHAPSO_GN_Sub_Final_Combine_Two_PartSL_Groups_At_SecondStep(mid_big_group,IN,LN, BN, GN_Int);
                            this.CHAPSO_GN_Root_Combine_Two_Different_PartSL_Groups_At_SecondStep(GN_Size);
                        }
                    }
                }else{
                    if(BN<S_PartSL_N[IN]) {
                        BN++;
                        this.CHAPSO_GN_Combine_Two_Different_PartSL_Groups_At_SecondStep(IN,LN, BN,GN_Int,GN_Size);
                    }                      
                }
            }catch(Exception ex){
                //this.SIMPSO_GN_Optimal_Simulation_For_One_Reservoir(ResNo);
                ex.printStackTrace();
            }
        }

	public void CHAPSO_GN_Simulation_for_Reservoirs_by_One_Hour() {
            try {
                for(int j=1;j<=PN;j++) {
                    MFrame.Reservoir[ResNo].Time_GN[j]=F_gbestGN_Int[j];
                    MFrame.Reservoir[ResNo].Time_GT[j]=(j-1)*1*3600; 	   		
                }
                MFrame.Reservoir[ResNo].FullOpenGN = MFrame.Reservoir[ResNo].Time_GN[1];
                MFrame.Reservoir[ResNo].W_GN = MFrame.Reservoir[ResNo].Time_GN[1];
                int CN=(int)(MFrame.Reservoir[1].Time_SelN);
                for (int k = 1; k <= 2; k++) {
                MFrame.Reservoir[k].CountT = 0;
                MFrame.Reservoir[k].TotalN = CN;
                MFrame.Reservoir[k].FullOpenGN = MFrame.Reservoir[k].W_GN;
                }
                for (int k = 1; k <= CN; k++) {
                        MFrame.Reservoir[ResNo].FullOpenGN=MFrame.Reservoir[ResNo].Time_GN[k];
                        MFrame.Reservoir[ResNo].FloodControl_For_Reservoir_By_One_Hour();
                }	 
                for (int k = 1; k <= 2; k++) {
                     MFrame.Reservoir[k].Find_MaxZ_MaxQ_MinZ_MinQ();
                }                
                this.CHAPSO_GN_Calculate_Down_MaxQ();				
            }catch(Exception ex) {
                    ex.printStackTrace();
            }
	}
        
}
