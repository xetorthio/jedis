package redis.clients.jedis.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.StreamConsumersInfo;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.StreamGroupInfo;
import redis.clients.jedis.StreamInfo;
import redis.clients.jedis.StreamPendingEntry;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamPendingSummary;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.GeoAddParams;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.GetExParams;
import redis.clients.jedis.params.RestoreParams;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.XAddParams;
import redis.clients.jedis.params.XAutoClaimParams;
import redis.clients.jedis.params.XClaimParams;
import redis.clients.jedis.params.XPendingParams;
import redis.clients.jedis.params.XTrimParams;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZIncrByParams;
import redis.clients.jedis.params.LPosParams;
import redis.clients.jedis.resps.KeyedListElement;
import redis.clients.jedis.args.RangeEndpoint;

/**
 * Common interface for sharded and non-sharded Jedis
 */
public interface JedisCommands {
  String set(String key, String value);

  String set(String key, String value, SetParams params);

  String get(String key);

  String getDel(String key);

  String getEx(String key, GetExParams params);

  Boolean exists(String key);

  Long persist(String key);

  String type(String key);

  byte[] dump(String key);

  /**
   * @deprecated Use {@link #restore(java.lang.String, long, byte[])}.
   */
  @Deprecated
  default String restore(String key, int ttl, byte[] serializedValue) {
    return restore(key, (long) ttl, serializedValue);
  }

  String restore(String key, long ttl, byte[] serializedValue);

  /**
   * @deprecated Use {@link #restore(java.lang.String, long, byte[], redis.clients.jedis.params.RestoreParams)}.
   */
  @Deprecated
  default String restoreReplace(String key, int ttl, byte[] serializedValue) {
    return restoreReplace(key, (long) ttl, serializedValue);
  }

  /**
   * @deprecated Use {@link #restore(java.lang.String, long, byte[], redis.clients.jedis.params.RestoreParams)}.
   */
  @Deprecated
  String restoreReplace(String key, long ttl, byte[] serializedValue);

  String restore(String key, long ttl, byte[] serializedValue, RestoreParams params);

  /**
   * @deprecated Use {@link #expire(java.lang.String, long)}.
   */
  @Deprecated
  default Long expire(String key, int seconds) {
    return expire(key, (long) seconds);
  }

  Long expire(String key, long seconds);

  Long pexpire(String key, long milliseconds);

  Long expireAt(String key, long unixTime);

  Long pexpireAt(String key, long millisecondsTimestamp);

  Long ttl(String key);

  Long pttl(String key);

  Long touch(String key);

  Boolean setbit(String key, long offset, boolean value);

  /**
   * @deprecated Use {@link #setbit(java.lang.String, long, boolean)}.
   */
  @Deprecated
  Boolean setbit(String key, long offset, String value);

  Boolean getbit(String key, long offset);

  Long setrange(String key, long offset, String value);

  String getrange(String key, long startOffset, long endOffset);

  String getSet(String key, String value);

  Long setnx(String key, String value);

  /**
   * @deprecated Use {@link #setex(java.lang.String, long, java.lang.String)}.
   */
  @Deprecated
  default String setex(String key, int seconds, String value) {
    return setex(key, (long) seconds, value);
  }

  String setex(String key, long seconds, String value);

  String psetex(String key, long milliseconds, String value);

  Long decrBy(String key, long decrement);

  Long decr(String key);

  Long incrBy(String key, long increment);

  Double incrByFloat(String key, double increment);

  Long incr(String key);

  Long append(String key, String value);

  String substr(String key, int start, int end);

  Long hset(String key, String field, String value);

  Long hset(String key, Map<String, String> hash);

  String hget(String key, String field);

  Long hsetnx(String key, String field, String value);

  String hmset(String key, Map<String, String> hash);

  List<String> hmget(String key, String... fields);

  Long hincrBy(String key, String field, long value);

  Double hincrByFloat(String key, String field, double value);

  Boolean hexists(String key, String field);

  Long hdel(String key, String... field);

