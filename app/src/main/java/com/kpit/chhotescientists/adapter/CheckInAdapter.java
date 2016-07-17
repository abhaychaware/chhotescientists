package com.kpit.chhotescientists.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.adapter.viewholders.CheckInViewHolder;
import com.kpit.chhotescientists.model.CheckInItem;
import com.kpit.chhotescientists.model.CheckInQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grahamearley on 7/16/16.
 */
public class CheckInAdapter extends RecyclerView.Adapter<CheckInViewHolder> {

    List<CheckInItem> items;
    Context context;

    public CheckInAdapter(List<CheckInItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public CheckInViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_check_in, parent, false);

        return new CheckInViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CheckInViewHolder holder, int position) {
        CheckInItem item = items.get(position);

        // Create views from the questions:
        for (CheckInQuestion question : item.getQuestions()) {
            TextView itemQuestionText = new TextView(context);
            itemQuestionText.setText(question.getQuestionText());
            itemQuestionText.setTextSize(18);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            holder.getItemLayout().addView(itemQuestionText, layoutParams);
            holder.getItemLayout().addView(question.getQuestionView(context), layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
