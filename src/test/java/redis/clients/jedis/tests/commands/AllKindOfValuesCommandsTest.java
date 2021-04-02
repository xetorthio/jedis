package redis.clients.jedis.tests.commands;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static redis.clients.jedis.Protocol.Command.BLPOP;
import static redis.clients.jedis.Protocol.Command.HGETALL;
import static redis.clients.jedis.Protocol.Command.GET;
import static redis.clients.jedis.Protocol.Command.LRANGE;
import static redis.clients.jedis.Protocol.Command.PING;
import static redis.clients.jedis.Protocol.Command.RPUSH;
import static redis.clients.jedis.Protocol.Command.SET;
import static redis.clients.jedis.Protocol.Command.XINFO;
import static redis.clients.jedis.ScanParams.SCAN_POINTER_START;
import static redis.clients.jedis.ScanParams.SCAN_POINTER_START_BINARY;
import static redis.clients.jedis.params.SetParams.setParams;
import static redis.clients.jedis.tests.utils.AssertUtil.assertCollectionContains;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import redis.clients.jedis.Protocol.Keyword;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.args.FlushMode;
import redis.clients.jedis.params.RestoreParams;
import redis.clients.jedis.tests.HostAndPortUtil;
import redis.clients.jedis.util.SafeEncoder;
import redis.clients.jedis.exceptions.JedisDataException;

public class AllKindOfValuesCommandsTest extends JedisCommandTestBase {
  final byte[] bfoo = { 0x01, 0x02, 0x03, 0x04 };
  final byte[] bfoo1 = { 0x01, 0x02, 0x03, 0x04, 0x0A };
  final byte[] bfoo2 = { 0x01, 0x02, 0x03, 0x04, 0x0B };
  final byte[] bfoo3 = { 0x01, 0x02, 0x03, 0x04, 0x0C };
  final byte[] bbar = { 0x05, 0x06, 0x07, 0x08 };
  final byte[] bbar1 = { 0x05, 0x06, 0x07, 0x08, 0x0A };
  final byte[] bbar2 = { 0x05, 0x06, 0x07, 0x08, 0x0B };
  final byte[] bbar3 = { 0x05, 0x06, 0x07, 0x08, 0x0C };

  final byte[] bfoobar = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
  final byte[] bfoostar = { 0x01, 0x02, 0x03, 0x04, '*' };
  final byte[] bbarstar = { 0x05, 0x06, 0x07, 0x08, '*' };

  final byte[] bnx = { 0x6E, 0x78 };
  final byte[] bex = { 0x65, 0x78 };
  final int expireSeconds = 2;
  private static final HostAndPort lfuHnp = HostAndPortUtil.getRedisServers().get(7);

  @Test
  public void ping() {
    String status = jedis.ping();
    assertEquals("PONG", status);
  }

  @Test
  public void pingWithMessage() {
    String argument = "message";
    assertEquals(argument, jedis.ping(argument));

    assertArrayEquals(bfoobar, jedis.ping(bfoobar));
  }

  @Test
  public void exists() {
    String status = jedis.set("foo", "bar");
    assertEquals("OK", status);

    status = jedis.set(bfoo, bbar);
    assertEquals("OK", status);

    boolean reply = jedis.exists("foo");
    assertTrue(reply);

    reply = jedis.exists(bfoo);
    assertTrue(reply);

    long lreply = jedis.del("foo");
    assertEquals(1, lreply);

    lreply = jedis.del(bfoo);
    assertEquals(1, lreply);

    reply = jedis.exists("foo");
    assertFalse(reply);

    reply = jedis.exists(bfoo);
    assertFalse(reply);
  }

  @Test
  public void existsMany() {
    String status = jedis.set("foo1", "bar1");
    assertEquals("OK", status);

    status = jedis.set("foo2", "bar2");
    assertEquals("OK", status);

    long reply = jedis.exists("foo1", "foo2");
    assertEquals(2, reply);

    long lreply = jedis.del("foo1");
    assertEquals(1, lreply);

    reply = jedis.exists("foo1", "foo2");
    assertEquals(1, reply);
  }

