package redis.clients.jedis;

import java.util.List;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.exceptions.JedisNoReachableClusterNodeException;

public class JedisSlotBasedConnectionHandler extends JedisClusterConnectionHandler {

  public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes,
      final GenericObjectPoolConfig<Jedis> poolConfig, int timeout) {
    this(nodes, poolConfig, timeout, timeout);
  }

  public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes,
      final GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout) {
    this(nodes, poolConfig, connectionTimeout, soTimeout, null);
  }

  public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes,
      GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout,
      String password) {
    super(nodes, poolConfig, connectionTimeout, soTimeout, password);
  }

  public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes,
      GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout,
      String password, String clientName) {
    super(nodes, poolConfig, connectionTimeout, soTimeout, password, clientName);
  }

  public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes,
      GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout, String user,
      String password, String clientName) {
    super(nodes, poolConfig, connectionTimeout, soTimeout, user, password, clientName);
  }

  public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes,
      GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout,
      int infiniteSoTimeout, String user, String password, String clientName) {
    super(nodes, poolConfig, connectionTimeout, soTimeout, infiniteSoTimeout, user, password,
        clientName);
  }

  public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes,
      GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout,
      String password, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory,
      SSLParameters sslParameters, HostnameVerifier hostnameVerifier,
      JedisClusterHostAndPortMap portMap) {
    super(nodes, poolConfig, connectionTimeout, soTimeout, password, clientName, ssl,
        sslSocketFactory, sslParameters, hostnameVerifier, portMap);
  }

  public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes,
      GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout, String user,
      String password, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory,
      SSLParameters sslParameters, HostnameVerifier hostnameVerifier,
      JedisClusterHostAndPortMap portMap) {
    super(nodes, poolConfig, connectionTimeout, soTimeout, user, password, clientName, ssl,
        sslSocketFactory, sslParameters, hostnameVerifier, portMap);
  }

  public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes,
      GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout,
      int infiniteSoTimeout, String user, String password, String clientName, boolean ssl,
      SSLSocketFactory sslSocketFactory, SSLParameters sslParameters,
      HostnameVerifier hostnameVerifier, JedisClusterHostAndPortMap portMap) {
    super(nodes, poolConfig, connectionTimeout, soTimeout, infiniteSoTimeout, user, password,
        clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier, portMap);
  }

  public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes,
      GenericObjectPoolConfig<Jedis> poolConfig, JedisClientConfig clientConfig) {
    super(nodes, poolConfig, clientConfig);
  }

  @Override
  public Jedis getConnection() {
    // In antirez's redis-rb-cluster implementation, getRandomConnection always
    // return valid connection (able to ping-pong) or exception if all
    // connections are invalid

    List<JedisPool> pools = cache.getShuffledNodesPool();

    JedisException suppressed = null;
    for (JedisPool pool : pools) {
      Jedis jedis = null;
      try {
        jedis = pool.getResource();

        if (jedis == null) {
          continue;
        }

        if (jedis.ping().equalsIgnoreCase("pong")) {
          return jedis;
        }

        jedis.close();
      } catch (JedisException ex) {
        if (suppressed == null) { // remembering first suppressed exception
          suppressed = ex;
        }
        if (jedis != null) {
          jedis.close();
        }
      }
    }

    JedisNoReachableClusterNodeException noReachableNode =
        new JedisNoReachableClusterNodeException("No reachable node in cluster.");
    if (suppressed != null) {
      noReachableNode.addSuppressed(suppressed);
    }
    throw noReachableNode;
  }

  @Override
  public Jedis getConnectionFromSlot(int slot) {
    JedisPool connectionPool = cache.getSlotPool(slot);
    if (connectionPool != null) {
      // It can't guaranteed to get valid connection because of node assignment
      return connectionPool.getResource();
    } else {
      // It's abnormal situation for cluster mode that we have just nothing for slot.
      // Try to rediscover state
      renewSlotCache();
      connectionPool = cache.getSlotPool(slot);
      if (connectionPool != null) {
        return connectionPool.getResource();
      } else {
        // no choice, fallback to new connection to random node
        return getConnection();
      }
    }
  }
}
