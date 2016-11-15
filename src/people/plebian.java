package people;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static people.PropagandaTypes.*;


public class plebian {
    private ImageView face = new ImageView();
    private double cScale = 100;
    private double bScale = 100;
    private double pScale = 100;
    private double max = 0;


    public plebian() {
        this.face.setImage(new Image("plebian.png"));
    }

    public ImageView getPlebian() {
        return face;
    }

    public double getcScale() {
        return cScale;
    }

    public double getbScale() {
        return bScale;
    }

    public double getpScale() {
        return pScale;
    }

    public int getMaxType() {
        if (pScale > bScale && pScale > cScale) {
            max = pScale;
            return PROLETARIAN;
        } else if (bScale > pScale && bScale > cScale) {
            max = bScale;
            return BOURGEOIS;
        } else if (cScale > pScale && cScale > bScale) {
            max = cScale;
            return COMMUNIST;
        }
        max = 0;
        return NONE;
    }

    public double getMax() {
        return max;
    }

    public void readPropaganda(int type) {
        switch (type) {
            case COMMUNIST: {
                cScale += 20;
                bScale -= 10;
                pScale -= 10;
                break;
            }
            case PROLETARIAN: {
                pScale += 20;
                cScale -= 10;
                bScale -= 10;
                break;
            }
            case BOURGEOIS: {
                bScale += 20;
                cScale -= 10;
                pScale -= 10;
                break;
            }
        }
    }

    public void move(double x, double y) {
        face.setLayoutX(x);
        face.setLayoutY(y);
    }

    public ImageView getFace() {
        return face;
    }
}
