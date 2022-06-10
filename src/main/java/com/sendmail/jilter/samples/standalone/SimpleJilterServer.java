/*
 * Copyright (c) 2001-2004 Sendmail, Inc. All Rights Reserved
 */

package com.sendmail.jilter.samples.standalone;

import com.sendmail.jilter.JilterHandler;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Jilter server for handling connections from an MTA.
 */

public class SimpleJilterServer implements Runnable
{
	private static final Logger LOG = LoggerFactory.getLogger(SimpleJilterServer.class);

    private ServerSocketChannel serverSocketChannel = null;
    private Class<?> handlerClass = null;

    private JilterHandler newHandler()
        throws InstantiationException, IllegalAccessException
    {
        return (JilterHandler) this.handlerClass.newInstance();
    }

    public void run()
    {
        while (true)
        {
            SocketChannel connection = null;

            try
            {
                LOG.debug("Going to accept");
                connection = this.serverSocketChannel.accept();
                LOG.debug("Got a connection from " + connection.socket().getInetAddress().getHostAddress());

                LOG.debug("Firing up new thread");
                new Thread(
                    new ServerRunnable(
                        connection,
                        newHandler()
                        ),
                    "Jilter " + connection.socket().getInetAddress().getHostAddress()
                    ).start();
                LOG.debug("Thread started");
            }
            catch (IOException e)
            {
                LOG.debug("Unexpected exception", e);
            }
            catch (InstantiationException e)
            {
                LOG.debug("Unexpected exception", e);
            }
            catch (IllegalAccessException e)
            {
                LOG.debug("Unexpected exception", e);
            }
        }
    }

    public SocketAddress getSocketAddress()
    {
        return this.serverSocketChannel.socket().getLocalSocketAddress();
    }

    public SimpleJilterServer(SocketAddress endpoint, String handlerClassName)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        this.handlerClass = Class.forName(handlerClassName);

        // Fire up a test handler and immediately close it to make sure everything's OK.

        newHandler().close();

        LOG.debug("Opening socket");
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.configureBlocking(true);
        LOG.debug("Binding to endpoint " + endpoint);
        this.serverSocketChannel.socket().bind(endpoint);
        LOG.debug("Bound to " + getSocketAddress());
    }

    private static class SimpleGetopt
    {
        private String optstring = null;
        private String[] args = null;
        private int argindex = 0;
        private String optarg = null;

        public SimpleGetopt(String[] args, String optstring)
        {
            this.args = args;
            this.optstring = optstring;
        }

        public int nextopt()
        {
            int argChar = -1;

            for (int counter = this.argindex;counter < this.args.length;++counter)
            {
                if ((args[counter] != null) && (args[counter].length() > 1) && (args[counter].charAt(0) == '-'))
                {
                    int charIndex = 0;

                    LOG.debug("Found apparent argument " + args[counter]);

                    argChar = args[counter].charAt(1);
                    charIndex = this.optstring.indexOf(argChar);
                    this.optarg = null; 
                    if (charIndex != -1)
                    {
                        this.argindex = counter + 1;

                        if ((this.optstring.length() > (charIndex + 1)) &&
                            (this.optstring.charAt(charIndex + 1) == ':'))
                        {
                            LOG.debug("Argument apparently requires a parameter");
                            if (args[counter].length() > 2)
                            {
                                this.optarg = args[counter].substring(2).trim();
                            }
                            else if (args.length > (counter + 1))
                            {
                                this.optarg = args[counter + 1];
                                ++this.argindex;
                            }
                            LOG.debug("Parameter is " + this.optarg);
                        }
                    }
                    break;
                }
            }

            return argChar;
        }

        public String getOptarg()
        {
            return this.optarg;
        }
    }

    private static SocketAddress parseSocketAddress(String address)
        throws UnknownHostException
    {
        Pattern pattern = Pattern.compile("inet\\s*:\\s*(\\d+)\\s*@\\s*(\\S+)");
        Matcher matcher = pattern.matcher(address);

        if (!matcher.matches())
        {
            System.out.println("Unrecognized port \"" + address + "\"");
            return null;
        }

        LOG.debug("Successfully parsed socket address, port is " + matcher.group(1) + ", host is " + matcher.group(2));
        return new InetSocketAddress(InetAddress.getByName(matcher.group(2)), Integer.parseInt(matcher.group(1)));
    }

    private static void usage()
    {
        System.out.println("Usage: -p <port information> -c <handler class> [-v]");
        System.out.println();
        System.out.println("       -p <port information> -- the port to listen on");
        System.out.println("       -c <handler class> -- a class implementing the JilterHandler interface");
        System.out.println("       -v -- turn on verbosity");
        System.out.println();
        System.out.println("       <port information> is of the format \"inet:port@host\"");
    }

    public static void main(String[] args)
        throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        SimpleGetopt options = new SimpleGetopt(args, "p:c:");
        String handlerClass = null;
        SocketAddress socketAddress = null;

        while (true)
        {
            int option = options.nextopt();

            if (option == -1)
            {
                break;
            }

            switch (option)
            {
                case 'c':
                    handlerClass = options.getOptarg();
                    LOG.debug("Handler class specified is " + handlerClass);
                    break;

                case 'p':
                    LOG.debug("Socket address specified is " + options.getOptarg());
                    socketAddress = parseSocketAddress(options.getOptarg());
                    break;
            }
        }

        if ((socketAddress == null) || (handlerClass == null))
        {
            usage();
            System.exit(1);
        }

        new SimpleJilterServer(socketAddress, handlerClass).run();
    }
}
