package halewang.com.bangbang.model.zhihu;

import java.util.List;

/**
 * Created by p_whaohwang on 2017/4/5.
 */

public class News {
    private String title;
    private List<String> images;
    private String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", images=" + images +
                ", id='" + id + '\'' +
                '}';
    }
}
