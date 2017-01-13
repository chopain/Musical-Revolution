package people;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Composer {
    private final int id;
    private final String name;
    private int period;
    private final ImageView face = new ImageView();

    public void setPeriod(int period) {
        if (this.period == period)
            return;
        this.period = period;
        switch (period) {
            case 0: {
                this.face.setImage(new Image("atonal.png", 0, 75, true, true));
                break;
            }
            case 1: {
                this.face.setImage(new Image("classical.png", 0, 75, true, true));
                break;
            }
        }
    }

    public Composer(int id, int period, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.period = period;
        switch (period) {
            case 0: {
                this.face.setImage(new Image("atonal.png", 0, 75, true, true));
                break;
            }
            case 1: {
                this.face.setImage(new Image("classical.png", 0, 75, true, true));
                break;
            }
        }
        move(x, y);
    }

    public String getName() {
        return name;
    }

    public ImageView getFace() {
        return face;
    }

    public int getID() {
        return id;
    }

    public void move(int x, int y) {
        face.setLayoutX(x);
        face.setLayoutY(y);
    }
}
