package ac.id.atmaluhur.kesehatan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    Button  btn_logout;
    ImageButton btn_jadwal, btn_store, btn_info;
    CircleView btn_to_profile;
    ImageView photo_home_user;
    TextView nama_lengkap, no_handphone, uang_sekarang;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUsernameLocal();
        btn_to_profile = findViewById(R.id.btn_to_profile);
        btn_jadwal = findViewById(R.id.btn_jadwal);
        btn_store = findViewById(R.id.btn_store);
        btn_info = findViewById(R.id.btn_info);
        btn_logout = findViewById(R.id.btn_logout);
        photo_home_user = findViewById(R.id.photo_home_user);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        no_handphone = findViewById(R.id.no_handphone);
        uang_sekarang = findViewById(R.id.uang_sekarang);

        reference = FirebaseDatabase.getInstance().getReference().child("Pengguna").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                no_handphone.setText(dataSnapshot.child("no_handphone").getValue().toString());
                uang_sekarang.setText("Rp. " + dataSnapshot.child("uang_sekarang").getValue().toString());
                Picasso.with(MainActivity.this).load(dataSnapshot.child("url_foto_profil").getValue().toString()).centerCrop().fit().into(photo_home_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotopisaticket = new Intent(MainActivity.this, DoctorActivity.class);
                startActivity(gotopisaticket);
            }
        });

        btn_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotopagodaticket = new Intent(MainActivity.this, HealthStoreActivity.class);
                startActivity(gotopagodaticket);
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotocanditicket = new Intent(MainActivity.this, InfoAplikasiActivity.class);
                startActivity(gotocanditicket);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor =  sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                Intent gotohome = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(gotohome);
                finish();
            }
        });
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
