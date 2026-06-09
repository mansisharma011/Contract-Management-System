package com.contractmanagementsystem.repository;

import com.contractmanagementsystem.model.Contract;
import com.contractmanagementsystem.model.ContractStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends MongoRepository<Contract, String> {
    List<Contract> findByConsultantId(String consultantId);
    List<Contract> findByClientId(String clientId);
    List<Contract> findByContractStatus(ContractStatus contractStatus);
}
