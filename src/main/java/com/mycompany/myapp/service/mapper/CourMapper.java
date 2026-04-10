package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cour;
import com.mycompany.myapp.service.dto.CourDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourMapper extends EntityMapper<CourDTO, Cour>{
}
