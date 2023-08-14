package controller;

import db.DBConnection;
import model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailController {

    public static boolean addOrderDetail(ArrayList<OrderDetail> orderDetailArrayList) throws SQLException, ClassNotFoundException {
        boolean result = true;
        for (OrderDetail orderDetail : orderDetailArrayList) {
            boolean isAdded = addOrderDetail(orderDetail);
            if (!isAdded) {
                result = false;
                break;
            }
        }
        return result;
    }
    public static boolean addOrderDetail(OrderDetail orderDetail) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement pstm = connection.prepareStatement("Insert into OrderDetail values(?,?,?,?)")) {
            pstm.setObject(1, orderDetail.getOrderId());
            pstm.setObject(2, orderDetail.getItemCode());
            pstm.setObject(3, orderDetail.getQty());
            pstm.setObject(4, orderDetail.getUnitPrice());
            return pstm.executeUpdate() > 0;
        }
    }
}
