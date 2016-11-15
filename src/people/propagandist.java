package people;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * Created by lain on 10/11/16.
 */

public class propagandist {
    private String classType;
    private String user;
    private ImageView face = new ImageView();

    public propagandist(String type, String name) {
        this.classType = type;
        this.user = name;
        this.face.setImage(new Image("file:res:" + type + ".png"));
    }

    public ImageView getFace() {
        return face;
    }

    public void move(double x, double y){
        face.setLayoutX(x);
        face.setLayoutY(y);
    }

    public void spreadPropaganda(String user, int move){

    }

}
