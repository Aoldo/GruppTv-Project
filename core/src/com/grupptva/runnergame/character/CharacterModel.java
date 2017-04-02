package com.grupptva.runnergame.character;

public class CharacterModel {

	public Rectangle box;
	private Point position;
	
	private float xVelocity = 0;
	
	/**
	 * Temporary value for gravity, it is currently a set speed that happens whenever the character isn't on the ground.
	 * TODO: Change to be accelleration and stuff.
	 */
	private float temporaryGravityValue = 1;
	
	private boolean collidingWithGround = false;
	private float groundCollisionCoordinate = 0;

	public CharacterModel(float x, float y) {
		box = new Rectangle(x, y, 25, 25);
		position = box.position;
	}

	//-----------------Private methods used for gameloop---------------------------
	public void update() {
		moveRight();
		fall();
	}
	
	private void moveRight()
	{
		position.moveX(xVelocity);
	}
	private void fall()
	{
		//TODO: Gravitation accelleration and stuff.
		if(!collidingWithGround)
		{
			position.moveY(temporaryGravityValue);
		}
	}
	private void jump()
	{
		//TODO: Jumping.
	}
	private void hook(){
		//TODO: Hooking.
	}
	
	
	
	//-----------------Public methods used for controlling the model-------------
	
	public void setXVelocity(float velocity)
	{
		xVelocity = velocity;
	}
	
	/**
	 * Tell model it is colliding with the ground at coordinate {@Code y}.
	 * @param y the y coordinate the character is colliding with.
	 * @See setGroundCollision, setGroundCollisionCoordinate.
	 */
	public void collideWithGround(float y)
	{
		//Order here important because setGroundCollision updates characters y position to the coordinate!
		setGroundCollisionCoordinate(y);
		setGroundCollision(true);
	}
	/**
	 * Sets whether the model is colldiding with the ground currently or not.
	 * If setting to true, consider using {@See collideWithGround} instead since this method doesn't 
	 * update the coordinate that the model is colliding at.
	 * @param truth the value to set collidingWithGround to.
	 */
	public void setGroundCollision(boolean truth)
	{
		collidingWithGround = truth;
		if(truth)
		{
			position.setY(groundCollisionCoordinate);
		}
	}
	/**
	 * Sets the y coordinate that the character is collding with ground at.
	 * The character will not move below this value due to gravity as long as
	 * {@See collidingWithGround} is true.
	 * @param y
	 */
	public void setGroundCollisionCoordinate(float y)
	{
		groundCollisionCoordinate = y;
	}
	
	
	
}