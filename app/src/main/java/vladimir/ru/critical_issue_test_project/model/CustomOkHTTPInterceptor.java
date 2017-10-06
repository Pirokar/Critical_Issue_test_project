package vladimir.ru.critical_issue_test_project.model;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Vladimir on 15.10.2016.
 * Interceptor
 */

public class CustomOkHTTPInterceptor {
    private static final int CONNECT_TIMEOUT_MILLIS = 15000;
    private static final int READ_TIMEOUT_MILLIS = 15000;
    private static final int WRITE_TIMEOUT_MILLIS = 15000;

    public static OkHttpClient getCustomOKHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .build();
    }
}
