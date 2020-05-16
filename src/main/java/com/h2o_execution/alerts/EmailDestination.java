package com.h2o_execution.alerts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class EmailDestination extends AbstractDestination
{
    private final IEmailSender emailSender;
    private final String email;

    @Override
    protected void sendToDestination(String formattedIoI)
    {
        emailSender.sendMail(formattedIoI, "H2O Execution Services Alert", email);
    }
}
