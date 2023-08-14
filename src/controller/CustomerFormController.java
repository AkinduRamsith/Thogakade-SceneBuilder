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

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {
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
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setTableValues(newValue);
            }
        });
    }

    public static ObservableList<String> getAllCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("Select id From Customer").executeQuery();
        ObservableList<String> list = FXCollections.observableArrayList();
        while (rst.next()) {
            list.add(rst.getString("id"));
        }
        return list;
    }

    public static Customer getAllCustomers(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        String SQL = "Select * From Customer where id='" + id + "'";
        ResultSet rst = stm.executeQuery(SQL);
        if (rst.next()) {
            return new Customer(id, rst.getString("name"), rst.getString("address"), rst.getDouble("salary"));
        }

        return null;
    }

    private void setTableValues(Customer newValue) {
        txtId.setText(newValue.getId());
        txtName.setText(newValue.getName());
        txtAddress.setText(newValue.getAddress());
        txtSalary.setText(String.valueOf(newValue.getSalary()));
    }


    private void loadTable() throws SQLException, ClassNotFoundException {
       ObservableList<Customer> customerList=new CustomerController().getAllCustomer();
        tblCustomer.setItems(customerList);
    }

    private void setCellValueFactory() {
        colID.setCellValueFactory(new PropertyValueFactory<Customer, String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<Customer, Double>("salary"));
    }

    public void btnAddCustomer(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            String id = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            double salary = Double.parseDouble(txtSalary.getText());
            System.out.println(id + "," + name + "," + address + "," + salary);
            Customer customer = new Customer(id, name, address, salary);
            boolean isAdded=new CustomerController().addCustomer(customer);
            if (isAdded) {
                loadTable();
                System.out.println("Added Success");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Customer Add");
                alert.setHeaderText(null);
                alert.setContentText("Added Success");

                alert.showAndWait();

            }
            txtId.setText("");
            txtName.setText("");
            txtAddress.setText("");
            txtSalary.setText("");
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }
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
        try {
            String id = txtId.getText();
            String name = txtName.getText();
            String address = txtAddress.getText();
            double salary = Double.parseDouble(txtSalary.getText());
            Customer customer = new Customer(id, name, address, salary);
            boolean isAdded=new CustomerController().updateCustomer(customer);

            if (isAdded) {
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
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

    }

    public void btnDeleteCustomer(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        boolean isDeleted=new CustomerController().deleteCustomer(txtId.getText());
        if (isDeleted) {
            loadTable();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deleting");
            alert.setHeaderText(null);
            alert.setContentText("Delete Success");
            alert.showAndWait();

            txtId.setText("");
            txtName.setText("");
            txtAddress.setText("");
            txtSalary.setText("");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deleting");
            alert.setHeaderText(null);
            alert.setContentText("Empty Fields");

            alert.showAndWait();
        }

    }

    public void txtIdOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Customer customer=new CustomerController().searchCustomer(txtId.getText());

        if (null!=customer) {
            txtId.setText(customer.getId());
            txtName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
            txtSalary.setText(String.valueOf(customer.getSalary()));
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Searching");
            alert.setHeaderText(null);
            alert.setContentText("Customer Not Found");

            alert.showAndWait();
        }
    }


}
