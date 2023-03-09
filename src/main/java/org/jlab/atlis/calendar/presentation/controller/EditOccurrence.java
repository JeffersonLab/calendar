package org.jlab.atlis.calendar.presentation.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jlab.atlis.calendar.business.session.EventFacade;
import org.jlab.atlis.calendar.business.session.OccurrenceFacade;
import org.jlab.atlis.calendar.business.session.OccurrenceStyleChoiceFacade;
import org.jlab.atlis.calendar.persistence.entity.Event;
import org.jlab.atlis.calendar.persistence.entity.Occurrence;
import org.jlab.atlis.calendar.persistence.entity.OccurrenceStyleChoice;
import org.jlab.atlis.calendar.persistence.enumeration.Display;
import org.jlab.atlis.calendar.persistence.enumeration.Shift;
import org.jlab.atlis.calendar.presentation.converter.BigIntegerConverter;
import org.jlab.atlis.calendar.presentation.converter.DisplayConverter;
import org.jlab.atlis.calendar.presentation.converter.OccurrenceStyleChoiceConverter;
import org.jlab.atlis.calendar.presentation.converter.ShiftConverter;
import org.jlab.atlis.calendar.presentation.converter.YearMonthDayConverter;
import org.jlab.atlis.calendar.presentation.exception.CalendarException;
import org.jlab.atlis.calendar.presentation.exception.ConverterException;
import org.jlab.atlis.calendar.presentation.exception.ValidationException;
import org.jlab.atlis.calendar.presentation.utility.CalendarServletHelper;
import org.jlab.atlis.calendar.presentation.validator.DescriptionValidator;
import org.jlab.atlis.calendar.presentation.validator.DisplayValidator;
import org.jlab.atlis.calendar.presentation.validator.LiaisonValidator;
import org.jlab.atlis.calendar.presentation.validator.RemarkValidator;
import org.jlab.atlis.calendar.presentation.validator.ShiftValidator;
import org.jlab.atlis.calendar.presentation.validator.TitleValidator;
import org.jlab.atlis.calendar.presentation.validator.YearMonthDayValidator;

/**
 *
 * @author ryans
 */
@WebServlet(name = "EditOccurrence", urlPatterns = {"/edit-occurrence"})
public class EditOccurrence extends HttpServlet {

    @EJB
    private EventFacade eventFacade;
    @EJB
    private OccurrenceFacade occurrenceFacade;
    @EJB
    private OccurrenceStyleChoiceFacade styleChoiceFacade;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CalendarServletHelper.determineAndSetReturnPath(request);

        String occurrenceIdStr = request.getParameter("occurrenceId");
        String copyIdStr = request.getParameter("copyId");

        BigIntegerConverter bic = new BigIntegerConverter();

        Occurrence occurrence = null;

        if (occurrenceIdStr != null && !occurrenceIdStr.trim().isEmpty()) { // Edit existing
            BigInteger occurrenceId = null;

            try {
                occurrenceId = bic.getObject(occurrenceIdStr);
            } catch (ConverterException e) {
                throw new CalendarException("Unable to edit occurrence: Invalid occurrenceId: " + e.getMessage());
            }

            occurrence = occurrenceFacade.find(occurrenceId);

            if (occurrence == null) {
                throw new CalendarException("Unable to edit occurrence: Unable to locate occurrence with ID = " + occurrenceId);
            }
        } else if (copyIdStr != null && !copyIdStr.trim().isEmpty()) { // Create new by copying an occurrence
            BigInteger copyId = null;
            Occurrence copy = null;

            try {
                copyId = bic.getObject(copyIdStr);
            } catch (ConverterException e) {
                throw new CalendarException("Unable to edit occurrence: Invalid copyId: " + e.getMessage());
            }

            copy = occurrenceFacade.find(copyId);

            if (copy == null) {
                throw new CalendarException("Unable to edit occurrence: Unable to locate occurrence to copy with ID = " + copyId);
            }

            occurrence = new Occurrence(copy);
            occurrence.copyStyles(copy);

            occurrence.setShift(Shift.DAY); // Set default

        } else { // Create new
            Event event = null;

            try {
                event = convertAndValidateEvent(request);
            } catch (Exception e) {
                throw new CalendarException("Unable to create occurrence: " + e.getMessage());
            }

            occurrence = new Occurrence();
            occurrence.setEvent(event);
            occurrence.setDisplay(Display.SHOW); // Set default 
            occurrence.setShift(Shift.DAY); // Set default
        }

        List<OccurrenceStyleChoice> styleChoices = styleChoiceFacade.findAllInOrder();

        request.setAttribute("styleChoices", styleChoices);

        request.setAttribute("occurrence", occurrence);

        getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/edit-occurrence.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
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

        Map<String, String> messages = new HashMap<String, String>();
        request.setAttribute("messages", messages);

        Occurrence occurrence = convertAndValidateOccurrence(request);

        Event event = null;

        try {
            event = convertAndValidateEvent(request);
        } catch (Exception e) {
            messages.put("eventId", e.getMessage());
        }

        boolean create = (occurrence.getOccurrenceId() == null);

        if (!messages.isEmpty()) {
            String action = create ? "create" : "edit";
            messages.put("error", "Unable to " + action + " occurrence");

            List<OccurrenceStyleChoice> styleChoices = styleChoiceFacade.findAllInOrder();
            request.setAttribute("styleChoices", styleChoices);

            getServletConfig().getServletContext().getRequestDispatcher("/WEB-INF/views/edit-occurrence.jsp").forward(request, response);
            return;
        }

