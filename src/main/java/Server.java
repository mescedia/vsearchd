import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.client.ClientMessageQueueHandler;
import org.vsearchd.crawler.client.ClientQueueHandler;
import org.vsearchd.crawler.helper.ServerConfig;


public class Server {

	public static void main(String[] args) throws Exception {
		Logger log = LoggerFactory.getLogger(Server.class);
		XMLConfiguration configFile = null;
		
		try {
			configFile = new XMLConfiguration("config.xml");
		} catch (ConfigurationException e) {
			log.warn("config.xml not found - quit ...");
			log.error(e.getMessage());
			return;
		}

		ServerConfig config = ServerConfig.getInstance();

		config.setTcpPort(Integer.valueOf(configFile.getString("listen-port")));
		config.setNumHttpClient(Integer.valueOf(configFile
				.getString("number-http-clients")));
		config.setMaxRequestsPerSecond(Integer.valueOf(configFile
				.getString("max-requests-per-second")));
		config.setIPV4Address(configFile.getString("listen-address"));
		config.setMaxClient(Integer.valueOf(configFile
				.getString("number-max-clients")));

		IoAcceptor acceptor = new NioSocketAcceptor();
		TextLineCodecFactory tldf = new TextLineCodecFactory(
				Charset.forName("UTF-8"));
		tldf.setDecoderMaxLineLength(99999999);
		tldf.setEncoderMaxLineLength(99999999);
		ProtocolCodecFilter pcf = new ProtocolCodecFilter(tldf);

		acceptor.getFilterChain().addLast("codec", pcf);
		acceptor.setHandler(new ServerHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setMinReadBufferSize(30);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

		log.info("vertical search daemon [" + config.getIPV4Address() + ":"
				+ config.getTcpPort() + "] started ...");

		try {
			acceptor.bind(new InetSocketAddress(config.getIPV4Address(), config
					.getTcpPort()));

			Thread clientQueueManager = new Thread(new ClientQueueHandler());
			clientQueueManager.setName("clthad");
			clientQueueManager.start();

			int numMsgThreads = Integer.valueOf(configFile
					.getString("number-message-threads"));

			Thread[] clientMessageThread = new Thread[numMsgThreads];

			for (int i = 0; i < numMsgThreads; i++) {
				clientMessageThread[i] = new Thread(
						new ClientMessageQueueHandler());
				clientMessageThread[i].setName("cmthd" + String.valueOf(i));
				clientMessageThread[i].start();
			}

			log.info("initialized " + Integer.toString(numMsgThreads)
					+ " client-message-thread(s) ...");

		} catch (IOException e) {
			log.error("cannot start server ...");
			log.error(e.getMessage());
		}
	}
}