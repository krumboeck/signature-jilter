/*
 * Copyright (c) 2001-2004 Sendmail, Inc. All Rights Reserved
 */

package com.sendmail.jilter.internal;

import java.io.IOException;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */

public class JilterPacket
{
	private static final Logger LOG = LoggerFactory.getLogger(JilterPacket.class);

    private static final int STATE_COLLECTING_LENGTH = 0;
    private static final int STATE_COLLECTING_COMMAND = 1;
    private static final int STATE_COLLECTING_DATA = 2;
    private static final int STATE_COMPLETED = 3;
        
    private int currentState = STATE_COLLECTING_LENGTH;
    private int currentLength = 0;
    private int currentLengthLength = 0;
    private int currentCommand = 0;
    private ByteBuffer currentData = null;
    private int currentDataLength = 0;

    private static int unsignedByteToInt(byte b)
    {
        return (((int) b) & 0x0FF);
    }

    public boolean
    process(
        ByteBuffer dataBuffer
        )
        throws IOException
    {
        int bytesToUse = 0;

        do
        {
            switch (this.currentState)
            {
                case STATE_COLLECTING_LENGTH:
                    LOG.debug("STATE_COLLECTING_LENGTH");
                    bytesToUse = Math.min(4 - this.currentLengthLength, dataBuffer.remaining());

                    for (int counter = 0;counter < bytesToUse;++counter)
                    {
                        this.currentLength <<= 8;
                        this.currentLength += unsignedByteToInt(dataBuffer.get());
                        ++this.currentLengthLength;
                    }

                    if (this.currentLengthLength == 4)
                    {
                        currentState = STATE_COLLECTING_COMMAND;
                        --this.currentLength;   // Minus one for the command byte
                        LOG.debug("Collected length is " + this.currentLength);
                        this.currentData = ByteBuffer.allocate(this.currentLength);
                    }

                    break;

                case STATE_COLLECTING_COMMAND:
                    LOG.debug("STATE_COLLECTING_COMMAND");

                    this.currentCommand = unsignedByteToInt(dataBuffer.get());
                    LOG.debug("Collected command is '" + ((char) this.currentCommand) + "'");

                    this.currentState = (this.currentLength == 0) ? STATE_COMPLETED : STATE_COLLECTING_DATA;
                    LOG.debug("New state is " + this.currentState);
                    break;

                case STATE_COLLECTING_DATA:
                    LOG.debug("STATE_COLLECTING_DATA");
                    bytesToUse = Math.min(this.currentLength - this.currentDataLength, dataBuffer.remaining());

                    this.currentData.put((ByteBuffer) dataBuffer.asReadOnlyBuffer().limit(dataBuffer.position() + bytesToUse));
                    dataBuffer.position(dataBuffer.position() + bytesToUse);

                    this.currentDataLength += bytesToUse;
                    LOG.debug("Found " + bytesToUse + " bytes to apply to data");

                    if (this.currentDataLength == this.currentLength)
                    {
                        LOG.debug("Collected all the data");

                        this.currentData.flip();
                        this.currentState = STATE_COMPLETED;
                    }

                    break;

                case STATE_COMPLETED:
                    LOG.debug("STATE_COMPLETED");
                    break;

                default:
                    LOG.error("Unhandled case", new Exception());
                    break;
            }
        }
        while ((dataBuffer.remaining() > 0) && (this.currentState != STATE_COMPLETED));

        return this.currentState == STATE_COMPLETED;
    }

    public int getCommand()
    {
        return this.currentCommand;
    }

    public ByteBuffer getData()
    {
        return this.currentData;
    }

    public void reset()
    {
        this.currentState = STATE_COLLECTING_LENGTH;
        this.currentLength = 0;
        this.currentLengthLength = 0;
        this.currentDataLength = 0;
        this.currentData = null;
    }
}
