package cl.ckelar.android.ketito.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import cl.ckelar.android.ketito.MainActivity;
import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.EncuestaApi;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.LinkEncuesta;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.SettingUser;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.EncuestaData;
import cl.ckelar.android.ketito.dto.api.LinkEncuestaData;
import cl.ckelar.android.ketito.dto.api.PreguntasAlternativasData;
import cl.ckelar.android.ketito.dto.api.PreguntasData;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.tags.TagApi;

/**
 * Este activity genera el menú de encuestas, También hace un llamado a la API para actualizar las preguntas de la
 * encuesta que está seleccionada en el menú de configuraciones
 */

public class MenuEncuestasActivity extends AppCompatActivity {

    private static final String TAG = "";
    private ImageButton imgBtnLogo;
    private Button btnEnviarEncuesta;
    private Button btnResponderEncuesta;
    private Button btnResponderEncuesta2;
    private ImageView imgQrCode;
    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Encuesta encuesta;
    private Empresa empresa;
    private SettingUser settingUser;
    private Bitmap bitmap;
    private TextView txtEncuestaName;
    private TextView txtDescriptionName;
    private ConstraintLayout headerLayout;

    private EncuestaApi encuestaApi;
    private LinkEncuestaData linkEncuestaData;

    private List<Encuesta> listEncuesta;
    private List<EncuestaData> listEncuestaData;
    private Encuesta encuestaElim;
    private String idEncuesta;
    private String qrLinkEncuesta;
    private String encuestaName;
    private String descriptionName;
    private Boolean ischeck;
    private Boolean access = true;
    private Drawable banner;
    private String colorBanner;
    private List<PreguntasData> listPreguntasData;
    private List<PreguntasAlternativasData> listPreguntasAlternativasData;
    private List<Preguntas> listPreguntas;
    private List<PreguntasAlternativas> listPreguntasAlternativas;
    private Preguntas preguntas;
    private PreguntasAlternativas preguntasAlternativas;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu_encuestas);

        imgBtnLogo = findViewById(R.id.imgBtnLogoEnc);
        imgQrCode = findViewById(R.id.imgQRcode);
        txtEncuestaName = findViewById(R.id.txtEncuestaWelcome);
        txtDescriptionName = findViewById(R.id.txtDescriptionWelcome);
        btnEnviarEncuesta = findViewById(R.id.btnEnviarEncuesta);
        btnResponderEncuesta= findViewById(R.id.btnPresentarEncuesta);
        btnResponderEncuesta2= findViewById(R.id.btnPresentarEncuesta2);
        headerLayout = findViewById(R.id.HeaderLayout);

        ketitoDatabase = ketitoDatabase.getInstance(MenuEncuestasActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
        BuscarEncuesta();
        empresa = ketitoDatabase.empresaDao().getEmpresaBySelected();

        List<SettingUser> settingUserList = ketitoDatabase.settingUserDao().getAll();
        if(settingUserList == null || settingUserList.size() == 0){
            Intent intent = new Intent(MenuEncuestasActivity.this, SettingSelectActivity.class);
            startActivity(intent);
            finish();
        }

        settingUser = ketitoDatabase.settingUserDao().getSettingUserById(1);
        if(settingUser != null){
            ischeck = settingUser.getEnvioSMS();
            if(ischeck == false){
                btnEnviarEncuesta.setVisibility(View.GONE);
                btnResponderEncuesta.setVisibility(View.GONE);
                btnResponderEncuesta2.setVisibility(View.VISIBLE);
            }
        }

        encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);

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

        //Logo
        String url = empresa.getRutaImagen();
        Picasso.get().load(url).into(imgBtnLogo);

        new AsyncGetQR().execute();

        imgBtnLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                intoMenuPrincipal();
                return false;
            }
        });


        btnResponderEncuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iMain = new Intent(MenuEncuestasActivity.this, EncuestaPresentationActivity.class);
                startActivity(iMain);
                finish();
            }
        });

        btnResponderEncuesta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iMain = new Intent(MenuEncuestasActivity.this, EncuestaPresentationActivity.class);
                startActivity(iMain);
                finish();
            }
        });

        btnEnviarEncuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iMain = new Intent(MenuEncuestasActivity.this, SingleSMSActivity.class);
                startActivity(iMain);
                finish();
            }
        });


    }

    private class AsyncGetQR extends AsyncTask<String, String, String>{


        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            dialogBuilder = new AlertDialog.Builder(MenuEncuestasActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_loading_layout, null);
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);
        }

        @Override
        protected String doInBackground(String... strings) {
            linkEncuestaData = new LinkEncuestaData();
            encuestaApi = new EncuestaApi();
            Encuesta encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);
            if(encuesta != null){ idEncuesta = encuesta.getIdEncuesta(); }
            if(linkEncuestaData != null) {
                qrLinkEncuesta = TagApi.LINK_ENCUESTA_RESPONSE +idEncuesta;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int smallerDimension = width < height ? width : height;
                        smallerDimension = smallerDimension * 3 / 5;
                        QRGEncoder qrgEncoder = new QRGEncoder(qrLinkEncuesta, null, QRGContents.Type.TEXT, smallerDimension);
                        qrgEncoder.setColorBlack(Color.BLACK);
                        qrgEncoder.setColorWhite(Color.WHITE);
                        try {
                            // Getting QR-Code as Bitmap
                            bitmap = qrgEncoder.getBitmap();
                            // Setting Bitmap to ImageView
                            imgQrCode.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            Log.v(TAG, e.toString());
                        }
                    }


                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            LinkEncuesta linkEncuesta = ketitoDatabase.linkEncuestaDao().getLinkEncuestaByEncuesta(idEncuesta);
            if (linkEncuesta != null) {
                Intent intent = new Intent(MenuEncuestasActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            alertDialog.cancel();
        }



    }

    private void intoMenuPrincipal() {
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.error_icon)
                .setTitle("Ingrese su contraseña de protección")
                .setMessage("Debe ingresar su contraseña configurada:")
                .setCancelable(false)
                .setView(editText)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //android.os.Process.killProcess(android.os.Process.myPid()); //Su funcion es algo similar a lo que se llama cuando se presiona el botón "Forzar Detención" o "Administrar aplicaciones", lo cuál mata la aplicación
                        //finish(); Si solo quiere mandar la aplicación a segundo plano
                        dialog.dismiss();
                        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
                            SettingUser settingUser = ketitoDatabase.settingUserDao().getSettingUserById(1);
                            String password = settingUser.getPassword();
                            String ingreso = editText.getText().toString();
                            if(ingreso == null){
                                ingreso = "";
                            }
                            if(password != null && password.length() >0){
                                    if(password.equals(ingreso)){
                                        Intent intentLogin = new Intent(MenuEncuestasActivity.this, MainActivity.class);
                                        startActivity(intentLogin);
                                        finish();
                                    }else{
                                        access = false;
                                        if(access == false){
                                            errorDialog();
                                        }
                                        dialog.dismiss();
                                    }
                            }else{
                                Intent intentLogin = new Intent(MenuEncuestasActivity.this, MainActivity.class);
                                startActivity(intentLogin);
                                finish();
                            }
                    }
                }).show();


    }

    private void errorDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.error_icon)
                .setTitle("Error:")
                .setMessage("Contraseña Inválida")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    private void BuscarEncuesta(){

            listEncuesta = new ArrayList<>();
            listEncuesta = ketitoDatabase.encuestaDao().getAll();
            String nombreEncuestaAux = ketitoDatabase.encuestaDao().getEncuestaBySelected(true).getTitulo();
            //Buscamos las encuestas actuales y las eliminamos
            if(listEncuesta != null && listEncuesta.size()>0){
                encuestaElim = new Encuesta();
                for (Encuesta enc: listEncuesta) {
                    encuestaElim = ketitoDatabase.encuestaDao().getEncuestaById(enc.getIdEncuesta());
                    ketitoDatabase.encuestaDao().deleteEncuesta(encuestaElim);
                }
            }

            //Traemos las nuevas encuestas
            listEncuestaData = new ArrayList<>();
            encuestaApi = new EncuestaApi();
            listEncuestaData = encuestaApi.getEncuestas(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type()+" "+userSesion.getAccess_token());

            if(listEncuestaData != null && listEncuestaData.size()>0){
                encuesta = new Encuesta();
                for (EncuestaData enc: listEncuestaData) {
                    encuesta.setIdEncuesta(enc.getIdEncuesta());
                    encuesta.setSelected(false);
                    encuesta.setDescripcion(enc.getDescripcion());
                    encuesta.setFH_creation(enc.getFh_Creation());
                    encuesta.setUsuarioCreacion(enc.getUsuarioCreacion());
                    encuesta.setTitulo(enc.getTitulo());
                    encuesta.setBannerBackgroundColor(enc.getBannerBackgroundColor());
                    encuesta.setTitleFontColor(enc.getTitleFontColor());
                    encuesta.setSubtitleFontColor(enc.getSubtitleFontColor());
                    encuesta.setBackgroundImageUrl(enc.getBackgroundImageUrl());
                    ketitoDatabase.encuestaDao().insertEncuesta(encuesta);
                }
            }

            //Asignamos a la encuesta que anteriormente estaba seleccionada en True
            encuesta = ketitoDatabase.encuestaDao().getEncuestaByTitulo(nombreEncuestaAux);
            encuesta.setSelected(true);
            ketitoDatabase.encuestaDao().updateEncuesta(encuesta);


            //Buscamos las preguntas asociada a la encuesta y la eliminamos.
            listPreguntas = new ArrayList<>();
            listPreguntas = ketitoDatabase.preguntasDao().getAll();
            if(listPreguntas != null && listPreguntas.size() >0){
                for (Preguntas preg: listPreguntas){
                    ketitoDatabase.preguntasDao().deletePreguntas(preg);
                }
            }

            //Buscamos las alternativas asociadas y las eliminamos
            listPreguntasAlternativas = new ArrayList<>();
            listPreguntasAlternativas = ketitoDatabase.preguntasAlternativasDao().getAll();
            if(listPreguntasAlternativas != null && listPreguntasAlternativas.size() >0){
                for (PreguntasAlternativas pregAlt: listPreguntasAlternativas) {
                    ketitoDatabase.preguntasAlternativasDao().deletePreguntasAlternativas(pregAlt);
                }
            }

            encuestaApi = new EncuestaApi();
            String idEncuestaPreg = ketitoDatabase.encuestaDao().getEncuestaBySelected(true).getIdEncuesta();
            listPreguntasData = encuestaApi.getFindEncuesta(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type()+" "+userSesion.getAccess_token(), idEncuestaPreg);

            if(listPreguntasData != null && listPreguntasData.size() > 0){
                for (PreguntasData prg : listPreguntasData){
                    preguntas = new Preguntas();
                    preguntas.setIdPregunta(prg.getIdPregunta());
                    preguntas.setEscala(prg.getEscala());
                    preguntas.setEtiquetaMaximo(prg.getEtiquetaMaximo());
                    preguntas.setEtiquetaMinimo(prg.getEtiquetaMinimo());
                    preguntas.setIdEncuesta(idEncuestaPreg);
                    preguntas.setIdSimbolo(prg.getIdSimbolo());
                    preguntas.setIdTipo(prg.getIdTipo());
                    preguntas.setIndice(prg.getIndice());
                    preguntas.setNomTipo(prg.getTipo());
                    preguntas.setObligatoria(prg.getObligatoria());
                    preguntas.setPregunta(prg.getPregunta());
                    preguntas.setRespuestaLarga(prg.getRespuestaLarga());
                    preguntas.setSubtitulo(prg.getSubtitulo());
                    listPreguntasAlternativasData = prg.getListPreguntasAlternativasData();
                    if(listPreguntasAlternativasData != null && listPreguntasAlternativasData.size() >0){
                        for (PreguntasAlternativasData prgAl : listPreguntasAlternativasData){
                            preguntasAlternativas = new PreguntasAlternativas();
                            preguntasAlternativas.setIdAlternativa(prgAl.getIdAlternativa());
                            preguntasAlternativas.setIdPregunta(prg.getIdPregunta());
                            preguntasAlternativas.setAlternativa(prgAl.getAlternativa());
                            preguntasAlternativas.setIndice(prgAl.getIndice());

                            PreguntasAlternativas auxPregAlter = ketitoDatabase.preguntasAlternativasDao().getPreguntaAlternativaById(prgAl.getIdAlternativa());

                            if(auxPregAlter == null){
                                ketitoDatabase.preguntasAlternativasDao().insertPreguntasAlternativas(preguntasAlternativas);
                            }
                        }
                    }

                    Preguntas auxPreg = ketitoDatabase.preguntasDao().getPreguntasById(prg.getIdPregunta());
                    if(auxPreg == null){
                        ketitoDatabase.preguntasDao().insertPreguntas(preguntas);
                    }

                }
            }
    }

}
