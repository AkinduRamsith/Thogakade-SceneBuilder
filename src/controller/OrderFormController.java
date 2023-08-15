package controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Order;
import model.OrderCart;


import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {

    @FXML
    public TextField txtOrderId;
    @FXML
    private TableView<OrderCart> tblOrderDetail;
    @FXML
    private TableColumn<Order, String> colCustId;

    @FXML
    private TableColumn<Order, String> colOrderDate;

    @FXML
    private TableColumn<Order, String> colOrderId;
    @FXML
    private TableColumn<OrderCart, String> colCustomerID;

    @FXML
    private TableColumn<OrderCart, String> colDescription;

    @FXML
    private TableColumn<OrderCart, String> colItemCode;
    @FXML
    private TableColumn<OrderCart, String> colOrderID;
    @FXML
    private TableColumn<OrderCart,Integer> colQty;



    @FXML
    private TableView<Order> tblOrder;
    ObservableList<OrderCart> orderCartList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadTable();
        tblOrder.getSelectionModel().selectedItemProperty().addListener((observableValue, order, t1) -> {
            getItem(observableValue);
        });

    }
    private void setCellValueFactory1(){
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    }

    private void getItem(ObservableValue<? extends Order> observableValue) {
        Order orderCart =observableValue.getValue();

        orderCartList = OrderController.getAllOrderDetails(orderCart.getOrderId());
        loadTableOrder();
        setCellValueFactory1();
    }

    public void loadTableOrder() {

        tblOrderDetail.setItems(orderCartList);

    }

    private void loadTable(){
        ObservableList<Order> orderList = new OrderController().getAllOrders();
        tblOrder.setItems(orderList);
    }

    private void setCellValueFactory() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

    }


    public void btnSearchOrder(ActionEvent actionEvent) {
        txtOrderIdOnAction(actionEvent);

    }

    public void txtOrderIdOnAction(ActionEvent actionEvent) {
        orderCartList = OrderController.getAllOrderDetails(txtOrderId.getText());
        loadTableOrder();
        setCellValueFactory1();


    }
}
