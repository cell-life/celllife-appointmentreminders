package org.celllife.appointmentreminders.interfaces.service;

import org.celllife.appointmentreminders.application.utils.FindMessageStatusesStoredProc;
import org.celllife.pconfig.model.Pconfig;
import org.celllife.reporting.service.PconfigParameterHtmlService;
import org.celllife.reporting.service.ReportService;
import org.celllife.reporting.service.impl.PconfigParameterHtmlServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

@Controller
public class ReportsController {

    @Autowired
    private ReportService reportService;

    @Autowired
    FindMessageStatusesStoredProc findMessageStatusesStoredProc;

    private PconfigParameterHtmlService pconfigParameterHtmlService = new PconfigParameterHtmlServiceImpl();

    private static Logger log = LoggerFactory.getLogger(ReportsController.class);

    @ResponseBody
    @RequestMapping(
            value = "/service/reports",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Collection<Pconfig> getReports() {
        return reportService.getReports();
    }

    //TODO: need to find a more sophisticated way to do this, not just hard-coding html.
    @ResponseBody
    @RequestMapping(value = "/service/getHtml", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getHtmlForReport(@RequestParam("reportId") String reportId) {
        Pconfig pconfig;
            try {
                pconfig = reportService.getReportByName(reportId);
            } catch (Exception e) {
                return "No such Report.";
            }
            String htmlString = pconfigParameterHtmlService.createHtmlFieldsFromPconfig(pconfig, "submitButton");
            return htmlString;
    }

    @ResponseBody
    @RequestMapping(value = "/service/downloadMessageDetailReport", method = RequestMethod.GET)
    public void downloadCSVReport(HttpServletRequest request, HttpServletResponse response, @RequestParam("clinicId") String clinicId) {

        Map<String, Object> parameters = new HashMap<>();
        List<HashMap<String, Object>> rows;
        Integer numberOfRows;

        parameters.put("clinicId", clinicId);
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
                log.warn("No data found for message report of clinic. Could not generate report." + clinicId);
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