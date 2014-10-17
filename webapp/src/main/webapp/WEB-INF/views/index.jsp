<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Appointment Reminders</title>

    <c:set var="url">${pageContext.request.requestURL}</c:set>
    <base href="${fn:substring(url, 0, fn:length(url) - fn:length(pageContext.request.requestURI))}${pageContext.request.contextPath}/" />

    <link href="resources/css/bootstrap-3.0.2.css" rel="stylesheet" media="screen">
    <link href="resources/css/bootstrap-theme-3.0.2.css" rel="stylesheet">
    <script type="text/javascript" src="resources/js/jquery-1.8.2.js"></script>
    <script type="text/javascript" src="resources/js/jquery-ui-1.9.1.min.js"></script>
    <script type="text/javascript" src="resources/js/bootstrap-3.0.2.js"></script>
</head>
<body>

<div class="container">

    <div class="masthead">
        <ul class="nav nav-pills pull-right">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="j_spring_cas_security_logout">Logout</a>
        </ul>
         <h2><img src="resources/img/logo.png"></h2>
        <h3 class="muted">Appointment Reminders</h3>
    </div>

    <hr>

    <h1>Reports</h1>

    <div class="row">
        <div class="col-md-4">
            <ul>
                <li><a href="#message_detail_report" class="active csvReportLink">Message Detail (CSV) Report</a></li>
                <li><a href="#message_summary_report" class="active pdfReportLink">Message Summary Report</a></li>
            </ul>
        </div>

        <div class="col-md-8 form">
                <!-- the parameters for the report will be inserted here when they click on the above link -->
        </div>
    </div>
    
    <hr>

    <div class="footer">
        <jsp:useBean id="date" class="java.util.Date" />
        <p>&copy; Cell-Life (NPO) - <fmt:formatDate value="${date}" pattern="yyyy" /></p>
    </div>

</div>

<script>
    $(document).ready(function () {
        $(".pdfReportLink").click(function () {
            var reportId = $(this).attr('href') + '';
            reportId = reportId.replace('#', '');
            $.get("service/getHtml",
                    {reportId: reportId},
                    function (data) {
                        $(".form").html(data);
                        $(".form").on('submit', function (e) {
                            e.preventDefault();
                            window.location = "service/pdfReport" + "?reportId=" + reportId + "&" + $("form").serialize();
                        });
                    }
            );
        });
        $(".csvReportLink").click(function () {
            var reportId = $(this).attr('href') + '';
            reportId = reportId.replace('#', '');
            $.get("service/getHtml",
                    {reportId: reportId},
                    function (data) {
                        $(".form").html(data);
                        $(".form").on('submit', function (e) {
                            e.preventDefault();
                            window.location = "service/downloadMessageDetailReport" + "?reportId=" + reportId + "&" + $("form").serialize();
                        });
                    }
            );
        });
    });
</script>

</body>
</html>