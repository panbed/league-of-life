public abstract class Automaton
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

    public Automaton(Board input, int x, int y)
    {
        this.input = input;
        this.x = x;
        this.y = y;

        size = input.getSize();
        countLivingNeighbors();
    }
}
