--------------------------------------------------
-- INSERT ACCOUNT
--------------------------------------------------
INSERT INTO account VALUES ('0000000001','João Silva','joao@email.com','hash123',1000,'f','12345678900');
INSERT INTO account VALUES ('0000000002','Empresa XP','empresa@email.com','hash456',5000,'j','12345678000100');

--------------------------------------------------
-- INSERT ASSET
--------------------------------------------------
INSERT INTO asset VALUES ('BTC','Bitcoin',100000);
INSERT INTO asset VALUES ('ETH','Ethereum',4000);

--------------------------------------------------
-- INSERT ACCOUNT_ASSET
--------------------------------------------------
INSERT INTO account_asset VALUES ('0000000001','BTC',0.5);
INSERT INTO account_asset VALUES ('0000000002','ETH',10);

--------------------------------------------------
-- INSERT OPERATION
--------------------------------------------------
INSERT INTO operation VALUES (1,'0000000001','BTC','b',0.5,100000,SYSTIMESTAMP);

--------------------------------------------------
-- INSERT TRANSFER
--------------------------------------------------
INSERT INTO transfer VALUES (1,'0000000001','0000000002','BTC',0.1,10000,'transfer_ativo');

--------------------------------------------------
-- UPDATE
--------------------------------------------------
UPDATE account SET acc_balance = 2000 WHERE acc_number = '0000000001';

--------------------------------------------------
-- DELETE
--------------------------------------------------
DELETE FROM account_asset WHERE account_number = '0000000001' AND asset_symbol='BTC';

--------------------------------------------------
-- SELECT
--------------------------------------------------
SELECT * FROM account;
SELECT * FROM asset;
SELECT * FROM account_asset;
SELECT * FROM operation;
SELECT * FROM transfer;