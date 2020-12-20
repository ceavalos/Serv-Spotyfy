package innotec.com.sv.controllers;




import java.util.Base64;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = { "http://localhost:4200" })
@Controller
public class spotifyResponse {

	
	final static String CLIENT_ID_SECRET = "b8389dafa4a94132be46aa00ba334941";
	final static String CLIENTSECRET = "f1515deccde84caaa3fb7d6efa143f04";
	
	
	@RequestMapping("/")
	public String raiz(Model modelo) {
		System.out.println("En inicio de pagina");
		modelo.addAttribute("mensaje","Bienvenido al servidor Post-apy Spoty-id");	
		return "index";
	}
	
	@GetMapping(value="/spotify-response",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object spotifyResponse() {
		//
		System.out.println("Dentro de :  /spotify-response");
		//
		 RestTemplate restTemplate = new RestTemplate();

		    HttpHeaders httpHeaders = new HttpHeaders();
		    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		    String usernamePassword = CLIENT_ID_SECRET+":"+CLIENTSECRET;
		    String basicToken = Base64.getEncoder().encodeToString(usernamePassword.getBytes());
		    httpHeaders.set("Authorization", "Basic "+basicToken);

		    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		    requestBody.add("grant_type", "client_credentials");
		    //requestBody.add("code", code.trim());
		    //requestBody.add("redirect_uri", "http://localhost:11001/api/spotify-response");
		
		    HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
		    
		    try {
		    	Object response = restTemplate.postForObject("https://accounts.spotify.com/api/token", httpEntity, Object.class);
		    	//System.out.println(response);
		    	Map<String, Object> map = (Map<String, Object>) response;
		    	map.forEach((k, v) -> System.out.println(k+": "+v));
		    	//httpServletResponse.setStatus(302);
		    	  String accessToken = (String) map.get("access_token");
		          String token_type = (String) map.get("token_type");
		          int expires_in = (int) map.get("expires_in");
		          
		    	return map;
		    	
			} 
		    catch(HttpClientErrorException e) {
		        e.printStackTrace();
		    }
			return httpEntity.toString().concat("Error de Coden");
		    
	}
}
