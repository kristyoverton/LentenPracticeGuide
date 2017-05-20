package whiletrue.ado.marionberryjam;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder> {

    private ArrayList<Practice> practices = new ArrayList<Practice>();
    private Activity contextActivity;
    private String dayInLent;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mCardView;
        TextView teaser;
        TextView subteaser;
        ImageView icon;
        String practiceId;

        public ViewHolder(CardView v) {
            super(v);
            mCardView = v;
            teaser = (TextView) v.findViewById(R.id.teaser);
            subteaser = (TextView) v.findViewById(R.id.subteaser);
            icon = (ImageView) v.findViewById(R.id.icon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PracticeAdapter(String practicesString, Set<String> dismissedPractices, Activity activity, String day) {
        contextActivity = activity;
        dayInLent = day;
        String dayInWeek = (Integer.valueOf(dayInLent) % 7 == 0) ? "7" : String.valueOf((Integer.valueOf(dayInLent) % 7));
            try {
                JSONArray jsonData = new JSONArray(practicesString);
                for (int i = 0; i < jsonData.length(); i++) {
                    Practice practice = new Practice(jsonData.getJSONObject(i));
                    if (!dismissedPractices.contains(practice.getId()) &&
                            (practice.getDay().isEmpty() || practice.getDay().equals(dayInWeek))) {
                        practices.add(practice);
                    }
                }
            }
            catch (JSONException e) {
                String msg = e.getMessage();
            }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public PracticeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.practice_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.teaser.setText(practices.get(position).getTeaser());
        holder.subteaser.setText(practices.get(position).getSubteaser());
        int imageID = contextActivity.getResources().getIdentifier("drawable/"+practices.get(position).getType().toString().toLowerCase(), null, contextActivity.getPackageName());
        holder.icon.setImageResource(imageID);
        holder.practiceId = practices.get(position).getId();
        holder.mCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int p = holder.getAdapterPosition();
                Intent intent = new Intent(contextActivity, ReadingActivity.class);
                String test = String.valueOf(practices.get(p).getType());
                intent.putExtra("whiletrue.ado.marionberryjam.BODY_TITLE", String.valueOf(practices.get(p).getType()));
                intent.putExtra("whiletrue.ado.marionberryjam.BODY", practices.get(p).getbody());
                contextActivity.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return practices.size();
    }


    public void onItemDismissed(int position) {
        Set<String> dismissedIDs = contextActivity.getSharedPreferences("MarionberryJam", Context.MODE_PRIVATE).getStringSet(dayInLent, new HashSet<String>());
        Set<String> dismissedIDsCopy = new HashSet<String>();

        for (String id : dismissedIDs) {
            dismissedIDsCopy.add(new String(id));
        }

        dismissedIDsCopy.add(practices.get(position).getId());

        SharedPreferences.Editor editor = contextActivity.getSharedPreferences("MarionberryJam", Context.MODE_PRIVATE).edit();
        editor.putStringSet("LENT_DISMISSED", dismissedIDsCopy);
        editor.apply();

        practices.remove(position);
        notifyItemRemoved(position);
    }



}
