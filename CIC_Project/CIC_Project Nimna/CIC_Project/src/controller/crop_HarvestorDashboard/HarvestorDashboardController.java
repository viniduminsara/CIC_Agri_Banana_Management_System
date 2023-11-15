package controller.crop_HarvestorDashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import module.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HarvestorDashboardController implements Initializable{

    public AnchorPane loadFormContext;
    public Label nameLabel;

    public void setUserName(User user) {
        nameLabel.setText(user.getName());

    }

    public void btnDash(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/HomeForm.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }
    private void home () throws IOException {
        URL resource = getClass().getResource("../../view/HomeForm.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }

    public void btnBlock(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/blokForm.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }

    public void dashboardHarvestOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/HarvestFrom.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            home();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
