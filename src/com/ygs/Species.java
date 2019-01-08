package com.ygs;

public class Species implements Comparable<Species>{
    private Gene gene;
    private float r=0;
    private float jX=0;
    private float rate=0;
    private float comRate=0;
    public Species(float a,int coilNum,float d,int angle,int angleStep,float distance){
        gene = new Gene(a,coilNum,d,angle,angleStep,distance);

    }

    public Species(Gene gene){
        this.gene = gene;
    }
    public Species(Gene perent,Gene perent1){
        gene = new Gene(perent.getA(),perent.getCoilNum(),perent1.getD(),perent.getAngle(),perent1.getAngleStep(),perent.getDistance());


    }
    private void calc(int frequency){

        double[]a = new double[gene.getCoilNum()];
        WriteToFile write =new WriteToFile(gene.getDistance(),gene.getAngleStep(),gene.getRadius(),"mm","mm",a,gene.getD(),frequency);
        //float startDist,int angleStep,float radius,final String ANTENNA_METRIC,final String WIRE_METRIC,double []a,float a_dist,int frequency
        WINAPIController winapiController =  new WINAPIController("MMANA-GAL basic");
        winapiController.makeResearch();
        r=winapiController.getResistance();
        jX=winapiController.getjX();
    }
    public Gene getGene(){
        return gene;
    }
    public float getjX() {
        return jX;
    }

    public float getR() {
        return r;
    }

    public float calcRate(float targetR ,float targetJx,int frequency) {
        calc(frequency);
        rate =  Math.abs((targetJx+targetR)-(Math.abs(jX)+Math.abs(r)));
        comRate +=rate;
        return rate;
    }
    public float getComRate() {

        return comRate;
    }
    @Override
    public int compareTo(Species species) {
        float compareRate = ((Species) species).getComRate();

        //ascending order
        return Math.round(this.comRate - compareRate);

    }
}
