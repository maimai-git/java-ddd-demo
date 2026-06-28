package com.example.domain.shared.external;

import com.example.domain.shared.model.NotificationMessage;

public interface NotificationService {

    String send(NotificationMessage message);
}
