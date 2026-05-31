package mg.carlos.torch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Color;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private ImageView imgTorch;
    private boolean isTorchOn = false;
    private ConstraintLayout rootLayout;
    private Switch switchTorch;
    private CameraManager cameraManager;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setPopupTheme(androidx.appcompat.R.style.ThemeOverlay_AppCompat_Light);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, android.R.drawable.ic_menu_more));


        switchTorch = findViewById(R.id.switchTorch);
        rootLayout = findViewById(R.id.rootLayout);
        imgTorch = findViewById(R.id.imgTorch);

        cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        }catch (Exception e){
            Log.e("Torch App","Erreur caméra : "+ e.getMessage());
        }

        switchTorch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    torchOn();
                }else{
                    torchOff();
                }
            }
        });
    }

    private void torchOn(){
        try {
            cameraManager.setTorchMode(cameraId, true);
            isTorchOn = true;
            rootLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.bg_on));
            imgTorch.setImageResource(R.drawable.ic_torch_on);
        }catch (Exception e){
            Log.e("Torch App","Erreur caméra : "+ e.getMessage());
        }

    }

    private void torchOff(){
        try {
            cameraManager.setTorchMode(cameraId,false);
            isTorchOn = false;
            rootLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.bg_off));
            imgTorch.setImageResource(R.drawable.ic_torch_off);
        }catch (Exception e){
            Log.e("Torch App","Erreur caméra : "+ e.getMessage());
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        if (isTorchOn) {
            torchOff();
            switchTorch.setChecked(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_quit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}