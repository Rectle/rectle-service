package com.rectle.score;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ScoreDto {
	private String score;
	private Timestamp scoreDate;
}
