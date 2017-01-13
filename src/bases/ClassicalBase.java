package bases;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//upper right corner
public class ClassicalBase implements PixelMeasures {

    private final ImageView face = new ImageView();

    public ClassicalBase(){
        this.face.setImage(new Image("Beethoven.png"));
        face.setX(WORLD_WIDTH - BASE_WIDTH-12);
        face.setY(10);
    }

    public ImageView getFace(){
        return face;
    }
}
