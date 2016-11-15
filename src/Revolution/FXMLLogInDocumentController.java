/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Revolution;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import people.propagandist;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class FXMLLogInDocumentController implements Initializable {
    private CommemeismGateway gateway;
    private TextArea textarea;
    private String selectedClass;
    private TreeMap<String, propagandist> players;

    @FXML
    private ComboBox<String> classes;
    @FXML
    private TextField handle;
    private WorldPane world;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void quit(ActionEvent event) {
        System.exit(0);
    }

    public void setGateway(CommemeismGateway gateway) {
        this.gateway = gateway;
        classes.setItems(gateway.getClassTypes());
        classSelect();
    }

    public void classSelect() {
        classes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedClass = newValue;
            System.out.println("Selected Class: " + selectedClass);
        });
    }

    public void setWorld(WorldPane world) {
        this.world = world;
    }

    public void setMap(TreeMap<String, propagandist> players) {
        this.players = players;
    }


    public void enterGame(ActionEvent event) {
        gateway.sendHandle(handle.getText());
        System.out.println(handle.getText());
        gateway.sendClass(selectedClass);
        System.out.println(selectedClass);
        new Thread(new GameCheck(gateway, world, handle.getText(), players)).start();
        handle.getScene().getWindow().hide();
    }

    private void getSelected() {
        selectedClass = this.classes.getValue();
    }
}

