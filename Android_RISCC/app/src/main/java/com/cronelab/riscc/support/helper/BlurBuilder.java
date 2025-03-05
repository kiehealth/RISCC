package com.cronelab.riscc.support.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

public class BlurBuilder {
    private static final float BITMAP_SCALE = 0.1f;
    private static final float BLUR_RADIUS = 7.5f;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS); theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut); tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    @SuppressLint("NewApi")
    public static void blur(final Context context, final Bitmap bitmap, final View view) {

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                Paint paint = new Paint(); paint.setFilterBitmap(true);
                Bitmap cropImage = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() - view.getHeight(), bitmap.getWidth(), view.getHeight());
                return BlurBuilder.blur(context, cropImage);
            }


            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap); if (bitmap != null) {
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        view.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));
                    } else {
                        view.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                    }

                }

            }
        }.execute();


    }

}