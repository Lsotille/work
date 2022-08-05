package com.rd.mongodb.work.resources;

import com.rd.mongodb.work.models.dto.ProductDTO;
import com.rd.mongodb.work.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductResource {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                    @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                    @RequestParam(value = "orderBy", defaultValue = "id") String orderBy) {
        Pageable pageable = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<ProductDTO> list = productService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> search(@RequestParam(value = "q", defaultValue = "", required = false) String q,
                                                   @RequestParam(value = "min_price", required = false) Double min_price,
                                                   @RequestParam(value = "max_price", required = false) Double max_price) {
           Pageable pageable  = PageRequest.of(0, 12, Sort.Direction.valueOf("ASC"), "id");
           Page<ProductDTO> list = productService.findSearch(pageable, q, min_price, max_price);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO object) {
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                             .buildAndExpand(object.getId()).toUri()).body(productService.insert(object));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@Valid @RequestBody ProductDTO object, @PathVariable Long id) {
        return ResponseEntity.ok().body(productService.update(id, object));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> delete(@Valid @PathVariable Long id) {
           productService.delete(id);
        return ResponseEntity.ok().build();
    }

}