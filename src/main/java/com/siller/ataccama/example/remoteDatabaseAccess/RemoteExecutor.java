package com.siller.ataccama.example.remoteDatabaseAccess;

import com.siller.ataccama.example.connectionManagement.Connection;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class RemoteExecutor {

    private java.sql.Connection connect(Connection connection) throws SQLException {
        String url = String.format(
                "jdbc:mysql://%s:%d/%s?serverTimezone=UTC",
                connection.getConnectionDetails().getHost(),
                connection.getConnectionDetails().getPort(),
                connection.getConnectionDetails().getDatabaseName()
        );
        String username = connection.getConnectionDetails().getUsername();
        String password = connection.getConnectionDetails().getPassword();
        return DriverManager.getConnection(url, username, password);
    }


    public Set<String> getTableNames(Connection connection){
        try (java.sql.Connection remoteConnection = connect(connection)) {
            ResultSet queryResult = remoteConnection.createStatement().executeQuery("show tables");
            Set<String> result = new HashSet<>();
            while(queryResult.next()){
                result.add(queryResult.getString(1));
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("An error with the database!");
        }
    }

    public List<Map<String, Object>> getColumnsOfTable_SQLInjectionUnsafe(Connection connection, String tableName)
            throws TableNotFoundException {
        try (java.sql.Connection remoteConnection = connect(connection)) {
            // following can lead to sql injection but I can't create prepared statement with variable table name
            ResultSet queryResult = remoteConnection.createStatement()
                    .executeQuery("show columns from " + tableName);
            Set<String> result = new HashSet<>();
            return getMappedRows(queryResult);
        } catch (SQLException e) {
            if(e.getErrorCode() == 1146){
                throw new TableNotFoundException();
            }
            e.printStackTrace();
            throw new IllegalStateException("An error with the database!");
        }
    }

    public List<Map<String, Object>> getPreviewOfTable_SQLInjectionUnsafe(Connection connection, String tableName) throws TableNotFoundException {
        try (java.sql.Connection remoteConnection = connect(connection)) {
            // following can lead to sql injection but I can't create prepared statement with variable table name
            ResultSet queryResult = remoteConnection.createStatement()
                    .executeQuery(String.format("select * from %s limit 10", tableName));
            return getMappedRows(queryResult);
        } catch (SQLException e) {
            if(e.getErrorCode() == 1146){
                throw new TableNotFoundException();
            }
            e.printStackTrace();
            throw new IllegalStateException("An error with the database!");
        }
    }

    private List<Map<String, Object>> getMappedRows(ResultSet queryResult) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        int columnCount = queryResult.getMetaData().getColumnCount();
        while(queryResult.next()){
            Map<String, Object> row = new HashMap<>();
            for(int i = 1; i <= columnCount; i++){
                row.put(
                        queryResult.getMetaData().getColumnName(i),
                        queryResult.getObject(i)
                );
            }
            result.add(row);
        }
        return result;
    }


}
