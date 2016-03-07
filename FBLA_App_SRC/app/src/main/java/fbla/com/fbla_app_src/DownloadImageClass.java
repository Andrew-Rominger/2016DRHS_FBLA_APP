package fbla.com.fbla_app_src;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by AromAdmin on 3/6/2016.
 */
public class DownloadImageClass extends AsyncTask<String, Integer, Drawable>
{
    private ImageView imageView;
    private RelativeLayout relativeLayout;
    private Drawable drawAble;

    public Drawable getDrawAble() {
        return drawAble;
    }

    public void setDrawAble(Drawable drawAble)
    {
        this.drawAble = drawAble;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    public RelativeLayout getRelativeLayout() {
        return relativeLayout;
    }

    public void setRelativeLayout(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Drawable doInBackground(String... arg0)
    {
        Drawable dl = downloadImage(arg0[0]);
        setDrawAble(dl);
        return dl;
    }
    protected void onPostExecute(Drawable image)
    {
        profilePage.hideSpinner();
        if(imageView != null)
        {
            setImage(image, imageView);
            imageView =  null;
        }
        else if(relativeLayout != null)
        {
            setPageReletiveLayout(image, relativeLayout);
            relativeLayout = null;
        }
        else
        {
            return;
        }


    }

    private Drawable downloadImage(String _url)
    {
        //Prepare to download image
        URL url;
        BufferedOutputStream out;
        InputStream in;
        BufferedInputStream buf;

        //BufferedInputStream buf;
        try {
            url = new URL(_url);
            in = url.openStream();

            /*
             * THIS IS NOT NEEDED
             *
             * YOU TRY TO CREATE AN ACTUAL IMAGE HERE, BY WRITING
             * TO A NEW FILE
             * YOU ONLY NEED TO READ THE INPUTSTREAM
             * AND CONVERT THAT TO A BITMAP
            out = new BufferedOutputStream(new FileOutputStream("testImage.jpg"));
            int i;

             while ((i = in.read()) != -1) {
                 out.write(i);
             }
             out.close();
             in.close();
             */

            // Read the inputstream
            buf = new BufferedInputStream(in);

            // Convert the BufferedInputStream to a Bitmap
            Bitmap bMap = BitmapFactory.decodeStream(buf);
            if (in != null) {
                in.close();
            }
            if (buf != null) {
                buf.close();
            }

            return new BitmapDrawable(bMap);

        } catch (Exception e) {
            Log.e("Error reading file", e.toString());
        }

        return null;
    }
    public void setImage(Drawable image, ImageView IV)
    {
        IV.setImageDrawable(image);


    }
    @SuppressWarnings("deprecation")
    public void setPageReletiveLayout(Drawable image, RelativeLayout rl)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            rl.setBackground(image);
        }
        else
        {
            rl.setBackgroundDrawable(image);
        }
    }

}
