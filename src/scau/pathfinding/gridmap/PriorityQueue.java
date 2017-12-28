package scau.pathfinding.gridmap;

import java.util.NoSuchElementException;

/**
 * 使用二叉堆实现的索引优先队列
 * <p>
 * 本实现参照了
 * <a href="http://algs4.cs.princeton.edu/24pq/IndexMaxPQ.java.html">IndexMaxPQ.java</a>
 *
 * @param <Key> 优先队列键值key的类型
 * @author Link
 */
public class PriorityQueue<Key extends Comparable<Key>> {

    private int maxN;        // 当前优先队列能够容纳的元素最大数量
    private int n;           // 当前优先队列的元素数量
    private int[] pq;        // 二叉堆（以序号1开始）
    private int[] qp;        // 用于反查pq，qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys;      // keys[i] = 元素i的优先级

    /**
     * 初始化一个当前最大容量为 maxN 的优先队列
     *
     * @param maxN 当前最大容量
     * @throws IllegalArgumentException 若 {@code maxN < 0}
     */
    public PriorityQueue(int maxN) {
        if (maxN < 0) {
            throw new IllegalArgumentException();
        }
        this.maxN = maxN;
        n = 0;
        keys = (Key[]) new Comparable[maxN + 1];
        pq = new int[maxN + 1];
        qp = new int[maxN + 1];
        for (int i = 0; i <= maxN; i++) {
            qp[i] = -1;
        }
    }

    /**
     * 若优先队列为空则返回 true
     *
     * @return {@code true} 若优先队列为空
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * 判断索引i是否包含在优先队列中
     *
     * @param i 索引
     * @return 若索引i包含在优先队列中则返回 true
     * @throws IndexOutOfBoundsException 除非 {@code 0 <= i < maxN}
     */
    public boolean contains(int i) {
        if (i < 0 || i >= maxN) {
            throw new IndexOutOfBoundsException();
        }
        return qp[i] != -1;
    }

    /**
     * 返回当前优先队列中元素的个数
     *
     * @return 当前优先队列中元素的个数
     */
    public int size() {
        return n;
    }

    /**
     * 将键值 key 与索引 i 进行关联
     *
     * @param i   索引
     * @param key 键值
     * @throws IndexOutOfBoundsException 除非 {@code 0 <= i < maxN}
     * @throws IllegalArgumentException  索引 i 早已被关联 {@code i}
     */
    public void insert(int i, Key key) {
        if (i < 0 || i >= maxN) {
            throw new IndexOutOfBoundsException();
        }
        if (contains(i)) {
            throw new IllegalArgumentException("index is already in the priority queue");
        }
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }

    /**
     * 移除最小键并返回其索引
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException 当前优先队列为空
     */
    public int delMin() {
        if (n == 0) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        int min = pq[1];
        exch(1, n--);
        sink(1);
        assert min == pq[n + 1];
        qp[min] = -1;        // delete
        keys[min] = null;    // to help with garbage collection
        pq[n + 1] = -1;        // not needed
        return min;
    }

    /**
     * 改变索引对应的键值 {@code i}
     *
     * @param i   索引
     * @param key 键值
     * @throws IndexOutOfBoundsException 除非 {@code 0 <= i < maxN}
     * @throws NoSuchElementException    没有键与索引 i 关联 {@code i}
     */
    public void changeKey(int i, Key key) {
        if (i < 0 || i >= maxN) {
            throw new IndexOutOfBoundsException();
        }
        if (!contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
    }

    /**
     * 判断索引i对应的键值是否比j对应的键值大
     *
     * @param i
     * @param j
     * @return true, 若索引i对应的键值比j对应的键值大, 否则为 false
     */
    private boolean greater(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    /**
     * 交换索引i和j指向的键
     *
     * @param i
     * @param j
     */
    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    /**
     * 上浮索引k对应的键
     *
     * @param k
     */
    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    /**
     * 下沉索引k对应的键
     *
     * @param k
     */
    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && greater(j, j + 1)) {
                j++;
            }
            if (!greater(k, j)) {
                break;
            }
            exch(k, j);
            k = j;
        }
    }
}
