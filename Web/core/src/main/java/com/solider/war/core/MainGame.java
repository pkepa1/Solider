package com.solider.war.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.kerberos.KerberosKey;

import org.w3c.dom.css.RGBColor;

import com.solider.war.core.model.DestinationPoint;
import com.solider.war.core.model.GameMap;
import com.solider.war.core.model.GPoint;
import com.solider.war.core.path.CalcPath;
import com.solider.war.core.path.MapPoint;
import com.solider.war.core.sprites.Animation;
import com.solider.war.core.sprites.model.Barrel;
import com.solider.war.core.sprites.model.Solider;
import com.solider.war.core.sprites.model.Tank;
import com.solider.war.core.tools.MarkArea;
import com.solider.war.core.tools.Point;
import com.solider.war.core.tools.Transform;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;
import playn.core.*;
import playn.core.Keyboard.TypedEvent;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import playn.core.Pointer.Event;
import playn.core.canvas.GroupLayerCanvas;
import static playn.core.PlayN.*;
import static com.solider.war.core.Config.MAP_SIZE;
import static com.solider.war.core.Config.WINDOW_HEIGHT;
import static com.solider.war.core.Config.WINDOW_WIDTH;
import static com.solider.war.core.Config.FIELD_SIZE;
import static com.solider.war.core.Config.PATH_MAP_SIZE;

public class MainGame extends Game.Default {
	
	public final  MapPoint[][] map =  new MapPoint[PATH_MAP_SIZE][PATH_MAP_SIZE];
	
	private Tank tank;
	private boolean MOUSE_RIGHT_BUTTON_DOWN = false;
 	private boolean MOUSE_LEFT_BUTTON_DOWN = false;
 	private boolean MOUSE_HAVE_MOVING_WITH_RIGHT_BITTON_DOWN = false;
 	private boolean KEY_CTRL_DOWN = false;
 	CalcPath calcPath;
 
	private GroupLayer layer;
	private GroupLayer animationLayer_2RD;
	private GroupLayer animationLayer_3RD;
	private GameMap gameMap = new GameMap(MAP_SIZE, MAP_SIZE); 
	private List<Animation> animations = new ArrayList<Animation>();
	
	ImageLayer bgLayer;	
	MarkArea markArea;
	
	public MainGame() {
		super(25); // call update every 33ms (30 times per second)
	}

