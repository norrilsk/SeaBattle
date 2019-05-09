package edu.phystech.seabattle;

import edu.phystech.seabattle.grid.Field;
import edu.phystech.seabattle.grid.Ship;
import edu.phystech.seabattle.grid.State;
import edu.phystech.seabattle.players.APlayer;
import edu.phystech.seabattle.players.Computer;
import edu.phystech.seabattle.players.User;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.SecureRandom;
import java.util.Random;

public class Main extends Applet implements MouseListener {

    public enum Action
    {
        START,
        GAME;
    }
    APlayer user;
    APlayer computer;
    Action action = Action.START;

    public void addButton(String str){

    }

    @Override
    public void init() {
        assert(1==0);
        super.init();
        user = User.newInstance(20,20);
        computer = Computer.newInstance(20, 300);



        Button random = new Button("Random");
        random.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                user.clearOwnField();
                try {
                    user.randomFill();
                    //computer.randomFill();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
                repaint();
            }
        });
        this.add(random);

        Button start = new Button("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (user.checkField()) {
                    action = Action.GAME;
                    random.hide();
                    start.hide();
                }

                repaint();
            }
        });
        this.add(start);

        try {
            //user.randomFill();
            computer.randomFill();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        this.addMouseListener(this);

    }



    @Override
    public void paint(Graphics graphics) {

        System.out.println("PAINT");
        this.user.paint(graphics,action);
        this.computer.paint(graphics,action);


    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {


        int x = mouseEvent.getX();
        int y = mouseEvent.getY();

        if (action == Action.GAME) {
            x -= user.getEnemyField().getStart_x_px();
            y -= user.getEnemyField().getStart_y_px();
        }

        if(x < 0 || y < 0){
            return;
        }

        int cell_size_px = user.getEnemyField().getCell_size_px();
        int offset_px    = user.getEnemyField().getOffset_px();

        int i = x / (cell_size_px + offset_px);
        int j = y / (cell_size_px + offset_px);

        if (action == Action.GAME) {
            if (i > 9 || j > 9){
                return;
            }


            try {
                shoot(i, j);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (action == Action.START) {


        }

//
//        System.out.println(i + " " + j);
//        try {
//            user.shoot(i, j, computer);
//        } catch (Exception e) {
//            System.out.println("You can't shoot here");
//        }


//        repaint();
    }

    private void shoot(int i, int j) throws Exception{

        State state = user.shoot(i, j, computer);
        repaint();

        if(state != State.MISSED){
            return;
        }

        State computerState = null;
        do{
            repaint();
            computerState = computer.smartShoot(user);
            System.out.println("COMP SHOOT : " + computerState);

            repaint();
        }while (computerState != State.MISSED);

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
