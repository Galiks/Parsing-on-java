package com.turchenkov.parsing.service;

import com.opencsv.CSVWriter;
import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.ParserInterface;
import com.turchenkov.parsing.repository.ShopRepository;
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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    private static final Logger log = Logger.getLogger(ShopServiceImpl.class);

    private static final String filePathForShopsInCSV = "E:\\Документы\\GitHub\\Parsing-on-java\\src\\main\\java\\com\\turchenkov\\parsing\\files\\shopsOnCSV.csv";
    private static final String filePathForShopsInExcel = "E:\\Документы\\GitHub\\Parsing-on-java\\src\\main\\java\\com\\turchenkov\\parsing\\files\\shops.xls";
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    private List<ParserInterface> parsers;

    private ShopServiceImpl() {
        BasicConfigurator.configure();
    }

    @Override
    public void parsingAndSaveInDB() {
        for (ParserInterface parser : this.parsers) {
            for (Shop shop : parser.parsing()) {
                if (shop != null) {
                    try {
                        shopRepository.save(shop);
                    } catch (Exception e) {
                        log.error(e + " : " + shop);
                    }
                }
            }
        }
    }

    @Override
    public void deleteAllFromDB() {
        shopRepository.deleteAll();
    }

    @Override
    public List<Shop> getListOfShop() {
        return (List<Shop>) shopRepository.findAllByOrderByName();
    }

    @Override
    public List<Shop> update() {
        deleteAllFromDB();
        parsingAndSaveInDB();
        return getListOfShop();
    }

    @Override
    public List<Shop> orderByDiscount() {
        return (List<Shop>) shopRepository.findAllByOrderByDiscount();
    }

    @Override
    public List<Shop> orderByDiscountDesc() {
        return (List<Shop>) shopRepository.findAllByOrderByDiscountDesc();
    }

    @Override
    public void saveInExcelFile() {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Shops");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("НАЗВАНИЕ");
        row.createCell(1).setCellValue("ЗНАЧЕНИЕ КЭШБЭКА");
        row.createCell(2).setCellValue("ВАЛЮТА");
        row.createCell(3).setCellValue("КАРТИНКА МАГАЗИНА");
        row.createCell(4).setCellValue("АДРЕС МАГАЗИНА");
        int i = 1;
        for (Shop shop : getListOfShop()) {
            if (shop != null) {
                row = sheet.createRow(i);
                Cell name = row.createCell(0);
                Cell discount = row.createCell(1);
                Cell label = row.createCell(2);
                Cell image = row.createCell(3);
                Cell url = row.createCell(4);
                name.setCellValue(shop.getName());
                discount.setCellValue(shop.getDiscount());
                label.setCellValue(shop.getLabel());
                url.setCellValue(shop.getPageOnTheSite());
                image.setCellValue(shop.getImage());
            }
            i++;
        }
        try {
            for (int columnPosition = 0; columnPosition < 7; columnPosition++) {
                sheet.autoSizeColumn((short) (columnPosition));
            }
            book.write(new FileOutputStream(filePathForShopsInExcel));
            book.close();
        } catch (IOException e) {
            log.error(e);
        }
    }

    @Override
    public void saveInCSVFile() throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePathForShopsInCSV)) {
            Writer fileWriter = new OutputStreamWriter(fileOutputStream,
                    StandardCharsets.UTF_8);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            for (Shop shop : getListOfShop()) {
                String discount = String.valueOf(shop.getDiscount());
                String[] array = new String[]{shop.getName(), discount + " " + shop.getLabel(), shop.getPageOnTheSite(), shop.getImage()};
                csvWriter.writeNext(array);
            }
            csvWriter.flush();
            csvWriter.close();
        }
    }
}
