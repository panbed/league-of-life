package com.ianbed.leagueoflife;

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
            for (int j = 0; j < size; j++)
            {
                Tile nuevo = new Tile(i, j, new Automaton(this, i, j).Maze());

                if (nuevo.isActive())
                    nuevo.setAge(board[i][j].getAge() + 1);

                boardNew[i][j] = nuevo;
            }

        board = boardNew;
    }

    public void printBoard()
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
                if (board[i][j].isActive())
                    System.out.print(": ");
                else
                    System.out.print(". ");

            System.out.println();
        }
    }

    public Board(int size)
    {
        this.size = size;
        buildBoard();
    }
}
