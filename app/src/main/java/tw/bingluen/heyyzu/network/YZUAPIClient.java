package tw.bingluen.heyyzu.network;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import tw.bingluen.heyyzu.constant.YZUSecret;
import tw.bingluen.heyyzu.model.AccessToken;
import tw.bingluen.heyyzu.model.PublicKey;

public class YZUAPIClient {

    private static final String API_BASE_URL = "https://unipop.yzu.edu.tw/YzuPortalAPI/";

    private static Retrofit retrofit;
    private static YZUAPIService sYZUAPIService;

    public static Retrofit getRetrofit() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request authenticatedRequest = request.newBuilder()
                                .header("Authorization", Credentials.basic(YZUSecret.getUsername(), YZUSecret.getPassword()))
                                .build();
                        return chain.proceed(authenticatedRequest);
                    }
                })
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static YZUAPIService get() {
        if (sYZUAPIService == null) {
            sYZUAPIService = getRetrofit().create(YZUAPIService.class);
        }

        return sYZUAPIService;
    }

    public static Call<PublicKey> getRSAKey() {
        return YZUAPIClient.get().publicKey(YZUSecret.getAppId());
    }

    public static Call<AccessToken> getAccessToken(String encryptUsername, String encryptPassword) {
        return YZUAPIClient.get().accessToken(YZUSecret.getAppId(), encryptUsername, encryptPassword);
    }

    public interface YZUAPIService {
        @FormUrlEncoded
        @POST("./api/Auth/RSAkeybyAppID")
        Call<PublicKey> publicKey(
                @Field("AppID") String appId
        );

        @FormUrlEncoded
        @POST("./api/Auth/UserAccessToken")
        Call<AccessToken> accessToken(
                @Field("AppID") String appId,
                @Field("account") String encryptUsername,
                @Field("password") String encryptPassword
        );

    }
}
