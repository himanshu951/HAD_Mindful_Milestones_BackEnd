package com.example.noidea2.repo.task;

import com.example.noidea2.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task,Integer> {
    public List<Task> findAllByTasktype(int ttype);

    public Task findByTid(int tid);
}
