package com.grupptva.runnergame.character;

public class CharacterModel {

	public Rectangle box;
	private Point position;

	private float xVelocity = 2;
	private float yVelocity = 0;

	private float gravityAcceleration = -0.4f;

	private boolean collidingWithGround = false;

	private float jumpInitialVelocity = 10f;

	private float groundCollisionCoordinate = 0;

	public boolean attachedWithHook = false;
	private boolean hookExtended = false;
	public Point hookPoint;
	private float hookAngle = 1.5f;
	public float hookRadius = 100;

	public CharacterModel(float x, float y) {
		box = new Rectangle(x, y, 25, 25);
		position = box.position;
	}

	//-----------------Methods used for gameloop---------------------------------
	public void update() {
		moveRight();
		fall();

		hook();

		if (position.getX() > 600)
			position.setX(0);
		if (position.getY() < -100)
			position.setY(0);
	}

	private void moveRight() {
		position.moveX(xVelocity);
	}

	private void fall() {
		if (!collidingWithGround) {
			yVelocity += gravityAcceleration;

			position.moveY(yVelocity);
		}
	}

	public void jump() {
		if (collidingWithGround) {
			//Force character out of the object it is currently standing on.
			position.moveY(1);

			yVelocity = jumpInitialVelocity;
		}
	}

	private void hook() {
		if (attachedWithHook) {
			if (!hookExtended) {
				if (position.distanceTo(hookPoint) > hookRadius) {
					hookExtended = true;
				}
			} else {
				float newY = hookGetNewY();
				if(attachedWithHook)
				{
					position.setY(newY);
				}
			}
		}
	}

	private void stopHook()
	{
		attachedWithHook = false;
	}
	
	private float hookGetNewY() {
		// Circle equation: r^2 = (x-a)^2+(y-b)^2 = x-2ax+a^2+y-2yb+b^2
		float r = hookRadius;
		float x = position.getX();
		float a = hookPoint.getX();
		float b = hookPoint.getY();
		float insideSqrt = -(a * a) + 2 * a * x + r * r - (x * x);
		if (insideSqrt < 0) {
			System.out.println("Sqrt negative number in hook()!");
			stopHook();
			return 0;
		}
		//get the lowest solution to y.
		return (b - Math.sqrt(insideSqrt) < b + Math.sqrt(insideSqrt))
				? b - (float) Math.sqrt(insideSqrt) : b + (float) Math.sqrt(insideSqrt);
	}

	public void initHook() {
		hookPoint = new Point(position);
		hookPoint.moveInDirection(hookRadius, hookAngle);
		attachedWithHook = true;
		hookExtended = false;
	}

	//-----------------Get methods-----------------------------------------------
	public float getX() {
		return position.getX();
	}

	public float getY() {
		return position.getY();
	}

	//-----------------Public methods used for controlling the model-------------

	public void setXVelocity(float velocity) {
		xVelocity = velocity;
	}

	/**
	 * Tell model it is colliding with the ground at coordinate {@Code y}, this
	 * coordinate is supposed to be point of collision because the character is
	 * then moved to that spot, in case it clipped deeper into whatever it is
	 * colliding with.
	 * 
	 * @param y
	 *            the y coordinate the character is colliding with.
	 * @See setGroundCollision, setGroundCollisionCoordinate.
	 */
	public void collideWithGround(float y) {
		//Order here important because setGroundCollision updates characters y position to the coordinate!
		setGroundCollisionCoordinate(y);
		setGroundCollision(true);
	}

	/**
	 * Sets whether the model is colliding with the ground currently or not. If
	 * setting to true, consider using {@See collideWithGround} instead since
	 * this method doesn't update the coordinate that the model is colliding at.
	 * 
	 * @param truth
	 *            the value to set collidingWithGround to.
	 */
	public void setGroundCollision(boolean truth) {
		collidingWithGround = truth;
		if (truth) {
			position.setY(groundCollisionCoordinate);
			yVelocity = 0;
		}
	}

	/**
	 * Sets the y coordinate that the character is collding with ground at. The
	 * character will not move below this value due to gravity as long as
	 * {@See collidingWithGround} is true.
	 * 
	 * @param y
	 */
	public void setGroundCollisionCoordinate(float y) {
		groundCollisionCoordinate = y;
	}
}