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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.RespuestaApi;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.RespuestaData;
import cl.ckelar.android.ketito.dto.api.RespuestasListData;
import cl.ckelar.android.ketito.helpers.CustomLayout;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.tags.TagApi;

public class EncuestaFinActivity extends AppCompatActivity {

    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Empresa empresa;
    private Encuesta encuesta;
    private Respuesta respuesta;
    private RespuestaApi respuestaApi;
    private Boolean response = false;
    private RespuestaData respuestaData;
    private List<Respuesta> respuestaList;
    private List<Respuesta> deleteList;
    private ImageButton imageButton;
    private int numPosts = 0;
    private ConstraintLayout headerLayout;
    private Drawable banner;
    private String colorBanner;
    private CustomLayout customLayout;
    private TimerTask task;

    private static final long SCREEN_DELAY = 7000;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_encuesta_fin);
        ketitoDatabase = ketitoDatabase.getInstance(EncuestaFinActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
        empresa = ketitoDatabase.empresaDao().getEmpresaBySelected();
        encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);
        imageButton = findViewById(R.id.encuestaFINBtnLogo);
        headerLayout= findViewById(R.id.bannerFINConstraintLayout);

        String url = empresa.getRutaImagen();
        Picasso.get().load(url).into(imageButton);
        //Se obtiene el valor del color del background y se aplica en el Banner.
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

        task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent().setClass(EncuestaFinActivity.this, MenuEncuestasActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };

        if(numPosts == 0){
            postRespuestas();
                if (response == true){
                    //DeleteRespuesta();
                }else{
                    Timer timer = new Timer();
                    timer.schedule(task, SCREEN_DELAY);
                }
        }

    }

    //Se realiza el post de las respuestas.
    private void postRespuestas(){
        respuestaApi = new RespuestaApi();
        respuestaList = ketitoDatabase.respuestasDao().getAll();
        if(respuestaList != null && respuestaList.size() >0){
            List<RespuestasListData> listRespuestaData = new ArrayList<>();
            for (Respuesta resp: respuestaList) {
                RespuestasListData auxData = new RespuestasListData();
                auxData.setIdPregunta(resp.getIdPregunta());
                auxData.setIdAlternativa(resp.getIdAlternativa());
                auxData.setIdToma(resp.getIdToma());
                auxData.setNivel(resp.getNivel());
                auxData.setRespuesta(resp.getRespuesta());
                listRespuestaData.add(auxData);
            }
            Log.d("Array", "Array: "+ String.valueOf(listRespuestaData));
            numPosts= respuestaList.size();
            if(listRespuestaData != null) {
                //Se obtiene la respuesta desde la API
                respuestaData = respuestaApi.postRespuesta(TagApi.API_HEADER_CONTENT_TYPE_VALUE2, userSesion.getToken_type()+" "+userSesion.getAccess_token(), listRespuestaData);
                if(respuestaData != null){
                    if(respuestaData.getResponse().equals("200")){

                        Timer timer = new Timer();
                        timer.schedule(task, SCREEN_DELAY);
                        response = true;
                    }else{
                        response = false;
                    }
                }else{
                    response = false;
                }
            }
        }
    }


}
