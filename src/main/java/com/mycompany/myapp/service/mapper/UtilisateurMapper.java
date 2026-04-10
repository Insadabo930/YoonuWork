package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Utilisateur;
import com.mycompany.myapp.service.dto.UtilisateurCreationDTO;
import com.mycompany.myapp.service.dto.UtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    UtilisateurDTO toDto(Utilisateur entity);

    List<UtilisateurDTO> toDto(List<Utilisateur> entities);

    @Mapping(target = "motDePasse", ignore = true)
    @Mapping(target = "confirmeMotDePasse", ignore = true)
    @Mapping(target = "utilisateurRole", source = "role")
    UtilisateurCreationDTO toCreationDto(Utilisateur entity);

    @Mapping(target = "role", source = "utilisateurRole")
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "cours", ignore = true)
    Utilisateur toEntity(UtilisateurCreationDTO dto);

    List<Utilisateur> toEntity(List<UtilisateurCreationDTO> dtos);
}