  @Test
  public void del() {
    jedis.set("foo1", "bar1");
    jedis.set("foo2", "bar2");
    jedis.set("foo3", "bar3");

    long reply = jedis.del("foo1", "foo2", "foo3");
    assertEquals(3, reply);

    Boolean breply = jedis.exists("foo1");
    assertFalse(breply);
    breply = jedis.exists("foo2");
    assertFalse(breply);
    breply = jedis.exists("foo3");
    assertFalse(breply);

    jedis.set("foo1", "bar1");

    reply = jedis.del("foo1", "foo2");
    assertEquals(1, reply);

    reply = jedis.del("foo1", "foo2");
    assertEquals(0, reply);

    // Binary ...
    jedis.set(bfoo1, bbar1);
    jedis.set(bfoo2, bbar2);
    jedis.set(bfoo3, bbar3);

    reply = jedis.del(bfoo1, bfoo2, bfoo3);
    assertEquals(3, reply);

    breply = jedis.exists(bfoo1);
    assertFalse(breply);
    breply = jedis.exists(bfoo2);
    assertFalse(breply);
    breply = jedis.exists(bfoo3);
    assertFalse(breply);

    jedis.set(bfoo1, bbar1);

    reply = jedis.del(bfoo1, bfoo2);
    assertEquals(1, reply);

    reply = jedis.del(bfoo1, bfoo2);
    assertEquals(0, reply);
  }

  @Test
  public void unlink() {
    jedis.set("foo1", "bar1");
    jedis.set("foo2", "bar2");
    jedis.set("foo3", "bar3");

    long reply = jedis.unlink("foo1", "foo2", "foo3");
    assertEquals(3, reply);

    reply = jedis.exists("foo1", "foo2", "foo3");
    assertEquals(0, reply);

    jedis.set("foo1", "bar1");

    reply = jedis.unlink("foo1", "foo2");
    assertEquals(1, reply);

    reply = jedis.unlink("foo1", "foo2");
    assertEquals(0, reply);

    // Binary ...
    jedis.set(bfoo1, bbar1);
    jedis.set(bfoo2, bbar2);
    jedis.set(bfoo3, bbar3);

    reply = jedis.unlink(bfoo1, bfoo2, bfoo3);
    assertEquals(3, reply);

    reply = jedis.exists(bfoo1, bfoo2, bfoo3);
    assertEquals(0, reply);

    jedis.set(bfoo1, bbar1);

    reply = jedis.unlink(bfoo1, bfoo2);
    assertEquals(1, reply);

    reply = jedis.unlink(bfoo1, bfoo2);
    assertEquals(0, reply);
  }

  @Test
  public void type() {
    jedis.set("foo", "bar");
    String status = jedis.type("foo");
    assertEquals("string", status);

    // Binary
    jedis.set(bfoo, bbar);
    status = jedis.type(bfoo);
    assertEquals("string", status);
  }

  @Test
  public void keys() {
    jedis.set("foo", "bar");
    jedis.set("foobar", "bar");

    Set<String> keys = jedis.keys("foo*");
    Set<String> expected = new HashSet<>();
    expected.add("foo");
    expected.add("foobar");
    assertEquals(expected, keys);

    expected = new HashSet<>();
    keys = jedis.keys("bar*");

    assertEquals(expected, keys);

    // Binary
    jedis.set(bfoo, bbar);
    jedis.set(bfoobar, bbar);

    Set<byte[]> bkeys = jedis.keys(bfoostar);
    assertEquals(2, bkeys.size());
    assertCollectionContains(bkeys, bfoo);
    assertCollectionContains(bkeys, bfoobar);

    bkeys = jedis.keys(bbarstar);

    assertEquals(0, bkeys.size());
  }

