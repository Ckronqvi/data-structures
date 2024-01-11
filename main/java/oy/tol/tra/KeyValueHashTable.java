package oy.tol.tra;

public class KeyValueHashTable<K extends Comparable<K>, V> implements Dictionary<K, V> {
    private static final int DEFAULT_SIZE = 1024;
    private static final float loadFactor = 0.60f;
    private static float growRate = 2;
    private int table_size;

    private Pair<K, V>[] table; // The hash table.
    private int maxDP; // The largest displacement of any element.
    private int size; // The size of the table.
    private int collissions;
    private int rehashCount;
    private int probeCount;

    public KeyValueHashTable() {
        ensureCapacity(DEFAULT_SIZE);
    }

    public KeyValueHashTable(int size) {
        if (size < DEFAULT_SIZE) {
            size = DEFAULT_SIZE;
        }
        ensureCapacity(size);
    }

    /**
    * Adds a value for a key to the dictionary.
    * This table uses Robin Hood hashing. Which means that every value
    * while inserted gets a distance score 'd'. When moving items, it prioritizes
    * the values with lower distance-scores. Doing so, we hopefully even out the buckets
    * of elements.
    *
    * @param k The key value to use in adding elements. Must not be null and must implement hashCode.
    * @param v The associated value for the key. Must not be null;
    * @return True if new value added, false otherwise.
    * @throws IllegalArgumentException Throws if key or value is null.
    */
    @Override
    public boolean add(K k, V v){
        if (null == k || null == v) {
            throw new IllegalArgumentException();
        }

        if (load() >= loadFactor) {
            rehash();
        }
        int probeTemp = 0;
        int index = hash(k);
        int d = 0;

        Pair<K, V> p = new Pair<K, V>(k, v);
        while (true) {
            if (table[index] == null) {
                table[index] = p;
                size++;
                if (probeTemp > probeCount) {
                    probeCount = probeTemp;
                }
                return true;

                //Same key
            } else if(table[index].getKey().equals(k)){
                table[index].setvalue(v);
                return true;

                // Try another hash if our distance is lower than with the one
                // that is currently inserted into the table[index].
            } else if (displacement(table[index].getKey(), index) >= d) {
                d++;
                index = (index + 1) % table_size;
                maxDP = Math.max(d, maxDP);
            } else {
                // Start moving another element with lower distance-score.
                Pair<K, V> temp = p;
                p = table[index];
                table[index] = temp;
                index = (index + 1) % table_size;
                d = displacement(k, index);
                maxDP = Math.max(d, maxDP);
            }
            collissions++;
            probeTemp++;
        }
    }

    // Returns the number of elements in the table.
    public int size() {
        return size;
    }

    

    /**
     * Grows the size of the array.
     * Also makes sure that the size of the
     * Array is a prime number.
     * @see oy.tol.tra.Algorithms#findPrime(int)
     * 
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        rehashCount++;
        Pair<K, V>[] oldTable = table;
        table = new Pair[Algorithms.findPrime((int)((float)table_size * growRate))];
        if(growRate > 2){
            growRate--;
        } else {
            if(growRate > 1.75)
                growRate -= 0.05;
        }
        table_size = table.length;
        size = 0;
        maxDP = 0;

        for (Pair<K, V> p : oldTable) {
            if (p != null) {
                add(p.getKey(), p.getValue());
            }
        }
    }

    /**
     * Calculates the distance from hash key to the actual location
     * of the item in the array.
     * For example, the character's 'a' hash key is 97. If its location on
     * the array were to be in index 100, this function would then return 3. 
     * @param k key of the item whos distance we are calculating
     * @param loc index where the element actually is.
     * @return returns the distance from the key and the actual index where the element is.
     */
    private int displacement(K k, int loc) {
        int h = hash(k);
        return (loc >= h) ? (loc - h) : (table_size + loc - h);
    }

    private int hash(K k) {
        return (k.hashCode() & 0x7fffffff) % table_size;
    }

    @Override
    public Type getType() {
        return Type.HASHTABLE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void ensureCapacity(int size) throws OutOfMemoryError {
        if(size >= Integer.MAX_VALUE){
            throw new OutOfMemoryError();
        }
        this.table = new Pair[Algorithms.findPrime(size)];
        this.table_size = Algorithms.findPrime(size);
        this.size = 0;
        this.rehashCount = 0;
        this.probeCount = 0;
        this.collissions = 0;
        if(size < 5000){
            growRate = 5;
        }
    }

    @Override
    public V find(K key) throws IllegalArgumentException {
        int loc = hash(key);
        for (int d = 0; d <= maxDP; d++) {
            int index = (loc + d) % table_size;
            if (null != table[index] && key.equals(table[index].getKey())) {
                return table[index].getValue();
            }
        }
        return null;
    }

    private float load() {
        return ((float) size / (float) table_size);
    }

    @Override
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Hash table fill factor is %.2f.%n", loadFactor));
        sb.append(String.format("Hash table had %d collisions when filling the hash table.%n", collissions));
        sb.append(String.format("Hash table had to probe %d times in the worst case.%n", probeCount));
        sb.append(String.format("Hash table had to reallocate %d times.%n", rehashCount));
        sb.append(String.format("Current fill rate is %.2f%%%n", (load() * 100)));
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<K, V>[] toSortedArray() {
        Pair<K, V>[] arr = new Pair[size];
        int i = 0;
        for (Pair<K, V> p : table) {
            if (null != p) {
                arr[i++] = p;
            }
        }
        Algorithms.fastSort(arr, 0, arr.length - 1);
        return arr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void compress() throws OutOfMemoryError {
        //Best collission handeling method in this case would be linear probing.
        Pair<K, V>[] newTable = (Pair<K, V> [] )new Pair[size];
        for(int i = 0; i < table_size; i++){
            if(null != table[i]){
                int loc = (table[i].getKey().hashCode() & 0x7fffffff) % size;
                if(null == newTable[loc]){
                    newTable[loc] = table[i]; 
                } else {
                    int counter = 0; //avoid infinite loop
                    while(null != newTable[loc] && counter++ < size + 1){
                        loc = (loc+1) % size;
                    }
                    newTable[loc] = table[i];
                }
            }
        }
        this.table = newTable;
        this.table_size = size;
        maxDP = table_size; 
    }
}