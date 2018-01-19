package hua.music.huamusic.widget;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hua.music.huamusic.R;


/**
 * 网易音乐扫描音乐控件。
 * 效果是一个手机加一个放大镜，同时还有上下扫描的动画。
 *
 * @author hua
 * @version 2018/1/5 16:48
 */

public class ScanMusicView extends View {

    private int mCurScanState = SCAN_STATE_NORMAL;
    public static final int SCAN_STATE_NORMAL = 0;
    public static final int SCAN_STATE_SCANNING = 1;
    public static final int SCAN_STATE_COMPLETE = 2;

    private Bitmap mPhoneScaledBitmap;
    private Bitmap mSearchScaledBitmap;
    private Bitmap mCompleteBitmap;

    private ShaderAnimRunnable mShaderAnimRunnable;

    private SearchAnimRunnable mSearchAnimRunnable;

    private Bitmap mCanvasBitmap;
    private Canvas mTempCanvas;
    private Paint mPhonePaint;

    public ScanMusicView(Context context) {
        this(context, null);
    }

    public ScanMusicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanMusicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mPhonePaint = new Paint();
        mPhonePaint.setAntiAlias(true);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScanMusicView);
        float mScalePhone = array.getFloat(R.styleable.ScanMusicView_scale_phone, 1.0f);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = (int) (1 / mScalePhone);
        mPhoneScaledBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.scan_music_phone, options);

        Drawable mSearchDrawable = array.getDrawable(R.styleable.ScanMusicView_src_search);
        float mScaleSearch = array.getFloat(R.styleable.ScanMusicView_scale_search, 1.0f);
        mSearchScaledBitmap = drawableToBitmap(mSearchDrawable, mScaleSearch);
        if (mSearchScaledBitmap == null) {
            options.inSampleSize = 2;
            mSearchScaledBitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.scan_music_search, options);
        }

        Drawable completeDrawable = array.getDrawable(R.styleable.ScanMusicView_src_complete);
        float completeScale = array.getFloat(R.styleable.ScanMusicView_scale_complete, 1.0f);
        mCompleteBitmap = drawableToBitmap(completeDrawable, completeScale);
        if (mCompleteBitmap == null) {
            options.inSampleSize = 2;
            mCompleteBitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.scan_music_complete, options);
        }

        array.recycle();

        resolvePhoneImage(mPhoneScaledBitmap);

        mCanvasBitmap = Bitmap.createBitmap(mPhoneScaledBitmap.getWidth(), mPhoneScaledBitmap.getHeight(),
                mPhoneScaledBitmap.getConfig());
        mTempCanvas = new Canvas(mCanvasBitmap);

        mSearchAnimRunnable = new SearchAnimRunnable();
    }

    private Bitmap drawableToBitmap(Drawable srcDrawable, float scale) {
        if (srcDrawable != null && srcDrawable instanceof BitmapDrawable) {
            Bitmap srcBitmap = ((BitmapDrawable) srcDrawable).getBitmap();
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale, srcBitmap.getWidth() / 2, srcBitmap.getHeight() / 2);
            return Bitmap.createBitmap(srcBitmap,
                    0, 0,
                    srcBitmap.getWidth(), srcBitmap.getHeight(),
                    matrix, true);
        }
        return null;
    }

    /**
     * 解析手机图片，得到中间网格像素的起点、终点的位置和颜色值（实测颜色值是一样的）
     */
    private void resolvePhoneImage(Bitmap phoneBitmap) {
        if (phoneBitmap != null) {
            int srcWidth = phoneBitmap.getWidth();
            int srcHeight = phoneBitmap.getHeight();

            //取出所有像素值
            int[] pixels = new int[srcWidth * srcHeight];
            phoneBitmap.getPixels(pixels, 0, srcWidth, 0, 0, srcWidth, srcHeight);

            //对所有像素值遍历，并统计他们出现的次数，次数最多的就是中间网格的颜色值
            //存储像素值
            List<Integer> integerList = new ArrayList<>();
            //存储像素值，以及对应像素值的个数
            SparseIntArray sparseIntArray = new SparseIntArray();
            int mSideColor = -1;
            for (int pixel : pixels) {
                if (pixel != 0) {
                    if (integerList.contains(pixel)) {
                        sparseIntArray.put(pixel,
                                sparseIntArray.get(pixel) + 1);
                    } else {
                        integerList.add(pixel);
                        sparseIntArray.put(pixel, 1);
                    }
                    //保存边缘颜色值，作为LinearGradient的边界颜色
                    if (mSideColor == -1) {
                        mSideColor = pixel;
                    }
                }
            }
            int mScanColor = integerList.get(0);
            for (Integer pixel : integerList) {
                if (sparseIntArray.get(pixel) > sparseIntArray.get(mScanColor)) {
                    mScanColor = pixel;
                }
            }

            //顺序遍历所有像素，计算mScanColor出现的起始点
            Point pointStart = null;
            for (int i = 0; i < pixels.length; i++) {
                int pixel = pixels[i];
                if (pixel == mScanColor) {
                    int startX = (i + 1) % srcWidth == 0 ?
                            srcWidth - 1 :
                            (i + 1) % srcWidth - 1;
                    int startY = (i + 1) % srcWidth == 0 ?
                            (i + 1) / srcWidth :
                            (i + 1) / srcWidth + 1;

                    pointStart = new Point(startX, startY);
                    break;
                }
            }

            //倒序遍历所有像素，计算mScanColor出现的终点
            Point pointEnd = null;
            for (int i = pixels.length - 1; i >= 0; i--) {
                int pixel = pixels[i];
                if (pixel == mScanColor) {
                    int endX = (i + 1) % srcWidth == 0 ?
                            srcWidth - 1 :
                            (i + 1) % srcWidth - 1;
                    int endY = (i + 1) % srcWidth == 0 ?
                            (i + 1) / srcWidth :
                            (i + 1) / srcWidth + 1;
                    pointEnd = new Point(endX, endY);
                    break;
                }
            }

            if (pointStart != null && pointEnd != null) {
                mSideColor |= 0xff000000;
                mScanColor |= 0xff000000;
                mShaderAnimRunnable = new ShaderAnimRunnable(pointStart, pointEnd, mScanColor, mSideColor);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(Math.max(mPhoneScaledBitmap.getWidth(), mSearchScaledBitmap.getWidth()),
                    widthSize);
        } else {
            width = Math.max(mPhoneScaledBitmap.getWidth(), mSearchScaledBitmap.getWidth());
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(Math.max(mPhoneScaledBitmap.getHeight(), mSearchScaledBitmap.getHeight()),
                    heightSize);
        } else {
            height = Math.max(mPhoneScaledBitmap.getHeight(), mSearchScaledBitmap.getHeight());
        }

        setMeasuredDimension(width, height);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        //绘制流程：
        //1、绘制手机图片
        //2、动态绘制一层LinearGradient，与原图混合，配合动画实现扫描效果
        //3、动态绘制放大镜

        //因为要使用Xfermode混合像素，因此这里先另起一个临时画布绘制。
        mTempCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mTempCanvas.drawBitmap(mPhoneScaledBitmap, 0, 0, mPhonePaint);
        if (mShaderAnimRunnable != null) {
            mShaderAnimRunnable.drawShader(mTempCanvas);
        }

        if (mCurScanState == SCAN_STATE_COMPLETE) {
            if (mCompleteBitmap != null) {
                canvas.drawBitmap(mCompleteBitmap,
                        getWidth() / 2 - mCompleteBitmap.getWidth() / 2,
                        getHeight() / 2 - mCompleteBitmap.getHeight() / 2, mPhonePaint);
            }
        } else {
            if (mSearchAnimRunnable != null) {
                mSearchAnimRunnable.drawSearch(canvas);
            }
        }

        canvas.drawBitmap(mCanvasBitmap, 0, 0, mPhonePaint);
    }

    public int getScanState() {
        return mCurScanState;
    }

    /**
     * 开始扫描
     */
    public void start() {
        if (mCurScanState == SCAN_STATE_NORMAL &&
                mShaderAnimRunnable != null &&
                mSearchAnimRunnable != null) {
            mCurScanState = SCAN_STATE_SCANNING;
            mShaderAnimRunnable.start();
            mSearchAnimRunnable.start();
        }
    }

    /**
     * 停止扫描
     */
    public void stop() {
        if (mCurScanState == SCAN_STATE_SCANNING &&
                mShaderAnimRunnable != null &&
                mSearchAnimRunnable != null) {
            mCurScanState = SCAN_STATE_NORMAL;
            mShaderAnimRunnable.stop();
            mSearchAnimRunnable.stop();
        }
    }

    /**
     * 扫描完成
     */
    public void complete() {
        if (mCurScanState == SCAN_STATE_SCANNING &&
                mShaderAnimRunnable != null &&
                mSearchAnimRunnable != null) {
            mCurScanState = SCAN_STATE_COMPLETE;
            mShaderAnimRunnable.stop();
            mSearchAnimRunnable.stop();
        }
    }

    private class ShaderAnimRunnable implements Runnable {

        private static final int INVERSE_DELAY_TIME = 2000;
        private static final int SHADER_HEIGHT = 100;
        /**
         * shader位置改变的间隔距离，px
         */
        private static final int SHADER_CHANGE_INTERVAL = 5;

        private Point mShaderStart;
        private Point mShaderEnd;
        private int mShaderWidth;

        private LinearGradient mDownLinearGradient;
        private LinearGradient mUpLinearGradient;

        private Rect mRect;

        private Paint mShaderPaint;

        /**
         * 实时记录Shader的位置
         */
        private Point mCurrentShaderStart;

        /**
         * 标识是否向下扫描
         */
        private boolean isDownScan;

        private boolean isRunning = false;

        private ShaderAnimRunnable(Point start, Point end,
                                   int sideColor, int scanColor) {
            this.mShaderStart = start;
            this.mShaderEnd = end;
            mShaderWidth = mShaderEnd.x - mShaderStart.x;
            mRect = new Rect();
            mShaderPaint = new Paint();
            mShaderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            mCurrentShaderStart = new Point(-1, -1);

            mDownLinearGradient = new LinearGradient(
                    0, 0,
                    0, SHADER_HEIGHT,
                    sideColor, scanColor, Shader.TileMode.CLAMP
            );
            mUpLinearGradient = new LinearGradient(
                    0, 0,
                    0, SHADER_HEIGHT,
                    scanColor, sideColor, Shader.TileMode.CLAMP
            );
        }

        private void start() {
            isDownScan = true;
            mCurrentShaderStart.x = mShaderStart.x;
            mCurrentShaderStart.y = mShaderStart.y - SHADER_HEIGHT;
            isRunning = true;
            postAnim();
        }

        private void stop() {
            mCurrentShaderStart.x = -1;
            mCurrentShaderStart.y = -1;
            isRunning = false;
            removeAnim();
        }


        private void drawShader(Canvas canvas) {
            if (isCurrentShaderPositionValid()) {
                canvas.save();
                canvas.translate(mCurrentShaderStart.x, mCurrentShaderStart.y);
                mRect.set(0,
                        Math.max(mShaderStart.y - mCurrentShaderStart.y, 0),
                        mShaderWidth,
                        mShaderEnd.y - mCurrentShaderStart.y > SHADER_HEIGHT ?
                                SHADER_HEIGHT : mShaderEnd.y - mCurrentShaderStart.y);
                canvas.clipRect(mRect);
                if (isDownScan) {
                    mShaderPaint.setShader(mDownLinearGradient);
                } else {
                    mShaderPaint.setShader(mUpLinearGradient);
                }
                canvas.drawRect(mRect, mShaderPaint);
                mShaderPaint.setShader(null);
                canvas.restore();
            }
        }

        @Override
        public void run() {
            boolean posted = false;
            if (isDownScan) {
                mCurrentShaderStart.y += SHADER_CHANGE_INTERVAL;
                if (mCurrentShaderStart.y > mShaderEnd.y) {
                    //延时反转
                    isDownScan = false;
                    posted = true;
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            postAnim();
                        }
                    }, INVERSE_DELAY_TIME);
                }
            } else {
                mCurrentShaderStart.y -= SHADER_CHANGE_INTERVAL;
                if (mCurrentShaderStart.y < mShaderStart.y - SHADER_HEIGHT) {
                    //延时反转
                    isDownScan = true;
                    posted = true;
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            postAnim();
                        }
                    }, INVERSE_DELAY_TIME / 8);
                }
            }

            if (!posted) {
                postAnim();
            }
        }

        private void postAnim() {
            if (isRunning) {
                ViewCompat.postOnAnimation(ScanMusicView.this, this);
                ScanMusicView.this.invalidate();
            }
        }

        private void removeAnim() {
            ScanMusicView.this.removeCallbacks(this);
            ScanMusicView.this.invalidate();
        }

        private boolean isCurrentShaderPositionValid() {
            return mCurrentShaderStart.x != -1 && mCurrentShaderStart.y != -1;
        }

    }

    private class SearchAnimRunnable implements Runnable {
        private Paint mSearchPaint;

        /**
         * 放大镜左上角的位置
         */
        private Point mCurrentSearchPos;

        /**
         * 放大镜左上角顶点运动轨迹（圆）的半径。
         * 会决定放大镜扫描时运动的幅度
         */
        private final int mRadius = 15;
        /**
         * 圆心
         */
        private Point mCircleCenter;

        /**
         * 总的偏移角度，逆时针为正
         */
        private int mTotalDegree;
        private static final int DEGREE_INTERVAL = 3;
        private int startX;
        private int startY;

        SearchAnimRunnable() {
            mSearchPaint = new Paint();
            mSearchPaint.setAntiAlias(true);
            mCurrentSearchPos = new Point();
            resetSearchPosition();
            int sideLength = (int) (Math.sqrt(2) / 2 * mRadius);
            mCircleCenter = new Point(mCurrentSearchPos.x + sideLength,
                    mCurrentSearchPos.y + sideLength);
        }

        private void start() {
            mTotalDegree = 0;
            postAnim();
        }

        private void stop() {
            resetSearchPosition();
            removeAnim();
        }

        private void resetSearchPosition() {
            if (mSearchScaledBitmap != null && mPhoneScaledBitmap != null) {
                final int phoneWidth = mPhoneScaledBitmap.getWidth();
                final int phoneHeight = mPhoneScaledBitmap.getHeight();
                startX = phoneWidth / 2 - mSearchScaledBitmap.getWidth() / 2;
                mCurrentSearchPos.x = startX;
                startY = phoneHeight / 2 - mSearchScaledBitmap.getHeight() / 2;
                mCurrentSearchPos.y = startY;
            }
        }


        private void drawSearch(Canvas canvas) {
            if (mSearchScaledBitmap != null) {
                canvas.drawBitmap(mSearchScaledBitmap,
                        mCurrentSearchPos.x, mCurrentSearchPos.y,
                        mSearchPaint);
            }
        }

        @Override
        public void run() {
            if (mSearchScaledBitmap == null) {
                return;
            }

            mTotalDegree += DEGREE_INTERVAL;

            double newX = startX - mRadius * Math.sin(Math.toRadians(mTotalDegree));
            double newY = startY + (mRadius - mRadius * Math.cos(Math.toRadians(mTotalDegree)));

            mCurrentSearchPos.x = (int) newX;
            mCurrentSearchPos.y = (int) newY;

            postAnim();
        }

        private void postAnim() {
            ViewCompat.postOnAnimation(ScanMusicView.this, this);
            ScanMusicView.this.invalidate();
        }

        private void removeAnim() {
            ScanMusicView.this.removeCallbacks(this);
            ScanMusicView.this.invalidate();
        }
    }

}