  Long hlen(String key);

  Set<String> hkeys(String key);

  List<String> hvals(String key);

  Map<String, String> hgetAll(String key);

  String hrandfield(String key);

  List<String> hrandfield(String key, long count);

  Map<String, String> hrandfieldWithValues(String key, long count);

  Long rpush(String key, String... string);

  Long lpush(String key, String... string);

  Long llen(String key);

  List<String> lrange(String key, long start, long stop);

  String ltrim(String key, long start, long stop);

  String lindex(String key, long index);

  String lset(String key, long index, String value);

  Long lrem(String key, long count, String value);

  String lpop(String key);

  List<String> lpop(String key, int count);

  Long lpos(String key, String element);

  Long lpos(String key, String element, LPosParams params);

  List<Long> lpos(String key, String element, LPosParams params, long count);

  String rpop(String key);

  List<String> rpop(String key, int count);

  Long sadd(String key, String... member);

  Set<String> smembers(String key);

  Long srem(String key, String... member);

  String spop(String key);

  Set<String> spop(String key, long count);

  Long scard(String key);

  Boolean sismember(String key, String member);

  List<Boolean> smismember(String key, String... members);

  String srandmember(String key);

  List<String> srandmember(String key, int count);

  Long strlen(String key);

  Long zadd(String key, double score, String member);

  Long zadd(String key, double score, String member, ZAddParams params);

