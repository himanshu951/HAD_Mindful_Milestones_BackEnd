package com.example.noidea2.repo.pat;

import com.example.noidea2.model.pat.PatJournal;
import com.example.noidea2.model.pat.PatJournalId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatJournalRepo extends JpaRepository<PatJournal, PatJournalId> {
    public  List<PatJournal> findAllByPidOrderByFilledwhenAsc(int pid);
}
