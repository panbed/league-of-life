package com.ianbed.leagueoflife;

public class Algorithms {
    public static int countLivingNeighbors(Board input, int x, int y, int size)
    {
        int livingNeighbors = 0;

        // up direction
        if (y - 1 >= 0)
            if(input.retrieve(x, y - 1).getActive())
                livingNeighbors++;

        // down direction
        if (y + 1 < size)
            if(input.retrieve(x, y + 1).getActive())
                livingNeighbors++;

        // left direction
        if (x - 1 >= 0)
            if(input.retrieve(x - 1, y).getActive())
                livingNeighbors++;

        // right direction
        if (x + 1 < size)
            if(input.retrieve(x + 1, y).getActive())
                livingNeighbors++;

        // upleft direction
        if (y - 1 >= 0 && x - 1 >= 0)
            if(input.retrieve(x - 1, y - 1).getActive())
                livingNeighbors++;

        // downleft direction
        if (y + 1 < size && x - 1 >= 0)
            if(input.retrieve(x - 1, y + 1).getActive())
                livingNeighbors++;

        // downright direction
        if (y + 1 < size && x + 1 < size)
            if(input.retrieve(x + 1, y + 1).getActive())
                livingNeighbors++;

        // upright direction
        if (y - 1 >= 0 && x + 1 < size)
            if(input.retrieve(x + 1, y - 1).getActive())
                livingNeighbors++;

        return livingNeighbors;
    }

    public static boolean WillLiveLiving(Board input, int x, int y)
    {
        int livingNeighbors;
        int size = input.getSize();

        livingNeighbors = countLivingNeighbors(input, x, y, size);

        if (livingNeighbors < 2 || livingNeighbors > 3)
            return false;

        return true;
    }

    public static boolean WillComeBack(Board input, int x, int y)
    {
        int livingNeighbors;
        int size = input.getSize();

        livingNeighbors = countLivingNeighbors(input, x, y, size);

        if (livingNeighbors == 3)
            return true;

        return false;
    }
}
