import java.util.ArrayList;

public class SymbolTable {
    private ArrayList<String> hashTable;
    private int hashTableCapacity;

    public SymbolTable() {
        this.hashTableCapacity = 100;
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

    private int hash_function(String value, Integer i) {
        return (hash1(value) % this.hashTableCapacity + i) % this.hashTableCapacity;
    }

    public Integer put(String value){
        Integer position = search(value);
        if (position == null) {
            int i=0;
            position = hash_function(value, i);

            while(i < this.hashTableCapacity){
                if(this.hashTable.get(position).equals(""))
                    break;
                i++;
                position = hash_function(value, i);
            }
            this.hashTable.set(position, value);
        }
        return position;
    }

    public Integer search(String value) {
        for (String key : hashTable) {
            if (key.equals(value))
                return hashTable.indexOf(key);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder hashTableStr = new StringBuilder();
        for (String value : hashTable) {
            if (!value.equals("")) {
                String newStr = hashTable.indexOf(value) + " -> " + value + "\n";
                hashTableStr.append(newStr);
            }
        }
        return "ST_POS | SYMBOL\n\n" + hashTableStr + '}';
    }
}
