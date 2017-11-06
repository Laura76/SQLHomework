package DataImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

import util.Constant;

/**
 *created by Laura on 2017年10月28日
 * 
 */
public class Main {
	//初始化住宿安排数据库表并插入初始数据
	public void initialMySQLTables(Connection connection){
		InitialData initialData=new InitialData(connection);
		//		初始化hostel表格
		long startTime=System.currentTimeMillis();
		initialData.initialHostel();
		long endTime=System.currentTimeMillis();
		System.out.println("MySQL initial Hostel table takes "+(endTime-startTime)+"ms.");
		
		//		初始化postgraduate表格
		startTime=System.currentTimeMillis();
		initialData.initialPostgraduate();
		endTime=System.currentTimeMillis();
		System.out.println("MySQL initial Postgraduate table takes"+(endTime-startTime)+"ms");
	}
	//住宿安排数据库设计
	private void connectMySQL(){
		Connection connection=Constant.connector.getMySqlConnection();
		//初始化所有表格，插入所有初始数据
		this.initialMySQLTables(connection);
		//查询与王晓星同一栋楼的院系
		AccomodationArrange accomodationArrange=new AccomodationArrange(connection);
		Long startTime=System.currentTimeMillis();
		ArrayList<String> department=accomodationArrange.departmentSameHostel("王小星");
		Long endTime=System.currentTimeMillis();
		System.out.println("search the departmentSameHostel takes"+(endTime-startTime)+"ms");
		for(String item:department){
			System.out.println(item);
		}
		//修改宿舍住宿费
		startTime=System.currentTimeMillis();
		accomodationArrange.modifyPrice("陶园1舍", 1200);
		endTime=System.currentTimeMillis();
		System.out.println("modify price taks"+(endTime-startTime)+"ms");
		//男女生交换宿舍
		startTime=System.currentTimeMillis();
		accomodationArrange.changeHostel("软件学院");
		endTime=System.currentTimeMillis();
		System.out.println("change hostel taks"+(endTime-startTime)+"ms");
		}
		//初始化共享单车数据库表并插入初始数据
		public void initialMySQLTables2(Connection connection){
			InitialData initialData=new InitialData(connection);
			//		初始化user表格
			long startTime=System.currentTimeMillis();
			initialData.initialUser();
			long endTime=System.currentTimeMillis();
			System.out.println("MySQL initial user table takes"+(endTime-startTime)+"ms");
			
			//		初始化record表格
//			startTime=System.currentTimeMillis();
//			initialData.initialRecord();
//			endTime=System.currentTimeMillis();
//			System.out.println("MySQL initial record table takes"+(endTime-startTime)+"ms");
		
		}
		
	//共享单车数据库设计
	private void connectMySQL2(){
		Connection connection=Constant.connector.getMySqlConnection();
		this.initialMySQLTables2(connection);
		BikeSharingLogic bikeSharingLogic=new BikeSharingLogic(connection);
		//估计家庭住址
//		bikeSharingLogic.assumeHome();
		//添加纪录中的该趟费用
//		bikeSharingLogic.bikefee();
		//更新用户账户余额
//		bikeSharingLogic.modifyBalance();
		//维护单车维修表
//		Calendar now=Calendar.getInstance();
//		int dayNow=now.get(Calendar.DAY_OF_MONTH);
//		if(dayNow==1){
//			int yearNow=now.get(Calendar.YEAR);
//			int monthNow=now.get(Calendar.MONTH);
//			if(monthNow!=1)bikeSharingLogic.checkBike(yearNow,monthNow-1);
//			else bikeSharingLogic.checkBike(yearNow-1,12);
//		}
//		//test单车维修表逻辑
//		bikeSharingLogic.checkBike(2017,10);
}
	
	public static void main(String[] args){
		Main main=new Main();
		//住宿安排
//		main.connectMySQL();
		//共享单车
		main.connectMySQL2();
	}
}
