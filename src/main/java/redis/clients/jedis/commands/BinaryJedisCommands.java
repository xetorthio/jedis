package redis.clients.jedis.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.*;
import redis.clients.jedis.params.*;

/**
 * Common interface for sharded and non-sharded BinaryJedis
 */
public interface BinaryJedisCommands {
  String set(byte[] key, byte[] value);

  String set(byte[] key, byte[] value, SetParams params);

  byte[] get(byte[] key);

  byte[] getDel(byte[] key);

  byte[] getEx(byte[] key, GetExParams params);

  Boolean exists(byte[] key);

  Long persist(byte[] key);

  String type(byte[] key);

  byte[] dump(byte[] key);

  /**
   * @deprecated Use {@link #restore(byte[], long, byte[])}.
   */
  @Deprecated
  default String restore(byte[] key, int ttl, byte[] serializedValue) {
    return restore(key, (long) ttl, serializedValue);
  }

  String restore(byte[] key, long ttl, byte[] serializedValue);

  /**
   * @deprecated Use {@link #restoreReplace(byte[], long, byte[])}.
   */
  @Deprecated
  default String restoreReplace(byte[] key, int ttl, byte[] serializedValue) {
    return restoreReplace(key, (long) ttl, serializedValue);
  }

  /**
   * @deprecated Use
   *             {@link #restore(byte[], long, byte[], redis.clients.jedis.params.RestoreParams)}.
   */
  @Deprecated
  String restoreReplace(byte[] key, long ttl, byte[] serializedValue);

  String restore(byte[] key, long ttl, byte[] serializedValue, RestoreParams params);

  /**
   * @deprecated Use {@link #expire(byte[], long)}.
   */
  @Deprecated
  default Long expire(byte[] key, int seconds) {
    return expire(key, (long) seconds);
  }

  Long expire(byte[] key, long seconds);

  Long pexpire(byte[] key, long milliseconds);

  Long expireAt(byte[] key, long unixTime);

  Long pexpireAt(byte[] key, long millisecondsTimestamp);

  Long ttl(byte[] key);

  Long pttl(byte[] key);

  Long touch(byte[] key);

  Boolean setbit(byte[] key, long offset, boolean value);

  Boolean setbit(byte[] key, long offset, byte[] value);

  Boolean getbit(byte[] key, long offset);

  Long setrange(byte[] key, long offset, byte[] value);

  byte[] getrange(byte[] key, long startOffset, long endOffset);

  byte[] getSet(byte[] key, byte[] value);

  Long setnx(byte[] key, byte[] value);

  /**
   * @deprecated Use {@link #setex(byte[], long, byte[])}.
   */
  @Deprecated
  default String setex(byte[] key, int seconds, byte[] value) {
    return setex(key, (long) seconds, value);
  }

  String setex(byte[] key, long seconds, byte[] value);

  String psetex(byte[] key, long milliseconds, byte[] value);

  Long decrBy(byte[] key, long decrement);

  Long decr(byte[] key);

  Long incrBy(byte[] key, long increment);

  Double incrByFloat(byte[] key, double increment);

  Long incr(byte[] key);

  Long append(byte[] key, byte[] value);

  byte[] substr(byte[] key, int start, int end);

  Long hset(byte[] key, byte[] field, byte[] value);

  Long hset(byte[] key, Map<byte[], byte[]> hash);

  byte[] hget(byte[] key, byte[] field);

  Long hsetnx(byte[] key, byte[] field, byte[] value);

  String hmset(byte[] key, Map<byte[], byte[]> hash);

  List<byte[]> hmget(byte[] key, byte[]... fields);

  Long hincrBy(byte[] key, byte[] field, long value);

  Double hincrByFloat(byte[] key, byte[] field, double value);

  Boolean hexists(byte[] key, byte[] field);

  Long hdel(byte[] key, byte[]... field);

  Long hlen(byte[] key);

  Set<byte[]> hkeys(byte[] key);

  List<byte[]> hvals(byte[] key);

  Map<byte[], byte[]> hgetAll(byte[] key);

  byte[] hrandfield(byte[] key);

  List<byte[]> hrandfield(byte[] key, long count);

  Map<byte[], byte[]> hrandfieldWithValues(byte[] key, long count);

  Long rpush(byte[] key, byte[]... args);

  Long lpush(byte[] key, byte[]... args);

  Long llen(byte[] key);

