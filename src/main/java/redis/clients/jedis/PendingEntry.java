package redis.clients.jedis;

import java.io.IOException;
import java.io.Serializable;

public class PendingEntry implements Serializable{
  
  private static final long serialVersionUID = 1L;
  
  private EntryID id;
  private String consumerName;
  private long idleTime;
  private long deliveredTimes;
  
  public PendingEntry(EntryID id, String consumerName, long idleTime, long deliveredTimes) {
    this.id = id;
    this.consumerName = consumerName;
    this.idleTime = idleTime;
    this.deliveredTimes = deliveredTimes;
  }
  
  public EntryID getID() {
    return id;
  }

  public long getIdleTime() {
    return idleTime;
  }

  public long getDeliveredTimes() {
    return deliveredTimes;
  }

  public String getConsumerName() {
    return consumerName;
  }
  
  @Override
  public String toString() {
    return this.id + " " + this.consumerName + " idle:" + this.idleTime + " times:" + this.deliveredTimes;
  }
  
  private void writeObject(java.io.ObjectOutputStream out) throws IOException{
    out.writeUnshared(this.id);
    out.writeUTF(this.consumerName);
    out.writeLong(idleTime);
    out.writeLong(this.deliveredTimes);
  }
  
  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
    this.id = (EntryID) in.readUnshared();
    this.consumerName = in.readUTF();
    this.idleTime = in.readLong();
    this.deliveredTimes = in.readLong();
  }

}
