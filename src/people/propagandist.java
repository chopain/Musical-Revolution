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
    private int x;
    private int y;

    public propagandist(String type, String name) {
        this.classType = type;
        this.user = name;
        this.face.setImage(new Image("file:res/" + type + ".png"));
        move();
    }

    public ImageView getFace() {
        return face;
    }

    public void move(){
        face.setLayoutX(300);
        face.setLayoutY(100);
    }

    public void spreadPropaganda(String user, int move){

    }

}
