package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.Item;
import model.OrderDetail;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ItemController implements Initializable {
    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    @FXML
    private TableColumn<Item, String> colCode;

    @FXML
    private TableColumn<Item, String> colDescription;

    @FXML
    private TableColumn<Item, Integer> colQtyOnHand;

    @FXML
    private TableColumn<Item, Double> colUnitPrice;

    @FXML
    private TableView<Item> tblItem;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        try {
            loadTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setTableValues(newValue);
            }
        });
    }
    public static ObservableList<String> getAllItemCode() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("Select code From Item").executeQuery();
        ObservableList<String> list = FXCollections.observableArrayList();
        while (rst.next()) {
            list.add(rst.getString("code"));
        }
        return list;
    }
    public static Item getAllItems(String code) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        String SQL = "Select * From Item where code='" +code+ "'";
        ResultSet rst = stm.executeQuery(SQL);
        if (rst.next()) {
            return new Item(code, rst.getString("description"), rst.getDouble("unitPrice"),rst.getInt("qtyOnHand"));
        }

        return null;
    }

    private void setTableValues(Item newValue) {
        txtCode.setText(newValue.getCode());
        txtDescription.setText(newValue.getDescription());
        txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(newValue.getQtyOnHand()));
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        String SQL = "Select * From Item";
        ObservableList<Item> list = FXCollections.observableArrayList();
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            Item item = new Item(rst.getString("code"), rst.getString("description"), rst.getDouble("unitPrice"), rst.getInt("qtyOnHand"));
            list.add(item);
        }
        tblItem.setItems(list);
    }

    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<Item, String>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Item, String>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<Item, Double>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<Item, Integer>("qtyOnHand"));
    }

    public void btnAddOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            String code = txtCode.getText();
            String description = txtDescription.getText();
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
            System.out.println(code + "," + description + "," + unitPrice + "," + qtyOnHand);
            Item item = new Item(code, description, unitPrice, qtyOnHand);
            String SQL = "Insert into Item Values(?,?,?,?)";

            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1, item.getCode());
            pstm.setObject(2, item.getDescription());
            pstm.setObject(3, item.getUnitPrice());
            pstm.setObject(4, item.getQtyOnHand());
            int i = pstm.executeUpdate();
            if (i > 0) {
                loadTable();
                System.out.println("Added Success");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Item Add");
                alert.setHeaderText(null);
                alert.setContentText("Added Success");

                alert.showAndWait();

            } else {
                System.out.println("Fail");
            }


            cleanFields();
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }
    }

    private void cleanFields() {
        txtCode.setText("");
        txtDescription.setText("");
        txtUnitPrice.setText("");
        txtQtyOnHand.setText("");
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            String code = txtCode.getText();
            String description = txtDescription.getText();
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
            Item item = new Item(code, description, unitPrice, qtyOnHand);
            String SQL = "Update Item set description=?,unitPrice=?,qtyOnHand=? where code=?";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1, description);
            pstm.setObject(2, unitPrice);
            pstm.setObject(3, qtyOnHand);
            pstm.setObject(4, code);

            int i = pstm.executeUpdate();
            if (i > 0) {
                loadTable();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Success");
                alert.setHeaderText(null);
                alert.setContentText("Update Success");
                alert.showAndWait();


                cleanFields();

            }
        }catch(NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        txtCodeOnAction(actionEvent);
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String SQL = "Delete From Item where code='" + txtCode.getText() + "'";
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        int i = stm.executeUpdate(SQL);
        if (i > 0) {
            loadTable();
            System.out.println(i);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deleting");
            alert.setHeaderText(null);
            alert.setContentText("Delete Success");
            alert.showAndWait();

            cleanFields();
        } else {
            System.out.println("Fail");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deleting");
            alert.setHeaderText(null);
            alert.setContentText("Empty Fields");

            alert.showAndWait();
        }
    }

    public void txtCodeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        String SQL = "Select * From Item where code='" + txtCode.getText() + "'";
        ResultSet rst = stm.executeQuery(SQL);
        if (rst.next()) {
            txtCode.setText(rst.getString("code"));
            txtDescription.setText(rst.getString("description"));
            txtUnitPrice.setText(String.valueOf(rst.getDouble("unitPrice")));
            txtQtyOnHand.setText(String.valueOf(rst.getInt("qtyOnHand")));
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Searching");
            alert.setHeaderText(null);
            alert.setContentText("Customer Not Found");

            alert.showAndWait();
        }
    }
    public static boolean updateStock(ArrayList<OrderDetail> orderDetailArrayList) throws SQLException, ClassNotFoundException {
        for(OrderDetail orderDetail:orderDetailArrayList){
            if(!updateStock(orderDetail)){
                return false;
            }
        }
        return true;
    }
    public static boolean updateStock(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("Update Item set qtyOnHand=qtyOnHand-? where code=?");
        pstm.setObject(1,orderDetail.getQty());
        pstm.setObject(2,orderDetail.getItemCode());
        return pstm.executeUpdate()>0;
    }


}
