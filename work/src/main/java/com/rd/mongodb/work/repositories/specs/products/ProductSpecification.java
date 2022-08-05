package com.rd.mongodb.work.repositories.specs.products;


import com.rd.mongodb.work.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public class ProductSpecification {

    @Autowired
    private final MongoTemplate mongoTemplate;

    public ProductSpecification(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Page findSearch(Pageable pageable, String q, Double min_price, Double max_price){

        Query query = new Query();

        if(!q.isEmpty() && min_price != null && max_price != null) {
            /*
              procure pelo parametro q recebido em "name" OU "description"
              E em "price" valores entre minPrice e maxPrice
            */
            query.addCriteria(
                     new Criteria().orOperator(
                     Criteria.where("name").regex(q),
                     Criteria.where("description").regex(q)).andOperator(
                     Criteria.where("price").lte(max_price).gte(min_price)
                  )
            );
        }
        else if(!q.isEmpty() && min_price != null && max_price == null) {
            /*
             Procure pelo parametro q recebido em "name" OU "description"
             E maior ou igual a min_price em "price"
            */
            query.addCriteria(
                     new Criteria().orOperator(
                     Criteria.where("name").regex(q),
                     Criteria.where("description").regex(q)).andOperator(
                     Criteria.where("price").gte(min_price)
                )
            );
        }
        else if(!q.isEmpty() && min_price == null && max_price != null) {
            /*
            Procure pelo parametro q recebido em "name" OU "description"
            E menor ou igual a max_price em "price"
            */
            query.addCriteria(
                     new Criteria().orOperator(
                     Criteria.where("name").regex(q),
                     Criteria.where("description").regex(q)).andOperator(
                     Criteria.where("price").lte(max_price)
                )
            );
        }
        else if(!q.isEmpty() && min_price == null && max_price == null) {
            /*
            Procure pelo parametro q recebido em "name" OU "description"
            */
            query.addCriteria(
                     new Criteria().orOperator(
                     Criteria.where("name").regex(q),
                     Criteria.where("description").regex(q)
                )
            );
        }
        else if(q.isEmpty() && min_price != null && max_price != null) {
            /*
            Procure valores entre minPrice e maxPrice em "price"
            */
            query.addCriteria (
                     Criteria.where ("price").lte(max_price).gte(min_price)
            );
        }
        else if(q.isEmpty() && min_price != null && max_price == null) {
            /*
            Procure valores maior ou igual a min_price em "price"
            */
            query.addCriteria (
                    Criteria.where ("price").gte(min_price)
            );
        }
        else if(q.isEmpty() && min_price == null && max_price != null) {
            /*
            Procure valores menor ou igual a max_price em "price"
            */
            query.addCriteria (
                    Criteria.where ("price").lte(max_price)
            );
        }

        query.with(pageable);
        List<Product> list = mongoTemplate.find(query, Product.class);
        Query finalQuery = query;

        return PageableExecutionUtils.getPage(list, pageable, ()->mongoTemplate.count(finalQuery, Product.class));
	}
}