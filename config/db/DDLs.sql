-- CRIA TABELA ACCOUNT
CREATE TABLE account (
    acc_number CHAR(10) NOT NULL,
    acc_name VARCHAR(100) NOT NULL,
    acc_email VARCHAR(100) NOT NULL,
    acc_password_hash VARCHAR(255) NOT NULL,
    acc_balance DECIMAL(15,2) DEFAULT 0.00,
    acc_type CHAR(1) NOT NULL,
    acc_document_number CHAR(14) NOT NULL
);

COMMENT ON COLUMN account.acc_type IS 'Type pode ser F de Pessoa Física ou J de Pessoa Jurídica.';
COMMENT ON COLUMN account.acc_document_number IS 'Document Number pode ser CPF ou CNPJ';

-- ADICIONA CONSTRAINTS À TABELA ACCOUNT
-- CONFIGURA A COLUNA acc_number COMO CHAVE PRIMÁRIA
ALTER TABLE account ADD CONSTRAINT pk_account PRIMARY KEY (acc_number);

-- ADICIONA CONSTRAINTS DE UNICIDADE
ALTER TABLE account ADD CONSTRAINT un_account_email UNIQUE (acc_email);
ALTER TABLE account ADD CONSTRAINT un_account_pwd_hash UNIQUE (acc_password_hash);
ALTER TABLE account ADD CONSTRAINT un_account_document_number UNIQUE (acc_document_number);

-- ADICIONA CONSTRAINT CHECKS
ALTER TABLE account ADD CONSTRAINT ck_account_type CHECK (LOWER(acc_type) IN ('f', 'j'));



-- CRIA TABELA ASSET
CREATE TABLE asset (
    asset_symbol VARCHAR(10) NOT NULL,
    asset_name VARCHAR(100) NOT NULL,
    asset_current_value DECIMAL(15,2) DEFAULT 0
);

-- ADICIONA CONSTRAINTS À TABELA ASSET
-- CONFIGURA A COLUNA asset_symbol COMO CHAVE PRIMÁRIA
ALTER TABLE asset ADD CONSTRAINT pk_asset PRIMARY KEY (asset_symbol);

-- ADICIONA CONSTRAINTS DE UNICIDADE
ALTER TABLE asset ADD CONSTRAINT un_asset_name UNIQUE (asset_name);



-- CRIA TABELA DE JUNÇÃO PARA RELACIONAMENTO MUITOS-PARA-MUITOS ENTRE ACCOUNT E ASSET
CREATE TABLE account_asset (
    account_number CHAR(10) NOT NULL,
    asset_symbol VARCHAR(10) NOT NULL,
    quantity DECIMAL(18,6) DEFAULT 0
);

-- ADICIONA CONSTRAINTS À TABELA ACCOUNT_ASSET
-- ADICIONA CHAVES ESTRANGEIRAS
ALTER TABLE account_asset ADD CONSTRAINT fk_account_asset_account_number FOREIGN KEY (account_number) REFERENCES account (acc_number) ON DELETE CASCADE;
ALTER TABLE account_asset ADD CONSTRAINT fk_account_asset_asset_symbol FOREIGN KEY (asset_symbol) REFERENCES asset (asset_symbol) ON DELETE SET NULL;


-- CRIA TABELA TRANSFER
CREATE TABLE transfer (
    id NUMBER(19, 0) NOT NULL,
    from_account CHAR(10) NOT NULL,
    to_account CHAR(10) NOT NULL,
    asset_symbol VARCHAR(10) NOT NULL,
    quantity DECIMAL(18,6) DEFAULT 0,
    transfer_value DECIMAL(15,2) DEFAULT 0,
    transfer_type VARCHAR(20)
);

COMMENT ON COLUMN transfer.quantity IS 'quantity SE REFERE À QUANTIDADE DE COTAS';
COMMENT ON COLUMN transfer.transfer_value IS 'transfer_value SE REFERE AO VALOR DA TRANSAÇÃO';
COMMENT ON COLUMN transfer.transfer_type IS 'transfer_type PODE SER UM SAQUE, UM DEPÓSITO, OU UMA TRANSFERÊNCIA ATIVA';

-- ADICIONA CONSTRAINTS À TABELA TRANSFER
-- CONFIGURA A COLUNA id COMO CHAVE PRIMÁRIA
ALTER TABLE transfer ADD CONSTRAINT pk_transfer PRIMARY KEY (id);

-- ADICIONA CHAVES ESTRANGEIRAS
ALTER TABLE transfer ADD CONSTRAINT fk_transfer_from_account FOREIGN KEY (from_account) REFERENCES account (acc_number) ON DELETE SET NULL;
ALTER TABLE transfer ADD CONSTRAINT fk_transfer_to_account FOREIGN KEY (to_account) REFERENCES account (acc_number) ON DELETE SET NULL;
ALTER TABLE transfer ADD CONSTRAINT fk_transfer_asset_symbol FOREIGN KEY (asset_symbol) REFERENCES asset (asset_symbol) ON DELETE SET NULL;

-- ADICIONA CONSTRAINT CHECKS
ALTER TABLE transfer ADD CONSTRAINT ck_transfer_type CHECK (LOWER(transfer_type) IN ('saque', 'deposito', 'transfer_ativo'));


-- CRIA TABELA OPERATION
CREATE TABLE operation (
    id NUMBER(19,0) NOT NULL,
    account_number CHAR(10) NOT NULL,
    asset_symbol VARCHAR(10) NOT NULL,
    operation_type CHAR(1) NOT NULL,
    quantity DECIMAL(18,6) DEFAULT 0,
    price DECIMAL(15,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN operation.operation_type IS 'operation_type PODE SER B (BUY) OU S (SELL)';

-- ADICIONA CONSTRAINTS À TABELA OPERATION
-- CONFIGURA A COLUNA id COMO CHAVE PRIMÁRIA
ALTER TABLE operation ADD CONSTRAINT pk_operation PRIMARY KEY (id);

-- ADICIONA CHAVES ESTRANGEIRAS
ALTER TABLE operation ADD CONSTRAINT fk_operation_account_number FOREIGN KEY (account_number) REFERENCES account (acc_number) ON DELETE CASCADE;
ALTER TABLE operation ADD CONSTRAINT fk_operation_asset_symbol FOREIGN KEY (asset_symbol) REFERENCES asset (asset_symbol) ON DELETE SET NULL;

-- ADICIONA CONSTRAINT CHECKS
ALTER TABLE operation ADD CONSTRAINT ck_operation_type CHECK (LOWER(operation_type) IN ('b', 's'));



-- REMOVE AS TABELAS ACIMA
DROP TABLE account CASCADE CONSTRAINTS;
DROP TABLE asset CASCADE CONSTRAINTS;
DROP TABLE account_asset;
DROP TABLE transfer;
DROP TABLE operation;
