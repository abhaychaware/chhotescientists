package com.kpit.chhotescientists.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.kpit.chhotescientists.R;
import com.kpit.chhotescientists.activity.SessionCheckInActivity;
import com.kpit.chhotescientists.adapter.viewholders.SessionViewHolder;
import com.kpit.chhotescientists.model.Session;
import com.kpit.chhotescientists.model.SessionEvent;

import java.util.List;

/**
 * An adapter for filling a RecyclerView with Schedule/Session data cards.
 *  Each card can be tapped to reveal buttons. These buttons will lead to
 *  a questionnaire activity.
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

        // Create buttons for the questions:
        for (final SessionEvent event : item.events) {
            Button eventLinkButton = new Button(context);
            eventLinkButton.setText(event.title);

            // Set an event's button to open the questionnaire/check-in activity
            //  and pass in the parcelable Event object.
            eventLinkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sessionCheckInIntent = new Intent();
                    sessionCheckInIntent.setClass(context, SessionCheckInActivity.class);
                    sessionCheckInIntent.putExtra(SessionCheckInActivity.EVENT_KEY, event);
                    context.startActivity(sessionCheckInIntent);
                }
            });

            holder.getItemLayout().addView(eventLinkButton);
        }

        // Animate the dropdown arrow depending on whether the card is expanded or collapsed:
        final Animation rotateUpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_flip_up);
        rotateUpAnimation.setFillAfter(true);
        final Animation rotateDownAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_flip_down);
        rotateDownAnimation.setFillAfter(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getItemLayout().getVisibility() == View.VISIBLE) {
                    holder.expandArrow.startAnimation(rotateDownAnimation);
                    holder.getItemLayout().setVisibility(View.GONE);
                } else {
                    holder.expandArrow.startAnimation(rotateUpAnimation);
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
