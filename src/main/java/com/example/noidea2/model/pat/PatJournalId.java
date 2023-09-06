package com.example.noidea2.model.pat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public
class PatJournalId implements Serializable {
    private int pid;
    private Date filledwhen;
}
