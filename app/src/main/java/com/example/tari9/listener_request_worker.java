package com.example.tari9;
import com.example.tari9.data.get_requests;

public interface listener_request_worker {
    void onItemClicked(String document_id, get_requests request, int position);
}
