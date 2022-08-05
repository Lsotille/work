package com.rd.mongodb.work.services;

import com.rd.mongodb.work.models.Product;
import com.rd.mongodb.work.models.dto.ProductDTO;
import com.rd.mongodb.work.repositories.ProductRepository;
import com.rd.mongodb.work.repositories.specs.products.ProductSpecification;
import com.rd.mongodb.work.utils.exceptions.ObjectNotFoundException;
import com.rd.mongodb.work.utils.sequenceGeneratorId.SequenceGeneratorId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static com.rd.mongodb.work.models.Product.SEQUENCE_NAME;


@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final SequenceGeneratorId sequenceGeneratorId;

    @Autowired
    private final ProductSpecification productSpecification;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
           Page<Product> list =  productRepository.findAll(pageable);
        return list.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findSearch(Pageable pageable, String q, Double min_price, Double max_price) {
        return productSpecification.findSearch(pageable, q.toUpperCase(),  min_price,  max_price);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
           Optional<Product> object = productRepository.findById(id);
           Product product = object.orElseThrow(() -> new ObjectNotFoundException("Objeto solicitado não encontrado!"));
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO insert(ProductDTO object) {
           Product product = new Product();
           product.setId(sequenceGeneratorId.getSequenceNumber(SEQUENCE_NAME));
           product.setName(object.getName());
           product.setDescription(object.getDescription());
           product.setPrice(object.getPrice());
           product = productRepository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO object) {
        try {
            Optional<Product> entity = productRepository.findById(id);
            Product product = entity.get();
            product.setName(object.getName());
            product.setDescription(object.getDescription());
            product.setPrice(object.getPrice());
            product = productRepository.save(product);
            return new ProductDTO(product);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException("Objeto solicitado não encontrado!");
        }
    }

    public void delete(Long id) {
        try {
            Optional<Product> product = productRepository.findById(id);
            if (product.get().getId() != null) {
                productRepository.deleteById(id);
            }
        }catch (RuntimeException e){
            throw new ObjectNotFoundException("Objeto solicitado não encontrado!");
        }
    }
}
