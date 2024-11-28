package com.untracktion.controller;

import com.untracktion.models.ModelSabotage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sabotages")
public class ControlSabotage {

    private List<ModelSabotage> sabotages = new ArrayList<>();

    // Constructor to initialize the sabotages list
    public ControlSabotage() {
        sabotages.add(new ModelSabotage("Simulated network failure", "NETWORK", true, true, 0.8));
        sabotages.add(new ModelSabotage("Database connection lost", "DATABASE", true, false, 0.5));
        sabotages.add(new ModelSabotage("Unexpected error occurred", "ERROR", false, true, 0.9));
    }

    @PostMapping
    public ResponseEntity<String> addSabotage(@RequestBody ModelSabotage sabotage) {
        sabotages.add(sabotage);
        return new ResponseEntity<>("Sabotage added successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ModelSabotage>> getSabotages() {
        return new ResponseEntity<>(sabotages, HttpStatus.OK);
    }

    @GetMapping("/{method}")
    public ResponseEntity<ModelSabotage> getSabotageByMethod(@PathVariable String method) {
        Optional<ModelSabotage> sabotage = sabotages.stream()
            .filter(s -> s.getMethod().equalsIgnoreCase(method))
            .findFirst();

        if (sabotage.isPresent()) {
            return new ResponseEntity<>(sabotage.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{method}")
    public ResponseEntity<String> deleteSabotage(@PathVariable String method) {
        Optional<ModelSabotage> sabotageToDelete = sabotages.stream()
            .filter(s -> s.getMethod().equalsIgnoreCase(method))
            .findFirst();

        if (sabotageToDelete.isPresent()) {
            sabotages.remove(sabotageToDelete.get());
            return new ResponseEntity<>("Sabotage deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Sabotage not found", HttpStatus.NOT_FOUND);
    }
}