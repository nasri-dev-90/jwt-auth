package com.example.jwtToken.initializedatabase;

import com.example.jwtToken.action.Action;
import com.example.jwtToken.action.ActionRepository;
import com.example.jwtToken.resource.ResourceRepository;
import com.example.jwtToken.resource.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final ActionRepository actionRepository;
    private final ResourceRepository resourceRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Checking if database needs to be seeded...");

        seedActions();
        seedResources();

        log.info("Database seeding process completed.");
    }

    private void seedActions() {
        // فقط در صورتی که جدول actions خالی باشد اجرا می‌شود
        if (actionRepository.count() == 0) {
            log.info("Seeding initial actions...");
            Action create = Action.builder().name("CREATE").build();
            Action display = Action.builder().name("DISPLAY").build();
            Action update = Action.builder().name("UPDATE").build();
            Action delete = Action.builder().name("DELETE").build();
            Action report = Action.builder().name("REPORT").build();
            Action all = Action.builder().name("ALL").build(); // برای دسترسی کامل

            // ذخیره به صورت دسته‌ای (Batch)
            actionRepository.saveAll(List.of(create, display, update, delete, all));
        }
    }

    private void seedResources() {
        // ایجاد Resource ویژه (Wildcard) برای دسترسی به "همه چیز"
        if (resourceRepository.count() == 0) {
            log.info("Seeding initial resources...");
            Resource allResources = Resource.builder().name("ALL").build();
            resourceRepository.save(allResources);
        }
    }


}