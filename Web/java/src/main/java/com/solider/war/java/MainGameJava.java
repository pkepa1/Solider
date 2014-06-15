package com.solider.war.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.solider.war.core.MainGame;

public class MainGameJava {
	
  public static void main(String[] args) {
	  
	  JavaPlatform.Config config = new JavaPlatform.Config();
	  config.height = 600;
	  config.width = 600;
	  
	  JavaPlatform.register(config);
	  PlayN.run(new MainGame());
  }
}
