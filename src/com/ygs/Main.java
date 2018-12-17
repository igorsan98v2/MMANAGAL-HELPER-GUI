package com.ygs;

import java.io.PrintWriter;
import java.text.Format;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void enter(int start,int end,char args[]){
        for (char arg:args) {
            for(int i=start;i<=end;i+=2){
                System.out.printf("=%c%d\n",arg,i);
            }
        }
    }
    public static void main(String[] args) {
	// write your code here
        Calc antenna =   new Calc(21,25,15,"mm","mm");
        double []a= {0.00609234622,0.007,0.008,0.009,0.01};
        ArrayList<Result>results = antenna.calcAntenna(a);

        PrintWriter  writer = null;
        Locale.setDefault(Locale.US);

        try {
            writer  = new PrintWriter("echo.maa", "UTF-8");
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


    }
}
