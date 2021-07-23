package cl.ckelar.android.ketito.helpers.generator;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;

public class SeleccionMultipleGenerator {

    private Context contextT;
    private CheckBox checkBox;
    private TextView title;
    private TextView txtDescription;
    private LinearLayout linearLayout;
    private KetitoDatabase ketitoDatabase;
    private Preguntas pregunta;
    private PreguntasAlternativas preguntasAlternativas;
    private List<PreguntasAlternativas> preguntasAlternativasList;
    private View view;
    private int idPreg;
    private Respuesta respuesta;
    private String idRespuesta;


    /**
     * Inicializa los elementos y componentes de la vista
     * @param context Context del Activity
     * @param root Raiz de la interfaz, correspondiente a un ViewGroup o grupo de elementos de la vista.
     */
    public void init(Context context, ViewGroup root){
        contextT = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.partial_encuesta_seleccionmultiple, root, true);
        title = view.findViewById(R.id.seleccionMultipleTitle);
        txtDescription = view.findViewById(R.id.seleccionMultipleDescription);
        linearLayout = view.findViewById(R.id.seleccionMultipleLayout_checkbox);
        ketitoDatabase = ketitoDatabase.getInstance(context);

    }

    /**
     * Realiza el set de las variables y elementos de la vista asociada a la pregunta
     * @param idPregunta Id del objeto pregunta
     */
    public void setLayout(int idPregunta){
        idPreg= idPregunta;
        pregunta = ketitoDatabase.preguntasDao().getPreguntasById(idPregunta);
        String titleText = title.getText().toString();
        titleText = titleText.replace("%title", pregunta.getPregunta());
        title.setText(titleText);
        String subtitle = pregunta.getSubtitulo();
        String descriptionText = txtDescription.getText().toString();
        if(subtitle.length()>0 && subtitle != null){
            descriptionText = descriptionText.replace("%description", subtitle);
            txtDescription.setText(descriptionText);
        }else{
            descriptionText = descriptionText.replace("%description", "");
            txtDescription.setText(descriptionText);
        }
        preguntasAlternativasList = ketitoDatabase.preguntasAlternativasDao().getListPreguntasAlternativasByIdPregunta(idPregunta);
        for (PreguntasAlternativas pregAlt: preguntasAlternativasList) {
            int id= pregAlt.getIdAlternativa();
            checkBox = new CheckBox(contextT);
            checkBox.setButtonDrawable(R.drawable.seleccionmultiple_checbox);
            checkBox.setId(id);
            checkBox.setTextColor(ContextCompat.getColor(contextT, R.color.baseKetito));
            checkBox.setText(pregAlt.getAlternativa());
            checkBox.setTextSize(24);
            checkBox.setTypeface(null, Typeface.BOLD);
            int paddingDp = 15;
            float density = contextT.getResources().getDisplayMetrics().density;
            int paddingPixel = (int)(paddingDp * density);
            checkBox.setPadding(0,0,paddingDp,0);
            linearLayout.addView(checkBox);
        }

    }

    /**
     * Obtiene la respuesta de los elementos de la vista en relación a la selección del usuario.
     * @param idToma Id de la Toma correspondiente a la encuesta
     * @return Retorna el objeto respuesta de la clase Respuesta, con los parametros de la selección del usuario
     */
    public List<Respuesta> getRespuesta(int idToma){
        List<CheckBox> checkBoxList = new ArrayList<>();
        List<Respuesta> respuestaList = new ArrayList<>();
        for (PreguntasAlternativas preg: preguntasAlternativasList) {
            CheckBox check = view.findViewById(preg.getIdAlternativa());
            if(check.isChecked()){
                checkBoxList.add(check);
            }
        }

        if(checkBoxList != null && checkBoxList.size() >0){
            for (CheckBox check: checkBoxList){
                int id= check.getId();
                String textRespuesta= check.getText().toString();
                respuesta = new Respuesta();
                int random = (int) (Math.random() * 1000) + 1;
                String idRespuesta = "R"+idPreg+idToma+String.valueOf(random);
                respuesta.setIdRespuesta(idRespuesta);
                respuesta.setIdPregunta(idPreg);
                respuesta.setIdToma(idToma);
                respuesta.setIdAlternativa(String.valueOf(id));
                respuesta.setNivel("null");
                respuesta.setRespuesta(null);
                respuestaList.add(respuesta);
            }
        }else{
            respuesta = new Respuesta();
            int random = (int) (Math.random() * 1000) + 1;
            idRespuesta = "R"+idPreg+idToma+String.valueOf(random);
            respuesta.setIdRespuesta(idRespuesta);
            respuesta.setIdPregunta(idPreg);
            respuesta.setIdAlternativa("null");
            respuesta.setIdToma(idToma);
            respuesta.setNivel("null");
            respuesta.setRespuesta(null);
            respuestaList.add(respuesta);
        }

        return respuestaList;
    }

    /**
     * Crea el listado de elementos de la vista
     * @return listado o list View de elementos de la vista
     */
    public List<View> getListView(){
        List<View> listaView = new ArrayList<>();
        listaView.add(linearLayout);
        listaView.add(title);
        listaView.add(txtDescription);
        return listaView;
    }



}
