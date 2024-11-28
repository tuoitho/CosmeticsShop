package com.cosmeticsellingwebsite.service.google;


import com.cosmeticsellingwebsite.dto.gooogle.GooglePojo;
import com.cosmeticsellingwebsite.dto.gooogle.GoogleProperties;
import com.cosmeticsellingwebsite.dto.gooogle.google.GoogleTokenRequest;
import com.cosmeticsellingwebsite.dto.gooogle.google.GoogleTokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleService {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private GoogleProperties ggprop;

	public String getToken(final String code) throws Exception {
		String link = ggprop.getTokenUrl();

		GoogleTokenRequest tokenRequest = new GoogleTokenRequest(ggprop.getClientId(),
				ggprop.getClientSecret(), ggprop.getRedirectUri(), code);

		String response = restTemplate.postForObject(link, tokenRequest, String.class);

		return extractAccessToken(response);
	}

	private String extractAccessToken(String response) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		GoogleTokenResponse tokenResponse = mapper.readValue(response, GoogleTokenResponse.class);
		return tokenResponse.getAccessToken();
	}

	public GooglePojo getUserInfo(final String accessToken) throws Exception {
		String link = ggprop.getUserInfoUrl() + accessToken;
		String response = restTemplate.getForObject(link, String.class);
		return objectMapper.readValue(response, GooglePojo.class);
	}
}
