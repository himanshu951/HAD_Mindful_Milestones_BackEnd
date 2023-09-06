package com.example.noidea2.controller.doc;

import com.example.noidea2.model.auth.Creds;
import com.example.noidea2.model.consult.Consult;
import com.example.noidea2.model.doc.DocDetails;
import com.example.noidea2.model.pat.PatDetails;
import com.example.noidea2.repo.auth.CredsRepo;
import com.example.noidea2.repo.consult.ConsultRepo;
import com.example.noidea2.repo.doc.DocRepo;
import com.example.noidea2.repo.pat.PatRepo;
import com.example.noidea2.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin()
public class DocController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CredsRepo credsRepo;
    @Autowired
    private DocRepo docRepo;
    @Autowired
    private ConsultRepo consultRepo;
    @Autowired
    private PatRepo patRepo;

    @PostMapping("/doc/getcount")
    public Long countnoofdoc(@RequestHeader("Authorization") String token) throws Exception{
        token=token.substring(7);
        String uname=jwtUtil.extractUsername(token);
        Creds c=credsRepo.findByUsername(uname);
        if(c.getRole()!=0) throw new Exception("Chal na admin ko bhej");
        return docRepo.count();
    }

    @PostMapping("/doc/add")
    public String addDoc(@RequestBody DocDetails dd,@RequestHeader("Authorization") String token) throws Exception{
        if(dd== null || !credsRepo.findById(dd.getDid()).isPresent()) throw new Exception("nahi hoga add");
        token=token.substring(7);
        String uname=jwtUtil.extractUsername(token);
        Creds c=credsRepo.findByUsername(uname);
        if(c.getRole()!=0) throw new Exception("Chal na admin ko bhej");
        try {
            docRepo.save(dd);
        }catch (Exception e){
            throw new Exception("Kuch to galat ho raha");
        }
        return "added doc details";
    }

    @PostMapping("/doc/getall")
    public List<DocDetails> getall(@RequestHeader("Authorization") String token) throws Exception{
        token=token.substring(7);
        String uname=jwtUtil.extractUsername(token);
        Creds c=credsRepo.findByUsername(uname);
        if(c.getRole()!=0) throw new Exception("Chal na admin ko bhej");
        return docRepo.findAll();
    }


    @PostMapping("/doc/get/{id}")
    public Optional<DocDetails> getone(@RequestHeader("Authorization") String token,@PathVariable Integer id) throws Exception{
        token=token.substring(7);
        String uname=jwtUtil.extractUsername(token);
        Creds c=credsRepo.findByUsername(uname);
        if(c.getRole()==0 || (c.getRole()==1 && c.getId() == id) )
            return docRepo.findById(id);
        else throw new Exception("Not Authorized");
    }

    @PostMapping("/doc/change/{id}")
    public String changeDocDetails(@PathVariable Integer id,@RequestBody DocDetails dd,@RequestHeader("Authorization") String token) throws Exception{
        token=token.substring(7);
        String uname=jwtUtil.extractUsername(token);
        Creds c=credsRepo.findByUsername(uname);
        if(c.getRole()!=0) throw new Exception("Chal na admin ko bhej");
        DocDetails d=docRepo.findById(id).get();
        if(d==null) throw new Exception("no id found");
        docRepo.delete(d);
        docRepo.save(dd);
        return "Ho gya update";
    }

    @PostMapping("/get/patient/{pid}")
    public PatDetails getpatassdetil(@RequestHeader("Authorization") String token, @PathVariable int pid) throws Exception{
        try{
            token=token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()==1){
                List<Consult> l= consultRepo.getAllByConsultIdDid(c.getId());
                List<Integer> tt= new ArrayList<>();
                for(Consult a:l){
                    tt.add(a.getConsultId().getPid());
                }
                if(tt.contains(pid)){
                    return patRepo.findByPid(pid);
                }
                else throw new Exception("Not Assigned Patient");
            }
            else throw new Exception("Not Doctor");
        }catch (Exception e){
            throw e;
        }
    }
}
