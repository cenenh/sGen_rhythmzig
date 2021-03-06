package com.sgen.rp.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.sgen.DAO.UsersDAO;
import com.sgen.DTO.UsersDTO;
import com.sgen.util.JsonUtil;
import com.sgen.util.ResultStatus;

public class CheckDeleteUser extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	// 관리자가 맞는지 확인 Check !
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		

		String userCode = request.getParameter("userCode");
		String deviceCode = request.getParameter("deviceCode");
		String result = null;
		String returnResult = null;
		UsersDTO usersDTO = new UsersDTO();
		UsersDAO usersDAO = new UsersDAO();
		
		usersDTO.setUserCode(userCode);
		usersDTO.setDeviceCode(deviceCode);
		System.out.println("I WILL CHECK, deviceCode : " + usersDTO.getDeviceCode() + "and userCode : "+usersDTO.getUserCode()+" is Admin User");
		result = usersDAO.getAdminUserCode(usersDTO).getUserCode();
		
		if(result.equals(userCode))
		{
			System.out.println(usersDTO.getUserCode() 
					+ " is Admin user. CAN NOT DELETE");
			//jsonObj = JsonUtil.CAN_NOT_DELETE_USER();
			returnResult = ResultStatus.CAN_NOT_DELETE;
		}
		else if(!result.equals(userCode))
		{
			System.out.println(usersDTO.getUserCode() 
					+" is not Admin user. CAN DELETE");
			//jsonObj = JsonUtil.CAN_DELETE_USER();
			returnResult = ResultStatus.OK_TO_DELETE;
		}
		else if("".equals(result)) //아무것도 없을때.
		{
			System.out.println(ResultStatus.RE_CHECK_WANTED);
			//jsonObj = JsonUtil.UNKNOWN_USER();
			returnResult = ResultStatus.RE_CHECK_WANTED;
		}
		response.setContentType("charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(returnResult);
		out.close();
	}
}
