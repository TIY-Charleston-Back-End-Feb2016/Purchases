package com.theironyard;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by zach on 3/10/16.
 */
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
}
