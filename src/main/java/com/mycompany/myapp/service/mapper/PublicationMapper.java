package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.Publication;
import com.mycompany.myapp.service.dto.PublicationCreationDTO;
import com.mycompany.myapp.service.dto.PublicationDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PublicationMapper {

    PublicationDTO toDto(Publication publication);

   @Mapping(target = "employeur",ignore = true)
    Publication toEntity(PublicationCreationDTO publicationDTO);


    // Tu écris JUSTE ça dans PublicationMapper.java :

    // PUT — MapStruct va copier TOUS les champs de dto vers entity
    void updateEntityFromDto(PublicationDTO dto, @MappingTarget Publication entity);

    // PATCH — MapStruct va copier UNIQUEMENT les champs non-null
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateEntityFromDto(PublicationDTO dto, @MappingTarget Publication entity);
}
