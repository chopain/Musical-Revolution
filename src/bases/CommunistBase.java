package bases;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//lower left corner
public class CommunistBase implements PixelMeasures {

    private ImageView face = new ImageView();

    public CommunistBase(){
        this.face.setImage(new Image("communistbase.png"));
        this.face.setY(WORLD_HEIGHT - BASE_HEIGHT);
    }

    public ImageView getFace(){
        return face;
    }
}
