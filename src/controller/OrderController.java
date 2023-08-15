package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Alert;
import model.Customer;
import model.Order;
import model.OrderCart;


import java.sql.*;

public class OrderController {
    public static ObservableList<Order> getAllOrders() {
        ObservableList<Order> list;
        try {
            String SQL = "Select * From Orders";
            list = FXCollections.observableArrayList();
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet rst = pstm.executeQuery();

            while (rst.next()) {
                Order order = new Order(rst.getString("id"), rst.getString("date"), rst.getString("customerId"), null);
                list.add(order);

            }


        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            throw new RuntimeException(e);
        }
        return list;

    }
    public static ObservableList<OrderCart> getAllOrderDetails(String id) {

        try {

            ObservableList<OrderCart> list = FXCollections.observableArrayList();
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement( "SELECT *FROM orders INNER JOIN orderdetail ON orders.id = orderdetail.orderId inner join item on item.code=orderdetail.itemcode WHERE orderdetail.orderId = '" + id + "'");
            ResultSet rst = pstm.executeQuery();

            while (rst.next()) {
                OrderCart orderCart = new OrderCart(rst.getString("id"), rst.getString("customerId"), rst.getString("itemCode"), rst.getString("description"),rst.getInt("qty"));
                list.add(orderCart);

            }
            return list;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
    public static String getLastOrderId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT id FROM Orders ORDER BY id DESC LIMIT 1");
        return rst.next() ? rst.getString("id") : null;
    }
    public static boolean placeOrder(Order order) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement pstm = connection.prepareStatement("Insert into Orders values(?,?,?)");
            pstm.setObject(1, order.getOrderId());
            pstm.setObject(2, order.getOrderDate());
            pstm.setObject(3, order.getCustomerId());
            boolean orderIsAdded = pstm.executeUpdate() > 0;
            if (orderIsAdded) {
                boolean orderDetailAdded = OrderDetailController.addOrderDetail(order.getOrderDetailList());
                if (orderDetailAdded) {
                    boolean itemIsUpdated = ItemFormController.updateStock(order.getOrderDetailList());
                    if (itemIsUpdated) {
                        connection.commit();
                        return true;
                    }
                }
            }
            connection.rollback();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }
    }
}
