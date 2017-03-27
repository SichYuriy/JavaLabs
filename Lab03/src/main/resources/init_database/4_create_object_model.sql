INSERT INTO entity_type(name)
VALUES ('User');

INSERT INTO entity_type(name)
VALUES ('Role');

INSERT INTO entity_type(name, parent_id)
SELECT 'Employee', entity_type_id
FROM entity_type
WHERE name='User';

INSERT INTO entity_type(name)
VALUES ('Project');

INSERT INTO entity_type(name)
VALUES ('Sprint');

INSERT INTO entity_type(name)
VALUES ('Task');

INSERT INTO entity_type(name)
VALUES ('TaskConfirmation');

INSERT INTO entity_type(name)
VALUES ('TimeRequest');


--ATTRIBUTES

-- employee
INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'position', attr_type_id, 0
FROM attr_type WHERE name='text';

-- user
INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'firstName', attr_type_id, 0
FROM attr_type WHERE name='text';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'lastName', attr_type_id, 0
FROM attr_type WHERE name='text';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'login', attr_type_id, 0
FROM attr_type WHERE name='text';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'password', attr_type_id, 0
FROM attr_type WHERE name='text';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'roles', attr_type_id, 1
FROM attr_type WHERE name='reference';

-- project
INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'customerId', attr_type_id, 0
FROM attr_type WHERE name='reference';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'name', attr_type_id, 0
FROM attr_type WHERE name='text';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'startDate', attr_type_id, 0
FROM attr_type WHERE name='date';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'endDate', attr_type_id, 0
FROM attr_type WHERE name='date';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'plannedStartDate', attr_type_id, 0
FROM attr_type WHERE name='date';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'plannedEndDate', attr_type_id, 0
FROM attr_type WHERE name='date';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'managerId', attr_type_id, 0
FROM attr_type WHERE name='reference';

-- sprint
INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'finished', attr_type_id, 0
FROM attr_type WHERE name='number';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'nextSprintId', attr_type_id, 0
FROM attr_type WHERE name='reference';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'previousSprintId', attr_type_id, 0
FROM attr_type WHERE name='reference';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'projectId', attr_type_id, 0
FROM attr_type WHERE name='reference';

--task
INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'parentTaskId', attr_type_id, 0
FROM attr_type WHERE name='reference';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'estimateTime', attr_type_id, 0
FROM attr_type WHERE name='number';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'executionTime', attr_type_id, 0
FROM attr_type WHERE name='number';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'sprintId', attr_type_id, 0
FROM attr_type WHERE name='reference';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'status', attr_type_id, 0
FROM attr_type WHERE name='text';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'requiredPosition', attr_type_id, 0
FROM attr_type WHERE name='text';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'dependencies', attr_type_id, 1
FROM attr_type WHERE name='reference';

--taskConfirmation

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'employeeId', attr_type_id, 0
FROM attr_type WHERE name='reference';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'taskId', attr_type_id, 0
FROM attr_type WHERE name='reference';

--timeRequest
INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'reason', attr_type_id, 0
FROM attr_type WHERE name='text';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'requestTime', attr_type_id, 0
FROM attr_type WHERE name='number';

INSERT INTO attribute(name, attr_type_id, ismultiple)
SELECT 'responseTime', attr_type_id, 0
FROM attr_type WHERE name='number';

-- attr_binds

-- user
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (1, 2); -- user -> firstName
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (1, 3); -- user -> lastName
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (1, 4); -- user -> login
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (1, 5); -- user -> password
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (1, 6); -- user -> roles
-- employee
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (3, 1); -- employee -> position
-- project
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (4, 7); -- project -> custumerId
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (4, 8); -- project -> name
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (4, 9); -- project -> startDate
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (4, 10); -- project -> endDate
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (4, 11); -- project -> plannedStartDate
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (4, 12); -- project -> plannedEndDate
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (4, 13); -- project -> managerId
-- role
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (2, 8); -- role -> name
-- sprint
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (5, 8); -- sprint -> name
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (5, 9); -- sprint -> startDate
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (5, 10); -- sprint -> endDate
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (5, 11); -- sprint -> plannedStartDate
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (5, 12); -- sprint -> plannedEndDate
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (5, 14); -- sprint -> finished
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (5, 15); -- sprint -> nextSprintId
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (5, 16); -- sprint -> previousSprintId
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (5, 17); -- sprint -> projectId
-- task
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (6, 8); -- task -> name
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (6, 18); -- task -> parentTaskId
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (6, 19); -- task -> estimateTime
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (6, 20); -- task -> executionTime
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (6, 21); -- task -> sprintId
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (6, 22); -- task -> status
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (6, 23); -- task -> requiredPosition
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (6, 24); -- task -> dependencies
-- taskConfirmation
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (7, 25); -- taskConfirmation -> employeeId
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (7, 26); -- taskConfirmation -> taskId
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (7, 22); -- taskConfirmation -> status
-- timeRequest
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (8, 26); -- timeRequest -> taskId
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (8, 27); -- timeRequest -> reason
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (8, 28); -- timeRequest -> requestTime
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (8, 29); -- timeRequest -> responseTime
INSERT INTO attr_binds(entity_type_id, attribute_id)
VALUES (8, 22); -- timeRequest -> status


-- DEFAULT ROLES
INSERT INTO entity(entity_type_id) VALUES(2);
INSERT INTO entity(entity_type_id) VALUES(2);
INSERT INTO entity(entity_type_id) VALUES(2);
INSERT INTO entity(entity_type_id) VALUES(2);

INSERT INTO value(entity_id, attribute_id, text_value) VALUES(1, 8, 'ADMINISTRATOR');
INSERT INTO value(entity_id, attribute_id, text_value) VALUES(2, 8, 'PROJECT_MANAGER');
INSERT INTO value(entity_id, attribute_id, text_value) VALUES(3, 8, 'EMPLOYEE');
INSERT INTO value(entity_id, attribute_id, text_value) VALUES(4, 8, 'CUSTOMER');

commit;