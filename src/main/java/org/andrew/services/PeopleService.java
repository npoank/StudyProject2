package org.andrew.services;

import org.andrew.models.Book;
import org.andrew.models.Person;
import org.andrew.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findPerson(int id){
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save (Person person){
        peopleRepository.save(person);
    }


    @Transactional
    public void update (int id, Person updPerson){
        updPerson.setId(id);
        peopleRepository.save(updPerson);
    }

    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }


    public Optional<Person> findByFullName (String findingName){
       return peopleRepository.findByFullName(findingName);
    }

    public List<Book> booksByPerson(int id){
        Optional<Person> person = peopleRepository.findById(id);

        if(person.isPresent()){
            Hibernate.initialize(person.get().getPersonBooks());

            person.get().getPersonBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                //864_000_000 miliseconds = 10 days
                if(diffInMillies > 864_000_000){
                    book.setExpired(true);
                }
            });

            return person.get().getPersonBooks();
        } else {
            return Collections.emptyList();
        }
    }






}
