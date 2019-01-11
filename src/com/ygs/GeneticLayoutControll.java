package com.ygs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class GeneticLayoutControll {
    private String path;
    @FXML
    private TextField speciesNumField;

    @FXML
    private TextField epochNumField;

    @FXML
    private TextField mutantRateField;

    @FXML
    private TextField rField;

    @FXML
    private TextField jXField;

    @FXML
    private TextField a_minField;

    @FXML
    private TextField a_maxField;

    @FXML
    private TextField interval_minField;

    @FXML
    private TextField interval_maxField;

    @FXML
    private TextField distance_minField;

    @FXML
    private TextField distance_maxField;

    @FXML
    private TextField curvesNumField;

    @FXML
    private CheckBox verticalCheck;

    @FXML
    private TextField angleStepField;

    @FXML
    private Label pathText;

    @FXML
    private Button pathButton;

    @FXML
    private Button calcButton;

    @FXML
    private Canvas canvas;

    @FXML
    void onDrag(DragEvent event) {

    }

    @FXML
    void setPath(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Stage  stage = new Stage();
        File file= fileChooser.showOpenDialog(stage);
        if(file!=null){
            pathText.setText(file.getAbsolutePath());
            path = file.getAbsolutePath();
        }
    }
    @FXML
    void startResearch(ActionEvent event) {
        int population  = Integer.parseInt(speciesNumField.getText());
        int epoch = Integer.parseInt(epochNumField.getText());
        int mutateRate = Integer.parseInt(mutantRateField.getText());
        float R = Float.parseFloat(rField.getText());
        float jX = Float.parseFloat(jXField.getText());
        float aMin = Float.parseFloat(a_minField.getText());
        float aMax = Float.parseFloat(a_maxField.getText());
        float intervalMin = Float.parseFloat(interval_minField.getText());
        float intervalMax = Float.parseFloat(interval_maxField.getText());
        float distanceMin = Float.parseFloat(distance_minField.getText());
        float distanceMax = Float.parseFloat(distance_maxField.getText());
        int curvesNum = Integer.parseInt(curvesNumField.getText());
        boolean isVert =  verticalCheck.isSelected();
        int angleStep = Integer.parseInt(angleStepField.getText());
        try {
            Process mmanagal =new ProcessBuilder(path).start();
            Thread.sleep(500);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
       // new GeneticAlgoritm(population,epoch,mutateRate,curvesNum,R,jX,3,3).selection(800,4000,100);
        GeneticAlgoritm geneticAlgorithm =  new GeneticAlgoritm(population,mutateRate,epoch,R,jX,3,3,aMin,aMax,intervalMin,intervalMax,distanceMin,distanceMax,curvesNum,angleStep,isVert);
        geneticAlgorithm.selection(800,4000,100);

    }
}

