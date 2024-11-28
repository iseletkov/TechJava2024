package com.example.toolsleasing.controllers;

import com.example.toolsleasing.model.CTool;
import com.example.toolsleasing.repositories.IRepositoryTools;
import com.example.toolsleasing.services.CServiceReport;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tools")
public class CControllerTools {

    @Autowired
    private IRepositoryTools repositoryTools;

    @Autowired
    private CServiceReport serviceReport;

    @GetMapping
    public List<CTool> getAll() {
        return repositoryTools.findAll();
    }

    @GetMapping("/{id}") // /tools/100500
    //@GetMapping   /tools?id=100500,name=aodawdnaodn
    // @RequestParam
    public Optional<CTool> getById(@PathVariable Long id) {
        return repositoryTools.findById(id);
    }

    @PostMapping
    public CTool create(@RequestBody CTool tool) {
        return repositoryTools.save(tool);
    }

    @PutMapping("/{id}")
    public CTool update(
            @PathVariable Long id,
            @RequestBody CTool student)
    {
        return repositoryTools.save(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repositoryTools.deleteById(id);
    }

    //@PostMapping(params = "file", consumes = {"*/*"})
    @PostMapping(value = "/upload", consumes = {"*/*"})
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file
//                                   RedirectAttributes redirectAttributes
    ) {
        try{
            Workbook wb = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            //HSSF - xls
            //XSSF - xlsx
            int rows = sheet.getLastRowNum();
            Row row;
            long id;
            String name;
            double price;
            CTool tool;
            for (int i=1; i<=rows; i++)
            {
                row = sheet.getRow(i);
                if (row==null)
                    continue;

                id = (long)(row.getCell(0).getNumericCellValue());
                name = row.getCell(1).getStringCellValue();
                price = row.getCell(2).getNumericCellValue();

                tool = new CTool(name, price);

                repositoryTools.save(tool);
            }
            repositoryTools.flush();
        }
        catch (IOException e)
        {
            return "Ошибка!"+e.getMessage();
        }

        return "Загружено!";
    }
//    https://stackoverflow.com/questions/51684550/how-to-download-an-excel-file-in-spring-restcontroller
    @GetMapping(value = "/report")
    public ResponseEntity<ByteArrayResource> report()
    {
        byte[] report = serviceReport.createReport(); // creates the workbook
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Report.docx");
        if (report.length>0)
            return new ResponseEntity<>(new ByteArrayResource(report),
                header, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
