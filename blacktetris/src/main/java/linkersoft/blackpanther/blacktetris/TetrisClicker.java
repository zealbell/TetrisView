package linkersoft.blackpanther.blacktetris;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.ArrayList;

import linkersoft.blackpanther.blacktetris.Shape.Polygon;
import linkersoft.blackpanther.blacktetris.utils.util;

public class TetrisClicker extends FrameLayout {

    public TetrisClicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        in(context, attrs);
    }
    public TetrisClicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        in(context, attrs);
    }

    protected static int GlobalDimension;
    private float width,height;
    private void in(@NonNull Context context, @Nullable AttributeSet attrs){
        TypedArray trissa =context.getTheme().obtainStyledAttributes(attrs, R.styleable.TetrisClicker, 0, 0);
        try{

            String wWidth=trissa.getString(R.styleable.TetrisClicker_percentWidth);
            String hHeight=trissa.getString(R.styleable.TetrisClicker_percentHeight);
            String gGlobalWidth=trissa.getString(R.styleable.TetrisClicker_GlobalDimension);

            if(gGlobalWidth==null)gGlobalWidth= util.getScreenWidth(context)+"px";
            if(gGlobalWidth.contains("dp")) GlobalDimension = util.dp2px(Float.parseFloat(gGlobalWidth.split("dp")[0]),context);
            else if(gGlobalWidth.contains("px")) GlobalDimension = Integer.parseInt(gGlobalWidth.split("px")[0]);

            wWidth=(wWidth!=null)?wWidth.split("%")[0]:"0";
            hHeight=(hHeight!=null)?hHeight.split("%")[0]:"0";

            width= Float.parseFloat(wWidth);
            height= Float.parseFloat(hHeight);
        }finally{trissa.recycle();}
    }

    private int clickX,clickY;
    private ArrayList<TetrisView> TetrisLis;
    private View clickBlocker;
    private ViewGroup.LayoutParams vLP;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            ViewGroup.LayoutParams mLp= getLayoutParams();
            mLp.width= Math.round( (width/100F)* GlobalDimension);
            mLp.height= Math.round( (height/100F)* GlobalDimension);
            setLayoutParams(mLp);
        if(clickBlocker==null){
            TetrisLis=new ArrayList<>();
            clickBlocker=new View(getContext());
            vLP=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            clickBlocker.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    clickX=(int)event.getX();
                    clickY=(int)event.getY();
                    return false;
                }
            });
            clickBlocker.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v){
                    for (int i = 0; i < TetrisLis.size(); i++) {
                        TetrisView Tetris =TetrisLis.get(i);
                        Polygon absPoly=Tetris.getAbsPolygon();
                        if(absPoly.contains(clickX,clickY)){
                            Tetris.performClick();
                            break;
                        }
                    }
                }
            });
            getAllTetris(this);
            addView(clickBlocker,vLP);
        }for (int i = 0; i < getChildCount(); i++){
            View child=getChildAt(i);
            if(child!=clickBlocker && !(child instanceof TetrisView))throw new IllegalStateException("Can't take a Bastard for a Child i.e. Child must be an instanceof BlackTetris");
        }super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    private void getAllTetris(ViewGroup vG){
        View child;
        int childCount=vG.getChildCount();
        for (int i = 0; i < childCount; i++) {
            child=vG.getChildAt(i);
            if(child instanceof TetrisView)TetrisLis.add((TetrisView)child);
        }
    }




}
