package com.rectle.socket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketConfiguration {
	@Value("${socket.server.host}")
	private String host;

	@Value("${socket.server.port}")
	private Integer port;

	public static String COMPILATION_ID = "compilationId";

	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
		configuration.setHostname(host);
		configuration.setPort(port);
		return new SocketIOServer(configuration);
	}
}
