package com.example.noidea2.controller.pat;

//import com.example.noidea2.controller.task.ListobjectReturn;
import com.example.noidea2.model.auth.Creds;
import com.example.noidea2.model.consult.Consult;
import com.example.noidea2.model.pat.PatJournal;
import com.example.noidea2.model.task.AssignedTask;
import com.example.noidea2.repo.auth.CredsRepo;
import com.example.noidea2.repo.consult.ConsultRepo;
import com.example.noidea2.repo.pat.PatJournalRepo;
import com.example.noidea2.repo.task.AssignedTaskRepo;
import com.example.noidea2.repo.task.TaskRepo;
import com.example.noidea2.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class PatJournalController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CredsRepo credsRepo;
    @Autowired
    private PatJournalRepo patJournalRepo;
    @Autowired
    private AssignedTaskRepo assignedTaskRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private ConsultRepo consultRepo;
    @PostMapping("/journal/save")
    public PatJournal savejournal(@RequestHeader("Authorization") String token, @RequestBody PatJournal patJournal) throws Exception{
        try{
            token=token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=2) throw new Exception("Not Patient");
            patJournal.setFilledwhen(new Date());
            return patJournalRepo.save(patJournal);
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/get/alljournal")
    public List<PatJournal> listofjournal(@RequestHeader("Authorization") String token) throws Exception{
        try{
            token=token.substring(7);
            String uname=jwtUtil.extractUsername(token);
            Creds c=credsRepo.findByUsername(uname);
            if(c.getRole()!=2) throw new Exception("Not Patient");
            return patJournalRepo.findAllByPidOrderByFilledwhenAsc(c.getId());
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/getnote/consult")
    public String getnote(@RequestHeader("Authorization") String token, @RequestBody AskObj askObj) throws Exception{
        try{
            token = token.substring(7);
            String uname = jwtUtil.extractUsername(token);
            Creds c = credsRepo.findByUsername(uname);
            if(c.getRole()==1 && c.getId()==consultRepo.getByConsultId_Pid(askObj.getPid()).getConsultId().getDid()){
                return consultRepo.getByConsultId_Pid(askObj.getPid()).getNote();
            }
            else throw new Exception("Invalid");
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/setnote/consult")
    public String setnote(@RequestHeader("Authorization") String token, @RequestBody AskObj askObj) throws Exception{
        try{
            token = token.substring(7);
            String uname = jwtUtil.extractUsername(token);
            Creds c = credsRepo.findByUsername(uname);
            if(c.getRole()==1 && c.getId()==consultRepo.getByConsultId_Pid(askObj.getPid()).getConsultId().getDid()){
                Consult cd= consultRepo.getByConsultId_Pid(askObj.getPid());
                cd.setNote(askObj.getNote());
                consultRepo.save(cd);
                return "Updated Notes";
            }
            else throw new Exception("Invalid");
        }catch (Exception e){
            throw e;
        }
    }

    @PostMapping("/patget/status/{pid}")
    public List<ListObj> status(@RequestHeader("Authorization") String token,@PathVariable Integer pid ) throws Exception{
        try{
            ArrayList<ListObj> returnobj= new ArrayList<ListObj>();
            ArrayList<Integer[]> values= new ArrayList<>();
            values.add(new Integer[] {0,0});values.add(new Integer[] {0,0});values.add(new Integer[] {0,0});
            token = token.substring(7);
            String uname = jwtUtil.extractUsername(token);
            Creds c = credsRepo.findByUsername(uname);
            ArrayList<AssignedTask> arr = new ArrayList<AssignedTask>();
            if (c.getRole() == 2) {
                if (c.getId() != pid)
                    throw new Exception("Invalid patient");
                List<AssignedTask> tt= assignedTaskRepo.getAllByPid(pid);
                for(AssignedTask a:tt){
                    if(a.isComplete()==true){
                        values.get(taskRepo.findByTid(a.getTid()).getTasktype()-1)[0]++;
                    }
                    values.get(taskRepo.findByTid(a.getTid()).getTasktype()-1)[1]++;
                }
                returnobj.add(new ListObj(1,values.get(0)[0],values.get(0)[1]));
                returnobj.add(new ListObj(2,values.get(1)[0],values.get(1)[1]));
                returnobj.add(new ListObj(3,values.get(2)[0],values.get(2)[1]));
                return returnobj;
            }
            else if (c.getRole() == 1) {
                if (consultRepo.getByConsultId_Pid(pid).getConsultId().getDid() != c.getId())
                    throw new Exception("Not appropriate doctor");
                int did = consultRepo.getByConsultId_Pid(pid).getConsultId().getDid();
                List<AssignedTask> tt=  assignedTaskRepo.getAllByDidAndPid(did, pid);
                for(AssignedTask a:tt){
                    if(a.isComplete()==true){
                        values.get(taskRepo.findByTid(a.getTid()).getTasktype()-1)[0]++;
                    }
                    values.get(taskRepo.findByTid(a.getTid()).getTasktype()-1)[1]++;
                }
                returnobj.add(new ListObj(1,values.get(0)[0],values.get(0)[1]));
                returnobj.add(new ListObj(2,values.get(1)[0],values.get(1)[1]));
                returnobj.add(new ListObj(3,values.get(2)[0],values.get(2)[1]));
                return returnobj;
            }
            else throw new Exception("Not assicible by admin");
        }catch (Exception e){
            throw e;
        }
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class ListObj{
    private int ttype,compl,assig;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class AskObj{
    private int did,pid;
    private String note;
}
