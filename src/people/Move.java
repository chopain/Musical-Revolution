package people;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lain on 11/11/16.
 */
public class Move {
    private List<String> moveList = Collections.synchronizedList(new ArrayList<String>());
    private List<String> moveQueue = Collections.synchronizedList(new ArrayList<String>());
    public Move (){
    }

    public void addMove(String move){
        moveList.add(move);
        moveQueue.add(move);
    }

    public void reMove(){
        moveList.clear();
        moveQueue.clear();
    }

    public void updateQueue(){
        moveQueue.clear();
    }

}
