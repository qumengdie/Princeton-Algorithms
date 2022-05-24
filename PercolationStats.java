import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] thresholds;
    private final int t;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n and trianls should be > 0");

        this.t = trials;
        thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation pl = new Percolation(n);
            while (!pl.percolates()) {
                int x = StdRandom.uniform(1, n + 1);
                int y = StdRandom.uniform(1, n + 1);
                pl.open(x, y);
            }
            int num = pl.numberOfOpenSites();
            double threshold = (double) num / (n * n);
            thresholds[i] = threshold;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(t);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats pls = new PercolationStats(n, t);
        System.out.println("mean                    = " + pls.mean());
        System.out.println("stddev                  = " + pls.stddev());
        System.out.println("95% confidence interval = ["
                + pls.confidenceLo() + ", " + pls.confidenceHi() + "]");
    }

}
