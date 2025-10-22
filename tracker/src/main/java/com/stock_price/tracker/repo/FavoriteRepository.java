package com.stock_price.tracker.repo;

import com.stock_price.tracker.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsBySymbol(String symbol);

    boolean existsById(Long id);
}
