package com.project.serviceImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.dto.EmailDetails;
import com.project.entity.Transaction;
import com.project.entity.User;
import com.project.repository.TransactionRepository;
import com.project.repository.UsersRepository;
import com.project.service.EmailService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BankStatement {
	
	
	private TransactionRepository transactionRepository;
	
	private UsersRepository usersRepository;
	
	private EmailService emailService;
	
	private static final String FILE = "C:\\Users\\Bhargavi\\OneDrive\\Documents\\GeneratedBankStatements\\MyStatement.pdf";
	
	 private static final Logger log = LoggerFactory.getLogger(BankStatement.class);
	
	
	
	/* Retrieve List of transactions within a date range given an account number
	 * Generate a PDF file of transactions
	 * Send the file via email
	 */
	
	/*public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException{
		
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

		List<Transaction> transactionList = transactionRepository.findAll().stream()
				            .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
				            .filter(transaction -> !transaction.getCreatedAt().isBefore(start) 
				            		&& !transaction.getCreatedAt().isAfter(end)).toList();
		
		System.out.println("------------" + transactionList);
		
		User user = usersRepository.findByAccountNumber(accountNumber);
		String accountName = user.getFirstName() + " " + user.getLastName();
		
		Rectangle statementSize = new Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("Setting size of the Document");
		
		FileOutputStream outputstream = new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputstream);
		document.open();
		
		PdfPTable bankInfotable = new PdfPTable(12);
		PdfPCell bankName = new PdfPCell(new Phrase("Ponnamanda's Bank Application"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.YELLOW);
		bankName.setPadding(20f);
		
		PdfPCell bankAddress = new PdfPCell(new Phrase("72, Some Address, Bhargavi"));
		bankAddress.setBorder(0);
		bankInfotable.addCell(bankName);
		bankInfotable.addCell(bankAddress);
		
		PdfPTable statementInfo = new PdfPTable(2);
		PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
		customerInfo.setBorder(0);
		
		PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);
		
		PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
		stopDate.setBorder(0);
		
		PdfPCell name = new PdfPCell(new Phrase("CustomerName: " + accountName));
		name.setBorder(0);
		
		PdfPCell space = new PdfPCell();
		
		PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
		address.setBorder(0);
		
		PdfPTable transactionTable = new PdfPTable(4);
		PdfPCell date = new PdfPCell(new Phrase("Date"));
		date.setBackgroundColor(BaseColor.MAGENTA);
		date.setBorder(0);
		
		PdfPCell transactionType = new PdfPCell(new Phrase("Transaction Type"));
		transactionType.setBackgroundColor(BaseColor.BLUE);
		transactionType.setBorder(0);
		
		PdfPCell transactionAmount = new PdfPCell(new Phrase("Transaction Amount"));
		transactionAmount.setBackgroundColor(BaseColor.BLUE);
		transactionAmount.setBorder(0);
		
		
		PdfPCell status = new PdfPCell(new Phrase("Status"));
		status.setBackgroundColor(BaseColor.BLUE);
		status.setBorder(0);
		
		transactionTable.addCell(date);
		transactionTable.addCell(transactionType);
		transactionTable.addCell(transactionAmount);
		transactionTable.addCell(status);
		
		transactionList.forEach(transaction -> {
			transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionTable.addCell(new Phrase(transaction.getTransactionType().toString()));
			transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionTable.addCell(new Phrase(transaction.getStatus().toString()));
			

		});
		
		statementInfo.addCell(customerInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(endDate);
		statementInfo.addCell(name);
		statementInfo.addCell(space);
		statementInfo.addCell(address);
		
		document.add(bankInfotable);
		document.add(statementInfo);
		document.add(transactionTable);
		
		document.close();
		transactionList.forEach(transaction -> System.out.println("========================="+transaction));
		return transactionList;
	} 
	*/
	
	 public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate)
	         throws DocumentException, MalformedURLException, IOException {

	     LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
	     LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

	     // Filter transactions by account number and date range
	     List<Transaction> transactionList = transactionRepository.findAll().stream()
	             .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
	             .filter(transaction -> {
	                 LocalDate createdAt = transaction.getCreatedAt();
	                 return !createdAt.isBefore(start) && !createdAt.isAfter(end);
	             })
	             .toList();

	     User user = usersRepository.findByAccountNumber(accountNumber);
	     String accountName = user.getFirstName() + " " + user.getLastName();

	     // Fonts
	     Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
	     Font headerFont = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC, 16, BaseColor.WHITE);
	     Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
	     Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.DARK_GRAY);
	     Font addressFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, BaseColor.GRAY);
	     Font captionFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY);

	     // Document setup
	     Rectangle statementSize = PageSize.A4;
	     Document document = new Document(statementSize, 36, 36, 36, 36); // margins: left, right, top, bottom
	     FileOutputStream outputStream = new FileOutputStream(FILE);
	     PdfWriter.getInstance(document, outputStream);
	     document.open();

	     // Title
	     Paragraph title = new Paragraph("STATEMENT OF ACCOUNT", titleFont);
	     title.setAlignment(Element.ALIGN_CENTER);
	     title.setSpacingAfter(20f);
	     document.add(title);

	     // Outer Table: 2 columns (Logo | Text)
	     PdfPTable bankInfoTable = new PdfPTable(2);
	     bankInfoTable.setWidthPercentage(100);
	     bankInfoTable.setWidths(new float[]{1f, 4f});

	     // Logo Image
	     Image logo = Image.getInstance(getClass().getClassLoader().getResource("logo.png"));
	     logo.scaleToFit(60, 60);
	     logo.setAlignment(Image.ALIGN_LEFT);

	     PdfPCell logoCell = new PdfPCell(logo);
	     logoCell.setBorder(Rectangle.NO_BORDER);
	     logoCell.setRowspan(3); // spans all 3 rows on the left
	     logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	     logoCell.setPadding(5f);

	     // Bank Name Cell
	     PdfPCell bankNameCell = new PdfPCell(new Phrase("Ponnamanda's Bank Application", headerFont));
	     bankNameCell.setBackgroundColor(BaseColor.BLUE);
	     bankNameCell.setBorder(Rectangle.NO_BORDER);
	     bankNameCell.setPaddingBottom(3f);
	     bankNameCell.setVerticalAlignment(Element.ALIGN_BOTTOM);

	     // Caption Cell
	     PdfPCell captionCell = new PdfPCell(new Phrase("Mee Dabbu, maa Samrakshna", captionFont));
	     captionCell.setBorder(Rectangle.NO_BORDER);
	     captionCell.setPaddingTop(0f);
	     captionCell.setPaddingBottom(3f);

	     // Address Cell
	     PdfPCell bankAddressCell = new PdfPCell(new Phrase("72, Bhimavaram", addressFont));
	     bankAddressCell.setBorder(Rectangle.NO_BORDER);
	     bankAddressCell.setPaddingTop(0f);
	     bankAddressCell.setPaddingBottom(10f);

	     // Add cells to the table
	     bankInfoTable.addCell(logoCell);
	     bankInfoTable.addCell(bankNameCell);
	     bankInfoTable.addCell(captionCell);
	     bankInfoTable.addCell(bankAddressCell);

	     bankInfoTable.setSpacingAfter(20f);
	     document.add(bankInfoTable);



	     // Customer Info Table
	     PdfPTable customerInfoTable = new PdfPTable(2);
	     customerInfoTable.setWidthPercentage(100);
	     customerInfoTable.setSpacingAfter(20f);

	     customerInfoTable.addCell(createCell("Customer Name:", labelFont));
	     customerInfoTable.addCell(createCell(accountName, valueFont));

	     customerInfoTable.addCell(createCell("Account Number:", labelFont));
	     customerInfoTable.addCell(createCell(accountNumber, valueFont));

	     customerInfoTable.addCell(createCell("Start Date:", labelFont));
	     customerInfoTable.addCell(createCell(startDate, valueFont));

	     customerInfoTable.addCell(createCell("End Date:", labelFont));
	     customerInfoTable.addCell(createCell(endDate, valueFont));

	     customerInfoTable.addCell(createCell("Address:", labelFont));
	     customerInfoTable.addCell(createCell(user.getAddress(), valueFont));

	     document.add(customerInfoTable);

	     // Transaction Table Header
	     PdfPTable transactionTable = new PdfPTable(new float[]{2f, 3f, 2f, 2f});
	     transactionTable.setWidthPercentage(100);
	     transactionTable.setSpacingBefore(10f);

	     transactionTable.addCell(createHeaderCell("Date"));
	     transactionTable.addCell(createHeaderCell("Transaction Type"));
	     transactionTable.addCell(createHeaderCell("Amount"));
	     transactionTable.addCell(createHeaderCell("Status"));

	     // Transaction Rows
	     for (Transaction transaction : transactionList) {
	         transactionTable.addCell(createCell(transaction.getCreatedAt().toString(), valueFont));
	         transactionTable.addCell(createCell(transaction.getTransactionType().toString(), valueFont));
	         transactionTable.addCell(createCell(transaction.getAmount().toString(), valueFont));
	         transactionTable.addCell(createCell(transaction.getStatus().toString(), valueFont));
	     }

	     document.add(transactionTable);
	     document.close();

	     System.out.println("PDF generated with " + transactionList.size() + " transactions.");
	     
	     EmailDetails emailDetails = EmailDetails.builder()
	    		                     .recipient(user.getEmail())
	    		                     .subject("STATEMENT OF ACCOUNT")
	    		                     .messageBody("Kindly find your requested account statement attached!")
	    		                     .attachment(FILE)
	    		                     .build();
	     emailService.sendEmailAttachment(emailDetails);
	     
	     return transactionList;
	 }
	 
	 //Helper Methods
	 private PdfPCell createCell(String text, Font font) {
		    PdfPCell cell = new PdfPCell(new Phrase(text, font));
		    cell.setPadding(6f);
		    cell.setBorder(Rectangle.NO_BORDER);
		    return cell;
		}

		private PdfPCell createHeaderCell(String text) {
		    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
		    PdfPCell cell = new PdfPCell(new Phrase(text, headerFont));
		    cell.setBackgroundColor(BaseColor.DARK_GRAY);
		    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    cell.setPadding(8f);
		    return cell;
		}


}
