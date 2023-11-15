package controllerPhandWater;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class PhandwaterControle implements Initializable {

    public JFXComboBox comboBox1;
    public JFXComboBox comboBox2;
    public JFXSlider phSlider;
    public JFXSlider humiditySlider;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String [] block={"Block A","Block B","Block C","Block D","Block E"};
        comboBox1.setItems(FXCollections.observableArrayList(block));
        comboBox2.setItems(FXCollections.observableArrayList(block));
        /*phSlider.setL*/
    }
}
