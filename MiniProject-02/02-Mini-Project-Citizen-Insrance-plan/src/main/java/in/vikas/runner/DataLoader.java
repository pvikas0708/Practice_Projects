package in.vikas.runner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import in.vikas.entity.CitizenPlan;
import in.vikas.repo.CitizenPlanRepo;

@Component // to make it springBeans
public class DataLoader implements ApplicationRunner {

	// to insert some dummy data in table
	@Autowired
	private CitizenPlanRepo repo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
	
			repo.deleteAll(); // when server restarted it will delete previous record and will update new records
		
		CitizenPlan p1=
				new CitizenPlan("John", "john@gmail.com", 12341, 987651, "Male", "Cash Plan", "Approved", LocalDate.now(), LocalDate.now().plusMonths(6));	
		
		CitizenPlan p2=
				new CitizenPlan("Smith", "smith@gmail.com", 99999, 88888, "Male", "Cash Plan", "Denied", null,null);//for denial start date and end date will not be available.
		
		CitizenPlan p3=
				new CitizenPlan("cathy", "cathy@gmail.com", 77777, 6666, "feMale", "Food Plan", "Approved", LocalDate.now(), LocalDate.now().plusMonths(6));	
		
		CitizenPlan p4=
				new CitizenPlan("Jony", "jony@gmail.com", 11111, 22222, "FeMale", "Food Plan", "Denied", null,null);//for denial start date and end date will not be available.
		
		CitizenPlan p5=
				new CitizenPlan("Robert", "robert@gmail.com", 323232, 212121, "Male", "Medical Plan", "Approved", LocalDate.now(), LocalDate.now().plusMonths(6));		
		
		CitizenPlan p6=
				new CitizenPlan("Anny", "Anny@gmail.com", 1414141, 515151, "FeMale", "Medical Plan", "Denied", null,null);	//for denial start date and end date will not be available.
		
		List<CitizenPlan> records = Arrays.asList(p1,p2,p3,p4,p5,p6);
		
		repo.saveAll(records);
		
	}
}
