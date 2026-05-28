package com.rrdm.library_api.controller;

import com.rrdm.library_api.model.Book;
import com.rrdm.library_api.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable UUID id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Book> save(@RequestBody Book book){
        System.out.println(book);
        return ResponseEntity.status(201).body(bookService.save(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID id) {
        return ResponseEntity.status(201).body(bookService.deleteById(id));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Book> update(@PathVariable UUID id, @RequestBody Book book) {
        Book updated = bookService.update(id, book);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> findBookByAuthor(@RequestParam("author") String author) {
        return ResponseEntity.ok(bookService.findByAuthor(author));
    }
}
