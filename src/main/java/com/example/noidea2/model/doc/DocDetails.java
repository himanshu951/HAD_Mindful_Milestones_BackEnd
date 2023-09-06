package com.example.noidea2.model.doc;

import com.example.noidea2.model.chat.Encrypt;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "docDetails")
public class DocDetails {
    @Id
    private Integer did;
    private String name;
    @Convert(converter = Encrypt.class)
    private String phone;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date bDate;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    @Convert(converter = Encrypt.class)
    private String lic;
    @Convert(converter = Encrypt.class)
    private String qual;
    @Convert(converter = Encrypt.class)
    private String specs;
}
