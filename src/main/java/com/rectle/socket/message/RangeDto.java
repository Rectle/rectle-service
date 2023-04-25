package com.rectle.socket.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RangeDto {
	private String room;
	private Integer from;
	private Integer to;
}
