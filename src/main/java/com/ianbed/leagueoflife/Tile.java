package com.ianbed.leagueoflife;

public class Tile
{
    boolean active;
    int age;
    int x_pos, y_pos;

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public Tile()
    {
        active = false;
        age = 0;
    }

    public Tile(int x, int y)
    {
        this();

        x_pos = x;
        y_pos = y;
    }

    public Tile(int x, int y, boolean active)
    {
        this(x, y);

        this.active = active;
    }
}
