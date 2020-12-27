package com.patrick.sneakerkillerweb.controller;

import com.patrick.sneakerkillermodel.entity.SecondKillItem;
import com.patrick.sneakerkillerservice.service.ItemService;
import com.patrick.sneakerkillerweb.result.Result;
import com.patrick.sneakerkillerweb.result.ResultFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/item")
public class ItemController {
    ItemService itemService;

    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping("/all")
    public Result listAll(){
        List<SecondKillItem> items = itemService.getAllSecondKillItem();
        return ResultFactory.buildSuccessResult(items);
    }
}
