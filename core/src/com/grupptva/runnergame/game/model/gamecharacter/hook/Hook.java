package com.grupptva.runnergame.game.model.gamecharacter.hook;

import com.grupptva.runnergame.game.model.gamecharacter.Point;

/**
 * Created by agnesmardh on 2017-05-20.
 */
public class Hook extends AbstractHook {

	@Override
	public void initHook(Point hookPosition, float hookMaxLength) {
		setPosition(hookPosition);
		setMaxLength(hookMaxLength);
		setLength(getMaxLength());
	}

	@Override
	public float getYPosAfterSwing(float characterXPos) {
		return getPosition().getY() - (float) Math.sqrt(getLength() * getLength() -
				((characterXPos + 1 - getPosition().getX()) * (characterXPos + 1 - getPosition().getX())));
	}

	@Override
	public float getReleaseVelocity(float characterXPos, float characterYPos, float jumpInitialVelocity) {
		return getReleaseVelocity(getPosition().getX(), getPosition().getY(), characterXPos, characterYPos,
				jumpInitialVelocity);
	}

	@Override
	public float getReleaseVelocity(float hookXPos, float hookYPos, float characterXPos, float characterYPos,
	                                float jumpInitialVelocity) {
		double angle = Math.atan((characterYPos - hookYPos)/(characterXPos - hookXPos))+3.1415f/2;
		return (float) Math.sin(angle) * jumpInitialVelocity;
	}

	@Override
	public void moveHook(float pixelsPerFrame) {
		getPosition().setX(getPosition().getX() - pixelsPerFrame);
	}
}
