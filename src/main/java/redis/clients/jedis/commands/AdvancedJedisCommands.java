package redis.clients.jedis.commands;

import java.util.List;

import redis.clients.jedis.AccessControlLogEntry;
import redis.clients.jedis.AccessControlUser;
import redis.clients.jedis.args.UnblockType;
import redis.clients.jedis.params.MigrateParams;
import redis.clients.jedis.params.ClientKillParams;
import redis.clients.jedis.util.Slowlog;

public interface AdvancedJedisCommands {

  long move(String key, int dbIndex);

  List<String> configGet(String pattern);

  String configSet(String parameter, String value);

  String slowlogReset();

  long slowlogLen();

  List<Slowlog> slowlogGet();

  List<Slowlog> slowlogGet(long entries);

  Long objectRefcount(String key);

  String objectEncoding(String key);

  Long objectIdletime(String key);

  List<String> objectHelp();

  Long objectFreq(String key);

  String migrate(String host, int port, String key, int destinationDB, int timeout);

  String migrate(String host, int port, int destinationDB, int timeout, MigrateParams params,
      String... keys);

  String clientKill(String ipPort);

  String clientKill(String ip, int port);

  long clientKill(ClientKillParams params);

  String clientGetname();

  String clientList();

  String clientList(long... clientIds);

  String clientInfo();

  String clientSetname(String name);

  long clientId();

  long clientUnblock(long clientId, UnblockType unblockType);

  String memoryDoctor();

  Long memoryUsage(String key);

  Long memoryUsage(String key, int samples);

  String aclWhoAmI();

  String aclGenPass();

  List<String> aclList();

  List<String> aclUsers();

  AccessControlUser aclGetUser(String name);

  String aclSetUser(String name);

  String aclSetUser(String name, String... keys);

  long aclDelUser(String name);

  List<String> aclCat();

  List<String> aclCat(String category);

  List<AccessControlLogEntry> aclLog();

  List<AccessControlLogEntry> aclLog(int limit);

  String aclLog(String options);

  String aclLoad();

  String aclSave();
}
