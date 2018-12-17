package com.ygs;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class AntennaDrawer {

    AntennaDrawer(Canvas canvas, ArrayList<Result>results){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setLineWidth(3);
        double midlOfScreenX = canvas.getWidth()/2;
        double midlOfScreenY = canvas.getHeight()/2;
        for(Result result:results){
            double x1= (result.getX()*1000)+midlOfScreenX;
            double y1= (result.getY()*1000)+midlOfScreenY;
            double x2= (result.getX1()*1000)+midlOfScreenX;
            double y2= (result.getY1()*1000)+midlOfScreenY;
            gc.strokeLine(x1,y1,x2,y2);
        }
    }
}
