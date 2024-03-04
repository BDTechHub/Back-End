package com.bdtc.technews.service.news;

import com.bdtc.technews.dto.NewsDetailingDto;
import com.bdtc.technews.dto.NewsPreviewDto;
import com.bdtc.technews.dto.NewsRequestDto;
import com.bdtc.technews.dto.NewsUpdateDto;
import com.bdtc.technews.model.News;
import com.bdtc.technews.repository.NewsRepository;
import com.bdtc.technews.service.news.utils.DateHandler;
import com.bdtc.technews.service.news.utils.ImageHandler;
import com.bdtc.technews.service.news.utils.TagHandler;
import com.bdtc.technews.service.tag.TagService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private DateHandler dateHandler;

    @Autowired
    private TagHandler tagHandler;

    @Autowired
    private ImageHandler imageHandler;

    @Transactional
    public NewsDetailingDto createNews(NewsRequestDto newsDto) {
        var news = new News(newsDto);
        var dateNow = dateHandler.getCurrentDateTime();
        var tagSet = tagService.getTagSet(newsDto.tags());
        var imageUrl = imageHandler.saveImageToUploadDir(newsDto.image());

        news.setCreationDate(dateNow);
        news.setUpdateDate(dateNow);
        news.setTags(tagSet);
        news.setImageUrl(imageUrl);

        newsRepository.save(news);
        return new NewsDetailingDto(
                news,
                tagHandler.convertSetTagToSetString(news.getTags()),
                dateHandler.formatDate(news.getUpdateDate())
        );
    }

    public Page<NewsPreviewDto> getNewsPreview(Pageable pageable, boolean sortByView) {
        Page<News> newsPage;
        if(sortByView) {
            newsPage = newsRepository.findByOrderByViewsDesc(pageable);
        } else {
            newsPage = newsRepository.findAll(pageable);
        }
        return newsPage.map(news -> new NewsPreviewDto(
                news,
                dateHandler.formatDate(news.getUpdateDate())
                )
        );
    }

    @Transactional
    public NewsDetailingDto getNewsById(UUID newsId) {
        var news = newsRepository.getReferenceById(newsId);
        news.addAView();
        return new NewsDetailingDto(
                news,
                tagHandler.convertSetTagToSetString(news.getTags()),
                dateHandler.formatDate(news.getUpdateDate())
        );
    }

    public Page<NewsPreviewDto> getNewsPreviewFilteringByTags(Pageable pageable, String tags) {
        List<String> tagList = Arrays.asList(tags.split(","));
        var newsPage = newsRepository.findByTagNames(pageable, tagList, (long) tagList.size());
        return newsPage.map(news -> new NewsPreviewDto(
                        news,
                        dateHandler.formatDate(news.getUpdateDate())
                )
        );
    }

    @Transactional
    public NewsDetailingDto updateNews(UUID newsId, NewsUpdateDto updateDto) {
        var news = newsRepository.getReferenceById(newsId);

        if(updateDto.title() !=null) news.updateTitle(updateDto.title());
        if(updateDto.summary() !=null) news.updateSummary(updateDto.summary());
        if(updateDto.body() !=null) news.updateBody(updateDto.body());

        if(updateDto.tags() !=null) {
            var tagSet = tagService.getTagSet(updateDto.tags());
            news.setTags(tagSet);
        }

        if(updateDto.image() !=null) {
            var imageUrl = imageHandler.saveImageToUploadDir(updateDto.image());
            news.setImageUrl(imageUrl);
        }

        news.setUpdateDate(dateHandler.getCurrentDateTime());

        return new NewsDetailingDto(
                news,
                tagHandler.convertSetTagToSetString(news.getTags()),
                dateHandler.formatDate(news.getUpdateDate())
        );
    }
}
