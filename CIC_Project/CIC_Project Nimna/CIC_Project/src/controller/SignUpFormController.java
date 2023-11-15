package controller;

import com.jfoenix.controls.JFXTextField;
import controller.face.UnlockingFormController;
import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.QrPerformance;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SignUpFormController implements Initializable, QrPerformance {

    public AnchorPane signUpFormContext;
    public JFXTextField nametex;
    public JFXTextField passwordtext;
    public JFXTextField rpassword;
    public Label repasswordLabel;
    public JFXTextField email;
    public ComboBox sigeUpFromComboBox;
    private Connection connection;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] ar = {"Manager","crop Harvestor","Financial Officer"};
        sigeUpFromComboBox.setItems(FXCollections.observableArrayList(ar));
    }
    public void btnSignUPin(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage)  signUpFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LogInForm.fxml"))));
    }
    public void registerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if(nametex.getText().equals("")||
        passwordtext.getText().equals("")||
        rpassword.getText().equals("") ||
        sigeUpFromComboBox.getSelectionModel().getSelectedIndex()==-1||
        email.getText().equals("")){
            new Alert(Alert.AlertType.ERROR,"Fill All The Fields With Valid Details").show();
        }else {
            setDatabaseToData();
            new Alert(Alert.AlertType.INFORMATION, "Register is Sucessfully").show();
            nametex.setText("");
            passwordtext.setText("");
            rpassword.setText("");
            email.setText("");
        }



    }
    public void setDatabaseToData() throws SQLException, ClassNotFoundException {
        connection=DbConnection.getInstance().getConnection();
        PreparedStatement ps=connection.prepareStatement("insert into admin values (?,?,?,?)");
        ps.setObject(1,email.getText());
        ps.setObject(2,nametex.getText());
        ps.setObject(3,passwordtext.getText());
        ps.setObject(4,sigeUpFromComboBox.getSelectionModel().getSelectedItem());
        ps.executeUpdate();
    }

    public void faceRegistrationOnAction(ActionEvent actionEvent){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/face/UnlockingForm.fxml"));
        Parent load = null;
        try {
            load = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UnlockingFormController controller = fxmlLoader.getController();
        controller.setController(this);
        Scene scene  = new Scene(load);
        stage  = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(nametex.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void qrIdRequestAction(String id) {
        stage.close();
        new Alert(Alert.AlertType.INFORMATION,"Face Registration Sucessful!").show();
    }

    @Override
    public String getStudentDetail(String id) {
        return null;
    }
}
