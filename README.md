# Pravega SCDF Binder (Prototype)

Spring Cloud Stream Binder to be used in a Spring Cloud Data Flow streaming data pipeline. This is a sample prototype that reuses the
Getting Started Pravega sample code and default properties as a Binder implementation.

## Sample Application

The project consists of the Binder implementation and a simple Spring Cloud Stream example that reads messages from a Sink channel named `input` and writes the contents back to a Source channel named `output`. 

```java
@EnableBinding({Sink.class, Source.class})

@StreamListener(Sink.INPUT)
@SendTo(Source.OUTPUT)
public String handle(String message) {
    return String.format("%s there", message);
}
```

## Configuration File

The `input` and `output` channels are mapped to Pravega streams using the external `application.properties` configuration file.

The `spring.cloud.stream.pravega.binder.*` properties configures the Controller URI: 
```properties
spring.cloud.stream.pravega.binder.uri=tcp://127.0.0.1:9090
```

The `input` channel is mapped to the Pravega `inputStream` stream. The scope is configured using extended binder properties:
```properties
spring.cloud.stream.bindings.input.destination=inputStream
spring.cloud.stream.pravega.bindings.input.consumer.scope=examples
```

The `output` chananel is mapped to the Pravega `outputStream` stream. The scope and routing key is configured using extended binder properties:
```properties
spring.cloud.stream.bindings.output.destination=outputStream
spring.cloud.stream.pravega.bindings.output.producer.scope=examples
spring.cloud.stream.pravega.bindings.output.producer.routingKey=helloRoutingKey
```

## Building

Build the sample application and the Binder:

```shell script
./mvnw package
```

## Running

Start Pravega using docker:
```shell script
docker run -it -e HOST_IP=127.0.0.1 -p 9090:9090 -p 12345:12345 pravega/pravega:0.7.0 standalone
```

Start the sample application:
```shell script
./mvnw spring-boot:run
```

Using the Pravega Samples, send a message to the `inputStream` stream:
```shell script
bin/helloWorldWriter -n inputStream -m hello
```

Using the Pravega Samples, read the message written back in the `outputStream` stream:
```shell script
bin/helloWorldReader -n outputStream
```

The console output is `hello there`.

## Binder Implementation Classes Description

![Binder Diagram](https://raw.githubusercontent.com/spring-cloud/spring-cloud-stream/master/docs/src/main/asciidoc/images/producers-consumers.png)

A Binder connects input and output channels to external middleware. The message producer listens to an external stream and writes 
them to the internal channel, working as a producer. The message handler receives messages from the internal channels and writes
them to the external stream, working as a consumer.  

* `PravegaBinderConfiguration` - Spring Boot configuration class that injects the properties and create the main binder classes.
* `PravegaMessageChannelBinder` - Main Binder implementation class that customizes the base `AbstractMessageChannelBinder` base implementation.
from Spring Cloud Stream. The Channel Binder binds the Producer and Consumer to the Pravega custom implementation.
* `PravegaMessageHandler` and `PravegaMessageProducer` - Binder implementation based in the Getting Started sample code.  

## TODO list

- Move the `StreamManager` code to the related Channel Provisioners
- Create a Pravega autoconfiguration class for defaults customization and creation of the factory classes for injection
- Understand and map the concurrency model of the Pravega Client to the Consumer and Producer implementations
- Map additional Binder configuration properties to Pravega, such as reader groups and partitioning (https://cloud.spring.io/spring-cloud-static/spring-cloud-stream/current/reference/html/spring-cloud-stream.html#_configuration_options)
- Create sample Spring Cloud Dataflow example
- Add unit tests
