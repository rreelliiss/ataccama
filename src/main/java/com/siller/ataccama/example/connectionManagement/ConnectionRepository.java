package com.siller.ataccama.example.connectionManagement;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ConnectionRepository extends CrudRepository<Connection, String> {

    default public String generateId(){
            String id = UUID.randomUUID().toString();
            while(existsById(id)){
                id = UUID.randomUUID().toString();
            }
            return id;
    }
}