package com.example.noidea2.repo.task;

import com.example.noidea2.model.consult.ConsultId;
import com.example.noidea2.model.task.AssignedTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignedTaskRepo extends JpaRepository<AssignedTask, ConsultId> {
    public List<AssignedTask> getAllByPid(int pid);
    public List<AssignedTask> getAllByDidAndPid(int did,int pid);

    public AssignedTask getByPidAndDidAndTid(int pid,int did,int tid);
//    public void deleteByPidAndDidAndTid(int pid,int did,int tid);
}
