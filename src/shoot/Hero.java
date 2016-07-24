package shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 英雄机
 * @author KeHao
 *
 */
public class Hero extends FlyingObject{
	private int life;//生命值
	private int fire;//火力值
	private BufferedImage[] images;//图片数组
	private int index;//切换频率
	public Hero() {
		image=ShootGame.hero0;
		width=image.getWidth();
		height=image.getHeight();
		x=150;
		y=400;
		life=3;
		fire=0;
		images=new BufferedImage[]{ShootGame.hero0,ShootGame.hero1};
		index=0;
	}
	public int getLife(){
		return this.life;
	}
	public int getFire(){
		return this.fire;
	}
	/**
	 * 走步
	 */
	@Override
	public void step() {
		image=images[index++/10%images.length];
	}
	/**
	 * 发射子弹
	 */
	public Bullet[] shoot(){
		if(fire>0){//双倍火力
			Bullet[] bullets=new Bullet[2];
			bullets[0]=new Bullet(this.x+width/4,this.y-20);
			bullets[1]=new Bullet(this.x+width*3/4,this.y-20);
			fire-=2;
			return bullets;
		}else{//单倍火力
			Bullet[] bullets=new Bullet[1];
			bullets[0]=new Bullet(this.x+width/2,this.y-20);
			return bullets;
		}
	}
	/**
	 * 随鼠标移动
	 * @param x
	 * @param y
	 */
	public void moveTo(int x,int y){
		this.x=x-this.width/2;
		this.y=y-this.height/2;
	}
	public void setLife(int n){
		life+=n;
	}
	/**
	 * 加生命
	 */
	public void addLife(){
		life++;
	}
	/**
	 * 减生命
	 */
	public void subtractLife(){
		life--;
	}
	/**
	 * 加火力值
	 */
	public void addFire(){
		fire+=40;
	}
	/**
	 * 火力归零
	 */
	public void setFire(int n){
		this.fire=n;
	}
	@Override
	public boolean outOfBounds() {
		return false;
	}
	/**
	 * 检测英雄机与敌人相撞
	 * @param f
	 * @return
	 */
	public boolean hit(FlyingObject f){
		int x1=f.x-this.width/2;
		int x2=f.x+f.width+this.width/2;
		int y1=f.y-this.height/2;
		int y2=f.y+f.height+this.height/2;
		if(this.x+this.width/2>x1&&this.x+this.width/2<x2
				&&this.y+this.height/2>y1&&this.y+this.height/2<y2){
			return true;
		}
		return false;
	}
}
