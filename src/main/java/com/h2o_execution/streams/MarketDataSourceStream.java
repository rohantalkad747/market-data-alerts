package com.h2o_execution.streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.h2o_execution.misc.AbstractQTAccessAware;
import com.h2o_execution.misc.QTAccessKeyRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@Slf4j
public class MarketDataSourceStream extends AbstractQTAccessAware implements SourceFunction<String>, QTStreamSink
{
    private String accessToken;
    private static final String MD_URL = "https://api01.iq.questrade.com/v1/markets/quotes?ids=%s\"stream=true\"mode=RawSocket";
    private SocketConnection CONNECTION;
    private volatile boolean running;
    private List<String> symbolIds;

    private String getConnString()
    {
        return String.format(MD_URL, String.join(",", symbolIds));
    }

    @Override
    public void updateAccessKey(String accessKey)
    {
        this.cancel();
        this.accessToken = accessKey;
    }

    @Override
    public void updateSubscriptionList(final List<String> symbolIds)
    {
        this.symbolIds = symbolIds;
        this.cancel();
        this.initiateConnection();
    }

    public MarketDataSourceStream(QTAccessKeyRegistry accessKeyRegistry)
    {
        super(accessKeyRegistry);
    }

    private void initiateConnection()
    {
        this.running = true;
        String connStr = getConnString();
        final HttpUriRequest request = RequestBuilder
                .get(connStr)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/json")
                .build();
        try
        {
            final HttpResponse response = HttpClientBuilder.create().build().execute(request);
            final String jsonString = EntityUtils.toString(response.getEntity());
            int streamPort = new ObjectMapper().readTree(jsonString).get("streamPort").asInt();
            CONNECTION = new SocketConnection(connStr,streamPort);
        }
        catch (IOException e)
        {
            log.warn("Error while trying to connect to stream", e);
        }
    }

    @Override
    public void run(final SourceContext<String> context) throws Exception
    {
        try (final SocketConnection conn = CONNECTION)
        {
            handshake(conn);
            String line;
            while (this.running && (line = conn.getReader().readLine()) != null)
            {
                context.collect(line);
            }
        }
    }

    private void handshake(SocketConnection conn) throws IOException
    {
        PrintStream writer = conn.getWriter();
        writer.write(accessToken.getBytes());
        writer.write(' ');
    }

    @Override
    public void cancel()
    {
        this.running = false;
    }
}
