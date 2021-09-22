package ch.eddjos.qualitool.goups;

import ch.eddjos.qualitool.checkboxes.Checkbox;
import ch.eddjos.qualitool.person.Person;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelWriter {
    private static final String[] columns={"Level 1","Level 2","Level 3","Level 4","Level 5","Markiert","Block","Autor","Beobachtung"};

    private Workbook workbook;
    private Sheet sheet;
    private CellStyle textCellStyle;
    public ExcelWriter(String name){
        workbook=new XSSFWorkbook();

        CreationHelper createHelper = workbook.getCreationHelper();

        sheet=workbook.createSheet(name);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        textCellStyle = workbook.createCellStyle();
        textCellStyle.setWrapText(true);



        rowNumber=1;
    }

    private int rowNumber=0;
    public void add(Checkbox cb1,Checkbox cb2, Checkbox cb3,Checkbox cb4,Checkbox cb5,Person pe, ch.eddjos.qualitool.comments.Comment com){
        Row row= sheet.createRow(rowNumber++);
        if(cb1!=null) {
            row.createCell(0).setCellValue(cb1.getName());
        }else{
            row.createCell(0).setCellValue("");
        }
        if(cb2!=null) {
            row.createCell(1).setCellValue(cb2.getName());
        }else{
            row.createCell(1).setCellValue("");
        }
        if(cb3!=null) {
            row.createCell(2).setCellValue(cb3.getName());
        }else{
            row.createCell(2).setCellValue("");
        }
        if(cb4!=null) {
            row.createCell(3).setCellValue(cb4.getName());
        }else{
            row.createCell(3).setCellValue("");
        }
        if(cb5!=null) {
            row.createCell(4).setCellValue(cb5.getName());
        }else{
            row.createCell(4).setCellValue("");
        }

        row.createCell(5).setCellValue(com.isStared());
        row.createCell(6).setCellValue(com.getBlock().getName());
        row.createCell(7).setCellValue(com.getUser().getUsername());
        Cell cell=row.createCell(8);
        cell.setCellValue(com.getText());
        cell.setCellStyle(textCellStyle);


    }

    public byte[] create(){
        for(int i = 0; i < columns.length-1; i++) {
            sheet.autoSizeColumn(i);
        }
        sheet.setColumnWidth(8, 20000);

        //InputStreamResource res=null;
        ByteArrayOutputStream bout=null;
        try {
            //FileOutputStream fileOut = new FileOutputStream("poi-generated-file.xlsx");
            //PipedOutputStream out = new PipedOutputStream();
            //PipedInputStream in = new PipedInputStream(out);
            //res = new InputStreamResource(in);

            bout=new ByteArrayOutputStream();

            workbook.write(bout);
            bout.close();
            workbook.close();

        }catch(IOException e){
            e.printStackTrace();
        }
        //return res;
        return bout.toByteArray();
    }


}
