package controller;

import db.DbConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class HomeFormController implements Initializable {

    public Label lbltime;
    public Label lbldate;
    public PieChart piecharDashBoard;
    public BarChart<String, Double> barChart;
    public Label StockSummaryLabel;
    public Label cultivatedLabel;
    public Label harvestPerMonth;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClock();
        try {
            createpieChart();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            loadBarChart();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadLabels();
    }

    public void loadLabels(){

        //Set cultivated area value to label
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            ResultSet rs = connection.createStatement().executeQuery("SELECT SUM(area) FROM block;");

            if (rs.next()){
                cultivatedLabel.setText("Cultivated   "+rs.getString(1)+"Acres");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Set stock summary value to label
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            ResultSet rs = connection.createStatement().executeQuery("SELECT SUM(stock_amount) FROM stock;");

            if (rs.next()){
                StockSummaryLabel.setText("Stock Summary   "+rs.getString(1)+"Kg");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Set harvest per month value to label
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            ResultSet rs = connection.createStatement().executeQuery("SELECT SUM(harvest_amount)/12 FROM harvest;");

            if (rs.next()){
                harvestPerMonth.setText("Harvest per Month   "+rs.getString(1)+"Kg");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadBarChart() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        String query1 = "SELECT YEAR(harvest_date) AS Year,(SUM(plant.total_amount)-SUM(block_plant.amount)) AS Plants FROM plant JOIN block_plant on plant.plant_id = block_plant.plant_id JOIN harvest on block_plant.block_id = harvest.block_id JOIN harvest_manage on harvest.full_harvest_id = harvest_manage.full_harvest_id JOIN block_fertilizer on block_plant.block_id = block_fertilizer.block_id JOIN fertilizer on fertilizer.fertilizer_id = block_fertilizer.fertilizer_id GROUP BY YEAR(harvest_date);";

        XYChart.Series<String,Double> series = new XYChart.Series<>();
        series.setName("Plants");
        try {
            ResultSet rs = connection.createStatement().executeQuery(query1);
            while (rs.next()){
                series.getData().add(new XYChart.Data<>(rs.getString(1),rs.getDouble(2)));
            }
        }catch (Exception e){

        }

        String query2 = "SELECT  YEAR(harvest_date) AS Year,(SUM(harvest.harvest_amount)-SUM(harvest_manage.remainning_harvest_amount)) AS Harvest FROM plant JOIN block_plant on plant.plant_id = block_plant.plant_id JOIN harvest on block_plant.block_id = harvest.block_id JOIN harvest_manage on harvest.full_harvest_id = harvest_manage.full_harvest_id JOIN block_fertilizer on block_plant.block_id = block_fertilizer.block_id JOIN fertilizer on fertilizer.fertilizer_id = block_fertilizer.fertilizer_id GROUP BY YEAR(harvest_date);";
        XYChart.Series<String,Double> series2 = new XYChart.Series<>();
        series2.setName("Harvest");
        try {
            ResultSet rs = connection.createStatement().executeQuery(query2);
            while (rs.next()){
                series2.getData().add(new XYChart.Data<>(rs.getString(1),rs.getDouble(2)));
            }
        }catch (Exception e){

        }

        String query3 = "SELECT  YEAR(harvest_date) AS Year,(sum(fertilizer.fertilizer_amount)-SUM(block_fertilizer.fertilizer_distributed_amount)) AS Fertilizer FROM plant JOIN block_plant on plant.plant_id = block_plant.plant_id JOIN harvest on block_plant.block_id = harvest.block_id JOIN harvest_manage on harvest.full_harvest_id = harvest_manage.full_harvest_id JOIN block_fertilizer on block_plant.block_id = block_fertilizer.block_id JOIN fertilizer on fertilizer.fertilizer_id = block_fertilizer.fertilizer_id GROUP BY YEAR(harvest_date);";

        XYChart.Series<String,Double> series3 = new XYChart.Series<>();
        series3.setName("Fertilizer");
        try {
            ResultSet rs = connection.createStatement().executeQuery(query3);
            while (rs.next()){
                series3.getData().add(new XYChart.Data<>(rs.getString(1),rs.getDouble(2)));
            }
        }catch (Exception e){

        }

        barChart.getData().addAll(series,series2,series3);
    }

    public void createpieChart()throws Exception{
        String query1 = "SELECT (SUM(stock_order.qty)*harvest_manage.price_per_kg)-(SUM(block.additionalCost)+SUM(block.employee_salary)) AS Profit FROM stock_order JOIN harvest on stock_order.stock_id = harvest.stock_id JOIN harvest_manage on harvest.full_harvest_id = harvest_manage.full_harvest_id JOIN block on block.block_id = harvest.block_id;";
        String query2 = "SELECT SUM(block.additionalCost) AS Additional_Cost FROM stock_order JOIN harvest on stock_order.stock_id = harvest.stock_id JOIN harvest_manage on harvest.full_harvest_id = harvest_manage.full_harvest_id JOIN block on block.block_id = harvest.block_id;";
        String query3 = "SELECT SUM(block.employee_salary) AS Employee_Salary FROM stock_order JOIN harvest on stock_order.stock_id = harvest.stock_id JOIN harvest_manage on harvest.full_harvest_id = harvest_manage.full_harvest_id JOIN block on block.block_id = harvest.block_id;";

        Connection connection = DbConnection.getInstance().getConnection();

        ResultSet rs1 = connection.createStatement().executeQuery(query1);
        ResultSet rs2 = connection.createStatement().executeQuery(query2);
        ResultSet rs3 = connection.createStatement().executeQuery(query3);

        ObservableList<PieChart.Data> pieData= FXCollections.observableArrayList();

        try {
            while (rs1.next()){
                pieData.add(new PieChart.Data("Profit",rs1.getDouble(1)));
            }
            while (rs2.next()){
                pieData.add(new PieChart.Data("Additional Cost",rs2.getDouble(1)));
            }
            while (rs3.next()){
                pieData.add(new PieChart.Data("Employee Salary",rs3.getDouble(1)));
            }
        }catch (Exception e){

        }

        piecharDashBoard.setData(pieData);


    }

    private void initClock() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            lbltime.setText(LocalDateTime.now().format(formatter));

            SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            lbldate.setText(formatter2.format(date));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}
