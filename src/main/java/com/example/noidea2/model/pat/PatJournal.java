package com.example.noidea2.model.pat;

import com.example.noidea2.model.chat.Encrypt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@IdClass(PatJournalId.class)
@Entity(name = "journal")
public class PatJournal {
    @Id
    private  int pid;
    @Id
    private Date filledwhen;
    @Convert(converter = Encrypt.class)
    private String journaltext;
}
