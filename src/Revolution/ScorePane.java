package Revolution;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

import static bases.PixelMeasures.WORLD_WIDTH;

public class ScorePane extends Pane {
    Rectangle c;
    Rectangle b;
    Rectangle cBar;
    Rectangle bBar;

    public ScorePane() {
        c = RectangleBuilder.create()
                .height(20).width(WORLD_WIDTH / 3.0 - 10)
                .stroke(Color.BLACK)
                .build();
        c.setLayoutX(5);
        c.setLayoutY(5);

        cBar = RectangleBuilder.create()
                .height(18).width((WORLD_WIDTH / 3.0 - 10) * (100.0 / 200.0))
                .fill(Color.RED)
                .build();
        cBar.setLayoutX(6);
        cBar.setLayoutY(6);

        b = RectangleBuilder.create()
                .height(20).width(WORLD_WIDTH / 3.0 - 10)
                .stroke(Color.BLACK)
                .build();
        b.setLayoutX(5);
        b.setLayoutY(30);

        bBar = RectangleBuilder.create()
                .height(18).width((WORLD_WIDTH / 3.0 - 10) * (100.0 / 200.0))
                .stroke(Color.BLACK)
                .fill(Color.GREEN)
                .build();
        bBar.setLayoutX(6);
        bBar.setLayoutY(31);
    }

    public void setScores(int team, int score) {
        switch (team) {
            case 0: {
                cBar.setWidth((WORLD_WIDTH / 3.0 - 10) * (score / 200.0));
            }
            case 1: {
                bBar.setWidth((WORLD_WIDTH / 3.0 - 10) * (score / 200.0));
            }
        }
        this.getChildren().clear();
        this.getChildren().addAll(c, b, cBar, bBar);
    }
}
