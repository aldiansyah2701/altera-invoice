package com.altera.invoice.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.altera.invoice.entity.Currency;
import com.altera.invoice.entity.Invoice;
import com.altera.invoice.entity.Invoice.STATUS;
import com.altera.invoice.entity.Product;
import com.altera.invoice.entity.User;
import com.altera.invoice.message.BaseResponse;
import com.altera.invoice.message.RequestCreateInvoice;
import com.altera.invoice.repository.CurrencyRepository;
import com.altera.invoice.repository.InvoiceRepository;
import com.altera.invoice.repository.ProductRepository;
import com.altera.invoice.repository.RoleRepository;
import com.altera.invoice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InvoiceService {
	
	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	public ResponseEntity<Object> createInvoice(RequestCreateInvoice request) {
		BaseResponse response = new BaseResponse();
		Invoice invoice = invoiceRepository.findByBusinessKey(request.getBusinessKey());
		if (invoice != null) {
			response.setMessage(BaseResponse.ALREADY_EXIST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			Invoice data = new Invoice();
			data.setCreatedDate(new Date());
			data.setBusinessKey(request.getBusinessKey());
			data.setBuyerCode(request.getBuyerName());
			data.setSellerCode(request.getSellerName());
			data.setCreatedDate(new Date());
			data.setStatus(STATUS.PENDING.toString());
			Optional<Product> product = productRepository.findById(request.getProductUuid());
			Optional<Currency> currency = currencyRepository.findById(request.getCurrencyUuid());
			Optional<User> user = userRepository.findById(request.getUserUuid());

			data.setUser(user.get());
			data.setCurrency(currency.get());
			data.setProduct(product.get());

			data.setAmount(Double.parseDouble(request.getAmount()));

			data = invoiceRepository.save(data);

			response.setMessage(BaseResponse.SUCCESS);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<Object> getInvoiceDetail(String businessKey) {
		BaseResponse response = new BaseResponse();

		Invoice invoice = invoiceRepository.findByBusinessKey(businessKey);
		if (invoice == null) {
			response.setMessage(BaseResponse.NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(invoice, HttpStatus.OK);
	}

	public ResponseEntity<Object> getInvoices(String userUuid, Pageable pageable) {
		BaseResponse response = new BaseResponse();

		try {
			Optional<User> user = userRepository.findById(userUuid);
			Page<Invoice> invoices = invoiceRepository.findByUser(user.get(), pageable);
			return new ResponseEntity<>(invoices, HttpStatus.OK);
		} catch (Exception e) {
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<Object> updateInvoice(String businessKey, String status) {
		BaseResponse response = new BaseResponse();

		try {
			Invoice invoice = invoiceRepository.findByBusinessKey(businessKey);
			if (invoice == null) {
				response.setMessage(BaseResponse.NOT_FOUND);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			invoice.setStatus(STATUS.valueOf(status).toString());
			invoice = invoiceRepository.save(invoice);
			return new ResponseEntity<>(invoice, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}

	}

}
