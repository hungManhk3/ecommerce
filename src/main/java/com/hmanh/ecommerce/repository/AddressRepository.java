package com.hmanh.ecommerce.repository;

import com.hmanh.ecommerce.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
