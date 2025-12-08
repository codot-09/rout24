package com.example.rout24.repository;

import com.example.rout24.entity.BillingNumberSeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingNumberSeqRepository extends JpaRepository<BillingNumberSeq,Integer> {
}
