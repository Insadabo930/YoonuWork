package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cour;
import com.mycompany.myapp.domain.Utilisateur;
import com.mycompany.myapp.service.dto.CourCreationDTO;
import com.mycompany.myapp.service.dto.CourDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourMapper extends EntityMapper<CourDTO, Cour> {

    @Mapping(source = "formateur.id", target = "formateurId")
    CourDTO toDto(Cour cour);


    @Mapping(target = "formateur", ignore = true)
    Cour toEntity(CourCreationDTO courCreationDTO);

}
