package edu.phystech.seabattle.players;

import edu.phystech.seabattle.Rules;
import edu.phystech.seabattle.grid.Field;
import edu.phystech.seabattle.grid.State;

import java.util.*;

public class Computer extends APlayer {

    enum Direction{
        UP, DOWN, RIGHT, LEFT, NONE;
    }

    private State prevShoot  = null;
    private Direction prevDirection = Direction.NONE;
    private Direction currentDirection = Direction.NONE;
    private int prev_x = -1;
    private int prev_y = -1;
    private Computer(Field ownField, Field enemyField) {
        super(ownField, enemyField);
    }

    public static Computer newInstance(int x, int y) {
        Field ownField = new Field(Rules.GRID_SIZE,x,y,20,1); //TODO::
        Field enemyField = new Field(Rules.GRID_SIZE,x + 300,y,20,1);//TODO::
        return new Computer(ownField, enemyField);
    }

    @Override
    public State shoot(int x, int y, APlayer enemy) throws Exception {

        State st  = null;
        st = super.shoot(x, y, enemy);
        if (st != State.MISSED){
            prevShoot = st;
            prev_x = x;
            prev_y = y;

            prevDirection = currentDirection;
        }
//        currentDirection = Direction.NONE;
        if (st == State.KILLED) {
            prevDirection = Direction.NONE;
            currentDirection= Direction.NONE;
            prevShoot = null;
        }
        return st;
    }

    @Override
    public State smartShoot(APlayer enemy) throws Exception {
        int x = prev_x;
        int y = prev_y;
        if (prevShoot == State.DAMAGED){
            while (true)
            {
                switch (prevDirection)
                {
                    case UP:
                        y--;
                        if ((y < 0) || enemyField.get(x,y) == State.MISSED) {
                            prevDirection = Direction.DOWN;
                            currentDirection = prevDirection;
                            continue;
                        }
                        if (enemyField.get(x,y) == State.DAMAGED) {
                            continue;
                        }
                        if (enemyField.get(x,y) == State.KILLED) {
                            throw new Exception("KILLED exception");
                        }
                        return shoot(x, y, enemy);
                    case DOWN:
                        y++;
                        if ((y > 9) || enemyField.get(x,y) == State.MISSED) {
                            prevDirection = Direction.UP;
                            currentDirection = prevDirection;
                            continue;
                        }
                        if (enemyField.get(x,y) == State.DAMAGED) {
                            continue;
                        }
                        if (enemyField.get(x,y) == State.KILLED) {
                            throw new Exception("KILLED exception");
                        }
                        return shoot(x, y, enemy);
                    case LEFT:
                        x--;
                        if ((x < 0) || enemyField.get(x,y) == State.MISSED) {
                            prevDirection = Direction.RIGHT;
                            currentDirection = prevDirection;
                            continue;
                        }
                        if (enemyField.get(x,y) == State.DAMAGED) {
                            continue;
                        }
                        if (enemyField.get(x,y) == State.KILLED) {
                            throw new Exception("KILLED exception");
                        }
                        return shoot(x, y, enemy);
                    case RIGHT:
                        x++;
                        if ((x > 9) || enemyField.get(x,y) == State.MISSED) {
                            prevDirection = Direction.LEFT;
                            currentDirection = prevDirection;
                            continue;
                        }
                        if (enemyField.get(x,y) == State.DAMAGED) {
                            continue;
                        }
                        if (enemyField.get(x,y) == State.KILLED) {
                            throw new Exception("KILLED exception");
                        }
                        return shoot(x, y, enemy);
                    default:
                        List<Direction> list = new ArrayList<>(4);
                        list.add(Direction.UP);
                        list.add(Direction.DOWN);
                        list.add(Direction.LEFT);
                        list.add(Direction.RIGHT);


                        Collections.shuffle(list);
                        for(int num = 0 ; num <4; num++) {
                            currentDirection = list.get(num);
                            switch (currentDirection){
                                case UP:

                                    if ((y -1 < 0) || enemyField.get(x,y - 1) == State.MISSED) {
                                        break;
                                    }

                                    return shoot(x, y - 1, enemy);
                                case DOWN:

                                    if ((y + 1 > 9) || enemyField.get(x,y + 1) == State.MISSED) {
                                        break;
                                    }

                                    return shoot(x, y + 1, enemy);
                                case LEFT:

                                    if ((x - 1 < 0) || enemyField.get(x - 1,y) == State.MISSED) {
                                        break;
                                    }

                                    return shoot(x - 1, y, enemy);
                                case RIGHT:

                                    if ((x + 1 > 9) || enemyField.get(x + 1,y) == State.MISSED) {
                                        break;
                                    }

                                    return shoot(x + 1, y, enemy);

                            }

                        }


                }
            }
        }

        Random r = new Random();

        do {
            x = r.nextInt(10);
            y = r.nextInt(10);
        }while (enemyField.get(x,y) != State.EMPTY);
        return shoot(x, y, enemy);
    }
}
