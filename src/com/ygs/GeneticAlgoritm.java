package com.ygs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class GeneticAlgoritm {
    private int mutateRate;
    private int epochNum;
    private float targetR;
    private float targetJx;
    private Species[]speciesArr;
    private int topBest ;
    private int topWorst;
    private float aMin;
    private float aMax;
    private float intervalMin;
    private float intervalMax;
    private float distanceMin;
    private float distanceMax;
    private int curvsNum;
    private int angleStep;
    private boolean isVertEnabled = false;
    Random random = new Random();
    private  int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }


        return random.nextInt((max - min) + 1) + min;
    }
    private  float getRandomNumberInRange(float min, float max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return min + random.nextFloat() * (max - min );

    }


    public GeneticAlgoritm(int speciesNum,int mutateRate, int epochNum, float targetR, float targetJx, int topBest, int topWorst, float aMin, float aMax, float intervalMin, float intervalMax, float distanceMin, float distanceMax, int curvsNum,int angleStep, boolean isVertEnabled) {
        speciesArr = new Species[speciesNum];
        this.mutateRate = mutateRate;
        this.epochNum = epochNum;
        this.targetR = targetR;
        this.targetJx = targetJx;
        this.topBest = topBest;
        this.topWorst = topWorst;
        this.aMin = aMin;
        this.aMax = aMax;
        this.intervalMin = intervalMin;
        this.intervalMax = intervalMax;
        this.distanceMin = distanceMin;
        this.distanceMax = distanceMax;
        this.curvsNum = curvsNum;
        this.isVertEnabled = isVertEnabled;
        this.angleStep = angleStep;
    }

    public  GeneticAlgoritm(int speciesNum, int epochNum, int mutateRate, int spiralNum, float targetR, float targetJx, int topBest, int topWorst){
        speciesArr = new Species[speciesNum];
        this.mutateRate = mutateRate;
        this.epochNum = epochNum ;
        this.targetJx=targetJx;
        this.targetR=targetR;
        this.topBest = topBest;
        this.topWorst = topWorst;
    }
    private Species speciesFabric(){

        float a = (float)(getRandomNumberInRange(Math.round(aMin/0.0001f),Math.round(aMax/0.0001f))*0.0001);
        float d = (float) getRandomNumberInRange(Math.round(intervalMin*100),Math.round(intervalMax*100))/100;
        float distance = (float)  getRandomNumberInRange(Math.round(distanceMin*100),Math.round(distanceMax*100))/100;
        System.out.println("a:"+a);
        System.out.println("d:"+d);
        System.out.println("distance:"+distance);
        Gene gene = new Gene(a,curvsNum,d,720,angleStep,distance);

        return new Species(gene);
    }
    private void initArr(){
        for (int i=0;i<speciesArr.length;i++){
            speciesArr[i] = speciesFabric();
        }
    }
    private Species cross(Species species,Species species1){
        return new Species(species.getGene(),species1.getGene());
    }
    private Species mutate(Species mutant){
        System.out.println("mutate");
        float a = (getRandomNumberInRange(-aMin,aMin)/2)+mutant.getGene().getA();
        float d = (getRandomNumberInRange(-intervalMin,intervalMin)/2)+mutant.getGene().getD();
        float distance = (getRandomNumberInRange(-distanceMin,distanceMin)/2)+mutant.getGene().getDistance();
        Gene gene = new Gene(a,curvsNum,d,720,angleStep,distance);
        return new Species(gene);
    }
    public void selection(int startFreq ,int endFreq,int freqStep){
        initArr();
        for(int i=0;i<epochNum;i++){

            int iters=0;
            for(int freq = startFreq;freq<endFreq;freq+=freqStep){
                calc(freq);
                iters++;
            }
            for(int z=0;z<speciesArr.length;z++){
                speciesArr[z].computeComRate(iters);

            }
            Arrays.sort(speciesArr);
            for(int z=0;z<speciesArr.length;z++){
                speciesArr[z].setComRateZero();
            }
            int length = speciesArr.length;
            for(int j=0;i<topWorst;j++){
                if(getRandomNumberInRange(0,100)>mutateRate){
                    int partner = getRandomNumberInRange(0,topBest);
                    int partner1 = getRandomNumberInRange(topBest,length-1-topWorst);
                    speciesArr[length-1-topWorst]=  cross(speciesArr[partner],speciesArr[partner1]);
                }
                else {

                    speciesArr[length-1-j]= mutate(speciesArr[length-1-j]);
                }
            }
            writeToLog(i);
            if(i>5){
                if(speciesArr[0].getComRate()/i<50){
                    break;
                }
            }
        }
        speciesArr[0].calcRate(targetR,targetJx,3000);
    }

    private void calc(int freq){
        for(int i=0;i<speciesArr.length;i++){
            speciesArr[i].calcRate(targetR,targetJx,freq);

        }
        //место где сортировка стояла прежде
    }
    private void writeToLog(int epochNum){
        String filePath = "epoch.log";
        String text = String.format("\nepoch:%d\t",epochNum );
        try {
            Files.write(Paths.get(filePath), text.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println(e);
        }

        for(Species species: speciesArr) {
            text = String.format("\na:%f\tinterval:%f\tdistance:%f\tR:%f Omh\tjX:%f Omh",species.getGene().getA(),species.getGene().getD(),species.getGene().getDistance(),species.getR(),species.getjX());
            try {
                Files.write(Paths.get(filePath), text.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

    }
}
