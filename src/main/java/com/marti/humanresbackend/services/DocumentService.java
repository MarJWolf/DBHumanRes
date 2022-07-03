package com.marti.humanresbackend.services;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.marti.humanresbackend.models.entities.*;
import com.marti.humanresbackend.repositories.CompanyInfoRepository;
import com.marti.humanresbackend.repositories.DocumentRepository;
import com.marti.humanresbackend.repositories.JobTitleRepository;
import com.marti.humanresbackend.repositories.WorkplaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final UserService userService;
    private final WorkLeaveService workLeaveService;
    private final DocumentRepository documentRep;
    private final JobTitleRepository jobRep;
    private final CompanyInfoRepository compRep;
    private final TemplateEngine templateEngine;

    private final DateTimeFormatter dtf =  DateTimeFormatter.ofPattern("dd-MM-yyyy");


    public byte[] getWorkleavePdf(Long workleaveId, Boolean createAgain) {
        if(!documentRep.existsByWorkleaveId(workleaveId)){
            documentRep.save(new Document(createWorkleavePDF(workleaveId), workleaveId));
        }
        else if(createAgain){
            Document document = documentRep.findByWorkleaveId(workleaveId);
            document.setDocument(createWorkleavePDF(workleaveId));
            documentRep.save(document);
        }
        return documentRep.findByWorkleaveId(workleaveId).getDocument();
    }

    private byte[] createWorkleavePDF(Long workleaveId){
        WorkLeave wl = workLeaveService.getById(workleaveId);

        User u = userService.getUserById(wl.getUserId());
        CompanyInfo company = compRep.getById(1L);
        int daysOff = workLeaveService.getBusinessDays(wl);
        String startDate = wl.getStartDate().format(dtf);
        String endDate = wl.getEndDate().format(dtf);
        String fillDate = wl.getFillDate().format(dtf);
        String jobTitle = jobRep.getById(u.getJobTitleId()).getJobTitle();



        Context context = new Context(Locale.getDefault());
        context.setVariable("user",u);
        context.setVariable("company",company);
        context.setVariable("type",wl.getType());
        context.setVariable("daysOff",daysOff);
        context.setVariable("startDate",startDate);
        context.setVariable("endDate",endDate);
        context.setVariable("fillDate",fillDate);
        context.setVariable("jobTitle",jobTitle);


        String workLeaveHtml = templateEngine.process("work_leave", context);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        HtmlConverter.convertToPdf(workLeaveHtml,pdfDocument,new ConverterProperties());

        return byteArrayOutputStream.toByteArray();
    }


}
