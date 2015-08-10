package com.datapush.buyhand.view;
import java.io.FileNotFoundException;  

import java.io.FileOutputStream;  
import java.io.IOException;  

import com.daoshun.lib.util.DensityUtils;
import com.datapush.buyhand.common.Settings;

import android.app.Activity;  
import android.graphics.Bitmap;  
import android.graphics.Rect;  
import android.view.View;
   
/**局域截取屏幕
 * 
 * @author Fei
 *
 */

public class ScreenShot {  
	
	static Activity activity1 = null;
	
    private static Bitmap takeScreenShot(Activity activity){  
    	activity1 = activity;
        View view = activity.getWindow().getDecorView();  
        view.setDrawingCacheEnabled(true);  
        view.buildDrawingCache();  
        Bitmap b1 = view.getDrawingCache();  
           
        Rect frame = new Rect();    
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);    
        int statusBarHeight = frame.top;    
        System.out.println(statusBarHeight);  
           
        int width = Settings.DISPLAY_WIDTH;    
        int height = Settings.DISPLAY_HEIGHT;    
        
        int w = (Settings.DISPLAY_WIDTH - DensityUtils.dp2px(activity1, 90)) / 5;
        
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight+DensityUtils.dp2px(activity1, 85), width, height - 
        		(w*3/2+statusBarHeight+DensityUtils.dp2px(activity1, 101)));
        view.destroyDrawingCache();  
        return b;  
    }  
       
    private static void savePic(Bitmap b,String strFileName){  
        FileOutputStream fos = null;  
        try {  
            
            fos = new FileOutputStream(strFileName);
            if (null != fos)  
            {  
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);  
                fos.flush();  
                fos.close();
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }  
       
    public static void shoot(Activity a, String str1){
        ScreenShot.savePic(ScreenShot.takeScreenShot(a), str1);  
    }
}  
