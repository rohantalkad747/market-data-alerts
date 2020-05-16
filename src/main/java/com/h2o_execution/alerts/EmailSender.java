package com.h2o_execution.alerts;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailSender implements IEmailSender
{
    private static final SendGrid SEND_GRID = new SendGrid("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");
    private static final Email SLOPOKE_EMAIL = new Email("alerts@h2o_execution.com");

    @Override
    public void sendMail(final String to, final String subject, final String message)
    {
        final Email emailTo = new Email(to);
        final Content content = new Content("text/html", message);
        final Mail mail = new Mail(SLOPOKE_EMAIL, subject, emailTo, content);
        final Request request = new Request();
        try
        {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            SEND_GRID.api(request);
        }
        catch (final IOException e)
        {
            throw new RuntimeException("Email failed to send!", e);
        }
    }
}
