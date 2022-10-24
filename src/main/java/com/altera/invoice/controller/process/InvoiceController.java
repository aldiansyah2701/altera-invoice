package com.altera.invoice.controller.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altera.invoice.message.RequestCreateInvoice;
import com.altera.invoice.service.InvoiceService;

@RestController
@RequestMapping("/api/v1/invoice")
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;
	
	@PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPERVISOR') or hasAuthority('ROLE_CUSTOMERSERVICE')")
	public ResponseEntity<Object> createInvoice(@RequestBody RequestCreateInvoice request) {
		return invoiceService.createInvoice(request);
	}

	@GetMapping(value = "/{businessKey}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPERVISOR') or hasAuthority('ROLE_CUSTOMERSERVICE')")
	public ResponseEntity<Object> getInvoice(@PathVariable("businessKey") String businessKey) {
		return invoiceService.getInvoiceDetail(businessKey);
	}

	@GetMapping(value = "/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPERVISOR') or hasAuthority('ROLE_CUSTOMERSERVICE')")
	public ResponseEntity<Object> getInvoices(@RequestParam String userUuid, Pageable pageable) {
		return invoiceService.getInvoices(userUuid, pageable);
	}

	@PutMapping(path = "/{businessKey}/{status}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPERVISOR')")
	public ResponseEntity<Object> updateInvoice(@PathVariable("businessKey") String businessKey,
			@PathVariable("status") String status) {
		return invoiceService.updateInvoice(businessKey, status);
	}
}
