package people;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class propagandist {
    private int id;
    private String name;
    private int party;
    private int x;
    private int y;
    private ImageView face = new ImageView();

    public propagandist(int id, int party, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.party = party;
        switch (party) {
            case 1: {
                this.face.setImage(new Image("communist.png"));
                break;
            }
            case 0: {
                this.face.setImage(new Image("bourgeois.png"));
                break;
            }
        }
        move(x, y);
    }

    public ImageView getFace() {
        return face;
    }

    public int getID(){
        return id;
    }

    public void move(int x, int y) {
        face.setLayoutX(this.x = x);
        face.setLayoutY(this.y = y);
    }
}
