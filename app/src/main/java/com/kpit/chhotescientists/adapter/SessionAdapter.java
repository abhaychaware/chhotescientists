package com.kpit.chhotescientists.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.adapter.viewholders.SessionViewHolder;
import com.kpit.chhotescientists.model.CheckInItem;
import com.kpit.chhotescientists.model.CheckInQuestion;
import com.kpit.chhotescientists.model.Session;
import com.kpit.chhotescientists.model.SessionEvent;

import java.util.List;

/**
 * Created by grahamearley on 7/16/16.
 */
public class SessionAdapter extends RecyclerView.Adapter<SessionViewHolder> {

    List<Session> items;
    Context context;

    public SessionAdapter(List<Session> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_session, parent, false);

        return new SessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SessionViewHolder holder, int position) {
        Session item = items.get(position);
        holder.getItemLayout().setVisibility(View.GONE);

        holder.getTitleTextView().setText(item.date);
        holder.getSubtitleTextView().setText(item.location);

        // Create views from the questions:
        for (SessionEvent event : item.events) {
            Button eventLinkButton = new Button(context);
            eventLinkButton.setText(event.title);

            holder.getItemLayout().addView(eventLinkButton);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getItemLayout().getVisibility() == View.VISIBLE) {
                    holder.getItemLayout().setVisibility(View.GONE);
                } else {
                    holder.getItemLayout().setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
