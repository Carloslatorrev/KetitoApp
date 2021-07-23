package cl.ckelar.android.ketito.helpers.generator;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Preguntas;
import cl.ckelar.android.ketito.dto.PreguntasAlternativas;
import cl.ckelar.android.ketito.dto.Respuesta;
import cl.ckelar.android.ketito.helpers.db.KetitoDatabase;

public class NotaScoreGenerator {
    private Context contextT;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioButton radioButton6;
    private RadioButton radioButton7;
    private RadioButton radioButton8;
    private RadioButton radioButton9;
    private RadioButton radioButton10;
    private TextView title;
    private TextView txtDescription;

    private KetitoDatabase ketitoDatabase;
    private Preguntas pregunta;
    private PreguntasAlternativas preguntasAlternativas;
    private List<PreguntasAlternativas> preguntasAlternativasList;
    private int idPreg;
    private Respuesta respuesta;
    private String idRespuesta;

    /**
     * Inicializa los elementos de la vista
     * @param context Context del Activity
     * @param root Raiz de la interfaz, correspondiente a un ViewGroup o grupo de elementos de la vista.
     */
    public void init(Context context, ViewGroup root){
        contextT = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.partial_encuesta_notescore, root, true);
        title = view.findViewById(R.id.noteScoreTitle);
        txtDescription = view.findViewById(R.id.noteScoreDescription);
        radioGroup = view.findViewById(R.id.noteScoreRadioGroup);
        radioButton1 = view.findViewById(R.id.noteScoreRadioButton1);
        radioButton2 = view.findViewById(R.id.noteScoreRadioButton2);
        radioButton3 = view.findViewById(R.id.noteScoreRadioButton3);
        radioButton4 = view.findViewById(R.id.noteScoreRadioButton4);
        radioButton5 = view.findViewById(R.id.noteScoreRadioButton5);
        radioButton6 = view.findViewById(R.id.noteScoreRadioButton6);
        radioButton7 = view.findViewById(R.id.noteScoreRadioButton7);
        radioButton8 = view.findViewById(R.id.noteScoreRadioButton8);
        radioButton9 = view.findViewById(R.id.noteScoreRadioButton9);
        radioButton10 = view.findViewById(R.id.noteScoreRadioButton10);

        ketitoDatabase = ketitoDatabase.getInstance(context);
    }

    /**
     * Realiza el set de las variables y elementos de la vista asociada a la pregunta
     * @param idPregunta Id del objeto pregunta
     */
    public void setLayout(int idPregunta){
        idPreg = idPregunta;
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
    }

    /**
     * Obtiene la respuesta de los elementos de la vista en relación a la selección del usuario.
     * @param idToma Id de la Toma correspondiente a la encuesta
     * @return Retorna el objeto respuesta de la clase Respuesta, con los parametros de la selección del usuario
     */
    public Respuesta getRespuesta(int idToma){
        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = radioGroup.findViewById(id);
        int idx = radioGroup.indexOfChild(radioButton)+1;
        String textRespuesta= "";
        textRespuesta = null;
        respuesta = new Respuesta();
        int random = (int) (Math.random() * 1000) + 1;
        idRespuesta = "R"+idPreg+idToma+String.valueOf(random);
        respuesta.setIdRespuesta(idRespuesta);
        respuesta.setIdPregunta(idPreg);
        respuesta.setIdAlternativa("null");
        respuesta.setIdToma(idToma);
        respuesta.setNivel(String.valueOf(idx));
        respuesta.setRespuesta(textRespuesta);
        return respuesta;
    }

    /**
     * Crea el listado de elementos de la vista
     * @return listado o list View de elementos de la vista
     */
    public List<View> getListView(){
        List<View> listaView = new ArrayList<>();
        listaView.add(radioGroup);
        listaView.add(title);
        listaView.add(txtDescription);
        return listaView;
    }

    /** Retorna el Id de la respuesta
     * @return IdRespuesta
     */
    public String GetIdRespuesta(){
        return idRespuesta;
    }

}
