/**
 * create by Laura on 2017年11月5日
 */
package DataImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *created by Laura on 2017年11月5日
 *共享单车业务逻辑
 * 
 */
public class BikeSharingLogic {
	private Connection connection;
	public BikeSharingLogic(Connection connection){
		this.connection=connection;
	}
	//估计家庭住址
	public void assumeHome(){
		  try{
		   Statement statement=connection.createStatement();
		   String sql="update user set user.homeAddress=( "
		     + " select T2.endAddress home  "
		     + "from record T2   "
		     + "where T2.userid=user.userid and T2.endAddress=(select  r.endAddress  "
		     + "from record r"
		     + " where r.userid=T2.userid and hour(r.endTime)>=18 and hour(r.endTime)<=24   "
		     + "group by r.endAddress   "
		     + "order by count(r.endAddress) desc   "
		     + "limit 1)   "
		     + "group by T2.userid );";
		   statement.executeUpdate(sql);
		   System.out.println("assumeHome succeed!");
		   }catch(SQLException e){
		   System.out.println("Oooops.....sql execute failed!");
		  }
	}
	//为记录添加费用
	public void bikefee(){
		try{
			Statement statement=connection.createStatement();
			String sql="update record set fee=case    "
					+ "when TIMESTAMPDIFF(minute,record.startTime,record.endTime)<=30 then 1  "
					+ "when TIMESTAMPDIFF(minute,record.startTime,record.endTime)>30 and TIMESTAMPDIFF(minute,record.startTime,record.endTime)<=60 then 2  "
					+ "when TIMESTAMPDIFF(minute,record.startTime,record.endTime)>60 and TIMESTAMPDIFF(minute,record.startTime,record.endTime)<=90 then 3  "
					+ "when TIMESTAMPDIFF(minute,record.startTime,record.endTime)>90 then 4   "
					+ "end;";
			statement.executeUpdate(sql);
			 System.out.println("add bikefee succeed!");
		}catch(SQLException e){
			System.out.println("Oooops....sql execute failed!");
		}
	}
	//修改用户余额,并修改用户可否使用的权限
	public void modifyBalance(){
		try{
			Statement statement=connection.createStatement();
			String sql="update user set user.balance=user.balance-(select sum(record.fee)  "
					+ "from record  "
					+ "where user.userid=record.userid  "
					+ ") ";
			statement.executeUpdate(sql);
			System.out.println("modifyBalance succeed!");
			sql="update user set user.privilege=0 where user.balance<0";
			statement.executeUpdate(sql);
			System.out.println("modifyPrivilege succeed!");
		}catch(SQLException e){
			System.out.println("Oooops....sql execute failed!");
		}
	}
	//每月月初检查单车（每个月重新维护一张维修表，删除旧有的维修表）
	public void checkBike(int year,int month){
		try{
			Statement statement=connection.createStatement();
			String sql="DROP TABLE IF EXISTS `bikeService`; ";
			statement.execute(sql);
			sql="create table bikeService(  bikeid int(10) not null, endAddress varchar(255) not null default \"\" , primary key(bikeid)   "
							+ ")engine=innodb charset=utf8; ";
			statement.execute(sql);
			System.out.println("create checkBike table succeed!");
			sql="insert into bikeservice(bikeid)   "
					+ "select r.bikeid bikeid  "
					+ "from record r  "
					+ "where year(r.startTime)=\"" + year + "\" and month(r.startTime)=\"" + month + "\" and year(r.endTime)=\"" + year + "\" and month(r.endTime)=\"" + month + "\"  "
					+ "group by r.bikeid  "
					+ "having sum(TIMESTAMPDIFF(hour,r.startTime,r.endTime))>200 "
					+ "; ";
			statement.execute(sql);
			System.out.println("insert the broken bikeid succeed!");
			sql="update bikeservice set endAddress=(select r.endAddress "
					+ "								from record r "
					+ "                             where bikeservice.bikeid=r.bikeid and year(r.endTime)=\"" + year + "\" and month(r.endTime)=\"" + month + "\" "
					+ "                             order by r.endTime desc "
					+ "                              limit 1);";
			statement.execute(sql);
			System.out.println("update the endAddress succeed!");
		}catch(SQLException e){
			System.out.println("Oooops....sql execute failed!");
		}
	}
}
