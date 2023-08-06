package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Customer;
import model.Item;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {
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

    private void loadDate() {
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    public void btnAddCustomerOnAction(ActionEvent actionEvent) {
    }

    public void txtQtyOnAction(ActionEvent actionEvent) {
    }

    public void btnAddOnAction(ActionEvent actionEvent) {

    }

    public void btnRemoveOnAction(ActionEvent actionEvent) {
    }

    public void tblOrder(SortEvent<TableView> tableViewSortEvent) {
    }

    public void btnCommitOnAction(ActionEvent actionEvent) {
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
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
