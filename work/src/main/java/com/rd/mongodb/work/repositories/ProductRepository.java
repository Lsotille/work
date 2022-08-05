package com.rd.mongodb.work.repositories;

import com.rd.mongodb.work.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends MongoRepository<Product, Long> {

}
