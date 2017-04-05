package halewang.com.bangbang.model.zhihu;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by p_whaohwang on 2017/4/5.
 */

public interface ZhihuNewsService {
    @GET("latest")
    Observable<NewsBean> getNews();
}
