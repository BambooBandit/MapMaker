package com.mygdx.mapmaker;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Siavash Ranjbar on 4/16/2017.
 */
public class MapTile implements Comparable<MapTile> {

    Array<String> properties;
    int id;

    public MapTile(int id)
    {
        properties = new Array<String>();
        System.out.println("created");
        this.id = id;
    }

    public MapTile(int id, String property)
    {
        properties = new Array<String>();
        System.out.println("created");
        addProperty(property);
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public Array<String> getProperties()
    {
        return properties;
    }

    public void addProperty(String string)
    {
        if(string == null)
        {
            return;
        }
        if(!properties.contains(string, false)) {
            System.out.println("added " + string);
            if(properties.contains("none", false))
            {
                properties.removeValue("none", false);
            }
            properties.add(string);
        }
        else if(properties.contains(string, false))
        {
            properties.removeValue(string, false);
            System.out.println("removed " + string);
        }
    }

    @Override
    public int compareTo(MapTile other) {
        int first = getId();
        int second = other.getId();

        if(first > second)
        {
            return 1;
        }
        else if(first < second)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
}
