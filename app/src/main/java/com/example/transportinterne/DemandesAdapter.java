package com.example.transportinterne;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;

public class DemandesAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> demandesList;
    private HashMap<String, Demande> demandesMap;
    private DatabaseReference databaseReference;

    public DemandesAdapter(Context context, ArrayList<String> demandesList, HashMap<String, Demande> demandesMap) {
        super(context, R.layout.item_demandes, demandesList);
        this.context = context;
        this.demandesList = demandesList;
        this.demandesMap = demandesMap;
        this.databaseReference = FirebaseDatabase.getInstance("https://transportinterne-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("demandes");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_demandes, parent, false);
            holder = new ViewHolder();
            holder.tvDemande = convertView.findViewById(R.id.tvDemande);
            holder.btnAccepter = convertView.findViewById(R.id.btnAccepter);
            holder.btnRefuser = convertView.findViewById(R.id.btnRefuser);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String demandeId = demandesList.get(position);
        Demande demande = demandesMap.get(demandeId);
        holder.tvDemande.setText("Demande ID: " + demandeId);
        holder.btnAccepter.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous vraiment accepter cette demande ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        databaseReference.child(demandeId).child("statut").setValue("acceptee")
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("DemandesAdapter", "Statut mis à jour avec succès pour la demande: " + demandeId);

                                        demandesList.remove(position);
                                        demandesMap.remove(demandeId);
                                        // Notify the adapter that the data has changed
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Demande acceptée !", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("DemandesAdapter", "Erreur lors de la mise à jour du statut: " + task.getException().getMessage());
                                        Toast.makeText(context, "Erreur lors de l'acceptation de la demande.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });
        holder.btnRefuser.setOnClickListener(v -> {
            Intent intent = new Intent(context, RefusActivity.class);
            intent.putExtra("demandeId", demandeId);
            context.startActivity(intent);
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tvDemande;
        Button btnAccepter;
        Button btnRefuser;
    }
}