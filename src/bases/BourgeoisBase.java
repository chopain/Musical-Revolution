package bases;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//upper right corner
public class BourgeoisBase implements PixelMeasures {

    private ImageView face = new ImageView();

    public BourgeoisBase(){
        this.face.setImage(new Image("bourgeoisbase.png"));
        face.setX(WORLD_WIDTH - BASE_WIDTH);
    }

    public ImageView getFace(){
        return face;
    }
}
