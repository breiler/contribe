package com.breiler.contribe.repository;

import com.breiler.contribe.model.Book;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class BookListImplTest {

    @Mock
    private InventoryProvider inventoryProvider;

    private BookListImpl target;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(inventoryProvider.fetchInventory()).thenReturn(new ArrayList<>());

        this.target = new BookListImpl(inventoryProvider);
    }

    @Test
    public void addingOneBookManyTimesShouldOnlyIncreaseInventory() {
        // When
        Book book = Book.builder().title("1").build();
        target.add(book, 1);
        target.add(book, 3);
        target.add(book, 0);

        // Then
        Book[] books = target.list("");
        assertNotNull(books);
        assertEquals(1, books.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingBooksWithNegativeQuantityShouldThrowError() {
        // When
        target.add(Book.builder().title("1").build(), -1);
    }

    @Test
    public void listBooksShouldReturnAllBooksThatExistsInInventory() {

        // Given
        target.add(Book.builder().title("1").build(), 2);
        target.add(Book.builder().title("2").build(), 1);
        target.add(Book.builder().title("3").build(), 0);


        // When
        Book[] books = target.list("");

        // Then
        assertNotNull(books);
        assertEquals(2, books.length);
    }

    @Test
    public void listBooksWithNoSearchStringReturnAllBooksThatExistsInInventory() {

        // Given
        target.add(Book.builder().title("1").build(), 2);


        // When
        Book[] books = target.list(null);

        // Then
        assertNotNull(books);
        assertEquals(1, books.length);
    }

    @Test
    public void searchStringShouldReturnBooksWithStringInTitle() {

        // Given
        target.add(Book.builder().title("The book title").build(), 2);
        target.add(Book.builder().title("Another book title").build(), 2);
        target.add(Book.builder().author("The author").build(), 2);


        // When
        Book[] books = target.list("title");

        // Then
        assertNotNull(books);
        assertEquals(2, books.length);
    }

    @Test
    public void searchStringShouldReturnBooksWithStringInAuthor() {

        // Given
        target.add(Book.builder().title("The book title").build(), 2);
        target.add(Book.builder().title("Another book title").build(), 2);
        target.add(Book.builder().author("The author").build(), 2);
        target.add(Book.builder().author("Another author").build(), 2);

        // When
        Book[] books = target.list("author");

        // Then
        assertNotNull(books);
        assertEquals(2, books.length);
    }

    @Test
    public void searchStringShouldReturnBooksWithStringInBothTitleAndAuthor() {

        // Given
        target.add(Book.builder().title("The book title with searchstring").build(), 2);
        target.add(Book.builder().title("Another book title").build(), 2);
        target.add(Book.builder().author("The author").build(), 2);
        target.add(Book.builder().author("Another author with searchstring").build(), 2);

        // When
        Book[] books = target.list("searchstring");

        // Then
        assertNotNull(books);
        assertEquals(2, books.length);
    }

    @Test
    public void searchStringShouldIgnoreCase() {

        // Given
        target.add(Book.builder().title("The book title with searchstring").build(), 2);
        target.add(Book.builder().title("Another book title").build(), 2);
        target.add(Book.builder().author("The author").build(), 2);
        target.add(Book.builder().author("Another author with searchstring").build(), 2);

        // When
        Book[] books = target.list("SEARCHSTRING");

        // Then
        assertNotNull(books);
        assertEquals(2, books.length);
    }

    @Test
    public void buyingBookShouldRemoveItFromInventory() {

        // Given
        Book book = Book.builder().title("1").build();
        target.add(book, 1);

        // When
        int[] statuses = target.buy(book, book);
        int[] statusesAfterBought = target.buy(book, book);

        // Then
        assertNotNull(statuses);
        assertEquals(2, statuses.length);
        assertEquals(StatusEnum.OK.getStatusCode(), statuses[0]);
        assertEquals(StatusEnum.NOT_IN_STOCK.getStatusCode(), statuses[1]);

        assertNotNull(statusesAfterBought);
        assertEquals(2, statusesAfterBought.length);
        assertEquals(StatusEnum.NOT_IN_STOCK.getStatusCode(), statusesAfterBought[0]);
        assertEquals(StatusEnum.NOT_IN_STOCK.getStatusCode(), statusesAfterBought[1]);
    }

    @Test
    public void buyingBookNotInInventoryShouldReturnNotFound() {

        // Given
        Book book = Book.builder().title("1").build();

        // When
        int[] statuses = target.buy(book);

        // Then
        assertNotNull(statuses);
        assertEquals(1, statuses.length);
        assertEquals(StatusEnum.DOES_NOT_EXIST.getStatusCode(), statuses[0]);
    }
}