  @Test
  public void randomKey() {
    assertNull(jedis.randomKey());

    jedis.set("foo", "bar");

    assertEquals("foo", jedis.randomKey());

    jedis.set("bar", "foo");

    String randomkey = jedis.randomKey();
    assertTrue(randomkey.equals("foo") || randomkey.equals("bar"));

    // Binary
    jedis.del("foo");
    jedis.del("bar");
    assertNull(jedis.randomKey());

    jedis.set(bfoo, bbar);

    assertArrayEquals(bfoo, jedis.randomBinaryKey());

    jedis.set(bbar, bfoo);

    byte[] randomBkey = jedis.randomBinaryKey();
    assertTrue(Arrays.equals(randomBkey, bfoo) || Arrays.equals(randomBkey, bbar));

  }

  @Test
  public void rename() {
    jedis.set("foo", "bar");
    String status = jedis.rename("foo", "bar");
    assertEquals("OK", status);

    String value = jedis.get("foo");
    assertNull(value);

    value = jedis.get("bar");
    assertEquals("bar", value);

    // Binary
    jedis.set(bfoo, bbar);
    String bstatus = jedis.rename(bfoo, bbar);
    assertEquals("OK", bstatus);

    byte[] bvalue = jedis.get(bfoo);
    assertNull(bvalue);

    bvalue = jedis.get(bbar);
    assertArrayEquals(bbar, bvalue);
  }

  @Test
  public void renameOldAndNewAreTheSame() {
    assertEquals("OK", jedis.set("foo", "bar"));
    assertEquals("OK", jedis.rename("foo", "foo"));

    // Binary
    assertEquals("OK", jedis.set(bfoo, bbar));
    assertEquals("OK", jedis.rename(bfoo, bfoo));
  }

  @Test
  public void renamenx() {
    jedis.set("foo", "bar");
    long status = jedis.renamenx("foo", "bar");
    assertEquals(1, status);

    jedis.set("foo", "bar");
    status = jedis.renamenx("foo", "bar");
    assertEquals(0, status);

    // Binary
    jedis.set(bfoo, bbar);
    long bstatus = jedis.renamenx(bfoo, bbar);
    assertEquals(1, bstatus);

    jedis.set(bfoo, bbar);
    bstatus = jedis.renamenx(bfoo, bbar);
    assertEquals(0, bstatus);

  }

  @Test
  public void dbSize() {
    long size = jedis.dbSize();
    assertEquals(0, size);

    jedis.set("foo", "bar");
    size = jedis.dbSize();
    assertEquals(1, size);

    // Binary
    jedis.set(bfoo, bbar);
    size = jedis.dbSize();
    assertEquals(2, size);
  }

  @Test
  public void expire() {
    long status = jedis.expire("foo", 20);
    assertEquals(0, status);

    jedis.set("foo", "bar");
    status = jedis.expire("foo", 20);
    assertEquals(1, status);

    // Binary
    long bstatus = jedis.expire(bfoo, 20);
    assertEquals(0, bstatus);

    jedis.set(bfoo, bbar);
    bstatus = jedis.expire(bfoo, 20);
    assertEquals(1, bstatus);

  }

  @Test
  public void expireAt() {
    long unixTime = (System.currentTimeMillis() / 1000L) + 20;

    long status = jedis.expireAt("foo", unixTime);
    assertEquals(0, status);

    jedis.set("foo", "bar");
    unixTime = (System.currentTimeMillis() / 1000L) + 20;
    status = jedis.expireAt("foo", unixTime);
    assertEquals(1, status);

    // Binary
    long bstatus = jedis.expireAt(bfoo, unixTime);
    assertEquals(0, bstatus);

    jedis.set(bfoo, bbar);
    unixTime = (System.currentTimeMillis() / 1000L) + 20;
    bstatus = jedis.expireAt(bfoo, unixTime);
    assertEquals(1, bstatus);

  }

  @Test
  public void ttl() {
    long ttl = jedis.ttl("foo");
    assertEquals(-2, ttl);

    jedis.set("foo", "bar");
    ttl = jedis.ttl("foo");
    assertEquals(-1, ttl);

    jedis.expire("foo", 20);
    ttl = jedis.ttl("foo");
    assertTrue(ttl >= 0 && ttl <= 20);

    // Binary
    long bttl = jedis.ttl(bfoo);
    assertEquals(-2, bttl);

    jedis.set(bfoo, bbar);
    bttl = jedis.ttl(bfoo);
    assertEquals(-1, bttl);

    jedis.expire(bfoo, 20);
    bttl = jedis.ttl(bfoo);
    assertTrue(bttl >= 0 && bttl <= 20);

  }

