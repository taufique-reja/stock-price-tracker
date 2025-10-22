package com.stock_price.tracker.controller;

import com.stock_price.tracker.entity.Favorite;
import com.stock_price.tracker.repo.FavoriteRepository;
import com.stock_price.tracker.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @GetMapping("/stocks/{symbol}")
    public ResponseEntity<?> getStock(@PathVariable String symbol){
        try{
            return ResponseEntity.ok(stockService.getStockData(symbol));
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/favorites")
    public List<Favorite> getFavorites(){
        return favoriteRepository.findAll();
    }

    @PostMapping("/favorites")
    public ResponseEntity<?> addFavorite(@RequestBody Favorite favorite){
        if (favoriteRepository.existsBySymbol(favorite.getSymbol())){
            return ResponseEntity.status(409).body(Map.of("error", "Already exists"));
        }
        return ResponseEntity.ok(favoriteRepository.save(favorite));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Long id){
        if (!favoriteRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        favoriteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
