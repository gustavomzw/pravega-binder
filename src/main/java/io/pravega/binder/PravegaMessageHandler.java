package io.pravega.binder;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.EventStreamWriter;
import io.pravega.client.stream.EventWriterConfig;
import io.pravega.client.stream.ScalingPolicy;
import io.pravega.client.stream.StreamConfiguration;
import io.pravega.client.stream.impl.JavaSerializer;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.messaging.Message;

import java.net.URI;

public class PravegaMessageHandler extends AbstractMessageHandler {

    private final String scope;
    private final String streamName;
    private final URI controllerURI;
    private final String routingKey;
    private ClientConfig clientConfig;
    private EventWriterConfig writerConfig;

    public PravegaMessageHandler(String controllerURI, String scope, String streamName, String routingKey) {
        this.controllerURI = URI.create(controllerURI);
        this.scope = scope;
        this.streamName = streamName;
        this.routingKey = routingKey;
    }

    @Override
    protected void onInit() {
        super.onInit();
        final StreamManager streamManager = StreamManager.create(controllerURI);
        streamManager.createScope(scope);
        StreamConfiguration streamConfig = StreamConfiguration.builder().scalingPolicy(ScalingPolicy.fixed(1)).build();
        streamManager.createStream(scope, streamName, streamConfig);
        clientConfig = ClientConfig.builder().controllerURI(controllerURI).build();
        writerConfig = EventWriterConfig.builder().build();
    }

    @Override
    protected void handleMessageInternal(Message<?> message) {
        try (EventStreamClientFactory clientFactory = EventStreamClientFactory.withScope(scope, clientConfig);
             EventStreamWriter<String> writer = clientFactory.createEventWriter(streamName,
                     new JavaSerializer<String>(), writerConfig)) {
            String msg = new String((byte[])message.getPayload());
            System.out.format("Writing message: '%s' with routing-key: '%s' to stream '%s / %s'%n", msg, routingKey, scope, streamName);
            writer.writeEvent(routingKey, msg);
        }
    }

}
