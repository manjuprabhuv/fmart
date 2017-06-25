package com.fmart.ui.beans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.dao.ProductDao;
import com.fmart.hibernate.pojos.Product;

@ManagedBean
@ApplicationScoped
public class Images {
	
	byte[] noImagesrc;
	
	public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
        
        
        	
            String productId = context.getExternalContext().getRequestParameterMap().get("productId");
            ProductDao dao = new ProductDao();
            Integer prodId=null;
            try{
            	prodId = Integer.parseInt(productId);
            }catch (Exception e){
            	 return new DefaultStreamedContent();
            }
            if(noImagesrc==null){
            	loadNoImage();
            }
            Product p =  dao.findById(prodId);
            if(p.getImagesrc()==null && noImagesrc!=null){
            	 return new DefaultStreamedContent(new ByteArrayInputStream(noImagesrc));
            }
            
            if(p.getImagesrc()!=null){
            	 return new DefaultStreamedContent(new ByteArrayInputStream(p.getImagesrc()));
            }
            //return by default
            return new DefaultStreamedContent();
            
          
           
        }
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
