package com.h2o_execution.misc;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class QuestradeAuthorizationManager implements OAuth2Flow
{
    @Value("${refreshToken}")
    private String refreshToken;
    @Getter
    @Value("${accessToken}")
    private String accessToken;
    private final ScheduledExecutorService scheduledExecutorService;

    public QuestradeAuthorizationManager()
    {
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public String getRefreshLink()
    {
        return String.format("https://login.questrade.com/grant_type=refresh_token&refresh_token=%s", refreshToken);
    }

    @Override
    public void refreshAccessToken() throws Exception
    {
        final JsonNode node = getNode();
        final String accessToken = node.get("access_token").asText();
        final int expiresIn = node.get("expires_in").asInt();
        this.updateAccess(accessToken);
        scheduledExecutorService.schedule(() -> refreshToken, expiresIn, TimeUnit.SECONDS);

    }

    private JsonNode getNode() throws IOException
    {
        final HttpUriRequest request = RequestBuilder
                .get(getRefreshLink())
                .addHeader("Accept", "application/json")
                .addParameter("refreshToken", refreshToken)
                .build();
        final HttpResponse response = HttpClientBuilder.create().build().execute(request);
        final String jsonString = EntityUtils.toString(response.getEntity());
        return new ObjectMapper().readTree(jsonString);
    }

    @Override
    public void updateAccess(final String accessToken) throws ConfigurationException
    {
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        final PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(classLoader.getResource("application.properties").getFile());
        propertiesConfiguration.setProperty("accessToken", accessToken);
        propertiesConfiguration.save();
        this.accessToken = accessToken;
    }
}
