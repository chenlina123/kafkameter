# kafkameter - Kafka JMeter Extension

This extension provides two components:

* Kafka Producer Sampler: sends keyed messages to Kafka
* Load Generator Config Element: generates messages as synthetic load.

The purpose of the Load Generator Config Element is to dynamically create the messages for sending
to Kafka. You could alternatively use the CSV Data Set or another source for messages.

As each application has its own input message format and load distribution, the Load Generator
Config provides a pluggable framework for application-specific message generation. An example
implementation is included.

A sample JMeter Test Plan demonstrates wiring the Load Generator and Kafka Sampler together for a
complete test. A [Counter](http://jmeter.apache.org/usermanual/component_reference.html#Counter)
is used as the `kafka_key` to ensure the load is distributed across all available Kafka partitions.
This sample JMeter Test Plan can be found in `Tagserve-Kafka.jmx`.

## Install

Build the extension and install the extension into ```$JMETER_HOME/lib/ext```:

```
mvn clean package
cp target/kafkameter-0.2.1.jar $JMETER_HOME/lib/ext
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

### Load Generator Config

After installing `kafkameter`, the Load Generator will be available as a Config Element.

This component reads a Synthetic Load Description from a given file, uses a Synthetic Load Generator
plugin to generate a new message on each iteration, and makes this message available to other
elements under the given variable name. The Synthetic Load Description format will be specific
to each Synthetic Load Generator.

#### Simplest Possible Example

A dummy example is useful for demonstrating integration with the Load Generator framework in JMeter.

Update pom.xml:

```
<dependency>
  <groupId>com.github.qin</groupId>
  <artifactId>kafkameter</artifactId>
  <version>0.2.1</version>
</dependency>
```

Create a file called ```DummyGenerator.java```:

```
import co.signal.loadgen.SyntheticLoadGenerator;

public class DummyGenerator implements SyntheticLoadGenerator {

  public DummyGenerator(String ignored) {}

  @Override
  public String nextMessage() {
    return "Hey! Dum-dum! You give me gum-gum.";
  }
}
```

Now compile and place the jar into JMeter's ```lib/ext``` directory:

```
mvn clean package
cp target/DummyGenerator.jar $JMETER_HOME/lib/ext/
```

Now you should see `DummyGenerator` as an option in the Load Generator's "Class Name" drop-down.

