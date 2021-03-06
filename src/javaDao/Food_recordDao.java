package javaDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javaModel.Food;
import javaModel.Food_record;
import javaModel.Result;
import javaModel.User;
import javaUtil.ConUtil;
import javaUtil.TimeNowUtil;

public class Food_recordDao {
	private ConUtil conUtil=new ConUtil();
	//创建餐单
	public Result create(int bill_id,int food_id,int food_num){
		Result result = new Result();
		result.message = "网络连接错误！";
		ArrayList<Food> foodList = new ArrayList();
		String sqls="select * from food where id=? ";
		Connection cons=null;
		try {
			cons = conUtil.getCon();
			PreparedStatement pst;
			pst = cons.prepareStatement(sqls);
			pst.setInt(1, food_id);
			ResultSet res=pst.executeQuery();
			if(!res.next()){
				return null;
			}else{
				do{
					Food food = new Food();
					food.setid(res.getInt("id"));
					food.setname(res.getString("name"));
					food.setprice(res.getDouble("price"));
					foodList.add(food);
		        }while(res.next());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try {
				conUtil.closeCon(cons);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		double record_price = foodList.get(0).getprice() * food_num;

	      System.out.println(foodList.get(0).getprice());
		String createTime = new TimeNowUtil().now();
		String sql="insert into food_record(bill_id,food_id,food_num,record_price,create_time) values(?,?,?,?,?)";
		Connection con=null;
		try {
			con = conUtil.getCon();
			PreparedStatement pst;
			pst = con.prepareStatement(sql);
			pst.setInt(1, bill_id);
			pst.setInt(2, food_id);
			pst.setInt(3, food_num);
			pst.setDouble(4, record_price);
			pst.setString(5, createTime);
			int n=pst.executeUpdate();
			if(n <= 0){
				result.message = "创建餐单失败！";
				result.success = false;
				return result;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try {
				conUtil.closeCon(con);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		result.message = "创建餐单成功！";
		result.success = true;
		return result;
	}
	//取消订单
			public Result abolish(int id){
				Result result = new Result();
				result.message = "网络连接错误！";
				String deleteTime = new TimeNowUtil().now();
				String sql="update food_record set delete_time = ? where id = ?";
				Connection con=null;
				try {
					con = conUtil.getCon();
					PreparedStatement pst;
					pst = con.prepareStatement(sql);
					pst.setString(1, deleteTime);
					pst.setInt(2, id);
					int n=pst.executeUpdate();
					if(n <= 0){
						result.message = "餐单取消失败！";
						result.success = false;
						return result;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}finally{
					try {
						conUtil.closeCon(con);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				result.message = "餐单取消成功！";
				result.success = true;
				return result;
			}
	//根据定单号获取餐单
	public ArrayList list(int bill_id){
		ArrayList<Food_record> frList = new ArrayList();
		String sql="select food_record.*,food.name as food_name from food_record left join food on food_record.food_id = food.id  where food_record.bill_id=?  and food_record.delete_time is NULL limit 0, 50";
		Connection con=null;
		try {
			con = conUtil.getCon();
			PreparedStatement pst;
			pst = con.prepareStatement(sql);
			pst.setInt(1, bill_id);
			ResultSet res=pst.executeQuery();
			if(!res.next()){
				return null;
			}else{
				do{
					Food_record fr = new Food_record();
					fr.setid(res.getInt("id"));
					fr.setfood_name(res.getString("food_name"));
					fr.setfood_num(res.getInt("food_num"));
					fr.setrecord_price(res.getDouble("record_price"));
					fr.setcreate_time(res.getString("create_time"));
					frList.add(fr);
		        }while(res.next());
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try {
				conUtil.closeCon(con);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return frList;
		}
	//删除
	public Result delete(int id){
		Result result = new Result();
		result.message = "网络连接错误！";
		String updateTime = new TimeNowUtil().now();
		String sql;
		Connection con=null;
		if(id == 0){
			result.message = "取消菜品失败！";
			result.success = false;
			return result;
		}else{
			sql = "update food_record set delete_time = ? where id = ?";
			try {
				con = conUtil.getCon();
				PreparedStatement pst;
				pst = con.prepareStatement(sql);
				pst.setString(1, updateTime);
				pst.setInt(2, id);
				int n=pst.executeUpdate();
				if(n <= 0){
					result.message = "取消菜品失败！";
					result.success = false;
					return result;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally{
				try {
					conUtil.closeCon(con);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		result.message = "删除新桌成功！";
		result.success = true;
		return result;
	}
	
}
