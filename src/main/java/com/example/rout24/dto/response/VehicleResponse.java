package com.example.rout24.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse {

    private String plateNumber;
    private String name;
    private String type;
    private List<String> images;
}
