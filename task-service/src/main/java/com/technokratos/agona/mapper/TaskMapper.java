package com.technokratos.agona.mapper;

import com.technokratos.agona.api.dto.TaskRequestDto;
import com.technokratos.agona.api.dto.TaskResponseDto;
import com.technokratos.agona.api.dto.TaskUpdateDto;
import com.technokratos.agona.model.Task;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponseDto toDto(Task task);

    List<TaskResponseDto> toDto(List<Task> tasks);

    Task toEntity(TaskRequestDto taskRequestDto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(TaskUpdateDto taskUpdateDto, @MappingTarget Task task);

}
