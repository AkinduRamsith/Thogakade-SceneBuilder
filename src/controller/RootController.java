package controller;

import db.DBConnection;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    public Label lblCustomerCount;
    public Label lblOrderCount;
    public Label lblItemCount;
    public LineChart lineChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String SQL1="Select Count(*) AS customerCount From Customer";
        String SQL2="Select Count(*) AS itemCount From Item";
        String SQL3="Select Count(*) AS orderCount From Orders";
        int customerCount=0;
        int orderCount = 0;
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm1 = connection.prepareStatement(SQL1);
            ResultSet rst1 = pstm1.executeQuery();

            while(rst1.next()){
                customerCount = rst1.getInt("customerCount");
            }
            lblCustomerCount.setText(String.valueOf(customerCount));
            rst1.beforeFirst();

            PreparedStatement pstm2 = connection.prepareStatement(SQL2);
            ResultSet rst2 = pstm2.executeQuery();
            int itemCount = 0;
            if (rst2.next()) {
                itemCount = rst2.getInt("itemCount");
            }
            lblItemCount.setText(String.valueOf(itemCount));

            PreparedStatement pstm3 = connection.prepareStatement(SQL3);
            ResultSet rst3 = pstm3.executeQuery();

            if (rst3.next()) {
                orderCount = rst3.getInt("orderCount");
            }
            lblOrderCount.setText(String.valueOf(orderCount));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }



    }
}
