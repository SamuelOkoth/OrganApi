package dao;

import models.News;

import java.util.List;

public interface NewsDao {
    //create
    void add (News news);

    //Find news
    News findByiId(int id);
    //get dpt news
    List<News>getAllNews();
    //update
    void updateNews(int id, String news_name, String news_content,int dpt_id);

    //delete
    void deleteById(int id);

    //clear
    void clearAll();

}