  @Test
  public void touch() throws Exception {
    long reply = jedis.touch("foo1", "foo2", "foo3");
    assertEquals(0, reply);

    jedis.set("foo1", "bar1");

    Thread.sleep(1100); // little over 1 sec
    assertTrue(jedis.objectIdletime("foo1") > 0);

    reply = jedis.touch("foo1");
    assertEquals(1, reply);
    assertEquals(0L, jedis.objectIdletime("foo1").longValue());

    reply = jedis.touch("foo1", "foo2", "foo3");
    assertEquals(1, reply);

    jedis.set("foo2", "bar2");

    jedis.set("foo3", "bar3");

    reply = jedis.touch("foo1", "foo2", "foo3");
    assertEquals(3, reply);

    // Binary
    reply = jedis.touch(bfoo1, bfoo2, bfoo3);
    assertEquals(0, reply);

    jedis.set(bfoo1, bbar1);

    Thread.sleep(1100); // little over 1 sec
    assertTrue(jedis.objectIdletime(bfoo1) > 0);

    reply = jedis.touch(bfoo1);
    assertEquals(1, reply);
    assertEquals(0L, jedis.objectIdletime(bfoo1).longValue());

    reply = jedis.touch(bfoo1, bfoo2, bfoo3);
    assertEquals(1, reply);

    jedis.set(bfoo2, bbar2);

    jedis.set(bfoo3, bbar3);

    reply = jedis.touch(bfoo1, bfoo2, bfoo3);
    assertEquals(3, reply);

  }

  @Test
  public void select() {
    jedis.set("foo", "bar");
    String status = jedis.select(1);
    assertEquals("OK", status);
    assertNull(jedis.get("foo"));
    status = jedis.select(0);
    assertEquals("OK", status);
    assertEquals("bar", jedis.get("foo"));
    // Binary
    jedis.set(bfoo, bbar);
    String bstatus = jedis.select(1);
    assertEquals("OK", bstatus);
    assertNull(jedis.get(bfoo));
    bstatus = jedis.select(0);
    assertEquals("OK", bstatus);
    assertArrayEquals(bbar, jedis.get(bfoo));
  }

  @Test
  public void getDB() {
    assertEquals(0, jedis.getDB());
    jedis.select(1);
    assertEquals(1, jedis.getDB());
  }

  @Test
  public void move() {
    long status = jedis.move("foo", 1);
    assertEquals(0, status);

    jedis.set("foo", "bar");
    status = jedis.move("foo", 1);
    assertEquals(1, status);
    assertNull(jedis.get("foo"));

    jedis.select(1);
    assertEquals("bar", jedis.get("foo"));

    // Binary
    jedis.select(0);
    long bstatus = jedis.move(bfoo, 1);
    assertEquals(0, bstatus);

    jedis.set(bfoo, bbar);
    bstatus = jedis.move(bfoo, 1);
    assertEquals(1, bstatus);
    assertNull(jedis.get(bfoo));

    jedis.select(1);
    assertArrayEquals(bbar, jedis.get(bfoo));

  }

  @Test
  public void swapDB() {
    jedis.set("foo1", "bar1");
    jedis.select(1);
    assertNull(jedis.get("foo1"));
    jedis.set("foo2", "bar2");
    String status = jedis.swapDB(0, 1);
    assertEquals("OK", status);
    assertEquals("bar1", jedis.get("foo1"));
    assertNull(jedis.get("foo2"));
    jedis.select(0);
    assertNull(jedis.get("foo1"));
    assertEquals("bar2", jedis.get("foo2"));

    // Binary
    jedis.set(bfoo1, bbar1);
    jedis.select(1);
    assertArrayEquals(null, jedis.get(bfoo1));
    jedis.set(bfoo2, bbar2);
    status = jedis.swapDB(0, 1);
    assertEquals("OK", status);
    assertArrayEquals(bbar1, jedis.get(bfoo1));
    assertArrayEquals(null, jedis.get(bfoo2));
    jedis.select(0);
    assertArrayEquals(null, jedis.get(bfoo1));
    assertArrayEquals(bbar2, jedis.get(bfoo2));
  }

