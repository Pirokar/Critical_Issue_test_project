package vladimir.ru.critical_issue_test_project.model.rest;

import retrofit.Callback;
import vladimir.ru.critical_issue_test_project.model.entites.ChatAnswer;

/**
 * Created by Vladimir on 15.10.2016.
 * Presents model for chat
 */

public interface ChatModel {
    void getChat(Callback<ChatAnswer> callback);
}