  List<byte[]> lrange(byte[] key, long start, long stop);

  String ltrim(byte[] key, long start, long stop);

  byte[] lindex(byte[] key, long index);

  String lset(byte[] key, long index, byte[] value);

  Long lrem(byte[] key, long count, byte[] value);

  byte[] lpop(byte[] key);

  List<byte[]> lpop(byte[] key, int count);

  Long lpos(byte[] key, byte[] element);

  Long lpos(byte[] key, byte[] element, LPosParams params);

  List<Long> lpos(byte[] key, byte[] element, LPosParams params, long count);

  byte[] rpop(byte[] key);

  List<byte[]> rpop(byte[] key, int count);

  Long sadd(byte[] key, byte[]... member);

  Set<byte[]> smembers(byte[] key);

  Long srem(byte[] key, byte[]... member);

  byte[] spop(byte[] key);

  Set<byte[]> spop(byte[] key, long count);

  Long scard(byte[] key);

  Boolean sismember(byte[] key, byte[] member);

  List<Boolean> smismember(byte[] key, byte[]... members);

  byte[] srandmember(byte[] key);

  List<byte[]> srandmember(byte[] key, int count);

  Long strlen(byte[] key);

  Long zadd(byte[] key, double score, byte[] member);

  Long zadd(byte[] key, double score, byte[] member, ZAddParams params);

