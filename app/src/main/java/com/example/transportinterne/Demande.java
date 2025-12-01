package com.example.transportinterne;

public class Demande {
    private String statut;
    private Long timestamp;
    private String raison;

    public Demande() {
        // Default constructor required for calls to DataSnapshot.getValue(Demande.class)
    }

    public Demande(String statut, Long timestamp, String raison) {
        this.statut = statut;
        this.timestamp = timestamp;
        this.raison = raison;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }
}