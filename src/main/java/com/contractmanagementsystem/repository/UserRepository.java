package com.contractmanagementsystem.repository;

import com.contractmanagementsystem.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    boolean existsByEmailId(String emailId);
    Optional<User> findByEmailId(String emailId);
}