	@Override
	public void init() {
		
		// fill table with value equals -1
		for(int i = 0; i< map.length; i++) {
			for(int j=0; j<map[i].length; j++) {
				map[i][j] = new MapPoint(i, j);
				map[i][j].setValue(-1);
				map[i][j].setVisited(false);
			}
		}
		
		Image bgImage = assets().getImage("sprites/map.png");
		
		// create a group layer to hold everything
		layer = graphics().createGroupLayer();
		bgLayer = graphics().createImageLayer(bgImage);
		animationLayer_2RD = graphics().createGroupLayer();
		animationLayer_3RD = graphics().createGroupLayer();
		
	    // draw a soothing flat background
	    CanvasImage bgtile = graphics().createImage(FIELD_SIZE, FIELD_SIZE);
	    bgtile.canvas().setFillColor(0xFFCCCCCC);
	    bgtile.canvas().setStrokeColor(0xFFFFFFFF);
	    bgtile.canvas().strokeRect(0, 0, FIELD_SIZE, FIELD_SIZE);
	    bgtile.setRepeat(true, true);
	    ImageLayer bg = graphics().createImageLayer(bgtile);
	    calcPath = new CalcPath();
	    
	    bg.setWidth(MAP_SIZE);
	    bg.setHeight(MAP_SIZE);
	    
		graphics().rootLayer().add(layer);
		layer.add(bgLayer);  // BACKGROUND
		
		layer.add(bg);
		markArea = new MarkArea(layer);
		
		layer.add(animationLayer_2RD);
		layer.add(animationLayer_3RD);
		
		// Add one solider sprite  to game the game
		addSolider(graphics().width() / 2, graphics().height() / 2);
		addSolider(100, 250);
		
		
		addTank(50,50);

//////////////////////////////////////////////////////////////////////////
//***********************************************************************
//			MOUSE
//***********************************************************************
		PlayN.mouse().setListener(new Mouse.Adapter() {
			
			    @Override
			    public void onMouseDown(ButtonEvent event) {
			    	Point.setStartPoint(event.x(), event.y());
			    	if( event.button() ==  Mouse.BUTTON_RIGHT ) {
			    		MOUSE_RIGHT_BUTTON_DOWN = true;
			    		
			    		
			    		
			    	}
			    	
			    	if( event.button() ==  Mouse.BUTTON_LEFT ) {
			    		MOUSE_LEFT_BUTTON_DOWN = true;
			    		if(KEY_CTRL_DOWN == true ) {
			    			int corX =(int) (Point.getTransformStartPoint().getX()/FIELD_SIZE);
			    			int corY =(int) (Point.getTransformStartPoint().getY()/FIELD_SIZE);
			    			markArea.markPathOnClick(corX*FIELD_SIZE, corY*FIELD_SIZE, FIELD_SIZE, FIELD_SIZE);
			    			map[corX][corY].setOccupied(true);
			    		}
			    	}
			    }
			    
			    @Override
			    public void onMouseMove(MotionEvent event) {
			    	
			    	if(MOUSE_LEFT_BUTTON_DOWN) {
			    			markArea.mark(Point.getTransformStartPoint().getX(),
			    						  Point.getTransformStartPoint().getY(),
			    						  event.x()+(-Transform.getX()), 
			    						  event.y()+(-Transform.getY()) );
			    			
			    			Point.setEndPoint(event.x(), event.y());
			    	}
			    	
			    	if(MOUSE_RIGHT_BUTTON_DOWN) {
			    		MOUSE_HAVE_MOVING_WITH_RIGHT_BITTON_DOWN = true;
			    		checkMapBoundariesForCamera(event);
			    		
			    	} else {
			    		MOUSE_HAVE_MOVING_WITH_RIGHT_BITTON_DOWN = false;
			    	}
			    	
			    	if( MOUSE_LEFT_BUTTON_DOWN  ) {
			    		
					}
			    }
			    
			    @Override
			    public void onMouseUp(ButtonEvent event) {
			    	
			    	markArea.intersects(animations);
			    	markArea.clear( );
			    	if( event.button() ==  Mouse.BUTTON_RIGHT ) {
			    		
			    		MOUSE_RIGHT_BUTTON_DOWN = false;
			    		Point.setMousePoint(event.x(), event.y());
			    		
			    		if(!MOUSE_HAVE_MOVING_WITH_RIGHT_BITTON_DOWN) {
				    		for (Animation animation : animations) {
								if(animation.isSelected()) {
									animation.setPath(animation, markArea, map);
								}								
							}
			    		}
			    	}
			    	
					if( event.button() ==  Mouse.BUTTON_LEFT  ) {
						MOUSE_LEFT_BUTTON_DOWN = false;
						for(Animation animation : animations) {
							animation.select(event.x(), event.y(), markArea);
						}
					}
			    }
			    
			    @Override
			    public void onMouseWheelScroll(WheelEvent event) {
			    }
		});


//////////////////////////////////////////////////////////////////////////		
//***********************************************************************
//			KEYBOARD 
//***********************************************************************
		
	    PlayN.keyboard().setListener(new Keyboard.Adapter() {
	    	
	    	@Override
	        public void onKeyDown(Keyboard.Event event) {
	    		KEY_CTRL_DOWN = true;
	    		tank.getBarrel().setFire(true);
	    		tank.getBarrel().fire();
	        }

	        @Override
	        public void onKeyUp(Keyboard.Event event) {
	        	KEY_CTRL_DOWN = false;
	        }
	     });
	    
	}

///////////////////////////////////////////////////////////////////////////		
//*************************************************************************
// 			UPDATE AND FUNCTJONS
//*************************************************************************
	
	@Override
	public void update(int delta) {
		for (Animation animation : animations) {
			if(animation.isMoving()) animation.update(delta, animations);
			if(animation instanceof Tank) {
				((Tank) animation ).updateBarrel(delta, animations);
			}
			animation.fire();
		}
	}
	
	@Override
	public void paint(float alpha) {
		// the background automatically paints itself, so no need to do anything
		// here!
	}
	
	private void addSolider(float x, float y) {
		Solider solider = new Solider(x, y, animationLayer_2RD);	
		animations.add(solider);
	}
	
	private void addTank(float x, float y) {
		this.tank = new Tank(x, y, animationLayer_2RD, animationLayer_3RD);
		animations.add(tank);
	}
	
	// checking Boundaries if camera deasn't come out off map size
	private void checkMapBoundariesForCamera(MotionEvent event ) {
		float tempTransformX = event.x() - Point.getTransformStartPoint().getX();
		float tempTransformY = event.y() - Point.getTransformStartPoint().getY();
		
		if(tempTransformX <= 0 &&  (tempTransformX - WINDOW_WIDTH) >= (-MAP_SIZE) ) {
			Transform.setX(tempTransformX);
			layer.setTx(Transform.getX());
		}
		
		if(tempTransformY <= 0 && (tempTransformY - WINDOW_HEIGHT) >= (-MAP_SIZE) ) {
			Transform.setY(tempTransformY);
			layer.setTy(tempTransformY);
		}
	}
}
