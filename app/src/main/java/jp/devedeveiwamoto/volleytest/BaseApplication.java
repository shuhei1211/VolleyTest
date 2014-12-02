package jp.devedeveiwamoto.volleytest;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by shuhe_000 on 2014/10/16.
 */
public class BaseApplication extends Application {
    private static RequestQueue requestQueue;
    private static ImageLoader imageLoader;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "Application onCreate");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        imageLoader = new ImageLoader(requestQueue, new ImageLruCache());
        context = getApplicationContext();
    }

    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }
        return requestQueue;
    }

    public static ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(requestQueue, new ImageLruCache());
        }
        return imageLoader;
    }

    public static Context getContext() {
        return context;
    }

    public static class ImageLruCache implements ImageLoader.ImageCache {

        // key String : value Bitmap
        private LruCache<String, Bitmap> memoryChache;

        public ImageLruCache() {
            int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
            int cacheSize = maxMemory / 8;

            memoryChache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return memoryChache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            memoryChache.put(url, bitmap);
        }
    }
}
