package edu.phystech.seabattle.grid;

import java.awt.*;

public class Ship {

    private int x;
    private int y;
    private boolean horizontal;
    private int length;

    private int lives;

    public Ship(int x, int y, boolean horizontal, int length) {
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
        this.length = length;
        this.lives = length;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public int getLength() {
        return length;
    }

    public boolean hurt(){
        this.lives--;
        return (lives == 0);
    }

    public void draw(Graphics gr, int start_x_px, int start_y_px, int cell_size_px,  int offset_px) {
        int height = horizontal ? 1:length;
        int width = !horizontal ? 1:length;

        gr.setColor(Color.BLACK);
        ((Graphics2D)gr).setStroke(new BasicStroke(offset_px * 2));

        gr.drawRect(start_x_px + (cell_size_px + offset_px)*this.x -offset_px,
                start_y_px + (cell_size_px + offset_px)*this.y -offset_px,
                (cell_size_px + offset_px)*width ,
                (cell_size_px + offset_px)*height);
    }
}
