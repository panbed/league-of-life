package com.ianbed.leagueoflife;

public abstract class Automaton
{
    Board input;
    int size, x, y;
    int livingNeighbors;

    public Automaton(Board input, int x, int y)
    {
        this.input = input;
        this.x = x;
        this.y = y;

        size = input.getSize();
    }
}
