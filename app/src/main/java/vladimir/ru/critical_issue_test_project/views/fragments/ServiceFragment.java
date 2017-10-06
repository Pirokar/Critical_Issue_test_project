package vladimir.ru.critical_issue_test_project.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;

import retrofit.RetrofitError;
import retrofit.client.Response;
import vladimir.ru.critical_issue_test_project.R;
import vladimir.ru.critical_issue_test_project.model.entites.ChatAnswer;
import vladimir.ru.critical_issue_test_project.model.rest.ChatRepo;
import vladimir.ru.critical_issue_test_project.views.adapters.ChatAdapter;

/**
 * Created by Vladimir on 14.10.2016.
 * Fragment for chat list from http://storage.space-o.ru/testXmlFeed.xml
 */

public class ServiceFragment extends Fragment implements Callback<ChatAnswer>,
        SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    TextView noDataText;

    ChatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        initViews(view);
        setRefreshLayoutListener();
        getData();

        return view;
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        noDataText = (TextView)view.findViewById(R.id.no_data_text);
    }

    private void setRefreshLayoutListener() {
        refreshLayout.setOnRefreshListener(this);
    }

    private void getData() {
        refreshLayout.setRefreshing(true);
        ChatRepo.getInstance().getChat(this);
    }

    private void setRecyclerView(ChatAnswer chatAnswer) {
        if(adapter == null) {
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            adapter = new ChatAdapter(chatAnswer.items);

            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setList(chatAnswer.items);
            adapter.notifyDataSetChanged();
        }
        refreshLayout.setRefreshing(false);
    }

    private void showError() {
        Toast.makeText(getContext(), getString(R.string.error_happened), Toast.LENGTH_LONG).show();
        noDataText.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        noDataText.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void success(ChatAnswer chatAnswer, Response response) {
        if(chatAnswer.items.size() > 0) {
            hideError();
            setRecyclerView(chatAnswer);
        } else {
            noDataText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        refreshLayout.setRefreshing(false);
        showError();
    }
}
