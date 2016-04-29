package fbla.com.fbla_app_src;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by AromAdmin on 3/9/2016.
 */
public class DownloadImagesClass extends AsyncTask<ArrayList<String>, Integer, ArrayList<Drawable>>
{
    ArrayList<Drawable> dlList = new ArrayList<>();
    mostupvotessearch from;
    trendingsearch fromT;
    recentsearch fromR;
    searchByTags fromS;
    commentView fromC;
    yourupvotes fromY;

    public commentView getFromC() {
        return fromC;
    }

    public void setFromC(commentView fromC) {
        this.fromC = fromC;
    }

    public searchByTags getFromS() {
        return fromS;
    }

    public void setFromS(searchByTags fromS) {
        this.fromS = fromS;
    }

    public recentsearch getFromR() {
        return fromR;
    }

    public void setFromR(recentsearch fromR) {
        this.fromR = fromR;
    }

    public trendingsearch getFromT() {
        return fromT;
    }

    public void setFromT(trendingsearch fromT) {
        this.fromT = fromT;
    }

    public mostupvotessearch getFrom() {
        return from;
    }

    public void setFrom(mostupvotessearch from) {
        this.from = from;
    }

    public ArrayList<Drawable> getDlList() {
        return dlList;
    }

    public yourupvotes getFromY(){return fromY;}

    public void setFromY(yourupvotes fromY){this.fromY = fromY;}

    public void setDlList(ArrayList<Drawable> dlList) {
        this.dlList = dlList;
    }

    @Override
    protected ArrayList<Drawable> doInBackground(ArrayList<String>... params)
    {
        ArrayList<String> ListofURls = params[0];
        ArrayList<Drawable> images = new ArrayList<>();
        for (int i = 0;i<ListofURls.size();i++)
        {
            images.add(downloadImage(ListofURls.get(i)));
        }
        return images;

    }
    private Drawable downloadImage(String _url)
    {
        Log.i("DOWNLOADING", _url);
        //Prepare to download image
        URL url;
        BufferedOutputStream out;
        InputStream in;
        BufferedInputStream buf;

        //BufferedInputStream buf;
        try {
            url = new URL(_url);
            in = url.openStream();

            // Read the inputstream
            buf = new BufferedInputStream(in);

            // Convert the BufferedInputStream to a Bitmap
            Bitmap bMap = BitmapFactory.decodeStream(buf);
            if (in != null)
            {
                in.close();
            }
            if (buf != null)
            {
                buf.close();
            }
            Log.i("Downloaded Image", url.toString());
            bMap = Bitmap.createScaledBitmap(bMap,bMap.getWidth()/4,bMap.getHeight()/4,true);
            return new BitmapDrawable(bMap);

        } catch (Exception e)
        {
            Log.e("Error reading file", e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Drawable> drawables)
    {
        dlList = drawables;
        super.onPostExecute(drawables);
        if(from != null)
        {
            update(from);
        }
        else if(fromT != null)
        {
            update(fromT);
        }
        else if(fromR != null)
        {
            update(fromR);
        }
        else if(fromS != null)
        {
            update(fromS);
        }
        else
        {
            update(fromC);
        }

    }
    public void update(mostupvotessearch muv)
    {
        muv.setDL(dlList);
        muv.hideSpinner();
        try {
            muv.populateListView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(trendingsearch muv)
    {
        muv.setDL(dlList);
        try {
            muv.populateListView();
            muv.hideSpinner();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(recentsearch muv)
    {
        muv.setDL(dlList);
        muv.hideSpinner();
        try {
            muv.populateListView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void update(searchByTags muv)
    {
        muv.setDraw(dlList);
        muv.hideSpinner();
        try {
            muv.populateListView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(commentView muv)
    {
        try {
            muv.populateListView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
