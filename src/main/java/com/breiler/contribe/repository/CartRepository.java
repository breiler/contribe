package com.breiler.contribe.repository;

import com.breiler.contribe.model.Cart;
import com.breiler.contribe.model.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
}
