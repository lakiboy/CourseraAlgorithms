import java.util.Arrays;

public class Board
{
    private static final char BLANK = (char) 0;
    private final short size;
    private final char[] blocks;
    private int blankPos;
    private int hamming;
    private int manhattan;
    
    public Board(int[][] blocks)
    {
        size = (short) blocks.length;
        
        if (size < 2) {
            throw new IllegalArgumentException();
        }
        
        // Flat array to store 2-dim data.
        this.blocks = new char[size * size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int pos = toPosition(x, y);
                
                // Position in flat array.
                this.blocks[pos] = (char) blocks[y][x];
                
                if (this.blocks[pos] == BLANK) {
                    blankPos = pos;
                } else if (!isAtPosition(pos)) {
                    hamming += 1;
                    manhattan += calcManhattan(pos);
                }
            }
        }
    }
    
    /**
     * Alternative constructor.
     */
    private Board(Board board, int oldPos, int newPos)
    {
        // Copy attributes for original oboard.
        size      = (short) board.dimension();
        hamming   = board.hamming();
        manhattan = board.manhattan();
        blocks    = new char[size * size];
        
        // Create new array copy, swap blocks.
        System.arraycopy(board.blocks, 0, blocks, 0, size * size);
        blocks[oldPos] = board.blocks[newPos];
        blocks[newPos] = board.blocks[oldPos];
        
        // Hamming and manhattan distanced might have changed.
        if (blocks[oldPos] != BLANK && !isAtPosition(oldPos)) {
            hamming += 1;
            manhattan += calcManhattan(oldPos);
        }
        if (blocks[newPos] != BLANK && !isAtPosition(newPos)) {
            hamming += 1;
            manhattan += calcManhattan(newPos);
        }

        // Remove distances from old board.
        if (board.blocks[oldPos] != BLANK && !board.isAtPosition(oldPos)) {
            hamming -= 1;
            manhattan -= board.calcManhattan(oldPos);
        }
        if (board.blocks[newPos] != BLANK && !board.isAtPosition(newPos)) {
            hamming -= 1;
            manhattan -= board.calcManhattan(newPos);
        }
        
        if (blocks[oldPos] == BLANK) {
            blankPos = oldPos;
        } else if (blocks[newPos] == BLANK) {
            blankPos = newPos;
        } else {
            blankPos = board.blankPos;
        }
    }
    
    public int dimension()
    {
        return size;
    }
    
    public int hamming()
    {
        return hamming;
    }
    
    public int manhattan()
    {             
        return manhattan;
    }
    
    /**
     * Board is at goal position when all blocks are at position.
     */
    public boolean isGoal()
    {        
        return hamming == 0;
    }
    
    /**
     * Find twin board.
     */
    public Board twin()
    {
        // Exchange blocks in second row.
        if (blankPos / size == 0) {
            return new Board(this, size, size + 1);
        }
        
        // Exchange block in first row.
        return new Board(this, 0, 1);
    }
    
    public Iterable<Board> neighbors()
    {
        Queue<Board> q = new Queue<Board>();
     
        int x = blankPos % size;
        int y = blankPos / size;
        
        if (x > 0) {
            q.enqueue(new Board(this, blankPos, blankPos - 1));
        }
        if (x < size - 1) {
            q.enqueue(new Board(this, blankPos, blankPos + 1));
        }
        if (y > 0) {
            q.enqueue(new Board(this, blankPos, blankPos - size));
        } 
        if (y < size - 1) {
            q.enqueue(new Board(this, blankPos, blankPos + size));
        }
        
        return q;
    }
    
    public boolean equals(Object object)
    {
        if (object == this) {
            return true;
        }
        if (object == null) { 
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        
        Board board = (Board) object;
        if (board.dimension() != dimension() || board.blankPos != blankPos || board.hamming() != hamming()) {
            return false;
        }
        
        return Arrays.equals(blocks, board.blocks);
    }
    
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        
        result.append(size);
        for (int i = 0; i < blocks.length; i++) {
            if (i % size == 0) {
                result.append("\n");
            }
            result.append(String.format("%2d ", (int) blocks[i]));
        }
        result.append("\n");
        
        return result.toString();
    }
    
    public static void main(String[] args)
    {
        int[][] blocks = new int[3][3];
        
        blocks[0][0] = 7;
        blocks[0][1] = 1;
        blocks[0][2] = 8;
        blocks[1][0] = 2;
        blocks[1][1] = 0;
        blocks[1][2] = 3;
        blocks[2][0] = 4;
        blocks[2][1] = 5;
        blocks[2][2] = 6;
        
        Board board = new Board(blocks);
        
        StdOut.println(board);
        StdOut.println("Goal: " + board.isGoal());
        StdOut.println("Hamming: " + board.hamming());
        StdOut.println("Manhattan: " + board.manhattan());
        StdOut.println("Twin:\n" + board.twin());
        
        StdOut.println("Neighbours:");
        for (Board neighbour : board.neighbors()) {
            StdOut.println("Manhattan: " + neighbour.manhattan());
            StdOut.println("Hamming: " + neighbour.hamming());
            StdOut.println(neighbour);
        }
    }
    
    /**
     * Calculate manhattan distance.
     */
    private int calcManhattan(int pos)
    {
        int target = ((int) blocks[pos]) - 1;
        
        return Math.abs(pos % size - target % size) + Math.abs(pos / size - target / size);
    }
    
    private int toPosition(int x, int y)
    {
        return y * size + x;
    }
    
    private boolean isAtPosition(int pos)
    {
        return ((int) blocks[pos]) == pos + 1;
    }
}
