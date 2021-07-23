package cl.ckelar.android.ketito.helpers.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cl.ckelar.android.ketito.R;
import cl.ckelar.android.ketito.dto.Empresa;

/**
 * Clase que extiende a BaseAdapter para el componente ImageView
 */
public class AdapterImageList extends BaseAdapter {

    private final Activity _context;
    private final ArrayList<Empresa> elements;
    private int layoutResource;

    public class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    public AdapterImageList(Activity context, ArrayList<Empresa> elements){
        this._context = context;
        this.elements = elements;
    }


    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Empresa getItem(int position) {
        Empresa emp = elements.get(position);
        return emp;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;


        if(convertView == null) {
            //LayoutInflater inflater = _context.getLayoutInflater();
            //convertView = inflater.inflate(R.layout.list_business_wi,parent,false);

            convertView = LayoutInflater.from(_context).inflate(
                    R.layout.list_business_wi, null);

            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.item_title);
            holder.imageView = (ImageView)convertView.findViewById(R.id.item_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(elements.get(position).getRazon());
        String url = elements.get(position).getRutaImagen();

        //* Carga url de imagen en ImageView
        holder.imageView.setMaxWidth(60);
        holder.imageView.setMaxHeight(50);
        Picasso.get().load(url).into(holder.imageView);

        return convertView;
    }

}
