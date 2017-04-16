package com.mygdx.mapmaker;

import com.badlogic.gdx.Game;

import java.util.Scanner;

public class Starter extends Game {
    Integer[][] tiles;
    int mapWidth, mapHeight, tileSize, baseSize;

    Scanner scanner;

    @Override
    public void create () {

        scanner = new Scanner(System.in);
        System.out.println("Enter the original size of the tile");
     //   baseSize = scanner.nextInt();
        baseSize = 64; // delete later
        System.out.println("Enter the size of a tile");
      //  tileSize = scanner.nextInt();
        tileSize = 64; //delete later
        System.out.println("Enter the desired map width");
      //  mapWidth = scanner.nextInt();
        mapWidth = 15; //delete later
        System.out.println("Enter the desired map height");
     //   mapHeight = scanner.nextInt();
        mapHeight = 10; // delete later
        tiles = new Integer[mapWidth][mapHeight];
        setScreen(new MapMaker(baseSize, tileSize, tiles));
    }

    @Override
    public void dispose () {
    }
}
