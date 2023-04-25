package com.rectle.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.rectle.socket.message.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SocketService {

	public void sendMessage(String room, String eventName, SocketIOClient senderClient, MessageDto messageDto) {
		for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
			if (!client.getSessionId().equals(senderClient.getSessionId())) {
				client.sendEvent(eventName, messageDto);
			}
		}
	}

	public void sendMessageToSpecificClient(String eventName, SocketIOClient client, MessageDto messageDto) {
		client.sendEvent(eventName, messageDto);
	}

	public boolean isRangeValid(Integer from, Integer to) {
		if (from == null || to == null) {
			return false;
		}
		return from > 0 && to > 0 && to - from <= 50 && from < to;
	}
}