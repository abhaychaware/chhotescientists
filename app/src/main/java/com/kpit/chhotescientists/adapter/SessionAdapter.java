package com.kpit.chhotescientists.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

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
        holder.itemLayout.setVisibility(View.GONE);

        // Set location as title
        if (!TextUtils.isEmpty(item.location)) {
            holder.titleTextView.setVisibility(View.VISIBLE);
            holder.titleTextView.setText(item.location);
        } else {
            holder.titleTextView.setVisibility(View.GONE);
        }

        // Set date textview
        if (!TextUtils.isEmpty(item.getDateString())) {
            holder.dateTextView.setVisibility(View.VISIBLE);
            holder.dateTextView.setText(item.getDateString());
        } else {
            holder.dateTextView.setVisibility(View.GONE);
        }

        // Set theme text
        if (!TextUtils.isEmpty(item.theme)) {
            holder.themeTextView.setVisibility(View.VISIBLE);
            holder.themeTextView.setText(
                    context.getString(R.string.theme_x, item.theme)
            );
        } else {
            holder.themeTextView.setVisibility(View.GONE);
        }

        // Set expected students text
        if (!TextUtils.isEmpty(item.expectedStudentCount)) {
            holder.expectedStudentCountTextView.setVisibility(View.VISIBLE);
            holder.expectedStudentCountTextView.setText(
                    context.getString(R.string.n_students_expected, item.expectedStudentCount)
            );
        } else {
            holder.expectedStudentCountTextView.setVisibility(View.GONE);
        }

        // Create buttons for the questions:
        for (final SessionEvent event : item.events) {
            TextView eventLink = (TextView) LayoutInflater.from(context)
                    .inflate(R.layout.button_questionnaire_link, holder.itemLayout, false);
            eventLink.setText(event.title);

            // Set an event's button to open the questionnaire/check-in activity
            //  and pass in the parcelable Event object.
            eventLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sessionCheckInIntent = new Intent();
                    sessionCheckInIntent.setClass(context, SessionCheckInActivity.class);
                    sessionCheckInIntent.putExtra(SessionCheckInActivity.EVENT_KEY, event);
                    context.startActivity(sessionCheckInIntent);
                }
            });

            holder.itemLayout.addView(eventLink);
        }

        // Animate the dropdown arrow depending on whether the card is expanded or collapsed:
        final Animation rotateUpAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_flip_up);
        rotateUpAnimation.setFillAfter(true);
        final Animation rotateDownAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_flip_down);
        rotateDownAnimation.setFillAfter(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.itemLayout.getVisibility() == View.VISIBLE) {
                    holder.expandArrow.startAnimation(rotateDownAnimation);
                    holder.itemLayout.setVisibility(View.GONE);
                } else {
                    //hide all other cards
                    hideAllCards();
                    holder.expandArrow.startAnimation(rotateUpAnimation);
                    holder.itemLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void hideAllCards() {
        //TODO write function to hide all the cards
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
