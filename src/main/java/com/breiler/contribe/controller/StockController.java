package com.breiler.contribe.controller;

import com.breiler.contribe.contract.CreateStockDTO;
import com.breiler.contribe.contract.StockDTO;
import com.breiler.contribe.model.Stock;
import com.breiler.contribe.service.StockService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@Api(basePath = "/api", tags = "Stock", description = "Resources for handling the inventory stock", produces = "application/json")
@RestController()
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Log4j
public class StockController {

    private final StockService stockService;
    private final ModelMapper modelMapper;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
        this.modelMapper = new ModelMapper();
    }

    @RequestMapping(value = "/api/stock", method = RequestMethod.GET)
    @ApiOperation(value = "Fetches the inventory stock for all books",
            notes = "Fetches the inventory stock for all books in the store.", responseContainer = "List", response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The stock was retreived successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<List<StockDTO>> fetchAll() {
        List<Stock> stocks = stockService.findAll();

        Type listType = new TypeToken<List<StockDTO>>() {}.getType();
        List<StockDTO> results = modelMapper.map(stocks, listType);

        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    @RequestMapping(value = "/api/stock/{bookId}", method = RequestMethod.PUT)
    @ApiOperation(value = "Updates the inventory stock for a book",
            notes = "Updates the inventory stock for a book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The inventory stock was updated successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<StockDTO> create(
            @ApiParam(name = "bookId", value = "The unique bookId of the book to update the stock")
            @PathVariable(name = "bookId")
                    Long bookId,
            @ApiParam(value = "The stock to be created")
            @RequestBody
                    CreateStockDTO stockDTO
    ) {
        Stock stock = stockService.setStock(bookId, stockDTO.getQuantity());
        StockDTO result = modelMapper.map(stock, StockDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
