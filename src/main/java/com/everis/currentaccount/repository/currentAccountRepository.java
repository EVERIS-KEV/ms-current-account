package com.everis.currentaccount.repository;

import com.everis.currentaccount.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface currentAccountRepository
  extends MongoRepository<currentAccount, String> {
  boolean existsByAccountNumber(String number);
  boolean existsByIdCustomer(String id);

  currentAccount findByAccountNumber(String number);
}
