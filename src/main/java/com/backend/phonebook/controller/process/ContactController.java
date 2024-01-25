package com.backend.phonebook.controller.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.phonebook.message.RequestCreateContact;
import com.backend.phonebook.message.RequestRegisterUser;
import com.backend.phonebook.service.ContactService;

@RestController
@RequestMapping("/api/v1/phonebook")
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	
	@PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPERVISOR')")
	public ResponseEntity<Object> createContact(@RequestBody RequestCreateContact request) {
		return contactService.createContact(request);
	}

	@GetMapping(value = "/search")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPERVISOR') or hasAuthority('ROLE_CUSTOMERSERVICE')")
	public ResponseEntity<Object> getInvoice(@RequestParam("search") String search, Pageable pageable) {
		return contactService.getSearchContact(search, pageable);
	}

	@GetMapping(value = "/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPERVISOR') or hasAuthority('ROLE_CUSTOMERSERVICE')")
	public ResponseEntity<Object> getInvoices(Pageable pageable) {
		return contactService.getContact(pageable);
	}

	@PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPERVISOR')")
	public ResponseEntity<Object> updateUser(@RequestParam("uuid") String uuid, @RequestBody RequestCreateContact request) {
		return contactService.updateContact(uuid, request);
	}
	
	@DeleteMapping(value = "/delete")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Object> deleteUser(@RequestParam("uuid") String uuid) {
		return contactService.deleteContact(uuid);
	}

}
