package com.rectle.project;

import com.rectle.project.dto.UploadedProjectDto;
import com.rectle.project.model.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectDtoMapper {

	UploadedProjectDto projectToUploadedProjectDto(Project project);
}
