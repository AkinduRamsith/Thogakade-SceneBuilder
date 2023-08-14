package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TableColumn<Order, String> colCustId;

    @FXML
    private TableColumn<Order, String> colOrderDate;

    @FXML
    private TableColumn<Order, String> colOrderId;

    @FXML
    private TableView<Order> tblOrder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        try {
            loadTable();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        tblOrder.getSelectionModel().selectedItemProperty().addListener();
    }
    public void loadTableOrder(){
        ObservableList<OrderCart> orderCartList=new OrderController().getAllOrderDetails();

    }

    private void loadTable() throws SQLException, ClassNotFoundException {
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
        System.out.println("Hello");
    }

    public void txtOrderIdOnAction(ActionEvent actionEvent) {
        btnSearchOrder(actionEvent);
        System.out.println("Hello");
    }
}
