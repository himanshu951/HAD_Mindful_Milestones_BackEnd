package com.example.noidea2.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatId implements Serializable {
    private int pid;
    private int did;
    private Date sentwhen;
}
