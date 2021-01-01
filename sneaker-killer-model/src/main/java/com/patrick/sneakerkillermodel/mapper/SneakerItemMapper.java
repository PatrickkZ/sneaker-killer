package com.patrick.sneakerkillermodel.mapper;

import com.patrick.sneakerkillermodel.entity.SneakerItem;
import org.springframework.stereotype.Repository;

@Repository
public interface SneakerItemMapper {
    SneakerItem getById(Integer id);
    String getNameById(Integer id);
}
