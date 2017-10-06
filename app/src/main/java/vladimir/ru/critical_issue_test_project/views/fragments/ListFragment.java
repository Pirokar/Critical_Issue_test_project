package vladimir.ru.critical_issue_test_project.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.List;

import vladimir.ru.critical_issue_test_project.R;
import vladimir.ru.critical_issue_test_project.helpers.SerializationHelper;
import vladimir.ru.critical_issue_test_project.model.entites.ListItem;
import vladimir.ru.critical_issue_test_project.model.events.AddElementEvent;
import vladimir.ru.critical_issue_test_project.model.events.ItemListChangedEvent;
import vladimir.ru.critical_issue_test_project.utils.BusProvider;
import vladimir.ru.critical_issue_test_project.views.activities.AddEditElementActivity;
import vladimir.ru.critical_issue_test_project.views.adapters.ListAdapter;

/**
 * Created by Vladimir on 14.10.2016.
 * Presents main list
 */

public class ListFragment extends Fragment {
    public final static int ADD_ELEMENT_TO_LIST_LAUNCH_CODE = 2233;

    RecyclerView recyclerView;
    ListAdapter adapter;
    List<ListItem> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        initViews(view);
        initRecyclerView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getUIBusInstance().register(this);
    }

    @Override
    public void onStop() {
        BusProvider.getUIBusInstance().unregister(this);
        super.onStop();
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
    }

    private void initRecyclerView() {
        list = SerializationHelper.deserealizeList(getContext());
        adapter = new ListAdapter(list, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Subscribe
    @SuppressWarnings("all")
    public void onAddElementEvent(AddElementEvent event) {
        AddEditElementActivity.openActivityForResult(list.size(), ADD_ELEMENT_TO_LIST_LAUNCH_CODE, event.getActivity());
    }

    @Subscribe
    @SuppressWarnings("all")
    public void onItemListChangedEvent(ItemListChangedEvent event) {
        if(event.getElementPosition() == list.size()) {
            list.add(new ListItem(event.getText(), false));
            adapter.notifyItemInserted(event.getElementPosition());
        } else {
            list.get(event.getElementPosition()).setText(event.getText());
            adapter.notifyItemChanged(event.getElementPosition());
        }
        SerializationHelper.serializeList(list, getContext());
    }
}
