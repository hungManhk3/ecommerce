package com.hmanh.ecommerce.controller;

import com.hmanh.ecommerce.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    public ApiResponse home() {
        ApiResponse response = new ApiResponse();
        response.setMessage("Success");
        return response;
    }
}
