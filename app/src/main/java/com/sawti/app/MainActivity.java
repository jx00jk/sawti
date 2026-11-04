package com.sawti.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQ_PERMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStart).setOnClickListener(v -> startListening());
        findViewById(R.id.btnStop).setOnClickListener(v ->
                stopService(new Intent(this, SpeechService.class)));
    }

    private void startListening() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "فعّل الظهور فوق التطبيقات ثم ارجع واضغط ابدأ", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())));
            return;
        }

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.POST_NOTIFICATIONS
            }, REQ_PERMS);
            return;
        }

        startForegroundService(new Intent(this, SpeechService.class));
        Toast.makeText(this, "بدأ الاستماع. اخرج إلى أي تطبيق", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMS && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        } else {
            Toast.makeText(this, "يحتاج التطبيق إذن الميكروفون", Toast.LENGTH_LONG).show();
        }
    }
  }
