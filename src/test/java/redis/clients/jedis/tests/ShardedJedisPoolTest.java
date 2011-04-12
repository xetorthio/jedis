package redis.clients.jedis.tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.tests.HostAndPortUtil.HostAndPort;

public class ShardedJedisPoolTest extends Assert {
    private static HostAndPort redis1 = HostAndPortUtil.getRedisServers()
            .get(0);
    private static HostAndPort redis2 = HostAndPortUtil.getRedisServers()
            .get(1);

    private List<JedisShardInfo> shards;

    @Before
    public void startUp() {
        shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo(redis1.host, redis1.port));
        shards.add(new JedisShardInfo(redis2.host, redis2.port));
        shards.get(0).setPassword("foobared");
        shards.get(1).setPassword("foobared");
        Jedis j = new Jedis(shards.get(0));
        j.connect();
        j.flushAll();
        j.disconnect();
        j = new Jedis(shards.get(1));
        j.connect();
        j.flushAll();
        j.disconnect();
    }

    @Test
    public void checkConnections() {
        ShardedJedisPool pool = new ShardedJedisPool(new Config(), shards);
        ShardedJedis jedis = pool.getResource();
        jedis.set("foo", "bar");
        assertEquals("bar", jedis.get("foo"));
        pool.returnResource(jedis);
        pool.destroy();
    }

    @Test
    public void checkConnectionsWithNoServers() {
        shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("localhost", 6379, "ssa"));
        shards.add(new JedisShardInfo("localhost", 6380, "ssa"));
		Config redisConfig = new Config();
		redisConfig.testOnBorrow = false; //deactivated for now
		redisConfig.testOnReturn = true;
		redisConfig.maxActive = 200; // nro threads + margen de seguridad?
		redisConfig.minIdle = 200;

		ShardedJedisPool pool = new ShardedJedisPool(redisConfig, shards);
        ShardedJedis jedis = pool.getResource();
        pool.returnResource(jedis);
        pool.destroy();
    }

    @Test
    public void checkConnectionWithDefaultPort() {
        ShardedJedisPool pool = new ShardedJedisPool(new Config(), shards);
        ShardedJedis jedis = pool.getResource();
        jedis.set("foo", "bar");
        assertEquals("bar", jedis.get("foo"));
        pool.returnResource(jedis);
        pool.destroy();
    }

    @Test
    public void checkJedisIsReusedWhenReturned() {
        ShardedJedisPool pool = new ShardedJedisPool(new Config(), shards);
        ShardedJedis jedis = pool.getResource();
        jedis.set("foo", "0");
        pool.returnResource(jedis);

        jedis = pool.getResource();
        jedis.incr("foo");
        pool.returnResource(jedis);
        pool.destroy();
    }

    @Test
    public void checkPoolRepairedWhenJedisIsBroken() {
        ShardedJedisPool pool = new ShardedJedisPool(new Config(), shards);
        ShardedJedis jedis = pool.getResource();
        jedis.disconnect();
        pool.returnBrokenResource(jedis);

        jedis = pool.getResource();
        jedis.incr("foo");
        pool.returnResource(jedis);
        pool.destroy();
    }

    @Test(expected = JedisConnectionException.class)
    public void checkPoolOverflow() {
        Config config = new Config();
        config.maxActive = 1;
        config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_FAIL;

        ShardedJedisPool pool = new ShardedJedisPool(config, shards);

        ShardedJedis jedis = pool.getResource();
        jedis.set("foo", "0");

        ShardedJedis newJedis = pool.getResource();
        newJedis.incr("foo");
    }

    @Test
    public void shouldNotShareInstances() {
        Config config = new Config();
        config.maxActive = 2;
        config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_FAIL;

        ShardedJedisPool pool = new ShardedJedisPool(config, shards);

        ShardedJedis j1 = pool.getResource();
        ShardedJedis j2 = pool.getResource();

        assertNotSame(j1.getShard("foo"), j2.getShard("foo"));
    }

    @Test
    public void checkFailedJedisServer() {
        ShardedJedisPool pool = new ShardedJedisPool(new Config(), shards);
        ShardedJedis jedis = pool.getResource();
        jedis.incr("foo");
        pool.returnResource(jedis);
        pool.destroy();
    }

    @Test
    public void shouldReturnActiveShardsWhenOneGoesOffline() {
		Config redisConfig = new Config();
		redisConfig.testOnBorrow = false;
        ShardedJedisPool pool = new ShardedJedisPool(redisConfig, shards);
        ShardedJedis jedis = pool.getResource();
        //fill the shards
        for (int i = 0; i < 1000; i++) {
            jedis.set("a-test-" + i, "0");
        }
        pool.returnResource(jedis);
        //check quantity for each shard
        Jedis j = new Jedis(shards.get(0));
        j.connect();
        Long c1 = j.dbSize();
        j.disconnect();
        j = new Jedis(shards.get(1));
        j.connect();
        Long c2 = j.dbSize();
        j.disconnect();
        //shutdown shard 2 and check thay the pool returns an instance with c1 items on one shard
        //alter shard 1 and recreate pool
        pool.destroy();
        shards.set(1, new JedisShardInfo("nohost", 1234));
        pool = new ShardedJedisPool(redisConfig, shards);
        jedis = pool.getResource();
        Long actual = new Long(0);
        Long fails = new Long(0);
        for (int i = 0; i < 1000; i++) {
            try {
                jedis.get("a-test-" + i);
                actual++;
            } catch (RuntimeException e) {
                fails++;
            }
        }
        pool.returnResource(jedis);
        pool.destroy();
        assertEquals (actual, c1);
        assertEquals (fails, c2);
    }


}

