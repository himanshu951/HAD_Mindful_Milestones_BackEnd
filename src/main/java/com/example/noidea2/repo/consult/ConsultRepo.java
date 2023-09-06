package com.example.noidea2.repo.consult;

import com.example.noidea2.model.consult.Consult;
import com.example.noidea2.model.consult.ConsultId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultRepo extends JpaRepository<Consult, ConsultId> {

    public List<Consult> getAllByConsultIdDid(Integer did);

    public Consult getByConsultId_Pid(Integer pid);
}
