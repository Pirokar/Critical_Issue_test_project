package vladimir.ru.critical_issue_test_project.views.adapters;

import android.app.Activity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import vladimir.ru.critical_issue_test_project.R;
import vladimir.ru.critical_issue_test_project.helpers.SerializationHelper;
import vladimir.ru.critical_issue_test_project.model.entites.ListItem;
import vladimir.ru.critical_issue_test_project.views.activities.AddEditElementActivity;
import vladimir.ru.critical_issue_test_project.views.fragments.ListFragment;

/**
 * Created by Vladimir on 17.10.2016.
 * Adapter for list from the first screen
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private List<ListItem> list;
    private Activity activity;

    public ListAdapter(List<ListItem> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.item_list, parent, false);

        return new ListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        setContentLayout(holder, position);
        setIcon(holder, position);
        setText(holder, position);
        setCheckBox(holder, position);
        setDivider(holder, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private void setContentLayout(final ListViewHolder holder, final int position) {
        holder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEditElementActivity.openActivityForResult(position, ListFragment.ADD_ELEMENT_TO_LIST_LAUNCH_CODE,
                        activity);
            }
        });
        holder.contentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity, holder.contentLayout);
                popupMenu.inflate(R.menu.menu_item);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit: {
                                AddEditElementActivity.openActivityForResult(position, ListFragment.ADD_ELEMENT_TO_LIST_LAUNCH_CODE,
                                        activity);

                                return true;
                            }
                            case R.id.delete: {
                                list.remove(position);
                                notifyItemRemoved(position);
                                SerializationHelper.serializeList(list, activity);

                                return true;
                            }
                            default: {
                                return false;
                            }
                        }
                    }
                });

                popupMenu.show();
                return true;
            }
        });
    }

    @SuppressWarnings("all")
    private void setIcon(ListViewHolder holder, int position) {
        if(list.get(position).isSelected()) {
            holder.iconImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.warning_icon));
        } else {
            holder.iconImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.info_icon));
        }
    }

    private void setText(final ListViewHolder holder, final int position) {
        holder.messageText.setText(list.get(position).getText());
    }

    @SuppressWarnings("all")
    private void setCheckBox(final ListViewHolder holder, final int position) {
        holder.checkBox.setChecked(list.get(position).isSelected());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean selected) {
                if(selected) {
                    list.get(position).markAsSelected();
                    holder.iconImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.warning_icon));
                } else {
                    list.get(position).markAsUnselected();
                    holder.iconImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.info_icon));
                }
                SerializationHelper.serializeList(list, activity);
            }
        });
    }

    private void setDivider(final ListViewHolder holder, final int position) {
        if(position == getItemCount() - 1) {
            holder.divider.setVisibility(View.INVISIBLE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout contentLayout;
        ImageView iconImage;
        TextView messageText;
        CheckBox checkBox;
        View divider;

        ListViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View itemView) {
            contentLayout = (LinearLayout)itemView.findViewById(R.id.content_layout);
            iconImage = (ImageView)itemView.findViewById(R.id.icon_image);
            messageText = (TextView)itemView.findViewById(R.id.message_text);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkbox);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
