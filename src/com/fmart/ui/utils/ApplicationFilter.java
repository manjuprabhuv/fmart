package com.fmart.ui.utils;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fmart.ui.beans.UserSession;

public class ApplicationFilter implements Filter {
	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession(true);
		try {
			SessionInfo sessionInfo = (SessionInfo) session
					.getAttribute("user.session");
			if (sessionInfo != null) {
				Cookie[] cookies = request.getCookies();
				boolean loginCookieExists = false;
				boolean adminCookieExists = false;
				for (Cookie cookie : cookies) {
					if ("fmart.logincheck".equalsIgnoreCase(cookie.getName())) {
						loginCookieExists = true;
					}
					if ("admin.login".equalsIgnoreCase(cookie.getName())) {
						adminCookieExists = true;
					}
				}
				String uri = request.getRequestURI();
				if (!loginCookieExists || !adminCookieExists) {
					if (!uri.contains("login.xhtml")) {
						session.invalidate();
						invalidate(servletRequest, servletResponse, filterChain);
					}
				} else {

					if (uri.contains("home.xhtml")) {
						filterChain.doFilter(servletRequest, servletResponse);

					} else {
						if (checkPageAccess(uri, request)) {
							filterChain.doFilter(servletRequest,
									servletResponse);
						} else {
							response.sendRedirect(request.getContextPath()
									+ "/home.xhtml?autherror=true");
						}
					}

				}

				// response.sendRedirect(request.getContextPath()+"/faces/home.xhtml");
			} else {
				invalidate(servletRequest, servletResponse, filterChain);

			}
		} catch (Exception e) {
			e.printStackTrace();
			filterChain.doFilter(servletRequest, servletResponse);

		}

	}

	private boolean checkPageAccess(String uri, HttpServletRequest request) {
		System.out.println(uri);
		String page = getPage(uri);
		SessionInfo session = (SessionInfo) request.getSession(true)
				.getAttribute("user.session");
		boolean disabled = false;
		switch (page) {
		case "company.xhtml":
			disabled = session.isAuthorized("Company");
			break;
		case "employee.xhtml":
			disabled = session.isAuthorized("Employee");
			break;
		case "dealer.xhtml":
			disabled = session.isAuthorized("Dealer");
			break;
		case "purchase.xhtml":
			disabled = session.isAuthorized("Purchase");
			break;
		case "purchase_update.xhtml":
			disabled = session.isAuthorized("Purchase");
			break;
		case "purchase_return.xhtml":
			disabled = session.isAuthorized("Purchase");
			break;
		case "sale.xhtml":
			disabled = session.isAuthorized("Sales");
			break;
		case "sale_update.xhtml":
			disabled = session.isAuthorized("Sales");
			break;
		case "sale_return.xhtml":
			disabled = session.isAuthorized("Sales");
			break;
		case "sale_dispatch.xhtml":
			disabled = session.isAuthorized("Sales");
			break;
		case "payment.xhtml":
			disabled = session.isAuthorized("Payments");
			break;
		case "receipt.xhtml":
			disabled = session.isAuthorized("Receipts");
			break;
		case "expense.xhtml":
			disabled = session.isAuthorized("Expenses");
			break;
		case "orderReport.xhtml":
			disabled = session.disableReport("Order Bookings");
			break;
		case "bookingReceiptReport.xhtml":
			disabled = session.disableReport("Booking Receipts");
			break;
		case "cancelledBookingReport.xhtml":
			disabled = session.disableReport("Cancelled Bookings");
			break;
		case "saleReport.xhtml":
			disabled = session.disableReport("Sale Deliveries");
			break;
		case "saleReceiptReport.xhtml":
			disabled = session.disableReport("Sale Receipts");
			break;
		case "saleReturnReport.xhtml":
			disabled = session.disableReport("Sale Returns");
			break;
		case "purchaseReport.xhtml":
			disabled = session.disableReport("Purchase Inwards");
			break;
		case "purchaseReturnReport.xhtml":
			disabled = session.disableReport("Purchase Returns");
			break;
		case "supplierPurchaseReport.xhtml":
			disabled = session.disableReport("Supplier - Purchases");
			break;
		case "supplierReturnReport.xhtml":
			disabled = session.disableReport("Supplier - Returns");
			break;
		default:
			disabled = false;
			break;
		}

		return !disabled;

	}

	private String getPage(String uri) {
		String[] uriFragments = uri.split("/");
		return uriFragments[uriFragments.length - 1];

	}

	private void invalidate(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (request.getRequestURI().contains("login.xhtml")
				|| request.getRequestURI().contains("javax.faces.resource")) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			response.sendRedirect(request.getContextPath()
					+ "/login.xhtml?error=true");
		}
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
