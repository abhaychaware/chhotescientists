package com.kpit.chhotescientists.adapter.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.kpit.chhotescientists.R;

/**
 * Created by grahamearley on 7/16/16.
 */
public class CheckInViewHolder extends RecyclerView.ViewHolder {

    LinearLayout itemLayout;

    public CheckInViewHolder(View itemView) {
        super(itemView);

        itemLayout = (LinearLayout) itemView.findViewById(R.id.check_in_item_layout);
    }

    public LinearLayout getItemLayout() {
        return this.itemLayout;
    }
}
