package com.h2o_execution.misc;

import org.apache.commons.configuration.ConfigurationException;

public interface OAuth2Flow
{
    String getRefreshLink();

    void refreshAccessToken() throws Exception;

    void updateAccess(String accessToken) throws ConfigurationException;

    String getAccessToken();
}
