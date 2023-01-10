package com.rectle.file;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "fileFeignClient", url = "${rectle.tasks.service.url}")
public interface FilesFeignClient {

	@RequestMapping(method = RequestMethod.POST, value = "/")
	void postForCompileFile(@RequestBody String task);
}
