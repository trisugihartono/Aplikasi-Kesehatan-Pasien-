package ac.id.atmaluhur.kesehatan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
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

public class HealthStoreActivity extends AppCompatActivity {
    ImageButton btn_vitamin, btn_vaksin, btn_obat_tetes, btn_alat_medis;
    TextView uang_sekarang;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_store);

        getUsernameLocal();

        btn_vitamin = findViewById(R.id.barang1);
        btn_vaksin = findViewById(R.id.barang2);
        btn_obat_tetes = findViewById(R.id.barang3);
        btn_alat_medis = findViewById(R.id.barang4);

        uang_sekarang = findViewById(R.id.uang_sekarang);

        reference = FirebaseDatabase.getInstance().getReference().child("Pengguna").child(username_key_new);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uang_sekarang.setText("Rp. " + dataSnapshot.child("uang_sekarang").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

        public void getUsernameLocal(){
            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
            username_key_new = sharedPreferences.getString(username_key, "");
        }
}