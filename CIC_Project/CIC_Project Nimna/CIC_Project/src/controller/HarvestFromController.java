package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import module.Harvest;
import module.HarvestSummary;

import java.sql.*;

public class HarvestFromController {

    public JFXComboBox comboBox1;
    public JFXTextField inputtext1;
    public JFXTextField inputtext2;
    public JFXButton plantmaintainbtn;
    public JFXDatePicker datepicker;
    public JFXTextField pricePerKg;
    public Label fullharvestlabel;
    public Label wastageharvestlabel;
    public Label remainingharvestlabel;
    public LineChart lineChart;
    public PieChart harvestPieChart;

    private Connection  connection = DbConnection.getInstance().getConnection();

    private Statement statement;

    private ResultSet resultSet;

    private PreparedStatement preparedStatement;

    private Double blockFullHarvest=0.0;

    private Double blockWastageHarvest=0.0;

    private Double blockRemainingHarvest=0.0;

    @FXML
    private TableView<Harvest> harvestTable;

    @FXML
    private javafx.scene.control.TableColumn<Harvest, String> dateColumn;

    @FXML
    private javafx.scene.control.TableColumn<Harvest, String> areaName;

    @FXML
    private javafx.scene.control.TableColumn<Harvest, Double> fullColumn;

    @FXML
    private javafx.scene.control.TableColumn<Harvest, Double> wastageColumn;

    @FXML
    private TableColumn<Harvest, Double> remainingColumn;

    @FXML
    public TableColumn<Harvest,Double> pricePerKgs;

    private ObservableList<HarvestSummary> list1=null;

    @FXML
    private TableView<HarvestSummary> table;

    @FXML
    private TableColumn<HarvestSummary,String> areaNameColumn;

//    @FXML
//    private TableColumn<HarvestSummary,String> monthColumn;

    @FXML
    private TableColumn<HarvestSummary,Double> fullHarvestColumn;

    @FXML
    private TableColumn<HarvestSummary,Double> wastageharvestcoloumn;

    @FXML
    private TableColumn<HarvestSummary,Double> stockHarvestColumn;

    @FXML
    private TableColumn<HarvestSummary,Double> profitColumn;


    private ObservableList<Harvest> list=null;

    public HarvestFromController() throws SQLException, ClassNotFoundException {
    }

    public void sendOnAction(ActionEvent actionEvent) {

    }

    public void initialize() throws SQLException, ClassNotFoundException {
        setCombobox();

        dateColumn.setCellValueFactory(new PropertyValueFactory<Harvest,String>("date"));
        areaName.setCellValueFactory(new PropertyValueFactory<Harvest,String>("blockName"));
        fullColumn.setCellValueFactory(new PropertyValueFactory<Harvest,Double>("fullHarvest"));
        wastageColumn.setCellValueFactory(new PropertyValueFactory<Harvest, Double>("wastageHarvest"));
        remainingColumn.setCellValueFactory(new PropertyValueFactory<Harvest,Double>("remainingHarvest"));
        pricePerKgs.setCellValueFactory(new PropertyValueFactory<Harvest,Double>("pricePerKg"));

        list=FXCollections.observableArrayList();
        harvestTable.setItems(list);

        statement=connection.createStatement();
        resultSet=statement.executeQuery("SELECT harvest_manage.harvest_date,block.block_name,harvest.harvest_amount,wastage.wastage_amount,harvest_manage.remainning_harvest_amount,harvest_manage.price_per_kg FROM harvest JOIN harvest_manage ON harvest.full_harvest_id=harvest_manage.full_harvest_id JOIN wastage ON wastage.wastage_id=harvest_manage.wastage_id JOIN block ON harvest.block_id=block.block_id");

        while(resultSet.next()){
            harvestTable.getItems().add(new Harvest(resultSet.getString("harvest_date"),
                    resultSet.getString("block_name"),
                    resultSet.getDouble("harvest_amount"),
                    resultSet.getDouble("wastage_amount"),
                    resultSet.getDouble("remainning_harvest_amount"),
                    resultSet.getDouble("price_per_kg")
                    ));
        }
        loadBarChart();
        loadPieChart();
    }

