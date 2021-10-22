import java.util.ArrayList;

public class SymbolTable {
    private ArrayList<String> hashTable;
    private int hashTableCapacity;
    private int primeNumber;

    public SymbolTable() {
        this.hashTableCapacity = 100;
        this.primeNumber = 7;
        this.hashTable = new ArrayList<>();

        for (int i = 0; i < hashTableCapacity; i++) {
            hashTable.add(i, "");
        }
    }

    private int hash1(String value) {
        //sum of ascii codes of chars of value
        int sum = 0;
        for (int i = 0; i < value.length(); i++) {
            int asciiValue = value.charAt(i);
            sum = sum + asciiValue;
        }
        return sum;
    }

    private int hash2(String value){
        return hash1(value) % this.hashTableCapacity;
    }

    private int hash_function(String value, Integer i) {
        return (hash1(value) % primeNumber + i * hash2(value)) % this.hashTableCapacity;
    }

    public Integer put(String value){
        int i=0;
        int position = hash_function(value, i);

        while(i < this.hashTableCapacity){
            if(this.hashTable.get(position).equals(""))
                break;
            i++;
            position = hash_function(value, i);
        }
        this.hashTable.set(position, value);
        return position;
    }

    public Integer search(String value) {
        //todo implement search() for hashtable
        return null;
    }
}
