package com.example.noidea2.repo.doc;

import com.example.noidea2.model.doc.DocDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocRepo extends JpaRepository<DocDetails,Integer> {

}
