package in.vikas.service;

import java.io.IOException;
import java.util.List;

import in.vikas.binding.SearchCriteria;
import in.vikas.entity.CitizenPlan;
import jakarta.servlet.http.HttpServletResponse;

public interface CitizenPlanService {

	public List<String> getPlanNames();

	public List<String> getPlanStatus();

	public List<CitizenPlan> searchCitizens(SearchCriteria criteria);

	public void generateExcel(HttpServletResponse response) throws Exception;

	public void generatePdf(HttpServletResponse response) throws Exception;
}
