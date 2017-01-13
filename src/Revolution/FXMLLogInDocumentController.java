/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Revolution;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import java.io.EOFException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FXMLLogInDocumentController implements Initializable {
    private Gateway gateway;
    private ObservableList<String> types = FXCollections.observableArrayList();
    private HandleChanges listener;

    @FXML
    private ComboBox<String> classes;
    @FXML
    private TextField handle;
    @FXML
    private TextField host;
    @FXML
    private TextField port;
    @FXML
    private Label error;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        types.add("Atonal");
        types.add("Classical");
        classes.setItems(types);
        classes.setValue("Classical");
    }

    public void quit(ActionEvent event) {
        System.exit(0);
    }

    public void setGateway(Gateway gate, HandleChanges listener) {
        this.gateway = gate;
        this.listener = listener;
    }

    @FXML
    private void enterGame(ActionEvent event) throws IOException {
        String selectedClass = classes.getSelectionModel().getSelectedItem();
        if (!types.contains(selectedClass) || handle.getText().isEmpty() ||
                host.getText().isEmpty() || port.getText().isEmpty())
            return;
        System.out.println(handle.getText());
        System.out.println(selectedClass);
        listener.setUser(handle.getText());
        try {
            gateway.setFields(host.getText(), Integer.parseInt(port.getText()), handle.getText(), types.indexOf(selectedClass));
        } catch (EOFException e) {
            error.setVisible(true);
            return;
        }
        gateway.start();
        handle.getScene().getWindow().hide();
    }
}

