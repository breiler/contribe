package com.breiler.contribe.repository;

import com.breiler.contribe.model.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, Long> {
    Stock findByBookId(Long bookId);
}
