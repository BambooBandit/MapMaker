package com.mygdx.mapmaker;

import com.badlogic.gdx.Game;

import javax.swing.*;

public class Starter extends Game {
    Integer[][] tiles;
    int mapWidth, mapHeight, tileSize, baseSize;

    @Override
    public void create () {

        System.out.println("Enter the original size of the tile");
        baseSize = Integer.parseInt(JOptionPane.showInputDialog("Enter the original size of the tile"));
        System.out.println("Enter the size of a tile");
        tileSize = Integer.parseInt(JOptionPane.showInputDialog("Enter the size of a tile"));
        System.out.println("Enter the desired map width");
        mapWidth = Integer.parseInt(JOptionPane.showInputDialog("Enter the desired map width"));
        System.out.println("Enter the desired map height");
        mapHeight = Integer.parseInt(JOptionPane.showInputDialog("Enter the desired map height"));
        tiles = new Integer[mapWidth][mapHeight];
        setScreen(new MapMaker(baseSize, tileSize, tiles));
    }

    @Override
    public void dispose () {
    }
}
