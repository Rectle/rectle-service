package com.rectle.socket.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageDto {
	private MessageType type;
	private List<String> message;
	private String room;

	public MessageDto(MessageType type, List<String> message) {
		this.message = message;
		this.type = type;
	}

	public enum MessageType {
		SERVER, CLIENT
	}
}
