package shoot;

import java.util.Random;

/**
 * 蜜蜂
 * @author KeHao
 *
 */
public class Bee extends FlyingObject implements Award{
	private int xSpeed;
	private int ySpeed=2;
	private int awardType;
	Random rand=new Random();
	public Bee() {
		image=ShootGame.bee;
		width=image.getWidth();
		height=image.getHeight();
		x=rand.nextInt(ShootGame.WIDTH-this.width);
		y=-this.height;
		awardType=rand.nextInt(2);
		//切换小蜜蜂入场方向
		if(rand.nextInt()%2==0){
			xSpeed=1;
		}else{
			xSpeed=-1;
		}
		
	}
	
	@Override
	public int getType() {
		return awardType;
	}
	
	@Override
	public void step() {
		
		x+=xSpeed;
		y+=ySpeed;
		if(x>ShootGame.WIDTH-width){
			xSpeed=-1;
		}
		if(x<0){
			xSpeed=1;
		}
	}

	@Override
	public boolean outOfBounds() {
		if(this.y>ShootGame.HEIGHT){
			return true;
		}
		return false;
	}
}
