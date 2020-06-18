package at.fhjoanneum.eatitall.network;

public interface RequestCallback {
    void onResult(String result);
    void onRequestStart();
}
