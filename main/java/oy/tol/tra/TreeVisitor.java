package oy.tol.tra;

/**
 * Interface created for visiting tree nodes.
 */
public interface TreeVisitor<K extends Comparable<K>, V> {
    void visit(Node<K, V> node);
}
