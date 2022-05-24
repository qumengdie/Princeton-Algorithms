import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private final int n;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufTop;
    private int cnt = 0;
    private final int sz;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n should be > 0");

        this.n = n;

        // initialize each site in an n-by-n grid to be blocked
        grid = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }

        // Initializes an empty union-find data structure
        sz = n * n + 2;    // +2 virtual sites on the top and bottom
        uf = new WeightedQuickUnionUF(sz);
        ufTop = new WeightedQuickUnionUF(sz - 1);

        // connect the first row with the virtual top site
        // and connect the last row with the virtual bottom site
        for (int i = 1; i <= n; i++) {
            uf.union(0, i);
            uf.union(sz - 1, sz - 1 - i);
            ufTop.union(0, i);
        }
    }

    // Throw an IllegalArgumentException if any argument to
    // open(), isOpen(), or isFull() is outside its prescribed range.
    private void isValid(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Input value out of range");
    }

    // get the id of a site
    private int getId(int row, int col) {
        return col + (row - 1) * n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        isValid(row, col);
        if (!grid[row - 1][col - 1]) {
            grid[row - 1][col - 1] = true;
            cnt++;
        }

        // connect it to its open neighbors
        int curId = getId(row, col);
        int[] direction = {-1, 0, 1, 0, -1};
        for (int i = 0; i < 4; i++) {
            int x = row + direction[i];
            int y = col + direction[i + 1];
            if (x > 0 && x <= n && y > 0 && y <= n) {
                int id = getId(x, y);
                if (isOpen(x, y)) {
                    uf.union(id, curId);
                    ufTop.union(id, curId);
                }
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        isValid(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        isValid(row, col);
        return isOpen(row, col) && ufTop.find(0) == ufTop.find(getId(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.cnt;
    }

    // does the system percolate?
    public boolean percolates() {
        if (n == 1) return isOpen(1, 1);
        return uf.find(0) == uf.find(sz - 1);
    }

}
