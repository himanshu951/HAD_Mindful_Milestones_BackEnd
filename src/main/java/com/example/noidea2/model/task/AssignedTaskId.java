package com.example.noidea2.model.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssignedTaskId implements Serializable {
    private Integer pid;
    private Integer did;
    private Integer tid;
}
