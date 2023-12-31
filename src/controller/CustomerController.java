package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Customer;

import java.sql.*;


public class CustomerController implements CustomerService {
    @Override
    public boolean addCustomer(Customer customer) {
        try {

            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("Insert into Customer Values(?,?,?,?)");
            pstm.setObject(1, customer.getId());
            pstm.setObject(2, customer.getName());
            pstm.setObject(3, customer.getAddress());
            pstm.setObject(4, customer.getSalary());

            if (pstm.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();

        }

        return false;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        try {

            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("Update Customer set name=?,address=?,salary=? where id=?");
            pstm.setObject(1, customer.getName());
            pstm.setObject(2, customer.getAddress());
            pstm.setObject(3, customer.getSalary());
            pstm.setObject(4, customer.getId());
            if (pstm.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();

        }

        return false;
    }

    @Override
    public Customer searchCustomer(String id) {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();

            ResultSet rst = stm.executeQuery("Select * From Customer where id='" + id + "'");
            while (rst.next()) {


                return new Customer(id, rst.getString("name"), rst.getString("address"), rst.getDouble("salary"));

            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();


        }

        return null;
    }

    @Override
    public boolean deleteCustomer(String id) {


        try {

            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();

            if (stm.executeUpdate("Delete From Customer where id='" + id + "'") > 0) {
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();

        }

        return false;
    }

    @Override
    public ObservableList<Customer> getAllCustomer() {

        try {

            ObservableList<Customer> list = FXCollections.observableArrayList();
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("Select * From Customer");
            ResultSet rst = pstm.executeQuery();

            while (rst.next()) {
                Customer customer = new Customer(rst.getString("id"), rst.getString("name"), rst.getString("address"), rst.getDouble("salary"));
                list.add(customer);

            }
            return list;

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();

        }

        return null;
    }
}
