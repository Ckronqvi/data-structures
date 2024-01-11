package oy.tol.tra;

/**
 * Interface created for BST traversal operations
 */
public interface Action<K extends Comparable<K>, V> {
    void action(Node<K, V> node);
}