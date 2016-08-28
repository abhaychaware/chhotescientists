package com.kpit.chhotescientists.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpit.chhotescientists.R;

/**
 * Created by grahamearley on 7/16/16.
 */
public class SessionViewHolder extends RecyclerView.ViewHolder {

    public View itemView;

    public TextView titleTextView;
    public TextView dateTextView;
    public TextView themeTextView;
    public TextView expectedStudentCountTextView;

    public LinearLayout itemLayout;
    public View expandArrow;

    public SessionViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;

        this.titleTextView = (TextView) itemView.findViewById(R.id.title_text);
        this.dateTextView = (TextView) itemView.findViewById(R.id.date_text);
        this.themeTextView = (TextView) itemView.findViewById(R.id.theme_text);
        this.expectedStudentCountTextView = (TextView) itemView.findViewById(R.id.expected_student_count_text);

        this.itemLayout = (LinearLayout) itemView.findViewById(R.id.event_item_layout);
        this.expandArrow = itemView.findViewById(R.id.expand_arrow);
    }
}
