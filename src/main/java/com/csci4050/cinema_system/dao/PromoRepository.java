package com.csci4050.cinema_system.dao;

import com.csci4050.cinema_system.model.Promotion;
import org.springframework.data.repository.CrudRepository;

public interface PromoRepository extends CrudRepository<Promotion, Long> {
    Promotion findPromotionById(Long promotionId);
}
