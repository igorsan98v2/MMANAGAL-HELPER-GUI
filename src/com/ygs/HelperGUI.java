package com.ygs;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.IOException;

public class HelperGUI extends Application {
    private Parent root;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try{
        //    root = FXMLLoader.load(getClass().getResource("geneticModelLayout.fxml"));
            root =FXMLLoader.load(getClass().getResource("layout.fxml"));
            primaryStage.setTitle("MMANAGAL HELPER");
            primaryStage.setScene(new Scene(root, 1200, 720));
            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
