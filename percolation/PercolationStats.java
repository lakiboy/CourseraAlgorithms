public class PercolationStats
{
    private final int size;
    private double[] results;
    
    public PercolationStats(int N, int T)
    {
        if (N < 1 || T < 1) {
            throw new IllegalArgumentException();
        }
        
        size = N;
        results = new double[T];
        
        run();
    }
    
    /**
     * Run Monte Carlo simulation.
     */
    private void run()
    {   
        int opened, y, x;
        int[] sites = new int[size * size];
        Percolation p;
        
        for (int i = 0; i < sites.length; i++) {
            sites[i] = i + 1;
        }
        
        for (int i = 0; i < results.length; i++) {
            p = new Percolation(size);
            opened = 0; // Maintain the amount of open sites.
            
            // Shuffle sites before each simulation.
            StdRandom.shuffle(sites);
            
            do {
                // Select random (y, x) site.
                x = sites[opened] % size;
                y = sites[opened] / size;
                if (x == 0) {
                    x = size; // Do not need to increase Y.
                } else {
                    y++; // Increae Y by 1 as division was with remainder.
                }
                
                opened++;
                p.open(y, x);
            } while (!p.percolates());
            
            results[i] = (double) opened / (size * size); // Percolation threshold.
        }
    }
    
    public double mean()
    {
        return StdStats.mean(results);
    }
    
    public double stddev()
    {
        return StdStats.stddev(results);
    }

    public double confidenceLo()
    {
        return mean() - (1.96 * stddev() / Math.sqrt(results.length));
    }
    
    public double confidenceHi()
    {
        return mean() + (1.96 * stddev() / Math.sqrt(results.length));
    }
    
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        
        PercolationStats stats = new PercolationStats(N, T);
        
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
