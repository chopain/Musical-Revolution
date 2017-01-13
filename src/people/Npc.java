package people;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class Npc {
    private final ImageView face = new ImageView();
    public final int id;


    public Npc(int id) {
        this.face.setImage(new Image("plebian.png"));
        this.id = id;
    }

    public void move(double x, double y) {
        face.setLayoutX(x);
        face.setLayoutY(y);
    }

    public ImageView getFace() {
        return face;
    }
}
