package in.vikas.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.vikas.entity.CitizenPlan;

public interface CitizenPlanRepo extends JpaRepository<CitizenPlan, Integer> {
	//custome HQL query
	@Query("select distinct(planName) from CitizenPlan") //sql query=>select distinct(Plan_status) from Citizen_plan;
	public List<String> getPlanNames();

	@Query("select distinct(planStatus) from CitizenPlan")
	public List<String> getPlanStatus();
}
