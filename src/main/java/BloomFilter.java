import com.google.common.hash.Hashing;
import com.google.common.hash.HashFunction;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class BloomFilter {
    private final BitSet bitSet;
    private final int bitSetSize;
    private final int hashCount;
    private final HashFunction hashFunction;

    public BloomFilter(int bitSetSize, int hashCount) {
        this.bitSetSize = bitSetSize;
        this.hashCount = hashCount;
        this.bitSet = new BitSet(bitSetSize);
        this.hashFunction = Hashing.murmur3_128();
    }

    public void add(String item) {
        byte[] bytes = item.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < hashCount; i++) {
            int hash = hashFunction.hashBytes(bytes).asInt() + i;
            int index = Math.abs(hash % bitSetSize);
            bitSet.set(index);
        }
    }

    public boolean mightContain(String item) {
        byte[] bytes = item.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < hashCount; i++) {
            int hash = hashFunction.hashBytes(bytes).asInt() + i;
            int index = Math.abs(hash % bitSetSize);
            if (!bitSet.get(index)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        BloomFilter bloomFilter = new BloomFilter(500000, 7);

        // 插入元素
        bloomFilter.add("hello");
        bloomFilter.add("world");

        // 查询元素
        System.out.println(bloomFilter.mightContain("hello"));  // 输出: true
        System.out.println(bloomFilter.mightContain("world"));  // 输出: true
        System.out.println(bloomFilter.mightContain("java"));   // 输出: false
    }
}
