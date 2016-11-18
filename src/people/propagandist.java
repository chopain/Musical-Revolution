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
    private int propagandaCount;

    public propagandist(int id, int party, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.party = party;
        this.propagandaCount = 0;
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
        move(x, y);
    }

    public String getName(){
        return name;
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
    public int getPropagandaCount(){
        return propagandaCount;
    }
    public void setPropagandaCount(int propagandaCount){
        this.propagandaCount = propagandaCount;
    }
}
