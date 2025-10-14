package com.example.techmatch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.techmatch.R;
import com.example.techmatch.models.Project;
import java.util.List;

public class TeamListAdapter extends BaseAdapter {

    private Context context;
    private List<Project> projects;
    private OnJoinClickListener listener;

    public interface OnJoinClickListener {
        void onJoinClick(Project project);
    }

    public TeamListAdapter(Context context, List<Project> projects, OnJoinClickListener listener) {
        this.context = context;
        this.projects = projects;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public Object getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return projects.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_team, parent, false);
            holder = new ViewHolder();
            holder.tvTeamName = convertView.findViewById(R.id.tvTeamName);
            holder.tvProjectDescription = convertView.findViewById(R.id.tvProjectDescription);
            holder.tvAdvisor = convertView.findViewById(R.id.tvAdvisor);
            holder.tvTeamSize = convertView.findViewById(R.id.tvTeamSize);
            holder.btnJoin = convertView.findViewById(R.id.btnJoin);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Project project = projects.get(position);

        // Bilgileri doldur
        holder.tvTeamName.setText(project.getName());
        holder.tvProjectDescription.setText(project.getDescription());
        holder.tvAdvisor.setText(project.getAdvisor());
        holder.tvTeamSize.setText(project.getCurrentParticipants() + "/" + project.getMaxParticipants() + " kişi");

        // Takım dolu mu kontrol et
        if (project.isFull()) {
            holder.btnJoin.setText("Takım Dolu");
            holder.btnJoin.setEnabled(false);
            holder.btnJoin.setBackgroundColor(0xFF999999); // Gri
        } else {
            holder.btnJoin.setText("Takıma Katıl");
            holder.btnJoin.setEnabled(true);
            holder.btnJoin.setBackgroundColor(0xFF6200EE); // Mor
        }

        // Katıl butonu click
        holder.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onJoinClick(project);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvTeamName;
        TextView tvProjectDescription;
        TextView tvAdvisor;
        TextView tvTeamSize;
        Button btnJoin;
    }
}