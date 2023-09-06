package com.example.noidea2.model.consult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConsultId implements Serializable {
    private Integer did;
    private Integer pid;
}
