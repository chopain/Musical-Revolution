package people;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by lain on 10/11/16.
 */
public class plebian {
    private ImageView face = new ImageView();

    public plebian(){
        this.face.setImage(new Image("plebian.png"));
    }

    public ImageView getPlebian(){
        return face;
    }
}
