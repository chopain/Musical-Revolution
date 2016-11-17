package objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Revolution.MoveType;

public class Propaganda implements MoveType {
    private ImageView i = new ImageView();

    //create an ImageView to hold the image and create a Box of the same size to check for intersections before we move the image
    public Propaganda(double startX, double startY) {
        i.setImage(new Image("propaganda.png"));
        i.setLayoutX(startX);
        i.setLayoutY(startY);
    }

    public ImageView getShape() {
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
}