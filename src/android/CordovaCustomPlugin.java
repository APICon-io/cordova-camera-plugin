package info.apicon.plugins.custom;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * This class echoes a string called from JavaScript.
 */
public class CordovaCustomPlugin extends CordovaPlugin  implements SurfaceHolder.Callback{

    private Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button capture_image;
    FrameLayout layout;
    LinearLayout innerLayout;
    CallbackContext c;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {

         layout = (FrameLayout) webView.getView().getParent();
        innerLayout=new LinearLayout(layout.getContext());
        innerLayout.setOrientation(LinearLayout.VERTICAL);

        capture_image = new Button(layout.getContext());
        surfaceView = new SurfaceView(cordova.getActivity());
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(CordovaCustomPlugin.this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,10f));
        capture_image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,0.9f));
        capture_image.setText("CAPTURE");
        capture_image.setGravity(Gravity.CENTER);




        capture_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                capture();
            }
        });



      //  layout.addView(surfaceView);
    }

    private void capture() {

        mCamera.takePicture(null, null, null, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Toast.makeText(cordova.getActivity(), "Picture Taken",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("image_arr", data);
                cordova.getActivity().setResult(RESULT_OK, intent);

                camera.stopPreview();
                if (camera != null) {
                    camera.release();
                    mCamera = null;


                    if (mCamera != null) {
                        mCamera.release();
                        mCamera = null;
                    }
                    coolMethod("noth", c);

                    ((ViewGroup) innerLayout.getParent()).removeView(innerLayout);

                 //   ((ViewGroup) capture_image.getParent()).removeView(capture_image);
                }

            }
        });


    }
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("Camera")) {
            // String message = args.getString(0);
            int direction=args.getInt(0);

            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {

                        mCamera = Camera.open(direction);
                        mCamera.setDisplayOrientation(90);
                        mCamera.setPreviewDisplay(surfaceHolder);
                        mCamera.startPreview();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(surfaceView.getParent() != null) {
                        ((ViewGroup)surfaceView.getParent()).removeView(surfaceView);
                        ((ViewGroup)capture_image.getParent()).removeView(capture_image);// <- fix
                    }
                    innerLayout.addView(surfaceView,0);
                    innerLayout.addView(capture_image,1);
                    layout.addView(innerLayout);

                    c=callbackContext;

                  //  callbackContext.success(); // Thread-safe.
                }
            });

            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
    //    Toast.makeText(cordova.getActivity(), "Sent", Toast.LENGTH_SHORT).show();
        if (message != null && message.length() > 0) {
          //  Toast.makeText(cordova.getActivity(), "Sent2", Toast.LENGTH_SHORT).show();
            callbackContext.success("captured");
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("Surface Changed", "format   ==   " + format + ",   width  ===  "
                + width + ", height   ===    " + height);
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
