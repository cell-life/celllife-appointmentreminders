package org.celllife.appointmentreminders.interfaces.service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.celllife.appointmentreminders.application.utils.FindMessageStatusesStoredProc;
import org.celllife.pconfig.model.DateParameter;
import org.celllife.pconfig.model.Pconfig;
import org.celllife.pconfig.model.StringParameter;
import org.celllife.reporting.service.PconfigParameterHtmlService;
import org.celllife.reporting.service.ReportService;
import org.celllife.reporting.service.impl.PconfigParameterHtmlServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

@Controller
public class CSVReportsController {

    @Autowired
    FindMessageStatusesStoredProc findMessageStatusesStoredProc;
    
    @Autowired
    private ReportService reportService;

    private PconfigParameterHtmlService pconfigParameterHtmlService = new PconfigParameterHtmlServiceImpl();

    private static Logger log = LoggerFactory.getLogger(CSVReportsController.class);

    // FIXME: add this to the reportsController so it can be re-used
    @ResponseBody
    @RequestMapping(value = "/service/downloadMessageDetailReport", method = RequestMethod.GET)
    public void downloadCSVReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String reportId = request.getParameter("reportId");
        if (reportId.isEmpty()) {
            throw new RuntimeException("Could not retrieve a report with an empty reportId.");
        }

        Pconfig pconfig = reportService.getReportByName(reportId);
        Pconfig returnedPconfig = pconfigParameterHtmlService.createPconfigFromHtmlFormSubmission(
                request.getParameterNames(), request.getParameterMap(), pconfig);
        
        String facilityCode = ((StringParameter)returnedPconfig.getParameter("facility_code")).getValue();
        Date startDate = ((DateParameter)returnedPconfig.getParameter("start_date")).getValue();
        Date endDate = ((DateParameter)returnedPconfig.getParameter("end_date")).getValue();

        Map<String, Object> parameters = new HashMap<>();
        List<HashMap<String, Object>> rows;
        Integer numberOfRows;

        parameters.put("clinicId", facilityCode); // FIXME: use facilityCode + start and end dates in stored procedure
        Map<String, Object> results;
        try {
            results = findMessageStatusesStoredProc.execute(parameters);
            rows = (List<HashMap<String, Object>>) results.get("#result-set-1");
            numberOfRows = rows.get(0).size();
        } catch (DataIntegrityViolationException e) {
            rows = new ArrayList<>();
            numberOfRows = 0;
        }

        try {

            if (numberOfRows > 0) {

                response.setContentType("application/csv");
                response.setHeader("Content-Disposition", "attachment; filename=\"messageReport" + new Date() + ".csv\"");

                ICsvMapWriter writer = new CsvMapWriter(new OutputStreamWriter(new BufferedOutputStream(response.getOutputStream())), CsvPreference.STANDARD_PREFERENCE);

                final String[] header = new String[numberOfRows];
                rows.get(0).keySet().toArray(header);
                writer.writeHeader(header);

                for (Map<String, Object> row : rows) {
                    writer.write(row, header, getProcessors(numberOfRows));
                }
                writer.close();

            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                log.warn("No data found for message report of clinic. Could not generate report for facility with code "
                        + facilityCode + " between " + startDate + " and " + endDate);
            }

        } catch (IOException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.warn("Could not send csv report.", e);
        }

    }

    private static CellProcessor[] getProcessors(Integer size) {
        final CellProcessor[] processors = new CellProcessor[size];
        for (int i=0; i< size; i++) {
            processors[i] = new Optional();
        }
        return processors;
    }
}