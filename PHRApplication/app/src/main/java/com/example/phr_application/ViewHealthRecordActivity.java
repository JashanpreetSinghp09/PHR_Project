package com.example.phr_application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewHealthRecordActivity extends AppCompatActivity {

    private TextView viewRecord;
    private Button viewButton;
    private WalletManager walletManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_health_record);

        viewRecord = findViewById(R.id.viewingHealth);
        viewButton = findViewById(R.id.viewingButton);
        walletManager = WalletManager.getInstance(this);

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                walletManager.retrieveFromSmartContract(new WalletManager.RetrieveFromSmartContractCallback() {
                    @Override
                    public void onRetrieveSuccess(String result) {

                        viewRecord.setText(result);
                    }

                    @Override
                    public void onRetrieveFailed(String errorMessage) {

                        Toast.makeText(ViewHealthRecordActivity.this, "Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}