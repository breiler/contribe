package com.breiler.contribe;

import com.breiler.contribe.model.Book;
import com.breiler.contribe.repository.BookRepository;
import com.breiler.contribe.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    private BookService bookService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.bookService = new BookService(this.bookRepository);
    }

    @Test
    public void creatingBooksShouldAlsoCreateEmptyStock() {
        // Given
        Book book = new Book();
        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        when(bookRepository.save(argumentCaptor.capture())).thenReturn(book);

        // When
        this.bookService.create(book);

        // Then
        verify(bookRepository, times(1)).save(any(Book.class));
        assertNotNull(argumentCaptor.getValue().getStock());
        assertEquals(0, argumentCaptor.getValue().getStock().getQuantity());
    }
}
