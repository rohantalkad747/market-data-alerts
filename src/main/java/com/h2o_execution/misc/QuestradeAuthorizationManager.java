package com.h2o_execution.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class QuestradeAuthorizationManager implements OAuth2Flow, AccessKeyRegistry
{
    @Value("${clientId}")
    private String clientId;
    @Value("${refreshToken}")
    private String refreshToken;
    @Getter
    @Value("${accessToken}")
    private String accessToken;

    @Override
    public String getRefreshLink()
    {
        return String.format("https://login.questrade.com/grant_type=refresh_token&refresh_token=%s", refreshToken);
    }

    @Override
    public void refreshAccessToken() throws Exception
    {
        final HttpUriRequest request = RequestBuilder
                .get(getRefreshLink())
                .addHeader("Accept", "application/json")
                .addParameter("refreshToken", refreshToken)
                .build();
        final HttpResponse response = HttpClientBuilder.create().build().execute(request);
        final String jsonString = EntityUtils.toString(response.getEntity());
        final String accessToken = new ObjectMapper().readTree(jsonString).get("access_token").asText();
        this.updateAccess(accessToken);
    }

    @Override
    public void updateAccess(String accessToken) throws ConfigurationException
    {
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        final PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(classLoader.getResource("application.properties").getFile());
        propertiesConfiguration.setProperty("accessToken", accessToken);
        propertiesConfiguration.save();
        this.accessToken = accessToken;
    }
}
