package shoot;

import java.util.Random;

/**
 * 敌机：飞行物也是敌人
 * @author KeHao
 *
 */
public class Airplane extends FlyingObject implements Enemy{
	private int speed=2;//走步的步数
	public Airplane(){
		image=ShootGame.airplane;
		width=image.getWidth();
		height=image.getHeight();
		Random rand=new Random();
		x=rand.nextInt(ShootGame.WIDTH-this.width);
		y=-this.height;
	}
	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 5;
	}
	@Override
	public void step() {
		y+=speed;
	}
	@Override
	public boolean outOfBounds() {
		if(this.y>ShootGame.HEIGHT){
			return true;
		}
		return false;
	}
	
}
