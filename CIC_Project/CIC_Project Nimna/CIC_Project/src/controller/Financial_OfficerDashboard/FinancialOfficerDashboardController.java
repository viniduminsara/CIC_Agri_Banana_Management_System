package controller.Financial_OfficerDashboard;

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

public class FinancialOfficerDashboardController implements Initializable {
    public AnchorPane loadFormContext;
    public Label nameLabel;

    public void setUserName(User user) {
        nameLabel.setText(user.getName());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            home();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void home () throws IOException {
        URL resource = getClass().getResource("../../view/HomeForm.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }
    public void btnDash(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/HomeForm.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }

    public void dashBoardProfitOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/Manageprofit.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }

    public void mainStockBtn(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("../../view/StockManagefrom.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContext.getChildren().clear();
        loadFormContext.getChildren().add(load);
    }


}
