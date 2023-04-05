package com.rectle.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.rectle.compilation.CompilationService;
import com.rectle.socket.message.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SocketModule {


	private final SocketIOServer server;
	private final SocketService socketService;
	private final CompilationService compilationService;

	public SocketModule(SocketIOServer server, SocketService socketService, CompilationService compilationService) {
		this.server = server;
		this.socketService = socketService;
		this.compilationService = compilationService;
		server.addConnectListener(onConnected());
		server.addDisconnectListener(onDisconnected());
		server.addEventListener("send_message", MessageDto.class, onChatReceived());

	}

	private DataListener<MessageDto> onChatReceived() {
		return (senderClient, data, ackSender) -> {
			compilationService.addNewLogs(data.getMessage(), data.getRoom());
			List<String> logs = compilationService.getLogsTextByCompilationId(data.getRoom());
			socketService.sendMessage(data.getRoom(),"get_message", senderClient, logs);
		};
	}

	private ConnectListener onConnected() {
		return (client) -> {
			String room = client.getHandshakeData().getSingleUrlParam(SocketConfiguration.COMPILATION_ID);
			client.joinRoom(room);
			List<String> logs = compilationService.getLogsTextByCompilationId(room);
			socketService.sendMessageToSpecificClient("get_message", client, logs);
			log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
		};
	}

	private DisconnectListener onDisconnected() {
		return client -> log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
	}
}
