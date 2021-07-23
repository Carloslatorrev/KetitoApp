package cl.ckelar.android.ketito.controllers;

import android.content.Context;
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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.api.impl.TomaApi;
import cl.ckelar.android.ketito.dto.Empresa;
import cl.ckelar.android.ketito.dto.Encuesta;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.dto.Toma;
import cl.ckelar.android.ketito.dto.UserSesion;
import cl.ckelar.android.ketito.dto.api.TomaData;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;
import cl.ckelar.android.ketito.helpers.generator.NotaCalificacionGenerator;
import cl.ckelar.android.ketito.helpers.generator.NotaScoreGenerator;
import cl.ckelar.android.ketito.helpers.generator.RespuestaTextoGenerator;
import cl.ckelar.android.ketito.helpers.generator.SatisfaccionEmojiGenerator;
import cl.ckelar.android.ketito.helpers.generator.SatisfaccionEstrellaGenerator;
import cl.ckelar.android.ketito.helpers.generator.SatisfaccionNumeroGenerator;
import cl.ckelar.android.ketito.helpers.generator.SeleccionMultipleGenerator;
import cl.ckelar.android.ketito.helpers.generator.SeleccionUnicaGenerator;
import cl.ckelar.android.ketito.helpers.tags.TagApi;

/**
 * Este Activity es el que genera las vistas programáticas de la encuesta
 * Llama a los generadores (Clases generator) y aplica la vista dentro del activity.
 */
public class EncuestaPresentationActivity extends AppCompatActivity {

    private ImageButton btnAnterior;
    private ImageButton btnSiguiente;
    private ImageButton imgBtnlogo;
    private ConstraintLayout headerLayout;

