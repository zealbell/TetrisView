package linkersoft.blackpanther.blacktetris;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.io.IOException;
import java.util.ArrayList;
import linkersoft.blackpanther.blacktetris.Shape.Polygon;
import linkersoft.blackpanther.blacktetris.utils.util;


public class TetrisView extends FrameLayout {

    public TetrisView(Context context, @Nullable AttributeSet attrs) throws IOException {
        super(context, attrs);
        in(context, attrs);
    }
    public TetrisView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) throws IOException {
        super(context, attrs, defStyleAttr);
        in(context, attrs);
    }


    private Context context;
    private Panther blackPanther;
    private int inX,inY, GlobalBlockSize;
    private float mLeft,mTop,mRight,mBottom;
    private boolean distort,dp,percent;
    private Polygon absolute;
    private View OnlyChild;


    private void in(final Context context, @Nullable AttributeSet attrs)throws IOException {

        TypedArray tetrissa =context.getTheme().obtainStyledAttributes(attrs, R.styleable.TetrisView, 0, 0);
        try{ setTetris(tetrissa.getString(R.styleable.TetrisView_percentMarginLeft),
                       tetrissa.getString(R.styleable.TetrisView_percentMarginTop),
                       tetrissa.getBoolean(R.styleable.TetrisView_paddDistortion,false),
                       tetrissa.getString(R.styleable.TetrisView_tetris),
                       tetrissa.getString(R.styleable.TetrisView_blocksize),
                       tetrissa.getString(R.styleable.TetrisView_radius),context);
        }finally{tetrissa.recycle();}


    }
    private void setBlackPanther(String tetris, int blocksize, int radius, Context context){
        int trX,trY,tlX,tlY,brX,brY=0,blX,blY=0,lengthUnit,elevationUnit,height=0,padTop=0,padRight=0,padBottom=0,padLeft=0;
        ArrayList<ShapeTetris> TetrisList=new ArrayList<>();
        String shapeTetrises[]=tetris.split("~");

        for (int i = 0; i < shapeTetrises.length; i++){
            String tetrisType= util.StringIO.getStringsInBetween(shapeTetrises[i],"\\[","\\]",false,false);
            String length_elevation[]=shapeTetrises[i].split("\\["+tetrisType+"\\]")[1].split("#");
            lengthUnit= Integer.parseInt(length_elevation[0]);
            elevationUnit= Integer.parseInt(length_elevation[1]);
            ShapeTetris shapytrx=new ShapeTetris();

            if(tetrisType.contains(":")){
                String tetrisPads[]=tetrisType.split(":")[1].split(",");
                padLeft= Math.round(dp? util.dp2px(Float.parseFloat(tetrisPads[0]),context): Float.parseFloat(tetrisPads[0]));
                padTop= Math.round(dp? util.dp2px(Float.parseFloat(tetrisPads[1]),context): Float.parseFloat(tetrisPads[1]));
                padRight= Math.round(dp? util.dp2px(Float.parseFloat(tetrisPads[2]),context): Float.parseFloat(tetrisPads[2]));
                padBottom= Math.round(dp? util.dp2px(Float.parseFloat(tetrisPads[3]),context): Float.parseFloat(tetrisPads[3]));
            }

            if(tetrisType.contains("horz-tetris")){
                tlX=inX;
                tlY=inY;

                trX=inX+(lengthUnit*blocksize);
                trY=inY;

                brX=inX+(lengthUnit*blocksize);
                brY=inY+blocksize;

                blX=inX;
                blY=inY+blocksize;

                tlY=tlY+(elevationUnit*blocksize);
                trY=trY+(elevationUnit*blocksize);
                brY=brY+(elevationUnit*blocksize);
                blY=blY+(elevationUnit*blocksize);

                tlX+=padLeft;    tlY+=padTop;
                blX+=padLeft;    trY+=padTop;
                trX-=padRight;   blY-=padBottom;
                brX-=padRight;   brY-=padBottom;

                shapytrx.tetrixX.add(tlX);    //0
                shapytrx.tetrixY.add(tlY);    //0

                shapytrx.tetrixX.add(trX);    //1
                shapytrx.tetrixY.add(trY);    //1

                shapytrx.tetrixX.add(brX);    //2
                shapytrx.tetrixY.add(brY);    //2

                shapytrx.tetrixX.add(blX);    //3
                shapytrx.tetrixY.add(blY);    //3
                if(!distort)inX+=(lengthUnit*blocksize);
                else inX+=(lengthUnit*blocksize)+padLeft-padRight;
            }else if(tetrisType.contains("vert-tetris")){
                tlX=inX;
                tlY=inY;

                trX=inX+blocksize;
                trY=inY;

                brX=inX+blocksize;
                brY=inY+(lengthUnit*blocksize);

                blX=inX;
                blY=inY+(lengthUnit*blocksize);

                tlY=tlY+(elevationUnit*blocksize);
                trY=trY+(elevationUnit*blocksize);
                brY=brY+(elevationUnit*blocksize);
                blY=blY+(elevationUnit*blocksize);

                tlX+=padLeft;    tlY+=padTop;
                blX+=padLeft;    trY+=padTop;
                trX-=padRight;   blY-=padBottom;
                brX-=padRight;   brY-=padBottom;

                shapytrx.tetrixX.add(tlX);    //0
                shapytrx.tetrixY.add(tlY);    //0

                shapytrx.tetrixX.add(trX);    //1
                shapytrx.tetrixY.add(trY);    //1

                shapytrx.tetrixX.add(brX);    //2
                shapytrx.tetrixY.add(brY);    //2

                shapytrx.tetrixX.add(blX);    //3
                shapytrx.tetrixY.add(blY);    //3
                if(!distort)inX+=blocksize;
                else inX+=blocksize+padLeft-padRight;
            }TetrisList.add(shapytrx);
            if(blY>height)height=brY;
        }

        inY=height;
        CurvifyShapes(WeldShapes(TetrisList),radius);
    }
    private void setTetris(String pMarginLeft, String pMarginTop, boolean padd_distortion, String tetris, String blocksizes, String tradius, Context context){
        this.context=context;
        inX=0;
        inY=0;
        absolute=null;
        SHURI=null;
        distort=padd_distortion;
        percent=false;
        int blocksize=0;
        int radius=0;

        if(tradius.contains("dp")){
            radius= util.dp2px(Integer.parseInt(tradius.split("dp")[0]),context);
            dp=true;
        }else if(tradius.contains("px"))radius= Integer.parseInt(tradius.split("px")[0]);

        if(blocksizes.contains("dp"))blocksize= util.dp2px(Integer.parseInt(blocksizes.split("dp")[0]),context);
        else if(blocksizes.contains("px"))blocksize= Integer.parseInt(blocksizes.split("px")[0]);
        else if(blocksizes.contains("%")){
            GlobalBlockSize =(TetrisClicker.GlobalBlockSize !=0)?TetrisClicker.GlobalBlockSize :(int) util.getScreenWidth(context);
            blocksize= Integer.parseInt(blocksizes.split("%")[0]);
            blocksize= Math.round( (blocksize/100F)* GlobalBlockSize);

            String left=pMarginLeft;
            String top=pMarginTop;
            String right;
            String bottom;

            left=(left!=null)?left.split("%")[0]:"0";
            top=(top!=null)?top.split("%")[0]:"0";
            right="0";
            bottom="0";

            mLeft= Float.parseFloat(left);
            mTop= Float.parseFloat(top);
            mRight= Float.parseFloat(right);
            mBottom= Float.parseFloat(bottom);

            percent=true;
        }setBlackPanther(tetris,blocksize,radius,context);
    }
    public void resetTetris(String pMarginLeft, String pMarginTop, boolean paddDistortion, String tetris, String blocksizes, String tradius){
        if(context==null)throw new IllegalStateException("never call resetTetris() too early! the world could blow up!!!");
        setTetris(pMarginLeft,pMarginTop,paddDistortion,tetris,blocksizes,tradius,context);
        requestLayout();
    }
    private ArrayList<pointTetris> WeldShapes(ArrayList<ShapeTetris> TetrisLis){
        int X=0,Y=1;
        ShapeTetris shapytrx;
        int[][] topTrx,bottomTrx;

        HashArrayList Tops=new HashArrayList();
        HashArrayList Bottoms=new HashArrayList();

        for (int i = 0; i < TetrisLis.size(); i++){
            shapytrx=TetrisLis.get(i);

            topTrx=shapytrx.getTop();
            Tops.add(new Integer[]{topTrx[X][0],topTrx[Y][0]});
            Tops.add(new Integer[]{topTrx[X][1],topTrx[Y][1]});

            bottomTrx=shapytrx.getBottom();
            Bottoms.add(new Integer[]{bottomTrx[X][0],bottomTrx[Y][0]} );
            Bottoms.add(new Integer[]{bottomTrx[X][1],bottomTrx[Y][1]} );
        }

        Integer[] qnK;
        ArrayList<pointTetris> Shape=new ArrayList<>();
        for (int i = 0; i < Tops.size(); i++){
            qnK=Tops.get(i);
            Shape.add(new pointTetris(qnK[0],qnK[1]));
        }
        for (int i = Bottoms.size()-1; i >-1 ; i--){
            qnK=Bottoms.get(i);
            Shape.add(new pointTetris(qnK[0],qnK[1]));
        }return Shape;
    }
    private void sieveDuplicates(ArrayList<pointTetris> shape){
        pointTetris filter,suspect;
        for (int i = 0; i < shape.size(); i++){
            filter=shape.get(i);
            for (int k = 0; k < shape.size(); k++){
                if(k!=i){
                    suspect=shape.get(k);
                    if(filter.x==suspect.x && filter.y==suspect.y)shape.remove(suspect);
                }
            }
        }

    }
    private void CurvifyShapes(ArrayList<pointTetris> shape, int radius){
        sieveDuplicates(shape);

        int X=0,Y=1;
        pointTetris oneT,twoT,threeT;
        int sSz=shape.size(),one,two,three;
        for (int i =0; i < sSz;i++ ){
            one=i;two=i+1;three=i+2;
            two=two>(sSz-1)?two-sSz:two;
            three=three>(sSz-1)?three-sSz:three;

            oneT=shape.get(one);
            twoT=shape.get(two);
            threeT=shape.get(three);
            twoT.setCurveType(oneT,threeT);
        }
        int[] Curve[],curveNodes,curveControls;
        blackPanther=new Panther(sSz*2,sSz*4);
        for (int i = 0,Nndx=0,Cndx=0; i < sSz; i++){
            twoT=shape.get(i);
            Curve=twoT.getCurve(radius);

            curveNodes=Curve[0];
            curveControls=Curve[1];

            blackPanther.Nodes[Nndx][X]=curveNodes[0];blackPanther.Nodes[Nndx][Y]=curveNodes[1];Nndx++;
            blackPanther.Nodes[Nndx][X]=curveNodes[2];blackPanther.Nodes[Nndx][Y]=curveNodes[3];Nndx++;

            blackPanther.Controls[Cndx][X]=curveControls[0];blackPanther.Controls[Cndx][Y]=curveControls[1];Cndx++;
            blackPanther.Controls[Cndx][X]=curveControls[2];blackPanther.Controls[Cndx][Y]=curveControls[3];Cndx++;
            blackPanther.Controls[Cndx][X]=curveControls[4];blackPanther.Controls[Cndx][Y]=curveControls[5];Cndx++;
            blackPanther.Controls[Cndx][X]=curveControls[6];blackPanther.Controls[Cndx][Y]=curveControls[7];Cndx++;
        }blackPanther.Freese();
    }

    private class ShapeTetris {

        ArrayList<Integer> tetrixX;
        ArrayList<Integer> tetrixY;
        ShapeTetris(){
            this.tetrixX=new ArrayList<>();
            this.tetrixY=new ArrayList<>();
        }

        int[][] getBottom(){
            //l-r
            return new int[][]{
                    new int[]{tetrixX.get(3),tetrixX.get(2)},
                    new int[]{tetrixY.get(3),tetrixY.get(2)}
            };
        }
        int[][] getTop(){
            //l-r
            return new int[][]{
                    new int[]{tetrixX.get(0),tetrixX.get(1)},
                    new int[]{tetrixY.get(0),tetrixY.get(1)}
            };
        }

    }
    private class pointTetris{
        static final int tlCONCAVE_CURVE=0x1,trCONCAVE_CURVE=0x2,brCONCAVE_CURVE=0x3,blCONCAVE_CURVE=0x4;
        static final int tlCONVEX_CURVE=0x5,trCONVEX_CURVE=0x6,brCONVEX_CURVE=0x7,blCONVEX_CURVE=0x8;
        static final int DOWNWARD_VERTICAL_NO_CURVE =0x9,UPWARD_VERTICAL_NO_CURVE=0x10, RIGHTWARD_HORIZONTAL_NO_CURVE =0x11,LEFTTWARD_HORIZONTAL_NO_CURVE=0x12;
        int CURVE_TYPE;
        int x;
        int y;

        pointTetris(int x,int y){
            this.x=x;
            this.y=y;
        }
        void setCurveType(pointTetris one, pointTetris three){
                /*
                     CONVEX               CONCAVE
                                        _ _ _  _ _ _
                 *    tl  ||   tr      |  tl    tr  |
                 *    =>  ||   =>      | =>     =>  |
                 *   _ _ _||_ _ _      |            |
                 *   _ _ _  _ _ _
                 *    bl  ||  br       | bl     br  |
                 *    <=  ||  <=       | <=     <=  |
                 *        ||           |_ _ _  _ _ _|
                 *
                 */
            if(y==one.y && y>three.y && three.x>one.x){
                /*        |
                 *        |   =>
                 *   _ _ _|
                 */
                CURVE_TYPE =tlCONCAVE_CURVE;
            }else if(y>one.y && y==three.y && three.x>one.x){
               /*   |
                *   |         =>
                *   |_ _ _
                */
                CURVE_TYPE =trCONCAVE_CURVE;
            }else if(one.y==y && three.y>y && one.x>three.x){
               /*   _ _ _
                *   |
                *   |         <=
                *   |
                */
                CURVE_TYPE =brCONCAVE_CURVE;
            }else if(one.y>y && y==three.y && one.x>three.x){
               /*    _ _ _
                *        |
                *        |     <=
                *        |
                */
                CURVE_TYPE =blCONCAVE_CURVE;
            }else if(one.y>y && y==three.y && three.x>one.x){
               /*   _ _ _
                *   |
                *   |         =>
                *   |
                */
                CURVE_TYPE =tlCONVEX_CURVE;
            }else if(one.y==y && three.y>y && three.x>one.x){
               /*    _ _ _
                *        |
                *        |     =>
                *        |
                */
                CURVE_TYPE =trCONVEX_CURVE;
            }else if(y>one.y && y==three.y && one.x>three.x){
                /*        |
                 *        |   <=
                 *   _ _ _|
                 */
                CURVE_TYPE =brCONVEX_CURVE;
            }else if(one.y==y && y>three.y && one.x>three.x){
               /*   |
                *   |         <=
                *   |_ _ _
                */
                CURVE_TYPE =blCONVEX_CURVE;
            }else if(one.y==y && y==three.y){
                /* _ _ _ */
                CURVE_TYPE= (three.x>one.x)?RIGHTWARD_HORIZONTAL_NO_CURVE:LEFTTWARD_HORIZONTAL_NO_CURVE;
            }else if(one.x==x && x==three.x){
                /*  |
                *   |
                *   |
                */
                CURVE_TYPE= (three.y>one.y)?DOWNWARD_VERTICAL_NO_CURVE:UPWARD_VERTICAL_NO_CURVE;
            }
        }
        int[][] getCurve(int radius){
/*
                     CONVEX               CONCAVE
                                        _ _ _  _ _ _
                 *    tl  ||   tr      |  tl    tr  |
                 *    =>  ||   =>      | =>     =>  |
                 *   _ _ _||_ _ _      |            |
                 *   _ _ _  _ _ _
                 *    bl  ||  br       | bl     br  |
                 *    <=  ||  <=       | <=     <=  |
                 *        ||           |_ _ _  _ _ _|
                 *
                 */
            int adius=radius/8;
            switch (CURVE_TYPE){
                case tlCONCAVE_CURVE:
                    /*        |
                     *        |   =>
                     *   _ _ _|
                     */
                    return new int[][]{
                            new int[]{x-radius,y, x,y-radius},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x-radius-adius,y, x-adius,y,   x,y-adius, x,y-radius-adius}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
                case trCONCAVE_CURVE:
                    /*   |
                     *   |         =>
                     *   |_ _ _
                     */
                    return new int[][]{
                            new int[]{x,y-radius, x+radius,y},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x,y-radius-adius, x,y-adius,   x+adius,y, x+radius+adius,y}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
                case brCONCAVE_CURVE:
                    /*   _ _ _
                     *   |
                     *   |         <=
                     *   |
                     */
                    return new int[][]{
                            new int[]{x+radius,y, x,y+radius},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x+radius+adius,y, x+adius,y,   x,y+adius, x,y+radius+adius}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
                case blCONCAVE_CURVE:
                    /*    _ _ _
                     *        |
                     *        |     <=
                     *        |
                     */
                    return new int[][]{
                            new int[]{x,y+radius, x-radius,y},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x,y+radius+adius, x,y+adius,   x-adius,y, x-radius-adius,y}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };

                case tlCONVEX_CURVE:
                    /*   _ _ _
                     *   |
                     *   |         =>
                     *   |
                     */
                    return new int[][]{
                            new int[]{x,y+radius, x+radius,y},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x,y+radius+adius, x,y+adius,   x+adius,y, x+radius+adius,y}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
                case trCONVEX_CURVE:
                     /*    _ _ _
                      *        |
                      *        |     =>
                      *        |
                      */
                    return new int[][]{
                            new int[]{x-radius,y, x,y+radius},                                      //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x-radius-adius,y, x-adius,y,   x,y+adius, x,y+radius+adius}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
                case brCONVEX_CURVE:
                    /*        |
                     *        |   <=
                     *   _ _ _|
                     */
                    return new int[][]{
                            new int[]{x,y-radius, x-radius,y},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x,y-radius-adius, x,y-adius,   x-adius,y, x-radius-adius,y}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
                case blCONVEX_CURVE:
                    /*   |
                     *   |         <=
                     *   |_ _ _
                     */
                    return new int[][]{
                            new int[]{x+radius,y, x,y-radius},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x+radius+adius,y, x+adius,y,   x,y-adius, x,y-radius-adius}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
                case RIGHTWARD_HORIZONTAL_NO_CURVE:
                     /* _ _ _  => */
                    return new int[][]{
                            new int[]{x-radius,y, x+radius,y},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x-radius-adius,y, x-adius,y,   x+adius,y, x+radius+adius,y}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
                case LEFTTWARD_HORIZONTAL_NO_CURVE:
                     /* _ _ _  <= */
                    return new int[][]{
                            new int[]{x+radius,y, x-radius,y},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x+radius+adius,y, x+adius,y,   x-adius,y, x-radius-adius,y}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
                case DOWNWARD_VERTICAL_NO_CURVE:
                     /*   |
                      *   |   down
                      *   |
                      */
                    return new int[][]{
                            new int[]{x,y-radius, x,y+radius},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x,y-radius-adius, x,y-adius,   x,y+adius, x,y+radius+adius}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
                case UPWARD_VERTICAL_NO_CURVE:
                     /*   |
                      *   |   up
                      *   |
                      */
                    return new int[][]{
                            new int[]{x,y+radius, x,y-radius},                                       //2nodes  [x0,y0,x1,y1,...,xN,yN]
                            new int[]{x,y+radius+adius, x,y+adius,   x,y-adius, x,y-radius-adius}    //4controls  [x0,y0,x1,y1,...,xN,yN]
                    };
            }return null;

        }

    }
    private class HashArrayList extends ArrayList<Integer[]> {
        Integer[] prv;
        @Override
        public boolean add(Integer[] curr) {
            return !equals(curr) && super.add(curr);
        }
        boolean equals(Integer[] curr){
            if(prv==null){
                prv=curr;
                return false;
            }return prv[0]==curr[0] && prv[1]==curr[1];
        }
    }

    protected Panther getBlackPanther(){
        return blackPanther;
    }
    protected Polygon getAbsPolygon(){
        if(absolute!=null)return absolute;
        MarginLayoutParams mLP=(MarginLayoutParams)getLayoutParams();
        int extraX=mLP.leftMargin-mLP.rightMargin;
        int extraY=mLP.topMargin-mLP.rightMargin;
        Polygon polygon=getBlackPanther().getNodePolygon();
        for (int i = 0; i < polygon.size(); i++) {
            polygon.xXx[i]+=extraX;
            polygon.yYy[i]+=extraY;
        }absolute=polygon;
         return polygon;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(getChildCount()>1)throw new BastardException("Only a Child.?. Can't Afford too many Kids.");
            ViewGroup.LayoutParams vLp=getLayoutParams();
            vLp.width=inX;
            vLp.height=inY;
            setLayoutParams(vLp);
        if(percent){
            MarginLayoutParams mLp= (MarginLayoutParams)vLp;
            mLp.leftMargin= Math.round( (mLeft/100F)* GlobalBlockSize);
            mLp.topMargin= Math.round( (mTop/100F)* GlobalBlockSize);
            mLp.rightMargin= Math.round( (mRight/100F)* GlobalBlockSize);
            mLp.bottomMargin= Math.round( (mBottom/100F)* GlobalBlockSize);
            setLayoutParams(mLp);
        }setMeasuredDimension(inX,inY);

        OnlyChild=getChildAt(0);
        widthMeasureSpec= MeasureSpec.makeMeasureSpec(inX, MeasureSpec.EXACTLY);
        heightMeasureSpec= MeasureSpec.makeMeasureSpec(inY, MeasureSpec.EXACTLY);
        OnlyChild.measure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        OnlyChild.layout(0,0,inX,inY);
    }


    private Bitmap SHURI;
    private int H;
    private int W;
    private Canvas WAKANDA;


    @Override
    protected void dispatchDraw(Canvas canvas){
        if((W = getWidth())==0||(H =getHeight())==0)return;
        if(SHURI ==null){
            SHURI = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
            WAKANDA =new Canvas(SHURI);
        }super.dispatchDraw(WAKANDA);
        canvas.drawBitmap(KillmongerTetris(SHURI),0,0,null);
    }
    Bitmap KillmongerTetris(Bitmap bmaP) {
        Bitmap ObMap = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ObMap);
        Rect rect = new Rect(0, 0, W, H);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawPath(blackPanther.path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bmaP, rect, rect, paint);
        return ObMap;
    }

    public static class BastardException extends IllegalMonitorStateException{
        BastardException(String errorInfo){
            super(errorInfo+": Tetris can't afford extra-Bastards as kids ");
        }
    }

    @Override
    public String toString() {
        return "@LiNKeR(>_<)~"+super.toString();
    }
}
