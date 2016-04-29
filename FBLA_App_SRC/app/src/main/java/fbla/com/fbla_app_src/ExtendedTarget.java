package fbla.com.fbla_app_src;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by 2144285 on 4/28/2016.
 */
public class ExtendedTarget implements Target
{
    private BitmapDrawable bd;

    public BitmapDrawable getBd() {
        return bd;
    }

    public void setBd(BitmapDrawable bd) {
        this.bd = bd;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
