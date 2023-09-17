package com.ianbed.leagueoflife;

public class Player {
    static int default_mana = 10;
    int x_min, y_min, x, y;
    int mana;

    public void addMana(int add)
    {
        mana += add;
    }

    public boolean attemptCast(int cost)
    {
        if (cost > mana)
            return false;

        mana -= cost;
        return true;
    }

    public int getMana()
    {
        return mana;
    }

    public Player()
    {
        mana = default_mana;
    }

    public Player(int x_min, int y_min, int x, int y)
    {
        this();

        this.x_min = x_min;
        this.y_min = y_min;
        this.x = x;
        this.y = y;
    }

}
