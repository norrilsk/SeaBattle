package edu.phystech.seabattle;

import edu.phystech.seabattle.grid.Field;
import edu.phystech.seabattle.grid.Ship;
import edu.phystech.seabattle.grid.State;
import edu.phystech.seabattle.players.APlayer;
import edu.phystech.seabattle.players.Computer;
import edu.phystech.seabattle.players.User;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.SecureRandom;
import java.util.Random;

public class Main extends Applet implements MouseListener {

    public enum Action
    {
        START,
        GAME,
        FINISH
    }
    String winner = "";
    APlayer user;
    APlayer computer;
    Action action = Action.START;
    int size_to_place = 0;
    boolean is_hor_to_place = false;
    int x_offset_to_place = 0;
    int y_offset_to_place = 0;

    public void addButton(String str){

    }

    @Override
    public void init() {

        super.init();
        user = User.newInstance(40,40);
        computer = Computer.newInstance(40, 300);



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




        Button clear = new Button("Clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                user.clearOwnField();

                repaint();
            }
        });
        this.add(clear);


        Button start = new Button("Start");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (user.checkField()) {
                    action = Action.GAME;
                    random.hide();
                    start.hide();
                    clear.hide();
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


        if(action == Action.FINISH) {
            graphics.setColor(Color.BLACK);
            graphics.drawString("GAME OVER",200,200);
            graphics.drawString(winner +" WINS",200,250);

        }
        else{
            this.user.paint(graphics,action);
        }
        //this.computer.paint(graphics,action);


    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {


        int x = mouseEvent.getX();
        int y = mouseEvent.getY();
        int my_x= x;
        int my_y = y;

        x -= user.getEnemyField().getStart_x_px();
        y -= user.getEnemyField().getStart_y_px();




        int cell_size_px = user.getEnemyField().getCell_size_px();
        int offset_px    = user.getEnemyField().getOffset_px();

        int i = x / (cell_size_px + offset_px);
        int j = y / (cell_size_px + offset_px);



        if (action == Action.GAME) {
            if (i > 9 || j > 9 || x < 0 || y < 0){
                return;
            }


            try {
                shoot(i, j);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (action == Action.START) {

            if (!(i > 9 || j > 9 || x < 0 || y < 0)) {

                user.clearSettingield();

                if (i == 0 && j >= 0 && j <= 3) {
                    x_offset_to_place = 0;
                    y_offset_to_place = j;
                    size_to_place = 4;
                    is_hor_to_place = false;
                    user.getSettingField().set(i,j,State.DAMAGED);
                }

                if (j == 9 && i >= 0 && i <= 3) {
                    x_offset_to_place = i;
                    y_offset_to_place = 0;
                    size_to_place = 4;
                    is_hor_to_place = true;
                    user.getSettingField().set(i,j,State.DAMAGED);
                }
                if (i == 2 && j >= 1 && j <= 3) {
                    x_offset_to_place = 0;
                    y_offset_to_place = j - 1;
                    size_to_place = 3;
                    is_hor_to_place = false;
                    user.getSettingField().set(i,j,State.DAMAGED);
                }
                if (j == 7 && i >= 0 && i <= 2) {
                    x_offset_to_place = i;
                    y_offset_to_place = 0;
                    size_to_place = 3;
                    is_hor_to_place = true;
                    user.getSettingField().set(i,j,State.DAMAGED);
                }

                if (i == 4 && j >= 2 && j <= 3) {
                    x_offset_to_place = 0;
                    y_offset_to_place = j -2 ;
                    size_to_place = 2;
                    is_hor_to_place = false;
                    user.getSettingField().set(i,j,State.DAMAGED);
                }
                if (j == 5 && i >= 0 && i <= 1) {
                    x_offset_to_place = i;
                    y_offset_to_place = 0;
                    size_to_place = 2;
                    is_hor_to_place = true;
                    user.getSettingField().set(i,j,State.DAMAGED);
                }

                if (i == 6 && j == 3) {
                    x_offset_to_place = 0;
                    y_offset_to_place = 0;
                    size_to_place = 1;
                    is_hor_to_place = false;
                    user.getSettingField().set(i,j,State.DAMAGED);
                }
                repaint();
            }

            my_x -= user.getOwnField().getStart_x_px();
            my_y -= user.getOwnField().getStart_y_px();
            int my_cell_size_px = user.getOwnField().getCell_size_px();
            int my_offset_px    = user.getOwnField().getOffset_px();
            int my_i = my_x / (cell_size_px + offset_px);
            int my_j = my_y / (cell_size_px + offset_px);

            if (size_to_place <1){
                return;
            }

            if (!(my_i > 9 || my_j > 9 || my_x < 0 || my_y < 0)) {
                Ship ship = new Ship(my_i - x_offset_to_place,my_j - y_offset_to_place,is_hor_to_place,size_to_place);
                if (user.getOwnField().isShipAddable(ship)) {
                    user.getOwnField().addShip(ship);
                    repaint();
                }
            }

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

        if (user.getEnemyField().getKilled() >= 20) {
            System.out.println("User WIN");
            winner = "USER";
            action = Action.FINISH;
            this.paint(getGraphics());

            repaint();
            return;
        }

        if(state != State.MISSED){
            return;
        }


        State computerState = null;
        do{
            repaint();
            computerState = computer.smartShoot(user);
            System.out.println("COMP SHOOT : " + computerState);

            repaint();
            if (user.getOwnField().getKilled() >= 20) {
                action = Action.FINISH;
                winner = "COMPUTER";
                System.out.println("Comp WIN");
                this.paint(getGraphics());
                repaint();
                return;
            }
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
