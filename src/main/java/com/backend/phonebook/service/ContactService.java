package com.backend.phonebook.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.phonebook.entity.Contact;
import com.backend.phonebook.entity.User;
import com.backend.phonebook.message.BaseResponse;
import com.backend.phonebook.message.RequestCreateContact;
import com.backend.phonebook.repository.ContactRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ContactService {
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	public ResponseEntity<Object> createContact(RequestCreateContact request) {
		BaseResponse response = new BaseResponse();
		Contact data = contactRepository.findByPhoneNumber(request.getPhoneNumber());
		if (data != null) {
			response.setMessage(BaseResponse.ALREADY_EXIST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		Contact saveData = new Contact();
		saveData.setActive(true);
		saveData.setCreatedDate(new Date());
		saveData.setName(request.getName());
		saveData.setPhoneNumber(request.getPhoneNumber());
		
		contactRepository.save(saveData);
		response.setMessage(BaseResponse.SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	public ResponseEntity<Object> getContact(Pageable pageable) {
		Page<Contact> data = contactRepository.findAll(pageable);
		
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getSearchContact(String search, Pageable pageable) {
		Page<Contact> data = contactRepository.findByNameContainingOrPhoneNumberContaining(search,search,pageable);	
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> updateContact(String uuid, RequestCreateContact request) {
		BaseResponse response = new BaseResponse();
		Optional<Contact> data = contactRepository.findById(uuid);
		
		if(!data.isPresent()){
			response.setMessage(BaseResponse.NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		Contact updateData = data.get();
		updateData.setName(request.getName());
		updateData.setPhoneNumber(request.getPhoneNumber());
		
		contactRepository.save(updateData);
		response.setMessage(BaseResponse.SUCCESS);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Transactional(readOnly = false)
	public ResponseEntity<Object> deleteContact(String uuid) {
		BaseResponse response = new BaseResponse();
		Optional<Contact> data = contactRepository.findById(uuid);

		if(data.isPresent()){
			Contact deleteData = data.get();
			deleteData.setActive(false);
			contactRepository.save(deleteData);
			response.setMessage(BaseResponse.SUCCESS);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
		response.setMessage(BaseResponse.NOT_FOUND);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
}
