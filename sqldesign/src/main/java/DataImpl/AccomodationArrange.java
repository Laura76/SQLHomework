package DataImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *created by Laura on 2017年10月31日
 *住宿安排业务逻辑
 * 
 */
public class AccomodationArrange {
	private Connection connection;
	public AccomodationArrange(Connection connection){
		this.connection=connection;
	}
	public ArrayList<String> departmentSameHostel(String name){
		ArrayList<String> department=new ArrayList<String>();
		try{
			Statement statement=connection.createStatement();
			String sql="select distinct p.department "
					+ "from postgraduate p"
					+ " where p.hostelname in(select p1.hostelname"
											+ " from postgraduate p1"
											+ " where p1.name=\"" + name + "\" );";
			ResultSet resultset=statement.executeQuery(sql);
			while(resultset.next()){
				department.add(resultset.getString("p.department"));
			}
		}catch(SQLException e){
			System.out.println("Oooops......sql execute failed!");
		}
		return department;
	}
	public void modifyPrice(String hostelName,int expense){
		try{
			Statement statement=connection.createStatement();
			String sql="update `hostel` set expense=\"" + expense + "\"  where hostel.hostelname=\"" + hostelName + "\" ;";
			statement.execute(sql);
		}catch(SQLException e){
			System.out.println("Oooops......sql execute failed!");
		}
	}
	public void changeHostel(String department){
		ArrayList<String> hostelName=new ArrayList<String>();
		try{
			Statement statement=connection.createStatement();
			String sql="select distinct postgraduate.hostelname from postgraduate where postgraduate.department='软件学院' and postgraduate.gender='男';";
			ResultSet resultset=statement.executeQuery(sql);
			while(resultset.next()) hostelName.add(resultset.getString("postgraduate.hostelname"));
			sql="select distinct postgraduate.hostelname from postgraduate where postgraduate.department='软件学院' and postgraduate.gender='女';";
			resultset=statement.executeQuery(sql);
			while(resultset.next()) hostelName.add(resultset.getString("postgraduate.hostelname"));
			sql="update postgraduate set hostelname=\"" + hostelName.get(0) + "\" where postgraduate.department=\"" + department + "\" and postgraduate.gender='女';";
			statement.execute(sql);
			sql="update postgraduate set hostelname=\"" + hostelName.get(1) + "\" where postgraduate.department=\"" + department + "\" and postgraduate.gender='男';";
			statement.execute(sql);
		}catch(SQLException e){
			System.out.println("Ooops.....sql execute failed!");
		}
	}
}
