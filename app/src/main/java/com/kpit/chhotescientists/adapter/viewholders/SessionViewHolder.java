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
    public View expandArrow;
    TextView titleTextView;
    TextView subtitleTextView;
    LinearLayout itemLayout;

    public SessionViewHolder(View itemView) {
        super(itemView);

        this.itemView = itemView;

        this.titleTextView = (TextView) itemView.findViewById(R.id.title_text);
        this.subtitleTextView = (TextView) itemView.findViewById(R.id.subtitle_text);
        this.itemLayout = (LinearLayout) itemView.findViewById(R.id.event_item_layout);
        this.expandArrow = itemView.findViewById(R.id.expand_arrow);
    }

    public LinearLayout getItemLayout() {
        return this.itemLayout;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getSubtitleTextView() {
        return subtitleTextView;
    }
}