    public void loadBarChart() throws SQLException, ClassNotFoundException {
        String query1 = "SELECT block_name,SUM(harvest_amount) FROM block JOIN harvest on block.block_id = harvest.block_id GROUP BY block.block_id;";

        LineChart.Series<String, Double> series1 = new LineChart.Series<>();
        series1.setName("Full Harvest");
        try {
            ResultSet rs = connection.createStatement().executeQuery(query1);
            while (rs.next()) {
                series1.getData().add(new LineChart.Data<>(rs.getString(1), rs.getDouble(2)));
            }
        } catch (Exception e) {

        }

        String query2 = "SELECT block_name,SUM(wastage_amount) FROM block JOIN harvest on block.block_id = harvest.block_id JOIN harvest_manage on harvest.full_harvest_id = harvest_manage.full_harvest_id JOIN wastage on wastage.wastage_id = harvest_manage.wastage_id GROUP BY block.block_id;";
        LineChart.Series<String,Double> series2 = new LineChart.Series<>();
        series2.setName("Wastage");
        try {
            ResultSet rs = connection.createStatement().executeQuery(query2);
            while (rs.next()){
                series2.getData().add(new LineChart.Data<>(rs.getString(1),rs.getDouble(2)));
            }
        }catch (Exception e){

        }

        String query3 = "SELECT block_name,SUM(remainning_harvest_amount) FROM block JOIN harvest on block.block_id = harvest.block_id JOIN harvest_manage on harvest.full_harvest_id = harvest_manage.full_harvest_id GROUP BY block.block_id;";

        LineChart.Series<String,Double> series3 = new LineChart.Series<>();
        series3.setName("Remain Stock");
        try {
            ResultSet rs = connection.createStatement().executeQuery(query3);
            while (rs.next()){
                series3.getData().add(new LineChart.Data<>(rs.getString(1),rs.getDouble(2)));
            }
        }catch (Exception e){

        }

        lineChart.getData().addAll(series1,series2,series3);
    }

    public void setCombobox(){

        String[] selectType1={"Block A","Block B","Block C","Block D","Block E"};
        comboBox1.setItems(FXCollections.observableArrayList(selectType1));

    }

    public void remainingHarvestLabel(){

        try {
            String blockFullHarvests = (inputtext1.getText().equals(null)) ? "0" : inputtext1.getText();
            blockFullHarvest = Double.parseDouble(blockFullHarvests);

            String blockWastageHarvests = (inputtext2.getText().equals(null)) ? "0" : inputtext2.getText();
            blockWastageHarvest = Double.parseDouble(blockWastageHarvests);

            blockRemainingHarvest = blockFullHarvest - blockWastageHarvest;

        }catch(Exception e){
            System.out.println("Exception");
        }

    }