  Long zadd(byte[] key, Map<byte[], Double> scoreMembers);

  Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params);

  Double zaddIncr(byte[] key, double score, byte[] member, ZAddParams params);

  Set<byte[]> zrange(byte[] key, long start, long stop);

  Long zrem(byte[] key, byte[]... members);

  Double zincrby(byte[] key, double increment, byte[] member);

  Double zincrby(byte[] key, double increment, byte[] member, ZIncrByParams params);

  Long zrank(byte[] key, byte[] member);

  Long zrevrank(byte[] key, byte[] member);

  Set<byte[]> zrevrange(byte[] key, long start, long stop);

  Set<Tuple> zrangeWithScores(byte[] key, long start, long stop);

  Set<Tuple> zrevrangeWithScores(byte[] key, long start, long stop);

  byte[] zrandmember(byte[] key);

  Set<byte[]> zrandmember(byte[] key, long count);

  Set<Tuple> zrandmemberWithScores(byte[] key, long count);

  Long zcard(byte[] key);

  Double zscore(byte[] key, byte[] member);

  List<Double> zmscore(byte[] key, byte[]... members);

  Tuple zpopmax(byte[] key);

  Set<Tuple> zpopmax(byte[] key, int count);

  Tuple zpopmin(byte[] key);

  Set<Tuple> zpopmin(byte[] key, int count);

  List<byte[]> sort(byte[] key);

  List<byte[]> sort(byte[] key, SortingParams sortingParameters);

  Long zcount(byte[] key, double min, double max);

  Long zcount(byte[] key, byte[] min, byte[] max);

  Set<byte[]> zrangeByScore(byte[] key, double min, double max);

  Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max);

  Set<byte[]> zrevrangeByScore(byte[] key, double max, double min);

  Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count);

  Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min);

  Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count);

  Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count);

  Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max);

  Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min);

  Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count);

  Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count);

  Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max);

  Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min);

  Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count);

  Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count);

  Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count);

  Long zremrangeByRank(byte[] key, long start, long stop);

  Long zremrangeByScore(byte[] key, double min, double max);

  Long zremrangeByScore(byte[] key, byte[] min, byte[] max);

  Long zlexcount(byte[] key, byte[] min, byte[] max);

  Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max);

  Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count);

  Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min);

  Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count);

  Long zremrangeByLex(byte[] key, byte[] min, byte[] max);

  Long linsert(byte[] key, ListPosition where, byte[] pivot, byte[] value);

  Long lpushx(byte[] key, byte[]... arg);

  Long rpushx(byte[] key, byte[]... arg);

  Long del(byte[] key);

  Long unlink(byte[] key);

  byte[] echo(byte[] arg);

  Long move(byte[] key, int dbIndex);

  Long bitcount(byte[] key);

  Long bitcount(byte[] key, long start, long end);

  Long pfadd(byte[] key, byte[]... elements);

  long pfcount(byte[] key);

  // Geo Commands

  Long geoadd(byte[] key, double longitude, double latitude, byte[] member);

  Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap);

  Long geoadd(byte[] key, GeoAddParams params, Map<byte[], GeoCoordinate> memberCoordinateMap);

  Double geodist(byte[] key, byte[] member1, byte[] member2);

  Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit);

  List<byte[]> geohash(byte[] key, byte[]... members);

  List<GeoCoordinate> geopos(byte[] key, byte[]... members);

  List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius,
      GeoUnit unit);

  List<GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude, double latitude,
      double radius, GeoUnit unit);

  List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius,
      GeoUnit unit, GeoRadiusParam param);

  List<GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude, double latitude,
      double radius, GeoUnit unit, GeoRadiusParam param);

  List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit);

  List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member, double radius,
      GeoUnit unit);

  List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit,
      GeoRadiusParam param);

  List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member, double radius,
      GeoUnit unit, GeoRadiusParam param);

  ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor);

  ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params);

  ScanResult<byte[]> sscan(byte[] key, byte[] cursor);

  ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params);

  ScanResult<Tuple> zscan(byte[] key, byte[] cursor);

  ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params);

  /**
   * Executes BITFIELD Redis command
   * @param key
   * @param arguments
   * @return
   */
  List<Long> bitfield(byte[] key, byte[]... arguments);

  List<Long> bitfieldReadonly(byte[] key, byte[]... arguments);

  /**
   * Used for HSTRLEN Redis command
   * @param key
   * @param field
   * @return lenth of the value for key
   */
  Long hstrlen(byte[] key, byte[] field);

  byte[] xadd(byte[] key, byte[] id, Map<byte[], byte[]> hash, long maxLen,
      boolean approximateLength);

  byte[] xadd(byte[] key, Map<byte[], byte[]> hash, XAddParams params);

  Long xlen(byte[] key);

  List<byte[]> xrange(byte[] key, byte[] start, byte[] end);

  /**
   * @deprecated Use {@link #xrange(byte[], byte[], byte[], int)}.
   */
  @Deprecated
  default List<byte[]> xrange(byte[] key, byte[] start, byte[] end, long count) {
    return xrange(key, start, end, (int) Math.min(count, (long) Integer.MAX_VALUE));
  }

  List<byte[]> xrange(byte[] key, byte[] start, byte[] end, int count);

  List<byte[]> xrevrange(byte[] key, byte[] end, byte[] start);

  List<byte[]> xrevrange(byte[] key, byte[] end, byte[] start, int count);

  Long xack(byte[] key, byte[] group, byte[]... ids);

  String xgroupCreate(byte[] key, byte[] consumer, byte[] id, boolean makeStream);

  String xgroupSetID(byte[] key, byte[] consumer, byte[] id);

  Long xgroupDestroy(byte[] key, byte[] consumer);

  Long xgroupDelConsumer(byte[] key, byte[] consumer, byte[] consumerName);

  Long xdel(byte[] key, byte[]... ids);

  Long xtrim(byte[] key, long maxLen, boolean approximateLength);

  Long xtrim(byte[] key, XTrimParams params);

  Object xpending(byte[] key, byte[] groupname);

  List<Object> xpending(byte[] key, byte[] groupname, byte[] start, byte[] end, int count,
      byte[] consumername);

  List<Object> xpending(byte[] key, byte[] groupname, XPendingParams params);

  List<byte[]> xclaim(byte[] key, byte[] groupname, byte[] consumername, long minIdleTime,
      long newIdleTime, int retries, boolean force, byte[]... ids);

  List<byte[]> xclaim(byte[] key, byte[] group, byte[] consumername, long minIdleTime,
      XClaimParams params, byte[]... ids);

  List<byte[]> xclaimJustId(byte[] key, byte[] group, byte[] consumername, long minIdleTime,
      XClaimParams params, byte[]... ids);

  /**
   * @deprecated Use {@link #xinfoStreamBinary(byte[])}.
   */
  @Deprecated
  StreamInfo xinfoStream(byte[] key);

  Object xinfoStreamBinary(byte[] key);

  /**
   * @deprecated Use {@link #xinfoGroupBinary(byte[])}.
   */
  @Deprecated
  List<StreamGroupInfo> xinfoGroup(byte[] key);

  List<Object> xinfoGroupBinary(byte[] key);

  /**
   * @deprecated Use {@link #xinfoConsumersBinary(byte[], byte[])}.
   */
  @Deprecated
  List<StreamConsumersInfo> xinfoConsumers(byte[] key, byte[] group);

  List<Object> xinfoConsumersBinary(byte[] key, byte[] group);
}
