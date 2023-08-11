package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Customer;
import model.Item;

import java.sql.*;

public class ItemController implements ItemService{
    @Override
    public boolean addItem(Item item) {
        try {
            String SQL = "Insert into Item Values(?,?,?,?)";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1, item.getCode());
            pstm.setObject(2, item.getDescription());
            pstm.setObject(3, item.getUnitPrice());
            pstm.setObject(4, item.getQtyOnHand());

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
    public boolean updateItem(Item item) {
        try {
            String SQL = "Update Item set description=?,unitPrice=?,qtyOnHand=? where code=?";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1, item.getDescription());
            pstm.setObject(2, item.getUnitPrice());
            pstm.setObject(3, item.getQtyOnHand());
            pstm.setObject(4, item.getCode());
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
    public Item searchitem(String code) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            String SQL = "Select * From Item where code='" + code+ "'";
            ResultSet rst = stm.executeQuery(SQL);
            while(rst.next()){
                Item item=new Item(code,rst.getString(2),rst.getDouble(3),rst.getInt(4));

                return item;

            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
        return null;
    }

    @Override
    public boolean deleteItem(String code) {
        try {
            String SQL = "Delete From Item where code='" + code + "'";
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
    public ObservableList<Item> getAllItems() {

        try {
            String SQL = "Select * From item";
            ObservableList<Item> list = FXCollections.observableArrayList();
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet rst = pstm.executeQuery();

            while (rst.next()) {
                Item item=new Item(rst.getString(1),rst.getString(2),rst.getDouble(3),rst.getInt(4));
                list.add(item);

            }
            return list;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
