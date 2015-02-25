public class Solver
{
    // 8 + 4 + 1 + 3 (padding)
    private boolean solvable = false;
    private int moves = -1;
    private Stack<Board> result;
        
    /**
     * Helper nested class to compare search nodes.
     */
    private static class Node implements Comparable<Node>
    {
        // 8 + 2 + 8 + 4 + 1 + 1 (padding)
        private Board board;
        private short steps;
        private Node prevNode;
        private int priority;
        private boolean twin;
        
        public Node(Board board, short steps, Node prevNode, boolean twin)
        {
            this.board = board;
            this.steps = steps;
            this.prevNode = prevNode;
            this.twin = twin;
            
            // Save priority in separate field to potentially speed up compareTo().
            // 5 bytes will be wasted anyway because of padding.
            priority = steps + board.manhattan();
        }
        
        public int compareTo(Node node)
        {
            int cmp = priority - node.priority;
            if (cmp != 0) {
                return cmp;
            }
            
            // Non-twin node goes first.
            if (twin != node.twin) {
                return twin ? 1 : -1;
            }
            
            // Compare manhattan distances.
            cmp = board.manhattan() - node.board.manhattan();
            if (cmp != 0) {
                return cmp;
            }
            
            // Compare hamming distances.
            return board.hamming() - node.board.hamming();
        }
    }
    
    public Solver(Board initial)
    {
        if (initial == null) {
            throw new NullPointerException();
        }
        
        Node node = new Node(initial, (short) 0, null, false);
        
        // Insert root nodes.
        MinPQ<Node> pq = new MinPQ<Node>();
        pq.insert(node);
        pq.insert(new Node(initial.twin(), (short) 0, null, true));
        
        do {           
            node = pq.delMin();            
            if (node.board.isGoal()) {
                if (node.twin) {
                    return;
                }
                break;
            }
            
            for (Board neighbour : node.board.neighbors()) {
                if (node.prevNode != null && node.prevNode.board.equals(neighbour)) {
                    continue;
                }
                pq.insert(new Node(neighbour, (short) (node.steps + 1), node, node.twin));
            }   
        } while (true);
        
        moves = node.steps;
        solvable = true;
        
        // Write result.
        result = new Stack<Board>();
        do {
            result.push(node.board);
            node = node.prevNode;
        } while (node != null);
    }
    
    public boolean isSolvable()
    {
        return solvable;
    }
    
    public int moves()
    {
        return moves;
    }
    
    public Iterable<Board> solution()
    {
        return result;
    }
    
    /**
     * Run puzzle.
     */
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        int size = in.readInt();
        
        int[][] blocks = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        
        Stopwatch stopwatch = new Stopwatch();
        
        Solver solver = new Solver(new Board(blocks));
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            StdOut.println("Time = " + stopwatch.elapsedTime());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
