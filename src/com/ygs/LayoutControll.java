package com.ygs;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class LayoutControll {
    boolean isInited =false;
    @FXML
    private Canvas canvas;
    @FXML
    private Label coil_space_metric;
    @FXML
    private Label wire_r_metric;
    @FXML
    private TextField a_e;

    @FXML
    private TextField a_s;

    @FXML
    private Spinner<Integer> a_c;

    @FXML
    private Spinner<Integer> angl_step;

    @FXML
    private TextField coil_space;

    @FXML
    private ChoiceBox<String> coil_metric;

    @FXML
    private TextField wire_r;

    @FXML
    private ChoiceBox<String> wire_metric;

    @FXML
    private Button calcButton;
    @FXML
    void changeMetricCoil(ActionEvent event) {
        coil_space_metric.setText(coil_metric.getValue());
    }
    @FXML
    void changeMetricWire(ActionEvent event) {
        wire_r_metric.setText(wire_metric.getValue());
    }
    @FXML
    void calc(ActionEvent event) {
        int aSize = a_c.getValue();
        float aStart =Float.parseFloat( a_s.getText());
        float aEnd = Float.parseFloat(a_e.getText());
        double []a =new double[aSize];
        double aStep = (aEnd-aStart)/aSize;
        double aIter =aStart;
        for(int i=0;i<aSize;i++){
            a[i]=aIter;
            aIter+=aStep;
        }
        String coilMetric = transMetric( coil_metric.getValue());
        float startDist =Float.parseFloat( coil_space.getText());
        int angleStep = angl_step.getValue();
        String wireMetric= transMetric(wire_metric.getValue());
        float wireR = Float.parseFloat( wire_r.getText());

        WriteToFile write = new WriteToFile(startDist,angleStep,wireR,coilMetric,wireMetric,a);
        ArrayList<Result> results = write.getResults();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
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

    private String transMetric(String metric){
        String []res={"mm","cm"};
        switch (metric){
            case "мм":return res[0];
            case "см":return res[1];
        }
        return "m";
    }
}
