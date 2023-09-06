package com.example.noidea2.controller.ques;

import com.example.noidea2.model.auth.Creds;
import com.example.noidea2.model.consult.Consult;
import com.example.noidea2.model.pat.PatDetails;
import com.example.noidea2.model.ques.Question;
import com.example.noidea2.model.ques.QuestionId;
import com.example.noidea2.repo.auth.CredsRepo;
import com.example.noidea2.repo.consult.ConsultRepo;
import com.example.noidea2.repo.doc.DocRepo;
import com.example.noidea2.repo.pat.PatRepo;
import com.example.noidea2.repo.ques.QuestionRepo;
import com.example.noidea2.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class QuestionController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ConsultRepo consultRepo;
    @Autowired
    private DocRepo docRepo;
    @Autowired
    private PatRepo patRepo;
    @Autowired
    private CredsRepo credsRepo;
    @Autowired
    private QuestionRepo questionRepo;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/save/question")
    public QuestionId savequestion(@RequestBody Question question,@RequestHeader("Authorization") String token) throws Exception {
        try{
            token = token.substring(7);
            String uname = jwtUtil.extractUsername(token);
            Creds c = credsRepo.findByUsername(uname);
            if (c.getRole() != 2) throw new Exception("error");
            question.getQuestionId().setPid(c.getId());
            question.getQuestionId().setFilledtime(new Date());
            float score= calcScore(question);
            PatDetails pd= patRepo.findByPid(question.getQuestionId().getPid());
            pd.setScore(score);
            Question q = questionRepo.save(question);
            if(consultRepo.getByConsultId_Pid(c.getId()).getConsultId() != null){
                Consult ct= consultRepo.getByConsultId_Pid(c.getId());
                ct.setIslastconsulted(null);
                consultRepo.save(ct);
            }
            return q.getQuestionId();
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/get/latestquestion/{pid}")
    public List<Integer> getlatestq(@RequestHeader("Authorization") String token, @PathVariable int pid) throws Exception{
        try{
            token = token.substring(7);
            String uname = jwtUtil.extractUsername(token);
            Creds c = credsRepo.findByUsername(uname);
//            System.out.println(consultRepo.getByConsultId_Pid(pid).getConsultId().getDid() == c.getId());
            if(c.getRole() == 1 && consultRepo.getByConsultId_Pid(pid).getConsultId().getDid() == c.getId()) {
                Question q=questionRepo.findByQuestionIdPidOrderByQuestionIdFilledtimeDesc(pid).get(0);
                ArrayList<Integer> t= new ArrayList<>();
                t.add(q.getQ1());t.add(q.getQ2());t.add(q.getQ3());t.add(q.getQ4());
                t.add(q.getQ5());t.add(q.getQ6());t.add(q.getQ7());t.add(q.getQ8());
                return t;
//                return null;/
            }
            else throw new Exception("Something went horribly wrong");
        }catch (Exception e){
            throw e;
        }
    }



    private float calcScore(Question question) {
        int q1,q2,q3,q4,q5,q6,q7,q8;
        q1= question.getQ1();
        q2= question.getQ2();
        q3= question.getQ3();
        q4= question.getQ4();
        q5= question.getQ5();
        q6= question.getQ6();
        q7= question.getQ7();
        q8= question.getQ8();
        return (float)(q1+q2+q3+q4+q5+q6+q7+q8)/8;
    }
}
