package linkersoft.blackpanther.blacktetris.Shape;





public class Polygon {


    public int xXx[], yYy[];
    private int minY,maxY,minX,maxX;


    public Polygon(int xX[], int yY[]) {
        xXx=xX;yYy=yY;
        initialize();
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
    public int size(){
        return xXx.length;
    }


    @Override
    public String toString() {
        String polygon=" (~([ ";for (int i = 0; i < size(); i++) polygon+="{"+xXx[i]+","+yYy[i]+"}";polygon+=" ])~)";
        return "@LiNKeR(>_<)~"+super.toString()+polygon;
    }
}


