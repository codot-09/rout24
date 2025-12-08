package com.example.rout24.service;

import com.example.rout24.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final RouteRepository routeRepository;
}
