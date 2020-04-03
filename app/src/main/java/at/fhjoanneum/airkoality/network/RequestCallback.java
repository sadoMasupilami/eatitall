package at.fhjoanneum.airkoality.network;

public interface RequestCallback {
    void onResult(String result);
    void onRequestStart();
}
