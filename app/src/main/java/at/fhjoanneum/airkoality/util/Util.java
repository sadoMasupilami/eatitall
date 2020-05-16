
package at.fhjoanneum.airkoality.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.View;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import at.fhjoanneum.airkoality.R;

public class Util {

    public static Bitmap getBitmapFromView(View view) {
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        view.draw(c);
        return b;
    }

    public static Uri savePng(Context context, Bitmap image, String filename) throws IOException {
        File imagesFolder = new File(context.getFilesDir(), "images");
        Uri uri;
        imagesFolder.mkdirs();
        File file = new File(imagesFolder, filename);

        FileOutputStream stream = new FileOutputStream(file);
        image.compress(Bitmap.CompressFormat.PNG, 90, stream);
        stream.flush();
        stream.close();
        uri = FileProvider.getUriForFile(context, context.getString(R.string.provider_authority), file);
        return uri;
    }
}
