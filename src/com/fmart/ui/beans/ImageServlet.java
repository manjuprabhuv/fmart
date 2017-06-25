package com.fmart.ui.beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;

import com.fmart.hibernate.dao.ProductDao;
import com.fmart.hibernate.pojos.Product;

/**
 * Servlet implementation class ImageServlet
 */

public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	byte[] noImagesrc;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String productId = request.getPathInfo().substring(1);
		
		
        ProductDao dao = new ProductDao();
        Integer prodId=null;
        try{
        	prodId = Integer.parseInt(productId);
        }catch (Exception e){
        	setImage("noImage", response, noImagesrc);
        }
        if(noImagesrc==null){
        	loadNoImage();
        }
        Product p =  dao.findById(prodId);
        if(p.getImagesrc()==null && noImagesrc!=null){
        	setImage("noImage", response, noImagesrc);
        }
        
        if(p.getImagesrc()!=null){
        	 setImage(p.getImage(), response, p.getImagesrc());
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void setImage(String imageName,  HttpServletResponse response,byte[] content) throws IOException{
		response.setContentType(getServletContext().getMimeType(imageName));
        response.setContentLength(content.length);
        response.getOutputStream().write(content);
	}
	
	private void loadNoImage()  throws IOException {
		/*FacesContext context = FacesContext.getCurrentInstance();
    	ExternalContext externalContext = context.getExternalContext();
    	ServletContext sc = (ServletContext)externalContext.getContext();
    	String realpath = sc.getRealPath("/resources/images/no_image_icon.gif");
    	File f = new File(realpath);*/
    	InputStream is = 
    			Images.class.getResourceAsStream("no_image_icon.gif");
    	//InputStream is = new FileInputStream(f);
    	byte[] b =IOUtils.toByteArray(is);
    	this.noImagesrc = b;
	}

}
