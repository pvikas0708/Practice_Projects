package in.vikas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.vikas.binding.SearchCriteria;
import in.vikas.entity.CitizenPlan;
import in.vikas.service.CitizenPlanService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CitizenPlanController {

	// we need to inject service here
	@Autowired
	private CitizenPlanService service;

	@GetMapping("/")
	public String index(Model model) {// if you want to send data to index we need to take Model object

		formInit(model);

		model.addAttribute("search", new SearchCriteria());

		return "index";
	}

	//this method will be used to load the data for drop down and store into model object
	private void formInit(Model model) {
		List<String> planNames = service.getPlanNames();
		List<String> planStatus = service.getPlanStatus();

		model.addAttribute("planNames", planNames);
		model.addAttribute("planStatus", planStatus);
	}
	
	@PostMapping("/filterData")
	public String handleSearchBtn(@ModelAttribute("search") SearchCriteria criteria,Model model)
	{
		System.out.println(criteria);
		
		List<CitizenPlan> citizensInfo = service.searchCitizens(criteria);
		model.addAttribute("citizens", citizensInfo);
		formInit(model);
		
		return "index";
	}
	
	@GetMapping("/excel")
	public void downloadExcel(HttpServletResponse response) throws Exception
	{
		response.setContentType("application/octet-stream");
		
		String headerKey="content-Disposition"; // we dont want any page to display we want download file on same page
		String headerValue="attachment;filename=data.xls";
		
		response.addHeader(headerKey, headerValue);
		
		service.generateExcel(response);
	}
	@GetMapping("/pdf")
	public void downloadPdf(HttpServletResponse response) throws Exception
	{
		response.setContentType("application/pdf");
		
		String headerKey="content-Disposition"; // we dont want any page to display we want download file on same page
		String headerValue="attachment;filename=data.pdf";
		
		response.addHeader(headerKey, headerValue);
		
		service.generatePdf(response);
	}

}
