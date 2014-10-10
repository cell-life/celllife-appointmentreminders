package org.celllife.appointmentreminders.application.utils;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;

public class FindMessageStatusesStoredProc extends StoredProcedure {

    /**
     * This constructor needs to be used in a spring xml file. In this case, see spring-jdbc.xml.
     * @param dataSource The datasource that contains the stored procedure.
     */
    public FindMessageStatusesStoredProc(DataSource dataSource) {
        super(dataSource,"FindMessageStatuses");
        SqlParameter parameter = new SqlParameter("clinicId", Types.BIGINT);
        SqlParameter[] parameters = {parameter};
        this.setParameters(parameters);
        compile();
    }

}
