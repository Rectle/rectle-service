package com.rectle.file;

import com.rectle.model.dto.ModelToCompileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "fileFeignClient", url = "${rectle.tasks.service.url}")
public interface FilesFeignClient {

	@PostMapping
	void postForCompileModel(ModelToCompileDto modelToCompileDto);
}
