package redis.clients.jedis.params;

import java.util.ArrayList;
import java.util.Collections;

import redis.clients.jedis.Protocol;
import redis.clients.jedis.util.SafeEncoder;

public class StrAlgoParams extends Params {

    private static final String IDX = "idx";
    private static final String LEN = "len";
    private static final String WITHMATCHLEN = "withmatchlen";
    private static final String MINMATCHLEN = "minmatchlen";
    private static final String STRINGS = "strings";
    private static final String KEYS = "keys";

    public static enum StrAlgo {
        LCS;

        private final byte[] raw;

        StrAlgo() {
            raw = SafeEncoder.encode(this.name());
        }

        public byte[] getRaw() {
            return raw;
        }
    }

    public StrAlgoParams() {
    }

    public static StrAlgoParams StrAlgoParams() {
        return new StrAlgoParams();
    }

    /**
     * When IDX is given the command returns an array with the LCS length
     * and all the ranges in both the strings, start and end offset for
     * each string, where there are matches.
     * @return StrAlgoParams
     */
    public StrAlgoParams idx() {
        addParam(IDX);
        return this;
    }

    /**
     * When LEN is given the command returns the length of the longest common substring.
     * @return StrAlgoParams
     */
    public StrAlgoParams len() {
        addParam(LEN);
        return this;
    }

    /**
     * When WITHMATCHLEN is given each array representing a match will also have the length of the match.
     * @return StrAlgoParams
     */
    public StrAlgoParams withMatchLen() {
        addParam(WITHMATCHLEN);
        return this;
    }

    /**
     * Specify the minimum match length.
     * @return StrAlgoParams
     */
    public StrAlgoParams minMatchLen(long minMatchLen) {
        addParam(MINMATCHLEN, minMatchLen);
        return this;
    }

    /**
     * Specify the strings to be passed.
     * @return StrAlgoParams
     */
    public StrAlgoParams strings(String... strings) {
        addParam(STRINGS, strings);
        return this;
    }

    /**
     * Specify the keys to be passed.
     * @return StrAlgoParams
     */
    public StrAlgoParams keys(String... keys) {
        addParam(KEYS, keys);
        return this;
    }

    public byte[][] getByteKeys() {
        if (contains(KEYS)) {
            return SafeEncoder.encodeMany(getParam(KEYS));
        }
        return null;
    }

    public byte[][] getByteParams(byte[]... args) {
        ArrayList<byte[]> byteParams = new ArrayList<>();
        Collections.addAll(byteParams, args);

        if (contains(IDX)) {
            byteParams.add(SafeEncoder.encode(IDX));
        }
        if (contains(LEN)) {
            byteParams.add(SafeEncoder.encode(LEN));
        }
        if (contains(WITHMATCHLEN)) {
            byteParams.add(SafeEncoder.encode(WITHMATCHLEN));
        }

        if (contains(MINMATCHLEN)) {
            byteParams.add(SafeEncoder.encode(MINMATCHLEN));
            byteParams.add(Protocol.toByteArray((long) getParam(MINMATCHLEN)));
        }
        if (contains(STRINGS)) {
            byteParams.add(SafeEncoder.encode(STRINGS));
            for (String str : (String[]) getParam(STRINGS)) {
                byteParams.add(SafeEncoder.encode(str));
            }
        }
        if (contains(KEYS)) {
            byteParams.add(SafeEncoder.encode(KEYS));
            for (String str : (String[]) getParam(KEYS)) {
                byteParams.add(SafeEncoder.encode(str));
            }
        }

        return byteParams.toArray(new byte[byteParams.size()][]);
    }
}
