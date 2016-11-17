package people;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class propagandist {
    private int id;
    private String name;
    private int size;
    private int x;
    private int y;
    private int width;
    private int height;
    private ImageView face = new ImageView();

    public propagandist(int id, int size,String name, int x, int y, int width, int height) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        switch ()
        this.face.setImage(new Image("communist.png"));
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
