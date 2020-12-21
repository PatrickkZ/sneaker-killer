package com.patrick.sneakerkillerservice.service;

import com.patrick.sneakerkillermodel.entity.SecondKillItem;
import com.patrick.sneakerkillermodel.mapper.SecondKillItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    SecondKillItemMapper itemMapper;

    public ItemService(SecondKillItemMapper itemMapper){
        this.itemMapper = itemMapper;
    }

    public List<SecondKillItem> getAllSecondKillItem(){
        return itemMapper.listAllItemCanSecondSkill();
    }
}
