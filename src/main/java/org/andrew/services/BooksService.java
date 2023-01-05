package org.andrew.services;

import org.andrew.models.Book;
import org.andrew.models.Person;
import org.andrew.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
    }

    public Book findBook(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear){
        if(sortByYear){
            return  booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public List<Book> searchByTitle(String title){
        return booksRepository.findByTitleStartingWith(title);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updBook) {
        Book bookToBeUpdated = booksRepository.findById(id).get();

        updBook.setId(id);
        updBook.setBookOwner(bookToBeUpdated.getBookOwner());

        booksRepository.save(updBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Person getBookOwner(int id){
        return booksRepository.findById(id).map(Book::getBookOwner).orElse(null);
    }

    @Transactional
    public void release(int id){
        booksRepository.findById(id).ifPresent(book -> {
            book.setBookOwner(null);
            book.setTakenAt(null);
        });
    }

    @Transactional
    public void assign(int id, Person selectedPerson){
        booksRepository.findById(id).ifPresent(book -> {
            book.setBookOwner(selectedPerson);
            book.setTakenAt(new Date());
        });
    }

}
