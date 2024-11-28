package com.untracktion.controller;

import com.untracktion.models.ModelInterrupt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/untracktion")
public class ControlInterrupt {

    private List<ModelInterrupt> interrupts = new ArrayList<>();

    public ControlInterrupt() {
        interrupts.add(new ModelInterrupt("Network latency issue", 4000, "Network latency spinner"));
        interrupts.add(new ModelInterrupt("Server overload", 6000, "Overload screen loop"));
        interrupts.add(new ModelInterrupt("Unexpected downtime", 8000, "Unexpected alert"));
    }

    @PostMapping("/interrupts")
    public ResponseEntity<String> addInterrupt(@RequestBody ModelInterrupt interrupt) {
        interrupts.add(interrupt);
        return new ResponseEntity<>("Interrupt added successfully", HttpStatus.CREATED);
    }

    @GetMapping("/interrupts")
    public ResponseEntity<List<ModelInterrupt>> getInterrupts() {
        return new ResponseEntity<>(interrupts, HttpStatus.OK);
    }

    @GetMapping("/interrupts/{method}")
    public ResponseEntity<ModelInterrupt> getInterruptByMethod(@PathVariable String method) {
        Optional<ModelInterrupt> interrupt = interrupts.stream()
                .filter(i -> i.getMethod().equalsIgnoreCase(method))
                .findFirst();

        if (interrupt.isPresent()) {
            return new ResponseEntity<>(interrupt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/interrupts/{method}")
    public ResponseEntity<String> deleteInterrupt(@PathVariable String method) {
        Optional<ModelInterrupt> interruptToDelete = interrupts.stream()
                .filter(i -> i.getMethod().equalsIgnoreCase(method))
                .findFirst();

        if (interruptToDelete.isPresent()) {
            interrupts.remove(interruptToDelete.get());
            return new ResponseEntity<>("Interrupt deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Interrupt not found", HttpStatus.NOT_FOUND);
    }
}