package com.trishasofttech.bottomnavigationbar.ui.deleterecord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.trishasofttech.bottomnavigationbar.R;

import java.util.HashMap;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    Button btn_delete;
    EditText et_mobile;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        btn_delete = root.findViewById(R.id.btn_delete);
        et_mobile = root.findViewById(R.id.et_mobile);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRecord();
            }
        });
        return root;
    }

    private void deleteRecord() {
        StringRequest sr = new StringRequest(1, "http://searchkero.com/calldir/deleterecord.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        et_mobile.setText("");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mobilekey", et_mobile.getText().toString());
                return map;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(sr);
    }
}