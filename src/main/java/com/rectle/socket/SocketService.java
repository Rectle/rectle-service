package com.rectle.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.rectle.socket.message.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SocketService {

	public void sendMessage(String room, String eventName, SocketIOClient senderClient, List<String> messages) {
		for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
			if (!client.getSessionId().equals(senderClient.getSessionId())) {
				client.sendEvent(eventName, new MessageDto(MessageDto.MessageType.SERVER, messages));
			}
		}
	}

	public void sendMessageToSpecificClient(String eventName, SocketIOClient client, List<String> messages) {
		client.sendEvent(eventName, new MessageDto(MessageDto.MessageType.SERVER, messages));
	}
}