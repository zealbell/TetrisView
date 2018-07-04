package linkersoft.blackpanther.blacktetris.Shape;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by LiNKeR on 6/7/2018.
 */

public class Line{


    /*
                   *                          |
                   *                          |
                   *              3rdQUAD     |     4thQUAD
                   *                          |
                   * 0,360(turn)--------------|-----------------
                   *                          |0,0
                   *              2ndQUAD     |     1stQUAD
                   *                          |
                   *                          |         xMx,yMx
                   * */
    private static final int
            Quadrant1=0x1,
            Quadrant2=0x2,
            Quadrant3=0x3,
            Quadrant4=0x4;
    private static final int scaleViaSTART=0x0,
                            scaleViaMID=0x1,
                            scaleViaEND=0x2;
    private int x1,x2,y1,y2,midX,midY;
    private int dx,dy,points[][];
    private boolean previously;

    private Line(int x1,int y1,int x2,int y2){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;

        midX= Math.round((x1 + x2) / 2.0f);
        midY = Math.round((y1 + y2) / 2.0f);
    }
    Line(int x1, int y1, float dst,float angle) {
        this.x1=x1;
        this.y1=y1;
        x2= Math.round(dst*(float) Math.cos(Math.toRadians(angle))) + x1;
        y2= Math.round(dst*(float) Math.sin(Math.toRadians(angle))) + y1;
        midX= Math.round((x1 + x2) / 2.0f);
        midY = Math.round((y1 + y2) / 2.0f);
    }

