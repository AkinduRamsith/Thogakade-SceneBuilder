package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {

    public PasswordField txtPassword;

    private Stage stage;
    private Scene scene;
    private Parent parent;

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {

        String password = "1234";
        if ( txtPassword.getText().equals(password)) {
            parent= FXMLLoader.load(getClass().getResource("/view/dashboard-controller.fxml"));
            stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(parent);

            stage.setScene(scene);
            stage.show();
        }else{
            new Alert(Alert.AlertType.WARNING,"InCorrect Pin").show();
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    public void txtPinOnAction(ActionEvent actionEvent) throws IOException {
        btnLoginOnAction(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtPassword.requestFocus();
    }
}
