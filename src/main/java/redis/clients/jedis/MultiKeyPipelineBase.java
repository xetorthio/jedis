package redis.clients.jedis;

import redis.clients.jedis.args.*;
import redis.clients.jedis.commands.*;
import redis.clients.jedis.params.*;
import redis.clients.jedis.resps.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class MultiKeyPipelineBase extends PipelineBase
    implements MultiKeyBinaryRedisPipeline, MultiKeyCommandsPipeline, ClusterPipeline,
    BinaryScriptingCommandsPipeline, ScriptingCommandsPipeline, BasicRedisPipeline {

  protected Client client = null;

  @Override
  public Response<Boolean> copy(byte[] srcKey, byte[] dstKey, int db, boolean replace) {
    client.copy(srcKey, dstKey, db, replace);
    return getResponse(BuilderFactory.BOOLEAN);
  }

  @Override
  public Response<Boolean> copy(byte[] srcKey, byte[] dstKey, boolean replace) {
    client.copy(srcKey, dstKey, replace);
    return getResponse(BuilderFactory.BOOLEAN);
  }

  @Override
  public Response<Boolean> copy(String srcKey, String dstKey, int db, boolean replace) {
    client.copy(srcKey, dstKey, db, replace);
    return getResponse(BuilderFactory.BOOLEAN);
  }

  @Override
  public Response<Boolean> copy(String srcKey, String dstKey, boolean replace) {
    client.copy(srcKey, dstKey, replace);
    return getResponse(BuilderFactory.BOOLEAN);
  }

  @Override
  public Response<String> lmove(String srcKey, String dstKey, ListDirection from,
      ListDirection to) {
    client.lmove(srcKey, dstKey, from, to);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<byte[]> lmove(byte[] srcKey, byte[] dstKey, ListDirection from,
      ListDirection to) {
    client.lmove(srcKey, dstKey, from, to);
    return getResponse(BuilderFactory.BYTE_ARRAY);
  }

  @Override
  public Response<String> blmove(String srcKey, String dstKey, ListDirection from, ListDirection to,
      double timeout) {
    client.blmove(srcKey, dstKey, from, to, timeout);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<byte[]> blmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to,
      double timeout) {
    client.blmove(srcKey, dstKey, from, to, timeout);
    return getResponse(BuilderFactory.BYTE_ARRAY);
  }

  @Override
  public Response<List<String>> blpop(String... args) {
    client.blpop(args);
    return getResponse(BuilderFactory.STRING_LIST);
  }

  @Override
  public Response<List<byte[]>> blpop(byte[]... args) {
    client.blpop(args);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<List<String>> blpop(int timeout, String... keys) {
    client.blpop(timeout, keys);
    return getResponse(BuilderFactory.STRING_LIST);
  }

  /**
   * @deprecated Use {@link #blpop(double, java.lang.String...)} or
   *             {@link #blpop(int, java.lang.String...)}.
   */
  @Deprecated
  public Response<Map<String, String>> blpopMap(int timeout, String... keys) {
    client.blpop(timeout, keys);
    return getResponse(BuilderFactory.STRING_MAP);
  }

  @Deprecated
  public Response<List<String>> blpop(int timeout, byte[]... keys) {
    client.blpop(timeout, keys);
    return getResponse(BuilderFactory.STRING_LIST);
  }

  @Override
  public Response<KeyedListElement> blpop(double timeout, String... keys) {
    client.blpop(timeout, keys);
    return getResponse(BuilderFactory.KEYED_LIST_ELEMENT);
  }

  @Override
  public Response<List<byte[]>> blpop(double timeout, byte[]... keys) {
    client.blpop(timeout, keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<List<String>> brpop(String... args) {
    client.brpop(args);
    return getResponse(BuilderFactory.STRING_LIST);
  }

  @Override
  public Response<List<byte[]>> brpop(byte[]... args) {
    client.brpop(args);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<List<String>> brpop(int timeout, String... keys) {
    client.brpop(timeout, keys);
    return getResponse(BuilderFactory.STRING_LIST);
  }

  /**
   * @deprecated Use {@link #brpop(double, java.lang.String...)} or
   *             {@link #brpop(int, java.lang.String...)}.
   */
  @Deprecated
  public Response<Map<String, String>> brpopMap(int timeout, String... keys) {
    client.blpop(timeout, keys);
    return getResponse(BuilderFactory.STRING_MAP);
  }

  @Deprecated
  public Response<List<String>> brpop(int timeout, byte[]... keys) {
    client.brpop(timeout, keys);
    return getResponse(BuilderFactory.STRING_LIST);
  }

  @Override
  public Response<KeyedListElement> brpop(double timeout, String... keys) {
    client.brpop(timeout, keys);
    return getResponse(BuilderFactory.KEYED_LIST_ELEMENT);
  }

  @Override
  public Response<List<byte[]>> brpop(double timeout, byte[]... keys) {
    client.brpop(timeout, keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<KeyedZSetElement> bzpopmax(double timeout, String... keys) {
    client.bzpopmax(timeout, keys);
    return getResponse(BuilderFactory.KEYED_ZSET_ELEMENT);
  }

  @Override
  public Response<List<byte[]>> bzpopmax(double timeout, byte[]... keys) {
    client.bzpopmax(timeout, keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<KeyedZSetElement> bzpopmin(double timeout, String... keys) {
    client.bzpopmin(timeout, keys);
    return getResponse(BuilderFactory.KEYED_ZSET_ELEMENT);
  }

  @Override
  public Response<List<byte[]>> bzpopmin(double timeout, byte[]... keys) {
    client.bzpopmin(timeout, keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<Long> del(String... keys) {
    client.del(keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> del(byte[]... keys) {
    client.del(keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> unlink(String... keys) {
    client.unlink(keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> unlink(byte[]... keys) {
    client.unlink(keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> exists(String... keys) {
    client.exists(keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> exists(byte[]... keys) {
    client.exists(keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Set<String>> keys(String pattern) {
    getClient(pattern).keys(pattern);
    return getResponse(BuilderFactory.STRING_SET);
  }

  @Override
  public Response<Set<byte[]>> keys(byte[] pattern) {
    getClient(pattern).keys(pattern);
    return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public Response<List<String>> mget(String... keys) {
    client.mget(keys);
    return getResponse(BuilderFactory.STRING_LIST);
  }

  @Override
  public Response<List<byte[]>> mget(byte[]... keys) {
    client.mget(keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<String> mset(String... keysvalues) {
    client.mset(keysvalues);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> mset(byte[]... keysvalues) {
    client.mset(keysvalues);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<Long> msetnx(String... keysvalues) {
    client.msetnx(keysvalues);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> msetnx(byte[]... keysvalues) {
    client.msetnx(keysvalues);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<String> rename(String oldkey, String newkey) {
    client.rename(oldkey, newkey);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> rename(byte[] oldkey, byte[] newkey) {
    client.rename(oldkey, newkey);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<Long> renamenx(String oldkey, String newkey) {
    client.renamenx(oldkey, newkey);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> renamenx(byte[] oldkey, byte[] newkey) {
    client.renamenx(oldkey, newkey);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<String> rpoplpush(String srckey, String dstkey) {
    client.rpoplpush(srckey, dstkey);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<byte[]> rpoplpush(byte[] srckey, byte[] dstkey) {
    client.rpoplpush(srckey, dstkey);
    return getResponse(BuilderFactory.BYTE_ARRAY);
  }

  @Override
  public Response<Set<String>> sdiff(String... keys) {
    client.sdiff(keys);
    return getResponse(BuilderFactory.STRING_SET);
  }

  @Override
  public Response<Set<byte[]>> sdiff(byte[]... keys) {
    client.sdiff(keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public Response<Long> sdiffstore(String dstkey, String... keys) {
    client.sdiffstore(dstkey, keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> sdiffstore(byte[] dstkey, byte[]... keys) {
    client.sdiffstore(dstkey, keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Set<String>> sinter(String... keys) {
    client.sinter(keys);
    return getResponse(BuilderFactory.STRING_SET);
  }

  @Override
  public Response<Set<byte[]>> sinter(byte[]... keys) {
    client.sinter(keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public Response<Long> sinterstore(String dstkey, String... keys) {
    client.sinterstore(dstkey, keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> sinterstore(byte[] dstkey, byte[]... keys) {
    client.sinterstore(dstkey, keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> smove(String srckey, String dstkey, String member) {
    client.smove(srckey, dstkey, member);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> smove(byte[] srckey, byte[] dstkey, byte[] member) {
    client.smove(srckey, dstkey, member);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> sort(String key, SortingParams sortingParameters, String dstkey) {
    client.sort(key, sortingParameters, dstkey);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
    client.sort(key, sortingParameters, dstkey);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> sort(String key, String dstkey) {
    client.sort(key, dstkey);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> sort(byte[] key, byte[] dstkey) {
    client.sort(key, dstkey);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Set<String>> sunion(String... keys) {
    client.sunion(keys);
    return getResponse(BuilderFactory.STRING_SET);
  }

  @Override
  public Response<Set<byte[]>> sunion(byte[]... keys) {
    client.sunion(keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public Response<Long> sunionstore(String dstkey, String... keys) {
    client.sunionstore(dstkey, keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> sunionstore(byte[] dstkey, byte[]... keys) {
    client.sunionstore(dstkey, keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<String> watch(String... keys) {
    client.watch(keys);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> watch(byte[]... keys) {
    client.watch(keys);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> unwatch() {
    client.unwatch();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<Set<byte[]>> zdiff(byte[]... keys) {
    client.zdiff(keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public Response<Set<Tuple>> zdiffWithScores(byte[]... keys) {
    client.zdiffWithScores(keys);
    return getResponse(BuilderFactory.TUPLE_ZSET);
  }

  @Override
  public Response<Set<String>> zdiff(String... keys) {
    client.zdiff(keys);
    return getResponse(BuilderFactory.STRING_ZSET);
  }

  @Override
  public Response<Set<Tuple>> zdiffWithScores(String... keys) {
    client.zdiffWithScores(keys);
    return getResponse(BuilderFactory.TUPLE_ZSET);
  }

  @Override
  public Response<Long> zdiffStore(final byte[] dstkey, final byte[]... keys) {
    client.zdiffStore(dstkey, keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> zdiffStore(final String dstkey, final String... keys) {
    client.zdiffStore(dstkey, keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Set<byte[]>> zinter(final ZParams params, final byte[]... keys) {
    client.zinter(params, keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public Response<Set<Tuple>> zinterWithScores(final ZParams params, final byte[]... keys) {
    client.zinterWithScores(params, keys);
    return getResponse(BuilderFactory.TUPLE_ZSET);
  }

  @Override
  public Response<Set<String>> zinter(final ZParams params, final String... keys) {
    client.zinter(params, keys);
    return getResponse(BuilderFactory.STRING_ZSET);
  }

  @Override
  public Response<Set<Tuple>> zinterWithScores(final ZParams params, final String... keys) {
    client.zinterWithScores(params, keys);
    return getResponse(BuilderFactory.TUPLE_ZSET);
  }

  @Override
  public Response<Long> zinterstore(String dstkey, String... sets) {
    client.zinterstore(dstkey, sets);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> zinterstore(byte[] dstkey, byte[]... sets) {
    client.zinterstore(dstkey, sets);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> zinterstore(String dstkey, ZParams params, String... sets) {
    client.zinterstore(dstkey, params, sets);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
    client.zinterstore(dstkey, params, sets);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Set<byte[]>> zunion(ZParams params, byte[]... keys) {
    client.zunion(params, keys);
    return getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public Response<Set<Tuple>> zunionWithScores(ZParams params, byte[]... keys) {
    client.zunionWithScores(params, keys);
    return getResponse(BuilderFactory.TUPLE_ZSET);
  }

  @Override
  public Response<Set<String>> zunion(ZParams params, String... keys) {
    client.zunion(params, keys);
    return getResponse(BuilderFactory.STRING_ZSET);
  }

  @Override
  public Response<Set<Tuple>> zunionWithScores(ZParams params, String... keys) {
    client.zunionWithScores(params, keys);
    return getResponse(BuilderFactory.TUPLE_ZSET);
  }

  @Override
  public Response<Long> zunionstore(String dstkey, String... sets) {
    client.zunionstore(dstkey, sets);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> zunionstore(byte[] dstkey, byte[]... sets) {
    client.zunionstore(dstkey, sets);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> zunionstore(String dstkey, ZParams params, String... sets) {
    client.zunionstore(dstkey, params, sets);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
    client.zunionstore(dstkey, params, sets);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<String> bgrewriteaof() {
    client.bgrewriteaof();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> bgsave() {
    client.bgsave();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<List<String>> configGet(String pattern) {
    client.configGet(pattern);
    return getResponse(BuilderFactory.STRING_LIST);
  }

  @Override
  public Response<String> configSet(String parameter, String value) {
    client.configSet(parameter, value);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> brpoplpush(String source, String destination, int timeout) {
    client.brpoplpush(source, destination, timeout);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<byte[]> brpoplpush(byte[] source, byte[] destination, int timeout) {
    client.brpoplpush(source, destination, timeout);
    return getResponse(BuilderFactory.BYTE_ARRAY);
  }

  @Override
  public Response<String> configResetStat() {
    client.configResetStat();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> save() {
    client.save();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<Long> lastsave() {
    client.lastsave();
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> publish(String channel, String message) {
    client.publish(channel, message);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> publish(byte[] channel, byte[] message) {
    client.publish(channel, message);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<String> randomKey() {
    client.randomKey();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<byte[]> randomKeyBinary() {
    client.randomKey();
    return getResponse(BuilderFactory.BYTE_ARRAY);
  }

  @Override
  public Response<String> flushDB() {
    client.flushDB();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> flushAll() {
    client.flushAll();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> flushDB(FlushMode flushMode) {
    client.flushDB(flushMode);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> flushAll(FlushMode flushMode) {
    client.flushAll(flushMode);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> info() {
    client.info();
    return getResponse(BuilderFactory.STRING);
  }

  public Response<String> info(final String section) {
    client.info(section);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<Long> dbSize() {
    client.dbSize();
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<String> shutdown() {
    client.shutdown();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> ping() {
    client.ping();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> select(int index) {
    client.select(index);
    Response<String> response = getResponse(BuilderFactory.STRING);
    client.setDb(index);

    return response;
  }

  @Override
  public Response<String> swapDB(int index1, int index2) {
    client.swapDB(index1, index2);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<Long> bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
    client.bitop(op, destKey, srcKeys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> bitop(BitOP op, String destKey, String... srcKeys) {
    client.bitop(op, destKey, srcKeys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<String> clusterNodes() {
    client.clusterNodes();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> clusterMeet(final String ip, final int port) {
    client.clusterMeet(ip, port);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> clusterAddSlots(final int... slots) {
    client.clusterAddSlots(slots);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> clusterDelSlots(final int... slots) {
    client.clusterDelSlots(slots);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> clusterInfo() {
    client.clusterInfo();
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<List<String>> clusterGetKeysInSlot(final int slot, final int count) {
    client.clusterGetKeysInSlot(slot, count);
    return getResponse(BuilderFactory.STRING_LIST);
  }

  @Override
  public Response<String> clusterSetSlotNode(final int slot, final String nodeId) {
    client.clusterSetSlotNode(slot, nodeId);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> clusterSetSlotMigrating(final int slot, final String nodeId) {
    client.clusterSetSlotMigrating(slot, nodeId);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> clusterSetSlotImporting(final int slot, final String nodeId) {
    client.clusterSetSlotImporting(slot, nodeId);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<Object> eval(String script) {
    return this.eval(script, 0, new String[0]);
  }

  @Override
  public Response<Object> eval(String script, List<String> keys, List<String> args) {
    String[] argv = Jedis.getParams(keys, args);
    return this.eval(script, keys.size(), argv);
  }

  @Override
  public Response<Object> eval(String script, int keyCount, String... params) {
    getClient(script).eval(script, keyCount, params);
    return getResponse(BuilderFactory.ENCODED_OBJECT);
  }

  @Override
  public Response<Object> evalsha(String sha1) {
    return this.evalsha(sha1, 0, new String[0]);
  }

  @Override
  public Response<Object> evalsha(String sha1, List<String> keys, List<String> args) {
    String[] argv = Jedis.getParams(keys, args);
    return this.evalsha(sha1, keys.size(), argv);
  }

  @Override
  public Response<Object> evalsha(String sha1, int keyCount, String... params) {
    getClient(sha1).evalsha(sha1, keyCount, params);
    return getResponse(BuilderFactory.ENCODED_OBJECT);
  }

  @Override
  public Response<Object> eval(byte[] script) {
    return this.eval(script, 0);
  }

  @Override
  public Response<Object> eval(byte[] script, byte[] keyCount, byte[]... params) {
    getClient(script).eval(script, keyCount, params);
    return getResponse(BuilderFactory.RAW_OBJECT);
  }

  @Override
  public Response<Object> eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
    byte[][] argv = BinaryJedis.getParamsWithBinary(keys, args);
    return this.eval(script, keys.size(), argv);
  }

  @Override
  public Response<Object> eval(byte[] script, int keyCount, byte[]... params) {
    getClient(script).eval(script, keyCount, params);
    return getResponse(BuilderFactory.RAW_OBJECT);
  }

  @Override
  public Response<Object> evalsha(byte[] sha1) {
    return this.evalsha(sha1, 0);
  }

  @Override
  public Response<Object> evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
    byte[][] argv = BinaryJedis.getParamsWithBinary(keys, args);
    return this.evalsha(sha1, keys.size(), argv);
  }

  @Override
  public Response<Object> evalsha(byte[] sha1, int keyCount, byte[]... params) {
    getClient(sha1).evalsha(sha1, keyCount, params);
    return getResponse(BuilderFactory.RAW_OBJECT);
  }

  @Override
  public Response<Long> pfcount(String... keys) {
    client.pfcount(keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> pfcount(final byte[]... keys) {
    client.pfcount(keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<String> pfmerge(byte[] destkey, byte[]... sourcekeys) {
    client.pfmerge(destkey, sourcekeys);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> pfmerge(String destkey, String... sourcekeys) {
    client.pfmerge(destkey, sourcekeys);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<List<String>> time() {
    client.time();
    return getResponse(BuilderFactory.STRING_LIST);
  }

  @Override
  public Response<Long> touch(String... keys) {
    client.touch(keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> touch(byte[]... keys) {
    client.touch(keys);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<String> moduleUnload(String name) {
    client.moduleUnload(name);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<List<Module>> moduleList() {
    client.moduleList();
    return getResponse(BuilderFactory.MODULE_LIST);
  }

  @Override
  public Response<String> moduleLoad(String path) {
    client.moduleLoad(path);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> migrate(final String host, final int port, final int destinationDB,
      final int timeout, final MigrateParams params, final String... keys) {
    client.migrate(host, port, destinationDB, timeout, params, keys);
    return getResponse(BuilderFactory.STRING);
  }

  @Override
  public Response<String> migrate(final String host, final int port, final int destinationDB,
      final int timeout, final MigrateParams params, final byte[]... keys) {
    client.migrate(host, port, destinationDB, timeout, params, keys);
    return getResponse(BuilderFactory.STRING);
  }

  public Response<Object> sendCommand(final ProtocolCommand cmd, final String... args) {
    client.sendCommand(cmd, args);
    return getResponse(BuilderFactory.RAW_OBJECT);
  }

  public Response<Object> sendCommand(final ProtocolCommand cmd, final byte[]... args) {
    client.sendCommand(cmd, args);
    return getResponse(BuilderFactory.RAW_OBJECT);
  }

  @Override
  public Response<Long> georadiusStore(final String key, final double longitude,
      final double latitude, final double radius, final GeoUnit unit, final GeoRadiusParam param,
      final GeoRadiusStoreParam storeParam) {
    client.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> georadiusStore(final byte[] key, final double longitude,
      final double latitude, final double radius, final GeoUnit unit, final GeoRadiusParam param,
      final GeoRadiusStoreParam storeParam) {
    client.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> georadiusByMemberStore(final byte[] key, final byte[] member,
      final double radius, final GeoUnit unit, final GeoRadiusParam param,
      final GeoRadiusStoreParam storeParam) {
    client.georadiusByMemberStore(key, member, radius, unit, param, storeParam);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<Long> georadiusByMemberStore(final String key, final String member,
      final double radius, final GeoUnit unit, final GeoRadiusParam param,
      final GeoRadiusStoreParam storeParam) {
    client.georadiusByMemberStore(key, member, radius, unit, param, storeParam);
    return getResponse(BuilderFactory.LONG);
  }

  @Override
  public Response<List<byte[]>> xread(int count, long block, Map<byte[], byte[]> streams) {
    client.xread(count, block, streams);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<List<byte[]>> xread(XReadParams xReadParams,
      Map.Entry<byte[], byte[]>... streams) {
    client.xread(xReadParams, streams);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<List<byte[]>> xreadGroup(byte[] groupname, byte[] consumer, int count, long block,
      boolean noAck, Map<byte[], byte[]> streams) {
    client.xreadGroup(groupname, consumer, count, block, noAck, streams);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<List<byte[]>> xreadGroup(final byte[] groupname, final byte[] consumer,
      final XReadGroupParams xReadGroupParams, final Map.Entry<byte[], byte[]>... streams) {
    client.xreadGroup(groupname, consumer, xReadGroupParams, streams);
    return getResponse(BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public Response<List<Map.Entry<String, List<StreamEntry>>>> xread(int count, long block,
      Map.Entry<String, StreamEntryID>... streams) {
    client.xread(count, block, streams);
    return getResponse(BuilderFactory.STREAM_READ_RESPONSE);
  }

  @Override
  public Response<List<Map.Entry<String, List<StreamEntry>>>> xread(final XReadParams xReadParams,
      final Map<String, StreamEntryID> streams) {
    client.xread(xReadParams, streams);
    return getResponse(BuilderFactory.STREAM_READ_RESPONSE);
  }

  @Override
  public Response<List<Map.Entry<String, List<StreamEntry>>>> xreadGroup(String groupname,
      String consumer, int count, long block, boolean noAck,
      Map.Entry<String, StreamEntryID>... streams) {
    client.xreadGroup(groupname, consumer, count, block, noAck, streams);
    return getResponse(BuilderFactory.STREAM_READ_RESPONSE);
  }

  @Override
  public Response<List<Map.Entry<String, List<StreamEntry>>>> xreadGroup(final String groupname,
      final String consumer, final XReadGroupParams xReadGroupParams,
      final Map<String, StreamEntryID> streams) {
    client.xreadGroup(groupname, consumer, xReadGroupParams, streams);
    return getResponse(BuilderFactory.STREAM_READ_RESPONSE);
  }
}
