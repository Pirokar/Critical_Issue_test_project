package vladimir.ru.critical_issue_test_project.views.adapters;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vladimir.ru.critical_issue_test_project.R;
import vladimir.ru.critical_issue_test_project.model.entites.ChatItem;

/**
 * Created by Vladimir on 16.10.2016.
 * Adapter for chat list
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatItem> list;

    public ChatAdapter(List<ChatItem> list) {
        this.list = list;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        setTextViews(holder, position);
        setDivider(holder, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<ChatItem> list) {
        this.list = list;
    }

    private void setTextViews(ChatViewHolder holder, int position) {
        holder.dateText.setText(list.get(position).date);
        holder.idText.setText(String.valueOf(list.get(position).id));
        holder.messageText.setText(list.get(position).text);
    }

    private void setDivider(ChatViewHolder holder, int position) {
        if(position == getItemCount() - 1) {
            holder.divider.setVisibility(View.INVISIBLE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, idText, messageText;
        View divider;

        public ChatViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            dateText = (TextView)itemView.findViewById(R.id.date_text);
            idText = (TextView)itemView.findViewById(R.id.id_text);
            messageText = (TextView)itemView.findViewById(R.id.message_text);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
