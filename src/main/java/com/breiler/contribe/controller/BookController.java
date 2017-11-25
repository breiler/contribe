package com.breiler.contribe.controller;

import com.breiler.contribe.contract.BookDTO;
import com.breiler.contribe.model.Book;
import com.breiler.contribe.repository.BookList;
import com.breiler.contribe.service.BookService;
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

@Api(basePath = "/api", value = "Books", description = "For handling books", produces = "application/json")
@RestController()
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@Log4j
public class BookController {

    private final BookService bookService;
    private final ModelMapper modelMapper;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
        this.modelMapper = new ModelMapper();
    }

    @RequestMapping(value = "/api/books", method = RequestMethod.GET)
    @ApiOperation(value = "Fetches books",
            notes = "Fetches the books in the book store", responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The status was retreived successfully"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<List<BookDTO>> fetchBooks(
            @ApiParam(name = "query", value="A query string for searching books")
            @RequestParam(value = "query", required = false)
            String query
    ) {
        List<Book> books = bookService.findBooks(query);

        Type listType = new TypeToken<List<BookDTO>>() {}.getType();
        List<BookDTO> results = modelMapper.map(books, listType);

        return ResponseEntity.status(HttpStatus.OK).body(results);
    }
}
