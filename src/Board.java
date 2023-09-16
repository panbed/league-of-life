public class Board
{
    int size;
    Tile[][] board;

    public int getSize()
    {
        return size;
    }

    public Tile retrieve(int x, int y)
    {
        return board[x][y];
    }

    public void buildBoard()
    {
        board = new Tile[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = new Tile(i, j);
    }

    public void progressGeneration()
    {
        Tile[][] boardNew = new Tile[size][size];

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
//                if (board[i][j].getActive())
//                {
//                    boardNew[i][j] = new Tile(i, j, Algorithms.WillLiveLiving(this, i, j));
//                }
//                else
//                {
//                    boardNew[i][j] = new Tile(i, j, Algorithms.WillComeBack(this, i, j));
//                }

                boardNew[i][j] = new Tile(i, j, Algorithms.Diamoeba(this, i, j));
            }
        }

        board = boardNew;
    }

    public void printBoard()
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
                if (board[i][j].getActive())
                    System.out.print(": ");
                else
                    System.out.print(". ");

            System.out.println();
        }
    }

    public Board(int size) throws Exception
    {
        this.size = size;
        buildBoard();
//        board[5][5].setActive(true);
//        board[5][4].setActive(true);
//        board[5][3].setActive(true);
//
//        board[4][5].setActive(true);
//        board[3][4].setActive(true);

        board[25][25].setActive(true);
        board[25][24].setActive(true);
        board[24][24].setActive(true);
        board[24][25].setActive(true);


        while (true)
        {
            progressGeneration();
            printBoard();
            Thread.sleep(1000);
        }
    }
}
