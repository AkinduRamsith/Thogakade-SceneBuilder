package controller;

import db.DBConnection;
import model.Order;

import java.sql.*;

public class OrderController {
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
