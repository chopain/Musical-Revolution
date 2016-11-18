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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;
import objects.Propaganda;
import people.plebian;
import people.propagandist;


import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FXMLLogInDocumentController implements Initializable {
    private CommemeismGateway gateway;
    private String selectedClass;
    private TreeMap<String, propagandist> players;
    private List<ImageView> propagandists;
    private List<ImageView> plebs;
    private List<ImageView> bases;
    private List<Shape> borders;
    private List<plebian> plebians;
    private ObservableList<String> types = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> classes;
    @FXML
    private TextField handle;
    private WorldPane world;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        types.add("Communist");
        types.add("Bourgeois");
        classes.setItems(types);
        classes.setValue("Communist");
    }

    public void quit(ActionEvent event) {
        System.exit(0);
    }

    public void setGateway(CommemeismGateway gate) {
        this.gateway = gate;
    }

    @FXML
    private void enterGame(ActionEvent event) throws IOException {
        String selectedClass = classes.getSelectionModel().getSelectedItem();
        if(!types.contains(selectedClass) || handle.getText().isEmpty())
            return;
        System.out.println(handle.getText());
        System.out.println(selectedClass);

        gateway.setFields(handle.getText(), types.indexOf(selectedClass));
        gateway.start();
        handle.getScene().getWindow().hide();
    }
}

