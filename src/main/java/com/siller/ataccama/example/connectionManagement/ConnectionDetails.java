package com.siller.ataccama.example.connectionManagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.nio.charset.Charset;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionDetails {
    @NotNull(message = "host can not be null")
    private String host;
    @NotNull(message = "port can not be null")
    private int port;
    @NotNull(message = "databaseName can not be null")
    private String databaseName;
    @NotNull(message = "username can not be null")
    private String username;
    @NotNull(message = "password can not be null")
    private String password;


//    @Column(columnDefinition= "LONGBLOB", name="password")
//    @ColumnTransformer(read = "AES_DECRYPT(password, 'mySecretKey')", write = "AES_ENCRYPT(?, 'mySecretKey')")
//    @NotNull(message = "password can not be null")
//    private byte[] password;
//
//    public String getPassword() {
//        return new String(password,  Charset.forName("UTF-8"));
//    }
}
