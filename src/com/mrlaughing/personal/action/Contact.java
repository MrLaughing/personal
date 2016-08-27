package com.mrlaughing.personal.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 联系我
 * @author Laughing_Lz
 * @time 2016年8月27日
 */
public class Contact extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String author = req.getParameter("author");// 寄信人
		String email = req.getParameter("email");// 邮箱
		String text = req.getParameter("text");// 内容,中文需要转码
		author = new String(author.getBytes("ISO8859-1"), "UTF-8");// 转码
		email = new String(email.getBytes("ISO8859-1"), "UTF-8");// 转码
		text = new String(text.getBytes("ISO8859-1"), "UTF-8");// 转码
		try {
			sendMessage(author, email, text);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resp.sendRedirect("./");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * 发送邮件
	 * 
	 * @param author
	 *            发送人
	 * @param email
	 *            发送人邮箱
	 * @param text
	 *            发送内容
	 * @return
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static void sendMessage(String author, String email, String text)
			throws AddressException, MessagingException, UnsupportedEncodingException {
		// Properties properties = new Properties();
		Properties properties = System.getProperties();
		properties.setProperty("mail.transport.protocol", "smtp");// 发送邮件协议
		properties.setProperty("mail.smtp.auth", "true");// 需要验证
		properties.setProperty("mail.debug", "false");// 设置debug模式 后台输出邮件发送的过程
		Session session = Session.getInstance(properties);
		session.setDebug(false);// debug模式
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("Laughing_Lz@163.com"));// 设置发送人(不能省略)
		text = "发件人：" + author + "，发件人邮箱：" + email + "，内容如下：\n" + text;
		message.setText(text);// 设置邮件内容
		message.setSubject("来自星星的你");// 设置邮件主题
		Transport tran = session.getTransport();
		tran.connect("smtp.163.com", 25, "Laughing_Lz@163.com", "liuzhen85269");// 连接到邮箱服务器，设置发送邮箱信息
		tran.sendMessage(message, new Address[] { new InternetAddress("Laughing_Lz@163.com") });// 设置邮件接收人
		tran.close();
	}
}
