package com.example.android.sunshine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by tmagalhaes on 05/04/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private String[] mWeatherData;

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item, parent, false);
        return (new ForecastAdapterViewHolder(view));
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        if(mWeatherData != null && mWeatherData.length > 0) {
            holder.bind(mWeatherData[position]);
        }
    }

    @Override
    public int getItemCount() {
        if(mWeatherData == null) {
            return 0;
        } else {
            return mWeatherData.length;
        }
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View itemView) {
            super(itemView);
            mWeatherTextView = (TextView) itemView.findViewById(R.id.tv_weather_data);
        }

        public void bind(String weatherDataPosition) {
            mWeatherTextView.setText(weatherDataPosition);
        }
    }

    public void setmWeatherData(String[] mWeatherData) {
        this.mWeatherData = mWeatherData;
        notifyDataSetChanged();
    }
}
