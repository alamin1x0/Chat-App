package com.developeralamin.goponchart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import es.dmoral.toasty.Toasty;

public class ChartFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;

    ImageView mimageviewofuser;

    FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder> chartAdapter;

    RecyclerView mrecyclerview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.chartfragment,container, false);

       firebaseAuth = FirebaseAuth.getInstance();
       firebaseFirestore = FirebaseFirestore.getInstance();
       mrecyclerview = view.findViewById(R.id.recyvlerView);


       // Query query = firebaseFirestore.collection("Users");

        Query query = firebaseFirestore.collection("Users").whereNotEqualTo("uid",firebaseAuth.getUid());
        FirestoreRecyclerOptions<FirebaseModel> allusername = new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query, FirebaseModel.class).build();


       chartAdapter = new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(allusername) {
           @Override
           protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull FirebaseModel FirebaseModel) {


               noteViewHolder.particularusername.setText(FirebaseModel.getName());
               String uri = FirebaseModel.getImage();

               Picasso.get().load(uri).into(mimageviewofuser);
               if (FirebaseModel.getStatus().equals("Online"))
               {
                   noteViewHolder.statusofuser.setText(FirebaseModel.getStatus());
                   noteViewHolder.statusofuser.setTextColor(Color.GREEN);
               }
               else {
                   noteViewHolder.statusofuser.setText(FirebaseModel.getStatus());
                   
               }
               noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(getActivity(),Specifchart.class);
                       intent.putExtra("name",FirebaseModel.getName());
                       intent.putExtra("receiveruid",FirebaseModel.getUid());
                       intent.putExtra("imageuri",FirebaseModel.getImage());
                       startActivity(intent);
                   }
               });
           }

           @NonNull
           @Override
           public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chartviewaclayout,parent,false);
              return  new NoteViewHolder(view);
           }
       };

       mrecyclerview.setHasFixedSize(true);
       linearLayoutManager = new LinearLayoutManager(getContext());
       linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
       mrecyclerview.setLayoutManager(linearLayoutManager);
       mrecyclerview.setAdapter(chartAdapter);

       return view;

    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView particularusername;
        private TextView statusofuser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            particularusername = itemView.findViewById(R.id.nameofuser);
            statusofuser = itemView.findViewById(R.id.statusofuser);
            mimageviewofuser = itemView.findViewById(R.id.imageviewofuser);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        chartAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (chartAdapter!=null)
        {
            chartAdapter.stopListening();
        }
    }
}
