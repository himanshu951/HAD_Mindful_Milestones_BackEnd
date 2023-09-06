package com.example.noidea2.repo.ques;

import com.example.noidea2.model.ques.Question;
import com.example.noidea2.model.ques.QuestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.lang.annotation.Native;
import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, QuestionId> {
    public List<Question> findByQuestionIdPidOrderByQuestionIdFilledtimeDesc(int pid);
}
