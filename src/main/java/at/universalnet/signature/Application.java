package at.universalnet.signature;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.sendmail.jilter.samples.standalone.ServerRunnable;

import at.universalnet.ad.api.main.entry.LdapBenutzer;
import at.universalnet.ad.api.main.repo.LdapBenutzerRepository;

@SpringBootApplication
@ComponentScan({
	"at.universalnet.signature",
	"at.universalnet.ad.spring.boot.main"
	})
public class Application implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	@Value("${server.address:0.0.0.0}")
	private String serverAddress;

	@Value("${server.port:8025}")
	private int serverPort;

	@Value("${server.threads.max:10}")
	private int maxServerThreads;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private LdapBenutzerRepository ldapBenutzerRepository;

	private ServerSocketChannel serverSocketChannel = null;
	private ExecutorService executor = null;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Override
    public void run(String...args) throws Exception {

    	SocketAddress endpoint = new InetSocketAddress(serverAddress, serverPort);

    	LOG.debug("Opening socket");
    	serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(true);
		LOG.info("Binding to endpoint " + endpoint);
		serverSocketChannel.socket().bind(endpoint);
		LOG.info("Bound to " + serverSocketChannel.socket().getLocalSocketAddress());

		executor = Executors.newFixedThreadPool(maxServerThreads);
    	while (true) {
			SocketChannel connection = null;

			try {
				LOG.debug("Waiting for a connection...");
				connection = serverSocketChannel.accept();
				LOG.debug("Got a connection from " + connection.socket().getInetAddress().getHostAddress());

				LOG.debug("Firing up new thread");
				MilterHandler milterHandler = applicationContext.getBean(MilterHandler.class);
				Runnable task = new ServerRunnable(connection, milterHandler);
				executor.submit(task);
			} catch (RejectedExecutionException e) {
				LOG.warn("Maximum number of connections reached. Please increase server.threads.max if you need more.");
			} catch (IOException e) {
				LOG.debug("Unexpected exception", e);
			}

		}
    }

}
