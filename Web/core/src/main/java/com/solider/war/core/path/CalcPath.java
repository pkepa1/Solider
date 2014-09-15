package com.solider.war.core.path;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.solider.war.core.sprites.Animation;
import com.solider.war.core.tools.MarkArea;
import com.solider.war.core.tools.Point;

import static com.solider.war.core.Config.PATH_MAP_SIZE;
import static com.solider.war.core.Config.FIELD_SIZE;


/**
 * @Autor Paweł Kępa
 * 
 * 
 */
public class CalcPath {
	
	private boolean foundDestinationPoint = false;
	private boolean foundPath = false;
	
	private PathPoint startPosition;
	private PathPoint destinationPosition;
 	private PathPoint[][]  pathMap= new PathPoint[PATH_MAP_SIZE][PATH_MAP_SIZE];
 	
 	private LinkedList<PathPoint> Q = new LinkedList<PathPoint>();
 	private LinkedList<PathPoint> W = new LinkedList<PathPoint>();
 	
	
	public CalcPath(Animation animation , int mapSize) {
		
		// calc destination point;
		int destX = (int) (Point.getTransformMousePoint().getX()/FIELD_SIZE);
		int destY = (int) (Point.getTransformMousePoint().getY()/FIELD_SIZE);
		
		// object cords
		int animationX = (int) (animation.getX()/FIELD_SIZE);
		int animationY = (int) (animation.getY()/FIELD_SIZE);
				
		System.out.println("object position(" + animationX +","+ animationY +")");
		System.out.println("destination position(" + destX +","+ destY +")");

		destinationPosition = new PathPoint(destX, destY);
		startPosition = new PathPoint(animationX, animationY);
		
	}
	
	public CalcPath() {
		// calc destination point;
		int destX = (int) (Point.getTransformMousePoint().getX()/FIELD_SIZE);
		int destY = (int) (Point.getTransformMousePoint().getY()/FIELD_SIZE);
		
		// fill table with value equals -1
		for(int i = 0; i< pathMap.length; i++) {
			for(int j=0; j<pathMap[i].length; j++) {
				pathMap[i][j] = new PathPoint(i, j);
				pathMap[i][j].setValue(-1);
				pathMap[i][j].setVisited(false);
			}
		}
	}
	
	public void beforeCalc(Animation animation ) {	
		this.Q.clear();
		this.W.clear();
		this.foundPath = false;
		// calc destination point;
		int destX = (int) (Point.getTransformMousePoint().getX()/FIELD_SIZE);
		int destY = (int) (Point.getTransformMousePoint().getY()/FIELD_SIZE);
		
		// object cords
		int animationX = (int) (animation.getX()/FIELD_SIZE);
		int animationY = (int) (animation.getY()/FIELD_SIZE);
				
		System.out.println("object position(" + animationX +","+ animationY +")");
		System.out.println("destination position(" + destX +","+ destY +")");

		destinationPosition = new PathPoint(destX, destY);
		startPosition = new PathPoint(animationX, animationY);
	}
	
