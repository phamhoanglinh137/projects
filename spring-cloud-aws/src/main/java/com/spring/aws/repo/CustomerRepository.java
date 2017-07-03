package com.spring.aws.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.spring.aws.repo.entity.Customer;

/**
 * 
 * @author linhpham
 *
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    public List<Customer> findByFirstName(String firstName); 
}