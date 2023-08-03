package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {
    public AnchorPane anpRoot;
    public AnchorPane anpDashBoard;

    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/root-form.fxml");
        assert resource!=null;
        Parent load=(Parent) FXMLLoader.load(resource);
        this.anpRoot.getChildren().clear();
        this.anpRoot.getChildren().add(load);
    }

    public void btnItemOnAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/item-form.fxml");
        assert resource!=null;
        Parent load=(Parent) FXMLLoader.load(resource);
        this.anpRoot.getChildren().clear();
        this.anpRoot.getChildren().add(load);
    }

    public void btnOrderOnAction(ActionEvent actionEvent) {
    }

    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/customer-form.fxml");
        assert resource!=null;
        Parent load=(Parent) FXMLLoader.load(resource);
        this.anpRoot.getChildren().clear();
        this.anpRoot.getChildren().add(load);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL resource=this.getClass().getResource("/view/root-form.fxml");
        assert resource!=null;
        Parent load= null;

        try {
            load = (Parent) FXMLLoader.load(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.anpRoot.getChildren().clear();
        this.anpRoot.getChildren().add(load);

    }
}
