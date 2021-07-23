package cl.ckelar.android.ketito.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadImage extends AsyncTask<String, Void, Bitmap> {

    /**
     * @param listener
     */
    public LoadImage(Listener listener) {
        mListener = listener;
    }

    public interface Listener{
        void onImageLoaded(Bitmap bitmap);
        void onError();
    }
    private Listener mListener;

    /**
     * @param args
     * @return objeto bitmap
     */
    @Override
    protected Bitmap doInBackground(String... args) {
        try {
            return BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param bitmap objeto de la Clase Bitmap
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            mListener.onImageLoaded(bitmap);
        } else {
            mListener.onError();
        }
    }

}
