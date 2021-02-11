package ac.id.atmaluhur.kesehatan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    EditText nama_pengguna, kata_sandi, alamat_email;
    DatabaseReference reference;
    Button btn_lanjut;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nama_pengguna = findViewById(R.id.nama_pengguna);
        kata_sandi = findViewById(R.id.kata_sandi);
        alamat_email = findViewById(R.id.alamat_email);
        btn_lanjut = findViewById(R.id.btn_lanjut);

        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //menyimpan data kepada local storage (handphone)
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, nama_pengguna.getText().toString());
                editor.apply();

                //simpan kepada database
                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Pengguna").child(nama_pengguna.getText().toString());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("nama_pengguna").setValue(nama_pengguna.getText().toString());
                        dataSnapshot.getRef().child("kata_sandi").setValue(kata_sandi.getText().toString());
                        dataSnapshot.getRef().child("alamat_email").setValue(alamat_email.getText().toString());
                        dataSnapshot.getRef().child("uang_sekarang").setValue(500000);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //berpindah activity
                Intent gotonextregister = new Intent(RegisterActivity.this, RegisterTwoActivity.class);
                startActivity(gotonextregister);

            }
        });
    }
}