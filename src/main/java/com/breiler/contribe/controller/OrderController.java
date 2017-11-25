package com.breiler.contribe.controller;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(basePath = "/api", value = "Orders", description = "For handling orders", produces = "application/json")
@RestController()
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Log4j
public class OrderController {

    private final ModelMapper modelMapper;

    @Autowired
    public OrderController() {
        this.modelMapper = new ModelMapper();
    }
}
