package com.example.noidea2.repo.pat;

import com.example.noidea2.model.pat.PatDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatRepo extends JpaRepository<PatDetails,Integer> {
    public PatDetails findByPid(Integer pid);
}
