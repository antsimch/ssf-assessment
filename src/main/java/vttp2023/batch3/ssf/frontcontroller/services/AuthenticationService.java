package vttp2023.batch3.ssf.frontcontroller.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2023.batch3.ssf.frontcontroller.respositories.AuthenticationRepository;

@Service
public class AuthenticationService {

	AuthenticationRepository authRepo;

	public AuthenticationService(AuthenticationRepository authRepo) {
        this.authRepo = authRepo;
    }

    // TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here
	public void authenticate(String username, String password) throws Exception {

		String apiUrl = "https://authservice-production-e8b2.up.railway.app/api/authenticate"; 

		RestTemplate restTemplate = new RestTemplate();

		JsonObject obj = Json.createObjectBuilder()
				.add("username", username)
				.add("password", password)
				.build();

		RequestEntity<String> req = RequestEntity
				.post(apiUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Accept", "application/json")
				.body(obj.toString(), String.class);

		ResponseEntity<String> resp = restTemplate.exchange(req, String.class);
		
		HttpStatusCode respStatus = resp.getStatusCode();

		System.out.println(respStatus.toString());

		if (!respStatus.equals(HttpStatus.ACCEPTED)) {
			if (respStatus.equals(HttpStatus.BAD_REQUEST)) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
			} else if (respStatus.equals(HttpStatus.UNAUTHORIZED)) {
				throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
			}
		}
	}

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
		authRepo.disableUser(username);
	}

	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		return authRepo.isLocked(username);
	}
}
