package com.patrick.sneakerkillermodel.mapper;

import com.patrick.sneakerkillermodel.entity.SecondKillItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecondKillItemMapper {
    List<SecondKillItem> listAllItemCanSecondSkill();
}
