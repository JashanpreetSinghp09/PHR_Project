package com.example.phr_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HealthReportAdapter extends RecyclerView.Adapter<HealthReportAdapter.ViewHolder> {

    private final List<String> healthReportsList;

    public HealthReportAdapter(List<String> healthReportsList) {
        this.healthReportsList = healthReportsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String healthReport = healthReportsList.get(position);
        holder.healthReportTextView.setText(healthReport);
    }

    @Override
    public int getItemCount() {
        return healthReportsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView healthReportTextView;

        public ViewHolder(View view) {
            super(view);
            healthReportTextView = view.findViewById(R.id.healthReportTextView);
        }
    }
}
