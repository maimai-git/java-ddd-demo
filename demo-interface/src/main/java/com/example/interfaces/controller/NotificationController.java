package com.example.interfaces.controller;

import com.example.application.common.Response;
import com.example.application.dto.command.SendNotificationCmd;
import com.example.application.service.NotificationAppService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationAppService notificationAppService;

    public NotificationController(NotificationAppService notificationAppService) {
        this.notificationAppService = notificationAppService;
    }

    @PostMapping("/send")
    public Response send(@RequestBody @Valid SendNotificationCmd cmd) {
        return notificationAppService.sendNotification(cmd);
    }
}
