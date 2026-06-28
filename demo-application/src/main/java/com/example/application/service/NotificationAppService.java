package com.example.application.service;

import com.example.application.common.Response;
import com.example.application.dto.command.SendNotificationCmd;
import com.example.common.log.BizLog;
import com.example.domain.shared.external.NotificationService;
import com.example.domain.shared.model.NotificationMessage;
import org.springframework.stereotype.Service;

@Service
public class NotificationAppService {

    private final NotificationService notificationService;

    public NotificationAppService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @BizLog("发送通知")
    public Response sendNotification(SendNotificationCmd cmd) {
        var message = new NotificationMessage(cmd.getTemplateId(), cmd.getRecipient(), cmd.getParams());
        notificationService.send(message);
        return Response.buildSuccess();
    }
}
