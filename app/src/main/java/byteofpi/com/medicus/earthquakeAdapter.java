package byteofpi.com.medicus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by root on 3/5/17.
 */

public class earthquakeAdapter extends RecyclerView.Adapter<earthquakeAdapter.MyViewHolder> {
    private Context mContext;
    private List<earthquakes> earthquakesList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView region, datetime,magnitude,longitude,latitude;


        public MyViewHolder(View view) {
            super(view);
            region = (TextView) view.findViewById(R.id.region);
            datetime = (TextView) view.findViewById(R.id.datetime);
            magnitude=(TextView)view.findViewById(R.id.magnitude);
            longitude = (TextView) view.findViewById(R.id.longitude);
            latitude = (TextView) view.findViewById(R.id.latitude);
        }
    }
    public earthquakeAdapter(Context mContext, List<earthquakes> earthquakesList) {
        this.mContext = mContext;
        this.earthquakesList = earthquakesList;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        earthquakes earthquake = earthquakesList.get(position);
        holder.region.setText("Region:- " + earthquake.getRegion());
        holder.datetime.setText("Date and Time:- "+earthquake.getDatetime());
        holder.magnitude.setText("Magnitude:-  "+earthquake.getMagnitude());
        holder.latitude.setText("Longitude:-  "+earthquake.getLatitude());
        holder.longitude.setText("Longitude:-  "+earthquake.getLongitude());

        // loading album cover using Glide library

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.earthquake_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return earthquakesList.size();
    }
}
