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

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, String> colID;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, Double> colSalary;

    @FXML
    private TableView<Customer> tblCustomer;


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
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) ->{
            if(null != newValue) {
                setTableValues(newValue);
            }
        });
    }
    private void setTableValues(Customer newValue) {
        txtId.setText(newValue.getId());
        txtName.setText(newValue.getName());
        txtAddress.setText(newValue.getAddress());
        txtSalary.setText(String.valueOf(newValue.getSalary()));
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        String SQL = "Select * From Customer";
        ObservableList<Customer> list = FXCollections.observableArrayList();
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        ResultSet rst = pstm.executeQuery();

        while (rst.next()) {
            Customer customer = new Customer(rst.getString("id"), rst.getString("name"), rst.getString("address"), rst.getDouble("salary"));
            list.add(customer);
        }
        tblCustomer.setItems(list);
    }

    private void setCellValueFactory() {
        colID.setCellValueFactory(new PropertyValueFactory<Customer, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<Customer, Double>("salary"));
    }

    public void btnAddCustomer(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        double salary = Double.parseDouble(txtSalary.getText());
        System.out.println(id + "," + name + "," + address + "," + salary);
        Customer customer = new Customer(id, name, address, salary);
        String SQL = "Insert into Customer Values(?,?,?,?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setObject(1, customer.getId());
        pstm.setObject(2, customer.getName());
        pstm.setObject(3, customer.getAddress());
        pstm.setObject(4, customer.getSalary());
        int i = pstm.executeUpdate();
        if (i > 0) {
            loadTable();
            System.out.println("Added Success");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Add");
            alert.setHeaderText(null);
            alert.setContentText("Added Success");

            alert.showAndWait();

        } else {
            System.out.println("Fail");
        }


        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtSalary.setText("");
    }

    public void btnSearchCustomer(ActionEvent actionEvent) {
        try {
            txtIdOnAction(actionEvent);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnUpdateCustomer(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        double salary = Double.parseDouble(txtSalary.getText());
        Customer customer = new Customer(id, name, address, salary);
        String SQL = "Update Customer set name=?,address=?,salary=? where id=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);
        pstm.setObject(1, name);
        pstm.setObject(2, address);
        pstm.setObject(3, salary);
        pstm.setObject(4, id);
        int i = pstm.executeUpdate();
        if (i > 0) {
            loadTable();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Success");
            alert.setHeaderText(null);
            alert.setContentText("Update Success");
            alert.showAndWait();


            txtId.setText("");
            txtName.setText("");
            txtAddress.setText("");
            txtSalary.setText("");

        }

    }

    public void btnDeleteCustomer(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String SQL = "Delete From Customer where id='" + txtId.getText() + "'";
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

            txtId.setText("");
            txtName.setText("");
            txtAddress.setText("");
            txtSalary.setText("");
        }
    }

    public void txtIdOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        String SQL = "Select * From Customer where id='" + txtId.getText() + "'";
        ResultSet rst = stm.executeQuery(SQL);
        if (rst.next()) {
            txtId.setText(rst.getString("id"));
            txtName.setText(rst.getString("name"));
            txtAddress.setText(rst.getString("address"));
            txtSalary.setText(String.valueOf(rst.getDouble("salary")));
        }
    }


}
