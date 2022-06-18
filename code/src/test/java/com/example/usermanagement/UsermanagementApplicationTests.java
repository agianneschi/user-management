package com.example.usermanagement;

import com.example.usermanagement.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsermanagementApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void testGetAllusers(){
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = testRestTemplate.exchange(getRootUrl() + "/user",
				HttpMethod.GET, entity, String.class);
		assertNotNull(response.getBody());
	}

	@Test
	public void testGetUserById() {
		UserDto userDto = testRestTemplate.getForObject(getRootUrl() + "/user/1", UserDto.class);
		System.out.println(userDto.getName());
		assertNotNull(userDto);
	}


	@Test
	public void testUpdateUser() {
		int id = 1;
		UserDto userDto = testRestTemplate.getForObject(getRootUrl() + "/user/" + id, UserDto.class);
		userDto.setName("Antony");
		userDto.setSurname("Verdi");
		testRestTemplate.put(getRootUrl() + "/user/" + id, userDto);
		UserDto updatedEUserDto = testRestTemplate.getForObject(getRootUrl() + "/user/" + id, UserDto.class);
		assertNotNull(updatedEUserDto);
	}
}
