package edu.phystech.seabattle.players;

import edu.phystech.seabattle.Main;
import edu.phystech.seabattle.Rules;
import edu.phystech.seabattle.grid.Field;
import edu.phystech.seabattle.grid.Ship;
import edu.phystech.seabattle.grid.State;


import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static edu.phystech.seabattle.grid.State.*;

public abstract class APlayer {

    protected Field ownField;
    protected Field enemyField;
    protected Field settingField;
    private List<Ship> fakes = new ArrayList<>(8);

    public APlayer(Field ownField, Field enemyField) {
        this.ownField = ownField;
        this.enemyField = enemyField;
        this.settingField = new Field(Rules.GRID_SIZE,enemyField.getStart_x_px(),enemyField.getStart_y_px(),
                enemyField.getCell_size_px() ,enemyField.getOffset_px());
        int start_x = enemyField.getStart_x_px();
        int start_y= enemyField.getStart_y_px();
        int offset = enemyField.getCell_size_px();
        fakes.add(new Ship(0,0,false,4));
        fakes.add(new Ship(0 ,9,true,4));
        fakes.add(new Ship(2,1,false,3));
        fakes.add(new Ship(0 ,7,true,3));
        fakes.add(new Ship(4,2,false,2));
        fakes.add(new Ship(0 ,5,true,2));
        fakes.add(new Ship(6,3,true,1));


    }

    public void clearOwnField()
    {
        ownField.clear();
    }
    public void clearSettingield()
    {
        settingField.clear();
    }
    public boolean checkField(){
        return ownField.check();
    }
    private  void drawShips(Graphics graphics){
        int start_x = enemyField.getStart_x_px();
        int start_y= enemyField.getStart_y_px();
        int cell_size = enemyField.getCell_size_px();
        int offset = enemyField.getOffset_px();
        for (int i = 0; i < fakes.size(); i++) {
            fakes.get(i).draw(graphics,start_x,start_y,cell_size,offset);
        }

    }


    public Field getOwnField() {
        return ownField;
    }

    public Field getEnemyField() {
        return enemyField;
    }
    public Field getSettingField() {
        return settingField;
    }

    private void virusShip(int x, int y, Field field)
    {
        field.set(x,y,KILLED);
//        if (x -1 >= 0  &&  enemyField.get(x,y) == DAMAGED )
//            virusShip(x-1,y);
//        if (y -1 >= 0  &&  enemyField.get(x,y) == DAMAGED )
//            virusShip(x,y-1);
//        if (x + 1 < Rules.GRID_SIZE &&  enemyField.get(x,y) == DAMAGED )
//            virusShip(x+1,y);
//        if (y + 1 < Rules.GRID_SIZE &&  enemyField.get(x,y) == DAMAGED )
//            virusShip(x,y+1);
        int min_x = (x-1) < 0? 0 : x-1;
        int min_y = (y-1) < 0? 0 : y-1;
        int max_x = x+1;
        int max_y = y+1;

        max_x = max_x > Rules.GRID_SIZE -1  ? Rules.GRID_SIZE -1 : max_x;
        max_y = max_y > Rules.GRID_SIZE -1  ? Rules.GRID_SIZE -1 : max_y;

        for (int i = min_x; i <= max_x; i++) {
            for (int j = min_y; j <= max_y; j++) {
                switch (field.get(i,j)){
                    case EMPTY:
                    case FORBIDDEN: {
                        field.set(i,j,MISSED);
                        break;
                    }
                    case DAMAGED:{
                        virusShip(i, j,field);
                    }
                }

            }
        }

    }
    public State shoot(int x, int y, APlayer enemy) throws Exception {
        State state =  enemy.getShoot(x,y);
        switch (state){
            case MISSED:{
                enemyField.set(x,y,MISSED);
                break;
            }
            case DAMAGED: {
                enemyField.set(x, y, DAMAGED);
                break;
            }
            case KILLED:{
                virusShip(x,y, enemyField);
                break;
            }
        }
        return enemyField.get(x,y);
    }

    public abstract State smartShoot(APlayer enemy) throws Exception;


    public State getShoot(int x, int y) throws Exception {
        State current = ownField.get(x, y);
        switch (current){
            case EMPTY:
            case FORBIDDEN:
            {
                ownField.set(x, y, MISSED);
                return MISSED;
            }
            case SHIP: {
                ownField.set(x, y, DAMAGED);
                Ship ship = ownField.getShip(x,y);
                if (ship.hurt())
                {
                    int x_ship = ship.getX();
                    int y_ship = ship.getY();
                    virusShip(x_ship,y_ship,ownField);

                    return KILLED;
                }
                else
                {
                    return DAMAGED;
                }
            }
        }
        throw new Exception("UNEXPECTED BEHAVIOUR" + this.getClass());
    }


    protected static void fillField(Field field) throws Exception {
        Random r = new SecureRandom();
        int need_to_place = 0;
        for (int s = 0; s < Math.pow(Rules.GRID_SIZE, 3); s++) {
            need_to_place = 0;
            for (int i = 0; i < Rules.SHIPS.length; i++) {
                int quantity = Rules.SHIPS[i][1];
                need_to_place += quantity;
                int size = Rules.SHIPS[i][0];
                for (int j = 0; j < quantity; j++) {
                    boolean horizontal = r.nextBoolean();
                    int max_x = horizontal ? Rules.GRID_SIZE - size + 1 : Rules.GRID_SIZE;
                    int max_y = !horizontal ? Rules.GRID_SIZE - size + 1 : Rules.GRID_SIZE;

                    for (int k = 0; k < Math.pow(Rules.GRID_SIZE, 3); k++) {
                        int possible_start_x = r.nextInt(max_x);
                        int possible_start_y = r.nextInt(max_y);

                        Ship ship = new Ship(possible_start_x, possible_start_y, horizontal, size);
                        if (field.isShipAddable(ship)) {
                            //add
                            field.addShip(ship);
                            need_to_place--;
                            break;
                        }

                    }

                }
            }

            if (need_to_place == 0)
                return;
            field.clear();
        }
        throw new Exception("Unable to set ships");
    }

    public void paint(Graphics graphics , Main.Action action) {
        ownField.draw(graphics);
        if(action == Main.Action.GAME) {
            enemyField.draw(graphics);
        }
        if (action == Main.Action.START) {

            settingField.draw(graphics);
            drawShips(graphics);
        }
    }

    public void randomFill() throws Exception {
        APlayer.fillField(ownField);
    }
}
