package cl.ckelar.android.ketito.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.EncuestaApi;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.EncuestaData;
import cl.ckelar.android.ketito.dto.api.PreguntasAlternativasData;
import cl.ckelar.android.ketito.dto.api.PreguntasData;
import cl.ckelar.android.ketito.helpers.Adapter.AdapterEncuesta;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.tags.TagApi;

public class EncuestaSelectActivity extends AppCompatActivity {

    private ListView lvEncuestaSelect;
    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Encuesta encuesta;
    private Preguntas preguntas;
    private PreguntasAlternativas preguntasAlternativas;
    private EncuestaApi encuestaApi;
    private List<EncuestaData> listEncuestaData;
    private List<String> listNomEncuesta = new ArrayList<>();
    private ArrayAdapter<String> encuestaAdapter;
    private String idEncuesta;
    private String auxIdEncuesta;
    private ArrayList<Encuesta> arrayListEncuesta = new ArrayList<>();
    private AdapterEncuesta listEncuestaView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_encuesta_select);

        ketitoDatabase = ketitoDatabase.getInstance(EncuestaSelectActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();

        if (userSesion == null) {
            Intent iAccount = new Intent(EncuestaSelectActivity.this, LoginActivity.class);
            startActivity(iAccount);
            finish();
        }
        else {
            new EncuestaSelectActivity.AsyncGetEncuestas().execute();
        }

        lvEncuestaSelect = findViewById(R.id.lvEncuestaSelect);
        lvEncuestaSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                lvEncuestaSelect.setSelection(position);
                Log.d("EncuestaSelectActivity", "Encuesta Selected: " + lvEncuestaSelect.getItemAtPosition(position).toString());

                Encuesta auxEncAdapter = listEncuestaView.getItem(position);
                Encuesta mEncuesta = ketitoDatabase.encuestaDao().getEncuestaByTitulo(auxEncAdapter.getTitulo());
                idEncuesta = mEncuesta.getIdEncuesta();

                List<Encuesta> getAll = ketitoDatabase.encuestaDao().getAll();
                if(getAll != null && getAll.size() > 0){
                    for(Encuesta en : getAll) {
                        en.setSelected(false);
                        ketitoDatabase.encuestaDao().updateEncuesta(en);
                    }
                }
                mEncuesta.setSelected(true);
                ketitoDatabase.encuestaDao().updateEncuesta(mEncuesta);
                new AsyncSelectEncuesta().execute();
            }
        });
    }

    private class AsyncGetEncuestas extends AsyncTask<String, String, String>{

        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            dialogBuilder = new AlertDialog.Builder(EncuestaSelectActivity.this);
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

            //
            listEncuestaData = new ArrayList<>();
            encuestaApi= new EncuestaApi();

            listEncuestaData = encuestaApi.getEncuestas(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type()+" "+userSesion.getAccess_token());

            if(listEncuestaData != null && listEncuestaData.size() > 0){
                for(EncuestaData enc : listEncuestaData){
                    encuesta = new Encuesta();
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
                    Encuesta encuestaAux = ketitoDatabase.encuestaDao().getEncuestaById(enc.getIdEncuesta());
                    if(encuestaAux == null){
                        ketitoDatabase.encuestaDao().insertEncuesta(encuesta);
                        auxIdEncuesta = encuesta.getIdEncuesta();
                    }
                    //Actualizamos la lista que almacena el objeto encuesta.
                    arrayListEncuesta.add(encuesta);
                    listNomEncuesta.add(encuesta.getTitulo());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listEncuestaView = new AdapterEncuesta(EncuestaSelectActivity.this, arrayListEncuesta);
                        listEncuestaView.getCount();
                        lvEncuestaSelect.setAdapter(listEncuestaView);
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(arrayListEncuesta != null && arrayListEncuesta.size() > 0){
                lvEncuestaSelect.setAdapter(listEncuestaView);
            }

            //if (empresas != null && empresas.size() > 0) {
            //lvBusinessSelect.setAdapter(empresasAdapter);
            //}

            alertDialog.cancel();
        }

    }

    private class AsyncSelectEncuesta extends AsyncTask<String, String, String>{


        AlertDialog.Builder dialogBuilder;
        AlertDialog alertDialog;
        ProgressBar progressBar;

        boolean isUsed = false;

        @Override
        protected void onPreExecute() {
            //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder = new AlertDialog.Builder(EncuestaSelectActivity.this);
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

            List<PreguntasData> listPreguntasData;
            List<PreguntasAlternativasData> listPreguntasAlternativasData;
            encuestaApi = new EncuestaApi();
            listPreguntasData = encuestaApi.getFindEncuesta(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type()+" "+userSesion.getAccess_token(), idEncuesta);

            if(listPreguntasData != null && listPreguntasData.size() > 0){
                for (PreguntasData prg : listPreguntasData){
                    preguntas = new Preguntas();
                    preguntas.setIdPregunta(prg.getIdPregunta());
                    preguntas.setEscala(prg.getEscala());
                    preguntas.setEtiquetaMaximo(prg.getEtiquetaMaximo());
                    preguntas.setEtiquetaMinimo(prg.getEtiquetaMinimo());
                    preguntas.setIdEncuesta(idEncuesta);
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

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Preguntas auxPreguntas = ketitoDatabase.preguntasDao().getPreguntasByEncuesta(idEncuesta);
            if (auxPreguntas != null) {
                Intent intent = new Intent(EncuestaSelectActivity.this, UbicacionSelectActivity.class);
                startActivity(intent);
                finish();
            }
            alertDialog.cancel();
        }

    }


}
