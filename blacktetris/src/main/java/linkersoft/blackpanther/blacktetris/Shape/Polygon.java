package linkersoft.blackpanther.blacktetris.Shape;


import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by LiNKeR on 6/6/2018.
 */

public class Polygon {

    private Point centroid;
    public int xXx[], yYy[];
    ArrayList<Point> points;
    private int minY,maxY,minX,maxX;

    public Polygon(int size) {
        xXx=new int[size];yYy=new int[size];
    }
    public Polygon(int xX[], int yY[]) {
        xXx=xX;yYy=yY;
        initialize();
    }
    public Polygon(int xXyY[]) {
         xXx=new int[xXyY.length/2];
         yYy=new int[xXyY.length/2];
        for (int i = 0,j=0,k=1; i < xXyY.length/2; i++,j+=2,k+=2){
            xXx[i]=xXyY[j];
            yYy[i]=xXyY[k];
        }initialize();
    }
    public Polygon(ArrayList<Point> xXyY){
        xXx=new int[xXyY.size()];
        yYy=new int[xXyY.size()];
        for (int i = 0; i < xXyY.size(); i++){
            Point vertex=xXyY.get(i);
            xXx[i]=vertex.x;
            yYy[i]=vertex.y;
        }initialize();
    }
    public void initialize(){
        minX = Integer.MAX_VALUE;
        maxX =-Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        maxY =-Integer.MAX_VALUE;

        for (int i = 0; i < xXx.length; i++){

            if(xXx[i]> maxX) maxX =xXx[i];
            if(xXx[i]< minX) minX =xXx[i];
            if(yYy[i]> maxY) maxY =yYy[i];
            if(yYy[i]< minY) minY =yYy[i];
        }
    }

