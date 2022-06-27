package com.marti.humanresbackend.controllers;

import com.marti.humanresbackend.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping(path = "workleave",produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getWorkleavePdf(@RequestParam Long workleaveId,
                                  @RequestParam(required = false, defaultValue ="false") Boolean createAgain){
        return documentService.getWorkleavePdf(workleaveId,createAgain);
    }


}
