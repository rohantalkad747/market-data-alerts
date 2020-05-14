package com.h2o_execution.alerts;

public interface IEmailSender
{
    void sendMail(final String to, final String subject, final String message);
}
