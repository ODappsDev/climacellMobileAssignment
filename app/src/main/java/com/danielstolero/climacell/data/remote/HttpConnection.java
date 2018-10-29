package com.danielstolero.climacell.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.danielstolero.climacell.MyApplication;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Daniel Stolero on 16/03/2017.
 */

public class HttpConnection {

    private final String TAG = HttpConnection.class.getSimpleName();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private enum Method {GET, POST, PUT, DELETE}

    private static volatile HttpConnection instance = null;
    private static final Object lock = new Object();

    static HttpConnection getInstance() {
        HttpConnection result = instance;
        if (result == null) {
            synchronized (lock) {    // While we were waiting for the lock, another
                result = instance;        // thread may have instantiated the object.
                if (result == null) {
                    result = new HttpConnection();
                    instance = result;
                }
            }
        }
        return instance;
    }

    private OkHttpClient client;

    private HttpConnection() {
        int cacheSize = 10 * 1024 * 1024; // 10MB

        client = new OkHttpClient.Builder()
                .cache(new Cache(MyApplication.getContext().getCacheDir(), cacheSize))
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    private Request setupRequest(@NonNull String url, boolean authenticateUsingAccessToken) {
        return setupRequest(url, Method.GET, null, authenticateUsingAccessToken);
    }

    private Request setupRequest(@NonNull String url, @NonNull Method method, @Nullable RequestBody body, boolean authenticateUsingAccessToken) {

        Request.Builder builder = new Request.Builder().url(url);

        if(authenticateUsingAccessToken) {
            builder.addHeader("apikey", "S984VQLZqIKmB7bCS6YFaV9sRUBAtYY7");
        }

        switch (method) {
            case GET:
                builder.get();
                break;
            case POST:
                builder.post(body);
                break;
            case PUT:
                builder.put(body);
                break;
            case DELETE:
                builder.delete(body);
                break;
        }

        return builder.build();
    }

    /**
     * An HTTP request. handle with GET request as synchronously.
     *
     * @param server                       - The host address. like "http://greeniq.net/"
     * @param api                          - The internal address. like "account/register"
     * @param authenticateUsingAccessToken - if true, will add a header with user token
     * @return - A JSON of initializeResponse as a String.
     */

    public String get(@NonNull String server, @NonNull String api, boolean authenticateUsingAccessToken) {
        String url = server + "/" + api;
        Log.d(TAG, "get() URL: " + url);

        try {
            Call call = client.newCall(setupRequest(url, authenticateUsingAccessToken));

            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * An HTTP request. handle with GET request as synchronously.
     *
     * @param server                       - The host address. like "http://greeniq.net/"
     * @param api                          - The internal address. like "account/register"
     * @param params                       - The parameters query
     * @param authenticateUsingAccessToken - if true, will add a header with user token
     * @return - A initializeResponse of the request
     */

    public String get(@NonNull String server, @NonNull String api, @Nullable Map<String, String> params, boolean authenticateUsingAccessToken) {
        String url = server + "/" + api;
        Log.d(TAG, "get() URL: " + url);

        try {

            HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();

            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    httpBuilder.addQueryParameter(param.getKey(), param.getValue());
                }
            }

            Call call = client.newCall(setupRequest(httpBuilder.build().url().toString(), authenticateUsingAccessToken));

            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * An HTTP request. handle with GET request as asynchronously.
     *
     * @param server                       - The host address. like "http://greeniq.net/"
     * @param api                          - The internal address. like "account/register"
     * @param callback                     - The callback of the request
     * @param authenticateUsingAccessToken - if true, will add a header with user token
     * @return - A initializeResponse of the request
     */

    public Call get(@NonNull String server, @NonNull String api, Callback callback, boolean authenticateUsingAccessToken) {
        return get(server, api, null, callback, authenticateUsingAccessToken);
    }

    /**
     * An HTTP request. handle with GET request as asynchronously.
     *
     * @param server                       - The host address. like "http://greeniq.net/"
     * @param api                          - The internal address. like "account/register"
     * @param params                       - The parameters query
     * @param callback                     - The callback of the request
     * @param authenticateUsingAccessToken - if true, will add a header with user token
     * @return - A initializeResponse of the request
     */

    public Call get(@NonNull String server, @NonNull String api, @Nullable Map<String, String> params, Callback callback, boolean authenticateUsingAccessToken) {
        String url = server + "/" + api;
        Log.d(TAG, "get() URL: " + url);

        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();

            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    httpBuilder.addQueryParameter(param.getKey(), param.getValue());
                }
            }

            Call call = client.newCall(setupRequest(httpBuilder.build().url().toString(), authenticateUsingAccessToken));
            call.enqueue(callback);
            return call;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * An HTTP request. handle with POST request as synchronously.
     *
     * @param server - The host address. like "http://greeniq.net/"
     * @param api    - The internal address. like "account/register"
     * @param map    - The form body of the request.
     * @return - A JSON of initializeResponse as a String.
     */

    public String post(@NonNull String server, @NonNull String api, @NonNull Map<String, String> map) {
        return post(server, api, map, false);
    }

    /**
     * An HTTP request. handle with POST request as synchronously.
     *
     * @param server                       - The host address. like "http://greeniq.net/"
     * @param api                          - The internal address. like "account/register"
     * @param params                       - The form body of the request.
     * @param authenticateUsingAccessToken - if true, will add a header with user token
     * @return - A JSON of initializeResponse as a String.
     */

    public String post(@NonNull String server, @NonNull String api, @Nullable Map<String, String> params, boolean authenticateUsingAccessToken) {
        String url = server + "/" + api;
        Log.d(TAG, "post() URL: " + url);

        try {
            FormBody.Builder formBody = new FormBody.Builder();

            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    if (param != null && param.getKey() != null && param.getValue() != null)
                        formBody.add(param.getKey(), param.getValue());
                }
            }

            Call call = client.newCall(setupRequest(url, Method.POST, formBody.build(), authenticateUsingAccessToken));

            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * An HTTP request. handle with POST request as asynchronously.
     *
     * @param server                       - The host address. like "http://greeniq.net/"
     * @param api                          - The internal address. like "account/register"
     * @param params                       - The form body of the request.
     * @param authenticateUsingAccessToken - if true, will add a header with user token
     * @return - A JSON of initializeResponse as a String.
     */

    public Call post(@NonNull String server, @NonNull String api, @Nullable Map<String, String> params, Callback callback, boolean authenticateUsingAccessToken) {
        String url = server + "/" + api;
        Log.d(TAG, "post() URL: " + url);

        FormBody.Builder formBody = new FormBody.Builder();

        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (param != null && param.getKey() != null && param.getValue() != null)
                    formBody.add(param.getKey(), param.getValue());
            }
        }

        Call call = client.newCall(setupRequest(url, Method.POST, formBody.build(), authenticateUsingAccessToken));
        call.enqueue(callback);
        return call;
    }


