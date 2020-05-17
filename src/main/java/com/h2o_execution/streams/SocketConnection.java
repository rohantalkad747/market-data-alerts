package com.h2o_execution.streams;

import java.io.*;
import java.net.Socket;

public class SocketConnection implements Closeable
{
    private final String host;
    private final int port;
    private volatile Socket socket;
    private volatile BufferedReader reader;
    private volatile PrintStream writer;

    public SocketConnection(final String host, final int port)
    {
        this.host = host;
        this.port = port;
        this.socket = null;
        this.reader = null;
        this.writer = null;
    }

    private void connect() throws IOException
    {
        this.socket = new Socket(this.host, this.port);
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new PrintStream(this.socket.getOutputStream());
    }

    private synchronized void ensureConnected() throws IOException
    {
        if (this.socket == null)
        {
            connect();
        }
    }

    public BufferedReader getReader() throws IOException
    {
        ensureConnected();
        return this.reader;
    }

    public PrintStream getWriter() throws IOException
    {
        ensureConnected();
        return this.writer;
    }

    @Override
    public void close() throws IOException
    {
        if (this.socket != null)
        {
            this.reader.close();
            this.reader = null;
            this.writer.close();
            this.writer = null;
            this.socket.close();
            this.socket = null;
        }
    }
}