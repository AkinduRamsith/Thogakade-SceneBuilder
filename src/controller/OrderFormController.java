package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {
    public Button btnAdd;
    @FXML
    private TableView<Cart> tblOrder;
    @FXML
    private TableColumn<Cart, String> colCode;

    @FXML
    private TableColumn<Cart, String> colDescription;

    @FXML
    private TableColumn<Cart, Integer> colQty;

    @FXML
    private TableColumn<Cart, Double> colTotal;

    @FXML
    private TableColumn<Cart, Double> colUnitPrice;
    ObservableList<Cart> listItem= FXCollections.observableArrayList();
    public Label lblID;
    public Label lblDate;
    public ComboBox cmbCustomerId;
    public Label lblCustomerName;
    public ComboBox cmbItemCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtQty;
    public Label lblTotal;

    private void loadAllCustomerId() {
            try {
            for(String id : CustomerController.getAllCustomerId()){
                cmbCustomerId.getItems().addAll(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllItemCode() {
        try {
            for(String code : ItemController.getAllItemCode()){
                cmbItemCode.getItems().addAll(code);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void calculateNetTotal(){
        double total=0;
        for(Cart cart:listItem){
            total+=cart.getTotal();
        }
        lblTotal.setText(String.valueOf(total));
    }
    private void loadDate() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    public void btnAddCustomerOnAction(ActionEvent actionEvent) {

    }

    public void txtQtyOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        btnAddOnAction(actionEvent);
    }
    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<Cart, String>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Cart, String>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<Cart, Double>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Cart, Double>("total"));
    }
    public void btnAddOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String code=cmbItemCode.getSelectionModel().getSelectedItem().toString();
        String description=txtDescription.getText();
        int qty= Integer.parseInt(txtQty.getText());
        double unitPrice= Double.parseDouble(txtUnitPrice.getText());
        double total=calculateTotal(qty,unitPrice);

        Cart cart =new Cart(code,description,unitPrice,qty,total);
        listItem.add(cart);

        tblOrder.setItems(listItem);
        calculateNetTotal();
        setCellValueFactory();
        txtQty.setText("");
        System.out.println(cart);
        
    }
    public double calculateTotal(int qty,double unitPrice){
        return qty*unitPrice;
    }

    public void btnRemoveOnAction(ActionEvent actionEvent) {

    }



    public void btnCommitOnAction(ActionEvent actionEvent) {
        try {

            DBConnection.getInstance().getConnection().commit();
        } catch (ClassNotFoundException | SQLException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {

        try {
            String orderId=lblID.getText();
            String orderDate=lblDate.getText();
            String customerId=cmbCustomerId.getSelectionModel().getSelectedItem().toString();
            ArrayList<OrderDetail> orderDetailArrayList=new ArrayList<>();
            for (Cart cart:listItem) {
                String itemCode=cart.getCode();
                int qty= cart.getQty();
                double unitPrice=cart.getUnitPrice();
                OrderDetail orderDetail=new OrderDetail(orderId,itemCode,qty,unitPrice);
                orderDetailArrayList.add(orderDetail);
            }
            Order order=new Order(orderId,orderDate,customerId,orderDetailArrayList);
            boolean isAdded=OrderController.placeOrder(order);
            if(isAdded){
                new Alert(Alert.AlertType.CONFIRMATION,"Order Placed ! ").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void setOrderId(){

        try {
            String id = OrderController.getLastOrderId();
            if(null!=id){
                id=id.split("[A-Z]")[1];
                id=String.format("D%03d",(Integer.parseInt(id)+1));
                lblID.setText(id);
            }else{
                lblID.setText("D001");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDate();
        loadAllCustomerId();
        loadAllItemCode();
        setOrderId();
    }

    public void comboBoxOnAction(ActionEvent actionEvent) {
        String customerId=cmbCustomerId.getSelectionModel().getSelectedItem().toString();
        try {
            lblCustomerName.setText(CustomerController.getAllCustomers(customerId).getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void comboBoxCodeOnAction(ActionEvent actionEvent) {
        String itemCode=cmbItemCode.getSelectionModel().getSelectedItem().toString();
        try {
            txtDescription.setText(ItemController.getAllItems(itemCode).getDescription());
            txtUnitPrice.setText(String.valueOf(ItemController.getAllItems(itemCode).getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(ItemController.getAllItems(itemCode).getQtyOnHand()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
