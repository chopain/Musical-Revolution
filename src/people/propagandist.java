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
        this.x = x;
        this.y = y;
        switch (party) {
            case 0: {
                this.face.setImage(new Image("communist.png"));
                break;
            }
            case 1: {
                this.face.setImage(new Image("bourgeois.png"));
                break;
            }
        }
    }

    public ImageView getFace() {
        return face;
    }

    public int getID(){
        return id;
    }

    public void move(int x, int y) {
        face.setLayoutX(x);
        face.setLayoutY(y);
    }
}
