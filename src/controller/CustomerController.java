package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Customer;

import java.sql.*;

public class CustomerController implements CustomerService{
    @Override
    public boolean addCustomer(Customer customer) {
        try {
            String SQL = "Insert into Customer Values(?,?,?,?)";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1, customer.getId());
            pstm.setObject(2, customer.getName());
            pstm.setObject(3, customer.getAddress());
            pstm.setObject(4, customer.getSalary());

            if(pstm.executeUpdate()>0){
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
           // throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        try {
            String SQL = "Update Customer set name=?,address=?,salary=? where id=?";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1, customer.getName());
            pstm.setObject(2, customer.getAddress());
            pstm.setObject(3, customer.getSalary());
            pstm.setObject(4, customer.getId());
            if(pstm.executeUpdate()>0){
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            // throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public Customer searchCustomer(String id) {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            String SQL = "Select * From Customer where id='" + id + "'";
            ResultSet rst = stm.executeQuery(SQL);
            while(rst.next()){
                Customer customer=new Customer(id,rst.getString("name"),rst.getString("address"),rst.getDouble("salary"));

                    return customer;

            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }

        return null;
    }

    @Override
    public boolean deleteCustomer(String id) {


        try {
            String SQL = "Delete From Customer where id='" + id + "'";
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
             if(stm.executeUpdate(SQL)>0){
                 return true;
             }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            // throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public ObservableList<Customer> getAllCustomer() {

        try {
            String SQL = "Select * From Customer";
            ObservableList<Customer> list = FXCollections.observableArrayList();
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet rst = pstm.executeQuery();

            while (rst.next()) {
                Customer customer = new Customer(rst.getString("id"), rst.getString("name"), rst.getString("address"), rst.getDouble("salary"));
                list.add(customer);

            }
            return list;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //return null;
    }
}