    public void onActionSave(ActionEvent actionEvent) throws SQLException {

        if (comboBox1.getSelectionModel().getSelectedItem()==null || datepicker.getValue() == null || inputtext1.getText().equals("") || inputtext2.getText().equals("") || pricePerKg.getText().equals("")){
            new Alert(Alert.AlertType.ERROR,"Fill All the fields").show();
        }else {

            String date = datepicker.getValue().toString();

            Double pricePerKgs = Double.parseDouble(pricePerKg.getText());

            String stock_id = null;

            int stock_id_Count = 0;

            int harvest_id_Count = 0;

            int wastage_id_Count = 0;

            Double stock_amount = 0.0;

            String stock_Date = "-1";

            String harvest_id = null;

            String westage_id = null;

            remainingHarvestLabel();

            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT * FROM stock WHERE stock_Date='" + date + "'");

            while (resultSet.next()) {

                try {
                    stock_id = resultSet.getString("stock_id");
                    stock_amount = resultSet.getDouble("stock_amount");
                    stock_Date = resultSet.getString("stock_Date");
                } catch (Exception e) {
                    System.out.println("Exception");
                }

            }

            if (stock_Date.equals("-1")) {
                statement = connection.createStatement();

                resultSet = statement.executeQuery("SELECT count(stock_id) FROM stock ");

                while (resultSet.next()) {
                    stock_id_Count = resultSet.getInt("count(stock_id)");
                }

                stock_id = "Stock_" + (++stock_id_Count);

                //Insert Data into stock Table

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO stock VALUES(?,?,?)");

                preparedStatement.setObject(1, stock_id);
                preparedStatement.setObject(2, blockRemainingHarvest);
                preparedStatement.setObject(3, date);

                preparedStatement.execute();

            } else {
                stock_amount += blockRemainingHarvest;

                preparedStatement = connection.prepareStatement("UPDATE stock SET stock_amount=(?) WHERE stock_id=(?)");

                preparedStatement.setObject(1, stock_amount);
                preparedStatement.setObject(2, stock_id);

                preparedStatement.execute();

            }

            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT block_id FROM block WHERE block_name=" +
                    "'" + comboBox1.getValue().toString() + "'");

            String block_id = null;

            while (resultSet.next()) {
                block_id = resultSet.getString("block_id");
            }

            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT count(full_harvest_id) FROM harvest ");

            while (resultSet.next()) {
                harvest_id_Count = resultSet.getInt("count(full_harvest_id)");
            }

            harvest_id = "harvest_" + (++harvest_id_Count);

            preparedStatement = connection.prepareStatement("INSERT INTO harvest VALUES(?,?,?,?)");

            preparedStatement.setObject(1, harvest_id);
            preparedStatement.setObject(2, blockFullHarvest);
            preparedStatement.setObject(3, stock_id);
            preparedStatement.setObject(4, block_id);


            preparedStatement.execute();

            statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT count(wastage_id) FROM wastage ");

            while (resultSet.next()) {
                wastage_id_Count = resultSet.getInt("count(wastage_id)");
            }

            westage_id = "wastage_" + (++wastage_id_Count);

            preparedStatement = connection.prepareStatement("INSERT INTO wastage VALUES(?,?)");

            preparedStatement.setObject(1, westage_id);
            preparedStatement.setObject(2, blockWastageHarvest);

            preparedStatement.execute();

            preparedStatement = connection.prepareStatement("INSERT INTO harvest_manage VALUES(?,?,?,?,?)");

            preparedStatement.setObject(1, westage_id);
            preparedStatement.setObject(2, harvest_id);
            preparedStatement.setObject(3, date);
            preparedStatement.setObject(4, blockRemainingHarvest);
            preparedStatement.setObject(5, pricePerKgs);

            preparedStatement.execute();

            harvestTable.getItems().add(new Harvest(date, comboBox1.getValue().toString(), blockFullHarvest, blockWastageHarvest, blockRemainingHarvest, pricePerKgs));

            comboBox1.setItems(null);
            setCombobox();

            inputtext1.setText(null);
            inputtext2.setText(null);
            datepicker.setValue(null);
            pricePerKg.setText(null);
        }

    }

    public void setLabel() throws SQLException {

        statement=connection.createStatement();

        resultSet=statement.executeQuery("SELECT sum(harvest.harvest_amount),sum(wastage.wastage_amount),sum(harvest_manage.remainning_harvest_amount) FROM harvest JOIN harvest_manage ON harvest.full_harvest_id=harvest_manage.full_harvest_id JOIN wastage ON wastage.wastage_id=harvest_manage.wastage_id");

        while (resultSet.next()){

            try {
                fullharvestlabel.setText(resultSet.getDouble("sum(harvest.harvest_amount)")+"");
                wastageharvestlabel.setText(resultSet.getDouble("sum(wastage.wastage_amount)")+"");
                remainingharvestlabel.setText(resultSet.getDouble("sum(harvest_manage.remainning_harvest_amount)")+"");
            }catch (Exception e){
                System.out.println("Exception");
            }

        }
    }

    public void setTable() throws SQLException {

        areaNameColumn.setCellValueFactory(new PropertyValueFactory<HarvestSummary,String>("areaName"));
        fullHarvestColumn.setCellValueFactory(new PropertyValueFactory<HarvestSummary,Double>("full_harvest"));
        wastageharvestcoloumn.setCellValueFactory(new PropertyValueFactory<HarvestSummary,Double>("wastage_harvest"));
        stockHarvestColumn.setCellValueFactory(new PropertyValueFactory<HarvestSummary,Double>("stock_harvest"));
        profitColumn.setCellValueFactory(new PropertyValueFactory<HarvestSummary,Double>("profit"));

        list1=FXCollections.observableArrayList();
        table.setItems(list1);

        Statement statement1=connection.createStatement();
        ResultSet resultSet1=statement1.executeQuery("SELECT block.block_name,sum(harvest.harvest_amount) AS full_harvest,sum(wastage.wastage_amount) AS wastage_harvest,sum(harvest_manage.remainning_harvest_amount) AS stock_harvest,sum((harvest_manage.remainning_harvest_amount)*(harvest_manage.price_per_kg)) AS profit FROM harvest JOIN harvest_manage ON harvest.full_harvest_id=harvest_manage.full_harvest_id JOIN wastage ON wastage.wastage_id=harvest_manage.wastage_id JOIN block ON block.block_id=harvest.block_id GROUP BY block.block_name");

        while(resultSet1.next()){
            table.getItems().add(new HarvestSummary(resultSet1.getString("block_name"),
                    resultSet1.getDouble("full_harvest"),
                    resultSet1.getDouble("wastage_harvest"),
                    resultSet1.getDouble("stock_harvest"),
                    resultSet1.getDouble("profit")));
        }
    }

    public void onSelectionHarvestSummary(Event event) throws SQLException {
        setLabel();
        setTable();
    }

    public void loadPieChart() throws SQLException, ClassNotFoundException {
        String query1 = "SELECT SUM(harvest_amount) FROM harvest;";
        String query2 = "SELECT SUM(remainning_harvest_amount) FROM harvest_manage;";
        String query3 = "SELECT SUM(wastage_amount) FROM wastage;";

        Connection connection = DbConnection.getInstance().getConnection();

        ResultSet rs1 = connection.createStatement().executeQuery(query1);
        ResultSet rs2 = connection.createStatement().executeQuery(query2);
        ResultSet rs3 = connection.createStatement().executeQuery(query3);

        ObservableList<PieChart.Data> pieData= FXCollections.observableArrayList();

        try {
            while (rs1.next()){
                pieData.add(new PieChart.Data("Full Harvest",rs1.getDouble(1)));
            }
            while (rs2.next()){
                pieData.add(new PieChart.Data("Remaining Harvest",rs2.getDouble(1)));
            }
            while (rs3.next()){
                pieData.add(new PieChart.Data("Wastage Harvest",rs3.getDouble(1)));
            }
        }catch (Exception e){

        }

        harvestPieChart.setData(pieData);
    }

}
