package com.sgen.rp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sgen.DAO.UsersDAO;
import com.sgen.DTO.UsersDTO;
import com.sgen.util.JDBCUtil;
import com.sgen.util.ResultStatus;

public class AddUser extends HttpServlet{

	private static final long serialVersionUID = 1L;
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int result = 0;
		boolean addUserSuccess = false;
		
		System.out.println("---------------------------------------");
		System.out.println("AddUser was called!");
		String userCode = request.getParameter("userCode");
		String deviceCode = request.getParameter("deviceCode");
		
		UsersDAO usersDAO = new UsersDAO();
		UsersDTO usersDTO = new UsersDTO();
		UsersDTO dto = new UsersDTO();
		
		usersDTO.setUserName(userCode); //이름은 우선 userCode로 등록
		usersDTO.setUserCode(userCode);
		usersDTO.setDeviceCode(deviceCode);
		usersDTO.setAdmin(false); //우선 false
		usersDTO.setRegId(ResultStatus.INITIAL_REG_ID);
		
		dto = usersDAO.isAdminUser(usersDTO);
		
		result = usersDAO.addUser(dto); // result == 0 for ERROR , result == 1 (insert를 하나만 함) for NOT-ERROR 
		
		if(result > 0 || result == 1) 
			addUserSuccess = true;
		else if (result == 0) 
			addUserSuccess = false;
		else 
			addUserSuccess = false;
		
		System.out.println("Add User Success : " + addUserSuccess);
		System.out.println("userCode : " + dto.getUserCode() );
		System.out.println("deviceCode : " + dto.getDeviceCode() );
		System.out.println("---------------------------------------");
		
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("<script type=\"text/javascript\">");
		if(result == 1 || result > 0){
		    out.println("alert('Add User Success');");
		}
		else{
			out.println("alert('Add User Error');");
		}
	    out.println("</script>");
	}
}
