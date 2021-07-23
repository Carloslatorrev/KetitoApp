package cl.ckelar.android.ketito.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.MainActivity;
import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.EncuestaApi;
import cl.ckelar.android.ketito.api.impl.TokenApi;
import cl.ckelar.android.ketito.api.impl.UbicacionApi;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.SettingUser;
import cl.ckelar.android.ketito.dto.TokenC;
import cl.ckelar.android.ketito.dto.Ubicacion;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.EncuestaData;
import cl.ckelar.android.ketito.dto.api.PreguntasAlternativasData;
import cl.ckelar.android.ketito.dto.api.PreguntasData;
import cl.ckelar.android.ketito.dto.api.TokenData;
import cl.ckelar.android.ketito.dto.api.UbicacionData;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.tags.TagApi;

public class SettingSelectActivity extends AppCompatActivity {


    private ImageButton imgBtnLogo;
    private Spinner spinnerToken;
    private Spinner spinnerEncuesta;
    private Spinner spinnerUbicacion;
    private CheckBox checkEnvio;
    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private EncuestaApi encuestaApi;
    private EditText password;
    private Button btnGuardarSetting;
    private TokenApi tokenApi;
    private TextView txtUbiWelcome;
    private TextView txtBusinessWelcome;


    List<TokenC> listToken;
    List<EncuestaData> listEncuestaData;
    List<TokenData> listTokenData;
    List<Encuesta> listEncuesta;
    ArrayList<String> listNomToken;
    ArrayList<String> listNomEncuesta;
    ArrayList<String> listNomUbicacion;
    Boolean isChecked;
    Boolean isCheckedSaved;
    String passwordUser;
    String passwordSaved;
    String selectedToken;
    String selectedEncuesta;
    String selectedUbicacion;
    SettingUser settingUser;
    SettingUser settingUserSaved;
    Preguntas preguntas;
    PreguntasAlternativas preguntasAlternativas;
    List<SettingUser> listSettingUser;
    List<Preguntas> listPreguntas;
    List<PreguntasData> preguntasDataList;
    List<PreguntasAlternativas> listPreguntasAlternativas;
    Encuesta encuesta;
    Encuesta encuestaElim;
    Encuesta encuestaAux;
    EncuestaData encuestaData;
    TokenData tokenData;
    TokenC tokenC;
    TokenC tokenElim;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapterToken;
    ArrayAdapter<CharSequence> adapterUbicacion;
    Ubicacion ubicacion;
    Ubicacion ubicacionElim;
    Ubicacion ubicacionAux;
    List<UbicacionData> ubicacionDataList;
    List<Ubicacion> ubicacionList;
    UbicacionApi ubicacionApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting_select);

        ketitoDatabase = ketitoDatabase.getInstance(SettingSelectActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
        settingUser = ketitoDatabase.settingUserDao().getSettingUserById(1);
        if (settingUser == null){
            settingUser = new SettingUser();
            settingUser.setIdSetting(1);
            settingUser.setEnvioSMS(false);
            settingUser.setPassword("");
            ketitoDatabase.settingUserDao().insertSettingUser(settingUser);
        }


        spinnerEncuesta = findViewById(R.id.spinnerEncuesta);
        spinnerToken = findViewById(R.id.spinnerToken);
        spinnerUbicacion = findViewById(R.id.spinnerUbicacion);
        checkEnvio = findViewById(R.id.checkBoxEnvioSMS);
        password = findViewById(R.id.editTextTextPassword);
        btnGuardarSetting = findViewById(R.id.btnGuardarConfig);
        imgBtnLogo= findViewById(R.id.imgBtnLogoSetting);
        txtBusinessWelcome = findViewById(R.id.txtBusinessWelcome);
        txtUbiWelcome = findViewById(R.id.txtTokenWelcome);


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

        new AsyncRellenar().execute();

        btnGuardarSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncGuardarSetting().execute();
            }
        });

    }


    private class AsyncRellenar extends AsyncTask<String, String, String>{


        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        boolean isUsed = false;

        @Override
        protected void onPreExecute() {
            //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder = new AlertDialog.Builder(SettingSelectActivity.this);
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

            listEncuesta = new ArrayList<>();
            listEncuesta = ketitoDatabase.encuestaDao().getAll();
            listToken = ketitoDatabase.tokenDao().getAll();
            ubicacionList = ketitoDatabase.ubicacionDao().getAll();
            String nombreEncuestaAux = ketitoDatabase.encuestaDao().getEncuestaBySelected(true).getTitulo();
            String nombreTokenC = ketitoDatabase.tokenDao().getTokenBySelected().getTokenName();
            String nombreUbicacion = ketitoDatabase.ubicacionDao().getUbicacionBySelected(true).getUbicacion();

            //Buscamos las encuestas actuales y las eliminamos
            if(listEncuesta != null && listEncuesta.size()>0){
                encuestaElim = new Encuesta();
                for (Encuesta enc: listEncuesta) {
                    encuestaElim = ketitoDatabase.encuestaDao().getEncuestaById(enc.getIdEncuesta());
                    ketitoDatabase.encuestaDao().deleteEncuesta(encuestaElim);
                }
            }

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


            //Buscamos los token actuales y los eliminamos
            if(listToken != null && listToken.size() >0){
                tokenElim = new TokenC();
                for (TokenC token: listToken) {
                    tokenElim = ketitoDatabase.tokenDao().getTokenByNombre(token.getTokenName());
                    ketitoDatabase.tokenDao().deleteTokenC(tokenElim);
                }
            }


            //Buscamos las ubicaciones actuales y las eliminamos
            if(ubicacionList != null && ubicacionList.size() >0){
                ubicacionElim = new Ubicacion();
                for (Ubicacion ubi: ubicacionList) {
                    ubicacionElim = ketitoDatabase.ubicacionDao().getUbicacionById(ubi.getIdUbicacion());
                    ketitoDatabase.ubicacionDao().deleteUbicacion(ubicacionElim);
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

            //Traemos los tokens
            listTokenData = new ArrayList<>();
            tokenApi = new TokenApi();
            listTokenData = tokenApi.GetUsersToken(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type()+" "+userSesion.getAccess_token());
            if(listTokenData != null && listTokenData.size()>0){
                tokenC = new TokenC();
                for (TokenData tkn: listTokenData) {
                    tokenC.setToken(tkn.getToken());
                    tokenC.setFH_Token(tkn.getFh_token());
                    tokenC.setTokenName(tkn.getTokenName());
                    tokenC.setSelected(false);
                    ketitoDatabase.tokenDao().InsertToken(tokenC);
                }
            }

            ubicacionDataList = new ArrayList<>();
            ubicacionApi = new UbicacionApi();
            ubicacionDataList = ubicacionApi.getUbicaciones(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type()+" "+userSesion.getAccess_token());
            if(ubicacionDataList != null && ubicacionDataList.size()>0){
                ubicacion = new Ubicacion();
                for (UbicacionData ubi : ubicacionDataList) {
                    ubicacion.setIdUbicacion(ubi.getIdUbicacion());
                    ubicacion.setDescripcion(ubi.getDescripcion());
                    ubicacion.setIdLocalidad(ubi.getIdLocalidad());
                    ubicacion.setLocalidad(ubi.getLocalidad());
                    ubicacion.setUbicacion(ubi.getUbicacion());
                    ubicacion.setSelected(false);
                    ketitoDatabase.ubicacionDao().insertUbicacion(ubicacion);
                }
            }


            //Asignamos a la encuesta que anteriormente estaba seleccionada en True
            encuesta = ketitoDatabase.encuestaDao().getEncuestaByTitulo(nombreEncuestaAux);
            encuesta.setSelected(true);
            ketitoDatabase.encuestaDao().updateEncuesta(encuesta);

            //ponemos el token que anteriormente estaba seleccionada en True
            tokenC = ketitoDatabase.tokenDao().getTokenByNombre(nombreTokenC);
            tokenC.setSelected(true);
            ketitoDatabase.tokenDao().updateTokenC(tokenC);
            tokenC = ketitoDatabase.tokenDao().getTokenBySelected();

            //Asignamos a la ubicacion que estaba seleccionada en True
            ubicacion = ketitoDatabase.ubicacionDao().getUbicacionByNombre(nombreUbicacion);
            ubicacion.setSelected(true);
            ketitoDatabase.ubicacionDao().updateUbicacion(ubicacion);
            ubicacion = ketitoDatabase.ubicacionDao().getUbicacionBySelected(true);

            //rellenamos las listas
            listToken = new ArrayList<>();
            listToken = ketitoDatabase.tokenDao().getAll();

            listEncuesta = new ArrayList<>();
            listEncuesta = ketitoDatabase.encuestaDao().getAll();

            ubicacionList = new ArrayList<>();
            ubicacionList = ketitoDatabase.ubicacionDao().getAll();

            listSettingUser = new ArrayList<>();
            listSettingUser = ketitoDatabase.settingUserDao().getAll();


            if(listSettingUser != null && listSettingUser.size() > 0){
                for (SettingUser stg: listSettingUser) {
                    isChecked = stg.getEnvioSMS();
                    passwordUser = stg.getPassword();
                }
            }

            //seteamos el spinner encuesta
            if(listEncuesta != null && listEncuesta.size() > 0){
                listNomEncuesta = new ArrayList<>();
                for (Encuesta enc: listEncuesta) {
                    listNomEncuesta.add(enc.getTitulo());
                }
                encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ArrayAdapter(SettingSelectActivity.this, R.layout.spinner_row,listNomEncuesta);
                        spinnerEncuesta.setAdapter(adapter);
                        spinnerEncuesta.setSelection(obtenerPosicionItem(spinnerEncuesta, encuesta.getTitulo()));

                    }
                });

            }

            //seteamos el spinner token
            if(listToken != null && listToken.size() > 0){
                listNomToken = new ArrayList<>();
                for (TokenC token: listToken) {
                    listNomToken.add(token.getTokenName());
                }

                tokenC = ketitoDatabase.tokenDao().getTokenBySelected();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterToken = new ArrayAdapter(SettingSelectActivity.this, R.layout.spinner_row,listNomToken);
                        spinnerToken.setAdapter(adapterToken);
                        spinnerToken.setSelection(obtenerPosicionItem(spinnerToken, tokenC.getTokenName()));
                    }
                });

            }

            //seteamos el spinner ubicacion
            if(ubicacionList != null && ubicacionList.size() > 0){
                listNomUbicacion = new ArrayList<>();
                for (Ubicacion ubi: ubicacionList) {
                    listNomUbicacion.add(ubi.getUbicacion());
                }

                ubicacion = ketitoDatabase.ubicacionDao().getUbicacionBySelected(true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterUbicacion = new ArrayAdapter(SettingSelectActivity.this, R.layout.spinner_row, listNomUbicacion);
                        spinnerUbicacion.setAdapter(adapterToken);
                        spinnerUbicacion.setSelection(obtenerPosicionItem(spinnerUbicacion, ubicacion.getUbicacion()));
                    }
                });

            }


            if(isChecked == true){
                checkEnvio.setChecked(true);
            }else{
                checkEnvio.setChecked(false);
            }

            if(passwordUser != null){
                password.setText(passwordUser);
            }else{
                password.setText("");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(listNomToken != null && listNomToken.size() > 0 && listNomEncuesta != null){
                spinnerToken.setAdapter(adapterToken);
                spinnerToken.setSelection(obtenerPosicionItem(spinnerToken, tokenC.getTokenName()));
                spinnerEncuesta.setAdapter(adapter);
                spinnerEncuesta.setSelection(obtenerPosicionItem(spinnerEncuesta, encuesta.getTitulo()));
                spinnerUbicacion.setAdapter(adapterUbicacion);
                spinnerUbicacion.setSelection(obtenerPosicionItem(spinnerUbicacion, ubicacion.getUbicacion()));
            }
            //if (empresas != null && empresas.size() > 0) {
            //lvBusinessSelect.setAdapter(empresasAdapter);
            //}

            alertDialog.cancel();
        }


    }

    private class AsyncGuardarSetting extends AsyncTask<String, String, String>{

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder = new AlertDialog.Builder(SettingSelectActivity.this);
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

            isCheckedSaved = checkEnvio.isChecked();
            passwordSaved = password.getText().toString();
            selectedEncuesta = spinnerEncuesta.getSelectedItem().toString();
            selectedToken = spinnerToken.getSelectedItem().toString();
            selectedUbicacion = spinnerUbicacion.getSelectedItem().toString();
            List<PreguntasData> listPreguntasData;
            List<PreguntasAlternativasData> listPreguntasAlternativasData;

            //Sacamos la selección de la actual encuesta, token y ubicación
            encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);
            encuesta.setSelected(false);
            ketitoDatabase.encuestaDao().updateEncuesta(encuesta);
            //token
            tokenC = ketitoDatabase.tokenDao().getTokenBySelected();
            tokenC.setSelected(false);
            ketitoDatabase.tokenDao().updateTokenC(tokenC);
            //ubicación
            ubicacion = ketitoDatabase.ubicacionDao().getUbicacionBySelected(true);
            ubicacion.setSelected(false);
            ketitoDatabase.ubicacionDao().updateUbicacion(ubicacion);


            //seteamos la encuesta seleccionada
            try{
                encuestaAux = ketitoDatabase.encuestaDao().getEncuestaByTitulo(selectedEncuesta);
                encuestaAux.setSelected(true);
                ketitoDatabase.encuestaDao().updateEncuesta(encuestaAux);
            }catch (Exception ex){
            }

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
                    if(auxPreg == null){ ketitoDatabase.preguntasDao().insertPreguntas(preguntas); }

                }
            }


            //seteamos el token seleccionado
            try{
                TokenC tokenCAux = ketitoDatabase.tokenDao().getTokenByNombre(selectedToken);
                tokenCAux.setSelected(true);
                ketitoDatabase.tokenDao().updateTokenC(tokenCAux);
            }catch (Exception ex){
            }

            //seteamos la ubicación seleccionada
            try{
                ubicacionAux = ketitoDatabase.ubicacionDao().getUbicacionByNombre(selectedUbicacion);
                ubicacionAux.setSelected(true);
                ketitoDatabase.ubicacionDao().updateUbicacion(ubicacionAux);
            }catch (Exception ex){
            }

            //seteamos la configuración del usuario
            settingUserSaved = ketitoDatabase.settingUserDao().getSettingUserById(1);
            if(settingUserSaved != null){
                settingUserSaved.setPassword(passwordSaved);
                settingUserSaved.setEnvioSMS(isCheckedSaved);
                ketitoDatabase.settingUserDao().updateSettingUser(settingUserSaved);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            SettingUser auxSettingUser = ketitoDatabase.settingUserDao().getSettingUserById(1);
            if (auxSettingUser != null) {
                Intent intent = new Intent(SettingSelectActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            alertDialog.cancel();
        }
    }


    public static int obtenerPosicionItem(Spinner spinner, String texto) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String nomEncuesta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(texto)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }


}
