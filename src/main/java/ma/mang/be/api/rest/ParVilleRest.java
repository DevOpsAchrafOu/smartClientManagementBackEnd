/**
 * 
 */
package ma.mang.be.api.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.mang.be.api.dto.ParVilleDto;
import ma.mang.be.api.model.ParVille;
import ma.mang.be.api.service.ParVilleService;

/**
 * REST Api to manipulate ParVille objects
 * @author achraf
 * @version v0.3
 */
@RestController
@RequestMapping("api/mang/v1")
@Api(tags = { "02 - ParVilles : REST Interfaces to manipulate ParVille objects" })
public class ParVilleRest {
	
	private static final String MSG_FAILED_REQUEST= "Request failed. Please try later!";
	private static final String MSG_VILLE_CREATED = "ParVille successfully created.";
	private static final String MSG_VILLE_UPDATED = "ParVille successfully updated.";

	@Autowired
	private ParVilleService villeService;

	

	@PostMapping("/villes")
	@ApiOperation(notes = "Creates a ville ", value = "", response = ParVilleDto.class)
	public ResponseEntity<?> addParVille(@RequestBody ParVilleDto villeDetails)
			throws Exception {
		ParVille m =null;
		try {
		 m = villeService.save(ParVilleDto.to(villeDetails));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);//.body(new ResponseMessageDto("ParVille creation failed.",HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage() + "\n" + e.getCause()));
		}
		return ResponseEntity.ok(ParVilleDto.from(m));
	}
	
	@GetMapping("/villes")
	@ApiOperation(notes = "Retrieves all ville", value = "", response = ParVille.class)
	public ResponseEntity<List<ParVilleDto>> getParVilles() throws Exception {
		List<ParVille> villes = villeService.getAllParVilles();
		return ResponseEntity.ok(ParVilleDto.from(villes));
	}


	@GetMapping("/villes/{id}")
	@ApiOperation(notes = "Retrieves ville by ID", value = "", response = ParVille.class)
	public ResponseEntity<ParVilleDto> getParVilleById(@PathVariable(value = "id") Long villeId) throws Exception {
		ParVille ville = villeService.getParVilleById(villeId);
		if (ville == null) {
			throw new Exception("ParVille not found for this id :: " + villeId);
		}
	
		return ResponseEntity.ok(ParVilleDto.from(ville));
	}
	
	@GetMapping("/villes/pays/{id}")
	@ApiOperation(notes = "Retrieves ville by ID", value = "", response = ParVille.class)
	public List<ParVilleDto>  getParVilleByPaysId(@PathVariable(value = "id") Long id) throws Exception {
		List<ParVille> villes = villeService.getParVilleByPaysId(id);
		if (villes == null) {
			throw new Exception("ParVille not found for this pays id :: " + id);
		}
	
		return ParVilleDto.from(villes);
	}


	@PutMapping("/villes/{id}")
	@ApiOperation(notes = "Updates a ville identified by ID", value = "", response = String.class)
	public ResponseEntity<?> updateParVille(@PathVariable(value = "id") Long villeId, @RequestBody ParVilleDto villeDetails)
			throws Exception {
		ParVille ville = villeService.getParVilleById(villeId);
		String msg="";
		if(ville==null) {
			new Exception("ParVille not found for this id :: " + villeId);
		}
		ParVille m = villeService.save(ParVilleDto.to(villeDetails));
		if(m!=null) {
			msg = MSG_VILLE_UPDATED;
		}else {
			throw new Exception(MSG_FAILED_REQUEST);
		}
		return ResponseEntity.ok(msg);
	}
	

	@DeleteMapping("/villes/{id}")
	@ApiOperation(notes = "Deletes a ville identified by ID from database", value = "", response = String.class)
	public ResponseEntity<?> deleteParVille(@PathVariable(value = "id") Long villeId) throws Exception {
		ParVille ville = villeService.getParVilleById(villeId);
		if(ville==null) {
			new Exception("ParVille not found for this id :: " + villeId);
		}
		villeService.delete(villeId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
