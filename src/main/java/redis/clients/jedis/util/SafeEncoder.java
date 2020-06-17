package redis.clients.jedis.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

/**
 * The only reason to have this is to be able to compatible with java 1.5 :(
 */
public final class SafeEncoder {
  private SafeEncoder(){
    throw new InstantiationError( "Must not instantiate this class" );
  }

  public static byte[][] encodeMany(final String... strs) {
    byte[][] many = new byte[strs.length][];
    for (int i = 0; i < strs.length; i++) {
      many[i] = encode(strs[i]);
    }
    return many;
  }

  public static byte[] encode(final String str) {
    try {
      if (str == null) {
        throw new JedisDataException("value sent to redis cannot be null");
      }
      return str.getBytes(Protocol.CHARSET);
    } catch (UnsupportedEncodingException e) {
      throw new JedisException(e);
    }
  }

  public static String encode(final byte[] data) {
    try {
      return new String(data, Protocol.CHARSET);
    } catch (UnsupportedEncodingException e) {
      throw new JedisException(e);
    }
  }

  /**
   * This method takes an object and will convert all bytes[] and list of byte[]
   * and will encode the object in a recursive way.
   * @param dataToEncode
   * @return the object fully encoded
   */
  public static Object encodeObject(Object dataToEncode) {
    Object returnValue = dataToEncode;
    if (dataToEncode instanceof byte[]) {
      returnValue = SafeEncoder.encode((byte[]) dataToEncode);
    } else if (dataToEncode instanceof ArrayList) {
      ArrayList arrayToDecode = (ArrayList)dataToEncode;
      ArrayList returnValueArray = new ArrayList(arrayToDecode.size());
      for (Object arrayEntry : arrayToDecode) {
        returnValueArray.add(encodeObject(arrayEntry));
      }
      returnValue = returnValueArray;
    }
    return returnValue;
  }
}
