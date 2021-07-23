package cl.ckelar.android.ketito.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import cl.ckelar.android.ketito.MainActivity;
import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Ubicacion;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;

public class WithoutChartActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton imgBtnLogo;
    private TextView txtUbiWelcome;
    private TextView txtBusinessWelcome;


    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_withouttoma);

        ketitoDatabase = ketitoDatabase.getInstance(WithoutChartActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();

        imgBtnLogo= findViewById(R.id.imgBtnLogoSetting);
        txtBusinessWelcome = findViewById(R.id.txtBusinessWelcome);
        txtUbiWelcome = findViewById(R.id.txtTokenWelcome);

        //Banner
        Ubicacion ubi = ketitoDatabase.ubicacionDao().getUbicacionBySelected(true);
        String ubiText = txtUbiWelcome.getText().toString();
        ubiText = ubiText.replace("%ubi", ubi.getUbicacion());
        txtUbiWelcome.setText(ubiText);

        String businessText = txtBusinessWelcome.getText().toString();
        Empresa empresa = ketitoDatabase.empresaDao().getEmpresaBySelected();

        if (empresa != null) {
            businessText = businessText.replace("%business", empresa.getRazon());
        } else {
            businessText = businessText.replace("%business", "No Seleccionada");
        }

        txtBusinessWelcome.setText(businessText);

        if(empresa.getRutaImagen() != null && empresa.getRutaImagen().length() > 0){
            String url = empresa.getRutaImagen();
            Picasso.get().load(url).into(imgBtnLogo);
        }

        btnBack = findViewById(R.id.imgButtonBackDash);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent().setClass(WithoutChartActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

    }


}
