package com.ntq.vnexpress.provider;

import com.ntq.vnexpress.model.RSS;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

public class VNExpressProvider {

    private final VNExpressService vnExpressService;

    public interface VNExpressService{
        @GET("tin-moi-nhat.rss")
        Observable<RSS> getNews();
    }

    public VNExpressProvider() {
        Retrofit vnpressRetrofit = new Retrofit.Builder()
                .baseUrl("https://vnexpress.net/rss/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        vnExpressService = vnpressRetrofit.create(VNExpressService.class);
    }

    public VNExpressService getVnExpressService() {
        return vnExpressService;
    }
}