  @Test
  public void flushDB() {
    jedis.set("foo", "bar");
    assertEquals(1, jedis.dbSize().intValue());
    jedis.set("bar", "foo");
    jedis.move("bar", 1);
    String status = jedis.flushDB();
    assertEquals("OK", status);
    assertEquals(0, jedis.dbSize().intValue());
    jedis.select(1);
    assertEquals(1, jedis.dbSize().intValue());
    assertEquals("OK", jedis.flushDB(FlushMode.SYNC));
    assertEquals(0, jedis.dbSize().intValue());

    // Binary
    jedis.select(0);
    jedis.set(bfoo, bbar);
    assertEquals(1, jedis.dbSize().intValue());
    jedis.set(bbar, bfoo);
    jedis.move(bbar, 1);
    String bstatus = jedis.flushDB();
    assertEquals("OK", bstatus);
    assertEquals(0, jedis.dbSize().intValue());
    jedis.select(1);
    assertEquals(1, jedis.dbSize().intValue());
    assertEquals("OK", jedis.flushDB(FlushMode.ASYNC));
    assertEquals(0, jedis.dbSize().intValue());
  }

  @Test
  public void flushAll() {
    jedis.set("foo", "bar");
    assertEquals(1, jedis.dbSize().intValue());
    jedis.set("bar", "foo");
    jedis.move("bar", 1);
    String status = jedis.flushAll();
    assertEquals("OK", status);
    assertEquals(0, jedis.dbSize().intValue());
    jedis.select(1);
    assertEquals(0, jedis.dbSize().intValue());
    jedis.set("foo", "bar");
    assertEquals(1, jedis.dbSize().intValue());
    assertEquals("OK", jedis.flushAll(FlushMode.SYNC));
    assertEquals(0, jedis.dbSize().intValue());

    // Binary
    jedis.select(0);
    jedis.set(bfoo, bbar);
    assertEquals(1, jedis.dbSize().intValue());
    jedis.set(bbar, bfoo);
    jedis.move(bbar, 1);
    String bstatus = jedis.flushAll();
    assertEquals("OK", bstatus);
    assertEquals(0, jedis.dbSize().intValue());
    jedis.select(1);
    assertEquals(0, jedis.dbSize().intValue());
    jedis.set(bfoo, bbar);
    assertEquals(1, jedis.dbSize().intValue());
    assertEquals("OK", jedis.flushAll(FlushMode.ASYNC));
    assertEquals(0, jedis.dbSize().intValue());
  }

  @Test
  public void persist() {
    jedis.setex("foo", 60 * 60, "bar");
    assertTrue(jedis.ttl("foo") > 0);
    long status = jedis.persist("foo");
    assertEquals(1, status);
    assertEquals(-1, jedis.ttl("foo").intValue());

    // Binary
    jedis.setex(bfoo, 60 * 60, bbar);
    assertTrue(jedis.ttl(bfoo) > 0);
    long bstatus = jedis.persist(bfoo);
    assertEquals(1, bstatus);
    assertEquals(-1, jedis.ttl(bfoo).intValue());

  }

  @Test
  public void echo() {
    String result = jedis.echo("hello world");
    assertEquals("hello world", result);

    // Binary
    byte[] bresult = jedis.echo(SafeEncoder.encode("hello world"));
    assertArrayEquals(SafeEncoder.encode("hello world"), bresult);
  }

  @Test
  public void dumpAndRestore() {
    jedis.set("foo1", "bar");
    byte[] sv = jedis.dump("foo1");
    jedis.restore("foo2", 0, sv);
    assertEquals("bar", jedis.get("foo2"));
  }

