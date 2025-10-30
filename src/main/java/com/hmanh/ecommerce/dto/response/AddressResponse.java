package com.hmanh.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private String name;
    private String locality;
    private String address;
    private String city;
    private String state;
    private String country;
}