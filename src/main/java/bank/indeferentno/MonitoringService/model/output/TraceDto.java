package bank.indeferentno.MonitoringService.model.output;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TraceDto {
    private String traceId;
    private List<SpanDto> spans;
}

