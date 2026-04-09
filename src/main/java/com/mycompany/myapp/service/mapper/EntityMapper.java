package com.mycompany.myapp.service.mapper;

import jakarta.persistence.Entity;

import java.util.List;

public interface EntityMapper <D,E>{
    D toDto(E entity);
    E toEntity(D dto);
    List<D> toDto(List<E> entities);
    List<E> toEntity(List<D> dtos);
}
