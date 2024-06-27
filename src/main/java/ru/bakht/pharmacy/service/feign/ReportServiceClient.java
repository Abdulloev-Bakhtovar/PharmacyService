package ru.bakht.pharmacy.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bakht.pharmacy.service.enums.ReportType;

@FeignClient(name = "REPORT-SERVICE")
public interface ReportServiceClient {

    @PostMapping("/report/record")
    void recordReportRequest(@RequestParam("reportName") ReportType reportName);
}

