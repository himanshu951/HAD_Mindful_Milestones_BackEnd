package com.example.noidea2.model.task;

import com.example.noidea2.model.consult.ConsultId;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(AssignedTaskId.class)
public class AssignedTask{
    @Id
    private Integer pid;
    @Id
    private Integer did;
    @Id
    private Integer tid;
    private boolean complete;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date assigneddate;
}
