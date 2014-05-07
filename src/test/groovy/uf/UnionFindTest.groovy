package uf

/**
 * @author Serge Pruteanu
 */
class UnionFindTest extends GroovyTestCase {

    private static List<ArrayList<Integer>> getConnectPairs() {
        return [[3, 4], [4, 9], [8, 0], [2, 3], [5, 6], [5, 9], [7, 3], [4, 8], [6, 1]]
    }

    void 'test quick find'() {
        final uf = new QuickFindUF(10)
        final ufSeq = getConnectPairs()
        for (final pair : ufSeq) {
            uf.union(pair[0] as int, pair[1] as int)
        }
        assertEquals([1, 1, 1, 1, 1, 1, 1, 1, 1, 1,], uf.id as List)
        for (final pair : ufSeq) {
            assertTrue uf.connected(pair[0] as int, pair[1] as int)
        }
    }

    void 'test quick union'() {
        final uf = new QuickUnionUF(10)
        final ufSeq = getConnectPairs()
        for (final pair : ufSeq) {
            uf.union(pair[0] as int, pair[1] as int)
        }
        assertEquals([1, 1, 9, 4, 9, 6, 9, 9, 0, 0,], uf.id as List)
        for (final pair : ufSeq) {
            assertTrue uf.connected(pair[0] as int, pair[1] as int)
        }
    }

    void 'test weighted quick union'() {
        final uf = new WeightedQuickUnionUF(10)
        final ufSeq = getConnectPairs()
        for (final pair : ufSeq) {
            uf.union(pair[0] as int, pair[1] as int)
        }
        assertEquals([8, 3, 3, 3, 3, 3, 5, 3, 3, 3], uf.id as List)
        assertEquals([1, 1, 1, 10, 1, 2, 1, 1, 2, 1], uf.sz as List)
        for (final pair : ufSeq) {
            assertTrue uf.connected(pair[0] as int, pair[1] as int)
        }
    }

    void 'test weighted compressed quick union'() {
        final uf = new WeightedCompressedQUF(10)
        final ufSeq = getConnectPairs()
        for (final pair : ufSeq) {
            uf.union(pair[0] as int, pair[1] as int)
        }
        assertEquals([8, 5, 3, 5, 5, 5, 5, 5, 5, 3], uf.id as List)
        assertEquals([0, 0, 0, 1, 0, 2, 0, 0, 1, 0], uf.rank as List)
        for (final pair : ufSeq) {
            assertTrue uf.connected(pair[0] as int, pair[1] as int)
        }
    }

}
