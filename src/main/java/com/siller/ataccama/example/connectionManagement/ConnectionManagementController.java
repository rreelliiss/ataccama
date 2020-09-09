package com.siller.ataccama.example.connectionManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/connections")
public class ConnectionManagementController {

    private ConnectionRepository connectionRepository;

    ConnectionManagementController(@Autowired ConnectionRepository connectionRepository){
        this.connectionRepository = connectionRepository;
    }

    @GetMapping()
    public List<Connection> listAllConnections(){
        List<Connection> connections = new ArrayList<>();
        connectionRepository.findAll().forEach(connections::add);
        return connections;
    }

    @GetMapping("/{connectionName}")
    public Connection getConnection(@PathVariable String connectionName){
        Optional<Connection> possibleConnection = connectionRepository.findById(connectionName);
        if(possibleConnection.isPresent()){
            return possibleConnection.get();
        }
        throw  new ResponseStatusException(
                HttpStatus.NOT_FOUND, "connection not found"
        );
    }

    @PostMapping
    public String addConnection(@RequestBody @Valid ConnectionDetails connectionDetails){
        String id = connectionRepository.generateId();
        connectionRepository.save(new Connection(id, connectionDetails));
        return id;
    }

    @PutMapping
    public void updateConnection(@Valid @RequestBody Connection connection){
        connectionRepository.save(connection);
    }

    @DeleteMapping("/{connectionName}")
    public void deleteConnection(@PathVariable String connectionName){
        if(connectionRepository.existsById(connectionName)){
            connectionRepository.deleteById(connectionName);
            return;
        }
        throw  new ResponseStatusException(
                HttpStatus.NOT_FOUND, "connection not found"
        );
    }


}
