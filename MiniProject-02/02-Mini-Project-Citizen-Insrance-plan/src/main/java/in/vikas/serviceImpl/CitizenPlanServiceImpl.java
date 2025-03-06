package in.vikas.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import in.vikas.binding.SearchCriteria;
import in.vikas.emailutils.EmailUtils;
import in.vikas.entity.CitizenPlan;
import in.vikas.repo.CitizenPlanRepo;
import in.vikas.service.CitizenPlanService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service //to make it spring bean
public class CitizenPlanServiceImpl implements CitizenPlanService{

	//VIDEO FOR PDF GENERATION:https://springjava.com/spring-boot/export-data-into-pdf-file-in-spring-boot/
	
	//service should talk to repository
	@Autowired
	private CitizenPlanRepo repo;
	
	//for Email Sending
	@Autowired
	private EmailUtils emailUtils;
	
	@Override
	public List<String> getPlanNames() {
		return repo.getPlanNames();
	}

	@Override
	public List<String> getPlanStatus() {
		return repo.getPlanStatus();
	}

	@Override
	public List<CitizenPlan> searchCitizens(SearchCriteria criteria) {
		
		CitizenPlan entity=new CitizenPlan();
		
		//below logic is for filter (data selected as criteria will be set to entity object)
		if(StringUtils.isNotBlank(criteria.getPlanName()))
		{
			entity.setPlanName(criteria.getPlanName());
		}

		if(StringUtils.isNotBlank(criteria.getPlanStatus()))
		{
			entity.setPlanStatus(criteria.getPlanStatus());
		}
		
		if(StringUtils.isNotBlank(criteria.getGender()))
		{
			entity.setGender(criteria.getGender());
		}
		
		if(null!=criteria.getPlanStartDate())
		{
			criteria.setPlanStartDate(criteria.getPlanStartDate());
		}
		
		if(null!=criteria.getPlanEndDate())
		{
			criteria.setPlanEndDate(criteria.getPlanEndDate());
		}
		
		//its Query By Example (QBE=> It is used to filter the data based on info provided in entity obj)
		Example<CitizenPlan> of = Example.of(entity);
		//when all the fields are null then it will prepare a query select * from table & will display all the table data
		
		return repo.findAll(of);
	}

	@Override
	public void generateExcel(HttpServletResponse response) throws Exception {
		
		List<CitizenPlan> records = repo.findAll();
		
		HSSFWorkbook workbook=new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Data");
		
		HSSFRow headerRow = sheet.createRow(0);
		
		//set data for header Row cell
		headerRow.createCell(0).setCellValue("Name");
		headerRow.createCell(1).setCellValue("Email");
		headerRow.createCell(2).setCellValue("Gender");
		headerRow.createCell(3).setCellValue("SSN");
		headerRow.createCell(4).setCellValue("PlanName");
		headerRow.createCell(5).setCellValue("PlanStatus");
		
		int rowIndex=1; // 1 because 0 index already allocated for headerRow
		
		for(CitizenPlan record: records)
		{
			HSSFRow dataRow = sheet.createRow(rowIndex);
			
			dataRow.createCell(0).setCellValue(record.getName());
			dataRow.createCell(1).setCellValue(record.getEmail());
			dataRow.createCell(2).setCellValue(record.getGender());
			dataRow.createCell(3).setCellValue(record.getSsn());
			dataRow.createCell(4).setCellValue(record.getPlanName());
			dataRow.createCell(5).setCellValue(record.getName());
			
			rowIndex ++;
		}
		
		//to send file in emial attachment
		File f=new File("data.xls"); //for email sending
		FileOutputStream fos=new FileOutputStream(f);
		workbook.write(fos);
		emailUtils.sendEmail(f);
		
		//below code for downloading file in browser
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		fos.close();
		
	}

	@Override
	public void generatePdf(HttpServletResponse response) throws Exception {
		List<CitizenPlan> records = repo.findAll();
		
		Document pdfDoc1=new Document(PageSize.A4); //for browser
		
		ServletOutputStream outputStream = response.getOutputStream();
		PdfWriter.getInstance(pdfDoc1, outputStream);
		pdfDoc1.open();
		
		Document pdfDoc2=new Document(PageSize.A4); //for email-attachment
		
		//for sending pdf on email
		File f=new File("data.pdf");
		FileOutputStream fos=new FileOutputStream(f);
		PdfWriter.getInstance(pdfDoc2, fos);
		pdfDoc2.open();
		
		
		//Code for paragraph creation
		Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fontTitle.setSize(20);
		Paragraph p=new Paragraph("Citizen Plans Info",fontTitle);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		
		pdfDoc1.add(p);
		pdfDoc2.add(p);

		//to create table header cells
		PdfPTable table=new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setWidths(new int[] {3,3,3,3,3,3});
		table.setSpacingBefore(5);
		
		
		PdfPCell cell=new PdfPCell();
		cell.setBackgroundColor(CMYKColor.BLUE);
		cell.setPadding(5);
		
		Font font=FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(CMYKColor.WHITE);
		
		cell.setPhrase(new Phrase("Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Email",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Gender",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("SSN",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Plan Name",font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Plan Status",font));
		table.addCell(cell);
		
		//to create table data cells
		List<CitizenPlan> records1 = repo.findAll();
		
		for(CitizenPlan record :records1)
		{
			table.addCell(record.getName());
			table.addCell(record.getEmail());
			table.addCell(record.getGender());
			table.addCell(String.valueOf(record.getSsn()));//ssn is long so converting
			table.addCell(record.getPlanName());
			table.addCell(record.getPlanStatus());
			
		}
			
		pdfDoc1.add(table);
		pdfDoc2.add(table);
				
		pdfDoc1.close();
		outputStream.close();
		
		pdfDoc2.close();
		fos.close();
		
		emailUtils.sendEmail(f); //this line must be after closing all the docs otherwise pdf will not hold data
	}

}
