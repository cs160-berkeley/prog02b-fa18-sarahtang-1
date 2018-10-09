package com.example.sarahtang.representv2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CongressAdapter extends RecyclerView.Adapter<CongressAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    private Context context;
    private ArrayList<Representative> listRepresentatives;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private TextView partyView, typeView;
        private CardView cardView;
        private ImageView repPhoto;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
            partyView = (TextView) v.findViewById(R.id.partyView);
            cardView = (CardView) v.findViewById(R.id.filterListCardView);
            typeView = (TextView) v.findViewById(R.id.typeView);
            repPhoto = (ImageView) v.findViewById(R.id.imageView);
        }
    }


    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CongressAdapter(Context context, ArrayList<Representative> dataSet) {
        listRepresentatives = dataSet;
        this.context = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(v);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        final Representative rep = listRepresentatives.get(position);

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        String name = listRepresentatives.get(position).getFirst_name()
                + " " + listRepresentatives.get(position).getLast_name();
        String party = listRepresentatives.get(position).getParty();
        String type = listRepresentatives.get(position).getRepresentative_type();
        String upperType = type.substring(0,1).toUpperCase() + type.substring(1);
        viewHolder.textView.setText(name);
        viewHolder.partyView.setText(party);
        viewHolder.typeView.setText(upperType);
//        if (party == "Democrat") {
//            viewHolder.cardView.setCardBackgroundColor();
//        }

        final String bioguide_id = listRepresentatives.get(position).getBioguide_id();
        final String first_bio = bioguide_id.substring(0, 1);
        String photo = "http://bioguide.congress.gov/bioguide/photo/" + first_bio + "/"
                + bioguide_id + ".jpg";
        //Photo is getting correct website
        //Get photo!
        Picasso.get().load(photo).into(viewHolder.repPhoto);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("rep", rep);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listRepresentatives.size();
    }

}
