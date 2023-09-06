package com.example.noidea2.model.ques;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
//SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2017-12-15 10:00"))

//2023-04-08T23:13:45.733+00:00
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionId implements Serializable {
    private Integer pid;
    private Date filledtime;
}
