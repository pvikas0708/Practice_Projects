package in.vikas.emailutils;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailsender; // class for sending mails

	public boolean sendEmail(File file) {

		boolean status = false;
		
		try {
			
			MimeMessage msg=mailsender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(msg, true);
			//MimeMessageHelper used for setting the details to MimeMessageobject

			helper.setTo("pvikas0708@gmail.com");
			helper.setSubject("Your Report is Ready...!");
			helper.setText("<h2>Please Download Your Report..!</h2>", true); //setText(boolean html)
			
			helper.addAttachment(file.getName(), file); //select file format
			
			mailsender.send(msg);
			
			status=true;
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return status;
	}
}
