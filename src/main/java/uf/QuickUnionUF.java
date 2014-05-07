package uf;
/****************************************************************************
 *  Compilation:  javac QuickUnionUF.java
 *  Execution:  java QuickUnionUF < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  Quick-union algorithm.
 *
 ****************************************************************************/

/**
 * The <tt>QuickUnionUF</tt> class represents a union-find data structure.
 * It supports the <em>union</em> and <em>find</em> operations, along with
 * methods for determinig whether two objects are in the same component
 * and the total number of components.
 * <p/>
 * This implementation uses quick union.
 * Initializing a data structure with <em>N</em> objects takes linear time.
 * Afterwards, <em>union</em>, <em>find</em>, and <em>connected</em> take
 * time linear time (in the worst case) and <em>count</em> takes constant
 * time.
 * <p/>
 * For additional documentation, see <a href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class QuickUnionUF {
    private int[] id;    // id[i] = parent of i
    private int count;   // number of components

    /**
     * Initializes an empty union-find data structure with N isolated components 0 through N-1.
     *
     * @param N the number of objects
     * @throws java.lang.IllegalArgumentException if N < 0
     */
    public QuickUnionUF(int N) {
        id = new int[N];
        count = N;
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between 1 and N)
     */
    public int count() {
        return count;
    }

    public int root(int p) {
        while (p != id[p]) {
            p = id[p];
        }
        return p;
    }

    /**
     * Are the two sites <tt>p</tt> and <tt>q</tt> in the same component?
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return <tt>true</tt> if the sites <tt>p</tt> and <tt>q</tt> are in the same
     * component, and <tt>false</tt> otherwise
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= p < N and 0 <= q < N
     */
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    /**
     * Merges the component containing site<tt>p</tt> with the component
     * containing site <tt>q</tt>.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= p < N and 0 <= q < N
     */
    public void union(int p, int q) {
        int rootP = root(p);
        int rootQ = root(q);
        if (rootP == rootQ) {
            return;
        }
        id[rootP] = rootQ;
        count--;
    }

//    public static void main(String[] args) {
//        int N = StdIn.readInt();
//        QuickUnionUF uf = new QuickUnionUF(N);
//        while (!StdIn.isEmpty()) {
//            int p = StdIn.readInt();
//            int q = StdIn.readInt();
//            if (uf.connected(p, q)) continue;
//            uf.union(p, q);
//            StdOut.println(p + " " + q);
//        }
//        StdOut.println(uf.count() + " components");
//    }

}
