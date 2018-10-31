package fr.remram.taquindroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageTools {

    public static String getExtension(String path)
    {
        int index = path.lastIndexOf(".");
        if(index > 0 && index < path.length() - 1 && index >= path.length() - 5)
            return path.substring(index).toLowerCase();
        else
            return "";
    }

    public static int getOrientation(Context context, Uri photoUri)
    {
        try {
            InputStream istream = context.getContentResolver().openInputStream(photoUri);
            if(istream == null)
                return ExifInterface.ORIENTATION_UNDEFINED;

            File outputDir = context.getCacheDir();
            String extension = getExtension(photoUri.getPath());
            File tempfile = File.createTempFile("taquindroid", extension, outputDir);
            FileOutputStream ostream = new FileOutputStream(tempfile);

            byte[] buffer = new byte[4096];
            int len;
            while((len = istream.read(buffer)) != -1)
                ostream.write(buffer, 0, len);
            ostream.close();
            istream.close();

            ExifInterface exif = new ExifInterface(tempfile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            if(!tempfile.delete())
                Log.e("TAQUINDROID", String.format("Couldn't delete teporary file %s", tempfile.getAbsolutePath()));
            return orientation;
        } catch(IOException e) {
            Log.e("TAQUINDROID", "Error reading image orientation");
            return ExifInterface.ORIENTATION_UNDEFINED;
        }
    }

    public static Matrix getImageMatrix(int orientation)
    {
        Matrix matrix = new Matrix();
        switch(orientation)
        {
            case ExifInterface.ORIENTATION_NORMAL:
                return null;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return null;
        }
        return matrix;
    }

    public static Matrix getImageMatrix(Context context, Uri photoUri)
    {
        return getImageMatrix(getOrientation(context, photoUri));
    }

    public static Bitmap orientBitmap(Bitmap image, int orientation)
    {
        Matrix matrix = getImageMatrix(orientation);
        if(matrix == null)
            return image;
        try {
            Bitmap image2 = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
            image.recycle();
            return image2;
        }
        catch(OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmap(Context context, Uri uri)
    {
        try {
            ParcelFileDescriptor descriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            if(descriptor != null)
            {
                FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                descriptor.close();

                int orientation = ImageTools.getOrientation(context, uri);
                image = ImageTools.orientBitmap(image, orientation);

                return image;
            }
            else
                return null;
        } catch(IOException e) {
            return null;
        }
    }

}
