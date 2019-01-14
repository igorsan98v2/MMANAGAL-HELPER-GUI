package com.ygs;

import java.util.ArrayList;

public class Calc {
    //Начальное расстояние до центра от внутреннего витка
    private float startDist;
    private int angleStep;
    private float radius;
    private double dif;
    private float difStep;
    private boolean isEnabled;
    private ArrayList<Result>results =new ArrayList<>();
    private ArrayList<Integer>endsPos = new ArrayList<>();
    private ArrayList<Integer>startPos = new ArrayList<>();
    private double toRadian(double angle){
        return (Math.PI*angle/180f);
    }
    private void mirror(int curSize){
        for(int i=0;i<curSize;i++ ){
            Result res = results.get(i);
            results.add(new Result( res.getX()*-1,res.getY()*-1,res.getZ()*-1,res.getX1()*-1,res.getY1()*-1,res.getZ1()*-1,res.getRadius()));
        }
    }
    private void calcEdges(boolean isCalc){
        System.out.println("all curves:"+endsPos.size());
        /*if(results.size()>1){
            int oneCurvSize= endsPos.get(0);
            for(int j=0;j<endsPos.size()-1;j++){
                for(int i =endsPos.get(j);i<endsPos.get(j+1);i++){
                    if(endsPos.get(j)+i<results.size()) {
                        Result res = results.get(i);
                        Result res1 = results.get((endsPos.get(j) + i));
                        results.add(new Result(res.getX1(), res.getY1(), res.getZ1(), res1.getX1(), res1.getY1(), res1.getZ1(), res.getRadius()));
                    }
                }
            }
        }*/
        if(isCalc){
            if(results.size()>1){
                for(int i=0;i<startPos.size()-1;i++){
                    for(int j=startPos.get(i),z= endsPos.get(i);j<endsPos.get(i);j++,z++){
                        if(z<results.size()) {
                            Result res = results.get(j);
                            Result res1 = results.get(z);
                            results.add(new Result(res.getX1(), res.getY1(), res.getZ1(), res1.getX1(), res1.getY1(), res1.getZ1(), res.getRadius()));
                        }

                    }
                }
            }
        }
    }
    private void calcEdges(){
        int oneCurveSize=(360*2)/angleStep;
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
        int oneCurveSize=(360*2)/angleStep;
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
    private void calcCurve(double a,int side,float curDif){
        startPos.add(results.size());
        for(float angle=0,angle_1=angleStep;angle_1<=360*2;angle+=angleStep,angle_1+=angleStep ){
            double protoRes =(startDist*Math.exp(a*(angle-curDif)))*side;
            double protoRes_1= (startDist*Math.exp(a*(angle_1-curDif)))*side;
            double radAngel = toRadian(angle);
            double radAngel_1 = toRadian(angle_1);
            results.add(new Result((protoRes*Math.cos(radAngel)),(protoRes*Math.sin(radAngel)),0,
                    (protoRes_1*Math.cos(radAngel_1)),(protoRes_1*Math.sin(radAngel_1)),0,radius));
        }
        endsPos.add(results.size());
        System.out.println("CurvSize"+results.size());
    }
    public Calc(float startDist,int angleStep,float radius,boolean isEnabled,final String ANTENNA_METRIC,final String WIRE_METRIC){
        int metricCofAntenna =0;
        int metricCofWire =0;
        this.isEnabled = isEnabled;
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

    public ArrayList<Result> calcAntenna(double a[],float dif){
        float difSize = a.length;
        this.dif = dif;
        difStep=dif/difSize;
        int i=0;/*
        for(double param:a){
            System.out.println(param);

            calcCurve(param, 1, 0);

            i++;

        }*/
        System.out.println("real near by curvs:"+a.length);
        for(double param:a){
            calcCurve(a[0], 1, i*difStep);
            i++;
        }
        calcEdges(isEnabled);
       // calcEdges();
        //calcEnds(a.length);
        System.out.println("before mirior size"+results.size());
        mirror(results.size());
        System.out.println("after mirior size"+results.size());
        return results;
    }
    public ArrayList<Result> getResults() {
        return results;
    }
}
