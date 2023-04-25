package com.rectle.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.rectle.compilation.CompilationService;
import com.rectle.socket.message.MessageDto;
import com.rectle.socket.message.RangeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SocketModule {


	private final SocketIOServer server;
	private final SocketService socketService;
	private final CompilationService compilationService;
	private final Map<String, RangeDto> logsRange = new HashMap<>();

	public SocketModule(SocketIOServer server, SocketService socketService, CompilationService compilationService) {
		this.server = server;
		this.socketService = socketService;
		this.compilationService = compilationService;
		server.addConnectListener(onConnected());
		server.addDisconnectListener(onDisconnected());
		server.addEventListener("send_message", MessageDto.class, onChatReceived());
		server.addEventListener("change_range", RangeDto.class, changeRange());
	}

	private DataListener<RangeDto> changeRange() {
		return (senderClient, data, ackSender) -> {
			if (!socketService.isRangeValid(data.getFrom(), data.getTo())) {
				return;
			}
			if (logsRange.containsKey(data.getRoom())) {
				logsRange.replace(data.getRoom(), data);
			} else {
				logsRange.put(data.getRoom(), data);
			}
		};
	}

	private List<String> getSubList(List<String> allLogs, String room) {
		if (allLogs.size() < 50) {
			return allLogs;
		}
		if (!logsRange.containsKey(room)) {
			return allLogs.subList(0, 50);
		}
		Integer to = logsRange.get(room).getTo();
		Integer from = logsRange.get(room).getFrom();
		return (to > allLogs.size()) ? allLogs.subList(allLogs.size() - 50, allLogs.size()) : allLogs.subList(from, to);
	}

	private DataListener<MessageDto> onChatReceived() {
		return (senderClient, data, ackSender) -> {
			compilationService.addNewLogs(data.getMessage(), data.getRoom());
			List<String> logs = compilationService.getLogsTextByCompilationId(data.getRoom());
			MessageDto messageDto = MessageDto.builder()
					.message(getSubList(logs, data.getRoom()))
					.numberOfAllLogs(logs.size())
					.type(MessageDto.MessageType.SERVER)
					.build();
			socketService.sendMessage(data.getRoom(), "get_message", senderClient, messageDto);
		};
	}

	private ConnectListener onConnected() {
		return (client) -> {
			String room = client.getHandshakeData().getSingleUrlParam(SocketConfiguration.COMPILATION_ID);
			client.joinRoom(room);
			List<String> logs = compilationService.getLogsTextByCompilationId(room);
			MessageDto messageDto = MessageDto.builder()
					.message(getSubList(logs, room))
					.numberOfAllLogs(logs.size())
					.type(MessageDto.MessageType.SERVER)
					.build();
			socketService.sendMessageToSpecificClient("get_message", client, messageDto);
		};
	}

	private DisconnectListener onDisconnected() {
		return client -> log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
	}
}
