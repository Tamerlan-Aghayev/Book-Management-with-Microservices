package az.expressbank.book.controller;

import az.expressbank.book.data.dto.BookDTO;
import az.expressbank.book.service.impl.BookServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookServiceImpl bookServiceImpl;
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getALl() throws Exception {
        return ResponseEntity.ok(bookServiceImpl.getAll());
    }
    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable("id") Long id)throws Exception{
        return ResponseEntity.ok(bookServiceImpl.getById(id));
    }
    @PostMapping("/book")
    public ResponseEntity<Long> addBook(@RequestBody BookDTO bookDTO)throws Exception{
        return ResponseEntity.ok(bookServiceImpl.addBook(bookDTO));
    }
}
