package Revolution;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static bases.PixelMeasures.WORLD_WIDTH;

public class ScorePane extends Pane {
    Rectangle c;
    Rectangle b;
    Rectangle cBar;
    Rectangle bBar;
    int count = 0;
    Text pCount = new Text(5, 80, "Propaganda Count: " + count);

    public ScorePane() {
        c = RectangleBuilder.create()
                .height(22).width(WORLD_WIDTH / 3.0 - 10)
                .stroke(Color.BLACK)
                .build();
        c.setLayoutX(5);
        c.setLayoutY(5);

        cBar = RectangleBuilder.create()
                .height(18).width((WORLD_WIDTH / 3.0 - 10) * (750.0 / 1500.0))
                .fill(Color.RED)
                .build();
        cBar.setLayoutX(6);
        cBar.setLayoutY(6);

        b = RectangleBuilder.create()
                .height(22).width(WORLD_WIDTH / 3.0 - 10)
                .stroke(Color.BLACK)
                .build();
        b.setLayoutX(5);
        b.setLayoutY(30);

        bBar = RectangleBuilder.create()
                .height(18).width((WORLD_WIDTH / 3.0 - 10) * (750.0 / 1500.0))
                .stroke(Color.BLACK)
                .fill(Color.GREEN)
                .build();
        bBar.setLayoutX(6);
        bBar.setLayoutY(31);
        pCount.setFill(Color.YELLOW);
        pCount.setFont(new Font(25));
        pCount.setStroke(Color.BLACK);
    }

    public void setpCount(int n) {
        count = n;
        pCount.setText("Propaganda Count: " + count);
    }

    public void setScores(int team, int score) {
        switch (team) {
            case 0: {
                cBar.setWidth((WORLD_WIDTH / 3.0 - 10) * (score / 1500.0));
                break;
            }
            case 1: {
                bBar.setWidth((WORLD_WIDTH / 3.0 - 10) * (score / 1500.0));
                break;
            }
        }
        this.getChildren().clear();
        this.getChildren().addAll(c, b, cBar, bBar, pCount);
    }
}
