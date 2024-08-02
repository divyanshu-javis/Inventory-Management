package com.application.inventorymanagement.service;

import com.application.inventorymanagement.dto.InventoryDto;
import com.application.inventorymanagement.dto.request.ConsumptionRequestDto;
import com.application.inventorymanagement.dto.request.ProductionRequestDto;
import com.application.inventorymanagement.dto.response.ConsumptionResponseDto;
import com.application.inventorymanagement.dto.response.ProductionResponseDto;
import com.application.inventorymanagement.exception.InvalidInputException;
import com.application.inventorymanagement.exception.ResourceNotFoundException;
import com.application.inventorymanagement.mapper.InventoryMapper;
import com.application.inventorymanagement.mapper.ProductMapper;
import com.application.inventorymanagement.model.Inventory;
import com.application.inventorymanagement.model.Product;
import com.application.inventorymanagement.repository.InventoryRepository;
import com.application.inventorymanagement.repository.ProductRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class DataProcessingService {

    private final StockManagementService stockManagementService;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper = new InventoryMapper();

    public DataProcessingService(StockManagementService stockManagementService, ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.stockManagementService = stockManagementService;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public ConsumptionResponseDto processConsumptionFile(MultipartFile consumptionFile) {
        ConsumptionResponseDto consumptionResponseDto = new ConsumptionResponseDto();
        List<ConsumptionRequestDto> consumptionRequestList;
        try{
            consumptionRequestList = this.parseConsumptionFile(consumptionFile);

            for(ConsumptionRequestDto consumptionRequestDto : consumptionRequestList){
                InventoryDto reduceRequest = new InventoryDto(consumptionRequestDto.getProductName(), consumptionRequestDto.getQuantity());
                try{
//                    InventoryDto reduceResult =
                    stockManagementService.reduceStock(reduceRequest);
                    consumptionResponseDto.getConsumptionList().add(inventoryMapper.toInventoryDto(consumptionRequestDto));
                }
                catch(ResourceNotFoundException e){
                    consumptionResponseDto.getProductsNotAvailable().add(consumptionRequestDto.getProductName());
                }
                catch(InvalidInputException e){
                    consumptionResponseDto.getNotEnoughProducts().add(consumptionRequestDto.getProductName());
                }
            }
            return consumptionResponseDto;
        }
        catch (IOException | IllegalArgumentException e){
            e.printStackTrace();
            consumptionResponseDto.setErrorMessage(e.getMessage());
            return consumptionResponseDto;
        }

    }

    public ProductionResponseDto processProductionFile(MultipartFile productionFile) {
        ProductionResponseDto productionResponseDto = new ProductionResponseDto();

        List<ProductionRequestDto> productionRequestList;
        try{
            productionRequestList = this.parseProductionFile(productionFile);

            for(ProductionRequestDto productionRequestDto : productionRequestList){
                InventoryDto addRequest = new InventoryDto(productionRequestDto.getProductName(), productionRequestDto.getQuantity());

                try{
                    InventoryDto addResult = stockManagementService.addStock(addRequest);
                    productionResponseDto.getProductsUpdated().add(addResult);
                }
                catch(ResourceNotFoundException e){

                    Product newProduct = new Product();
                    newProduct.setName(productionRequestDto.getProductName());
                    newProduct.setDescription(productionRequestDto.getDescription());
                    newProduct.setPrice(productionRequestDto.getPrice());
                    newProduct.setCategory(productionRequestDto.getCategory());
                    newProduct.setSupplier(productionRequestDto.getSupplier());

                    Inventory newInventory = new Inventory();
                    newInventory.setProduct(newProduct);
                    newInventory.setQuantity(productionRequestDto.getQuantity());

                    productRepository.save(newProduct);
                    inventoryRepository.save(newInventory);

                    productionResponseDto.getProductsCreated().add(inventoryMapper.toInventoryDto(newInventory));
                }
            }

            return productionResponseDto;
        }
        catch (IOException | IllegalArgumentException e){
            e.printStackTrace();
            productionResponseDto.setErrorMessage(e.getMessage());
            return productionResponseDto;
        }
    }


    public List<ConsumptionRequestDto> parseConsumptionFile(MultipartFile consumptionFile) throws IOException {
        if(consumptionFile.isEmpty()){
            throw new IllegalArgumentException("File is empty");
        }

        List<ConsumptionRequestDto> consumptionRequestList = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(consumptionFile.getInputStream());

        Sheet sheet = workbook.getSheetAt(0);

        int noOfRows = sheet.getLastRowNum();
        int noOfColumns = sheet.getRow(0).getLastCellNum();

        ArrayList<String> headerList = new ArrayList<>();

        System.out.println(noOfRows);
        System.out.println(noOfColumns);

        boolean isHeader = true;

        for(Row row : sheet){
            if(isHeader){
                for(Cell cell : row){
                    headerList.add(cell.getStringCellValue());
                }
                isHeader = false;
                continue;
            }

            ConsumptionRequestDto consumptionRequestDto = getConsumptionRequestDto(row, headerList);
            consumptionRequestList.add(consumptionRequestDto);
        }
        return consumptionRequestList;
    }

    private static ConsumptionRequestDto getConsumptionRequestDto(Row row, ArrayList<String> headerList) {
        ConsumptionRequestDto consumptionRequestDto = new ConsumptionRequestDto();

        for(Cell cell : row){
            String cellName = headerList.get(cell.getColumnIndex());
            switch (cellName) {
                case "name":
                    consumptionRequestDto.setProductName(cell.getStringCellValue());
                    break;
                    case "quantity":
                        consumptionRequestDto.setQuantity((int)cell.getNumericCellValue());
                        break;
            }
        }
        return consumptionRequestDto;
    }


    public List<ProductionRequestDto> parseProductionFile(MultipartFile productionFile) throws IOException{

        if(productionFile.isEmpty()){
            throw new IllegalArgumentException("File is empty");
        }

        List<ProductionRequestDto> productionRequestList = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(productionFile.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        int noOfRows = sheet.getLastRowNum();
        int noOfColumns = sheet.getRow(0).getLastCellNum();

        System.out.println(noOfRows);
        System.out.println(noOfColumns);

        ArrayList<String> headerList = new ArrayList<>();


        boolean isHeader = true;

        for(Row row : sheet){
            if(isHeader){
                for(Cell cell : row){
                    headerList.add(cell.getStringCellValue());
                }
                isHeader = false;
                continue;
            }

            ProductionRequestDto productionRequestDto = getProductionRequestDto(row, headerList);
            productionRequestList.add(productionRequestDto);
        }
        return productionRequestList;
    }

    public static ProductionRequestDto getProductionRequestDto(Row row, ArrayList<String> headerList) {
        ProductionRequestDto productionRequestDto = new ProductionRequestDto();

        for(Cell cell : row){
            String cellName = headerList.get(cell.getColumnIndex());

            switch (cellName) {
                case "name":
                    productionRequestDto.setProductName(cell.getStringCellValue());
                    break;
                case "description":
                    productionRequestDto.setDescription(cell.getStringCellValue());
                    break;
                case "category":
                    productionRequestDto.setCategory(cell.getStringCellValue());
                    break;
                case "price":
                    productionRequestDto.setPrice(cell.getNumericCellValue());
                    break;
                case "supplier":
                    productionRequestDto.setSupplier(cell.getStringCellValue());
                    break;
                case "quantity":
                    productionRequestDto.setQuantity((int)cell.getNumericCellValue());
            }
        }
        return productionRequestDto;
    }


}
