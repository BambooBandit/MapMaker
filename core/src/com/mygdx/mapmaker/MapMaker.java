package com.mygdx.mapmaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MapMaker implements Screen {
	static int baseSize;

	SpriteBatch batch;
	Sprite img;

	static Vector2 selectSnapper;
	static Vector2 selectDragger;
	static Vector2 camera;

	static Array<MapButton> buttons;
	static Array<MapButton> palleteButtons;
	MapButton eraseButton;
	MapButton generateButton;
	MapButton propertiesButton;
	MapButton flipCounterButton;
	MapButton flipClockButton;
	MapButton bucketButton;
	//MapButton selectButton;

	static int scrollAmount = 0;

	static Vector2 viewportMultiplier;

	static Integer[][] tiles; //The map written out in IDs
	static Array<MapTile> properties; //IDs are the index of the exact object holding arrays of properties
	Integer[][] palette; //IDs of tiles you can choose from the palette
	static Array<TextureRegion> paletteTextures = new Array<TextureRegion>(); //IDs are the index of the exact texture of the tile

	static int mapWidth;
	static int mapHeight;
	static int tileSize;

	ShapeRenderer shapeRenderer;

	private UserInput userInput = new UserInput();

	static int selectedTile = 0;
	static int lastSelectedTile = 0;

	static int buttonId = 0;

	Color selectedAlpha = new Color(1, 1, 1, .5f);
	Color placedAlpha = new Color(1, 1, 1, 1);

	public MapMaker(int baseSize, int tileSize, Integer tiles[][])
	{
		this.baseSize = baseSize;
		this.mapWidth = tiles.length;
		this.mapHeight = tiles[0].length;
		this.tileSize = tileSize;
		this.tiles = tiles;
	}

	@Override
	public void show()
	{
		camera = new Vector2(0,0);
		selectSnapper = new Vector2();
		selectDragger = new Vector2();
		buttons = new Array<MapButton>();
		palleteButtons = new Array<MapButton>();
		properties = new Array<MapTile>();

		shapeRenderer = new ShapeRenderer();
		Gdx.input.setInputProcessor(userInput);

		img = new Sprite(new Texture("tileset.png"));

		palette = new Integer[(int) (img.getHeight() / baseSize)][(int) (img.getWidth() / baseSize)];

		System.out.println("Window size: " + mapWidth * tileSize + ", " + mapHeight * tileSize);

		float windowWidth = mapWidth * tileSize;
		float windowHeight = mapHeight * tileSize;
		if(windowWidth < 300)
		{
			windowWidth = 300;
			if(windowWidth < tileSize)
			{
				windowWidth += tileSize;
			}
		}
		if(windowHeight < 300)
		{
			windowHeight = 300;
			if(windowHeight < tileSize)
			{
				windowHeight += tileSize;
			}
		}
		windowWidth += 300;

		if(windowWidth > 1700)
		{
			windowWidth = 1700;
		}
		if(windowHeight > 900)
		{
			windowHeight = 900;
		}

		viewportMultiplier = new Vector2(windowWidth / Gdx.graphics.getWidth(), windowHeight / Gdx.graphics.getHeight());

		resize((int) windowWidth, (int) windowHeight);

		int id = 0;
		int index = 0;
		for(int i = 0; i < palette.length; i++)
		{
			for(int j = 0; j < palette[0].length; j++)
			{
				palette[i][j] = id;
				TextureRegion region = new TextureRegion(img, j * baseSize, i * baseSize, baseSize, baseSize);
				paletteTextures.add(region);
				Sprite sprite = new Sprite(region);
				Sprite pressedSprite = new Sprite(region);
				pressedSprite.setFlip(true, true);
				boolean clickable = true;
				if(index > 6)
				{
					clickable = false;
				}
				MapButton palleteButton = new MapButton(sprite, pressedSprite, ((mapWidth * tileSize) + 20), ((mapHeight * tileSize)) - (index * baseSize) - 100, baseSize, baseSize, clickable, id);
				palleteButtons.add(palleteButton);
				id ++;
				index ++;
			}
		}

		batch = new SpriteBatch();

	//	selectButton = new MapButton(new Sprite(new Texture("select.png")), new Sprite(new Texture("selectPress.png")), ((mapWidth * tileSize) + 20 + tileSize * 3), ((mapHeight * tileSize)) - (7 * tileSize) - 100, 64, 64, true, -6);
		bucketButton = new MapButton(new Sprite(new Texture("bucket.png")), new Sprite(new Texture("bucketPress.png")), ((mapWidth * tileSize) + 20 + tileSize * 3), ((mapHeight * tileSize)) - (7 * tileSize) - 100, 64, 64, true, -6);
		flipClockButton = new MapButton(new Sprite(new Texture("flipClock.png")), new Sprite(new Texture("flipClockPress.png")), ((mapWidth * tileSize) + 20 + tileSize * 2), ((mapHeight * tileSize)) - (7 * tileSize) - 100, 64, 64, true, -5);
		flipCounterButton = new MapButton(new Sprite(new Texture("flipCounter.png")), new Sprite(new Texture("flipCounterPress.png")), ((mapWidth * tileSize) + 20 + tileSize), ((mapHeight * tileSize)) - (7 * tileSize) - 100, 64, 64, true, -4);
		generateButton = new MapButton(new Sprite(new Texture("generate.png")), new Sprite(new Texture("generatePress.png")), ((mapWidth * tileSize) + 90), 20, 120, 45, true, -3);
		eraseButton = new MapButton(new Sprite(new Texture("erase.png")), new Sprite(new Texture("erasePress.png")), ((mapWidth * tileSize) + 20), ((mapHeight * tileSize)) - (7 * tileSize) - 100, 64, 64, true, -1);
		propertiesButton = new MapButton(new Sprite(new Texture("properties.png")), new Sprite(new Texture("propertiesPress.png")), ((mapWidth * tileSize) + 30 + baseSize), ((mapHeight * tileSize)) - (selectedTile * baseSize) - 91, 120, 45, true, -2);
		buttons.add(generateButton);
		buttons.add(eraseButton);
		buttons.add(propertiesButton);
		buttons.add(flipCounterButton);
		buttons.add(flipClockButton);
		buttons.add(bucketButton);
	//	buttons.add(selectButton);
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.setColor(placedAlpha);
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {
			camera.y += 10;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {
			camera.y -= 10;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {
			camera.x -= 10;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
			camera.x += 10;
		}

		for(int i = 0; i < tiles.length; i ++)
		{
			for(int j = 0; j < tiles[0].length; j++)
			{
				if(tiles[i][j] != null && tiles[i][j] >= 0)
				{
					batch.draw(paletteTextures.get(tiles[i][j]), ((i * tileSize) + camera.x) / viewportMultiplier.x, ((j * tileSize) + camera.y) / viewportMultiplier.y, tileSize / viewportMultiplier.x, tileSize / viewportMultiplier.y);
				}
			}
		}

		batch.setColor(selectedAlpha);
		if(selectedTile >= 0)
		{
			batch.draw(paletteTextures.get(selectedTile), (((mouseToTileX2(Gdx.input.getX())) * tileSize)) / viewportMultiplier.x, (((mouseToTileY(Gdx.input.getY()) ) * tileSize)) / viewportMultiplier.y, tileSize / viewportMultiplier.x, tileSize / viewportMultiplier.y);
		}
		batch.end();
		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.rect((mapWidth * tileSize) / viewportMultiplier.x, 0, 300 / viewportMultiplier.x, mapHeight * tileSize / viewportMultiplier.y);
		shapeRenderer.setColor(Color.DARK_GRAY);
		shapeRenderer.rect((((mapWidth * tileSize) + 5) / viewportMultiplier.x), 5 / viewportMultiplier.y, (290 / viewportMultiplier.x), (((mapHeight * tileSize) - 10) / viewportMultiplier.y));
	//	if(buttonId == -6)
	//	{
	//		shapeRenderer.setColor(200, 200, 200, .2f);
	//		shapeRenderer.rect(selectSnapper.x, selectSnapper.y, selectDragger.x, selectDragger.y);
	//	}
		shapeRenderer.end();
		batch.begin();
		batch.setProjectionMatrix(shapeRenderer.getProjectionMatrix());

		int index = selectedTile - scrollAmount;

		if ((index < 0 || index > 6))
		{
			propertiesButton.setIsClickable(false);
		}
		else
		{
			propertiesButton.setIsClickable(true);
		}

		if(index != -1)
		{
			index = selectedTile;
			if(selectedTile == -1)
			{
				index = lastSelectedTile;
			}
			propertiesButton.setY(((mapHeight * tileSize)) - ((index - scrollAmount) * baseSize) - 91);
		}

		for(int i = 0; i < buttons.size; i ++)
		{
			if (buttons.get(i).isClickable())
			{
				batch.setColor(placedAlpha);
				batch.draw(buttons.get(i).getDrawableSprite(), buttons.get(i).getX() / viewportMultiplier.x, buttons.get(i).getY() / viewportMultiplier.y, buttons.get(i).getWidth() / viewportMultiplier.x, buttons.get(i).getHeight() / viewportMultiplier.y);
			}
		}
		for(int i = 0; i < palleteButtons.size; i ++) {
			if (palleteButtons.get(i).isClickable()) {
				batch.setColor(placedAlpha);
				batch.draw(palleteButtons.get(i).getDrawableSprite(), palleteButtons.get(i).getX() / viewportMultiplier.x, palleteButtons.get(i).getY() / viewportMultiplier.y, palleteButtons.get(i).getWidth() / viewportMultiplier.x, palleteButtons.get(i).getHeight() / viewportMultiplier.y);
			}
		}
		batch.end();

	}

	@Override
	public void resize(int width, int height) {
	//	Gdx.graphics.
		Gdx.graphics.setWindowedMode(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public static int mouseToTileX(int pos)
	{
		int newPos = pos % tileSize;
		newPos = pos - newPos;
		return (newPos / tileSize);
	}

	public static int mouseToTileX2(int pos)
	{
		int newPos = (pos ) % tileSize;
		newPos = (pos ) - newPos;
		return (newPos / tileSize);
	}

	public static int mouseToTileY(int y){
		int t = tileSize * mapHeight - mouseToTileX(y) * tileSize;
		return t / tileSize - 1;
	}

	public static int mouseToTileY2(int y)
	{
		return 0;
	}
}

