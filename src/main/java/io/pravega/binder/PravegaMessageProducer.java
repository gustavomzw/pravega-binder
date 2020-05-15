package io.pravega.binder;

import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.JavaSerializer;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class PravegaMessageProducer extends MessageProducerSupport  {

    private static final int READER_TIMEOUT_MS = 2000;

    private final AtomicBoolean stop = new AtomicBoolean();
    private final URI controllerURI;
    private final String scope;
    private final String streamName;
    private String readerGroup;
    private ReaderGroupConfig readerGroupConfig;
    private ClientConfig clientConfig;
    private ReaderConfig readerConfig;

    public PravegaMessageProducer(String controllerURI, String scope, String streamName) {
        this.controllerURI = URI.create(controllerURI);
        this.scope = scope;
        this.streamName = streamName;
    }

    @Override
    protected void onInit() {
        super.onInit();
        final StreamManager streamManager = StreamManager.create(controllerURI);
        streamManager.createScope(scope);
        StreamConfiguration streamConfig = StreamConfiguration.builder().scalingPolicy(ScalingPolicy.fixed(1)).build();
        streamManager.createStream(scope, streamName, streamConfig);
        readerGroup = UUID.randomUUID().toString().replace("-", "");
        readerGroupConfig = ReaderGroupConfig.builder().stream(Stream.of(scope, streamName)).build();
        clientConfig = ClientConfig.builder().controllerURI(controllerURI).build();
        readerConfig = ReaderConfig.builder().build();
    }

    @Override
    protected void doStart() {
        try (ReaderGroupManager readerGroupManager = ReaderGroupManager.withScope(scope, controllerURI)) {
            readerGroupManager.createReaderGroup(readerGroup, readerGroupConfig);
        }
        try (EventStreamClientFactory clientFactory = EventStreamClientFactory.withScope(scope, clientConfig);
             EventStreamReader<String> reader = clientFactory.createReader("reader", readerGroup,
                     new JavaSerializer<String>(), readerConfig)) {
            System.out.format("Reading all the events from %s/%s%n", scope, streamName);
            EventRead<String> event = null;
            while (!stop.get()) {
                try {
                    event = reader.readNextEvent(READER_TIMEOUT_MS);
                    if (event.getEvent() != null) {
                        System.out.format("Read event '%s'%n", event.getEvent());
                        Message<?> msg = MessageBuilder.withPayload(event.getEvent()).build();
                        sendMessage(msg);
                    }
                } catch (ReinitializationRequiredException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doStop() {
        stop.set(true);
    }
}