  @Test
  public void restoreReplace() {
    // take a separate instance
    Jedis jedis2 = new Jedis(hnp.getHost(), 6380, 500);
    jedis2.auth("foobared");
    jedis2.flushAll();

    jedis2.set("foo", "bar");

    Map<String, String> map = new HashMap<>();
    map.put("a", "A");
    map.put("b", "B");

    jedis.hset("from", map);
    byte[] serialized = jedis.dump("from");

    try {
      jedis2.restore("foo", 0, serialized);
      fail("Simple restore on a existing key should fail");
    } catch (JedisDataException e) {
      // should be here
    }
    assertEquals("bar", jedis2.get("foo"));

    jedis2.restoreReplace("foo", 0, serialized);
    assertEquals(map, jedis2.hgetAll("foo"));

    jedis2.close();
  }

  @Test
  public void restoreParams() {
    // take a separate instance
    Jedis jedis2 = new Jedis(hnp.getHost(), 6380, 500);
    jedis2.auth("foobared");
    jedis2.flushAll();

    jedis2.set("foo", "bar");
    jedis.set("from", "a");
    byte[] serialized = jedis.dump("from");

    try {
      jedis2.restore("foo", 0, serialized, null);
      fail("Simple restore on a existing key should fail");
    } catch (JedisDataException e) {
      // should be here
    }
    assertEquals("bar", jedis2.get("foo"));

    jedis2.restore("foo", 1000, serialized, RestoreParams.restoreParams().replace());
    assertEquals("a", jedis2.get("foo"));
    assertTrue(jedis2.pttl("foo") <= 1000);

    jedis2.restore("bar", System.currentTimeMillis() + 1000, serialized,
      RestoreParams.restoreParams().replace().absTtl());
    assertTrue(jedis2.pttl("bar") <= 1000);

    jedis2.restore("bar1", 1000, serialized,
      RestoreParams.restoreParams().replace().idleTime(1000));
    assertEquals(1000, jedis2.objectIdletime("bar1").longValue());
    jedis2.close();

    Jedis lfuJedis = new Jedis(lfuHnp.getHost(), lfuHnp.getPort(), 500);
    lfuJedis.restore("bar1", 1000, serialized,
      RestoreParams.restoreParams().replace().frequency(90));
    assertEquals(90, lfuJedis.objectFreq("bar1").longValue());
    lfuJedis.close();
  }

  @Test
  public void pexpire() {
    long status = jedis.pexpire("foo", 10000);
    assertEquals(0, status);

    jedis.set("foo1", "bar1");
    status = jedis.pexpire("foo1", 10000);
    assertEquals(1, status);

    jedis.set("foo2", "bar2");
    status = jedis.pexpire("foo2", 200000000000L);
    assertEquals(1, status);

    long pttl = jedis.pttl("foo2");
    assertTrue(pttl > 100000000000L);
  }

  @Test
  public void pexpireAt() {
    long unixTime = (System.currentTimeMillis()) + 10000;

    long status = jedis.pexpireAt("foo", unixTime);
    assertEquals(0, status);

    jedis.set("foo", "bar");
    unixTime = (System.currentTimeMillis()) + 10000;
    status = jedis.pexpireAt("foo", unixTime);
    assertEquals(1, status);
  }

  @Test
  public void pttl() {
    long pttl = jedis.pttl("foo");
    assertEquals(-2, pttl);

    jedis.set("foo", "bar");
    pttl = jedis.pttl("foo");
    assertEquals(-1, pttl);

    jedis.pexpire("foo", 20000);
    pttl = jedis.pttl("foo");
    assertTrue(pttl >= 0 && pttl <= 20000);
  }

  @Test
  public void psetex() {
    long pttl = jedis.pttl("foo");
    assertEquals(-2, pttl);

    String status = jedis.psetex("foo", 200000000000L, "bar");
    assertTrue(Keyword.OK.name().equalsIgnoreCase(status));

    pttl = jedis.pttl("foo");
    assertTrue(pttl > 100000000000L);
  }

  @Test
  public void scan() {
    jedis.set("b", "b");
    jedis.set("a", "a");

    ScanResult<String> result = jedis.scan(SCAN_POINTER_START);

    assertEquals(SCAN_POINTER_START, result.getCursor());
    assertFalse(result.getResult().isEmpty());

    // binary
    ScanResult<byte[]> bResult = jedis.scan(SCAN_POINTER_START_BINARY);

    assertArrayEquals(SCAN_POINTER_START_BINARY, bResult.getCursorAsBytes());
    assertFalse(bResult.getResult().isEmpty());
  }

