package com.hybridsoft;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;




public class Pagination extends HttpServlet
{

	SessionFactory factory;
	
	
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		// TODO Auto-generated method stub
		factory = new Configuration().configure().buildSessionFactory();
	
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		
	    int pageIndex = 0;
		int totalNumberOfRecords = 0;
		int numberOfRecordsPerPage = 4;

		String sPageIndex = req.getParameter("pageIndex");

		if(sPageIndex ==null)
		{
		pageIndex = 1;
		}else
		{
		pageIndex = Integer.parseInt(sPageIndex);
		}
		
		Session session=factory.openSession();
		
		int s = (pageIndex*numberOfRecordsPerPage) -numberOfRecordsPerPage;
        Criteria crit=session.createCriteria(Product.class);
        crit.setFirstResult(s);
        crit.setMaxResults(numberOfRecordsPerPage);
        
        
        List<Product> list=crit.list();
        
        Iterator itr=list.iterator();
	    PrintWriter pw=res.getWriter();
	    pw.println("<table border=1>");
	    pw.println("<tr>");
	    pw.println("<th>PID</th><th>PNAME</th><th>PRICE</th>");
	    pw.println("</tr>");
	    
	   while (itr.hasNext()) 
	   {
		Product p= (Product) itr.next();
		pw.println("<tr>");
		pw.println("<td>"+p.getProductId()+"</td>");
		pw.println("<td>"+p.getProductName()+"</td>");
		pw.println("<td>"+p.getProductPrice()+"</td>");
		pw.println("</tr>");
	  }
	   
	   pw.println("<table>");
	   
	   Criteria crit1=session.createCriteria(Product.class);
	  // crit1.setProjection(Projections.rowCount());
        
	   List list1=crit1.list();
	  // pw.println(list1.size());
	   
	   System.out.println(list1.size());
	   totalNumberOfRecords=list1.size();
	   //Iterator itr1=list1.iterator();
	  /* while (itr1.hasNext()) 
	   {
		Object object = (Object) itr1.next();
		totalNumberOfRecords=Integer.parseInt(object.toString());
		
	   }*/
	   
	   int noOfPages = totalNumberOfRecords/numberOfRecordsPerPage;
	   System.out.println(noOfPages);
	   if(totalNumberOfRecords>(noOfPages*numberOfRecordsPerPage))
	   {
		   noOfPages=noOfPages + 1;
		   System.out.println("====="+noOfPages);
	   }
	   
	   for(int i=1;i<=noOfPages;i++)
	   {
		   String myurl ="ind?pageIndex="+i;
		   pw.println("<a href="+myurl+">"+ "     "+i+"</a>");

	   }
	   
	   	   pw.close();
	   	   session.close();
	}
	
		
}
