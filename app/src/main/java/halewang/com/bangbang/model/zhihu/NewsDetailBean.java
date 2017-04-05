package halewang.com.bangbang.model.zhihu;

/**
 * Created by p_whaohwang on 2017/4/5.
 */

public class NewsDetailBean {
    private String title;
    private String share_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    @Override
    public String toString() {
        return "NewsDetailBean{" +
                "title='" + title + '\'' +
                ", share_url='" + share_url + '\'' +
                '}';
    }
}
