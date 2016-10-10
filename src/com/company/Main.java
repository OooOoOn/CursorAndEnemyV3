package com.company;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.nio.charset.Charset;
import java.lang.*;
import java.util.Random;

public class Main {


    public static void main(String[] args) throws InterruptedException {

        //creating list of enemies with random values within the lines

        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < 4; i++) {

            Random rand = new Random();
            int j;

            Enemy enemy = new Enemy(rand.nextInt(19), rand.nextInt(19));
            enemies.add(enemy);
        }

        //player created

        Player player = new Player(10, 10);
        boolean gameOver = false;

        Terminal terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
        terminal.enterPrivateMode();

        while (true) {

            //check if player has lost and print appropriate message

            UpdateScreen(player, terminal, enemies);
            if (gameOver) {

                PrintText(5, 5, "GAME OVER", terminal);
                break;

            }
            MovePlayer(player, terminal);
            gameOver = GameLogic(player, enemies);

        }
    }

    //MOVEMENT

    public static void UpdateScreen(Player player, Terminal terminal, List<Enemy> enemies) {

        terminal.clearScreen();

        //for loops for printing line around board

        for (int i = 1; i < 21; i++) {

            terminal.moveCursor(i, 21);
            terminal.applyForegroundColor(0, 255, 0);
            terminal.putCharacter('-');
        }
        for (int i = 20; i > 0; i--) {

            terminal.moveCursor(i, 0);
            terminal.applyForegroundColor(0, 255, 0);
            terminal.putCharacter('-');
        }

        for (int j = 0; j < 22; j++) {

            terminal.moveCursor(0, j);
            terminal.applyForegroundColor(0, 255, 0);
            terminal.putCharacter('|');
        }

        for (int j = 21; j > -1; j--) {

            terminal.moveCursor(21, j);
            terminal.applyForegroundColor(0, 255, 0);
            terminal.putCharacter('|');
        }


        //player character

        terminal.moveCursor(player.x, player.y);
        terminal.applyForegroundColor(255, 255, 255);
        terminal.putCharacter('O');

        //enemy characters

        for (Enemy enemy : enemies) {
            terminal.moveCursor((int) enemy.x, (int) enemy.y);
            terminal.applyForegroundColor(255, 0, 0);
            terminal.putCharacter('X');
            terminal.applyForegroundColor(255, 0, 0);
            terminal.moveCursor(0, 0);
        }
    }

    //player movement

    private static void MovePlayer(Player player, Terminal terminal) throws InterruptedException {


        Key key;
        do {
            Thread.sleep(5);
            key = terminal.readInput();
        }

        while (key == null);

        System.out.println(key.getCharacter() + " " + key.getKind());



        switch (key.getKind()) {

            case ArrowLeft:
                if (player.x > 1 && player.x < 21) {

                    player.x = player.x - 1;

                }
                break;

            case ArrowRight:
                if (player.x > 0 && player.x < 20) {

                    player.x = player.x + 1;

                }
                break;

            case ArrowUp:
                if (player.y > 1 && player.y < 21) {

                    player.y = player.y - 1;

                }
                break;

            case ArrowDown:
                if (player.y > 0 && player.y < 20) {

                    player.y = player.y + 1;

                }
                break;

        }
    }

    //enemy movement - enemy can move Y + 1 and X + 0.5

    public static boolean GameLogic(Player player, List<Enemy> enemies) {

        float movement = 0.5f;

        for (Enemy enemy : enemies) {

            if (player.x > enemy.x) {

                enemy.x += movement;

            } else if (player.x < enemy.x) {

                enemy.x -= movement;
            }

            if (player.y > enemy.y) {

                enemy.y += 1.0f;

            } else if (player.y < enemy.y) {

                enemy.y -= 1.0f;
            }

        }

        //checking collision between player and AI

        for (Enemy enemy : enemies) {

            if (player.y == enemy.y) {
                if (player.x == enemy.x) {

                    return true;
                } else if (player.x - 0.5f == enemy.x) {

                    return true;
                }


            }

        }
        return false;
    }

    //loop through characters of final message

    private static void PrintText(int x, int y, String message, Terminal terminal) {


        for (int i = 0; i < message.length(); i++) {
            terminal.moveCursor(x, y);
            terminal.putCharacter(message.charAt(i));
            x++;
        }

    }

}


