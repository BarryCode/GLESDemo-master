package idv.chatea.gldemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import idv.chatea.gldemo.gles20.RectangleTransparentTexture;

public class GLES2_Transparent_Texture_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new MyRenderer());
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(glSurfaceView);
    }

    private class MyRenderer implements GLSurfaceView.Renderer {
        private float[] mProjectMatrix = new float[16];
        private float[] mViewMatrix = new float[16];

        private RectangleTransparentTexture mTexture;

        @Override
        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            GLES20.glClearColor(0.4f, 0.4f, 0.4f, 1.0f);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android_logo);
            mTexture = new RectangleTransparentTexture(bitmap);
            bitmap.recycle();
        }

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / height;
            Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 1.5f, 10);
        }

        @Override
        public void onDrawFrame(GL10 unused) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            Matrix.setLookAtM(mViewMatrix, 0,
                    0f, 0f, 5f,
                    0f, 0f, 0f,
                    0f, 1f, 0);

            float[] mvpMatrix = new float[16];
            Matrix.multiplyMM(mvpMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);

            float[] moduleMatrix = new float[16];
            Matrix.setIdentityM(moduleMatrix, 0);

            Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, moduleMatrix, 0);

            mTexture.draw(mvpMatrix);
        }
    }
}
