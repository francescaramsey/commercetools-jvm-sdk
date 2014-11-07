package io.sphere.sdk.customers.commands;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerName;
import io.sphere.sdk.customers.commands.updateactions.*;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.test.IntegrationTest;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Predicate;

import java.time.LocalDate;

import static io.sphere.sdk.customers.CustomerFixtures.withCustomer;
import static io.sphere.sdk.customers.CustomerFixtures.withCustomerWithOneAddress;
import static org.fest.assertions.Assertions.assertThat;
import static io.sphere.sdk.test.OptionalAssert.assertThat;
import static io.sphere.sdk.test.SphereTestUtils.*;

public class CustomerUpdateCommandTest extends IntegrationTest {
    @Test
    public void changeName() throws Exception {
        withCustomer(client(), customer -> {
            final CustomerName newName = CustomerName.ofTitleFirstAndLastName("Mister", "John", "Smith");
            assertThat(customer.getName()).isNotEqualTo(newName);
            final Customer updatedCustomer = execute(new CustomerUpdateCommand(customer, ChangeName.of(newName)));
            assertThat(updatedCustomer.getName()).isEqualTo(newName);
        });
    }

    @Test
    public void changeEmail() throws Exception {
        withCustomer(client(), customer -> {
            final String newEmail = randomEmail(CustomerUpdateCommandTest.class);
            assertThat(customer.getEmail()).isNotEqualTo(newEmail);
            final Customer updatedCustomer = execute(new CustomerUpdateCommand(customer, ChangeEmail.of(newEmail)));
            assertThat(updatedCustomer.getEmail()).isEqualTo(newEmail);
        });
    }

    @Test
    public void addAddress() throws Exception {
        withCustomer(client(), customer -> {
            final String city = "addAddress";
            final Address newAddress = AddressBuilder.of(DE).city(city).build();
            final Predicate<Address> containsNewAddressPredicate = a -> a.getCity().equals(Optional.of(city));
            assertThat(customer.getAddresses().stream()
                    .anyMatch(containsNewAddressPredicate))
                    .overridingErrorMessage("address is not present, yet")
                    .isFalse();
            final Customer updatedCustomer = execute(new CustomerUpdateCommand(customer, AddAddress.of(newAddress)));
            assertThat(updatedCustomer.getAddresses().stream()
            .anyMatch(containsNewAddressPredicate)).isTrue();
        });
    }

    @Test
    public void changeAddress() throws Exception {
        withCustomerWithOneAddress(client(), customer -> {
            final String city = "new city";
            assertThat(customer.getAddresses()).hasSize(1);
            assertThat(customer.getAddresses().get(0).getCity()).isPresent().butNotAs(city);

            final Address oldAddress = customer.getAddresses().get(0);
            assertThat(oldAddress.getId())
                    .overridingErrorMessage("only fetched address contains an ID")
                    .isPresent();

            final Address newAddress = oldAddress.withCity(city);
            final ChangeAddress updateAction = ChangeAddress.ofOldAddressToNewAddress(oldAddress, newAddress);
            final Customer customerWithReplacedAddress = execute(new CustomerUpdateCommand(customer, updateAction));

            assertThat(customerWithReplacedAddress.getAddresses()).hasSize(1);
            assertThat(customerWithReplacedAddress.getAddresses().get(0).getCity()).isPresentAs(city);
        });
    }

    @Test
    public void removeAddress() throws Exception {
        withCustomerWithOneAddress(client(), customer -> {
            final Address oldAddress = customer.getAddresses().get(0);
            assertThat(oldAddress.getId())
                    .overridingErrorMessage("only fetched address contains an ID")
                    .isPresent();

            final RemoveAddress action = RemoveAddress.of(oldAddress);
            final Customer customerWithoutAddresses =
                    execute(new CustomerUpdateCommand(customer, action));

            assertThat(customerWithoutAddresses.getAddresses()).isEmpty();
        });
    }

    @Test
    public void setDefaultShippingAddress() throws Exception {
        withCustomerWithOneAddress(client(), customer -> {
            final Address address = customer.getAddresses().get(0);
            assertThat(address.getId())
                    .overridingErrorMessage("only fetched address contains an ID")
                    .isPresent();
            assertThat(customer.getDefaultShippingAddressId()).isAbsent();
            assertThat(customer.getDefaultShippingAddress()).isAbsent();

            final Customer updatedCustomer =
                    execute(new CustomerUpdateCommand(customer, SetDefaultShippingAddress.of(address)));

            assertThat(updatedCustomer.getDefaultShippingAddressId()).isEqualTo(address.getId());
            assertThat(updatedCustomer.getDefaultShippingAddress()).isPresentAs(address);
        });
    }

    @Test
    public void setDefaultBillingAddress() throws Exception {
        withCustomerWithOneAddress(client(), customer -> {
            final Address address = customer.getAddresses().get(0);
            assertThat(address.getId())
                    .overridingErrorMessage("only fetched address contains an ID")
                    .isPresent();
            assertThat(customer.getDefaultBillingAddressId()).isAbsent();
            assertThat(customer.getDefaultBillingAddress()).isAbsent();

            final Customer updatedCustomer =
                    execute(new CustomerUpdateCommand(customer, SetDefaultBillingAddress.of(address)));

            assertThat(updatedCustomer.getDefaultBillingAddressId()).isEqualTo(address.getId());
            assertThat(updatedCustomer.getDefaultBillingAddress()).isPresentAs(address);
        });
    }

    @Test
    public void setCustomerNumber() throws Exception {
        withCustomer(client(), customer -> {
            assertThat(customer.getCustomerNumber()).isAbsent();

            final String customerNumber = randomString();
            final Customer updatedCustomer =
                    execute(new CustomerUpdateCommand(customer, SetCustomerNumber.of(customerNumber)));

            assertThat(updatedCustomer.getCustomerNumber()).isPresentAs(customerNumber);
        });
    }

    @Test
    public void setExternalId() throws Exception {
        withCustomer(client(), customer -> {
            assertThat(customer.getExternalId()).isAbsent();

            final String externalId = randomString();
            final Customer updatedCustomer =
                    execute(new CustomerUpdateCommand(customer, SetExternalId.of(externalId)));

            assertThat(updatedCustomer.getExternalId()).isPresentAs(externalId);
        });
    }

    @Test
    public void setCompanyName() throws Exception {
        withCustomer(client(), customer -> {
            assertThat(customer.getCompanyName()).isAbsent();

            final String companyName = "Big coorp";
            final Customer updatedCustomer =
                    execute(new CustomerUpdateCommand(customer, SetCompanyName.of(companyName)));

            assertThat(updatedCustomer.getCompanyName()).isPresentAs(companyName);
        });
    }

    @Test
    public void setVatId() throws Exception {
        withCustomer(client(), customer -> {
            assertThat(customer.getVatId()).isAbsent();

            final String vatId = randomString();
            final Customer updatedCustomer =
                    execute(new CustomerUpdateCommand(customer, SetVatId.of(vatId)));

            assertThat(updatedCustomer.getVatId()).isPresentAs(vatId);
        });
    }

    @Test
    public void setDateOfBirth() throws Exception {
        withCustomer(client(), customer -> {
            assertThat(customer.getDateOfBirth()).isAbsent();

            final LocalDate dateOfBirth = LocalDate.now();
            final Customer updatedCustomer =
                    execute(new CustomerUpdateCommand(customer, SetDateOfBirth.of(dateOfBirth)));

            assertThat(updatedCustomer.getDateOfBirth()).isPresentAs(dateOfBirth);
        });
    }
}