    public int[][] getPoints(){
        if(points !=null)return points;
        float m= getSlope();
        int b,ady= Math.abs(dy),adx= Math.abs(dx);

        if(adx>=ady){
            points = new int[adx][2];
            b= Math.round(y1-(m*x1));
            if(dx>=0)
                for(int i=0; i<dx; i++){
                    points[i][0]=x1+i;//x
                    points[i][1]= Math.round((m* points[i][0])+b);
                }
            else for(int i=0; i<-dx; i++){
                points[i][0]=x1-i;//x
                points[i][1]= Math.round((m* points[i][0])+b);
            }
        }else{
            points = new int[ady][2];
            if(adx==0){
                if(dy>=0) for(int i=0; i<dy; i++){
                    points[i][1]=y1+i;//x
                    points[i][0]=x1;//or x2
                }
                else for(int i=0; i<-dy; i++){
                    points[i][1]=y1-i;//x
                    points[i][0]=x1;//or x2
                }
            }else{
                b= Math.round(y1-(m*x1));
                if(dy>=0)
                    for(int i=0; i<dy; i++){
                        points[i][1]=y1+i;//x
                        points[i][0]= Math.round((points[i][1]-b)/m);
                    }
                else for(int i=0; i<-dy; i++){
                    points[i][1]=y1-i;//x
                    points[i][0]= Math.round((points[i][1]-b)/m);
                }
            }
        }return points;
    }
    public ArrayList<Point> intersectPolygon(Polygon polygon, int atleast) {
        if(points==null)getPoints();
        ArrayList<Point> intersections=new ArrayList<>();
        for (int j = 0; j < points.length; j++) {
            int x = points[j][0];
            int y = points[j][1];
            if (!previously && (previously =polygon.contains(x, y)))intersections.add(new Point(x, y));
            else if(previously && !(previously =polygon.contains(x, y)))intersections.add(new Point(x, y));
            if(intersections.size()==atleast)break;
        }return intersections;
    }
    public float getSlope(){
        dy=y2-y1;
        dx=x2-x1;
        return dy/(float)dx;
    }
    float getLength() {
        return distBtwn2pts(x1,y1,x2,y2);
    }
    int getQuadrant(){
        if(x2>x1&&y2>y1)return Quadrant1;
        else if(x2<x1&&y2>y1)return Quadrant2;
        else if(x2<x1&&y2<y1)return Quadrant3;
        else if(x2>x1&&y2<y1)return Quadrant4;
        else return Quadrant1;
    }
    int getOrientation(){
        switch (getQuadrant()){
            case Quadrant1:
                return 180+(int)getHorzOrientation();
            case Quadrant2:
                return 180+(int)getHorzOrientation();
            case Quadrant3:
                return (int)getHorzOrientation();
            case Quadrant4:
                return 180+(int)getHorzOrientation();
        }return 0;
    }
    float getHorzOrientation(){//Angle is relative to the horizontal
        return anglBtwn2pts(x1,y1,x2,y2);
    }
    static float anglBtwn2pts(float x1,float y1,float x2,float y2){
        float angle;
        float Dy=y2-y1,Dx=x2-x1;
        angle= (Dx!=0)?(float) Math.toDegrees(Math.atan(Dy/Dx)):((Dy<0)?-90:90);
        return ( y2>y1 && x1>x2)?angle+180:angle;
    }
    static float distBtwn2pts(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    Line rotate(int degrees, boolean usesPivot, int pivotX, int pivotY){
        int  rx1, ry1, rx2, ry2;
        Line Rotated;
        if (!usesPivot) {
            int PivotX = Math.round((x1 + x2) / 2.0f), PivotY = Math.round((y1 + y2) / 2.0f);
            rx1 = (int) Math.round(PivotX + ((x1 - PivotX) * Math.cos(Math.toRadians(degrees))) - ((y1 - PivotY) * Math.sin(Math.toRadians(degrees))));
            ry1 = (int) Math.round(PivotY + ((x1 - PivotX) * Math.sin(Math.toRadians(degrees))) + ((y1 - PivotY) * Math.cos(Math.toRadians(degrees))));

            rx2 = (int) Math.round(PivotX + ((x2 - PivotX) * Math.cos(Math.toRadians(degrees))) - ((y2 - PivotY) * Math.sin(Math.toRadians(degrees))));
            ry2 = (int) Math.round(PivotY + ((x2 - PivotX) * Math.sin(Math.toRadians(degrees))) + ((y2 - PivotY) * Math.cos(Math.toRadians(degrees))));
        } else {
            rx1 = (int) Math.round(pivotX + ((x1 - pivotX) * Math.cos(Math.toRadians(degrees))) - ((y1 - pivotY) * Math.sin(Math.toRadians(degrees))));
            ry1 = (int) Math.round(pivotY + ((x1 - pivotX) * Math.sin(Math.toRadians(degrees))) + ((y1 - pivotY) * Math.cos(Math.toRadians(degrees))));

            rx2 = (int) Math.round(pivotX + ((x2 - pivotX) * Math.cos(Math.toRadians(degrees))) - ((y2 - pivotY) * Math.sin(Math.toRadians(degrees))));
            ry2 = (int) Math.round(pivotY + ((x2 - pivotX) * Math.sin(Math.toRadians(degrees))) + ((y2 - pivotY) * Math.cos(Math.toRadians(degrees))));
        }Rotated=new Line(rx1,ry1,rx2,ry2);
        return Rotated;
    }
    Line rotate(int degrees){
        return rotate(degrees,true,midX,midY);
    }
    Line swap(){
        int xx1=x1,yy1=y1,xx2=x2,yy2=y2;
        x1=xx2;
        y1=yy2;
        x2=xx1;
        y2=yy1;
        return this;
    }
    Line scale(float scale,int via){
        float dst=scale*getLength();
        float orientation=getHorzOrientation();

        switch (via){
            case scaleViaSTART:/*preserves the start*/
            switch (getQuadrant()){
                case Quadrant1: return new Line(x1,y1,-dst,orientation);
                case Quadrant2: return new Line(x1,y1,-dst,orientation);
                case Quadrant3: return new Line(x1,y1,dst,orientation);
                case Quadrant4: return new Line(x1,y1,-dst,orientation);
            }
            case scaleViaMID:/*preserves the middle*/
                float half_dst=dst/2;
                Line end=null,start=null;
                switch (getQuadrant()){
                    case Quadrant1:
                        end=new Line(midX,midY,-half_dst,orientation);
                        start=new Line(midX,midY,half_dst,orientation);
                        break;
                    case Quadrant2:
                        end=new Line(midX,midY,-half_dst,orientation);
                        start=new Line(midX,midY,half_dst,orientation);
                        break;
                    case Quadrant3:
                        end=new Line(midX,midY,half_dst,orientation);
                        start=new Line(midX,midY,-half_dst,orientation);
                        break;
                    case Quadrant4:
                        end=new Line(midX,midY,-half_dst,orientation);
                        start=new Line(midX,midY,half_dst,orientation);
                        break;
                }return new Line(start.x2,start.y2,end.x2,end.y2);
            case scaleViaEND:/*preserves the end*/
                switch (getQuadrant()){
                    case Quadrant1: return new Line(x2,y2,-dst,orientation).swap();
                    case Quadrant2: return new Line(x2,y2,-dst,orientation).swap();
                    case Quadrant3: return new Line(x2,y2,dst,orientation).swap();
                    case Quadrant4: return new Line(x2,y2,-dst,orientation).swap();
                }
        }return null;
    }


}