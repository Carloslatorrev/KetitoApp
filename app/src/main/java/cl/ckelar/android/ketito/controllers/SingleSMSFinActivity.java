package cl.ckelar.android.ketito.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.helpers.CustomLayout;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;


/**
 * Este activity solo muestra la pantalla post envio de SMS
 */
public class SingleSMSFinActivity extends AppCompatActivity {

    private static final long SCREEN_DELAY = 5000;

    private ImageButton btnBack;
    private ImageButton imgBtnLogo;
    private ConstraintLayout headerLayout;
    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Encuesta encuesta;
    private Empresa empresa;
    private Drawable banner;
    private String colorBanner;
    private CustomLayout customLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_singlesms_fin);

        ketitoDatabase = ketitoDatabase.getInstance(SingleSMSFinActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
        encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);
        empresa = ketitoDatabase.empresaDao().getEmpresaBySelected();
        imgBtnLogo = findViewById(R.id.enviosmsFINBtnLogo);
        headerLayout = findViewById(R.id.bannerConstraintLayout);

        String url = empresa.getRutaImagen();
        Picasso.get().load(url).into(imgBtnLogo);

        if(encuesta.getBackgroundImageUrl() != null){
            String urlBackground = encuesta.getBackgroundImageUrl();
            final ImageView img = new ImageView(this);
            Picasso.get().load(urlBackground).into(img, new Callback() {
                @Override
                public void onSuccess() {
                    headerLayout.setBackground(img.getDrawable());
                }

                @Override
                public void onError(Exception e) {
                    Log.d("ERROR:", "error: "+e.toString());
                }
            });
        }else{
            colorBanner = encuesta.getBannerBackgroundColor();
            banner = headerLayout.getBackground();
            if(colorBanner != null){
                int myColor = Color.parseColor(colorBanner);
                headerLayout.getBackground().setColorFilter(myColor, PorterDuff.Mode.SRC_ATOP);
                if (banner instanceof ShapeDrawable) {
                    ShapeDrawable shapeDrawable = (ShapeDrawable) banner;
                    shapeDrawable.getPaint().setColor(myColor);
                } else if (banner instanceof GradientDrawable) {
                    GradientDrawable gradientDrawable = (GradientDrawable) banner;
                    gradientDrawable.setColor(myColor);
                } else if (banner instanceof ColorDrawable) {
                    ColorDrawable colorDrawable = (ColorDrawable) banner;
                    colorDrawable.setColor(myColor);
                } else if(banner instanceof BitmapDrawable){
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) banner;
                    bitmapDrawable.setTint(myColor);
                }
            }
        }


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // Inicio de la nueva actividad
                Intent mainIntent = new Intent().setClass(
                        SingleSMSFinActivity.this, MenuEncuestasActivity.class);
                startActivity(mainIntent);
                // Cierra la actividad
                finish();
            }
        };

        //iniciamos el timer.
        Timer timer = new Timer();
        timer.schedule(task, SCREEN_DELAY);
    }


}