    public boolean contains(int x,int y) {
        boolean contains = false;
        for ( int xi=0, yj=xXx.length - 1; xi<xXx.length; yj=xi++)
            if((yYy[xi]>y)!=(yYy[yj]>y)&&(x<(xXx[yj]-xXx[xi])*(y-yYy[xi])/(yYy[yj]-yYy[xi])+xXx[xi]))contains = !contains;
        return contains;
    }
    public boolean contains(Polygon polygon){
        for (int i = 0; i < polygon.size(); i++)
        if(!contains(polygon.xXx[i],polygon.yYy[i]))return false;
        return true;
    }
    public int size(){
        return xXx.length;
    }
    public Point getCentroid(){
        if(centroid!=null)return new Point(centroid);
        int sumX=0,sumY=0;
        if(points==null)getPoints();
        for (int i = 0; i < points.size(); i++) {
            centroid=points.get(i);
            sumX+=centroid.x;
            sumY+=centroid.y;
        }centroid=new Point(sumX/points.size(),sumY/points.size());
        return new Point(centroid);
    }
    public ArrayList<Point> getPoints(){
        if(points !=null)return points;
        points =new ArrayList<>();
        for(int yj = minY; yj < maxY +1; yj++) {
            for(int xi = minX; xi < maxX +1; xi++)
            if(contains(xi,yj)) points.add(new Point(xi,yj));
        }return points;
    }
    public Polygon scale(float scale){
        if(scale!=0){
            Point centroid=getCentroid();
            int[] xXx=new int[this.xXx.length];
            int[] yYy=new int[this.yYy.length];
            for (int i = 0; i < xXx.length; i++) {
                Line vertex2centroid=new Line(this.xXx[i],this.yYy[i],centroid.x,centroid.y);
                Line scaled=vertex2centroid.scale(scale,Line.scaleViaEND);
                xXx[i]=scaled.x1;
                yYy[i]=scaled.y1;
            }return new Polygon(xXx,yYy);
        }else return new Polygon(xXx,yYy);
    }
    public void setCentroid(int x,int y){
          Point centroid=getCentroid();
        if(x!=centroid.x||y!=centroid.y){
            int xdff=x-centroid.x;
            int ydff=y-centroid.y;
            for (int i = 0; i < size(); i++) {
                xXx[i]+=xdff;
                yYy[i]+=ydff;
            }this.centroid.x+=xdff;
             this.centroid.y+=ydff;
        }
    }
    public void setCentroid(Point centroid){
        setCentroid(centroid.x,centroid.y);
    }
    public Path getPath(){
        Path path=new Path();
        path.moveTo(xXx[0],yYy[0]);
        for (int i = 1; i < size(); i++)path.lineTo(xXx[i],yYy[i]);
        path.lineTo(xXx[0],yYy[0]);
        return path;
    }
    public Polygon rotate(int degrees, int pivotX, int pivotY){
        int rXX[]=new int[size()],rYY[]=new int[size()];
        for (int i = 0; i < size(); i++){
            Line vertex2pivot=new Line(xXx[i],yYy[i],pivotX,pivotY);
            Line rotated=vertex2pivot.rotate(degrees,true,pivotX,pivotY);
            rXX[i]=rotated.x1;
            rYY[i]=rotated.y1;
        }return new Polygon(rXX,rYY);
    }
    public Polygon rotate(int degrees){
        centroid=getCentroid();
        return rotate( degrees,centroid.x,centroid.y);
    }
    public int getStride(){
        /*get the farthest distance between vertices of the polygon and its centroid*/
        centroid=getCentroid();
        int farthest=-Integer.MAX_VALUE,current;
        for (int i = 0; i <size() ; i++){
            current=(int)new Line(xXx[i],yYy[i],centroid.x,centroid.y).getLength();
            if(current>farthest)farthest=current;
        }return farthest;
    }
    public Polygon insert(int x,int y){
       return insert(new Point(x,y));
    }
    public Polygon insert(Point insertion){
        ArrayList<Point> insertions=new ArrayList<>();
        insertions.add(insertion);
        return insert(insertions);
    }
    public Polygon insert(ArrayList<Point> insertions){
        ArrayList<Point> vertices=new ArrayList<>();
        for (int i = 0; i < size(); i++)vertices.add(new Point(xXx[i],yYy[i]));
        Point centroid=getCentroid();
        Point insertion,closest=null;
        for (int k = 0; k < insertions.size(); k++){
            insertion=insertions.get(k);
            int closestIndx=0;
            int smallest= Integer.MAX_VALUE,current;/* the closest-vertex to the insertion is the point@ which the insertion exist */
            for (int i = 0; i < size(); i++) {
                current=(int)new Line(xXx[i],yYy[i],centroid.x,centroid.y).getLength();
                if(current<smallest){
                    smallest=current;
                    closest=new Point(xXx[i],yYy[i]);
                    closestIndx=i;
                }
            }
            boolean infront=new Line(closest.x,closest.y,centroid.x,centroid.y).getOrientation()>new Line(insertion.x,insertion.y,centroid.x,centroid.y).getOrientation();
            if(infront){
                int insertIndx=closestIndx+1;
                insertIndx=insertIndx==size()?0:insertIndx;
                vertices.add(insertIndx,insertion);
            }else vertices.add(closestIndx,insertion);
        }return new Polygon(vertices);
    }
    public Polygon Spore(int vertices){
        /*this method adds more vertices/points 2 d already existing polygon to give a new one*/
        /*sporing is done with a vertical-line(stride in length) rotating from 0~vertices*turn(in degrees) */
        /*sporrer => name of the vertical line*/


        int division=vertices/2;
        int turn=360/division;
        int angle=0;
        int vertcount=0;
        int stride=getStride();
        Line sporrer=new Line(centroid.x,centroid.y-stride,centroid.x,centroid.y+stride);
        ArrayList<Point> insertions=new ArrayList<>();

        for (;;) {
            Line rotorspore=sporrer.rotate(angle+=turn);
            ArrayList<Point> intersections=rotorspore.intersectPolygon(this,2);
            Point first=intersections.get(0);
            insertions.add(first);
            vertcount++;
            if(vertcount==vertices)break;
            Point second=intersections.get(1);
            insertions.add(second);
            vertcount++;
            if(vertcount==vertices)break;
            if(angle>=360)break;
        }return insert(insertions);
    }


    @Override
    public String toString() {
        String polygon=" (~([ ";for (int i = 0; i < size(); i++) polygon+="{"+xXx[i]+","+yYy[i]+"}";polygon+=" ])~)";
        return super.toString()+polygon;
    }
}


