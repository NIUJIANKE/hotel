package javaDao;

import java.sql.Connection;

import javaModel.Result;
import javaModel.User;
import javaUtil.ConUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
	private ConUtil conUtil=new ConUtil();
	public Result login(String name,String password,int role){
		Result result = new Result();
		result.message = "�������Ӵ���";
		String sql="select * from user where name=? and role_id=?";
		Connection con=null;
		try {
			con = conUtil.getCon();
			PreparedStatement pst;
			pst = con.prepareStatement(sql);
			pst.setString(1, name);
			pst.setInt(2, role);
			ResultSet res=pst.executeQuery();

			if(!res.next()){
				result.success = false;
				result.message = "�������û������ڣ�";
				return result;
			}else{
				do{
					 System.out.print(res.getString("Password"));
					//user.setId(Integer.parseInt(res.getString("id")));
					if(!res.getString("Password").equals(password)){
						result.success = false;
						result.message = "�������";
						return result;
					}
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
		result.message = "��¼�ɹ���";
		result.success = true;
		return result;
	}
}