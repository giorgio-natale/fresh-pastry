package it.gionatale.fp.commons;

public interface EventPublisher {
    void publish(String topic, Event event);
}
