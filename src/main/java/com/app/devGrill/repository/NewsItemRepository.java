package com.app.devGrill.repository;

import com.app.devGrill.entity.NewsItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface NewsItemRepository extends JpaRepository<NewsItem, Serializable> {
}