    private ConstraintLayout rootLayout;
    private Context context;
    private KetitoDatabase ketitoDatabase;
    private UserSesion userSesion;
    private Empresa empresa;
    private Encuesta encuesta;
    private List<Preguntas> preguntasList;
    private List<PreguntasAlternativas> preguntasAlternativasList;
    private int tipoPregunta;
    private int indice = 1;
    private int totalPreguntas= 0;
    private SeleccionMultipleGenerator seleccionMultipleGenerator;
    private SeleccionUnicaGenerator seleccionUnicaGenerator;
    private SatisfaccionNumeroGenerator satisfaccionNumeroGenerator;
    private SatisfaccionEstrellaGenerator satisfaccionEstrellaGenerator;
    private SatisfaccionEmojiGenerator satisfaccionEmojiGenerator;
    private RespuestaTextoGenerator respuestaTextoGenerator;
    private NotaCalificacionGenerator notaCalificacionGenerator;
    private NotaScoreGenerator notaScoreGenerator;
    private Toma toma;
    private TomaApi tomaApi;
    private TomaData tomaData;
    private int idToma;
    private Respuesta respuesta;
    private List<Respuesta> respuestaList;
    private List<View> removeListView;
    private String idRespuestaElim;
    private Drawable banner;
    private String colorBanner;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_encuesta_presentation);

        btnAnterior = findViewById(R.id.btnEncuestaMainAnterior);
        btnSiguiente = findViewById(R.id.btnEncuestaMainSiguiente);
        imgBtnlogo = findViewById(R.id.encuestaMainBtnLogo);
        ketitoDatabase = ketitoDatabase.getInstance(EncuestaPresentationActivity.this);
        userSesion = ketitoDatabase.userSesionDao().getUserSesionFirst();
        encuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true);
        empresa = ketitoDatabase.empresaDao().getEmpresaBySelected();
        imgBtnlogo = findViewById(R.id.encuestaMainBtnLogo);
        headerLayout = findViewById(R.id.bannerEncuestaPresentationConstraintLayout);

        rootLayout = (ConstraintLayout) findViewById(R.id.content_encuesta_presentation);
        context = this;

        View view = new View(this);



        String url = empresa.getRutaImagen();
        Picasso.get().load(url).into(imgBtnlogo);
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


        String idEncuesta = ketitoDatabase.encuestaDao().getEncuestaBySelected(true).getIdEncuesta();
        String idUbicacion = ketitoDatabase.ubicacionDao().getUbicacionBySelected(true).getIdUbicacion();
        tomaApi = new TomaApi();
        tomaData = tomaApi.getToma(TagApi.API_HEADER_CONTENT_TYPE_VALUE, userSesion.getToken_type()+" "+userSesion.getAccess_token(), idUbicacion, idEncuesta);
        if(tomaData != null){
            toma = new Toma();
            idToma = tomaData.getIdToma();
            toma.setIdToma(tomaData.getIdToma());
            toma.setIdEncuesta(tomaData.getIdEncuesta());
            toma.setIdTipoToma(tomaData.getIdTipoToma());
            toma.setFH_Toma(tomaData.getFh_toma());
            toma.setFH_Termino(tomaData.getFh_termino());
            toma.setEstado(tomaData.getEstado());
            toma.setIdArea(tomaData.getUbicacion().getIdUbicacion());
            ketitoDatabase.tomaDao().insertToma(toma);
        }

        if(indice == 1){
            btnAnterior.setVisibility(View.GONE);
        }

        final int[] tiempo = {150000}; // Indícalo.

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiempo[0] = tiempo[0] +30000;
                if(indice < totalPreguntas){
                    GetPreguntas();
                    removeRView();
                    btnAnterior.setVisibility(View.VISIBLE);
                    SetPreguntas();
                }else if(indice >= totalPreguntas){
                    GetPreguntas();
                    Intent iMain = new Intent(EncuestaPresentationActivity.this, EncuestaFinActivity.class);
                    startActivity(iMain);
                    finish();
                }

            }
        });

        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiempo[0] = tiempo[0] +30000;
                indice = indice -1;
                if(indice == 1){
                    btnAnterior.setVisibility(View.GONE);
                }
                if(tipoPregunta == 1){
                    deleteRespuestaList(respuestaList);
                }else{
                    removeRespuesta(idRespuestaElim);
                }
                removeRView();
                SetPreguntas();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSiguiente.setVisibility(View.VISIBLE);
            }
        });

        imgBtnlogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent iMain = new Intent(EncuestaPresentationActivity.this, MenuEncuestasActivity.class);
                startActivity(iMain);
                return false;
            }
        });


        //EJECUTAMOS LAS PREGUNTAS
        SetPreguntas();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent i = new Intent(EncuestaPresentationActivity.this, MenuEncuestasActivity.class);
                EncuestaPresentationActivity.this.startActivity(i);
                EncuestaPresentationActivity.this.finish();
            }
        }, tiempo[0]);


    }

    //Se llama a la clase generador correspondiente a la pregunta
    private void SetPreguntas(){
        //Se obtiene la pregunta
        preguntasList = ketitoDatabase.preguntasDao().getAll();
        if(preguntasList != null && preguntasList.size() >0){
            totalPreguntas = preguntasList.size();
            //Booleano que define si se termina el recorrido del for, es true cuando la pregunta está asociada al código del generador
            Boolean exitLoop = false;
            for (final Preguntas preg: preguntasList) {
                if(indice == preg.getIndice() && indice <= (preguntasList.size())){
                    tipoPregunta = preg.getIdTipo();
                    switch(tipoPregunta) {
                        case 1: //selección multiple
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    seleccionMultipleGenerator = new SeleccionMultipleGenerator();
                                    seleccionMultipleGenerator.init(context, rootLayout);
                                    seleccionMultipleGenerator.setLayout(preg.getIdPregunta());
                                    if(preg.getObligatoria()== true){
                                        btnSiguiente.setVisibility(View.GONE);
                                    }
                                    removeListView = seleccionMultipleGenerator.getListView();
                                }
                            });
                            exitLoop = true;
                            break;
                        case 2: //selección única
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    seleccionUnicaGenerator = new SeleccionUnicaGenerator();
                                    seleccionUnicaGenerator.init(context, rootLayout);
                                    seleccionUnicaGenerator.setLayout(preg.getIdPregunta());
                                    if(preg.getObligatoria()== true){
                                        btnSiguiente.setVisibility(View.GONE);
                                    }
                                    removeListView = seleccionUnicaGenerator.getListView();
                                }
                            });
                            exitLoop = true;
                            break;
                        case 3: //Nivel satisfacción
                            int idSimbolo = preg.getIdSimbolo();
                            if(idSimbolo == 1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        satisfaccionNumeroGenerator = new SatisfaccionNumeroGenerator();
                                        satisfaccionNumeroGenerator.init(context, rootLayout);
                                        satisfaccionNumeroGenerator.setLayout(preg.getIdPregunta());
                                        if(preg.getObligatoria()== true){
                                            btnSiguiente.setVisibility(View.GONE);
                                        }
                                        removeListView = satisfaccionNumeroGenerator.getListView();
                                    }
                                });
                                exitLoop = true;
                                break;
                            }else if(idSimbolo == 2){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        satisfaccionEstrellaGenerator = new SatisfaccionEstrellaGenerator();
                                        satisfaccionEstrellaGenerator.init(context, rootLayout);
                                        satisfaccionEstrellaGenerator.setLayout(preg.getIdPregunta());
                                        if(preg.getObligatoria()== true){
                                            btnSiguiente.setVisibility(View.GONE);
                                        }
                                        removeListView = satisfaccionEstrellaGenerator.getListView();
                                    }
                                });
                                exitLoop = true;
                                break;
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        satisfaccionEmojiGenerator = new SatisfaccionEmojiGenerator();
                                        satisfaccionEmojiGenerator.init(context, rootLayout);
                                        satisfaccionEmojiGenerator.setLayout(preg.getIdPregunta());
                                        if(preg.getObligatoria()== true){
                                            btnSiguiente.setVisibility(View.GONE);
                                        }
                                        removeListView = satisfaccionEmojiGenerator.getListView();
                                    }
                                });
                                exitLoop = true;
                                break;
                            }
                        case 4: //Pregunta abierta
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    respuestaTextoGenerator = new RespuestaTextoGenerator();
                                    respuestaTextoGenerator.init(context, rootLayout, preg.getRespuestaLarga());
                                    respuestaTextoGenerator.setLayout(preg.getIdPregunta(), "Ingrese su respuesta");
                                    if(preg.getObligatoria()== true){
                                        btnSiguiente.setVisibility(View.GONE);
                                    }
                                    removeListView = respuestaTextoGenerator.getListView();
                                }
                            });

                            exitLoop = true;
                            break;
                        case 5: //Nota/calificación
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notaCalificacionGenerator = new NotaCalificacionGenerator();
                                    notaCalificacionGenerator.init(context, rootLayout);
                                    notaCalificacionGenerator.setLayout(preg.getIdPregunta());
                                    if(preg.getObligatoria()== true){
                                        btnSiguiente.setVisibility(View.GONE);
                                    }
                                    removeListView = notaCalificacionGenerator.getListView();
                                }
                            });
                            exitLoop = true;
                            break;
                        case 6: //NoteScore
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notaScoreGenerator = new NotaScoreGenerator();
                                    notaScoreGenerator.init(context, rootLayout);
                                    notaScoreGenerator.setLayout(preg.getIdPregunta());
                                    if(preg.getObligatoria()== true){
                                        btnSiguiente.setVisibility(View.GONE);
                                    }
                                    removeListView = notaScoreGenerator.getListView();
                                }
                            });

                            exitLoop = true;
                            break;
                        default:
                            Intent intent = new Intent(EncuestaPresentationActivity.this, MenuEncuestasActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }

                if(exitLoop){
                    break;
                }
            }

        }

    }

    //Obtiene las respuestas dependiendo de la vista en la cual se encuentre el usuario.
    public void GetPreguntas(){

        preguntasList = ketitoDatabase.preguntasDao().getAll();
        if(preguntasList != null && preguntasList.size() >0){
            final String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String idEnc = ketitoDatabase.encuestaDao().getEncuestaBySelected(true).getIdEncuesta();
            //int idToma = (int) (Math.random() * 100000) + 1;
            Toma auxToma = ketitoDatabase.tomaDao().getTomaById(idToma);
            final int idToma = auxToma.getIdToma();
            Boolean exitLoop = false;
            for (final Preguntas preg: preguntasList) {
                if(indice == preg.getIndice() && indice <= (preguntasList.size()+1)){
                    tipoPregunta = preg.getIdTipo();
                    switch(tipoPregunta) {
                        case 1: //selección multiple
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    respuestaList = new ArrayList<>();
                                    respuestaList = seleccionMultipleGenerator.getRespuesta(idToma);
                                    if(respuestaList != null && respuestaList.size()>0){
                                        for (Respuesta resp: respuestaList) {
                                            ketitoDatabase.respuestasDao().insertRespuesta(resp);
                                        }
                                    }
                                    removeListView = seleccionMultipleGenerator.getListView();
                                    //idRespuestaElim = seleccionMultipleGenerator.GetIdRespuesta();
                                    indice = indice+1;
                                }
                            });
                            exitLoop = true;
                            break;
                        case 2: //selección única
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    respuesta = seleccionUnicaGenerator.getRespuesta(idToma);
                                    if(respuesta != null){
                                        ketitoDatabase.respuestasDao().insertRespuesta(respuesta);
                                    }
                                    removeListView = seleccionUnicaGenerator.getListView();
                                    idRespuestaElim = seleccionUnicaGenerator.GetIdRespuesta();
                                    indice = indice+1;
                                }
                            });
                            exitLoop = true;
                            break;
                        case 3: //Nivel satisfacción
                            int idSimbolo = preg.getIdSimbolo();
                            if(idSimbolo == 1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        respuesta = satisfaccionNumeroGenerator.getRespuesta(idToma);
                                        if(respuesta != null){
                                            ketitoDatabase.respuestasDao().insertRespuesta(respuesta);
                                        }
                                        //satisfaccionNumeroGenerator.RemoveView();
                                        removeListView = satisfaccionNumeroGenerator.getListView();
                                        idRespuestaElim = satisfaccionNumeroGenerator.GetIdRespuesta();
                                        //remView = satisfaccionNumeroGenerator.getView();
                                        indice = indice+1;
                                    }
                                });
                                exitLoop = true;
                                break;
                            }else if(idSimbolo == 2){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        respuesta = satisfaccionEstrellaGenerator.getRespuesta(idToma);
                                        if(respuesta != null){
                                            ketitoDatabase.respuestasDao().insertRespuesta(respuesta);
                                        }
                                        removeListView = satisfaccionEstrellaGenerator.getListView();
                                        idRespuestaElim = satisfaccionEstrellaGenerator.GetIdRespuesta();
                                        indice = indice+1;
                                    }
                                });

                                exitLoop = true;
                                break;
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        respuesta = satisfaccionEmojiGenerator.getRespuesta(idToma);
                                        if(respuesta != null){
                                            ketitoDatabase.respuestasDao().insertRespuesta(respuesta);
                                        }
                                        removeListView = satisfaccionEmojiGenerator.getListView();
                                        idRespuestaElim = satisfaccionEmojiGenerator.GetIdRespuesta();
                                        indice = indice+1;
                                    }
                                });
                                exitLoop = true;
                                break;
                            }
                        case 4: //Pregunta abierta
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    respuesta = respuestaTextoGenerator.getRespuesta(idToma);
                                    if(respuesta != null){
                                        ketitoDatabase.respuestasDao().insertRespuesta(respuesta);
                                    }
                                    removeListView = respuestaTextoGenerator.getListView();
                                    idRespuestaElim = respuestaTextoGenerator.GetIdRespuesta();
                                    indice = indice+1;
                                }
                            });

                            exitLoop = true;
                            break;
                        case 5: //Nota/calificación
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    respuesta= notaCalificacionGenerator.getRespuesta(idToma);
                                    if(respuesta != null){
                                        ketitoDatabase.respuestasDao().insertRespuesta(respuesta);
                                    }
                                    removeListView = notaCalificacionGenerator.getListView();
                                    //idRespuestaElim = notaCalificacionGenerator.GetIdRespuesta();
                                    indice = indice+1;
                                }
                            });
                            exitLoop = true;
                            break;
                        case 6: //NoteScore
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    respuesta = notaScoreGenerator.getRespuesta(idToma);
                                    if(respuesta != null){
                                        ketitoDatabase.respuestasDao().insertRespuesta(respuesta);
                                    }
                                    removeListView = notaScoreGenerator.getListView();
                                    idRespuestaElim = notaScoreGenerator.GetIdRespuesta();
                                    indice = indice+1;
                                }
                            });

                            exitLoop = true;
                            break;
                        default:
                            Intent intent = new Intent(EncuestaPresentationActivity.this, MenuEncuestasActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }

                if(exitLoop){
                    break;
                }
            }

        }

    }


    public void removeRespuesta(String idRespuesta){
        Respuesta respuestaAux = new Respuesta();
        respuestaAux = ketitoDatabase.respuestasDao().getRespuestaById(idRespuesta);
        if(respuestaAux != null){
            ketitoDatabase.respuestasDao().deleteRespuesta(respuestaAux);
        }
    }

    public void deleteRespuestaList(List<Respuesta> listResp){
        if(listResp != null){
            for (Respuesta resp: listResp) {
                Respuesta respuestaAux = new Respuesta();
                respuestaAux = ketitoDatabase.respuestasDao().getRespuestaById(resp.getIdRespuesta());
                ketitoDatabase.respuestasDao().deleteRespuesta(respuestaAux);
            }
        }
    }

    public void removeRView(){
        for (View v: removeListView
        ) {
            ViewGroup parent = (ViewGroup) v.getParent();
            parent.removeView(v);
        }

    }

}
