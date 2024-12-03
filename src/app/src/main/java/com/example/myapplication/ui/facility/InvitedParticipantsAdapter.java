package com.example.myapplication.ui.facility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class InvitedParticipantsAdapter extends RecyclerView.Adapter<InvitedParticipantsAdapter.ViewHolder> {

    private ArrayList<String> invitedNames;

    public InvitedParticipantsAdapter(ArrayList<String> invitedNames) {
        this.invitedNames = invitedNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invited_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the name to the TextView
        holder.nameTextView.setText(invitedNames.get(position));
    }

    @Override
    public int getItemCount() {
        return invitedNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);  // Assuming you have a TextView with this ID in the layout
        }
    }
}
