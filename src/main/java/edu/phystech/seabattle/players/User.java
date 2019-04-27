package edu.phystech.seabattle.players;

import edu.phystech.seabattle.Rules;
import edu.phystech.seabattle.grid.Field;

public class User extends APlayer {
    private User(Field ownField, Field enemyField) {
        super(ownField, enemyField);
    }

    public static User newInstance(int x, int y) {
        Field ownField = new Field(Rules.GRID_SIZE,x,y,20,1); //TODO::
        Field enemyField = new Field(Rules.GRID_SIZE,x + 300,y,20,1);//TODO::
        return new User(ownField, enemyField);
    }

    public void randomFill() throws Exception {
        APlayer.fillField(ownField);
    }
}
