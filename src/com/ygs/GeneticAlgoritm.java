package com.ygs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GeneticAlgoritm {
    private int mutateRate;
    private int epochNum;
    private float targetR;
    private float targetJx;
    private Species[]speciesArr;
    private int topBest ;
    private int topWorst;
    Random random = new Random();
    private  int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }


        return random.nextInt((max - min) + 1) + min;
    }

    public  GeneticAlgoritm(int speciesNum,int epochNum,int mutateRate,float targetR,float targetJx,int topBest,int topWorst){
        speciesArr = new Species[speciesNum];
        this.mutateRate = mutateRate;
        this.epochNum = epochNum ;
        this.targetJx=targetJx;
        this.targetR=targetR;
        this.topBest = topBest;
        this.topWorst = topWorst;
    }
    private Species speciesFabric(){

        float a = (float)(getRandomNumberInRange(20,100)*0.0001);
        float d = (float) getRandomNumberInRange(20,180);
        float distance = (float) getRandomNumberInRange(5,10);
        System.out.println("a:"+a);
        System.out.println("d:"+d);
        System.out.println("distance:"+distance);
        Gene gene = new Gene(a,4,d,720,25,distance);

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
        float a = (float)(getRandomNumberInRange(-25,25)*0.0001)+mutant.getGene().getA();
        float d = (float) getRandomNumberInRange(-125,125)+mutant.getGene().getD();
        float distance = (float) getRandomNumberInRange(-20,20)+mutant.getGene().getDistance();
        Gene gene = new Gene(a,4,d,720,30,distance);

        return new Species(gene);
    }
    public void selection(int startFreq ,int endFreq,int freqStep){
        for(int i=0;i<epochNum;i++){
            initArr();
            for(int freq = startFreq;freq<endFreq;freq+=freqStep){
                calc(freq);
            }
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
        Arrays.sort(speciesArr);
        int length = speciesArr.length;
        for(int i=0;i<topWorst;i++){
            if(getRandomNumberInRange(0,100)>mutateRate){
                int partner = getRandomNumberInRange(0,topBest);
                int partner1 = getRandomNumberInRange(topBest,length-1-topWorst);
                speciesArr[length-1-topWorst]=  cross(speciesArr[partner],speciesArr[partner1]);
            }
            else {

                speciesArr[length-1-i]= mutate(speciesArr[length-1-i]);
            }
        }

    }

}
