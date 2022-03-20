package com.example.snow_scrapper.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.snow_scrapper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class OrderDetailsFragment extends Fragment {

    private static final String TAG = "OrderDetailsFragment";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String orderId;

    public OrderDetailsFragment(String orderId){
        Log.d("Zhou3333", orderId);
        this.orderId = orderId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getOrderDetailsById(mAuth.getUid(), this.orderId);

        Button btnPurchase = getActivity().findViewById(R.id.order_details_back);
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                fragment = new OrdersFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName())
                        .commit();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName());
                transaction.commit();

                BottomNavigationView bnv = getActivity().findViewById(R.id.bottom_navigation);
                bnv.setSelectedItemId(R.id.orders);
            }
        });

        FloatingActionButton btnCart = getActivity().findViewById(R.id.order_details_feedback);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getActivity().findViewById(R.id.snackbar_feedback), "Feedback function is coming~",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void getOrderDetailsById(String uid, String order_id) {

        db.collection("orders")
                .whereEqualTo(FieldPath.documentId(), order_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView item_name = getActivity().findViewById(R.id.order_details_item_name);
                                item_name.setText(document.getString("name"));

                                TextView location = getActivity().findViewById(R.id.order_details_item_location);
                                location.setText(document.getString("location"));

                                TextView seller = getActivity().findViewById(R.id.order_details_item_seller);
                                seller.setText(document.getString("seller"));

                                ImageView image = getActivity().findViewById(R.id.order_details_item_image);
                                image.setImageResource(R.drawable.snow1);
                                // document.getString("image")

                                TextView price = getActivity().findViewById(R.id.order_details_item_price);
                                price.setText(document.getString("price"));

                                TextView rating = getActivity().findViewById(R.id.order_details_item_rating);
                                rating.setText(document.getString("rating"));

                                TextView range = getActivity().findViewById(R.id.order_details_item_range);
                                range.setText(document.getString("range"));

//                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

}