package com.turchenkov.parsing.service;

import com.turchenkov.parsing.domains.Timer;
import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.repository.TimerRepository;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class TimerServiceImpl implements TimerService {

    private static final Logger log = Logger.getLogger(TimerServiceImpl.class);

    @Autowired
    private TimerRepository timerRepository;

    public TimerServiceImpl() {
        BasicConfigurator.configure();
    }

    @Override
    public void saveInExcelFile() {
        Iterable<Timer> timers = timerRepository.findAll();
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Timer");
        int i = 1;
        for (Timer timer : timers) {
            if (timer!=null){
                Row row = sheet.createRow(i);
                Cell name = row.createCell(0);
                Cell time = row.createCell(1);
                Cell date = row.createCell(2);
                name.setCellValue(timer.getName());
                time.setCellValue(timer.getTime());
                date.setCellValue(timer.getDate());
            }
            i++;
        }
        try {
            for(int columnPosition = 0; columnPosition < 3; columnPosition++) {
                sheet.autoSizeColumn((short) (columnPosition));
            }
            book.write(new FileOutputStream("times.xls"));
            book.close();
        } catch (IOException e) {
            log.error(e);
        }
    }
}
