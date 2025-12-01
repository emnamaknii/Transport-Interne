package com.example.transportinterne;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RefusActivity extends AppCompatActivity {
    private RadioGroup radioGroupRaisons;
    private Button btnValider;
    private DatabaseReference databaseReference;
    private String demandeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refus);

        // Initialisation des éléments de l'interface
        radioGroupRaisons = findViewById(R.id.radioGroupRefus);
        btnValider = findViewById(R.id.btnValiderRefus);

        // Récupération de l'ID de la demande passée via Intent
        demandeId = getIntent().getStringExtra("demandeId");
        if (demandeId == null || demandeId.isEmpty()) {
            Toast.makeText(this, "ID de demande invalide", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Référence à la base de données Firebase
        databaseReference = FirebaseDatabase.getInstance("https://transportinterne-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("demandes");

        // Définir un événement de clic pour le bouton "Valider"
        btnValider.setOnClickListener(view -> enregistrerRefus());
    }

    private void enregistrerRefus() {
        // Vérification de la raison sélectionnée
        int selectedRadioId = radioGroupRaisons.getCheckedRadioButtonId();
        if (selectedRadioId == -1) {
            Toast.makeText(this, "Veuillez sélectionner une raison", Toast.LENGTH_SHORT).show();
            return;
        }

        // Récupération de la raison sélectionnée
        RadioButton selectedRadioButton = findViewById(selectedRadioId);
        String raison = selectedRadioButton.getText().toString();

        // Préparation des mises à jour pour Firebase
        Map<String, Object> updates = new HashMap<>();
        updates.put("statut", "refuser");
        updates.put("raison", raison);
        updates.put("timestamp", 0);

        // Mise à jour de la base de données Firebase
        databaseReference.child(demandeId).updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    // Succès de la mise à jour
                    Toast.makeText(RefusActivity.this, "Refus enregistré", Toast.LENGTH_SHORT).show();
                    // Redirection vers HomeActivity
                    Intent intent = new Intent(RefusActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Échec de la mise à jour
                    Toast.makeText(RefusActivity.this, "Erreur d'enregistrement : " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
