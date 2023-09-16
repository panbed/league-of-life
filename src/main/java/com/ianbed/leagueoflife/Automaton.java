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
            if (input.retrieve(x, y - 1).getActive())
                livingNeighbors++;

        // down direction
        if (y + 1 < size)
            if (input.retrieve(x, y + 1).getActive())
                livingNeighbors++;

        // left direction
        if (x - 1 >= 0)
            if (input.retrieve(x - 1, y).getActive())
                livingNeighbors++;

        // right direction
        if (x + 1 < size)
            if (input.retrieve(x + 1, y).getActive())
                livingNeighbors++;

        // upleft direction
        if (y - 1 >= 0 && x - 1 >= 0)
            if (input.retrieve(x - 1, y - 1).getActive())
                livingNeighbors++;

        // downleft direction
        if (y + 1 < size && x - 1 >= 0)
            if (input.retrieve(x - 1, y + 1).getActive())
                livingNeighbors++;

        // downright direction
        if (y + 1 < size && x + 1 < size)
            if (input.retrieve(x + 1, y + 1).getActive())
                livingNeighbors++;

        // upright direction
        if (y - 1 >= 0 && x + 1 < size)
            if (input.retrieve(x + 1, y - 1).getActive())
                livingNeighbors++;
    }

    public boolean Life()
    {
        if (input.retrieve(x, y).getActive())
            return !(livingNeighbors < 2 || livingNeighbors > 3);

        return livingNeighbors == 3;
    }

    public boolean Serviettes()
    {
        if (input.retrieve(x, y).getActive())
            return false;

        return (livingNeighbors >= 2 && livingNeighbors <= 4);
    }

    public boolean Diamoeba()
    {
        if (input.retrieve(x, y).getActive())
            return (livingNeighbors >= 5 && livingNeighbors <= 8);

        return livingNeighbors == 3 || (livingNeighbors >= 5 && livingNeighbors <= 8);
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
