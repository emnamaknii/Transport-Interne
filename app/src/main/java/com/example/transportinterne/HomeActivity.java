package com.example.transportinterne;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    private ListView listView;
    private DemandesAdapter demandesAdapter;
    private ArrayList<String> demandesList;
    private DatabaseReference databaseReference;
    private HashMap<String, Demande> demandesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = findViewById(R.id.listViewDemandes);
        demandesList = new ArrayList<>();
        demandesMap = new HashMap<>();

        // Initialisation de l'adaptateur personnalisé
        demandesAdapter = new DemandesAdapter(this, demandesList, demandesMap);
        listView.setAdapter(demandesAdapter);

        // Référence à la base de données Firebase
        databaseReference = FirebaseDatabase.getInstance("https://transportinterne-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference()
                .child("demandes");

        // Écoute des mises à jour en temps réel dans Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("HomeActivity", "Data fetched from Firebase: " + snapshot.getChildrenCount() + " items");
                demandesList.clear();
                demandesMap.clear();

                // Parcours des données dans Firebase
                for (DataSnapshot data : snapshot.getChildren()) {
                    String id = data.getKey();
                    Demande demande = data.getValue(Demande.class);

                    if (demande != null && "en_attente".equals(demande.getStatut())) {
                        demandesList.add(id);
                        demandesMap.put(id, demande);
                    }
                }

                // Mise à jour de l'adaptateur
                Log.d("HomeActivity", "Demandes list updated with " + demandesList.size() + " items");
                demandesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeActivity", "Error fetching data: " + error.getMessage());
                Toast.makeText(HomeActivity.this, "Erreur : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
