package oy.tol.tra;

/**
 * Tree Node class created for AVL self-balancing tree.
 * Keeps track of the height of each node.
 */
public class Node<K extends Comparable<K>, V> {

    private Pair<K, V> keyValue; // Data
    private int keyHash;         // Key
    private int height = 1;
    private Node<K, V> left = null;
    private Node<K, V> right = null;
    private LinkedLista<K, V> chain = null;

    

    Node(K key, V value) {
        keyValue = new Pair<K, V>(key, value);
        height = 1; // Default height.
        keyHash = key.hashCode(); // This way the hash needs to be calculated only once.
    }

    public boolean add(Pair<K, V> p){
        if (null == chain){
            chain = new LinkedLista<>(p);
            return true;
        }
        return chain.add(p);
    }

    public boolean hasNext(){
        return (null != chain) ? true : false;
    }

    public int chainLength(){
        return chain.getSize();
    }

    public Pair<K, V> chainGetIndex(int index){
        return chain.getIndex(index);
    }

    public Pair<K, V> find(K k){
        if(k.equals(keyValue.getKey())){
            return keyValue;
        }
        if(chain == null){
            return null;
        }
        return chain.find(k);
    }
 
    public int getHash() {
        return keyHash;
    }

    public Pair<K, V> getPair() {
        return keyValue;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Accepts the visitor.
     * @param visitor TreeVisitor.
     * @see oy.tol.tra.TreeVisitor
     */
    public void accept(TreeVisitor<K, V> visitor) {
        visitor.visit(this);
    }

    public K getKey() {
        return keyValue.getKey();
    }

    public V getValue() {
        return keyValue.getValue();
    }

    public Node<K, V> getLeft() {
        return left;
    }

    public void setLeft(Node<K, V> left) {
        this.left = left;
    }

    public Node<K, V> getRight() {
        return right;
    }

    public void setRight(Node<K, V> right) {
        this.right = right;
    }
}