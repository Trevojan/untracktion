package com.untracktion.controller;

import com.untracktion.models.ModelExcuse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/untracktion")
public class ControlExcuse {

    private final DatabaseReference excusesRef = FirebaseDatabase.getInstance().getReference("excuses");

    @GetMapping("/excuses")
    public CompletableFuture<ResponseEntity<List<ModelExcuse>>> getExcusesByCategory(@RequestParam String category) {
        List<ModelExcuse> excuses = new ArrayList<>();

        return CompletableFuture.supplyAsync(() -> {
            final CompletableFuture<ResponseEntity<List<ModelExcuse>>> future = new CompletableFuture<>();

            excusesRef.child("category").child(category).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot excuseSnapshot : dataSnapshot.getChildren()) {
                        ModelExcuse excuse = excuseSnapshot.getValue(ModelExcuse.class);
                        if (excuse != null) {
                            excuses.add(excuse);
                        }
                    }

                    if (excuses.isEmpty()) {
                        future.complete(new ResponseEntity<>(HttpStatus.NO_CONTENT));
                    } else {
                        future.complete(new ResponseEntity<>(excuses, HttpStatus.OK));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    future.complete(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                }
            });

            return future.join();
        });
    }

    @GetMapping("/excuses/all")
    public CompletableFuture<ResponseEntity<List<ModelExcuse>>> getAllExcuses() {
        List<ModelExcuse> allExcuses = new ArrayList<>();

        return CompletableFuture.supplyAsync(() -> {
            final CompletableFuture<ResponseEntity<List<ModelExcuse>>> future = new CompletableFuture<>();

            excusesRef.child("category").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        String category = categorySnapshot.getKey();
                        excusesRef.child("category").child(category)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot categoryDataSnapshot) {
                                        for (DataSnapshot excuseSnapshot : categoryDataSnapshot.getChildren()) {
                                            ModelExcuse excuse = excuseSnapshot.getValue(ModelExcuse.class);
                                            if (excuse != null) {
                                                allExcuses.add(excuse);
                                            }
                                        }

                                        if (allExcuses.isEmpty()) {
                                            future.complete(new ResponseEntity<>(HttpStatus.NO_CONTENT));
                                        } else {
                                            future.complete(new ResponseEntity<>(allExcuses, HttpStatus.OK));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        future.complete(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    future.complete(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                }
            });

            return future.join();
        });
    }

    @GetMapping("/excuses/{quoteid}")
    public ResponseEntity<ModelExcuse> getExcuseByQuoteid(@PathVariable String quoteid) {
        final CompletableFuture<ResponseEntity<ModelExcuse>> future = new CompletableFuture<>();

        excusesRef.child("excuses").child(quoteid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ModelExcuse excuse = dataSnapshot.getValue(ModelExcuse.class);
                if (excuse != null) {
                    future.complete(new ResponseEntity<>(excuse, HttpStatus.OK));
                } else {
                    future.complete(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.complete(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        });

        return future.join();
    }

    @PostMapping("/excuses/add")
    public ResponseEntity<String> postOneExcuse(@RequestBody ModelExcuse excuse) {
        DatabaseReference categoryRef = excusesRef.child("category").child(excuse.getCategory());

        DatabaseReference newExcuseRef = categoryRef.child(excuse.getQuoteid());

        newExcuseRef.setValueAsync(excuse);

        return new ResponseEntity<>("Excuse added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/excuses/{quoteid}")
    public ResponseEntity<String> deleteExcuse(@PathVariable String quoteid) {
        final CompletableFuture<ResponseEntity<String>> future = new CompletableFuture<>();

        excusesRef.child("excuses").child(quoteid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    future.complete(new ResponseEntity<>("Excuse deleted successfully", HttpStatus.OK));
                } else {
                    future.complete(new ResponseEntity<>("Failed to delete excuse: " + databaseError.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR));
                }
            }
        });

        return future.join();
    }

}
