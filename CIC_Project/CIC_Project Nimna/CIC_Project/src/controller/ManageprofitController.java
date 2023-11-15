package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import module.ProfitTable;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ManageprofitController implements Initializable {
    public JFXTextField fertilizerExpences;
    public JFXTextField plantExpences;
    public JFXTextField additionalCost;
    public JFXTextField EmployeeSalary;
    public JFXButton investDatasavebtn;
    public JFXComboBox selectArea;
    public TableColumn blockName;
    public TableColumn plantExpenses;
    public TableColumn additional_Cost;
    public TableColumn employeeSalary;
    public TableColumn fertilizerExpenses;
    public TableView profitTable;
    private Connection connection = DbConnection.getInstance().getConnection();


    public StackedBarChart stackedbarchatmanageProfit;

    public ManageprofitController() throws SQLException, ClassNotFoundException {
    }

    public void profitManageOnAction(Event event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBarChart();
        setComboBox1();
        try {
            populateProfitTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadBarChart(){
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            String query1 = "SELECT block_name,(SUM(stock_order.qty)*harvest_manage.price_per_kg)-((SUM(additionalCost))+(SUM(employee_salary))+(SUM(plant_Expenses))+(SUM(fertilizer_expenses))) FROM block JOIN harvest on block.block_id = harvest.block_id JOIN harvest_manage on harvest.full_harvest_id = harvest_manage.full_harvest_id JOIN stock on stock.stock_id = harvest.stock_id JOIN stock_order on stock.stock_id = stock_order.stock_id GROUP BY block.block_id;";

            XYChart.Series<String,Double> series = new XYChart.Series<>();
            series.setName("Profit");
            try {
                ResultSet rs = connection.createStatement().executeQuery(query1);
                while (rs.next()){
                    series.getData().add(new XYChart.Data<>(rs.getString(1),rs.getDouble(2)));
                }
            }catch (Exception e){

            }
            stackedbarchatmanageProfit.getData().add(series);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) throws SQLException {

        if (selectArea.getSelectionModel().getSelectedItem()==null || fertilizerExpences.getText().equals("") || plantExpences.getText().equals("") || additionalCost.getText().equals("") || EmployeeSalary.getText().equals("")){
            new Alert(Alert.AlertType.ERROR,"Fill All the fields").show();
        }else {

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE block SET fertilizer_expenses=?,employee_salary=?,additionalCost=?,plant_Expenses=? WHERE block_name=?");
            preparedStatement.setDouble(1, Double.parseDouble(fertilizerExpences.getText()));
            preparedStatement.setDouble(2, Double.parseDouble(EmployeeSalary.getText()));
            preparedStatement.setDouble(3, Double.parseDouble(additionalCost.getText()));
            preparedStatement.setDouble(4, Double.parseDouble(plantExpences.getText()));
            preparedStatement.setString(5, selectArea.getValue().toString());

            preparedStatement.executeUpdate();

            populateProfitTable();
            fertilizerExpences.setText(null);
            EmployeeSalary.setText(null);
            additionalCost.setText(null);
            plantExpences.setText(null);
            selectArea.setItems(null);

            setComboBox1();
        }
    }

    private void setComboBox1() {
        String[] selectType1 = {"Block A", "Block B", "Block C", "Block D", "Block E"};
        selectArea.setItems(FXCollections.observableArrayList(selectType1));
    }

    private void populateProfitTable() throws SQLException {

        blockName.setCellValueFactory(new PropertyValueFactory<ProfitTable,String>("blockName"));
        plantExpenses.setCellValueFactory(new PropertyValueFactory<ProfitTable,Double>("plant_expenses"));
        additional_Cost.setCellValueFactory(new PropertyValueFactory<ProfitTable,Double>("additional_costs"));
        employeeSalary.setCellValueFactory(new PropertyValueFactory<ProfitTable,Double>("employee_salary"));
        fertilizerExpenses.setCellValueFactory(new PropertyValueFactory<ProfitTable,Double>("fertilizer_Expenses"));

        ObservableList<ProfitTable> list = FXCollections.observableArrayList();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT block_name,plant_Expenses,additionalCost,employee_salary,fertilizer_expenses FROM block ");
        while (resultSet.next()) {
            list.add(new ProfitTable(resultSet.getString(1),resultSet.getDouble(2),resultSet.getDouble(3),resultSet.getDouble(4),resultSet.getDouble(5)));
        }
        profitTable.setItems(list);
    }
}
