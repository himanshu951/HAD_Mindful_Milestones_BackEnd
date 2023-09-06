package com.example.noidea2.controller.pat;

import com.example.noidea2.model.auth.AuthRequest;
import com.example.noidea2.model.auth.Creds;
import com.example.noidea2.model.pat.PatDetails;
import com.example.noidea2.repo.auth.CredsRepo;
import com.example.noidea2.repo.consult.ConsultRepo;
import com.example.noidea2.repo.pat.PatRepo;
import com.example.noidea2.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class PatController {
    @Autowired
    private JwtUtil jwtUtil;



    @Autowired
    private CredsRepo credsRepo;

    @Autowired
    private PatRepo patRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConsultRepo consultRepo;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    @PostMapping("/pat/register")
    public Integer addCreds(@RequestBody Creds pd) throws Exception{
        if(pd==null) throw new Exception("kya be");
        try{
            String p= pd.getPassword();
            pd.setPassword(passwordEncoder.encode(pd.getPassword()));
            sendEmail(pd.getEmail(),
                    "Subject: Welcome to MindfulMilestones!",
                    "Dear "+ pd.getUsername() +",\n" +
                            "\n" +
                            "Welcome to MindfulMilestones! We're thrilled to have you as a registered user. Thank you for choosing us.\n" +
                            "\n" +
                            "At MindfulMilestones, we're dedicated to providing you with exceptional mindfulness tools and resources. As a registered user, you now have access to our premium features designed to help you on your mindfulness journey. We're here to assist you in any way we can, so feel free to reach out if you need any help.\n" +
                            "\n" +
                            "Once again, welcome to MindfulMilestones! We're excited to serve you and support you in achieving your mindfulness goals.\n" +
                            "\n" +
                            "Best, \n"+
                            "MindfulMilestones\n" +
                            "\n" +
                            "\n" +
                            "\n");
            Creds c=credsRepo.save(pd);
            return c.getId();
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/pat/authenticate")
    public PatReruenObj login(@RequestBody AuthRequest pd) throws Exception{
        if(pd==null) throw new Exception("kya be");
//        System.out.println(pd.getRole()+" sakfanlskfnalk");
        if(pd.getRole().intValue()  !=  credsRepo.findByUsername(pd.getUsername()).getRole() ) {
//            throw new Exception("Not Admin");
            throw new Exception("Ja na Gandu");
        }
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(pd.getUsername(),pd.getPassword())
            );
        }catch (Exception ex){
            throw new Exception("Invalid Username/Password");
        }
        String xyz=jwtUtil.generateToken(pd.getUsername());
        Creds tt= credsRepo.findByUsername(pd.getUsername());
        if(consultRepo.getByConsultId_Pid(tt.getId())!=null)
            return new PatReruenObj(xyz,tt.getId(),consultRepo.getByConsultId_Pid(tt.getId()).getConsultId().getDid());
        return new PatReruenObj(xyz,tt.getId(),null);
    }

    @PostMapping("/pat/savedetail")
    public String savedetails(@RequestBody PatDetails pd) throws Exception{
        if(pd==null) throw new Exception("null valaue");
        if(credsRepo.findById(pd.getPid()) == null) throw new Exception("Could not add random");
        try{
            patRepo.save(pd);
            return "Patient details added";
        }catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/pat/savepatdetailbytoken")
    public String savedetailsbytoken(@RequestHeader("Authorization") String token,@RequestBody PatDetails pd) throws Exception{
        if(pd==null) throw new Exception("null valaue");
        token=token.substring(7);
        String uname=jwtUtil.extractUsername(token);
        Creds c=credsRepo.findByUsername(uname);
        pd.setPid(c.getId());
        if(credsRepo.findById(pd.getPid()) == null) throw new Exception("Could not add random");
        try{
            patRepo.save(pd);
            return "Patient details added";
        }catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/patget/detail")
    public PatDetails getdetailsofpat(@RequestHeader("Authorization") String token) throws Exception{
        try{
            token=token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=2) throw new Exception("Not Patient");
            return patRepo.findByPid(c.getId());
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/pat/getall")
    public Integer getpatcount(@RequestHeader("Authorization") String token) throws Exception{
        try{
            token=token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=0) throw new Exception("Not Admin");
            return patRepo.findAll().size();
        }catch (Exception e){
            throw e;
        }
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class PatReruenObj{
    private String token;
    private int pid;
    private Integer did;
}