    /**
     * An HTTP request. handle with POST request as synchronously.
     *
     * @param server                       - The host address. like "http://greeniq.net/"
     * @param api                          - The internal address. like "account/register"
     * @param json                         - The pure json body of the request.
     * @param authenticateUsingAccessToken - if true, will add a header with user token
     * @return - A JSON of initializeResponse as a String.
     */

    public String post(@NonNull String server, @NonNull String api, @NonNull String json, boolean authenticateUsingAccessToken) {
        String url = server + "/" + api;
        Log.d(TAG, "post() URL: " + url);

        try {
            RequestBody body = RequestBody.create(JSON, json);

            Call call = client.newCall(setupRequest(url, Method.POST, body, authenticateUsingAccessToken));

            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * An HTTP request. handle with POST request as asynchronously.
     *
     * @param server                       - The host address. like "http://greeniq.net/"
     * @param api                          - The internal address. like "account/register"
     * @param json                         - The pure json body of the request.
     * @param callback                     - The callback of the request
     * @param authenticateUsingAccessToken - if true, will add a header with user token
     * @return - A initializeResponse of the request
     */

    public Call post(@NonNull String server, @NonNull String api, @Nullable String json, Callback callback, boolean authenticateUsingAccessToken) {
        return post(server, api, json, null, callback, authenticateUsingAccessToken);
    }

    public Call post(@NonNull String server, @NonNull String api, @Nullable String json, @Nullable Map<String, String> params, Callback callback, boolean authenticateUsingAccessToken) {
        String url = server + "/" + api;
        Log.d(TAG, "post() URL: " + url);

        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();

            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    httpBuilder.addQueryParameter(param.getKey(), param.getValue());
                }
            }

            Call call = client.newCall(setupRequest(httpBuilder.build().url().toString(), Method.POST,
                    json != null ? RequestBody.create(JSON, json) : RequestBody.create(null, new byte[0]),
                    authenticateUsingAccessToken));
            call.enqueue(callback);
            return call;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * An HTTP request. handle with PUT request as asynchronously.
     *
     * @param server                       - The host address. like "http://greeniq.net/"
     * @param api                          - The internal address. like "account/register"
     * @param json                         - The pure json body of the request.
     * @param callback                     - The callback of the request
     * @param authenticateUsingAccessToken - if true, will add a header with user token
     * @return - A initializeResponse of the request
     */

    public Call put(@NonNull String server, @NonNull String api, @Nullable String json, Callback callback, boolean authenticateUsingAccessToken) {
        String url = server + "/" + api;
        Log.d(TAG, "post() URL: " + url);

        Call call = client.newCall(setupRequest(url, Method.PUT,
                json != null ? RequestBody.create(JSON, json) : RequestBody.create(null, new byte[0]),
                authenticateUsingAccessToken));
        call.enqueue(callback);
        return call;
    }

    public Call delete(@NonNull String server, @NonNull String api, @Nullable String json, Callback callback, boolean authenticateUsingAccessToken) {
        return delete(server, api, null, json, callback, authenticateUsingAccessToken);
    }


    public Call delete(@NonNull String server, @NonNull String api, @Nullable Map<String, String> params, @Nullable String json, Callback callback, boolean authenticateUsingAccessToken) {
        String url = server + "/" + api;
        Log.d(TAG, "delete() URL: " + url);

        try {
            HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();

            if (params != null) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    httpBuilder.addQueryParameter(param.getKey(), param.getValue());
                }
            }

            Call call = client.newCall(setupRequest(httpBuilder.build().url().toString(), Method.DELETE,
                    json != null ? RequestBody.create(JSON, json) : RequestBody.create(null, new byte[0]),
                    authenticateUsingAccessToken));
            call.enqueue(callback);
            return call;
        } catch (Exception e) {
            return null;
        }
    }
}