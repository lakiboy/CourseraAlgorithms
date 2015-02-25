public class Percolation
{
    private final int size, TOP, BOTTOM;
    private boolean[] sites;
    private boolean[] fulls;
    private WeightedQuickUnionUF uf;
    
    public Percolation(int N)
    {
        if (N < 1) {
            throw new IllegalArgumentException();
        }
        
        size = N;
        
        // All sites are blocked by default i.e. false.
        sites = new boolean[size * size];
        
        // 0 - root of top row and N^2+1 - root of bottom row.
        uf = new WeightedQuickUnionUF(size * size + 2);
        TOP = 0;
        BOTTOM = size * size + 1;
        
        // Additional structure to fix backwash problem.
        fulls = new boolean[size * size];
    }
    
    public void open(int y, int x)
    {
        if (isOpen(y, x)) {
            return;
        }
        
        int i = xyTo1D(y, x);
        boolean full = false;
        
        // Mark site as opened.
        sites[i - 1] = true;
        
        // Connect with root of top row.
        if (y == 1) {
            uf.union(TOP, i);
            
            // Top sites always full.
            full = true;
        }
        
        // Connect with root of bottom row.
        if (y == size) {
            uf.union(BOTTOM, i);
        }
        
        // Make connections if possible with top, bottom, left, right sites.
        if (isValidCoords(y - 1, x) && isOpen(y - 1, x)) {
            uf.union(i, xyTo1D(y - 1, x));
            
            // Adjacent site is full, then current site is full as well. 
            if (!full && isFull(y - 1, x)) {
                full = true;
            }
        }
        if (isValidCoords(y + 1, x) && isOpen(y + 1, x)) {
            uf.union(i, xyTo1D(y + 1, x));
            
            if (!full && isFull(y + 1, x)) {
                full = true;
            }
        }
        if (isValidCoords(y, x - 1) && isOpen(y, x - 1)) {
            uf.union(i, xyTo1D(y, x - 1));
            
            if (!full && isFull(y, x - 1)) {
                full = true;
            }
        }
        if (isValidCoords(y, x + 1) && isOpen(y, x + 1)) {
            uf.union(i, xyTo1D(y, x + 1));
            
            if (!full && isFull(y, x + 1)) {
                full = true;
            }
        }
        
        if (full) {
            propagateFullness(y, x);
        }
    }
    
    /**
     * Return state of site at (y, x).
     */
    public boolean isOpen(int y, int x)
    {        
        return sites[xyTo1D(y, x) - 1];
    }
    
    /**
     * If site at (y, x) connected with root of top row.
     */
    public boolean isFull(int y, int x)
    {   
        return fulls[xyTo1D(y, x) - 1];
    }
    
    /**
     * Root of top row connected with root of bottom row?
     */
    public boolean percolates()
    {
        return uf.connected(TOP, BOTTOM);
    }
    
    private boolean isValidCoords(int y, int x)
    {
        return x > 0 && x <= size && y > 0 && y <= size;
    }
    
    private int xyTo1D(int y, int x)
    {
        if (!isValidCoords(y, x)) {
            throw new IndexOutOfBoundsException();
        }
        
        return size * (y - 1) + x;
    }
    
    private void propagateFullness(int y, int x)
    {    
        fulls[xyTo1D(y, x) - 1] = true; 
        
        // Propagate to the top.
        if (isValidCoords(y - 1, x) && isOpen(y - 1, x) && !isFull(y - 1, x)) {
            propagateFullness(y - 1, x);
        }
        
        // Propagate to the bottom.
        if (isValidCoords(y + 1, x) && isOpen(y + 1, x) && !isFull(y + 1, x)) {
            propagateFullness(y + 1, x);
        }
        
        // Propagate to the left.
        if (isValidCoords(y, x - 1) && isOpen(y, x - 1) && !isFull(y, x - 1)) {
            propagateFullness(y, x - 1);
        }
        
        // Propagate to the right.
        if (isValidCoords(y, x + 1) && isOpen(y, x + 1) && !isFull(y, x + 1)) {
            propagateFullness(y, x + 1);
        }
    }
}
