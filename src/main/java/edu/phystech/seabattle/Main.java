package edu.phystech.seabattle;

import edu.phystech.seabattle.grid.Field;
import edu.phystech.seabattle.grid.Ship;
import edu.phystech.seabattle.players.APlayer;
import edu.phystech.seabattle.players.User;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.SecureRandom;
import java.util.Random;

public class Main extends Applet implements MouseListener {

    APlayer user;
    APlayer computer;

    @Override
    public void init() {
        super.init();
        user = User.newInstance(20,20);
        computer = User.newInstance(20, 300);
        try {
            ((User)user).randomFill();
            ((User)computer).randomFill();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        this.addMouseListener(this);

    }



    @Override
    public void paint(Graphics graphics) {
        System.out.println("PAINT");
        this.user.paint(graphics);
        this.computer.paint(graphics);


    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println("asdaSd");
        try {
            user.shoot(0,0, computer);
        } catch (Exception e) {
            System.out.println("You can't shoot here");
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
