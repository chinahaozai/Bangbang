package halewang.com.bangbang.model.zhihu;

import java.util.List;

/**
 * Created by p_whaohwang on 2017/4/5.
 */

public class NewsBean {
    private String date;
    private List<News> news;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "date='" + date + '\'' +
                ", news=" + news +
                '}';
    }
}
