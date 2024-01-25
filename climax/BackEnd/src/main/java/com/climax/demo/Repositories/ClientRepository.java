package com.climax.demo.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.climax.demo.Models.Client;

 
public interface  ClientRepository extends CrudRepository<Client, Long> {
    
}
