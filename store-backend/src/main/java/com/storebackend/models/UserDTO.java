package com.storebackend.models;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String password;
    private String mail;
    private String phone;
    private String country;
    private String address;
}
