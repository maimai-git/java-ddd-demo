package com.example.infrastructure.http;

import com.example.domain.shared.external.NotificationService;
import com.example.domain.shared.model.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "demo.notification.provider", havingValue = "wechat")
public class WechatTemplateMsgClient implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(WechatTemplateMsgClient.class);

    @Override
    public String send(NotificationMessage message) {
        log.info("微信 API 调用: templateId={}, recipient={}, params={}",
                message.templateId(), message.recipient(), message.params());
        // 实际代码：
        // 1. wechatApi.getAccessToken(appId, secret)
        // 2. wechatApi.sendTemplateMessage(accessToken, message)
        // 3. return messageId;
        String messageId = "wx_" + System.currentTimeMillis();
        log.info("微信发送成功: messageId={}", messageId);
        return messageId;
    }
}
