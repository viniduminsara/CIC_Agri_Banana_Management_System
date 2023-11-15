package controller;

import animatefx.animation.AnimationFX;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.Financial_OfficerDashboard.FinancialOfficerDashboardController;
import controller.crop_HarvestorDashboard.HarvestorDashboardController;
import controller.face.UnlockingFormController;
import controller.manager.DashBoardFormController;
import db.DbConnection;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import module.User;
import rex.utils.S;
import service.QrPerformance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInFormController implements QrPerformance {
    public Label lblError;

    public AnchorPane logInFormContext;
    public JFXPasswordField userPassword;
    public JFXTextField logIn_email;
    private static Pattern emailPattern;
    public User user;
    private Stage stage;


    private Connection connection;

    public void btnSignIn(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        LoginFormManager();
    }

    public void btnSignUp(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) logInFormContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/SignUpForm.fxml"))));
        window.setResizable(false);
    }

    private void LoginFormManager() throws SQLException, ClassNotFoundException, IOException {

        String userInputEmail = logIn_email.getText();
        String userInputPassword = userPassword.getText();
        String accountType;

        connection = DbConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select gmail_id,password,account_type,full_name from admin ");
        ResultSet resultSet = preparedStatement.executeQuery();

        boolean op = resultSet.next();
        int count=0;
        //Input Chek
        if (userInputEmail.equals("")){new animatefx.animation.Bounce(logIn_email).play();}
        if (userInputPassword.equals("")){new animatefx.animation.Bounce(userPassword).play();}
        if (userInputEmail.equals("")|| userPassword.getText().equals("")) {
            count++;

        } else {
            while (op) {
                //email chek
                if (resultSet.getString(1).equals(userInputEmail)) {
                    count++;
                    accountType=resultSet.getString(3);
                        //password chek ste......
                        if (resultSet.getString(2).equals(userInputPassword)) {
                            System.out.println(accountType);
                            System.out.println(resultSet.getString(4));
                            user=new User(resultSet.getString(4));
                            switch (accountType){
                                case "Manager":managelodeStage();
                                    break;
                                case "Crop Harvestor":harvestlodeStage();
                                    break;
                                case "Financial Officer":financiallodeStage();

                                    break;
                            }
                            op = false;

                        } else {
                            new Alert(Alert.AlertType.ERROR, "Invalid password").show();
                            op = false;
                        }

                } else {
                        op = resultSet.next();
                }
            }

        }
        if (count==0)new Alert(Alert.AlertType.ERROR, "Un Register User").show();
    }
    public static boolean emailValidator(String email){
        emailPattern = Pattern.compile("^([a-z|0-9]{3,})[@]([a-z]{2,})\\.(com|lk)$");
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }
    public void managelodeStage() throws IOException {
//        Stage sensor = new Stage();
//        sensor.setScene(new Scene(FXMLLoader.load(getClass().getResource("../controllerPhandWater/phandwaterControle.fxml"))));
//        sensor.setTitle("Sensor Control");
//        sensor.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ManagerDashboard/DashBoardForm.fxml"));
        Parent pane = (Parent) loader.load();
        DashBoardFormController controller = loader.getController();
        Stage window = (Stage) logInFormContext.getScene().getWindow();
        window.setScene(new Scene(pane));
        window.setResizable(true);
        window.centerOnScreen();
        controller.setUserName(user);
//        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/ManagerDashboard/DashBoardForm.fxml"))));


    }
    public void financiallodeStage() throws IOException {
//        Stage sensor = new Stage();
//        sensor.setScene(new Scene(FXMLLoader.load(getClass().getResource("../controllerPhandWater/phandwaterControle.fxml"))));
//        sensor.setTitle("Sensor Control");
//        sensor.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Financial_OfficerDashboard/Financial_OfficerDashboard.fxml"));
        Parent pane2 = (Parent) loader.load();
        FinancialOfficerDashboardController controller = loader.getController();
        Stage window = (Stage) logInFormContext.getScene().getWindow();
        window.setScene(new Scene(pane2));
        window.setResizable(true);
        window.centerOnScreen();
        controller.setUserName(user);
//        Stage window = (Stage) logInFormContext.getScene().getWindow();
//        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/Financial_OfficerDashboard/Financial_OfficerDashboard.fxml"))));
    }public void harvestlodeStage() throws IOException {
//        Stage sensor = new Stage();
//        sensor.setScene(new Scene(FXMLLoader.load(getClass().getResource("../controllerPhandWater/phandwaterControle.fxml"))));
//        sensor.setTitle("Sensor Control");
//        sensor.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/crop_HarvestorDashboard/HarvestorDashboard.fxml"));
        Parent pane3 = (Parent) loader.load();
        HarvestorDashboardController controller = loader.getController();
        Stage window = (Stage) logInFormContext.getScene().getWindow();
        window.setScene(new Scene(pane3));
        window.setResizable(true);
        window.centerOnScreen();
        controller.setUserName(user);
//        Stage window = (Stage) logInFormContext.getScene().getWindow();
//        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/crop_HarvestorDashboard/HarvestorDashboard.fxml"))));
    }

    public void btnFaceOnAction(ActionEvent actionEvent) throws Throwable {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/face/UnlockingForm.fxml"));
        Parent load = null;
        try {
            load = fxmlLoader.load();
        } catch (Exception e) {
            finalize();
            return;
        }
        UnlockingFormController controller = fxmlLoader.getController();
        controller.setController(this);
        Scene scene  = new Scene(load);
        stage  = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(logIn_email.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.btnStopOnAction(null);

            }
        });
    }

    @Override
    public void qrIdRequestAction(String id) {
        stage.close();
        new Alert(Alert.AlertType.INFORMATION,"Face Unlocking Successfully.").show();
        user = new User("Perera");
        try {

            managelodeStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStudentDetail(String id) {
        return null;
    }
}