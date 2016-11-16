package bases;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;
import physics.LineSegment;
import physics.Point;

import java.util.ArrayList;

public class CommunistBase implements PixelMeasures {

    private ImageView face = new ImageView();
    private LineSegment rightSegment;
    private LineSegment upSegment;

    public CommunistBase() {
        this.face.setImage(new Image("communistbase.png"));
        this.face.setY(WORLD_HEIGHT - BASE_HEIGHT);
        //create new line segments around the outer edges of the base to display and to check for colisions
        rightSegment = new LineSegment(new Point(BASE_WIDTH, WORLD_HEIGHT - BASE_HEIGHT), new Point(BASE_WIDTH, WORLD_HEIGHT)); //segment on right edge
        upSegment = new LineSegment(new Point(BASE_WIDTH * (2.0 / 3.0), WORLD_HEIGHT - BASE_HEIGHT), new Point(0.0, WORLD_HEIGHT - BASE_HEIGHT));
    }

    public ImageView getFace() {
        return face;
    }

    public ArrayList<Shape> getBorder() {
        ArrayList<Shape> linesToDisplay = new ArrayList<>();
        linesToDisplay.add(upSegment.toShape());
        linesToDisplay.add(rightSegment.toShape());
        return linesToDisplay;
    }
}
