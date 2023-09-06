package com.example.noidea2.model.pat;

import com.example.noidea2.model.chat.Encrypt;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patDetails")
public class PatDetails {
    @Id
    @NotNull
    private int pid;
    private String name;
    @Convert(converter = Encrypt.class)
    private  String phone,address;
    @Column(unique = true)
    private  String email;
    //initially Interger
    @Nullable
    private Float score;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date bDate;
    private int age;
    @Convert(converter = Encrypt.class)
    private String bloodgroup;

    private int gender,height,weight;
    @Convert(converter = Encrypt.class)
    private String journal;
}
