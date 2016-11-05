# kafkameter - Kafka JMeter Extension

This extension provides two components:

* Kafka Producer Sampler: sends keyed messages to Kafka
* Load Generator Config Element: generates messages as synthetic load.

The purpose of the Load Generator Config Element is to dynamically create the messages for sending
to Kafka.

As each application has its own input message format and load distribution, the Load Generator
Config provides a pluggable framework for application-specific message generation. An example
implementation is included.

A sample JMeter Test Plan demonstrates wiring the Load Generator and Kafka Sampler together for a
complete test. This sample JMeter Test Plan can be found in `Kafka_Request_Local.jmx`.

## Install

Build the extension and install the extension into ```$JMETER_HOME/lib/ext```:

```
mvn clean package
cp target/kafkameter-*.jar $JMETER_HOME/lib/ext
```

## Usage

### Kafka Producer Sampler

After installing `kafkameter`, add a Java Request Sampler and select the `KafkaProducerSampler`
class name. The following properties are required.

* **kafka_brokers**: comma-separated list of hosts in the format (hostname:port).
* **kafka_topic**: the topic in Kafka to which the message will be published.
* **kafka_key**: the partition key for the message.
* **kafka_message**: the message itself.

You may also override the following:

* **kafka_message_serializer**: the Kafka client `serializer.class` property.
* **kafka_key_serializer**: the Kafka client `key.serializer.class` property.

#### Simplest Possible Example

A dummy example is useful for demonstrating integration with the Load Generator framework in JMeter.

Update pom.xml:

```
<dependency>
  <groupId>com.github.qin</groupId>
  <artifactId>kafkameter</artifactId>
  <version>0.2.3</version>
</dependency>
```

Create a file called ```DummyGenerator.java```:

```
import org.apache.commons.lang3.tuple.Pair;

import co.signal.loadgen.SyntheticLoadGenerator;

public class DummyGenerator implements SyntheticLoadGenerator {

  public DummyGenerator(String ignored) {}

  @Override
  public Pair<String, String> nextMessage() {
    return Pair.of("key", "value");
  }
}
```

Now compile and place the jar into JMeter's ```lib/ext``` directory:

```
mvn clean package
cp target/DummyGenerator.jar $JMETER_HOME/lib/ext/
```

Now you should see `DummyGenerator` as an option in the Load Generator's "Class Name" drop-down.
