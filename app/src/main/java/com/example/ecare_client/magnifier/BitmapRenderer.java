/* Code developed by Team Morgaint
 * for Subject IT Project COMP30022
 * Team member:
 * Chengyao Xu
 * Jin Wei Loh
 * Philip Cervenjak
 * Qianqian Zheng
 * Sicong Hu
 */
package com.example.ecare_client.magnifier;


import android.graphics.Bitmap;

/**
 * Created by handspiel on 11.08.15.
 */
public interface BitmapRenderer {
    /**
     * renders bitmaps.
     * You can also use it as a setter method.
     * @param bitmap
     */
    void renderBitmap(Bitmap bitmap);
}