  Long zadd(String key, Map<String, Double> scoreMembers);

  Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params);

  Double zaddIncr(String key, double score, String member, ZAddParams params);

  Set<String> zrange(String key, long start, long stop);

  Long zrem(String key, String... members);

  Double zincrby(String key, double increment, String member);

  Double zincrby(String key, double increment, String member, ZIncrByParams params);

  Long zrank(String key, String member);

  Long zrevrank(String key, String member);

  Set<String> zrevrange(String key, long start, long stop);

  Set<Tuple> zrangeWithScores(String key, long start, long stop);

  Set<Tuple> zrevrangeWithScores(String key, long start, long stop);

  String zrandmember(String key);

  Set<String> zrandmember(String key, long count);

  Set<Tuple> zrandmemberWithScores(String key, long count);

  Long zcard(String key);

  Double zscore(String key, String member);

  List<Double> zmscore(String key, String... members);

  Tuple zpopmax(String key);

  Set<Tuple> zpopmax(String key, int count);

  Tuple zpopmin(String key);

  Set<Tuple> zpopmin(String key, int count);

  List<String> sort(String key);

  List<String> sort(String key, SortingParams sortingParameters);

  Long zcount(String key, double min, double max);

  Long zcount(String key, String min, String max);

  Long zcount(String key, RangeEndpoint<Double> min, RangeEndpoint<Double> max);

  Set<String> zrangeByScore(String key, double min, double max);

  Set<String> zrangeByScore(String key, String min, String max);

  Set<String> zrevrangeByScore(String key, double max, double min);

  Set<String> zrangeByScore(String key, double min, double max, int offset, int count);

  Set<String> zrevrangeByScore(String key, String max, String min);

  Set<String> zrangeByScore(String key, String min, String max, int offset, int count);

  Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count);

  Set<Tuple> zrangeByScoreWithScores(String key, double min, double max);

  Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min);

  Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count);

  Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count);

  Set<Tuple> zrangeByScoreWithScores(String key, String min, String max);

  Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min);

  Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count);

  Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count);

  Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count);

  Long zremrangeByRank(String key, long start, long stop);

  Long zremrangeByScore(String key, double min, double max);

  Long zremrangeByScore(String key, String min, String max);

  Long zlexcount(String key, String min, String max);

  Set<String> zrangeByLex(String key, String min, String max);

  Set<String> zrangeByLex(String key, String min, String max, int offset, int count);

  Set<String> zrevrangeByLex(String key, String max, String min);

  Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count);

  Long zremrangeByLex(String key, String min, String max);

  Long linsert(String key, ListPosition where, String pivot, String value);

  Long lpushx(String key, String... string);

  Long rpushx(String key, String... string);

  List<String> blpop(int timeout, String key);

  KeyedListElement blpop(double timeout, String key);

  List<String> brpop(int timeout, String key);

  KeyedListElement brpop(double timeout, String key);

  Long del(String key);

  Long unlink(String key);

  String echo(String string);

  /**
   * @deprecated This method will be removed from this interface. Use
   * {@link AdvancedJedisCommands#move(java.lang.String, int)}.
   */
  @Deprecated
  Long move(String key, int dbIndex);

  Long bitcount(String key);

  Long bitcount(String key, long start, long end);

  Long bitpos(String key, boolean value);

  Long bitpos(String key, boolean value, BitPosParams params);

  default ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
    return hscan(key, cursor, new ScanParams());
  }

  ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params);

  default ScanResult<String> sscan(String key, String cursor) {
    return sscan(key, cursor, new ScanParams());
  }

  ScanResult<String> sscan(String key, String cursor, ScanParams params);

  default ScanResult<Tuple> zscan(String key, String cursor) {
    return zscan(key, cursor, new ScanParams());
  }

  ScanResult<Tuple> zscan(String key, String cursor, ScanParams params);

  Long pfadd(String key, String... elements);

  long pfcount(String key);

  // Geo Commands

  Long geoadd(String key, double longitude, double latitude, String member);

  Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap);

  Long geoadd(String key, GeoAddParams params, Map<String, GeoCoordinate> memberCoordinateMap);

  Double geodist(String key, String member1, String member2);

  Double geodist(String key, String member1, String member2, GeoUnit unit);

  List<String> geohash(String key, String... members);

  List<GeoCoordinate> geopos(String key, String... members);

  List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
      GeoUnit unit);

  List<GeoRadiusResponse> georadiusReadonly(String key, double longitude, double latitude,
      double radius, GeoUnit unit);

  List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
      GeoUnit unit, GeoRadiusParam param);

  List<GeoRadiusResponse> georadiusReadonly(String key, double longitude, double latitude,
      double radius, GeoUnit unit, GeoRadiusParam param);

  List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit);

  List<GeoRadiusResponse> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit);

  List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit,
      GeoRadiusParam param);

  List<GeoRadiusResponse> georadiusByMemberReadonly(String key, String member, double radius,
      GeoUnit unit, GeoRadiusParam param);

  /**
   * Executes BITFIELD Redis command
   * @param key
   * @param arguments
   * @return
   */
  List<Long> bitfield(String key, String...arguments);

  List<Long> bitfieldReadonly(String key, String...arguments);

  /**
   * Used for HSTRLEN Redis command
   * @param key
   * @param field
   * @return length of the value for key
   */
  Long hstrlen(String key, String field);

  /**
   * XADD key ID field string [field string ...]
   *
   * @param key
   * @param id
   * @param hash
   * @return the ID of the added entry
   */
  StreamEntryID xadd(String key, StreamEntryID id, Map<String, String> hash);

  /**
   * XADD key MAXLEN ~ LEN ID field string [field string ...]
   *
   * @param key
   * @param id
   * @param hash
   * @param maxLen
   * @param approximateLength
   * @return
   */
  StreamEntryID xadd(String key, StreamEntryID id, Map<String, String> hash, long maxLen, boolean approximateLength);

  /**
   * XADD key [NOMKSTREAM] [MAXLEN|MINID [=|~] threshold [LIMIT count]] *|ID field value [field value ...]
   *
   * @param key
   * @param hash
   * @param params
   * @return
   */
  StreamEntryID xadd(String key, Map<String, String> hash, XAddParams params);
  redis.clients.jedis.args.StreamEntryID xaddV2(String key, Map<String, String> hash, XAddParams params);

  /**
   * XLEN key
   *
   * @param key
   * @return
   */
  Long xlen(String key);

  /**
   * XRANGE key start end
   *
   * @param key
   * @param start minimum {@link StreamEntryID} for the retrieved range, passing <code>null</code> will indicate minimum ID possible in the stream
   * @param end maximum {@link StreamEntryID} for the retrieved range, passing <code>null</code> will indicate maximum ID possible in the stream
   * @return The entries with IDs matching the specified range.
   */
  List<StreamEntry> xrange(String key, StreamEntryID start, StreamEntryID end);

  /**
   * XRANGE key start end COUNT count
   *
   * @param key
   * @param start minimum {@link StreamEntryID} for the retrieved range, passing <code>null</code> will indicate minimum ID possible in the stream
   * @param end maximum {@link StreamEntryID} for the retrieved range, passing <code>null</code> will indicate maximum ID possible in the stream
   * @param count maximum number of entries returned
   * @return The entries with IDs matching the specified range.
   */
  List<StreamEntry> xrange(String key, StreamEntryID start, StreamEntryID end, int count);

  /**
   * XREVRANGE key end start
   *
   * @param key
   * @param start minimum {@link StreamEntryID} for the retrieved range, passing <code>null</code> will indicate minimum ID possible in the stream
   * @param end maximum {@link StreamEntryID} for the retrieved range, passing <code>null</code> will indicate maximum ID possible in the stream
   * @return the entries with IDs matching the specified range, from the higher ID to the lower ID matching.
   */
  List<StreamEntry> xrevrange(String key, StreamEntryID end, StreamEntryID start);

  /**
   * XREVRANGE key end start COUNT count
   *
   * @param key
   * @param start minimum {@link StreamEntryID} for the retrieved range, passing <code>null</code> will indicate minimum ID possible in the stream
   * @param end maximum {@link StreamEntryID} for the retrieved range, passing <code>null</code> will indicate maximum ID possible in the stream
   * @param count The entries with IDs matching the specified range.
   * @return the entries with IDs matching the specified range, from the higher ID to the lower ID matching.
   */
  List<StreamEntry> xrevrange(String key, StreamEntryID end, StreamEntryID start, int count);

  List<StreamEntry> xrange(String key, RangeEndpoint<redis.clients.jedis.args.StreamEntryID> min,
      RangeEndpoint<redis.clients.jedis.args.StreamEntryID> max, Integer count, boolean rev);

  /**
   * XACK key group ID [ID ...]
   *
   * @param key
   * @param group
   * @param ids
   * @return
   */
  Long xack(String key, String group, StreamEntryID... ids);

  /**
   * XGROUP CREATE <key> <groupname> <id or $>
   *
   * @param key
   * @param groupname
   * @param id
   * @param makeStream
   * @return
   */
  String xgroupCreate( String key, String groupname, StreamEntryID id, boolean makeStream);

  /**
   * XGROUP SETID <key> <groupname> <id or $>
   *
   * @param key
   * @param groupname
   * @param id
   * @return
   */
  String xgroupSetID( String key, String groupname, StreamEntryID id);

  /**
   * XGROUP DESTROY <key> <groupname>
   *
   * @param key
   * @param groupname
   * @return
   */
  Long xgroupDestroy(String key, String groupname);

  /**
   * XGROUP DELCONSUMER <key> <groupname> <consumername>
   * @param key
   * @param groupname
   * @param consumername
   * @return
   */
  Long xgroupDelConsumer( String key, String groupname, String consumername);

  /**
   * XPENDING key group
   *
   * @param key
   * @param groupname
   * @return
   */
  StreamPendingSummary xpending(String key, String groupname);

  /**
   * XPENDING key group [start end count] [consumer]
   *
   * @param key
   * @param groupname
   * @param start
   * @param end
   * @param count
   * @param consumername
   * @return
   */
  List<StreamPendingEntry> xpending(String key, String groupname, StreamEntryID start,
      StreamEntryID end, int count, String consumername);

  /**
   * XPENDING key group [[IDLE min-idle-time] start end count [consumer]]
   *
   * @param key
   * @param groupname
   * @param params
   */
  List<StreamPendingEntry> xpending(String key, String groupname, XPendingParams params);

  /**
   * XDEL key ID [ID ...]
   * @param key
   * @param ids
   * @return
   */
  Long xdel(String key, StreamEntryID... ids);

  /**
   * XTRIM key MAXLEN [~] count
   * @param key
   * @param maxLen
   * @param approximate
   * @return
   */
  Long xtrim(String key, long maxLen, boolean approximate);

  /**
   * XTRIM key MAXLEN|MINID [=|~] threshold [LIMIT count]
   * @param key
   * @param params
   * @return
   */
  Long xtrim(String key, XTrimParams params);

  /**
   *  XCLAIM <key> <group> <consumer> <min-idle-time> <ID-1> <ID-2>
   *        [IDLE <milliseconds>] [TIME <mstime>] [RETRYCOUNT <count>]
   *        [FORCE] [JUSTID]
   */
  List<StreamEntry> xclaim( String key, String group, String consumername, long minIdleTime,
      long newIdleTime, int retries, boolean force, StreamEntryID... ids);

  /**
   *  XCLAIM <key> <group> <consumer> <min-idle-time> <ID-1> ... <ID-N>
   *        [IDLE <milliseconds>] [TIME <mstime>] [RETRYCOUNT <count>]
   *        [FORCE]
   */
  List<StreamEntry> xclaim(String key, String group, String consumername, long minIdleTime,
      XClaimParams params, StreamEntryID... ids);

  /**
   *  XCLAIM <key> <group> <consumer> <min-idle-time> <ID-1> ... <ID-N>
   *        [IDLE <milliseconds>] [TIME <mstime>] [RETRYCOUNT <count>]
   *        [FORCE] JUSTID
   */
  List<StreamEntryID> xclaimJustId(String key, String group, String consumername, long minIdleTime,
      XClaimParams params, StreamEntryID... ids);

  /**
   * XAUTOCLAIM key group consumer min-idle-time start [COUNT count]
   *
   * @param key Stream Key
   * @param group Consumer Group
   * @param consumerName Consumer name to transfer the auto claimed entries
   * @param minIdleTime Entries pending more than minIdleTime will be transferred ownership
   * @param start {@link StreamEntryID} - Entries >= start will be transferred ownership, passing <code>null</code> will indicate '-'
   * @param params {@link XAutoClaimParams}
   */
  Map.Entry<StreamEntryID, List<StreamEntry>> xautoclaim(String key, String group, String consumerName,
      long minIdleTime, StreamEntryID start, XAutoClaimParams params);

  /**
   * XAUTOCLAIM key group consumer min-idle-time start [COUNT count] JUSTID
   *
   * @param key Stream Key
   * @param group Consumer Group
   * @param consumerName Consumer name to transfer the auto claimed entries
   * @param minIdleTime Entries pending more than minIdleTime will be transferred ownership
   * @param start {@link StreamEntryID} - Entries >= start will be transferred ownership, passing <code>null</code> will indicate '-'
   * @param params {@link XAutoClaimParams}
   */
  Map.Entry<StreamEntryID, List<StreamEntryID>> xautoclaimJustId(String key, String group, String consumerName,
      long minIdleTime, StreamEntryID start, XAutoClaimParams params);

  /**
   * Introspection command used in order to retrieve different information about the stream
   * @param key Stream name
   * @return {@link StreamInfo} that contains information about the stream
   */
  StreamInfo xinfoStream (String key);

  /**
   * Introspection command used in order to retrieve different information about groups in the stream
   * @param key Stream name
   * @return List of {@link StreamGroupInfo} containing information about groups
   */
  List<StreamGroupInfo> xinfoGroup (String key);

  /**
   * Introspection command used in order to retrieve different information about consumers in the group
   * @param key Stream name
   * @param group Group name
   * @return List of {@link StreamConsumersInfo} containing information about consumers that belong
   * to the the group
   */
  List<StreamConsumersInfo> xinfoConsumers (String key, String group);

  Long memoryUsage(String key);

  Long memoryUsage(String key, int samples);
}
