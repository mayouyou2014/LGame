﻿package org.test;

import loon.action.sprite.SpriteBatch;
import loon.action.sprite.painting.DrawableState;
import loon.core.LSystem;
import loon.core.geom.Vector2f;
import loon.core.graphics.LColor;
import loon.core.timer.GameTime;

//初始加载用类
public class LoadingEntity extends GameEntity {

	private boolean loadingIsSlow;
	private boolean otherScreensAreGone;
	private GameEntity[] screensToLoad;

	private LoadingEntity(EntityManager screenManager, boolean loadingIsSlow,
			GameEntity[] screensToLoad) {
		this.loadingIsSlow = loadingIsSlow;
		this.screensToLoad = screensToLoad;

	}

	@Override
	public void Draw(SpriteBatch batch, GameTime gameTime) {
		if ((super.getScreenState() == DrawableState.Active)
				&& (super.getScreenManager().GetScreens().length == 1)) {
			this.otherScreensAreGone = true;
		}
		if (this.loadingIsSlow) {
			Vector2f v = batch.getFont().getOrigin("LOADING...");
			batch.drawString("LOADING...", LSystem.screenRect.width / 2 - v.x,
					LSystem.screenRect.height / 2 - v.y, LColor.white);
		}
	}

	public static void Load(EntityManager screenManager, boolean loadingIsSlow,
			GameEntity... screensToLoad) {
		for (GameEntity screen : screenManager.GetScreens()) {
			screen.ExitScreen();
		}
		LoadingEntity screen2 = new LoadingEntity(screenManager, loadingIsSlow,
				screensToLoad);
		screenManager.AddScreen(screen2);
	}

	@Override
	public void LoadContent() {

	}

	@Override
	public void Update(GameTime gameTime, boolean coveredByOtherScreen) {
		super.Update(gameTime, coveredByOtherScreen);
		if (this.otherScreensAreGone) {
			super.getScreenManager().LoadGameContent();
			super.ExitScreen();
			if (super.getScreenManager().GetScreens().length == 0) {
				for (GameEntity screen : this.screensToLoad) {
					if (screen != null) {
						super.getScreenManager().AddScreen(screen);
					}
				}
			}
		}
	}
}