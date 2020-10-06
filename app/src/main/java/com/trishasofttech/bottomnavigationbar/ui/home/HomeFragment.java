package com.trishasofttech.bottomnavigationbar.ui.home;

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
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.trishasofttech.bottomnavigationbar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    EditText etname, etmobile;
    Button btn_submit;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        etname = v.findViewById(R.id.etname);
        etmobile = v.findViewById(R.id.etmobile);
        btn_submit = v.findViewById(R.id.btn_submit);
        recyclerView = v.findViewById(R.id.recyclerview);


        /*to fetch the server record by default when app open*/
        displayrecord();


        /*to click on the button submit*/
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*after click on the submit button one time*/
                /*submit button should not be clickable*/
                btn_submit.setClickable(false);
                /*data should not be null*/
                if (etname.getText().toString().isEmpty() || etmobile.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Please fill the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                        datasendToserver();

                }
            }
        });

        return v;
    }

    private void displayrecord() {
        StringRequest sr = new StringRequest(0,
                "http://searchkero.com/calldir/showrecord.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        /*jsonparsing*/
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONArray jArray = jObj.getJSONArray("Data");
                            for (int i=0; i< jArray.length(); i++)
                            {
                                JSONObject jObj1 = jArray.getJSONObject(i);
                                String id = jObj1.getString("id");
                                String name = jObj1.getString("name");
                                String email = jObj1.getString("mobile");
                                String date = jObj1.getString("date");

                                //Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();

                                /*add your data in hashmap using keyid and key value*/
                                HashMap<String, String> h = new HashMap<>();
                                h.put("namekey", name);
                                h.put("idkey", id);
                                h.put("emailkey", email);
                                h.put("datekey", date);
                                /*add the hashmap data into arraylist*/
                                /*add all data into arraylist*/
                                arrayList.add(h);
                                Toast.makeText(getActivity(), arrayList.get(i).toString(), Toast.LENGTH_SHORT).show();
                            }
                            /*to fix the recyclerview area size to be display and fetch from api side*/
                            recyclerView.setHasFixedSize(true);
                            /*recyclerview in linear form*/
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            /*to call the Adapter for attaching the data into recyclerview*/
                            RachitAdapter rachit = new RachitAdapter();
                            recyclerView.setAdapter(rachit);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(sr);
    }

    private void datasendToserver() {
        StringRequest sr = new StringRequest(1, "http://searchkero.com/calldir/updaterecord.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        /*to clear the form after submit to server*/
                        etname.setText("");
                        etmobile.setText("");
                        /*turn on the button submit clickable*/
                        btn_submit.setClickable(true);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                /*to add the data into hashmap object*/
                map.put("namekey", etname.getText().toString());
                map.put("mobilekey", etmobile.getText().toString());
                return map;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(sr);
    }

    private class RachitAdapter extends RecyclerView.Adapter<RachitHolder> {

        @NonNull
        @Override
        public RachitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent,
                    false);
            RachitHolder rachitHolder = new RachitHolder(v);
            return rachitHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RachitHolder holder, int position) {
            /*to attach the data to item object*/
            final HashMap<String, String> h = arrayList.get(position);
            //Toast.makeText(getActivity(), h.toString(), Toast.LENGTH_SHORT).show();
            holder.tv_name.setText(h.get("namekey"));
            holder.tv_mobile.setText(h.get("emailkey"));
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /*to set/display the data into etname, etmobile*/
                   etmobile.setText(h.get("emailkey"));
                   etname.setText(h.get("namekey"));
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public class RachitHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_mobile;
        ConstraintLayout constraintLayout;;
        public RachitHolder(@NonNull View itemView) {
            super(itemView);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            tv_name = itemView.findViewById(R.id.tv_name);
            constraintLayout = itemView.findViewById(R.id.constraintlayout);
        }
    }
}