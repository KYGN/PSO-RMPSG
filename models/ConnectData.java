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
public class ConnectData {
    public int UpN;
    public String FStrUpT;
    public double Huichon_InQ[];
    public double Jangsu_InQ[];
    public double Daegak_InQ[];
    public double Sunchon_InQ[];
    public double Songchon_InQ[];
    public double Bonghua_InQ[];
    public double Mirim_InQ[];
    public String InQFile;
    public boolean Flag=false;
    public void Input_InQData_From_NotePad(String FileName){
        try{
            BufferedReader reader = new BufferedReader
                                    (new FileReader(new File(
                                            FileName)));
            String T_1 = reader.readLine();
            T_1=reader.readLine();
            UpN=Integer.parseInt(T_1);
            Huichon_InQ=new double[UpN+1];
            Jangsu_InQ=new double[UpN+1];
            this.Daegak_InQ=new double[UpN+1];
            this.Sunchon_InQ=new double[UpN+1];
            this.Songchon_InQ=new double[UpN+1];
            this.Bonghua_InQ=new double[UpN+1];
            this.Mirim_InQ=new double[UpN+1];
            T_1=reader.readLine();
            FStrUpT=T_1;
            T_1=reader.readLine();
            for(int i=1;i<=UpN;i++){
                T_1=reader.readLine();
                StringTokenizer st = new StringTokenizer(T_1, "\t");
                Huichon_InQ[i]=Integer.parseInt(st.nextToken());
                Jangsu_InQ[i]=Integer.parseInt(st.nextToken());
                Daegak_InQ[i]=Integer.parseInt(st.nextToken());
                Sunchon_InQ[i]=Integer.parseInt(st.nextToken());
                Songchon_InQ[i]=Integer.parseInt(st.nextToken());
                Bonghua_InQ[i]=Integer.parseInt(st.nextToken());
                Mirim_InQ[i]=Integer.parseInt(st.nextToken());
            }
            Flag=true;
        }catch(Exception ex){
            Flag=false;
            ex.printStackTrace();
        }
    }  

    public void Set_Parameters_To_Reservoirs(){
        MFrame.Reservoir[1].Name="Huichon";
        MFrame.Reservoir[2].Name="Jangsu";
        MFrame.Reservoir[1].FloodZ=197;
        MFrame.Reservoir[2].FloodZ=125.5;
        MFrame.Reservoir[1].DieZ=166;
        MFrame.Reservoir[2].DieZ=90;
        MFrame.Reservoir[1].NormalZ=196;
        MFrame.Reservoir[2].NormalZ=121.5;
        
        MFrame.Reservoir[1].GateN=14;
        MFrame.Reservoir[1].Gate_W=10;
        MFrame.Reservoir[1].Gate_H=4;
        MFrame.Reservoir[1].Z_Sill=181;
        
        MFrame.Reservoir[2].Gate_W=8;
        MFrame.Reservoir[2].Gate_H=8;
        MFrame.Reservoir[2].Z_Sill=98;
        MFrame.Reservoir[2].GateN=6;
        MFrame.Reservoir[1].Input_VZData_From_NotePad("D:\\MyWork\\04_Work_Java\\PSO-RMPSG\\data\\huichon_vz.txt");
        MFrame.Reservoir[2].Input_VZData_From_NotePad("D:\\MyWork\\04_Work_Java\\PSO-RMPSG\\data\\jangsu_vz.txt");
    }
}
