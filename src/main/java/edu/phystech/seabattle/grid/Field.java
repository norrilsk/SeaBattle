package edu.phystech.seabattle.grid;

import edu.phystech.seabattle.Rules;
import edu.phystech.seabattle.utils.Pair;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.phystech.seabattle.grid.State.*;

public class Field {

    private int cell_size_px;
    private int offset_px;
    private  int grid_size;
    private  State[][] grid;
    private int start_x_px;
    private int start_y_px;
    private int killed = 0;
    public int getCell_size_px() {
        return cell_size_px;
    }

    public int getOffset_px() {
        return offset_px;
    }

    public int getGrid_size() {
        return grid_size;
    }

    public State[][] getGrid() {
        return grid;
    }

    public int getStart_x_px() {
        return start_x_px;
    }

    public int getStart_y_px() {
        return start_y_px;
    }
    public int getKilled(){
        return killed;
    }
    public List<Ship> getShips() {
        return ships;
    }

    public Map<Pair, Ship> getShipMap() {
        return shipMap;
    }

    private List<Ship> ships = new ArrayList<>(10);
    private Map<Pair, Ship> shipMap = new HashMap<>();

    public Field(int grid_size, int start_x_px, int start_y_px, int cell_size_px, int offset_px){
        this.grid_size = grid_size;
        this.start_x_px = start_x_px;
        this.start_y_px = start_y_px;
        this.cell_size_px = cell_size_px;
        this.offset_px = offset_px;
        this.grid =  new State[grid_size][grid_size];
        clear();
    }

    public void set(int i, int j, State state){
        if (state == KILLED){
            killed++;
        }
        this.grid[i][j] = state;
    }

    public State get(int i, int j){
        return this.grid[i][j];
    }

    public void draw(Graphics gr)
    {
        // draw letter
        gr.setColor(Color.BLACK);
        for (int i = 0; i < Rules.GRID_SIZE; i++) {
            gr.drawString( i+"",start_x_px  -  cell_size_px,start_y_px  + (i + 1)*(cell_size_px + offset_px) - cell_size_px/3);
        }
        for (int i = 0; i < Rules.GRID_SIZE; i++) {
            gr.drawString( (char)('a' + i) + "",start_x_px  + (i + 1)*(cell_size_px + offset_px) - 2*cell_size_px/3 ,start_y_px  -  cell_size_px/3);
        }

        //draw grid
        gr.setColor(new Color(135, 206, 250));
        gr.fillRect(start_x_px - offset_px, start_y_px - offset_px,
                grid_size * cell_size_px + (grid_size + 1) * offset_px,
                grid_size * cell_size_px + (grid_size + 1)* offset_px);
        for (int i = 0; i < this.grid_size; i++) {
            for (int j = 0; j < grid_size; j++) {
                Color c = new Color(239, 252,255);
                switch (grid[i][j]){
                    case FORBIDDEN:{
                        c = Color.GREEN;
                        break;
                    }

                }



                gr.setColor(c);
                gr.fillRect(start_x_px + i*(cell_size_px + offset_px) ,
                        start_y_px +j*(cell_size_px +offset_px),
                        cell_size_px, cell_size_px);

            }
        }
        //draw ships

        for (int i = 0; i < ships.size(); i++) {
            Ship s = ships.get(i);
            s.draw(gr, start_x_px, start_y_px, cell_size_px, offset_px);
            //gr.drawRect(start_x_px + s.getX() * cell_size_px, start_y_px + s.getY() * cell_size_px, cell_size_px, cell_size_px);
        }

        for (int i = 0; i < this.grid_size; i++) {
            for (int j = 0; j < grid_size; j++) {
                Color c = new Color(239, 252,255);
                switch (grid[i][j]){
                    case KILLED:
                    case DAMAGED:{
                        c = Color.RED;
                        gr.setColor(c);
                        gr.fillRect(start_x_px + i*(cell_size_px + offset_px) ,
                                start_y_px +j*(cell_size_px +offset_px),
                                cell_size_px, cell_size_px);
                        break;
                    }
                    case MISSED:{
                        c = Color.YELLOW;
                        gr.setColor(c);
                        gr.fillRect(start_x_px + i*(cell_size_px + offset_px) ,
                                start_y_px +j*(cell_size_px +offset_px),
                                cell_size_px, cell_size_px);
                        break;
                    }

                }


            }
        }
    }
    public boolean check() {
        int[] test = new int[Rules.GRID_SIZE+1];
        for(int i = 0; i <ships.size(); i++){
            test[ships.get(i).getLength()]++;
        }
        for(int i = 0; i <Rules.SHIPS.length; i++){
            int size = Rules.SHIPS[i][0];
            int count = Rules.SHIPS[i][1];
            if (test[size]!= count) {
                return false;
            }
            test[size] = 0;
        }
        for (int i = 0; i < test.length; i++) {
            if (test[i]!= 0){
                return false;
            }
        }
        return true;
    }
    public void addShip(Ship ship){
        this.ships.add(ship);
        int x = ship.getX();
        int y = ship.getY();
        int lenght = ship.getLength();
        int min_x = (x-1) < 0? 0 : x-1;
        int min_y = (y-1) < 0? 0 : y-1;
        int max_x = ship.isHorizontal() ? x + lenght : x + 1;
        int max_y = !ship.isHorizontal() ? y + lenght : y + 1;

        max_x = max_x > Rules.GRID_SIZE -1  ? Rules.GRID_SIZE -1 : max_x;
        max_y = max_y > Rules.GRID_SIZE -1  ? Rules.GRID_SIZE -1 : max_y;
        for (int i = min_x; i <= max_x; i++) {
            for (int j = min_y; j <= max_y; j++) {
                grid[i][j] = FORBIDDEN;

            }
        }

        for (int i = x; i < (ship.isHorizontal() ? x + ship.getLength() : x + 1); i++) {

            for (int j = y; j < (!ship.isHorizontal() ? y + ship.getLength() : y + 1); j++) {
                grid[i][j] = SHIP;
                shipMap.put(new Pair(i, j), ship);
            }
        }

    }

    public boolean isShipAddable(Ship ship){
        int x = ship.getX();
        int y = ship.getY();
        for (int i = x; i < (ship.isHorizontal() ? x + ship.getLength() : x + 1); i++) {

            for (int j = y; j < (!ship.isHorizontal() ? y + ship.getLength() : y + 1); j++) {
                if (grid[i][j] != EMPTY)
                    return false;
            }
        }


        return true;
    }

    public void clear(){
        for (int i = 0; i < grid_size; i++) {
            for (int j = 0 ; j < grid_size; j++){
                this.grid[i][j] = EMPTY;
            }
        }
        ships.clear();
        killed = 0;
    }

    public Ship getShip(int x, int y){
        return shipMap.get(new Pair(x, y));
    }
}
