package com.breiler.contribe.controller;

import com.breiler.contribe.contract.BookDTO;
import com.breiler.contribe.contract.CreateBookDTO;
import com.breiler.contribe.model.Book;
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

@Api(basePath = "/api", tags = "Books", description = "Resources for handling books", produces = "application/json")
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
    @ApiOperation(value = "Fetches all books in the store",
            notes = "Fetches all books in the book store. It's possible to filter the results with a query string which will search for the title or author.",
            response = BookDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The books was retreived successfully", response = BookDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<List<BookDTO>> findBooks(
            @ApiParam(name = "query", value = "A query string for searching books")
            @RequestParam(value = "query", required = false)
                    String query
    ) {
        List<Book> books = bookService.findByQuery(query);

        Type listType = new TypeToken<List<BookDTO>>() {}.getType();
        List<BookDTO> results = modelMapper.map(books, listType);

        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    @RequestMapping(value = "/api/books", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new book",
            notes = "Creates a new book in the store along with an empty inventory stock.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The books was created successfully", response = BookDTO.class),
            @ApiResponse(code = 500, message = "Something went wrong when processing the request")
    })
    public ResponseEntity<BookDTO> create(
            @ApiParam(value = "The book to create")
            @RequestBody
                    CreateBookDTO bookDTO
    ) {
        Book book = modelMapper.map(bookDTO, Book.class);
        book = bookService.create(book);

        BookDTO result = modelMapper.map(book, BookDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
