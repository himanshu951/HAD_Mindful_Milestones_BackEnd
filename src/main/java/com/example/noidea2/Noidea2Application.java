package com.example.noidea2;

//import com.example.noidea2.model.admin.Admin;
//import com.example.noidea2.repo.admin.AdminRepo;
import com.example.noidea2.model.auth.Creds;
import com.example.noidea2.model.doc.DocDetails;
import com.example.noidea2.model.task.AssignedTask;
import com.example.noidea2.model.task.Task;
import com.example.noidea2.repo.auth.CredsRepo;
import com.example.noidea2.repo.doc.DocRepo;
import com.example.noidea2.repo.task.AssignedTaskRepo;
import com.example.noidea2.repo.task.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class Noidea2Application {
	@Autowired
	private TaskRepo taskRepo;
	@Autowired
	private CredsRepo credsRepo;
	@Autowired
	private DocRepo docRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AssignedTaskRepo assignedTaskRepo;

//	@Autowired
//	private AdminRepo adminRepo;
//
	@PostConstruct
	public void initAdmin(){
		Creds a=new Creds(1,"admin","admin@admin.com",passwordEncoder.encode("admin"),0);
		credsRepo.save(a);
		Creds doc1= new Creds(2,"Doctor1","langutajamru@gmail.com", passwordEncoder.encode("123"), 1 );
		credsRepo.save(doc1);
		DocDetails doc1detail= new DocDetails(2,"Doctor1","9898574839",new Date(),"langutajamru@gmail.com","LIC4356","MBBS, MS","Mental health");
		docRepo.save(doc1detail);
	}

	@PostConstruct
	public void initTask(){
//		Creds a=new Creds(1,"admin","admin@admin.com","admin",0);
		List<Task> tasks= Stream.of(
				new Task(1,1,null,"Run for 30 mins"),
				new Task(2,1,null,"Lift Weights"),
				new Task(3,1,null,"Talk to a friend"),
				new Task(4,1,null,"Meditate for 15 mins"),
				new Task(10,1,null,"Surya Namaskar 3 times"),
				new Task(11,1,null,"Perform Breating Exercise"),

				new Task(5,2,"https://www.youtube.com/embed/rCSCPujLs14","Deep Sleep"),
//				new Task(6,2,"https://www.youtube.com/embed/iz0yM5YoIow","How to stay happy"),
//				new Task(7,2,"https://www.youtube.com/embed/HwLK9dBQn0g","The Power of Postitivity"),
//				new Task(8,2,"https://www.youtube.com/embed/XqdDMNExvA0","How I Organize My Busy Schedule"),

				new Task(6,3,"https://www.scotthyoung.com/blog/2007/07/29/what-do-you-want-to-do-with-your-life/","What Do You Want to Do With Your Life?"),
				new Task(7,3,"https://www.scotthyoung.com/blog/2008/02/19/walk-your-talk-one-step-at-a-time/","Walk Your Talk… One Step at a Time"),
				new Task(8,3,"https://timesofindia.indiatimes.com/readersblog/sayantantoiblogs/benefits-of-staying-fit-and-healthy-39311/","Benefits of staying fit and healthy"),
				new Task(9,3,"https://newsinhealth.nih.gov/2018/03/creating-healthy-habits","Creating Healthy Habits")
		).collect(Collectors.toList());
		taskRepo.saveAll(tasks);
	}

	@PostConstruct
	public void setfalseintask(){
		List<AssignedTask> t=assignedTaskRepo.findAll();
		for (AssignedTask a : t) {
			Date current= new Date();
			if(a.getAssigneddate().before(current)) {
				AssignedTask te=a;
				te.setComplete(false);
				assignedTaskRepo.save(te);
			}
		}
	}
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedOrigins("*");
//			}
//		};
//	}
//	@PostConstruct
//	public void initUser(){
//		List<DocCreds> users= Stream.of(
//				new DocCreds(101,"santhil","santhil@gmail.com","123"),
//				new DocCreds(102,"doctor1","doctor1@gmail.com","123")
//		).collect(Collectors.toList());
//		docCredsRepo.saveAll(users);
//	}
	public static void main(String[] args) {
		SpringApplication.run(Noidea2Application.class, args);
	}

}
