package DataImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import jxl.Sheet;
import jxl.Workbook;

/**
 *created by Laura on 2017年10月28日
 *初始化数据库表并插入数据
 * 
 */
public class InitialData {
	Connection connection;
	static Boolean finishFlag=false;
	public InitialData(Connection connection){
		this.connection=connection;
	}
	public BufferedReader readFromTXT(String fileHostel){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileHostel)),"UTF8"));
			return br;
		}catch(FileNotFoundException e){
			System.out.println("Oooops......File not found!");
		}catch(IOException e){
			System.out.println("Oooops......Read File failed!");
		}
		return null;
	}
	public void initialHostel(){
		String hostelTable="hostelDetail.txt";
		BufferedReader br=this.readFromTXT(hostelTable);
		try{
			Statement statement=connection.createStatement();
			String sql="DROP TABLE IF EXISTS `Hostel`; ";
			statement.execute(sql);
			statement.executeUpdate("create table if not exists Hostel("
	        		+ "hostelname varchar(255) not null,"
	        		+ "phone char(8) not null ,"
	        		+ "expense int(4) not null,"
	        		+ "campus varchar(255) not null,"
	        		+ "primary key(hostelname)"
	        		+ ");");
		}catch(SQLException e){
			System.out.println("Oooops....create Hostel table failed!");
		}
		if(null!=br){
			String line="";
			try{
			while( null!=( line=br.readLine() ) ){
				String[] items=line.split(";");
				String hostelname=items[0];
				String phone=items[1];
				int expense=Integer.valueOf(items[2]);
				String campus=items[3];
				try{
					Statement statement=connection.createStatement();
					statement.execute("insert into Hostel values(\"" + hostelname + "\",\"" + phone + "\",\"" +expense + "\",\""+ campus + "\");");		
				}catch(SQLException e){
					System.out.println("Oooops......sql execute failed!");
				}
			}
			}catch(IOException e){
				System.out.println("Oooops......read file failed!");
			}finally{
				try{
					br.close();
				}catch(IOException e){
					System.out.println("Oooops.....the resourse close failed!");
				}
			}
		}
			System.out.println("Initial Hostel successful!");
	}
	public void initialPostgraduate(){
		String postgraduateTable="postgraduateDetail.xls";
		jxl.Workbook workbook=null;
		try{
			Statement statement=connection.createStatement();
			String sql="DROP TABLE IF EXISTS `Postgraduate`; ";
			statement.execute(sql);
			statement.executeUpdate("create table if not exists Postgraduate("
        		+ "department varchar(255) not null,"
        		+ "studid varchar(255) not null,"
        		+ "name varchar(255) not null,"
        		+ "gender varchar(255) not null,"
        		+ "hostelname varchar(255) not null,"
        		+ "primary key(studid)"
        		+ ")engine=innodb charset=utf8;");
			
		}catch(SQLException e){
			System.out.println("Ooops....create Postgraduate table failed!");
		}
		try{
			InputStream input=new FileInputStream(postgraduateTable);
			workbook=Workbook.getWorkbook(input);
			Sheet sheet=workbook.getSheet(0);
			int row=sheet.getRows();
			String sql="INSERT INTO Postgraduate VALUES  ";
			for(int i=0;i<row;i++){
				String department=sheet.getCell(0, i).getContents();
				String studid=sheet.getCell(1, i).getContents();
				String name=sheet.getCell(2, i).getContents();
				String gender=sheet.getCell(3, i).getContents();
				String hostelname=sheet.getCell(4, i).getContents(); 
				Statement statement;
				try{
					statement=connection.createStatement();
					sql=sql+" (\""+department+"\", \""+studid+"\", \""+name+"\", \""+gender+"\", \""+hostelname+"\") , ";
					if(i==(row-1)) {
						statement.executeUpdate(sql.substring(0,sql.length()-2)); 
					}
				}catch(SQLException e){
					System.out.println("Oooops......the sql execute failed!");
				}
			}
		}catch(Exception e){
			System.out.println("Oooops......File read failed!");
		}
	}
	public void initialUser(){
		String user="user.txt";
		BufferedReader br=this.readFromTXT(user);
		String sql1="create table if not exists user("
				+ "		userid int(10) not null,"
				+ "		name varchar(255) not null,"
				+ "		phone varchar(255) not null,"
				+ "		balance double not null,"
				+ "		primary key(userid) "
				+ ")engine=innodb charset=utf8;";
		Statement statement;
		try {
			statement = connection.createStatement();
			String sql="DROP TABLE IF EXISTS `user`; ";
			statement.execute(sql);
			statement.execute(sql1);
		} catch (SQLException e1) {
			System.out.println("Oooops....create user table failed!");
		}
		if(null!=br){
			String line="";
			String prefix = "INSERT INTO user (userid, name,phone,balance) VALUES ";
			String suffix = "";  
			int i=0;
			try{
				while(null!=(line=br.readLine())){
					i++;
					String[] items=line.split(";");
					int userid=Integer.parseInt(items[0]);
					String name=items[1]; 
					String phone=items[2];
					double balance=Double.parseDouble(items[3]);
			        suffix=suffix+" (\""+userid+"\", \""+name+"\", \""+name+"\", \""+phone+"\", \""+balance+"\") , ";
			        if(i==50000){
			        	String sql = prefix + suffix.substring(0, suffix.length()-2); 
						try {
							connection.setAutoCommit(false);
							PreparedStatement prestatement=connection.prepareStatement("");
							prestatement.addBatch(sql);
							prestatement.executeBatch();
							connection.commit();
							suffix="";
							i=0;
						} catch (SQLException e) {
							System.out.println("Oooops....sql execute failed!");
						}
			        }
			        
			     }
			}catch(IOException e){
				System.out.println("Oooops......file read failed!");
			}finally{
				try{
					br.close();
				}catch(IOException e){
					System.out.println("Oooops....bufferedreader close failed!");
				}
			}
		}
	}
	public void initialRecord(){
		String record="record.txt";
		BufferedReader br=this.readFromTXT(record);
		String sql1="create table if not exists record("
				+ "		userid int(10) not null,"
				+ "		bikeid int(10) not null,"
				+ "		startAddress varchar(255) not null,"
				+ "		startTime datetime not null,"
				+ "		endAddress varchar(255) not null,"
				+ "		endTime datetime,"
				+ "		primary key(userid,startTime) "
				+ ")engine=innodb charset=utf8;";
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.execute(sql1);
		} catch (SQLException e1) {
			System.out.println("Oooops....create user table failed!");
		}
		if(null!=br){
			String line="";
			String prefix = "INSERT INTO record (userid, bikeid,startAddress,startTime,endAddress,endTime) VALUES ";
			 StringBuffer suffix = new StringBuffer();  
			 Boolean weird=true;
			 int userid=0;
			 int i=0;
			try{
				while(null!=(line=br.readLine())){
					String[] items=line.split(";");
					if(true==weird){
						userid=Integer.parseInt(items[0].substring(1));
						weird=false;
					}else{
						userid=Integer.parseInt(items[0]);
					}
					int bikeid=Integer.parseInt(items[1]);
					String startAddress=items[2];
					String startTime=items[3];
					String endAddress=items[4];
					String endTime=items[5];
			        suffix.append("(" + userid +", "+ bikeid +",'"+ startAddress+"','" + startTime +"','" + endAddress +"','" + endTime + "'),");  
			        if(i==30000){
			        	String sql = prefix + suffix.substring(0, suffix.length() - 1); 
						 try {
							statement =connection.createStatement();
							 statement.execute(sql);
						} catch (SQLException e) {
							System.out.println("Oooops....sql execute failed!");
						}
						 i=0;
						 suffix.delete(0, suffix.length());
			        }else i++;
			     }
				 
			}catch(IOException e){
				System.out.println("Oooops......file read failed!");
			}finally{
				try{
					br.close();
				}catch(IOException e){
					System.out.println("Oooops....bufferedreader close failed!");
				}
			}
		}
		
	}
}
 
