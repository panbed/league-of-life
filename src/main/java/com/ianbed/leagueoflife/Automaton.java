package com.ianbed.leagueoflife;

public class Automaton
{
    Board input;
    int size, x, y;
    int livingNeighbors;

    public void countLivingNeighbors()
    {
        // up direction
        if (y - 1 >= 0)
            if (input.retrieve(x, y - 1).isActive())
                livingNeighbors++;

        // down direction
        if (y + 1 < size)
            if (input.retrieve(x, y + 1).isActive())
                livingNeighbors++;

        // left direction
        if (x - 1 >= 0)
            if (input.retrieve(x - 1, y).isActive())
                livingNeighbors++;

        // right direction
        if (x + 1 < size)
            if (input.retrieve(x + 1, y).isActive())
                livingNeighbors++;

        // upleft direction
        if (y - 1 >= 0 && x - 1 >= 0)
            if (input.retrieve(x - 1, y - 1).isActive())
                livingNeighbors++;

        // downleft direction
        if (y + 1 < size && x - 1 >= 0)
            if (input.retrieve(x - 1, y + 1).isActive())
                livingNeighbors++;

        // downright direction
        if (y + 1 < size && x + 1 < size)
            if (input.retrieve(x + 1, y + 1).isActive())
                livingNeighbors++;

        // upright direction
        if (y - 1 >= 0 && x + 1 < size)
            if (input.retrieve(x + 1, y - 1).isActive())
                livingNeighbors++;
    }

    public boolean Life()
    {
        if (input.retrieve(x, y).isActive())
            return livingNeighbors == 2 || livingNeighbors == 3;

        return livingNeighbors == 3;
    }

    public boolean Serviettes()
    {
        if (input.retrieve(x, y).isActive())
            return false;

        return (livingNeighbors >= 2 && livingNeighbors <= 4);
    }

    public boolean Diamoeba()
    {
        if (input.retrieve(x, y).isActive())
            return (livingNeighbors >= 5 && livingNeighbors <= 8);

        return livingNeighbors == 3 || (livingNeighbors >= 5 && livingNeighbors <= 8);
    }

    public boolean HighLife()
    {
        if (input.retrieve(x, y).isActive())
            return livingNeighbors == 2 || livingNeighbors == 3;

        return livingNeighbors == 3 || livingNeighbors == 6;
    }

    public boolean Vote()
    {
        if (input.retrieve(x, y).isActive())
            return livingNeighbors >= 4;

        return livingNeighbors >= 5;
    }

    public boolean Maze()
    {
        if (input.retrieve(x, y).isActive())
            return livingNeighbors >= 1 && livingNeighbors <= 5;

        return livingNeighbors == 3;
    }

    public Automaton(Board input, int x, int y)
    {
        this.input = input;
        this.x = x;
        this.y = y;

        size = input.getSize();
        countLivingNeighbors();
    }
}
