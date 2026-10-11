package com.example.jwtToken.entitymanager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationEntityScannerService {

    private final EntityManager entityManager;

    /**
     * این متد نام تمام کلاس‌های @Entity پروژه را برمی‌گرداند
     */
    public List<String> getAllApplicationEntities() {
        return entityManager.getMetamodel().getEntities().stream()
                .map(EntityType::getName)       // گرفتن نام انتی‌تی (مثلا Ticket)
                .map(String::toUpperCase)       // تبدیل به حروف بزرگ برای استانداردسازی (TICKET)
                .collect(Collectors.toList());
    }
}