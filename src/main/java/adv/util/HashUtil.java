package adv.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String md5(String input) {
        if (StringUtil.isEmpty(input)) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte hash[] = md.digest();
            return BitUtil.toBase16(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    static long LargestPrime48 = 281474976710597L;


    // https://stackoverflow.com/questions/32912894/how-to-shorten-a-64-bit-hash-value-down-to-a-48-bit-value?noredirect=1&lq=1
    public static long hash32(String input) {
        HashFunction hf = Hashing.murmur3_32();
        HashCode hc = hf.newHasher().putString(input, CharsetUtil.UTF8).hash();
        long hash32 = hc.asInt() & 0xFFFFFFFFL;
        return hash32;
    }


    public static long hash48(String input) {
        HashFunction hf = Hashing.murmur3_128();
        HashCode hc = hf.newHasher().putString(input, CharsetUtil.UTF8).hash();
        byte[] data = hc.asBytes();
        long low = BitUtil.readBELong(data, 0);
        long high = BitUtil.readBELong(data, 8);
        long hash64 = low ^ high;
        // поскольку хеш считаем равномерным, мы тупо берем 6*8=48бит младших
        // альтернатива - mod
        long hash48 = hash64 & 0xFFFFFFFFFFFFL;
        return hash48;
    }

    public static long hash64(String input) {
        if (input == null) {
            return 0;
        }
        HashFunction hf = Hashing.murmur3_128();
        HashCode hc = hf.newHasher().putString(input, CharsetUtil.ASCII).hash();
        long hash32 = hc.asLong();
        return hash32;
    }
}
