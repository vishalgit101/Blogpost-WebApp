package controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import entity.User;
import model.UserPrincipal;
import proxy.EmailProxy;
import repositories.UserRepository;
import services.UserService;

@RestController
public class ContactAdminController {
	// DI 
	private final EmailProxy emailProxy;
	private final UserRepository userRepository; // should be moved in the UserService Class
	private final UserService userService;
	@Autowired
	public ContactAdminController(EmailProxy emailProxy, UserRepository userRepository, UserService userService) {
		super();
		this.emailProxy = emailProxy;
		this.userRepository = userRepository;
		this.userService = userService;
	}



	@PostMapping("/api/write-permission")
	public ResponseEntity<Void> writeAccess(@RequestBody Map<String, String> payload, @AuthenticationPrincipal UserPrincipal userPrincipal){
		System.out.println("Contact Admin Controller...");
		System.out.println("Payload: " + payload);
		
		String userOwnEmail = userPrincipal.getUsername();
		User user = this.userRepository.findByUsername(userOwnEmail);
		
		String userId =  String.valueOf(user.getId()) ;
		payload.put("userId", userId);
		
		// paylaod from the frontend already has user details like email, phone number etc
		// we also append requesting users own email
		payload.put("userOwnEmail", userOwnEmail);
		String requestId = UUID.randomUUID().toString();
		// have the proxy interface where you pass the request id
		return this.emailProxy.getWriteAccess(requestId, payload);
	}
	
	@GetMapping("/api/write-access/confirmation")
	public ResponseEntity<String> confirmWriteRequest(@RequestParam("token") String token){
		System.out.println("COntact Admin COntroller for write acess confirmation hit");
		// generate the requestId
		String requestId = UUID.randomUUID().toString();
		
		System.out.println("Sending Feign call to email service with token: " + token + " and requestId: " + requestId);

		
		// pass the requestId and token to forward the request to email service
		ResponseEntity<Map<String, String>> confirmation = this.emailProxy.getConfirmation(requestId, token);
		// response will be stored in the confirmation
		
		// get the email for which write access was required
		String userAccessEmail = confirmation.getBody().get("userAccessEmail");
		String userOwnEmail = confirmation.getBody().get("userOwnEmail"); // users owm email
		
		// use service method to check if the user with that email exists in the dB
		boolean success = this.userService.grantWritePermission(userAccessEmail);
		
		// if no throw an exception and if yes then grant the permission
		if(!success) {
			System.out.println("Contact controller no user with that email exist in the dB");
			// Response Email to user that their request failed
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No User with that email exists in the dB"); // err 400
		}
		System.out.println("Permission Granted Successfully: From the Contact controller");
		
		// after granting permission and request to the email service should be sent
		//Response Email to to user for if request granted
		String requestId2 = UUID.randomUUID().toString();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userOwnEmail", userOwnEmail);
		
		this.emailProxy.accessGranted(requestId2, map);
		// which would send a mail to the user that their request has been confirmed
		
		return ResponseEntity.status(HttpStatus.OK).body("Permission Successfully granted and response sent");

	}
	
	@GetMapping("/api/admin/authors")
	public ResponseEntity<List<Map<String,String>>> getWriters(){
		List<User> users = this.userService.allAuthors();
		
		List<Map<String,String>> body = new ArrayList<Map<String,String>>();
		
		for(User user : users) {
			Map<String, String> userDetails = new HashMap<String, String>();
			userDetails.put("name", user.getUsername());
			userDetails.put("email", user.getEmail());
			body.add(userDetails);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}
	
	@PostMapping("/api/admin/authors/revoke")
	public ResponseEntity<Void> revokeWriteAccess(@RequestParam("email") String email) {
		this.userService.revokeWriteAccess(email);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
