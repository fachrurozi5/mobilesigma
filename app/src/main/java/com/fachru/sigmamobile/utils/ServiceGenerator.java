package com.fachru.sigmamobile.utils;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fachru on 07/03/16.
 */
public class ServiceGenerator {

	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
	private static GsonBuilder gsonBuilder = new GsonBuilder()
			.setDateFormat("yyyy-MM-dd HH:mm:ss")
			.excludeFieldsWithoutExposeAnnotation();

	private static Retrofit.Builder builder =
			new Retrofit.Builder()
					.baseUrl(Constanta.BASE_URL)
					.addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));

	public static <S> S createService(Class<S> serviceClass) {
		return createService(serviceClass, null);
	}

	public static <S> S createService(Class<S> serviceClass, final String authToken) {
		if (authToken != null) {
			httpClient.addInterceptor(new Interceptor() {
				@Override
				public Response intercept(Interceptor.Chain chain) throws IOException {
					Request original = chain.request();

					String remember_token = "";

					if (authToken.length() > 21) {
						int length = authToken.length();
						remember_token = authToken.substring(length - 20, length);
					}

					Request.Builder requestBuilder = original.newBuilder()
							.header("Accept", "application/json")
							.header("Content-Type", "application/json")
							.header("User-Agent", "Sigma User")
							.header("Authorization", authToken)
							.header("Remember-Token", remember_token)
							.method(original.method(), original.body());

					Request request = requestBuilder.build();
					return chain.proceed(request);
				}
			});

			httpClient.addInterceptor(new Interceptor() {
				@Override
				public Response intercept(Chain chain) throws IOException {
					Response originalResponse = chain.proceed(chain.request());
					String token = "";
					if (!originalResponse.headers("Set-Cookie").isEmpty()) {
						for (String header : originalResponse.headers("Set-Cookie")) {
							for (String s : header.split("\\;")) {
								if (s.split("\\=")[0].equals("Token")) token = s.split("\\=")[1];
							}
						}

						SessionManager.prefEdit()
								.putString(SessionManager.KEY_TOKEN, token)
								.commit();

					}
					return originalResponse;
				}
			});
		}

		OkHttpClient client = httpClient.build();
		Retrofit retrofit = builder.client(client).build();
		return retrofit.create(serviceClass);
	}
}