  @Test
  public void scanMatch() {
    ScanParams params = new ScanParams();
    params.match("a*");

    jedis.set("b", "b");
    jedis.set("a", "a");
    jedis.set("aa", "aa");
    ScanResult<String> result = jedis.scan(SCAN_POINTER_START, params);

    assertEquals(SCAN_POINTER_START, result.getCursor());
    assertFalse(result.getResult().isEmpty());

    // binary
    params = new ScanParams();
    params.match(bfoostar);

    jedis.set(bfoo1, bbar);
    jedis.set(bfoo2, bbar);
    jedis.set(bfoo3, bbar);

    ScanResult<byte[]> bResult = jedis.scan(SCAN_POINTER_START_BINARY, params);

    assertArrayEquals(SCAN_POINTER_START_BINARY, bResult.getCursorAsBytes());
    assertFalse(bResult.getResult().isEmpty());
  }

  @Test
  public void scanCount() {
    ScanParams params = new ScanParams();
    params.count(2);

    for (int i = 0; i < 10; i++) {
      jedis.set("a" + i, "a" + i);
    }

    ScanResult<String> result = jedis.scan(SCAN_POINTER_START, params);

    assertFalse(result.getResult().isEmpty());

    // binary
    params = new ScanParams();
    params.count(2);

    jedis.set(bfoo1, bbar);
    jedis.set(bfoo2, bbar);
    jedis.set(bfoo3, bbar);

    ScanResult<byte[]> bResult = jedis.scan(SCAN_POINTER_START_BINARY, params);

    assertFalse(bResult.getResult().isEmpty());
  }

  @Test
  public void scanIsCompleteIteration() {
    for (int i = 0; i < 100; i++) {
      jedis.set("a" + i, "a" + i);
    }

    ScanResult<String> result = jedis.scan(SCAN_POINTER_START);
    // note: in theory Redis would be allowed to already return all results on the 1st scan,
    // but in practice this never happens for data sets greater than a few tens
    // see: https://redis.io/commands/scan#number-of-elements-returned-at-every-scan-call
    assertFalse(result.isCompleteIteration());

    result = scanCompletely(result.getCursor());

    assertNotNull(result);
    assertTrue(result.isCompleteIteration());
  }

  private ScanResult<String> scanCompletely(String cursor) {
    ScanResult<String> scanResult;
    do {
      scanResult = jedis.scan(cursor);
      cursor = scanResult.getCursor();
    } while (!SCAN_POINTER_START.equals(scanResult.getCursor()));

    return scanResult;
  }

  @Test
  public void setNxExAndGet() {
    String status = jedis.set("hello", "world", setParams().nx().ex(expireSeconds));
    assertTrue(Keyword.OK.name().equalsIgnoreCase(status));
    String value = jedis.get("hello");
    assertEquals("world", value);

    jedis.set("hello", "bar", setParams().nx().ex(expireSeconds));
    value = jedis.get("hello");
    assertEquals("world", value);

    long ttl = jedis.ttl("hello");
    assertTrue(ttl > 0 && ttl <= expireSeconds);

    // binary
    byte[] bworld = { 0x77, 0x6F, 0x72, 0x6C, 0x64 };
    byte[] bhello = { 0x68, 0x65, 0x6C, 0x6C, 0x6F };

    String bstatus = jedis.set(bworld, bhello, setParams().nx().ex(expireSeconds));
    assertTrue(Keyword.OK.name().equalsIgnoreCase(bstatus));
    byte[] bvalue = jedis.get(bworld);
    assertTrue(Arrays.equals(bhello, bvalue));

    jedis.set(bworld, bbar, setParams().nx().ex(expireSeconds));
    bvalue = jedis.get(bworld);
    assertTrue(Arrays.equals(bhello, bvalue));

    long bttl = jedis.ttl(bworld);
    assertTrue(bttl > 0 && bttl <= expireSeconds);
  }

