package halewang.com.bangbang.model.zhihu;

import java.util.List;

/**
 * Created by p_whaohwang on 2017/4/5.
 */

public class NewsBean {
    private String date;
    private List<News> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<News> getStories() {
        return stories;
    }

    public void setStories(List<News> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                '}';
    }
}
