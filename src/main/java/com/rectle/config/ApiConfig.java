package com.rectle.config;

import org.springframework.beans.factory.annotation.Value;

public class ApiConfig {

	@Value("${api.url}")
	public static String URL;

}
