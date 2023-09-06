package com.example.noidea2.repo.auth;

import com.example.noidea2.model.auth.Creds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredsRepo extends JpaRepository<Creds,Integer> {

    Creds findByUsername(String username);

    Creds findByEmail(String email);

    Creds findById(int id);

}
