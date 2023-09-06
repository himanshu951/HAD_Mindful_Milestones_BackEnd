package com.example.noidea2.model.chat;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "chat")
@IdClass(ChatId.class)
public class Chat {
    @Id
    private int pid;
    @Id
    private int did;
    @Id
    private Date sentwhen;
    @NotNull
    @Convert(converter = Encrypt.class)
    private String msg;
    @NotNull
    private int sentfrom;
}
