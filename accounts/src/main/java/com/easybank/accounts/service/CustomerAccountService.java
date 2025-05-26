package com.easybank.accounts.service;

import com.easybank.accounts.dto.AccountsDto;
import com.easybank.accounts.dto.CustomerDto;
import com.easybank.accounts.entity.Accounts;
import com.easybank.accounts.entity.Customer;
import com.easybank.accounts.mapper.AccountsMapper;
import com.easybank.accounts.mapper.CustomerMapper;
import com.easybank.accounts.repository.AccountsRepository;
import com.easybank.accounts.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.easybank.commons.constants.AccountConstants;
import com.easybank.commons.exception.CustomerAlreadyExistsException;
import com.easybank.commons.exception.ResourceNotFoundException;


import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerAccountService {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;


    public CustomerDto createCustomerAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        String mobileNumber = customerDto.getMobileNumber();
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(mobileNumber);
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already exists with given mobile number: " + mobileNumber);
        }
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
        return CustomerMapper.mapToCustomerDto(savedCustomer, new CustomerDto());
    }

    private Accounts createNewAccount(Customer savedCustomer) {
        Accounts account = new Accounts();
        account.setCustomerId(savedCustomer.getCustomerId());
        account.setAccountType(AccountConstants.SAVINGS);
        account.setBranchAddress(AccountConstants.DEFAULT_BRANCH_ADDRESS);
        return account;
    }


    public CustomerDto fetchCustomerAccountsByMobileNumber(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    public boolean updateCustomerAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto!=null){
            Accounts accounts = accountsRepository.findByCustomerId(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "customerId", accountsDto.getAccountNumber().toString())
            );
            Accounts updatedAccounts = AccountsMapper.mapToAccounts(accountsDto, accounts);
            accountsRepository.save(updatedAccounts);

            long customerId = updatedAccounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "customerId", Long.toString(customerId))
            );

            Customer updatedCustomer = CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(updatedCustomer);
            isUpdated = true;
        }
        return isUpdated;
    }

    public boolean deleteCustomerAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        customerRepository.delete(customer);
        return true;
    }
}
