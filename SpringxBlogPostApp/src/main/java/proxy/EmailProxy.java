package proxy;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "emails", url= "${name.spring.url}", fallback = EmailFallback.class )
public interface EmailProxy { // ALl the calls to the email microservice service endpoints should gp from here

	@PostMapping("/api/users/write-access")
	ResponseEntity<Void> getWriteAccess(@RequestHeader String requestId, @RequestBody Map<String, String> payload);
	
	@GetMapping("/api/users/write-access/confirmation")
	ResponseEntity<Map<String, String>> getConfirmation(@RequestHeader(required = false) String requestId, @RequestParam("token") String token);
	
}


@Component
class EmailFallback implements EmailProxy {
    @Override
    public ResponseEntity<Void> getWriteAccess(String requestId, Map<String, String> payload) {
        System.out.println("Fallback triggered. Payload: " + payload);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

	@Override
	public ResponseEntity<Map<String, String>> getConfirmation(String requestId, String token) {
		 System.out.println("Fallback triggered, Failed to Grant Permission");
	      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
	}
}
