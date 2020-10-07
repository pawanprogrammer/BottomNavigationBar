package com.trishasofttech.bottomnavigationbar.ui.searchrecord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.trishasofttech.bottomnavigationbar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {

    /*to attach the layout xml file with fragment*/
    Button btn_search;
    EditText etmobile;
    TextView tv_record;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        btn_search = v.findViewById(R.id.btn_search);
        etmobile = v.findViewById(R.id.etmobile);
        tv_record = v.findViewById(R.id.tv_record);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRecord();
            }
        });
        return v;
    }

    private void searchRecord() {
        StringRequest sr = new StringRequest(1, "http://searchkero.com/calldir/searchrecord.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                etmobile.setText("");

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray jArray = jObj.getJSONArray("data");
                    for(int i = 0; i<jArray.length(); i++)
                    {
                        JSONObject jObj1 = jArray.getJSONObject(i);
                        tv_record.setText(jObj1.getString("name")+ "\n"+ jObj1.getString("mobile"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mobilekey", etmobile.getText().toString().trim());
                return map;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(sr);
    }
}
