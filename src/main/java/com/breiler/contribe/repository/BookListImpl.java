package com.breiler.contribe.repository;

import com.breiler.contribe.model.Book;
import com.breiler.contribe.model.Inventory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class BookListImpl implements BookList {

    private List<Inventory> inventoryList = new ArrayList<>();

    @Autowired
    public BookListImpl(InventoryProvider inventoryProvider) {
        List<Inventory> inventoryList = inventoryProvider.fetchInventory();
        inventoryList.forEach(inventory -> add(inventory.getBook(), Long.valueOf(inventory.getQuantity()).intValue()));
    }

    @Override
    public Book[] list(String searchString) {
        return inventoryList.stream()
                .filter(inventory -> inventory.getQuantity() > 0)
                .map(Inventory::getBook)
                .filter(filterBooksPredicate(searchString))
                .toArray(Book[]::new);
    }

    private Predicate<Book> filterBooksPredicate(String searchString) {
        return b -> StringUtils.isEmpty(searchString) || StringUtils.defaultString(b.getTitle()).toLowerCase().contains(searchString.toLowerCase()) || StringUtils.defaultString(b.getAuthor()).toLowerCase().contains(searchString.toLowerCase());
    }

    @Override
    public boolean add(Book book, int quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException("The quantity should not be negative");
        }

        Optional<Inventory> optionalBookInventory = getInventoryForBookById(book.getId());
        Inventory inventory = optionalBookInventory
                .orElse(Inventory
                        .builder()
                        .book(book)
                        .quantity(0)
                        .build());

        inventory.setQuantity(inventory.getQuantity() + quantity);

        if (!inventoryList.contains(inventory)) {
            inventoryList.add(inventory);
        }

        return true;
    }

    private Optional<Inventory> getInventoryForBookById(String bookId) {
        return inventoryList.stream()
                .filter(inventory -> inventory.getBook().getId().equals(bookId))
                .findFirst();
    }

    @Override
    public int[] buy(Book... books) {
        List<StatusEnum> statusList = new ArrayList<>();
        Arrays.asList(books).forEach(book -> statusList.add(buy(book)));

        return statusList.stream()
                .mapToInt(StatusEnum::getStatusCode)
                .toArray();
    }

    private StatusEnum buy(Book book) {
        Optional<Inventory> optionalInventory = getInventoryForBookById(book.getId());
        if(!optionalInventory.isPresent()) {
            return StatusEnum.DOES_NOT_EXIST;
        }

        if(optionalInventory.get().getQuantity() == 0 ) {
            return StatusEnum.NOT_IN_STOCK;
        }

        Inventory inventory = optionalInventory.get();
        inventory.setQuantity(inventory.getQuantity() - 1);
        return StatusEnum.OK;
    }
}
