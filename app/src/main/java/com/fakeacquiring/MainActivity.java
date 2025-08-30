package com.fakeacquiring;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends Activity implements NfcAdapter.ReaderCallback {
    private NfcAdapter nfcAdapter;
    private TextView statusText;
    private Handler handler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        statusText = findViewById(R.id.status_text);
        handler = new Handler();
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            statusText.setText("NFC не поддерживается");
            statusText.setTextColor(Color.RED);
            return;
        }
        
        nfcAdapter.enableReaderMode(this, this, 
            NfcAdapter.FLAG_READER_NFC_A | 
            NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, 
            null);
            
        statusText.setText("Готов к сканированию NFC");
        statusText.setTextColor(Color.GREEN);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        runOnUiThread(() -> {
            statusText.setText("Обработка оплаты...");
            statusText.setTextColor(Color.YELLOW);
            
            handler.postDelayed(() -> {
                statusText.setText("Оплата успешна!");
                statusText.setTextColor(Color.GREEN);
                
                handler.postDelayed(() -> {
                    statusText.setText("Готов к сканированию NFC");
                    statusText.setTextColor(Color.GREEN);
                }, 3000);
            }, 2000);
        });
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, this, 
                NfcAdapter.FLAG_READER_NFC_A | 
                NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, 
                null);
        }
    }
}