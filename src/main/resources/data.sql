
INSERT INTO `Order` (orderId,orderDate, orderStatus, customerId,total) VALUES (1,'2021-01-01', 'COMPLETED', 1,257285);
INSERT INTO `Order` (orderId,orderDate, orderStatus, customerId,total) VALUES (2,'2021-01-02', 'COMPLETED', 1,257285);
INSERT INTO `Order` (orderId,orderDate, orderStatus, customerId,total) VALUES (3,'2021-01-03', 'COMPLETED', 1,999999);
INSERT INTO `Order` (orderId,orderDate, orderStatus, customerId,total) VALUES (4,'2021-01-04', 'COMPLETED', 1,180000);
INSERT INTO `Order` (orderId,orderDate, orderStatus, customerId,total) VALUES (5,'2021-01-05', 'COMPLETED', 1,257285);
INSERT INTO `Order` (orderId,orderDate, orderStatus, customerId,total) VALUES (6,'2021-01-06', 'COMPLETED', 1,600000);
INSERT INTO `Order` (orderId,orderDate, orderStatus, customerId,total) VALUES (7,'2021-01-07', 'COMPLETED', 1,500000);
INSERT INTO `Order` (orderId,orderDate, orderStatus, customerId,total) VALUES (8,'2021-01-08', 'COMPLETED', 1,257285);

-- Insert data for OrderLine
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (1, 1, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (1, 2, 5);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (1, 3, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (1, 4, 2);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (1, 5, 6);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (1, 6, 7);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (1, 7, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (1, 8, 1);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (1, 9, 8);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (1, 10, 2);

INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (2, 1, 2);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (2, 3, 5);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (2, 4, 7);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (2, 6, 6);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (2, 8, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (2, 9, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (2, 10, 5);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (2, 11, 2);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (2, 12, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (2, 13, 1);

INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (3, 2, 6);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (3, 5, 7);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (3, 8, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (3, 9, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (3, 10, 2);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (3, 11, 5);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (3, 12, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (3, 13, 2);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (3, 14, 6);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (3, 15, 4);

INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (4, 1, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (4, 2, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (4, 5, 8);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (4, 6, 1);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (4, 7, 9);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (4, 8, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (4, 9, 6);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (4, 10, 5);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (4, 11, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (4, 12, 7);

INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (5, 1, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (5, 3, 2);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (5, 4, 8);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (5, 5, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (5, 6, 5);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (5, 7, 6);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (5, 8, 9);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (5, 9, 2);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (5, 10, 7);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (5, 11, 4);

INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (6, 2, 6);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (6, 3, 5);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (6, 6, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (6, 7, 8);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (6, 8, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (6, 9, 7);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (6, 10, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (6, 12, 6);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (6, 13, 9);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (6, 14, 5);

INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (7, 1, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (7, 2, 5);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (7, 3, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (7, 4, 2);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (7, 5, 6);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (7, 6, 7);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (7, 7, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (7, 8, 1);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (7, 9, 8);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (7, 10, 2);

INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (8, 1, 2);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (8, 3, 5);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (8, 4, 7);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (8, 6, 6);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (8, 8, 3);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (8, 9, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (8, 10, 5);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (8, 11, 2);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (8, 12, 4);
INSERT INTO `OrderLine` (orderId, productId, quantity) VALUES (8, 13, 1);

-- feedback

INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (5,5,'2021-01-01',1,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (4,5,'2021-01-01',2,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (3,5,'2021-01-01',3,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (2,5,'2021-01-01',4,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (1,5,'2021-01-01',5,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (5,5,'2021-01-01',6,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (4,5,'2021-01-01',7,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (3,5,'2021-01-01',8,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (2,5,'2021-01-01',9,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (1,5,'2021-01-01',10,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (5,5,'2021-01-01',11,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (4,5,'2021-01-01',12,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (3,5,'2021-01-01',13,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (2,5,'2021-01-01',14,'Good product');
INSERT INTO `ProductFeedback` (rating,customerId,feedbackDate,productId,comment) VALUES (1,5,'2021-01-01',15,'Good product');


