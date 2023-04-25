package com.rectle.socket.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MessageDto {
	private MessageType type;
	private List<String> message;
	private String room;
	private Integer numberOfAllLogs;

	public MessageDto(MessageType type, List<String> message) {
		this.message = message;
		this.type = type;
	}

	public MessageDto(MessageType type, List<String> subList, int size) {
		this.type = type;
		this.message = subList;
		this.numberOfAllLogs = size;
	}

	public enum MessageType {
		SERVER, CLIENT
	}
}
