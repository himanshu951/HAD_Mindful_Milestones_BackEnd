package com.example.noidea2.controller.auth;

import com.example.noidea2.model.auth.AuthRequest;
import com.example.noidea2.model.auth.Creds;
import com.example.noidea2.repo.auth.CredsRepo;
import com.example.noidea2.repo.consult.ConsultRepo;
import com.example.noidea2.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.http.HttpHeaders;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin
public class AuthController {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CredsRepo credsRepo;

    @Autowired
    private ConsultRepo consultRepo;
    @Autowired
    private AuthenticationManager authenticationManager;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    public String gneratePassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    @PostMapping("/try/something")
    public ResponseEntity<String> welcome(@RequestHeader("Authorization") String token) throws Exception{
        if(token==null) System.out.println("Hello this is not working");;
        try{
        token= token.substring(7);
        String uname=jwtUtil.extractUsername(token);
        System.out.println("Kall aya tha yaha par");
        return ResponseEntity.ok("hello "+uname);
        }catch (Exception e){
            throw e;
        }

    }

    @PostMapping("/only/admin")
    public String onlyAdmin() {
        return "hello";
    }

    @PostMapping("/doc/authenticate")
    public DocReturnObject generateToken(@RequestBody AuthRequest authRequest) throws Exception{
        if(authRequest.getRole()!=credsRepo.findByUsername(authRequest.getUsername()).getRole()) throw new Exception("Not Doctor");
        try{

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
            );
        }catch (Exception ex){
            throw new Exception("Invalid Username/Password");
        }
        String token=jwtUtil.generateToken(authRequest.getUsername());
        String uname=jwtUtil.extractUsername(token);
        Creds c=credsRepo.findByUsername(uname);
        return new DocReturnObject(token , c.getId());


    }

    @PostMapping("/doc/register")
    public Integer newReg(@RequestBody Creds registerRequest,@RequestHeader("Authorization") String token) throws Exception{
        token=token.substring(7);
        String uname=jwtUtil.extractUsername(token);
        Creds c=credsRepo.findByUsername(uname);
        if(c.getRole()!=0) throw new Exception("Chal na admin ko bhej");
        try{
            String genpass= gneratePassword();
            registerRequest.setPassword(passwordEncoder.encode(genpass));
            Creds ct = credsRepo.save(registerRequest);
            sendEmail(registerRequest.getEmail(),
                    "Your Credentials for Accessing the Medical Records System",
                    "Dear Doctor,\n" +
                            "\n" +
                            "I am writing to provide you with your login credentials for accessing our medical records system. As an authorized user, you will be able to access patient medical records and update them as needed.\n" +
                            "\n" +
                            "Your login credentials are as follows:\n" +
                            "\n" +
                            "Username: "+registerRequest.getUsername()+"\n" +
                            "Password: "+ genpass +"\n" +
                            "\n" +
                            "Please note that the password provided is case sensitive and must be kept confidential to ensure the security of patient data.\n" +
                            "\n" +
                            "To access the system, please visit [Insert Website URL] and enter your login credentials. If you experience any difficulties or have questions, please do not hesitate to contact our IT support team at [Insert Contact Information].\n" +
                            "\n" +
                            "Thank you for joining our team, and we look forward to working with you.\n" +
                            "\n" +
                            "Best regards,\n" +
                            "\n" +
                            "Admin\n" +
                            "admin@admin.com\n" +
                            "+91-10110101\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\n");
            return ct.getId();
        }catch (Exception ex){
//            System.out.println(ex);
            throw new Exception(ex);
        }

    }

    @PostMapping("/admin/authenticate")
    public ResponseEntity<String> generateTokenforAdmin(@RequestBody AuthRequest authRequest) throws Exception{
        System.out.println(authRequest.getRole()+" sakfanlskfnalk");
        if(authRequest.getRole() != credsRepo.findByUsername(authRequest.getUsername()).getRole() ) {
//            throw new Exception("Not Admin");
            return new ResponseEntity<>(
                    "Bad Credentials",
                    HttpStatus.BAD_REQUEST);
        }
        System.out.println(authRequest.getUsername());
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
            );
        }catch (Exception ex){
            throw new Exception("Invalid Username/Password");
        }
        String xyz=jwtUtil.generateToken(authRequest.getUsername());
        return new ResponseEntity<>(
                xyz,
                HttpStatus.OK);
    }

    @PostMapping("/doc/resetpwd")
    public String restdocpwd(@RequestHeader("Authorization") String token,@RequestBody Resetpwd resetpwd ) throws Exception{

        try{
            token=token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(resetpwd.getRole() != credsRepo.findByUsername(uname).getRole()) throw new Exception("Something Invalid");
            if(passwordEncoder.matches(resetpwd.getCurrentpwd(),c.getPassword())==false) throw new Exception("Invalid Current Pwd");
//            if(!c.getPassword().equals( resetpwd.getCurrentpwd())) throw new Exception("Invalid Current Pwd");
            Creds t= c;
            t.setPassword(passwordEncoder.encode(resetpwd.getNewpwd()));
            credsRepo.save(t);
            return "Changed pwd successfully";
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/pat/resetpwd")
    public Integer restpatpwd(@RequestHeader("Authorization") String token,@RequestBody Resetpwd resetpwd ) throws Exception{

        try{
            token=token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(resetpwd.getRole() != credsRepo.findByUsername(uname).getRole()) throw new Exception("Something Invalid");
            System.out.println(c.getPassword());
            if(passwordEncoder.matches(resetpwd.getCurrentpwd(),c.getPassword())==false) throw new Exception("Invalid Current Pwd");
//            if(!c.getPassword().equals(resetpwd.getCurrentpwd())) throw new Exception("Invalid Current Pwd");
            Creds t= c;
            t.setPassword(passwordEncoder.encode(resetpwd.getNewpwd()));
            credsRepo.save(t);
            return t.getId();
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/forgotpass")
    public String forgotdoc(@RequestBody Forgotpwd forgotpwd)throws Exception{
        try{
            Creds c=credsRepo.findByUsername(forgotpwd.getUsername());
            String newpass= gneratePassword();
            if(forgotpwd.getRole() != credsRepo.findByUsername(forgotpwd.getUsername()).getRole()) throw new Exception("Something Invalid");
                        sendEmail(c.getEmail(),
                    "Your Credentials for Accessing the Medical Records System",
                    "Dear User,\n" +
                            "\n" +
                            "I am writing to provide you with your login credentials for accessing our medical records system. As an authorized user, you will be able to access patient medical records and update them as needed.\n" +
                            "\n" +
                            "Your login credentials are as follows:\n" +
                            "\n" +
                            "Username: "+ c.getUsername() +"\n" +
                            "Password: "+ newpass +"\n" +
                            "\n" +
                            "Please note that the password provided is case sensitive and must be kept confidential to ensure the security of patient data.\n" +
                            "\n" +
                            "To access the system, please visit [Insert Website URL] and enter your login credentials. If you experience any difficulties or have questions, please do not hesitate to contact our IT support team at [Insert Contact Information].\n" +
                            "\n" +
                            "Thank you for joining our team, and we look forward to working with you.\n" +
                            "\n" +
                            "Best regards,\n" +
                            "\n" +
                            "Admin\n" +
                            "admin@admin.com\n" +
                            "+91-10110101\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\n");
            c.setPassword(passwordEncoder.encode(newpass));
            credsRepo.save(c);
            return "Retset Password";
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/notify/{pid}")
    public String notifypid(@RequestHeader("Authorization") String token,@PathVariable int pid) throws Exception{
        try{
            token=token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            Creds pat= credsRepo.findById(pid);
            if(c.getRole()==1 && consultRepo.getByConsultId_Pid(pid).getConsultId().getDid()==c.getId()){
                sendEmail(pat.getEmail(),
                        "Task Update: You've Got This!",
                        "Dear "+pat.getUsername()+",\n" +
                                "\n" +
                                "Just a friendly reminder about the task assigned to you at MindfulMilestones. We believe in your ability to complete it and make progress towards your goals. You're capable of achieving great things! \n" +
                                "\n"+
                                "If you require any support, please know that our team of experts, including our doctors, are available to assist you. Don't hesitate to reach out if you need any guidance or assistance. \n" +
                                "\n"+
                                "You've got this,"+ pat.getUsername() +"! We're here to support you every step of the way.\n" +
                                "\n"+
                                "Best regards,\n" +
                                "\n" +
                                c.getUsername()+"\n" +
                                c.getEmail()+"\n");
                return "Sent Mail";
            }else throw new Exception("Wrong doctor");
        }catch (Exception e){
            throw e;
        }
    }


}

@AllArgsConstructor
@NoArgsConstructor
@Data
class DocReturnObject{
    private String jwttoken;
    private Integer did;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Resetpwd{
    private String currentpwd,newpwd;
    private int role;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Forgotpwd{
    private String username;
    private int role;
}

