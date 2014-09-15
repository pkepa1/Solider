package com.solider.war.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.solider.war.core.MainGame;

public class MainGameHtml extends HtmlGame {

  @Override
  public void start() {

	  HtmlPlatform.Config config = new HtmlPlatform.Config();
	  // use config to customize the HTML platform, if needed
	  HtmlPlatform platform = HtmlPlatform.register(config);
	  platform.assets().setPathPrefix("Web/");
	  PlayN.run(new MainGame());
	  
  }
}
