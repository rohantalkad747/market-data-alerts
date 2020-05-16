package com.h2o_execution.alerts;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmailDestination implements IDestination
{
    private final IEmailSender emailSender;
    private final String email;

    @Override
    public void send(IoI ioi)
    {
        emailSender.sendMail(ioi.toString(), "H2O Execution Services Alert", email);
    }
}
