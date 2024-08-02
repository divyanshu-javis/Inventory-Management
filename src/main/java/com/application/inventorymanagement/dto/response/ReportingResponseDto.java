package com.application.inventorymanagement.dto.response;

import com.application.inventorymanagement.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportingResponseDto {

    private List<ProductDto> productList;
//    private String errorMessage;

}
