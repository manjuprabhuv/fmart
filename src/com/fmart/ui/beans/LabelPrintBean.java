package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * 
 * @author jay
 */
@ManagedBean
@ViewScoped
public class LabelPrintBean implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private List<Sticker> saveStickers = new ArrayList<Sticker>();

	public List<Sticker> getSaveStickers() {
		return saveStickers;
	}

	public void setSaveStickers(List<Sticker> saveStickers) {
		this.saveStickers = saveStickers;
	}

	public List<Sticker> retrieveStickersInfo(){
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    saveStickers = (List<Sticker>) ec.getFlash().get("stickers"); 
	    return saveStickers;
	}

}
