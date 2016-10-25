package co.signal.kafkameter;

public class HashPartitioner {

  public static int partition(Object key, int numPartitions) {
    return Math.abs(key.hashCode()) % numPartitions;
  }

}
