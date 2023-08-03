package com.example.my;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlmoxarifadoActivity extends AppCompatActivity {

    private final ArrayList<ClipData.Item> estoque = new ArrayList<>();

    private EditText nomeItemEditText;
    private TextView estoqueTextView;

    private DatabaseReference myRef;

    private static final String TAG = "AlmoxarifadoActivity";

    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almoxarifado);

        nomeItemEditText = findViewById(R.id.itemNameEditText);
        findViewById(R.id.itemQuantityEditText);
        estoqueTextView = findViewById(R.id.stockTextView);

        // Inicializar a referência do banco de dados
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        // Inicializar o Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Adicionar o listener para ler do banco de dados
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Este método é chamado sempre que os dados no local são atualizados
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Valor é: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Falha ao ler os dados
                Log.w(TAG, "Falha ao ler os dados.", error.toException());
            }
        });
    }

    public void adicionarItens(View view) {
        String nomeItem = nomeItemEditText.getText().toString();
        ClipData.Item item = new ClipData.Item(nomeItem);
        estoque.add(item);
        atualizarTextoEstoque();

        // Escrever a mensagem no banco de dados
        myRef.setValue("Hello, World!");
    }

    public void listarItens(View view) {
        atualizarTextoEstoque();
    }

    private void atualizarTextoEstoque() {
        StringBuilder stringBuilder = new StringBuilder("Itens em Estoque:\n");
        for (ClipData.Item item : estoque) {
            stringBuilder.append(item.getUri()).append(": ").append(item.getUri()).append("\n");
        }
        estoqueTextView.setText(stringBuilder.toString());
    }

    // Método para realizar o login
    public void realizarLogin() {
        realizarLogin(null);
    }

    // Método para realizar o login
    public void realizarLogin(View v) {
        EditText loginEditText = findViewById(R.id.loginEditText);
        EditText senhaEditText = findViewById(R.id.senhaEditText);

        mAuth.signInWithEmailAndPassword(loginEditText.getText().toString(), senhaEditText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Mensagem de sucesso ao realizar o login
                            Toast.makeText(getApplicationContext(), "Login Realizado com sucesso!!!",
                                    Toast.LENGTH_SHORT).show();

                            // Redireciona para a tela ListaUsuarios
                            Intent intent = new Intent(getApplicationContext(), ListaUsuarios.class);
                            startActivity(intent);
                        } else {
                            // Mensagem de erro ao realizar o login
                            Toast.makeText(getApplicationContext(), "Erro ao logar, usuário e/ou senha incorretos!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
