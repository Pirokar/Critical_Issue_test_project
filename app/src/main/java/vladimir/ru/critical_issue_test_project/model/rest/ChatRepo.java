package vladimir.ru.critical_issue_test_project.model.rest;

import com.jakewharton.retrofit.Ok3Client;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.SimpleXMLConverter;
import vladimir.ru.critical_issue_test_project.model.APIConstants;
import vladimir.ru.critical_issue_test_project.model.CustomOkHTTPInterceptor;
import vladimir.ru.critical_issue_test_project.model.entites.ChatAnswer;

/**
 * Created by Vladimir on 15.10.2016.
 * Presents repo for test xml
 */

public class ChatRepo implements ChatModel {
    private ChatAPI API;
    private static ChatRepo repo = null;

    private ChatRepo() {
        API = createAdapter();
    }

    public static ChatRepo getInstance() {
        if(repo == null) {
            repo = new ChatRepo();
        }
        return repo;
    }

    @Override
    public void getChat(Callback<ChatAnswer> callback) {
        API.getTestXml(callback);
    }

    private ChatAPI createAdapter() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setClient(new Ok3Client(CustomOkHTTPInterceptor.getCustomOKHttpClient()))
                .setEndpoint(APIConstants.API_URL)
                .setConverter(new SimpleXMLConverter())
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("Retrofit_LOG"))
                .build();

        return adapter.create(ChatAPI.class);
    }
}
