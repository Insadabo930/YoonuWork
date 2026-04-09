package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Utilisateur;
import com.mycompany.myapp.service.dto.UtilisateurCreationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurCreationDTO, Utilisateur> {

}
