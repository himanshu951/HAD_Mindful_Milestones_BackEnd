package com.example.noidea2.controller.chat;

import com.example.noidea2.model.auth.Creds;
import com.example.noidea2.model.chat.Chat;
import com.example.noidea2.repo.auth.CredsRepo;
import com.example.noidea2.repo.chat.ChatRepo;
import com.example.noidea2.repo.consult.ConsultRepo;
import com.example.noidea2.repo.doc.DocRepo;
import com.example.noidea2.repo.task.AssignedTaskRepo;
import com.example.noidea2.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class ChatController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ConsultRepo consultRepo;
    @Autowired
    private DocRepo docRepo;
    @Autowired
    private CredsRepo credsRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AssignedTaskRepo assignedTaskRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ChatRepo chatRepo;

    @PostMapping("/send/msg")
    public Chat sendmsg(@RequestHeader("Authorization") String token, @RequestBody Chat chat) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if( (c.getRole()==1 && c.getId()==chat.getDid()) || (c.getRole()==2 && c.getId()==chat.getPid())){
                chat.setSentwhen(new Date());
                return chatRepo.save(chat);
            }
            else throw new Exception("Restricted !!!");
        }catch (Exception e){
            throw e;
        }
    }


    @PostMapping("/get/chats")
    public List<Chat> getchats(@RequestHeader("Authorization") String token, @RequestBody Recievemsg recievemsg) throws Exception{
        try{
            token= token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if( (c.getRole()==1 && c.getId()==recievemsg.getDid()) || (c.getRole()==2 && c.getId()==recievemsg.getPid())){
                return chatRepo.findxyz(recievemsg.getPid(), recievemsg.getDid());
            }
            else throw new Exception("Unauthorized");
        }catch (Exception e) {
            throw e;
        }
    }

}
@AllArgsConstructor
@NoArgsConstructor
@Data
class Recievemsg{
    private int pid;
    private int did;
}