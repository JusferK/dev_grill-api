package com.app.devGrill.service;

import com.app.devGrill.entity.Administrator;
import com.app.devGrill.entity.NewsItem;
import com.app.devGrill.repository.NewsItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@CrossOrigin
public class NewsItemService {

    @Autowired
    NewsItemRepository newsItemRepository;

    @GetMapping("/all-news")
    public List<NewsItem> getAllNews() {
        return newsItemRepository.findAll();
    }

    @PostMapping("/post-new")
    public NewsItem postNew(@RequestBody NewsItem newsItem) {
        return newsItemRepository.save(newsItem);
    }

    @DeleteMapping("/delete-new/{idNewsItem}")
    public void deleteNew(@PathVariable Integer idNewsItem) {
        newsItemRepository.deleteById(idNewsItem);
    }

    @PutMapping("/update-new/{idNewsItem}")
    public NewsItem updateNew(@PathVariable Integer idNewsItem, @RequestBody NewsItem newsItem) {
        NewsItem newsSavedDB = newsItemRepository.findById(idNewsItem)
                .orElseThrow(() -> new EntityNotFoundException("news item not found"));

        if(newsItem.getPhoto() != null) newsSavedDB.setPhoto(newsItem.getPhoto());
        if(!newsItem.getTitle().isEmpty()) newsSavedDB.setTitle(newsItem.getTitle());
        if(!newsItem.getText().isEmpty()) newsSavedDB.setText(newsItem.getText());

        return newsItemRepository.save(newsSavedDB);
    }
}
