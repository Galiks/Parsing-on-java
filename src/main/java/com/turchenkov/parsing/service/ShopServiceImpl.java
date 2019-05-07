package com.turchenkov.parsing.service;

import com.turchenkov.parsing.domains.shop.Shop;
import com.turchenkov.parsing.parsingmethods.ParserInterface;
import com.turchenkov.parsing.repository.ShopRepository;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    private static final Logger log = Logger.getLogger(ShopServiceImpl.class);

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    private List<ParserInterface> parsers;

    @Override
    public void parsingAndSaveInDB() {
        for (ParserInterface parser : this.parsers) {
            for (Shop shop : parser.parsing()) {
              if (shop != null){
                  shopRepository.save(shop);
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
        int i = 1;
        for (Shop shop : getListOfShop()) {
            if (shop!=null){
                Row row = sheet.createRow(i);
                Cell name = row.createCell(0);
                Cell discount = row.createCell(1);
                Cell label = row.createCell(2);
                Cell url= row.createCell(3);
                Cell image = row.createCell(4);
                name.setCellValue(shop.getName());
                discount.setCellValue(shop.getDiscount());
                label.setCellValue(shop.getLabel());
                url.setCellValue(shop.getPageOnTheSite());
                image.setCellValue(shop.getImage());
            }
            i++;
        }
        try {
            for(int columnPosition = 0; columnPosition < 7; columnPosition++) {
                sheet.autoSizeColumn((short) (columnPosition));
            }
            book.write(new FileOutputStream("shops.xls"));
            book.close();
        } catch (IOException e) {
            log.error(e);
        }
    }

    @Override
    public void saveInCSVFile() throws IOException {
        FileWriter csvWriter = new FileWriter("shopsOnCSV.csv");
        for (Shop shop : getListOfShop()) {
            String discount = String.valueOf(shop.getDiscount());
            csvWriter.append(shop.getName()).append(", ")
                    .append(discount).append(" ")
                    .append(shop.getLabel()).append(", ")
                    .append(shop.getPageOnTheSite()).append(", ")
                    .append(shop.getImage()).append("\n");
        }
        csvWriter.flush();
        csvWriter.close();
    }


}
