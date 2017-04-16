package com.mygdx.mapmaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import static com.mygdx.mapmaker.MapMaker.*;

/**
 * Created by Siavash Ranjbar on 4/15/2017.
 */
public class UserInput implements InputProcessor {
    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if(button == Input.Buttons.LEFT)
        {

            for(int i = 0; i < buttons.size; i ++)
            {
                if(buttons.get(i).isClicked(screenX, screenY))
                {
                    break;
                }
            }
            for(int i = 0; i < palleteButtons.size; i ++)
            {
                if(palleteButtons.get(i).isClicked(screenX, screenY))
                {
                    break;
                }
            }
         //   if(buttonId == -6)
        //    {
        //        selectSnapper.set(Gdx.input.getX(), ((mapHeight * tileSize) - Gdx.input.getY()));
        //        return true;
        //    }
            try {
                tiles[mouseToTileX(Gdx.input.getX() - (int)camera.x)][mouseToTileY(Gdx.input.getY() + (int)camera.y)] = selectedTile;
            } catch (IndexOutOfBoundsException e) {

            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for(int i = 0; i < buttons.size; i ++)
        {
            buttons.get(i).setClicked(false);
        }
        for(int i = 0; i < palleteButtons.size; i ++)
        {
            palleteButtons.get(i).setClicked(false);
        }

        selectDragger.x = 0;
        selectDragger.y = 0;

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

  //      if(buttonId == -6)
  //     {
  //          selectDragger.x = Gdx.input.getX() / viewportMultiplier.x - selectSnapper.x;
  //          selectDragger.y = ((mapHeight * tileSize) - Gdx.input.getY()) / viewportMultiplier.y - selectSnapper.y;
  //          return true;
  //      }

        try {
            tiles[mouseToTileX(Gdx.input.getX() - (int)camera.x)][mouseToTileY(Gdx.input.getY() + (int)camera.y)] = selectedTile;
        } catch (IndexOutOfBoundsException e) {

        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        boolean topShifted = false;
        boolean bottomShifted = false;
        if(amount > 0) //scroll down
        {
            if(scrollAmount < paletteTextures.size - 7)
            {
                scrollAmount++;
                for (int i = 0; i < palleteButtons.size; i++) {
                    palleteButtons.get(i).setY(palleteButtons.get(i).getY() + baseSize);
                    palleteButtons.get(scrollAmount - 1).setIsClickable(false);
                    palleteButtons.get(scrollAmount + 6).setIsClickable(true);
                }
            }
        }
        else if(amount < 0) //scroll up
        {
            if(scrollAmount > 0) {
                scrollAmount--;
                for (int i = 0; i < palleteButtons.size; i++) {
                    palleteButtons.get(i).setY(palleteButtons.get(i).getY() - baseSize);
                    palleteButtons.get(scrollAmount + 7).setIsClickable(false);
                    palleteButtons.get(scrollAmount).setIsClickable(true);
                }
            }
        }
        return false;
    }
}
