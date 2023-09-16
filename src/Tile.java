public class Tile
{
    boolean active;
    int x_pos, y_pos;

    public boolean getActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public Tile()
    {
        active = false;
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
