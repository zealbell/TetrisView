package linkersoft.blackpanther.blacktetris;

import android.graphics.Path;

import linkersoft.blackpanther.blacktetris.Shape.Polygon;

public class  Panther{


    public Panther(int noOfNodes,int noOfControls){
        //never forget to call Freese(); later if using this constructor
        Nodes=new int[noOfNodes][2];
        Controls=new int[noOfControls][2];
        this.noOfNodes=noOfNodes;
        this.noOfControls=noOfControls;
    }
    public void Freese(){
         joinNodeControls();
    }
    private void joinNodeControls(){
        path=new Path();
        int cntrlAIndx=0,cntrlBIndx=1;
        path.moveTo(Nodes[0][X], Nodes[0][Y]);
        for (int nodeIndx = 0; nodeIndx < Nodes.length; nodeIndx++){
            if (nodeIndx != Nodes.length - 1) {
                bezierThrough(
                        Controls[cntrlAIndx][X], Controls[cntrlAIndx][Y],
                        Controls[cntrlBIndx][X], Controls[cntrlBIndx][Y],
                        Nodes[nodeIndx + 1][X], Nodes[nodeIndx + 1][Y]);
                cntrlBIndx+=2;
                cntrlAIndx+=2;
            }else{
                bezierThrough(
                        Controls[cntrlAIndx][X], Controls[cntrlAIndx][Y],
                        Controls[cntrlBIndx][X], Controls[cntrlBIndx][Y],
                        Nodes[0][X], Nodes[0][Y]);
            }
        }path.close();
    }
    public Polygon getNodePolygon(){
        if(NodeCage!=null)return NodeCage;
        int xXx[]=new int[Nodes.length],yYy[]=new int[Nodes.length];
        for (int i = 0; i < Nodes.length; i++) {
            xXx[i]=Nodes[i][X];
            yYy[i]=Nodes[i][Y];
        }NodeCage=new Polygon(xXx,yYy);
         return NodeCage;
    }
    private void bezierThrough(int cntrl_Ax, int cntrl_Ay, int cntr1_Bx, int cntr1_By, int node_Bx, int node_By){
        path.cubicTo( cntrl_Ax,cntrl_Ay,  cntr1_Bx,cntr1_By,  node_Bx,node_By);
    }

    public Path path;
    private static final int X=0,Y=1;
    public int noOfNodes,noOfControls;
    public int[][] Nodes;
    public int[][] Controls;
    private Polygon NodeCage;
}



