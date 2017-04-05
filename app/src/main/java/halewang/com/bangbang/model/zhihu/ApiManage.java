package halewang.com.bangbang.model.zhihu;


import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by halewang on 2016/11/25.
 */

public class ApiManage {
    private static final String TAG = "ApiManage";
    private static ZhihuNewsService newsService;
    private static NewsDetailService newsDetailService;

    public static ZhihuNewsService getNewsService(){
        if(newsService == null){
            createNewsService();
        }
        return newsService;
    }

    public static NewsDetailService getNewsDetailService(){
        if(newsDetailService == null){
            createNewsDetailService();
        }
        return newsDetailService;
    }

    private static void createNewsService(){
        newsService = createRetrofit("http://news-at.zhihu.com/api/3/news/").create(ZhihuNewsService.class);
    }
    private static void createNewsDetailService(){
        newsDetailService = createRetrofit("http://news-at.zhihu.com/api/3/news/").create(NewsDetailService.class);
    }

    private static Retrofit createRetrofit(String baseUrl){
        //HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                //.addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return  retrofit;
    }
}
