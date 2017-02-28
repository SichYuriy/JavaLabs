package com.gmail.at.sichyuriyy.netcracker.lab03.dao;

import com.gmail.at.sichyuriyy.netcracker.lab03.RelationUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestData;
import com.gmail.at.sichyuriyy.netcracker.lab03.TestUtils;
import com.gmail.at.sichyuriyy.netcracker.lab03.databaseconnector.DatabaseConnector;
import com.gmail.at.sichyuriyy.netcracker.lab03.entity.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Yuriy on 25.02.2017.
 */
public abstract class CustomerDaoTest {

    private DatabaseConnector databaseConnector;
    private CustomerDao customerDao;

    protected abstract DatabaseConnector getTestedDatabaseConnector();
    protected abstract void cleanResources();

    @Before
    public void setUp() {
        databaseConnector = getTestedDatabaseConnector();
        customerDao = databaseConnector.getCustomerDao();
    }

    @After
    public void tearDown() {
        cleanResources();
    }

    @Test
    public void create() {
        Customer customer = TestData.getCustomer();
        customerDao.create(customer);

        Customer dbCustomer = customerDao.findById(customer.getId());
        assertWeakEquals(customer, dbCustomer);
    }

    @Test
    public void delete() {
        Customer customer = TestData.getCustomer();
        customerDao.create(customer);
        assertNotNull(customerDao.findById(customer.getId()));

        customerDao.delete(customer.getId());
        assertNull(customerDao.findById(customer.getId()));
    }

    @Test
    public void findAll() {
        Customer customer1 = TestData.getCustomer("customer1");
        Customer customer2 = TestData.getCustomer("customer2");
        Customer customer3 = TestData.getCustomer("customer3");
        Employee employee = TestData.getEmployee();
        ProjectManager projectManager = TestData.getProjectManager();

        customerDao.create(customer1);
        customerDao.create(customer2);
        customerDao.create(customer3);
        databaseConnector.getEmployeeDao().create(employee);
        databaseConnector.getProjectManagerDao().create(projectManager);

        List<Customer> expectedList = new ArrayList<>();
        expectedList.add(customer1);
        expectedList.add(customer2);
        expectedList.add(customer3);

        List<Customer> actualList = customerDao.findAll();

        assertTrue(TestUtils.equalContentCollections(
                expectedList,
                actualList,
                this::weakEquals)
        );
    }

    @Test
    public void update() {
        Customer customer = TestData.getCustomer();
        customerDao.create(customer);
        customer.setLogin("updatedLogin");
        customer.setPassword("updatedPassword");
        customer.setFirstName("updatedFirstName");
        customer.setLastName("updatedLastName");
        customerDao.update(customer);

        Customer dbCustomer = customerDao.findById(customer.getId());
        assertWeakEquals(customer, dbCustomer);
    }

    @Test
    public void findByProjectId() {
        Customer customer = TestData.getCustomer();
        Project project = TestData.getProject(customer, null);
        customerDao.create(customer);
        databaseConnector.getProjectDao().create(project);

        RelationUtils.addOrderedProject(customer, project);

        Customer dbCustomer = customerDao.findByProjectId(project.getId());

        assertWeakEquals(customer, dbCustomer);
    }

    private void assertWeakEquals(Customer expected, Customer actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getLogin(), actual.getLogin());
        assertTrue(TestUtils.equalContentCollections(expected.getRoles(), actual.getRoles()));
        assertTrue(TestUtils.equalContentCollections(
                expected.getOrderedProjects(),
                actual.getOrderedProjects(),
                (p1, p2) -> p1.getId().equals(p2.getId()))
        );
    }

    private boolean weakEquals(Customer expected, Customer actual) {
        return expected.getId().equals(actual.getId())
                && expected.getFirstName().equals(actual.getFirstName())
                && expected.getLastName().equals(actual.getLastName())
                && expected.getLogin().equals(actual.getLogin())
                && expected.getPassword().equals(actual.getPassword())
                && TestUtils.equalContentCollections(expected.getRoles(), actual.getRoles())
                && TestUtils.equalContentCollections(
                        expected.getOrderedProjects(),
                        actual.getOrderedProjects(),
                        (p1, p2) -> p1.getId().equals(p2.getId())
        );
    }



}