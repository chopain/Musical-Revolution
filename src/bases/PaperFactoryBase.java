package bases;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//middle of board or something
public class PaperFactoryBase implements PixelMeasures {
    private ImageView face = new ImageView();

    public PaperFactoryBase(){
        this.face.setImage(new Image("paperfactory.png"));
        face.setX(WORLD_WIDTH/2 - BASE_WIDTH/2);
        face.setY(WORLD_HEIGHT/2 - BASE_HEIGHT/2);
    }

    public ImageView getFace(){
        return face;
    }
}