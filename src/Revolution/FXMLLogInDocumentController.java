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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;
import people.plebian;
import people.propagandist;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class FXMLLogInDocumentController implements Initializable {
    private CommemeismGateway gateway;
    private String selectedClass;
    private TreeMap<String, propagandist> players;
    private List<ImageView> propagandists;
    private List<ImageView> plebs;
    private List<ImageView> bases;
    private List<Shape> borders;
    private List<plebian> plebians;

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

    public void setWorld(WorldPane world, List<ImageView> p, List<ImageView> plebs, /*List<plebian> plebians,*/
                         List<ImageView> bases, List<Shape> borders, TreeMap<String, propagandist> players) {
        this.world = world;
        this.propagandists = p;
        this.players = players;
        this.plebians = plebians;
        this.plebs = plebs;
        this.bases = bases;
        this.borders = borders;
    }

    public void enterGame(ActionEvent event) {
        gateway.sendHandle(handle.getText());
        System.out.println(handle.getText());
        gateway.sendClass(selectedClass);
        ImageView background = new ImageView(new Image("gamebg.jpg", 1400, 0, true, true));
        world.setShapes(background, propagandists, plebs, bases, borders);
        System.out.println(selectedClass);
        new Thread(new GameCheck(gateway, world, handle.getText(), players, propagandists)).start();
        //new Thread(new PlayerCheck(gateway, players, world, propagandists)).start();
        handle.getScene().getWindow().hide();
    }

}

