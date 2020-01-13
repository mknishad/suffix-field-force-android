package com.suffix.fieldforce.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.model.Task;

import java.util.ArrayList;
import java.util.List;

public class SearchTaskAdapter extends ArrayAdapter<Task> {
  private Context context;
  private int resourceId;
  private List<Task> items, tempItems, suggestions;

  public SearchTaskAdapter(@NonNull Context context, int resourceId, List<Task> items) {
    super(context, resourceId, items);
    this.items = items;
    this.context = context;
    this.resourceId = resourceId;
    tempItems = new ArrayList<>(items);
    suggestions = new ArrayList<>();
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View view = convertView;
    try {
      if (convertView == null) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        view = inflater.inflate(resourceId, parent, false);
      }
      Task task = getItem(position);
      TextView taskTitleText = view.findViewById(R.id.taskTitleText);
      TextView taskDescriptionText = view.findViewById(R.id.taskDescriptionText);
      TextView taskDateText = view.findViewById(R.id.taskDateTextView);
      taskTitleText.setText(task.getTicketTitle());
      taskDescriptionText.setText(task.getTicketDesc());
      taskDateText.setText(task.getAddDate());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return view;
  }

  @Nullable
  @Override
  public Task getItem(int position) {
    return items.get(position);
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @NonNull
  @Override
  public Filter getFilter() {
    return fruitFilter;
  }

  private Filter fruitFilter = new Filter() {
    @Override
    public CharSequence convertResultToString(Object resultValue) {
      Task task = (Task) resultValue;
      return task.getTicketTitle();
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
      if (charSequence != null) {
        suggestions.clear();
        for (Task task : tempItems) {
          if (task.getTicketTitle().toLowerCase().startsWith(charSequence.toString().toLowerCase())
          || task.getTicketDesc().toLowerCase().startsWith(charSequence.toString().toLowerCase())
          || task.getAddDate().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
            suggestions.add(task);
          }
        }
        FilterResults filterResults = new FilterResults();
        filterResults.values = suggestions;
        filterResults.count = suggestions.size();
        return filterResults;
      } else {
        return new FilterResults();
      }
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
      ArrayList<Task> tempValues = (ArrayList<Task>) filterResults.values;
      if (filterResults != null && filterResults.count > 0) {
        clear();
        for (Task taskObj : tempValues) {
          add(taskObj);
        }
        notifyDataSetChanged();
      } else {
        clear();
        notifyDataSetChanged();
      }
    }
  };
}
