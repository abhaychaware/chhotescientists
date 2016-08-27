package com.kpit.chhotescientists.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
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
    SessionViewHolder currentExpandedViewHolder;

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
        final Session session = items.get(position);
        holder.itemLayout.setVisibility(View.GONE);

        setDetailText(holder.titleTextView, session.location);
        setDetailText(holder.dateTextView, session.getDateString());
        setDetailText(holder.themeTextView, session.theme, R.string.theme_x);
        setDetailText(holder.expectedStudentCountTextView, session.expectedStudentCount, R.string.n_students_expected);

        // Create buttons for the questions:
        for (final SessionEvent event : session.events) {
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
                    sessionCheckInIntent.putExtra(SessionCheckInActivity.SESSION_KEY, session);
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

                    // Collapse the card:
                    holder.expandArrow.startAnimation(rotateDownAnimation);
                    holder.itemLayout.setVisibility(View.GONE);
                } else {
                    collapseCurrentExpandedCard();

                    // Expand the card:
                    holder.expandArrow.startAnimation(rotateUpAnimation);
                    holder.itemLayout.setVisibility(View.VISIBLE);

                    // Keep a reference to this card, so we can collapse it when we open a different card
                    currentExpandedViewHolder = holder;
                }
            }
        });
    }

    private void setDetailText(TextView textView, String text) {
        if (textView != null && !TextUtils.isEmpty(text)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        } else if (textView != null) {
            textView.setVisibility(View.GONE);
        }
    }

    /**
     * Helper method for formatting strings and then setting detail text.
     */
    private void setDetailText(TextView textView, String formatStringParam, @StringRes int formatStringId) {
        String formattedText = context.getString(formatStringId, formatStringParam);
        setDetailText(textView, formattedText);
    }

    /**
     * Collapse the currently expanded card to make room
     *  for another card to be expanded.
     */
    private void collapseCurrentExpandedCard() {
        if (this.currentExpandedViewHolder != null) {
            final Animation rotateDownAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_flip_down);
            rotateDownAnimation.setFillAfter(true);

            this.currentExpandedViewHolder.expandArrow.startAnimation(rotateDownAnimation);
            this.currentExpandedViewHolder.itemLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}
