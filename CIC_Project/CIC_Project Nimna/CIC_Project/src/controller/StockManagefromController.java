package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import db.DbConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import module.AddOrderTable;
import module.Fertilizer;
import module.FertilizerTable;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class StockManagefromController implements Initializable {

    public JFXDatePicker OrderDate;
    public JFXTextField orderQty;
    public JFXTextField orderId;
    public JFXTextField custName;
    public JFXTextField custContact;
    public JFXTextField custAddress;
    public Label fullorderLabel;
    public Label remainLabel;
    public Label distributedLabel;
    public JFXComboBox fertilizerType;
    public JFXTextField expences;
    public JFXTextField stockOrdered;
    public JFXButton inputStocksavebtn;
    public TableView<FertilizerTable> fertilizerTable;
    public TableColumn<FertilizerTable,String> fertilizerTypeColomn;
    public TableColumn<FertilizerTable,String> fertilizerAmountColumn;
    public TableColumn<FertilizerTable,String> expencesColumn;

    private Connection connection = DbConnection.getInstance().getConnection();

    private  PreparedStatement preparedStatement;

    @FXML
    public JFXButton addbtn;

    @FXML
    private TableColumn<AddOrderTable, Integer> OrderQtyColumn;

    @FXML
    private TableColumn<AddOrderTable, String> OrderDateColumn;

    @FXML
    private TableColumn<AddOrderTable, String> OrderIdColomn;

    @FXML
    private TableColumn<AddOrderTable, String> customerName;

    @FXML
    private TableView<AddOrderTable> OrdertableView;

    private ObservableList<AddOrderTable> data;

    private ResultSet resultSet;

    public StockManagefromController() throws SQLException, ClassNotFoundException {
    }

    public void onSelectionManageForm(Event event) {

    }

    public void addbtnOnAction(ActionEvent actionEvent) throws SQLException {

        if (OrderDate.getValue() == null || orderQty.getText().equals("") || orderId.getText().equals("") || custName.getText().equals("") || custContact.getText().equals("") || custAddress.getText().equals("")){
            new Alert(Alert.AlertType.ERROR,"Fill All the fields").show();
        }else {

            //Generate customer id
            Random random = new Random();
            String customerId = "Cust" + random.nextInt(1000);
            Date date = Date.from(OrderDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date sqldate = new Date(date.getTime());
            System.out.println(date);

            preparedStatement = connection.prepareStatement("INSERT INTO customer VALUES(?,?,?,?)");

            //Insert data into database
            preparedStatement.setObject(1, customerId);
            preparedStatement.setObject(2, custName.getText());
            custName.setText(null);
            preparedStatement.setObject(3, custContact.getText());
            custContact.setText(null);
            preparedStatement.setObject(4, custAddress.getText());
            custAddress.setText(null);

            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("INSERT INTO orders VALUES(?,?,?)");
            preparedStatement.setObject(1, orderId.getText());
            preparedStatement.setObject(2, sqldate);
            preparedStatement.setObject(3, customerId);

            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("INSERT INTO stock_order VALUES(?,?,?,?)");
            preparedStatement.setObject(1, "Stock001");
            preparedStatement.setObject(2, orderId.getText());
            orderId.setText(null);
            preparedStatement.setObject(3, sqldate);
            OrderDate.setValue(null);
            preparedStatement.setObject(4, orderQty.getText());
            orderQty.setText(null);

            preparedStatement.executeUpdate();

            populateOrderTable();
            setFullOrderedValue();
            setDistributedValue();
            setRemainValue();
        }

    }

    public void populateOrderTable() throws SQLException {
        //populate order table view from database

        OrderQtyColumn.setCellValueFactory(new PropertyValueFactory<>("OrderId"));
        OrderDateColumn.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));
        OrderIdColomn.setCellValueFactory(new PropertyValueFactory<>("OrderQty"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));


        data = FXCollections.observableArrayList();

        resultSet = connection.createStatement().executeQuery("SELECT orders.order_id,orders.date,stock_order.qty,customer.name FROM orders JOIN stock_order ON orders.order_id = stock_order.order_id JOIN customer ON customer.customer_id=orders.customer_id ORDER BY orders.order_id;");
        while (resultSet.next()){
            data.add(new AddOrderTable(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getString(4)));
        }

        OrdertableView.setItems(data);
    }

    public void setFullOrderedValue(){
        // set Ordered stock value
        try {
            connection = DbConnection.getInstance().getConnection();
            ResultSet rs = connection.createStatement().executeQuery("SELECT SUM(qty) FROM stock_order;");

            if (rs.next()){
                fullorderLabel.setText(rs.getString(1));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setRemainValue(){
        //set Remain stock value
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            ResultSet rs = connection.createStatement().executeQuery("SELECT SUM(stock_amount) FROM stock;");

            if (rs.next()){
                remainLabel.setText(rs.getString(1));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setDistributedValue(){
        //set Distributed stock value
        try {
            Connection connection = DbConnection.getInstance().getConnection();
            ResultSet rs = connection.createStatement().executeQuery("SELECT SUM(stock_order.qty) FROM stock_order WHERE stock_order.date < CURDATE();");

            if (rs.next()){
                distributedLabel.setText(rs.getString(1));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //populate order table view from database
        try {
            populateOrderTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            populateFertilizerTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            setComboBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setFullOrderedValue();
        setRemainValue();
        setDistributedValue();
    }

    public void populateFertilizerTable() throws SQLException {
        fertilizerTypeColomn.setCellValueFactory(new PropertyValueFactory<>("fertilizerType"));
        fertilizerAmountColumn.setCellValueFactory(new PropertyValueFactory<>("fertilizerAmount"));
        expencesColumn.setCellValueFactory(new PropertyValueFactory<>("expenses"));

        ObservableList<FertilizerTable> fertilizerData = FXCollections.observableArrayList();
        ResultSet rs = connection.createStatement().executeQuery("SELECT fertilizer_type,SUM(fertilizer_amount),SUM(fertilizer_expenses) FROM fertilizer GROUP BY fertilizer_type;");
        while (rs.next()){
            fertilizerData.add(new FertilizerTable(rs.getString(1),rs.getString(2),rs.getString(3)));
        }

        fertilizerTable.setItems(fertilizerData);
    }

    public void setComboBox() throws SQLException {

        String[] selectType1 = {"Fertilizer A", "Fertilizer B", "Fertilizer C", "Fertilizer D", "Fertilizer E"};
        fertilizerType.setItems(FXCollections.observableArrayList(selectType1));
    }

    public void fertilizerSaveBtnOnAction(ActionEvent actionEvent) throws SQLException {

        if (fertilizerType.getSelectionModel().getSelectedItem()==null || expences.getText().equals("") || stockOrdered.getText().equals("")){
            new Alert(Alert.AlertType.ERROR,"Fill All the fields").show();
        }else {

            String type = fertilizerType.getValue().toString();
            Random random = new Random();
            String fertilizerId = "fertilizer" + random.nextInt(1000);

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO fertilizer(fertilizer_id,fertilizer_type,fertilizer_amount,fertilizer_expenses) VALUES(?,?,?,?);");
            preparedStatement.setObject(1, fertilizerId);
            preparedStatement.setObject(2, type);
            fertilizerType.setValue(null);
            preparedStatement.setObject(3, stockOrdered.getText());
            stockOrdered.setText(null);
            preparedStatement.setObject(4, expences.getText());
            expences.setText(null);

            preparedStatement.executeUpdate();

            populateFertilizerTable();
        }
    }

//    public void CustomerAddressKeyReleased(KeyEvent keyEvent) {
//        //Validate data input
//        if (Objects.equals(OrderDate.getValue(), "") && orderQty.getText().equals("") && orderId.getText().equals("") && custName.getText().equals("") && custContact.getText().equals("") && custAddress.getText().equals("")){
//            addbtn.setDisable(true);
//        }else{
//            addbtn.setDisable(false);
//        }
//    }

}

