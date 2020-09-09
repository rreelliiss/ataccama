package com.siller.ataccama.example.remoteDatabaseAccess;

import com.siller.ataccama.example.connectionManagement.Connection;
import com.siller.ataccama.example.connectionManagement.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/databases")
public class RemoteDatabaseAccessController {
    private ConnectionRepository connectionRepository;
    private RemoteExecutor remoteExecutor;

    RemoteDatabaseAccessController(
            @Autowired ConnectionRepository connectionRepository,
            @Autowired RemoteExecutor remoteExecutor){
        this.connectionRepository = connectionRepository;
        this.remoteExecutor = remoteExecutor;
    }

    @GetMapping("/{databaseName}/tables")
    public Set<String> listTables(@PathVariable String databaseName){
        Connection connection = getConnectionOrThrowNotFoundException(databaseName);
        return remoteExecutor.getTableNames(connection);
    }

    @GetMapping("/{databaseName}/tables/{tableName}/columns")
    public List<Map<String, Object>> listColumnsOfTable(@PathVariable String databaseName, @PathVariable String tableName){
        Connection connection = getConnectionOrThrowNotFoundException(databaseName);
        Set<String> tableNames = remoteExecutor.getTableNames(connection);
        if(!tableNames.contains(tableName)){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "table not found");
        }
        try {
            // I checked that table exist so SQL injection should not be possible
            return remoteExecutor.getColumnsOfTable_SQLInjectionUnsafe(connection, tableName);
        } catch (TableNotFoundException e){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "table not found");
        }
    }

    @GetMapping("/{databaseName}/tables/{tableName}/preview")
    public List<Map<String,Object>> tablePreview(@PathVariable String databaseName, @PathVariable String tableName){
        Connection connection = getConnectionOrThrowNotFoundException(databaseName);
        Set<String> tableNames = remoteExecutor.getTableNames(connection);
        if(!tableNames.contains(tableName)){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "table not found");
        }
        try {
            // I checked that table exist so SQL injection should not be possible
            return remoteExecutor.getPreviewOfTable_SQLInjectionUnsafe(connection, tableName);
        } catch (TableNotFoundException e){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "table not found");
        }
    }

    private Connection getConnectionOrThrowNotFoundException(String databaseName) {
        Optional<Connection> possibleConnection = connectionRepository.findById(databaseName);
        if(possibleConnection.isPresent()){
            return possibleConnection.get();
        }
        throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Database not found");
    }
}
