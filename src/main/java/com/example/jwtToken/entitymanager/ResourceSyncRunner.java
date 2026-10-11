package com.example.jwtToken.entitymanager;

import com.example.jwtToken.resource.Resource;
import com.example.jwtToken.resource.ResourceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResourceSyncRunner {

    private final ApplicationEntityScannerService scannerService;
    private final ResourceRepository resourceRepository;

    @PostConstruct
    public void syncEntitiesToDatabase() {
        List<String> appEntities = scannerService.getAllApplicationEntities();

        // اضافه کردن رکورد ویژه * اگر وجود نداشت
        if (!resourceRepository.existsByName("ALL")) {
            resourceRepository.save(Resource.builder().name("ALL").build());
        }

        // اضافه کردن انتی‌تی‌های جدید به دیتابیس
        for (String entityName : appEntities) {
            if (!resourceRepository.existsByName(entityName)) {
                resourceRepository.save(Resource.builder().name(entityName).build());
            }
        }
    }
}