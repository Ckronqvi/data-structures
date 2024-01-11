package oy.tol.tra;

public class KeyValueBSearchTree<K extends Comparable<K>, V> implements Dictionary<K, V> {

    private Node<K, V> root = null;
    private int count = 0;

    @Override
    public Type getType() {
        return Type.BST;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public String getStatus() {
        return String.format("Tree has max depth of: %d%n", getHeight(root));
    }

    @Override
    public boolean add(K key, V value) throws IllegalArgumentException, OutOfMemoryError {
        if (null == key || null == value) {
            throw new IllegalArgumentException();
        }

        int keyHash = key.hashCode(); // This is used for comparing. Only needs to calculate once.
        count++;
        this.root = insert(key, value, root, keyHash);
        return true;
    }

    private Node<K, V> insert(K key, V value, Node<K, V> node, int keyHash) throws OutOfMemoryError {
        if (null == node) {
            return new Node<K, V>(key, value);
        }
        try {
            if (keyHash < node.getHash()) {
                node.setLeft(insert(key, value, node.getLeft(), keyHash));
            } else if (keyHash > node.getHash()) {
                node.setRight(insert(key, value, node.getRight(), keyHash));
            } else {
                if(node.getKey().equals(key)){
                    node.getPair().setvalue(value); //Same value
                } else {
                    Pair<K, V> p = new Pair<K, V>(key, value);
                    if(!node.add(p)){ //Chaining.
                        count--;
                    }
                }
                return node;
            }
        } catch (Exception e) {
            throw new OutOfMemoryError();
        }
        // Update the node height.
        heightUpdate(node);
        // Fix the disbalance.
        return rotation(node);
    }

    /**
     * Updates the height value of the node
     * @param node Node whos height to update.
     */
    private void heightUpdate(Node<K, V> node) {
        int maxH = Math.max(height(node.getLeft()), height(node.getRight()));
        node.setHeight(maxH + 1); //max height of child nodes plus 1.
    }

    /**
     * Returns the height value of the node.
     * @param node whos height to get.
     * @return the height of the node. 0 if null.
     */
    private int height(Node<K, V> node) {
        return null != node ? node.getHeight() : 0;
    }

    /**
     * Returns the balance from the given node.
     * @param node Node whos balance we wan't to calculate
     * @return Height difference between the left leaf and the right leaf.
     */
    private int balance(Node<K, V> node) {
        return null != node ? height(node.getLeft()) - height(node.getRight())
                : 0;
    }

    /**
     * Rotates the tree if balance difference is great enough.
     * By doing this we avoid degenerate trees and keep the total height
     * of the tree at minimum.
     * @param node Node to performe the rotation on.
     * @return If rotation happened, returns the new "root" node. Else returns the input node.
     */
    private Node<K, V> rotation(Node<K, V> node) {
        int balance = balance(node);

        // Right heavy
        if (balance < -1) {
            if (balance(node.getRight()) > 0) {
                node.setRight(rightRotation(node.getRight())); // Right-left
            }
            return leftRotation(node);
        }
        // Left heavy
        if (balance > 1) {
            if (balance(node.getLeft()) < 0) {
                node.setLeft(leftRotation(node.getLeft())); // Left-Right.
            }
            return rightRotation(node);
        }
        return node;
    }

    /**
     * Performces a left-rotation.
     * @param node "parent" node, the node which the rotation is performed 
     * @return new "parent" node.
     */
    private Node<K, V> leftRotation(Node<K, V> node) {
        Node<K, V> right = node.getRight();
        Node<K, V> center = right.getLeft();
        right.setLeft(node);
        node.setRight(center);
        //Updating the heights of both nodes.
        heightUpdate(node);
        heightUpdate(right);
        return right; //Return the new parent node.
    }
    /**
     * Performces a right-rotation.
     * @param node "parent" node, the node which the rotation is performed 
     * @return new "parent" node.
     */
                                                                                     // RIGHT ROTATION //
    private Node<K, V> rightRotation(Node<K, V> node) {     //||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||//
        Node<K, V> left = node.getLeft();                   //                                                                    //
        Node<K, V> center = left.getRight();                //                                                                    //       
        left.setRight(node);                                //                                                                    // 
        node.setLeft(center);                               //                   (20)   <--- node     ==>         (10)            //
        heightUpdate(node);                                 //                  /    \                ==>        /    \           //
        heightUpdate(left);                                 //  left node --->(10)   (null)           ==>      (5)   (20)         //
        return left;                                        //                /  \                    ==>             /  \        //
                                                            //              (5)  (7)  <--center                     (7)  (null)   //
                                                            //                                                                    // 
                                                            //||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||//
    }

    @Override
public V find(K key) throws IllegalArgumentException {
    if (null == key) {
        throw new IllegalArgumentException();
    }
    int hashKey = key.hashCode();
    Node<K, V> current = root;
    while (current != null) {
        if (hashKey == current.getHash()) {
            Pair<K,V> pair = current.find(key);
            return pair != null ? pair.getValue() : null;
        }
        if (hashKey < current.getHash()) {
            current = current.getLeft();
        } else {
            current = current.getRight();
        }
    }
    return null;
}

    /**
     * Finds the value with given key
     * @param key key to the value
     * @param node root node.
     * @return Node with corresponding key, or null if not found.
     */
    private Pair<K, V> recursiveFind(int hash, Node<K, V> node, K k) {
        if (null == node) {
            return null;
        }
        if(hash == node.getHash()){
            if(node.getKey().equals(k)){
                return node.getPair();
            } else {
                if(node.hasNext()){
                    Pair<K, V> p = node.find(k);
                    return (null != p) ? p : null;
                } else {
                    return null;
                }
            }
        }
        if (hash < node.getHash()) {
            return recursiveFind(hash, node.getLeft(), k);
        }
        return recursiveFind(hash, node.getRight(), k);
    }

    @Override
    public void ensureCapacity(int size) throws OutOfMemoryError {
        return;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<K, V>[] toSortedArray() {
        Pair<K, V>[] arr = (Pair<K, V>[]) new Pair[count];
        if (null == root) {
            return null;
        }
        Action<K, V> action = new Action<K, V>() {
            int i = 0;

            @Override
            public void action(Node<K, V> node) {
                if(!node.hasNext()){
                    arr[i++] = node.getPair();
                } else {
                    arr[i++] = node.getPair();
                    for(int i = 0; i < node.chainLength(); i++){
                        arr[i++] = node.chainGetIndex(i);
                    }
                }
            }
        };
        TreeVisitor<K, V> visitor = new VisitInorder(action);
        visitor.visit(root);
        Algorithms.fastSort(arr, 0, arr.length - 1);
        return arr;
    }

    @Override
    public void compress() throws OutOfMemoryError {
        return;
    }

    /**
     * Calculates the height of the tree recurively.
     * @param Root Node
     * @return The height of the tree.
     */
    public int getTreeHeight(Node<K, V> node) {
        if (null == node) {
            return -1;
        }
        int left = getTreeHeight(node.getLeft());
        int right = getTreeHeight(node.getRight());
        return left > right ? left++ : right++;
    }

    class VisitInorder implements TreeVisitor<K, V> {

        private Action<K, V> some;

        public VisitInorder(Action<K, V> action) {
            this.some = action;
        }

        @Override
        public void visit(Node<K, V> node) {
            Node<K, V> left = node.getLeft();
            if (null != left) {
                left.accept(this);
            }
            some.action(node);
            Node<K, V> right = node.getRight();
            if (null != right) {
                right.accept(this);
            }
        }
    }

    /**
     * Calculates the height if the Node.
     * @param root Node whos height to calculate.
     * @return Amount of nodes between the Node and the farthest null.
     */
    public int getHeight(Node<K, V> root) {
        if (root != null)
            return 1 + Math.max(getHeight(root.getLeft()), getHeight(root.getRight()));
        else
            return 0;
    }
}
