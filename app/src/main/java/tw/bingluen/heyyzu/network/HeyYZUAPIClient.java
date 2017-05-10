package tw.bingluen.heyyzu.network;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tw.bingluen.heyyzu.constant.HeyYZUSecret;
import tw.bingluen.heyyzu.model.course.CourseAnnouncement;
import tw.bingluen.heyyzu.model.course.CourseHomework;
import tw.bingluen.heyyzu.model.course.CourseMaterial;
import tw.bingluen.heyyzu.model.library.LibraryDashboard;
import tw.bingluen.heyyzu.model.library.LibraryUsersBook;
import tw.bingluen.heyyzu.model.user.Curriculum;

public class HeyYZUAPIClient {

    private static final String API_BASE_URL = "";

    private static Retrofit retrofit;
    private static HeyYZUAPIService sHeyYZUAPIService;

    public static Retrofit getRetrofit() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(
                                chain.request().newBuilder()
                                        .header(HeyYZUSecret.getCipher(), HeyYZUSecret.getSecret())
                                        .build()
                        );
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

    public static HeyYZUAPIService get() {
        if (sHeyYZUAPIService == null) {
            sHeyYZUAPIService = getRetrofit().create(HeyYZUAPIService.class);
        }

        return sHeyYZUAPIService;
    }


    public interface HeyYZUAPIService {
        // Course
        @GET("./course/announcement/{id}")
        Call<List<CourseAnnouncement>> courseAnnouncements(
                @Path("id") String id,
                @Query("access_token") String accessToken
        );

        @GET("./course/material/{id}")
        Call<List<CourseMaterial>> courseMaterials(
                @Path("id") String id,
                @Query("access_token") String accessToken
        );

        @GET("./course/homework/{id}")
        Call<List<CourseHomework>> courseHomeworks(
                @Path("id") String id,
                @Query("access_token") String accessToken
        );

        // Library
        @GET("./library/dashboard")
        Call<LibraryDashboard> libraryDashboard(
                @Query("access_token") String accessToken
        );
        @GET("./library/reading")
        Call<List<LibraryUsersBook>> libraryReading(
                @Query("access_token") String accessToken
        );
        @GET("./library/read")
        Call<List<LibraryUsersBook>> libraryRead(
                @Query("access_token") String accessToken
        );
        @GET("./library/reserving")
        Call<List<LibraryUsersBook>> libraryReserving(
                @Query("access_token") String accessToken
        );
        @GET("./library/favorite")
        Call<List<LibraryUsersBook>> libraryFavorite(
                @Query("access_token") String accessToken
        );

        // User
        @GET("./user/curriculum")
        Call<List<Curriculum>> userCurriculum(
                @Query("access_token") String accessToken
        );

    }
}
