package com.breiler.contribe.exceptions;

import com.breiler.contribe.contract.ItemDTO;
import com.breiler.contribe.model.Item;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;
import java.util.List;

public class ItemsNotInStockException extends BaseException {
    private final List<Item> items;

    public ItemsNotInStockException(List<Item> items) {
        this.items = items;
    }

    public ResponseEntity getResponse() {
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<ItemDTO>>() {
        }.getType();
        List<ItemDTO> results = modelMapper.map(items, listType);
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(results);
    }
}
