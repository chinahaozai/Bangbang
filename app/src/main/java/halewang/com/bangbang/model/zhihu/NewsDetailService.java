package halewang.com.bangbang.model.zhihu;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by p_whaohwang on 2017/4/5.
 */

public interface NewsDetailService {
    @GET()
    Observable<NewsDetailBean> getNews(@Url String url);
}
