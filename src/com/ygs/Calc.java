package com.ygs;

import java.util.ArrayList;

public class Calc {
    //Начальное расстояние до центра от внутреннего витка
    private float startDist;
    private int angleStep;
    private float radius;
    private ArrayList<Result>results =new ArrayList<>();
    private double toRadian(double angle){
        return (Math.PI*angle/180f);
    }
    private void mirror(int curSize){
        for(int i=0;i<curSize;i++ ){
            Result res = results.get(i);
            results.add(new Result( res.getX()*-1,res.getY()*-1,res.getZ()*-1,res.getX1()*-1,res.getY1()*-1,res.getZ1()*-1,res.getRadius()));
        }
    }
    private void calcEdges(){
        int oneCurveSize=360/angleStep;
        int totalSize =results.size()/oneCurveSize;
        for(int i=0;i<totalSize;i++){
           for(int j=i+1;j<totalSize;j++){
               for(int z=0;z<oneCurveSize-1;z++){
                   Result res = results.get(z+i*oneCurveSize);
                   Result res1 =results.get(z+j*oneCurveSize);
                   results.add(new Result(res.getX1(),res.getY1(),res.getZ1(),res1.getX1(),res1.getY1(),res1.getZ1(),res.getRadius()));
               }
           }
        }
    }
    private void calcEnds(int curvesNumber){
        int oneCurveSize=360/angleStep;
        for(int i=0;i<curvesNumber-1;i++){
            for(int j=i+1;j<curvesNumber;j++){
                int lstIndex = oneCurveSize-1;
                    Result res = results.get(lstIndex+i*oneCurveSize);
                    Result res1 =results.get(lstIndex+j*oneCurveSize);
                    results.add(new Result(res.getX1(),res.getY1(),res.getZ1(),res1.getX1(),res1.getY1(),res1.getZ1(),res.getRadius()));
            }
        }
    }
    public  void clear(){
        results= new ArrayList<>();
    }
    private void calcCurve(double a,int side){
        for(float angle=0,angle_1=angleStep;angle_1<=360;angle+=angleStep,angle_1+=angleStep ){
            double protoRes =(startDist*Math.exp(a*angle))*side;
            double protoRes_1= (startDist*Math.exp(a*angle_1))*side;
            double radAngel = toRadian(angle);
            double radAngel_1 = toRadian(angle_1);
            results.add(new Result((protoRes*Math.cos(radAngel)),(protoRes*Math.sin(radAngel)),0,
                    (protoRes_1*Math.cos(radAngel_1)),(protoRes_1*Math.sin(radAngel_1)),0,radius));
        }
        System.out.println("CurvSize"+results.size());
    }
    public Calc(float startDist,int angleStep,float radius,final String ANTENNA_METRIC,final String WIRE_METRIC){
        int metricCofAntenna =0;
        int metricCofWire =0;
        switch (ANTENNA_METRIC){
            case "mm":metricCofAntenna=1000;
            break;
            case "cm":metricCofAntenna=100;
        }
        switch(WIRE_METRIC){
            case "mm":metricCofWire=1000;
                break;
            case "cm":metricCofWire=100;
        }
        this.startDist=startDist/metricCofAntenna;
        this.radius = radius/metricCofWire;
        this.angleStep=angleStep;
    }

    public ArrayList<Result> calcAntenna(double a[]){
        for(double param:a){
            System.out.println(param);
            calcCurve(param,1);
        }
        calcEdges();
        calcEnds(a.length);
        System.out.println("before mirior size"+results.size());
        mirror(results.size());
        System.out.println("after mirior size"+results.size());
        return results;
    }
    public ArrayList<Result> getResults() {
        return results;
    }
}
