package com.example.noidea2.repo.chat;

import com.example.noidea2.model.chat.Chat;
import com.example.noidea2.model.chat.ChatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, ChatId> {
    @Query("select c from chat  c where c.pid=?1 and c.did=?2")
    public List<Chat> findxyz(int pid,int did);
}
