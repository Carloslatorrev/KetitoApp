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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.SingleSMSApi;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.SingleSMS;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.SingleSMSData;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.tags.TagApi;
import cl.ckelar.android.ketito.helpers.tags.TagsMensajes;

/**
 * Este Activity genera el menú para el envio de de la encuesta por SMS
 */
public class SingleSMSActivity extends AppCompatActivity {


    private Button btnRnviarSMS;
    private EditText editTextTelefono;
    private ImageButton btnBack;
    private ImageButton imgBtnLogo;
    private TextView txtEncuestaName;
    private TextView txtDescriptionName;
    private ConstraintLayout headerLayout;

    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Empresa empresa;
    private Encuesta encuesta;
    private SingleSMS singleSMS;
    private SingleSMSData singleSMSData;
    private SingleSMSApi singleSMSApi;
    private Boolean response = false;
    private String destino;
    private String mensaje;
    private String encuestaName;
    private String descriptionName;
    private String colorBanner;
    private Color color;
    private Drawable banner;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_enviosms);

        ketitoDatabase = ketitoDatabase.getInstance(SingleSMSActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
        txtEncuestaName = findViewById(R.id.txtEncuestaWelcome);
        txtDescriptionName = findViewById(R.id.txtDescriptionWelcome);
        encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);
        empresa = ketitoDatabase.empresaDao().getEmpresaBySelected();
        headerLayout = findViewById(R.id.HeaderLayout);

        btnRnviarSMS = findViewById(R.id.btnEnviarSMS);
        editTextTelefono = findViewById(R.id.editTextTelefonoEnvioSMS);
        btnBack = findViewById(R.id.imgButtonBackEnvioSMS);
        btnRnviarSMS.setEnabled(false);
        imgBtnLogo = findViewById(R.id.imgBtnLogoEnvioSMS);

        String url = empresa.getRutaImagen();
        Picasso.get().load(url).into(imgBtnLogo);

        //Título Banner
        encuestaName = txtEncuestaName.getText().toString();
        encuestaName = encuestaName.replace("%encuesta", ketitoDatabase.encuestaDao().getEncuestaBySelected(true).getTitulo());
        txtEncuestaName.setText(encuestaName);
        if(encuesta.getTitleFontColor() != null){
            int titleFontColor = Color.parseColor(encuesta.getTitleFontColor());
            txtEncuestaName.setTextColor(titleFontColor);
        }


        //Subtítulo Banner
        descriptionName= txtDescriptionName.getText().toString();
        descriptionName= descriptionName.replace("%description", ketitoDatabase.encuestaDao().getEncuestaBySelected(true).getDescripcion());
        if(descriptionName != null){
            txtDescriptionName.setText(descriptionName);
        }else{
            txtDescriptionName.setText("");
        }
        if(encuesta.getSubtitleFontColor() != null){
            int subtitleFontColor = Color.parseColor(encuesta.getSubtitleFontColor());
            txtDescriptionName.setTextColor(subtitleFontColor);
        }


        //Banner
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


        editTextTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().length() > 0 && s.toString().length() <=9){
                    btnRnviarSMS.setEnabled(true);
                }else{
                    btnRnviarSMS.setEnabled(false);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnRnviarSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envioSMS();
                if(response == true){
                    Intent mainIntent = new Intent().setClass(SingleSMSActivity.this, SingleSMSFinActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    Intent mainIntent = new Intent().setClass(SingleSMSActivity.this, GenericErrorActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent().setClass(SingleSMSActivity.this, MenuEncuestasActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

    }

    private Boolean envioSMS(){
        destino = editTextTelefono.getText().toString();
        if(destino != null && destino != ""){
            btnRnviarSMS.setEnabled(true);
            String msg = TagsMensajes.MENSAJE_SMS;
            msg = msg.replace("%razon", empresa.getRazon());
            msg = msg.replace("%idEncuesta", encuesta.getIdEncuesta());
            mensaje = msg;
            int random = (int) (Math.random() * 1000) + 1;
            String idSS= "id"+destino+random;
            singleSMS = new SingleSMS();
            singleSMS.setIdSingleSMS(idSS);
            singleSMS.setDestino(destino);
            singleSMS.setMensaje(mensaje);
            singleSMS.setProgramacion(false);
            singleSMS.setFH_programacion("");
            singleSMS.setIdPlantilla("");
            ketitoDatabase.singleSMSDao().insertSingleSMS(singleSMS);
        }


        singleSMSApi = new SingleSMSApi();
        singleSMSData = singleSMSApi.postEnvioSMS(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type()+" "+userSesion.getAccess_token(), singleSMS.getDestino() , singleSMS.getMensaje(), String.valueOf(singleSMS.getProgramacion()), singleSMS.getFH_programacion(), singleSMS.getIdPlantilla());
        if (singleSMSData.getResponse().equals("200")){
            response = true;
        }

        return response;

    }


}
