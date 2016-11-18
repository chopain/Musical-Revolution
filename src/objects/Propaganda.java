package objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Revolution.MoveType;

public class Propaganda implements MoveType {
    private ImageView i = new ImageView();
    private int id;

    //create an ImageView to hold the image and create a Box of the same size to check for intersections before we move the image
    public Propaganda(int id, double startX, double startY) {
        this.id = id;
        i.setImage(new Image("propaganda.png"));
        i.setLayoutX(startX);
        i.setLayoutY(startY);
    }

    public ImageView
    getShape() {
        return i;
    }

    public void updateShape(double x, double y) {
        i.setLayoutX(x);
        i.setLayoutY(y);
    }

    public double getX() {
        return i.getLayoutX();
    }

    public double getY() {
        return i.getLayoutY();
    }
    public int getId(){
        return id;
    }

    public void setX(int x){
        i.setLayoutX(x);
    }

    public void setY(int y){
        i.setLayoutY(y);
    }
}