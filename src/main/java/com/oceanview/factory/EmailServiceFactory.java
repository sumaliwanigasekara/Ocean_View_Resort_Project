package com.oceanview.factory;

import com.oceanview.service.email.EmailService;
import com.oceanview.service.email.impl.NoopEmailService;
import com.oceanview.service.email.impl.SmtpEmailService;
import com.oceanview.util.MailConfig;

public final class EmailServiceFactory {
    private EmailServiceFactory() {
    }

    public static EmailService createDefault() {
        MailConfig config = MailConfig.getInstance();
        if (!config.isEnabled()) {
            return new NoopEmailService();
        }

        String provider = config.getProvider();
        if ("smtp".equalsIgnoreCase(provider)) {
            return new SmtpEmailService(config);
        }

        return new NoopEmailService();
    }
}
