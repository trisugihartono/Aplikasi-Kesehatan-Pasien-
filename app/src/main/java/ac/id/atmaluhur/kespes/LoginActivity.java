package ac.id.atmaluhur.kesehatan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextView daftar;
    Button btn_masuk;
    EditText xnama_pengguna, xkata_sandi;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        daftar = findViewById(R.id.daftar);
        btn_masuk = findViewById(R.id.btn_masuk);
        xnama_pengguna = findViewById(R.id.xnama_pengguna);
        xkata_sandi = findViewById(R.id.xkata_sandi);

        daftar = findViewById(R.id.daftar);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ubah state menjadi loading
                btn_masuk.setEnabled(false);
                btn_masuk.setText("Loading...");

                final String nama_pengguna = xnama_pengguna.getText().toString();
                final String kata_sandi = xkata_sandi.getText().toString();

                if (nama_pengguna.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nama Pengguna Kosong!", Toast.LENGTH_SHORT).show();
                    //ubah state menjadi nama_pengguna kosong
                    btn_masuk.setEnabled(true);
                    btn_masuk.setText("SIGN IN");
                }
                else {
                    if (kata_sandi.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Kata Sandi Kosong", Toast.LENGTH_SHORT).show();
                        //ubah state menjadi kata_sandi kosong
                        btn_masuk.setEnabled(true);
                        btn_masuk.setText("SIGN IN");
                    }
                    else {
                        reference = FirebaseDatabase.getInstance().getReference()
                                .child("Pengguna").child(nama_pengguna);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //ambil data kata_sandi dari firebase
                                    String kata_sandiFromFirebase = dataSnapshot.child("kata_sandi").getValue().toString();

                                    //validasi kata_sandi dengan kata_sandi firebase
                                    if (kata_sandi.equals(kata_sandiFromFirebase)) {

                                        //simpan nama_pengguna (key) pada local
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, xnama_pengguna.getText().toString());
                                        editor.apply();

                                        //berpindah activity
                                        Intent gotohome = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(gotohome);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Kata Sandi Salah!", Toast.LENGTH_SHORT).show();
                                        //ubah state menjadi kata_sandi salah
                                        btn_masuk.setEnabled(true);
                                        btn_masuk.setText("MASUK");
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Nama Pengguna Tidak Ada!", Toast.LENGTH_SHORT).show();
                                    //ubah state menjadi nama_pengguna tidak ada
                                    btn_masuk.setEnabled(true);
                                    btn_masuk.setText("MASUK");
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });
    }
}
