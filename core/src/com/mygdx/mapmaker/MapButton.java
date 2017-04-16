package com.mygdx.mapmaker;

import com.badlogic.gdx.graphics.g2d.Sprite;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;

import static com.mygdx.mapmaker.MapMaker.*;

/**
 * Created by Siavash Ranjbar on 4/16/2017.
 */
public class MapButton {

    boolean clickable = false;
    boolean isClicked = false;
    int x;
    int y;
    int width;
    int height;
    Sprite sprite;
    Sprite pressedSprite;
    int id = -200;

    public MapButton(Sprite sprite, Sprite pressedSprite, int x, int y, int width, int height, boolean clickable, int id)
    {
        this.sprite = sprite;
        this.pressedSprite = pressedSprite;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.clickable = clickable;
        this.id = id;
    }

    public MapButton(Sprite sprite, Sprite pressedSprite, int x, int y, int width, int height, boolean clickable)
    {
        this.sprite = sprite;
        this.pressedSprite = pressedSprite;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.clickable = clickable;
    }

    public int getId()
    {
        return id;
    }

    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }
    public Sprite getSprite()
    {
        return sprite;
    }
    public boolean isClicked()
    {
        return isClicked;
    }
    public Sprite getPressedSprite()
    {
        return pressedSprite;
    }
    public Sprite getDrawableSprite()
    {
        if(isClicked)
        {
            return getPressedSprite();
        }
        else
        {
            return getSprite();
        }
    }

    public void setX(int x)
    {
        this.x = x;
    }
    public void setY(int y)
    {
        this.y = y;
    }

    public void setIsClickable(boolean canClick)
    {
        clickable = canClick;
    }

    public boolean isClickable()
    {
        return clickable;
    }

    public boolean isClicked(int x, int y)
    {
        y = (mapHeight * tileSize) - y;
        if(x > this.x && x < this.x + width && y > this.y && y < this.y + height && clickable)
        {
            isClicked = true;
            if(id >= -1)
            {
                if(id == -1 && selectedTile != -1)
                {
                    lastSelectedTile = selectedTile;
                }
                selectedTile = id;
            }
            else if(id == -2)
            {
                buttonId = id;
                System.out.println("Enter the property to be added or removed from tile " + selectedTile);
                String property = JOptionPane.showInputDialog("Enter the property to be added or removed from tile");
                if(property == null)
                {
                    return true;
                }
                if(properties.size == 0)
                {
                    MapTile tile = new MapTile(selectedTile, property);
                    properties.add(tile);
                }
                else
                {
                        boolean exists = false;
                        for (int i = 0; i < properties.size; i++) {
                            if (properties.get(i).getId() == selectedTile) {
                                exists = true;
                                properties.get(i).addProperty(property);
                            }
                        }
                        if (!exists) {
                            MapTile tile = new MapTile(selectedTile, property);
                            properties.add(tile);
                    }
                }
            }
            else if(id == -3)
            {
                buttonId = id;
                generateFile();
            }
            else if(id == -4)
            {
                buttonId = id;
                tiles = counterRotation(tiles);
            }
            else if(id == -5)
            {
                buttonId = id;
                tiles = clockRotation(tiles);
            }
            else if(id == -6)
            {
                for(int i = 0; i < tiles.length; i++)
                {
                    for(int j = 0; j < tiles[0].length; j++)
                    {
                        tiles[i][j] = selectedTile;
                    }
                }
            }
            return true;
        }
        return false;
    }
    public void setClicked(boolean click)
    {
        isClicked = click;
    }

    public void generateFile()
    {
        String fileName = JOptionPane.showInputDialog("Enter the name of the map file");

        if(fileName == null)
        {
            return;
        }
        System.out.println("Sorting data...");
        for(int i = 0; i < tiles.length; i++)
        {
            for(int j = 0; j < tiles[0].length; j++)
            {
                boolean exists;
                while(1 == 1)
                {
                    exists = false;
                    for (int k = 0; k < properties.size; k++)
                    {
                        if (tiles[i][j] != null && properties.get(k).getId() == tiles[i][j])
                        {
                            exists = true;
                        }
                    }
                    if (!exists && tiles[i][j] != null)
                    {
                        MapTile tile = new MapTile(tiles[i][j]);
                        properties.add(tile);
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        properties.sort();
        for(int i = 0; i < properties.size; i++)
        {
            if(properties.get(i).getProperties().size == 0)
            {
                properties.get(i).addProperty("none");
            }
        }
        System.out.println("Generating map file...");

        try{
            PrintWriter writer = new PrintWriter(fileName + ".csv", "UTF-8");
            writer.println(tileSize);
            writer.println(mapWidth);
            writer.println(mapHeight);
            writer.println(properties.size);
            for(int i = 0; i < properties.size; i ++)
            {
                writer.print(properties.get(i).getId());
                for(int j = 0; j < properties.get(i).getProperties().size; j++)
                {
                    writer.print("," + properties.get(i).getProperties().get(j));
                }
                writer.println();
            }

            Integer[][] mapTiles = counterRotation(tiles);

            for(int i = 0; i < mapTiles.length; i++)
            {
                for(int j = 0; j < mapTiles[0].length - 1; j++)
                {
                    if(mapTiles[i][j] == null)
                    {
                        writer.print("-1,");
                    }
                    else
                    {
                        writer.print(mapTiles[i][j] + ",");
                    }
                }
                if(mapTiles[i][mapTiles[0].length - 1] == null)
                {
                    writer.print("-1");
                    writer.println();
                }
                else
                {
                    writer.print(mapTiles[i][mapTiles[0].length - 1]);
                    writer.println();
                }
            }
            writer.close();
        } catch (IOException e) {
        }
        System.out.println("Map made");
    }

    public Integer[][] counterRotation(Integer[][] array) {
        Integer[][] rotatedArray = new Integer[array[0].length][array.length];
        int positionFactor = rotatedArray.length - 1;
        for (int i = 0; i < rotatedArray.length; i++) {
            for (int j = 0; j < rotatedArray[i].length; j++) {
                int a = i - positionFactor;
                a = (a < 0) ? -a : a;
                rotatedArray[i][j] = array[j][a];
            }
        }
        return rotatedArray;
    }
    public Integer[][] clockRotation(Integer[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        Integer[][] ret = new Integer[n][m];
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                ret[c][m-1-r] = mat[r][c];
            }
        }
        return ret;
    }
}
