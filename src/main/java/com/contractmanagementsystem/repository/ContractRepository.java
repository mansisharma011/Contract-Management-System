package com.contractmanagementsystem.repository;

import com.contractmanagementsystem.model.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContractRepository extends MongoRepository<Contract, UUID> {
}
