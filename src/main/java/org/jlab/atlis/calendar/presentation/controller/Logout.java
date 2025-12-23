package org.jlab.atlis.calendar.presentation.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.jlab.atlis.calendar.presentation.utility.CalendarServletHelper;

/**
 * @author ryans
 */
@WebServlet(
    name = "Logout",
    urlPatterns = {"/logout"})
public class Logout extends HttpServlet {

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String returnPath = CalendarServletHelper.determineAndSetReturnPath(request);

    request.logout();
    request.getSession().invalidate();
    response.sendRedirect(returnPath);
  }
}