        if (create) { // Create New
            occurrence.setEvent(event);

            List<Occurrence> items = event.getOccurrenceList();

            if (items == null) {
                items = new ArrayList<Occurrence>();
            }

            items.add(occurrence);

            event.setOccurrenceList(items);

            Integer orderId = occurrenceFacade.findNextOrderId(occurrence.getEvent().getCalendar().getCalendarId(), occurrence.getYearMonthDay(), occurrence.getShift());
            occurrence.setOrderId(orderId);

            eventFacade.edit(event);

            //occurrenceFacade.create(occurrence);
        } else { // Edit Existing
            occurrenceFacade.edit(occurrence);
        }

        response.sendRedirect(returnPath);
    }

    private Event convertAndValidateEvent(HttpServletRequest request) throws ConverterException, ValidationException {
        String eventIdStr = request.getParameter("eventId");

        BigInteger eventId = null;
        Event event = null;

        if (eventIdStr != null && !eventIdStr.trim().isEmpty()) {

            BigIntegerConverter bic = new BigIntegerConverter();

            try {
                eventId = bic.getObject(eventIdStr);
            } catch (ConverterException e) {
                throw new ConverterException("Invaild eventId:  Not a number");
            }

            if (eventId != null) {
                event = eventFacade.findWithOccurrences(eventId);

                if (event == null) {
                    throw new ValidationException("Unable to locate event with ID = " + eventId);
                }
            }
        } else {
            throw new ValidationException("eventId must be specified");
        }

        return event;
    }

    private Occurrence convertAndValidateOccurrence(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, String> messages = (Map<String, String>) request.getAttribute("messages");

        Occurrence occurrence = null;

        String occurrenceIdStr = request.getParameter("occurrenceId");
        String yearMonthDayStr = request.getParameter("yearMonthDay");
        String shiftStr = request.getParameter("shift");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String liaison = request.getParameter("liaison");
        String displayStr = request.getParameter("display");
        String[] styleIdStrArray = request.getParameterValues("style");
        String remark = request.getParameter("remark");

        BigIntegerConverter bic = new BigIntegerConverter();
        YearMonthDayConverter ymdConverter = new YearMonthDayConverter();
        YearMonthDayValidator ymdValidator = new YearMonthDayValidator();
        ShiftConverter shiftConverter = new ShiftConverter();
        ShiftValidator shiftValidator = new ShiftValidator();
        TitleValidator titleValidator = new TitleValidator();
        DescriptionValidator descriptionValidator = new DescriptionValidator();
        LiaisonValidator liaisonValidator = new LiaisonValidator();
        DisplayConverter displayConverter = new DisplayConverter();
        DisplayValidator displayValidator = new DisplayValidator();
        OccurrenceStyleChoiceConverter oscc = new OccurrenceStyleChoiceConverter();
        RemarkValidator remarkValidator = new RemarkValidator();

        BigInteger occurrenceId = null;
        Date yearMonthDayObj = null;
        Shift shiftObj = null;
        Display displayObj = null;
        List<OccurrenceStyleChoice> styles = null;

        if (occurrenceIdStr != null && !occurrenceIdStr.trim().isEmpty()) {
            try {
                occurrenceId = bic.getObject(occurrenceIdStr);
            } catch (ConverterException e) {
                messages.put("occurrenceId", e.getMessage());
            }
        }

        try {
            yearMonthDayObj = ymdConverter.getObject(yearMonthDayStr);
            ymdValidator.validate(yearMonthDayObj);
        } catch (ConverterException e) {
            messages.put("yearMonthDay", e.getMessage());
        } catch (ValidationException e) {
            messages.put("yearMonthDay", e.getMessage());
        }

        try {
            shiftObj = shiftConverter.getObject(shiftStr);
            shiftValidator.validate(shiftObj);
        } catch (ConverterException e) {
            messages.put("shift", e.getMessage());
        } catch (ValidationException e) {
            messages.put("shift", e.getMessage());
        }

        try {
            titleValidator.validate(title);
        } catch (ValidationException e) {
            messages.put("title", e.getMessage());
        }

        try {
            descriptionValidator.validate(description);
        } catch (ValidationException e) {
            messages.put("description", e.getMessage());
        }

        try {
            liaisonValidator.validate(liaison);
        } catch (ValidationException e) {
            messages.put("liaison", e.getMessage());
        }

        try {
            displayObj = displayConverter.getObject(displayStr);
            displayValidator.validate(displayObj);
        } catch (ConverterException e) {
            messages.put("display", e.getMessage());
        } catch (ValidationException e) {
            messages.put("display", e.getMessage());
        }

        try {
            styles = oscc.getObjects(styleIdStrArray);
        } catch (ConverterException e) {
            messages.put("style", e.getMessage());
        }

        try {
            remarkValidator.validate(remark);
        } catch (ValidationException e) {
            messages.put("remark", e.getMessage());
        }

        if (occurrenceId != null) {
            occurrence = occurrenceFacade.find(occurrenceId);

            if (occurrence == null) {
                messages.put("error", "Unable to find occurrence with ID: " + occurrenceId);
                occurrence = new Occurrence();
            }
        } else {
            occurrence = new Occurrence();
        }

        occurrenceFacade.updateStyles(styles, occurrence);

        occurrence.setYearMonthDay(yearMonthDayObj);
        occurrence.setShift(shiftObj);
        occurrence.setTitle(title);
        occurrence.setDescription(description);
        occurrence.setLiaison(liaison);
        occurrence.setDisplay(displayObj);
        occurrence.setRemark(remark);

        return occurrence;
    }
}
