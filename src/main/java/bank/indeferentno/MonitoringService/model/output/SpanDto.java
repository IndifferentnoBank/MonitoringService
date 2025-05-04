package bank.indeferentno.MonitoringService.model.output;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class SpanDto {
    private String spanId;
    private String parentSpanId;
    private String serviceName;
    private String operationName;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Integer durationMs;
    private Map<String, String> tags = new HashMap<>();
}
