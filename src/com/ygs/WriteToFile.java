package com.ygs;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;

public class WriteToFile {
    ArrayList<Result>results;
    WriteToFile(float startDist,int angleStep,float radius,final String ANTENNA_METRIC,final String WIRE_METRIC,double []a){
        write("","echo",startDist,angleStep,radius,ANTENNA_METRIC,WIRE_METRIC,a);
    }
    WriteToFile(String path,String name,float startDist,int angleStep,float radius,final String ANTENNA_METRIC,final String WIRE_METRIC,double []a){
        if(name==null||name==""){
            name = "echo";
        }
        if(path==null){
            path="";
        }
        write(path,name,startDist,angleStep,radius,ANTENNA_METRIC,WIRE_METRIC,a);
    }
    private void write(String path,String name,float startDist,int angleStep,float radius,final String ANTENNA_METRIC,final String WIRE_METRIC,double []a){
        Calc antenna =   new Calc(startDist,angleStep,radius,ANTENNA_METRIC,WIRE_METRIC);
        ArrayList<Result> results = antenna.calcAntenna(a);

        PrintWriter writer = null;
        Locale.setDefault(Locale.US);

        try {
            writer  = new PrintWriter(path+name+".maa", "UTF-8");
            writer.println("ANTENNA ");
            writer.println("*");
            writer.println("14.5");
            writer.println( "***Wires***");
            writer.println(results.size());
            for(Result result:results) {

                String output = String.format("%.5f,\t%.5f,\t%.1f,\t%.5f,\t%.5f,\t0.0,\t%.5f,\t-1",result.getX(), result.getY(),result.getZ(), result.getX1(), result.getY1(), result.getRadius());
                writer.println(output);
            }
            writer.println("*** Source ***");
            writer.println("1,\t1");
            writer.println("w1c,\t0.0,\t1.0");
            writer.println("*** Load ***");
            writer.println("0,\t0");




            writer.println("***Segmentation***");
            writer.println("800,\t80,\t2.0,\t2");
            writer.println("***G/H/M/R/AzEl/X***");
            writer.println("2,\t1.5,\t1,\t50.0,\t120,\t60,\t0.0");




            writer.close();
            System.out.println("closet");
        }
        catch (Exception e){
            if (writer!=null||true) writer.close();
            e.printStackTrace();
        }

        writer.close();
        this.results = results;
    }

    public ArrayList<Result> getResults() {
        return results;
    }
}