  @Test
  public void setGetOptionTest() {
    String status = jedis.set("hello", "world");
    assertTrue(Keyword.OK.name().equalsIgnoreCase(status));

    String oldValue = jedis.set("hello", "jedis", setParams().get());
    assertEquals("world", oldValue);

    String newValue = jedis.get("hello");
    assertEquals("jedis", newValue);

    String nullValue = jedis.set("key", "value", setParams().get());
    assertNull(nullValue);
  }

  @Test
  public void sendCommandTest() {
    Object obj = jedis.sendCommand(SET, "x", "1");
    String returnValue = SafeEncoder.encode((byte[]) obj);
    assertEquals("OK", returnValue);
    obj = jedis.sendCommand(GET, "x");
    returnValue = SafeEncoder.encode((byte[]) obj);
    assertEquals("1", returnValue);

    jedis.sendCommand(RPUSH, "foo", "a");
    jedis.sendCommand(RPUSH, "foo", "b");
    jedis.sendCommand(RPUSH, "foo", "c");

    obj = jedis.sendCommand(LRANGE, "foo", "0", "2");
    List<byte[]> list = (List<byte[]>) obj;
    List<byte[]> expected = new ArrayList<>(3);
    expected.add("a".getBytes());
    expected.add("b".getBytes());
    expected.add("c".getBytes());
    for (int i = 0; i < 3; i++)
      assertArrayEquals(expected.get(i), list.get(i));

    assertEquals("PONG", SafeEncoder.encode((byte[]) jedis.sendCommand(PING)));
  }

  @Test
  public void sendBlockingCommandTest() {
    assertNull(jedis.sendBlockingCommand(BLPOP, "foo", Long.toString(1L)));

    jedis.sendCommand(RPUSH, "foo", "bar");
    assertEquals(Arrays.asList("foo", "bar"),
      SafeEncoder.encodeObject(jedis.sendBlockingCommand(BLPOP, "foo", Long.toString(1L))));

    assertNull(jedis.sendBlockingCommand(BLPOP, "foo", Long.toString(1L)));
  }

  @Test
  public void encodeCompleteResponse() {
    HashMap<String, String> entry = new HashMap<>();
    entry.put("foo", "bar");
    jedis.xadd("mystream", StreamEntryID.NEW_ENTRY, entry);
    String status = jedis.xgroupCreate("mystream", "mygroup", null, false);

    Object obj = jedis.sendCommand(XINFO, "STREAM", "mystream");
    List encodeObj = (List) SafeEncoder.encodeObject(obj);

    assertEquals(14, encodeObj.size());
    assertEquals("length", encodeObj.get(0));
    assertEquals(1L, encodeObj.get(1));

    List<String> entryAsList = new ArrayList<>(2);
    entryAsList.add("foo");
    entryAsList.add("bar");

    assertEquals(entryAsList, ((List) encodeObj.get(11)).get(1));

    assertEquals("PONG", SafeEncoder.encodeObject(jedis.sendCommand(PING)));

    entry.put("foo2", "bar2");
    jedis.hset("hash:test:encode", entry);
    encodeObj = (List) SafeEncoder.encodeObject(jedis.sendCommand(HGETALL, "hash:test:encode"));

    assertEquals(4, encodeObj.size());
    assertTrue(encodeObj.contains("foo"));
    assertTrue(encodeObj.contains("foo2"));

  }

  @Test
  public void copy() {
    jedis.set("foo", "bar");
    assertTrue(jedis.copy("foo", "bar", false));
    assertFalse(jedis.copy("unknown", "bar1", false));
    assertEquals("bar", jedis.get("bar"));

    // with destinationDb
    assertTrue(jedis.copy("foo", "bar1", 2, false));
    jedis.select(2);
    assertEquals("bar", jedis.get("bar1"));

    // replace
    jedis.set("foo", "bar");
    jedis.set("bar2", "b");
    assertTrue(jedis.copy("foo", "bar2", true));
    assertEquals("bar", jedis.get("bar2"));
  }
}
