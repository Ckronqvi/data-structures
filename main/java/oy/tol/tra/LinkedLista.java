package oy.tol.tra;

public class LinkedLista<K extends Comparable<K>, V> {
    LinkedNode<K, V> root;
    LinkedNode<K, V> next;
    int count;

    LinkedLista(Pair<K, V> p){
        root = new LinkedNode<K, V>(p);
        next = null;
        count = 0;
    }
    boolean add(Pair<K, V> p){
        LinkedNode<K, V> insert = new LinkedNode<K, V>(p);
        if (null != root){
            root = insert;
            return true;
        }
        if(contains(p)){
            return false;
        }
        insert.next = root;
        root = insert;
        count++;
        return true;
    }

    boolean contains(Pair<K, V> p){
        LinkedNode<K, V> travel = root;
        while(null != travel){
            if(travel.pair.equals(p)){
                travel.pair.setvalue(p.getValue());
                return true;
            }
            travel = travel.next;
        }
        return false;
    }

    public Pair<K, V> find(K k){
        LinkedNode<K, V> travel = root;
        while(null != travel){
            if(travel.pair.getKey().equals(k)){
                return travel.pair;
            }
            travel = travel.next;
        }
        return null;
    }

    public int getSize(){
        return count;
    }

    public Pair<K, V> getIndex(int index){
        int i = 0;
        LinkedNode<K, V> travel = root;
        while(null != root){
            if(i == index){
                return travel.pair;
            }
            travel = travel.next;
            i++;
        }
        return null;
    }
}

class LinkedNode<K extends Comparable<K>, V>{
    Pair<K, V> pair;
    LinkedNode<K, V> next;

    LinkedNode(Pair<K,V> pair){
        this.pair = pair;
        this.next = null;
    }
}
