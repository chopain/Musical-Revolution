package people;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class propagandist {
    private String classType;
    private String user;
    private ImageView face = new ImageView();

    public propagandist(String name, String type) {
        this.classType = type;
        this.user = name;
        this.face.setImage(new Image(type.toLowerCase() +".png"));
    }

    public ImageView getFace() {
        return face;
    }

    public void move(double x, double y) {
        face.setLayoutX(x);
        face.setLayoutY(y);
    }

    public void spreadPropaganda(String user, int move) {

    }

}
