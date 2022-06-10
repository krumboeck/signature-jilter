/*
 * Copyright (c) 2001-2004 Sendmail, Inc. All Rights Reserved
 */

package com.sendmail.jilter.samples.standalone;

import com.sendmail.jilter.JilterHandler;
import com.sendmail.jilter.JilterProcessor;

import java.io.IOException;

import java.nio.ByteBuffer;

import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample implementation of a handler for a socket based Milter protocol connection.
 */

public class ServerRunnable implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(ServerRunnable.class);

    private SocketChannel socket = null;
    private JilterProcessor processor = null;

    /**
     * Constructor.
     * 
     * @param socket the incoming socket from the MTA.
     * @param handler the handler containing callbacks for the milter protocol.
     */
    public ServerRunnable(SocketChannel socket, JilterHandler handler)
        throws IOException
    {
        this.socket = socket;
        this.socket.configureBlocking(true);
        this.processor = new JilterProcessor(handler);
    }

    public void run()
    {
        ByteBuffer dataBuffer = ByteBuffer.allocateDirect(4096);

        try
        {
            while (this.processor.process(this.socket, (ByteBuffer) dataBuffer.flip()))
            {
                dataBuffer.compact();
                LOG.debug("Going to read");
                if (this.socket.read(dataBuffer) == -1)
                {
                    LOG.debug("socket reports EOF, exiting read loop");
                    break;
                }
                LOG.debug("Back from read");
            }
        }
        catch (IOException e)
        {
            LOG.debug("Unexpected exception, connection will be closed", e);
        }
        finally
        {
            LOG.debug("Closing processor");
            this.processor.close();
            LOG.debug("Processor closed");
            try
            {
                LOG.debug("Closing socket");
                this.socket.close();
                LOG.debug("Socket closed");
            }
            catch (IOException e)
            {
                LOG.debug("Unexpected exception", e);
            }
        }
    }
}
