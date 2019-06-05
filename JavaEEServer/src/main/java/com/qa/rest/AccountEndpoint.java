package com.qa.rest;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.qa.model.Account;
import com.qa.repository.AccountRepository;

@Path("/")
public class AccountEndpoint {

	@Inject
	private AccountRepository accountRepository;
	
	@GET
	@Path("/accounts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		List<Account> list = accountRepository.readAll();
		if (list.size() == 0) {
			return Response.noContent().build();
		}
		return Response.ok(list).build();
	}
	
	@GET
	@Path("/accounts/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOne(@PathParam("id") int id) {
		if (accountRepository.read(id).equals(null)){
			return Response.status(Status.NOT_FOUND).build();
		}
		Account account = accountRepository.read(id);
		return Response.ok(account).build();
	}
	
	@POST
	@Consumes({"application/json"})
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/accounts")
	public Response addAccount(Account accountRS, @Context UriInfo uriInfo) {
		accountRS = accountRepository.create(accountRS);
		URI createdURI = uriInfo.getBaseUriBuilder().path(""+accountRS.getId()).build();
		System.out.println(createdURI);
		return Response.ok(createdURI.toString()).status(Status.CREATED).build();
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({"application/json"})
	@Path("/accounts/{id}")
	public Response updateAccount(Account account, @PathParam("id") int id) {
		if (accountRepository.read(id).equals(null)){
			return Response.status(Status.NOT_FOUND).build();
		}
		Account accountRS2 = accountRepository.update(id, account);
		return Response.ok(accountRS2).build();
	}
	
	@DELETE
	@Path("/accounts/{id}")
	public Response deleteAccount(@PathParam("id") int id) {
		if (accountRepository.read(id).equals(null)){
			return Response.status(Status.NOT_FOUND).build();
		}
		accountRepository.delete(id);
		return Response.noContent().build();
	}
	
	
	
	
	
}