	public LinkedList<PathPoint> calcPath( MarkArea markArea) {
			
		for(int i = 0; i< pathMap.length; i++) {
			for(int j=0; j<pathMap[i].length; j++) {
				pathMap[i][j].setValue(-1);
				pathMap[i][j].setVisited(false);
				pathMap[i][j].setX(i);
				pathMap[i][j].setY(j);
			}
		}
		
		// set start point in value 0
		pathMap[startPosition.getX()][startPosition.getY()].setValue(0);
		Q.add(pathMap[startPosition.getX()][startPosition.getY()]);
		
		PathPoint v;
		PathPoint w = null;
		
		while(!Q.isEmpty()) {
			w  = Q.poll();
			for(int i=0; i<8; i++) {		
				if(i == 0) {
					if( (w.getX()+1) < pathMap.length && !pathMap[w.getX()+1][w.getY()].isVisited() && !pathMap[w.getX()+1][w.getY()].isOccupied()) {
						pathMap[w.getX()+1][w.getY()].setVisited(true);
						pathMap[w.getX()+1][w.getY()].setValue(w.getValue()+1);
						Q.add(pathMap[w.getX()+1][w.getY()]);
					}
				}

				if(i == 1) {
					if((w.getY()+1) < pathMap.length && !pathMap[w.getX()][w.getY()+1].isVisited() && !pathMap[w.getX()][w.getY()+1].isOccupied()) {
						pathMap[w.getX()][w.getY()+1].setVisited(true);
						pathMap[w.getX()][w.getY()+1].setValue(w.getValue()+1);
						Q.add(pathMap[w.getX()][w.getY()+1]);
					}
				}
				
				if(i == 2) {
					if((w.getX()-1) >= 0 && !pathMap[w.getX()-1][w.getY()].isVisited() && !pathMap[w.getX()-1][w.getY()].isOccupied()) {
						pathMap[w.getX()-1][w.getY()].setVisited(true);
						pathMap[w.getX()-1][w.getY()].setValue(w.getValue()+1);
						Q.add(pathMap[w.getX()-1][w.getY()]);
					}
				}

				if(i == 3) {
					if((w.getY()-1) >= 0 && !pathMap[w.getX()][w.getY()-1].isVisited() && !pathMap[w.getX()][w.getY()-1].isOccupied() ) {
						pathMap[w.getX()][w.getY()-1].setVisited(true);
						pathMap[w.getX()][w.getY()-1].setValue(w.getValue()+1);
						Q.add(pathMap[w.getX()][w.getY()-1]);
					}
				}
				
				if(i == 4) {
					if( (w.getX()-1) >= 0 && (w.getY()+1) < pathMap.length && !pathMap[w.getX()-1][w.getY()+1].isVisited() && !pathMap[w.getX()-1][w.getY()+1].isOccupied()) {
						pathMap[w.getX()-1][w.getY()+1].setVisited(true);
						pathMap[w.getX()-1][w.getY()+1].setValue(w.getValue()+1);
						Q.add(pathMap[w.getX()-1][w.getY()+1]);
					}
				}
				
				if(i == 5) {
					if( (w.getX()+1) < pathMap.length && (w.getY()+1) < pathMap.length && !pathMap[w.getX()+1][w.getY()+1].isVisited() && !pathMap[w.getX()+1][w.getY()+1].isOccupied() ) {
						pathMap[w.getX()+1][w.getY()+1].setVisited(true);
						pathMap[w.getX()+1][w.getY()+1].setValue(w.getValue()+1);
						Q.add(pathMap[w.getX()+1][w.getY()+1]);
					}
				}
				
				if(i == 6) {
					if((w.getX()+1) < pathMap.length && (w.getY()-1) >= 0 && !pathMap[w.getX()+1][w.getY()-1].isVisited() && !pathMap[w.getX()+1][w.getY()-1].isOccupied() ) {
						pathMap[w.getX()+1][w.getY()-1].setVisited(true);
						pathMap[w.getX()+1][w.getY()-1].setValue(w.getValue()+1);
						Q.add(pathMap[w.getX()+1][w.getY()-1]);
					}
				}
				
				if(i == 7) {
					if((w.getX()-1) >= 0 && (w.getY()-1) > 0 && !pathMap[w.getX()-1][w.getY()-1].isVisited() && !pathMap[w.getX()-1][w.getY()-1].isOccupied()) {
						pathMap[w.getX()-1][w.getY()-1].setVisited(true);
						pathMap[w.getX()-1][w.getY()-1].setValue(w.getValue()+1);
						Q.add(pathMap[w.getX()-1][w.getY()-1]);
					}
				}
			}
			
			if(w.getX() == destinationPosition.getX() && w.getY() == destinationPosition.getY()) {
				System.out.println("Found Path"  + w.getValue());
				break;
			}
		}
		
		
		pathMap[startPosition.getX()][startPosition.getY()].setValue(0);
		W.add(w);
		while(!foundPath) {
			for(int i=0; i < 8; i++) {
				if(i == 0) {
					if(  (w.getX()+1)<pathMap.length && pathMap[w.getX()+1][w.getY()].getValue() == (w.getValue()-1) && !pathMap[w.getX()+1][w.getY()].isOccupied() ) {
						w=pathMap[w.getX()+1][w.getY()];
						break;
					}
				}
				
				if(i == 1) {
					if( (w.getY()+1) <pathMap.length && pathMap[w.getX()][w.getY()+1].getValue() == (w.getValue()-1) && !pathMap[w.getX()][w.getY()+1].isOccupied() ) {
						w=pathMap[w.getX()][w.getY()+1];
						break;
					}
				}
				
				if(i == 2) {
					if( (w.getX()-1) >= 0 && pathMap[w.getX()-1][w.getY()].getValue() == (w.getValue()-1) && !pathMap[w.getX()-1][w.getY()].isOccupied() ) {
						w=pathMap[w.getX()-1][w.getY()];
						break;
					}
				}
	
				if(i == 3) {
					if( (w.getY()-1) >= 0  && pathMap[w.getX()][w.getY()-1].getValue() == (w.getValue()-1) && !pathMap[w.getX()][w.getY()-1].isOccupied() ) {
						w=pathMap[w.getX()][w.getY()-1];
						break;
					}
				}
				
				if(i == 4) {
					if( (w.getY()-1) >= 0 && (w.getX()+1)<pathMap.length && pathMap[w.getX()+1][w.getY()-1].getValue() == (w.getValue()-1) && !pathMap[w.getX()+1][w.getY()-1].isOccupied() ) {
						w=pathMap[w.getX()+1][w.getY()-1];
						break;
					}
				}
				
				if(i == 5) {
					if( (w.getX()-1) >= 0 && (w.getY()+1)<pathMap.length && pathMap[w.getX()-1][w.getY()+1].getValue() == (w.getValue()-1) && !pathMap[w.getX()-1][w.getY()+1].isOccupied() ) {
						w=pathMap[w.getX()-1][w.getY()+1];
						break;
					}
				}
				
				if(i == 6) {
					if( (w.getX()+1) < pathMap.length&& (w.getY()+1)<pathMap.length && pathMap[w.getX()+1][w.getY()+1].getValue() == (w.getValue()-1) && !pathMap[w.getX()+1][w.getY()+1].isOccupied()) {
						w=pathMap[w.getX()+1][w.getY()+1];
						break;
					}
				}
				
				if(i == 7) {
					if( (w.getX()-1) >= 0 && (w.getY()-1)>=0 && pathMap[w.getX()-1][w.getY()-1].getValue() == (w.getValue()-1) && !pathMap[w.getX()-1][w.getY()-1].isOccupied() ) {
						w=pathMap[w.getX()-1][w.getY()-1];
						break;
					}
				}
			}
			W.add(w);
			
			if(w.getX() == startPosition.getX() && w.getY() == startPosition.getY()) {
				foundPath = true;
				System.out.println("Found Position");
				break;
			}	
		}
		
//		drawing path 
//		for(int i=0;i<W.size();i++) {
//			markArea.markPath((W.get(i).getX()*FIELD_SIZE), (W.get(i).getY()*FIELD_SIZE), FIELD_SIZE, FIELD_SIZE);
//		}

		return W;
	}
	
	public boolean isFoundDestinationPoint() {
		return foundDestinationPoint;
	}

	public void setFoundDestinationPoint(boolean foundDestinationPoint) {
		this.foundDestinationPoint = foundDestinationPoint;
	}

	public PathPoint getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(PathPoint startPosition) {
		this.startPosition = startPosition;
	}

	public PathPoint getDestinationPosition() {
		return destinationPosition;
	}

	public void setDestinationPosition(PathPoint destinationPosition) {
		this.destinationPosition = destinationPosition;
	}

	public PathPoint[][] getPathMap() {
		return pathMap;
	}

	public void setPathMap(PathPoint[][] pathMap) {
		this.pathMap = pathMap;
	}

	public LinkedList<PathPoint> getQ() {
		return Q;
	}

	public void setQ(LinkedList<PathPoint> q) {
		Q = q;
	}
}

