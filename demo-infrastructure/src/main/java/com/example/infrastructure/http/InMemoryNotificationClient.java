package com.example.infrastructure.http;

import com.example.domain.shared.external.NotificationService;
import com.example.domain.shared.model.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "demo.notification.provider", havingValue = "inmemory", matchIfMissing = true)
public class InMemoryNotificationClient implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryNotificationClient.class);

    private final List<NotificationMessage> sent = new ArrayList<>();

    @Override
    public String send(NotificationMessage message) {
        sent.add(message);
        String messageId = "mem_" + sent.size();
        log.info("内存通知: messageId={}, recipient={}", messageId, message.recipient());
        return messageId;
    }

    public List<NotificationMessage> getSentMessages() {
        return List.copyOf(sent);
    }
}
