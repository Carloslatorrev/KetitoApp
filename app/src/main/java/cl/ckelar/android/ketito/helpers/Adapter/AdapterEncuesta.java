package cl.ckelar.android.ketito.helpers.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Encuesta;

/**
 * Clase que extiende BaseAdapter para la creación del componente de la lista de encuestas
 */
public class AdapterEncuesta extends BaseAdapter {
    private final Activity _context;
    private final ArrayList<Encuesta> elements;
    private int layoutResource;

    public AdapterEncuesta(Activity context, ArrayList<Encuesta> elements) {
        _context = context;
        this.elements = elements;
    }

    public class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    @Override
    public int getCount() {

        return elements.size();
    }

    @Override
    public Encuesta getItem(int position) {
        Encuesta enc = elements.get(position);
        return enc;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        AdapterEncuesta.ViewHolder holder = null;


        if(convertView == null) {
            //LayoutInflater inflater = _context.getLayoutInflater();
            //convertView = inflater.inflate(R.layout.list_business_wi,parent,false);

            convertView = LayoutInflater.from(_context).inflate(
                    R.layout.list_encuestas_wi, null);

            holder = new AdapterEncuesta.ViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.item_textoEncuesta);

            convertView.setTag(holder);
        } else {
            holder = (AdapterEncuesta.ViewHolder) convertView.getTag();
        }

        holder.textView.setText(elements.get(position).getTitulo());


        return convertView;
    }
}
