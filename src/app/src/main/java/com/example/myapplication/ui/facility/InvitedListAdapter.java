package com.example.myapplication.ui.facility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.User;

import java.util.ArrayList;

public class InvitedListAdapter extends RecyclerView.Adapter<InvitedListAdapter.ViewHolder> {

    private final ArrayList<User> invitedParticipants;

    public InvitedListAdapter(ArrayList<User> invitedParticipants) {
        this.invitedParticipants = invitedParticipants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiting_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = invitedParticipants.get(position);
        holder.nameTextView.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return invitedParticipants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}
